package com.ravs.dashboard;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Singleton utility class to get the Floodlight firewall ReST APIs. We need APIs to make the HTTP calls using Apache HttpComponents
 * 
 * @author ravs
 *
 */
public class FloodlightConf {

	private final String LOCALHOST = "127.0.0.1";
	private final Integer DEFUALT_PORT = 8080;
	private final String FLOODLIGHT_HOST_ENV_KEY = "floodlightHost";
	private final String FLOODLIGHT_PORT_ENV_KEY = "floodlightPort";
	
	private static FloodlightConf instance = null;
	private String host;
	private Integer port;
	
	private FloodlightConf() {
		Context initCtx;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			host = envCtx.lookup(FLOODLIGHT_HOST_ENV_KEY) != null ? 
					(String) envCtx.lookup(FLOODLIGHT_HOST_ENV_KEY) : LOCALHOST;
			System.out.println("Read the host from context env, HOST : " + host);
			port = envCtx.lookup(FLOODLIGHT_PORT_ENV_KEY) != null ? 
					(Integer) envCtx.lookup(FLOODLIGHT_PORT_ENV_KEY) : DEFUALT_PORT;
			System.out.println("Read the port number from context env, PORT : " + port);
		} catch (NamingException e) {
			e.printStackTrace();
			host = LOCALHOST;
			port = DEFUALT_PORT;
		}
	}
	
	public static FloodlightConf getInstance() {
		if(instance == null) {
			instance = new FloodlightConf();
		}
		return instance;
	}

	public String getFloodlightFirewallUrl() {
	    return "http://" + host + ":" + port + "/wm/firewall/rules/json";
	}
	
	public String getFloodlightFirewallStatusUrl() {
		return "http://" + host + ":" + port + "/wm/firewall/module/status/json";
	}
	
	public String getFloodlightFirewallEnableUrl() {
		return "http://" + host + ":" + port + "/wm/firewall/module/enable/json";
	}
	
	public String getFloodlightFirewallDisableUrl() {
		return "http://" + host + ":" + port + "/wm/firewall/module/disable/json";
	}
	
}
