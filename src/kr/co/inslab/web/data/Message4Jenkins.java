package kr.co.inslab.web.data;

import java.util.Iterator;
import java.util.Map;

import kr.co.inslab.log.SLog;

import org.json.JSONArray;
import org.json.JSONObject;

public class Message4Jenkins {
	
	private ContextParams 	contextParams;
	private JSONObject		queryResult;
	
	public Message4Jenkins(ContextParams contextParams, JSONObject queryResult) {
		this.contextParams 	= contextParams;
		this.queryResult	= queryResult;
	}

	/**
	 * Jenkin trigger plugin : message structure
	 {
	  "project": "RPROJECTNAME",
	  "token": "TOKEN",
	  "parameter": [
	    {
	      "name": "PARAMETERNAME",
	      "value": "VALUE"
	    },
	    {
	      "name": "PARAMETERNAME2",
	      "value": "VALUE2"
	    }
	  ]
	}
	 */
	
	public JSONObject buildMessage(Map extraParams) {
		JSONObject message = null;

		JSONArray resultArray = queryResult.getJSONArray("result");
		
		String project = null;
		
		JSONArray parameters = new JSONArray();
		
		if(resultArray.length() > 0){
			JSONObject item = new JSONObject();
			item = (JSONObject) resultArray.get(0);
			
			Iterator<String> keys = item.keys();
			while(keys.hasNext()){
				
				String key 		= keys.next();
				Object value 	= item.get(key);
				
				JSONObject paramItem = new JSONObject();
				paramItem.put("name", key);
				paramItem.put("value", value);
				
				parameters.put(paramItem);
				
				if(key.equalsIgnoreCase("job_name"))
					project = (String) value;
			}
			
			
			/**
			 * 사용자정의 추가 파라미터
			 */
			if(extraParams != null){
				Iterator<String> paramKeys = extraParams.keySet().iterator();
				while(paramKeys.hasNext()){
					String key = paramKeys.next();
					Object value 	= extraParams.get(key);
					
					JSONObject paramItem = new JSONObject();
					paramItem.put("name", key);
					paramItem.put("value", value);
					
					parameters.put(paramItem);
					
				}
			}
			
			message = new JSONObject();
			message.put("project", project);
			//message.put("project", "rabbitmq-jenkins");	// test project : http://catool-jenkins.cloudapp.net:8080/
			message.put("token", contextParams.getRabbitmqJenkinsToken());
			message.put("parameter", parameters);
			
			
			SLog.d("Jenkin trigger plugin message", message.toString());
			
		}
		else
			return null;
		
		return message;
	}

	
	
}
