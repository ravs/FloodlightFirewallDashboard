package com.ravs.dashboard;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Servlet implementation class FirewallEnable.
 * Servlet to enable the Floodlight Firewall.
 * 
 * @author ravs
 */
@WebServlet({ "/FirewallEnable", "/firewall/enable" })
public class FirewallEnable extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
    private HttpGet httpGetRequest;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FirewallEnable() {
        super();
        httpGetRequest = new HttpGet(FloodlightConf.getInstance().getFloodlightFirewallEnableUrl());
    }

	/**
	 * Enables the firewall.
	 * Accepts request of type GET at /firewall/enable and makes a request of type GET to Floodlight Firewall API.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json");
		PrintWriter pWriter = response.getWriter();
		StringBuffer sb = new StringBuffer();
		//HttpClient httpClient = new DefaultHttpClient();
		CloseableHttpClient httpClient = HttpClients.createDefault();
	    try {
	      HttpResponse httpResponse = httpClient.execute(httpGetRequest);
	      if(httpResponse.getStatusLine().getStatusCode() != 200) {
	    	  pWriter.print("Http Request Failed with following error code : " 
	    			  + httpResponse.getStatusLine().getStatusCode());
	    	  pWriter.print(httpResponse.getStatusLine());
	    	  return;
	      }
	      System.out.println("----------------------------------------");
	      System.out.println(httpResponse.getStatusLine());
	      System.out.println("----------------------------------------");
	      HttpEntity entity = httpResponse.getEntity();
	      byte[] buffer = new byte[1024];
	      if (entity != null) {
	        InputStream inputStream = entity.getContent();
	        try {
	          int bytesRead = 0;
	          BufferedInputStream bis = new BufferedInputStream(inputStream);
	          while ((bytesRead = bis.read(buffer)) != -1) {
	            String chunk = new String(buffer, 0, bytesRead);
	            System.out.println(chunk);
	            sb.append(chunk);
	          }
	        } catch (Exception e) {
	          httpGetRequest.abort();
	          e.printStackTrace();
	        } finally {
	          try {
	            inputStream.close();
	          } catch (Exception ignore) {
	          }
	        }
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {	httpClient.close(); }
	    
	    pWriter.print(sb.toString());
	    return;
	}
}
