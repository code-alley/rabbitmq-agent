package kr.co.inslab.web.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import kr.co.inslab.log.SLog;
import kr.co.inslab.web.data.ContextParams;
import kr.co.inslab.web.data.Message4Jenkins;




@Path("/v1")
public class RabbitMqAgentService {
	@Context ServletContext context;
	
	public static ContextParams contextParams = null;
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/jenkins/build")
	public String jekinsBuild(
			@QueryParam("repository") String repository,
			@QueryParam("hostname") String hostname,
			@QueryParam("hostaddress") String hostaddress,
			@QueryParam("ref") String ref,
			@QueryParam("issueid") int issueid,
			@QueryParam("cause") String cause) 
	{
		serviceInit();
	
		JSONObject result = new JSONObject();
		
		SLog.d("repository ",  repository);
		SLog.d("hostname ",  hostname);
		SLog.d("hostaddress ",  hostaddress);
		SLog.d("ref ",  ref);
		SLog.d("issueid ",  issueid);
		SLog.d("cause ",  cause);
		
			
		HashMap<String, Object> extraParams = new HashMap<>();
		extraParams.put("issueid", issueid);
		
		/**
		 *  gitblit -> jenkins 연결상태(flow) 확인
		 */
		JSONObject message;
		if((message = checkPublish(hostname, extraParams)) == null)
		{	
			return result.put("error", "not found jenkins domain").toString();
		}
		
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost(contextParams.getRabbitmqHost());
	    factory.setVirtualHost(contextParams.getRabbitmqVirtualHost());
	    factory.setUsername(contextParams.getRabbitmqUserId());
	    factory.setPassword(contextParams.getRabbitmqUserPasswd());
	    Connection connection;
	    
	    
	    
		try {
			connection = factory.newConnection();
			Channel channel = connection.createChannel();
			
		
			String token[] = ref.split("/"); 	// refs/heads/master
			String branch = token[token.length-1]; 
			String queueName = hostname + contextParams.getRabbitmqQueueSuffix();
			
			
			AMQP.BasicProperties.Builder builder =   
                     new AMQP.BasicProperties().builder();
			/**
			 * A message must have two properties.
			 * app_id, content_type
			 */
			builder.appId(contextParams.getRabbitmqJenkinsAppId());
			builder.contentType("application/json");
			
		    channel.queueDeclare(queueName, true, false, false, null);
		    channel.basicPublish("", queueName, builder.build(), message.toString().getBytes());
		    
		    /**
		     * result : publish infomation
		     */
		    result.put("queue", queueName);
		    result.put("app_id", contextParams.getRabbitmqJenkinsAppId());
		    result.put("content_type", "application/json");
		    result.put("message", message);
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
		return result.toString();
	}


	private void serviceInit() {
		if(contextParams == null)
		{
			SLog.project = "RabbitMqAgent";
			
			contextParams = new ContextParams(context);
		}
	}


	private JSONObject checkPublish(String subdomain, Map extraParams) {
		JSONObject message = null;
		
		HttpResponse<JsonNode> response = null;
		try {
			
			String query = String.format("select * from v_workflow_ci where domain = \'%s\';", subdomain);
			response = Unirest.post(contextParams.getDatabasepoolServiceHost())
					.basicAuth(contextParams.getDatabasepoolAuthUser(),
							contextParams.getDatabasepoolAuthPassword())
					.body(query)
					.asJson();
			
			JSONObject result = response.getBody().getObject();
			SLog.d(result.toString());
			
			if(result.has("error")){
				SLog.d("error", result.getString("error"));
			}
			else{
				Message4Jenkins mj = new Message4Jenkins(contextParams, result);
				message = mj.buildMessage(extraParams);
			}
			
			
			if(response.getStatus() == 201)
				SLog.d("response result", result.toString());
			else
				SLog.d("response status", response.getStatusText());
			

		} catch (UnirestException e) {
			e.printStackTrace();
		}
		
		
		return message;
	}
	
}
