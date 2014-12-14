/**
 * JS for base functionality
 */

var FIREWALL_ALLOW_TEMPLATE_SRC = $("#firewall-rules-allow-template").html();
var FIREWALL_ALLOW_TEMPLATE = Handlebars.compile(FIREWALL_ALLOW_TEMPLATE_SRC);

var FIREWALL_DROP_TEMPLATE_SRC = $("#firewall-rules-drop-template").html();
var FIREWALL_DROP_TEMPLATE = Handlebars.compile(FIREWALL_DROP_TEMPLATE_SRC);

var FIREWALL_ENABLE_ALERT_SRC = $("#firewall-enable-alert-template").html();
var FIREWALL_ENABLE_ALERT_TEMPLATE = Handlebars.compile(FIREWALL_ENABLE_ALERT_SRC);

var FIREWALL_DISABLE_ALERT_SRC = $("#firewall-disable-alert-template").html();
var FIREWALL_DISABLE_ALERT_TEMPLATE = Handlebars.compile(FIREWALL_DISABLE_ALERT_SRC);

firewallStatus();

$.ajax({
	url: "/FloodlightFirewallDashboard/FloodlightFirewall"
}).done( function(data) {
	//console.log(data);
	for(var i = 0 ; i < data.length ; i++) {
		console.log(data[i].nw_proto);
		data[i].nw_proto = mapNetworkProtocol(data[i].nw_proto);
		console.log(data[i].nw_proto);
		if(data[i].action === "ALLOW") {
			$(".firewall-row:last-child").after(FIREWALL_ALLOW_TEMPLATE(data[i]));
		} else {
			$(".firewall-row:last-child").after(FIREWALL_DROP_TEMPLATE(data[i]));
		}
	}
});

function mapNetworkProtocol(numProto) {
	if(numProto === 6) {
		return "TCP";
	} else if(numProto === 17) {
		return "UDP";
	} else if(numProto === 1) {
		return "ICMP";
	} else {
		return numProto;
	}
};

function firewallStatus() {
	$.ajax({
		url: "/FloodlightFirewallDashboard/firewall/status"
	}).done( function(data) {
		console.log(data);
		var status = data.result.split(" ")[1];
		if (status === "enabled") {
			$(".firewall-alert").remove();
			$("#firewall-rules-label").before(FIREWALL_ENABLE_ALERT_TEMPLATE(data));
		} else if (status === "disabled") {
			$(".firewall-alert").remove();
			$("#firewall-rules-label").before(FIREWALL_DISABLE_ALERT_TEMPLATE(data));
		} else {
			
		}
	});
}