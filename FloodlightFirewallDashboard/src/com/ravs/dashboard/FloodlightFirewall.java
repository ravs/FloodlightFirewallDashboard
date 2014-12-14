package com.ravs.dashboard;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.ravs.httputil.HttpDeleteWithBody;

/**
 * Servlet implementation class FloodlightFirewall. We use GET, POST and DELETE methods to view, create and delete rules in the firewall flow table.
 * 
 * @author ravs
 */
@WebServlet("/FloodlightFirewall")
public class FloodlightFirewall extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    private static final String STRING_EQUAL = "=";
    
    private HttpGet httpGetRequest;
    private HttpPost httpPostRequest;
    private HttpDeleteWithBody httpDeleteRequest;
    
    public FloodlightFirewall() {
        super();
        String floodlightUrl = FloodlightConf.getInstance().getFloodlightFirewallUrl();
        httpGetRequest = new HttpGet(floodlightUrl);
        httpPostRequest = new HttpPost(floodlightUrl);
        httpDeleteRequest = new HttpDeleteWithBody(floodlightUrl);
    }

	/**
	 * Retrieves the list of rules in the flow table.
	 * Accepts request of type GET at /FloodlightFirewall and makes a request of type GET to Floodlight Firewall API.
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

	/**
	 * Creates new rule in the flow table.
	 * Accepts request of type POST at /FloodlightFirewall and makes a request of type POST to Floodlight Firewall API.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuffer data = new StringBuffer();
		data.append("{");
		Enumeration<String> paramNames = request.getParameterNames();
	    while(paramNames.hasMoreElements()) {
	      String paramName = (String)paramNames.nextElement();
	      System.out.println(paramName);
	      if(paramName.equalsIgnoreCase("srcip")) {
	    	  data.append("\"src-ip\":\"");
	      } else if (paramName.equalsIgnoreCase("dstip")) {
	    	  data.append("\"dst-ip\":\"");
	      } else {
	    	  data.append("\"" + paramName + "\":\"");
	      }
	      String[] paramValues = request.getParameterValues(paramName);
	      if (paramValues.length == 1) {
	        String paramValue = paramValues[0];
	        if (paramValue.length() == 0)  {
	        	data.append(paramValue);
	        } else {
	        	data.append(paramValue);
	        }
	      } else {
	    	  System.out.println("<UL>");
	        for(int i=0; i<paramValues.length; i++) {
	        	System.out.println("<LI>" + paramValues[i]);
	        }
	        System.out.println("</UL>");
	      }
	      data.append("\"");
	      if(paramNames.hasMoreElements()) {
	    	  data.append(", ");
	      }
	    }
	    data.append("}");
	    System.out.println(data.toString());
	    //create String entity to store the json data, which will be sent through POST
	    StringEntity requestEntity = new StringEntity(data.toString(), "application/json", "UTF-8");
	    httpPostRequest.setEntity(requestEntity);
	    CloseableHttpClient httpClient = HttpClients.createDefault();
	    CloseableHttpResponse response2 = httpClient.execute(httpPostRequest);
	    System.out.println(response2.getStatusLine());
	    response.sendRedirect(response.encodeRedirectURL("http://localhost:8080/FloodlightFirewallDashboard/index.html"));
	}
	
	
	/**
	 * Deletes the rule from flow table
	 * Accepts request of type DELETE at /FloodlightFirewall and makes a request of type DELETE to Floodlight Firewall API.
	 * 
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		StringEntity requestEntity = new StringEntity(formatQueryString(request.getQueryString()), "application/json", "UTF-8");
		httpDeleteRequest.setEntity(requestEntity);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response2 = httpClient.execute(httpDeleteRequest);
		System.out.println(response2.getStatusLine());
		PrintWriter pw = response.getWriter();
		if(response2.getStatusLine().getStatusCode() == 200) {
			pw.print("SUCCESS");
		} else {
			pw.print("FAILED");
		}
	}
	
	private String formatQueryString(String queryString) {
		String[] queryParameters = queryString.split(STRING_EQUAL);
		if (queryParameters.length == 2) {
			return "{\"" + queryParameters[0] + "\":\"" + queryParameters[1] + "\"}";
		} else {
			return null;
		}
	}

}
