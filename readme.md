# Floolight Firewall Dashboard

Floodlight controller is an [OpenFlow](https://www.opennetworking.org/sdn-resources/onf-specifications/openflow) controller. Also, Floodlight is a collection of applications built using Floodlight Controller. Floodlight Firewall is an application which allows user to create, view and delete rules in the flow table. Firewall application exposes the CRUD functionality using the ReST APIs.

Floodlight Firewall Dashboard is a new dashboard developed by Ravi Shankar, this functionaliy is not there in the xisting dashboard of Floodlight. Firewall Dashboard is a JEE compliant web application which can be installed on any JEE complaint web server (although the development and testing were performed on [Apache Tomcat](http://tomcat.apache.org/)).

## Prerequisite 

* **Eclipse**
* **Apache Tomcat (or any JEE Web Server)**

## Install
* **On native system**
	1.	Copy the FloodlightFirewallDashboard.war file in the webapps directory of Apache Tomcat.
	2.	Floodlight by default runs on port 8080 and Apache Tomcat by default runs on 8080 as well. Please change the port on which Apache Tomcat will run. We can change the port in the server.xml of Apache Tomcat. In server.xml, change the connector property. Change it to available port which is not used by any other application, for example 8081.
	3.	Set the host and port in context.xml file in Apache Tomcat. Add environment properties for host and port. Append the following lines with in <Context> tag of the context.xml. Here host IP is the host on which Floodlight is running, for native this will be 127.0.0.1 and port number is the port on which Floodlight is running, by default this is 8080.
	*<Environment name="floodlightHost" value="<host IP>" type="java.lang.String" override="false"/>*
	*<Environment name="floodlightPort" value="8080" type="java.lang.Integer" override="false"/>*
	4.	Run tomcat using the startup.sh script present in Tomcat bin folder.
	5.	Got to http://localhost:8080/FloodlightFirewallDashboard/index.html

* **On remote system**
	1.	Skip step number 2 in the native system steps
	2.	In step number 3, set the host IP of Floodlight application as per the host on which Floodlight is running. No need to change the port, since by default port is 8080 for Floodlight.
	3.	Follow same steps to start Tomcat and use the same link to launch dashboard.
