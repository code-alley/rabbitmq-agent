package kr.co.inslab.web.data;

import javax.servlet.ServletContext;

public class ContextParams {

	private final ServletContext context;
	
	private String rabbitmqHost;
	private String rabbitmqVirtualHost;
	private String rabbitmqUserId;
	private String rabbitmqUserPasswd;
	
	private String databasepoolServiceHost;
	private String databasepoolAuthUser;
	private String databasepoolAuthPassword;
	
	private String rabbitmqJenkinsToken;
	private String rabbitmqQueueSuffix;
	private String rabbitmqJenkinsAppId;
	

	public ContextParams(ServletContext context) {
		this.context = context;
		initParams();
	}
	private void initParams() {
		rabbitmqHost 				= context.getInitParameter("rabbitmq-host");
		rabbitmqVirtualHost 		= context.getInitParameter("rabbitmq-virtual-host");
		rabbitmqUserId 				= context.getInitParameter("rabbitmq-user-id");
		rabbitmqUserPasswd 			= context.getInitParameter("rabbitmq-user-passwd");
		
		databasepoolServiceHost		= context.getInitParameter("databasepool-service-host");
		databasepoolAuthUser		= context.getInitParameter("databasepool-auth-user");
		databasepoolAuthPassword	= context.getInitParameter("databasepool-auth-password");
		
		rabbitmqJenkinsToken		= context.getInitParameter("rabbitmq-jenkins-token");
		rabbitmqQueueSuffix			= context.getInitParameter("rabbitmq-jenkins-queue-suffix");
		rabbitmqJenkinsAppId 		= context.getInitParameter("rabbitmq-jenkins-app-id");
	}
	public String getRabbitmqHost() {
		return rabbitmqHost;
	}
	public void setRabbitmqHost(String rabbitmqHost) {
		this.rabbitmqHost = rabbitmqHost;
	}
	public String getRabbitmqVirtualHost() {
		return rabbitmqVirtualHost;
	}
	public void setRabbitmqVirtualHost(String rabbitmqVirtualHost) {
		this.rabbitmqVirtualHost = rabbitmqVirtualHost;
	}
	public String getRabbitmqUserId() {
		return rabbitmqUserId;
	}
	public void setRabbitmqUserId(String rabbitmqUserId) {
		this.rabbitmqUserId = rabbitmqUserId;
	}
	public String getRabbitmqUserPasswd() {
		return rabbitmqUserPasswd;
	}
	public void setRabbitmqUserPasswd(String rabbitmqUserPasswd) {
		this.rabbitmqUserPasswd = rabbitmqUserPasswd;
	}
	public String getDatabasepoolServiceHost() {
		return databasepoolServiceHost;
	}
	public void setDatabasepoolServiceHost(String databasepoolServiceHost) {
		this.databasepoolServiceHost = databasepoolServiceHost;
	}
	public String getDatabasepoolAuthUser() {
		return databasepoolAuthUser;
	}
	public void setDatabasepoolAuthUser(String databasepoolAuthUser) {
		this.databasepoolAuthUser = databasepoolAuthUser;
	}
	public String getDatabasepoolAuthPassword() {
		return databasepoolAuthPassword;
	}
	public void setDatabasepoolAuthPassword(String databasepoolAuthPassword) {
		this.databasepoolAuthPassword = databasepoolAuthPassword;
	}
	public String getRabbitmqJenkinsToken() {
		return rabbitmqJenkinsToken;
	}
	public void setRabbitmqJenkinsToken(String rabbitmqJenkinsToken) {
		this.rabbitmqJenkinsToken = rabbitmqJenkinsToken;
	}
	public String getRabbitmqQueueSuffix() {
		return rabbitmqQueueSuffix;
	}
	public void setRabbitmqQueueSuffix(String rabbitmqQueueSuffix) {
		this.rabbitmqQueueSuffix = rabbitmqQueueSuffix;
	}
	public String getRabbitmqJenkinsAppId() {
		return rabbitmqJenkinsAppId;
	}
	public void setRabbitmqJenkinsAppId(String rabbitmqJenkinsAppId) {
		this.rabbitmqJenkinsAppId = rabbitmqJenkinsAppId;
	}
}
