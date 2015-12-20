#!/usr/bin/perl -w

# Skript which sends SOAP request to TR064 enabled FritzBox to request online
# state. Useful for debugging replies from fbox.
#
# Jun. 2015, gitbock
#

use strict;
use warnings;
use LWP::Simple;
use LWP::UserAgent;
use XML::Simple;
use open qw(:std :utf8);

# ---------------- Konfig ----------------
# IP of FritzBox
my $ip = "192.168.178.1";

# preffered port is https 49443. If you experience issues you may also try
# http 49000. Dont forgert to change the url as well
my $port = "49443";

# specify the macs to check for online state
my @macs_to_check = ("11:22:33:44:55:66", "77:88:99:00:11:22");

# ----------------/Konfig ----------------



# disable SSL checks. No signed certificate!
$ENV{'PERL_LWP_SSL_VERIFY_HOSTNAME'} = 0;
$ENV{HTTPS_DEBUG} = 1;

# Discover Service Parameters
my $ua = new LWP::UserAgent;
$ua->default_headers;
$ua->ssl_opts( verify_hostname => 0 ,SSL_verify_mode => 0x00);

# Read all available services
my $resp_discover = $ua->get("https://$ip:$port/tr64desc.xml");
my $xml_discover;
if ( $resp_discover->is_success ) {
        $xml_discover = $resp_discover->decoded_content;
}
else {
        die $resp_discover->status_line;
}

my $discover = XMLin($xml_discover);
print "$discover->{device}->{modelName} detected...\n";

# Parse XML service response, get needed parameters for LAN host service
my $control_url = "not set";
my $service_type = "not set";
my $service_command = "GetSpecificHostEntry"; # fixed command for requesting info of specific MAC
foreach(@{$discover->{device}->{deviceList}->{device}->[0]->{serviceList}->{service}}){
        if("urn:LanDeviceHosts-com:serviceId:Hosts1" =~ m/.*$_->{serviceId}.*/){
                $control_url = $_->{controlURL};
                $service_type = $_->{serviceType};
                #print  "Control URL: $control_url\nService Type: $service_type\n";
        }
}

if ($control_url eq "not set" or $service_type eq "not set"){
        die "control URL/service type not found. Cannot request host info!";
}


# Prepare request for query LAN host
$ua->default_header( 'SOAPACTION' => "$service_type#$service_command" );
my $xml_mac_resp;

foreach my $mac (@macs_to_check){
        my $init_request = <<EOD;
        <?xml version="1.0" encoding="utf-8"?>
        <s:Envelope s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/" xmlns:s="http://schemas.xmlsoap.org/soap/envelope/" >
                <s:Header>
                </s:Header>
                <s:Body>
                        <u:$service_command xmlns:u="$service_type">
                                <NewMACAddress>$mac</NewMACAddress>
                        </u:$service_command>
                </s:Body>
        </s:Envelope>
EOD

        my $init_url = "https://$ip:$port$control_url";
        my $resp_init = $ua->post($init_url, Content_Type => 'text/xml; charset=utf-8', Content => $init_request);
        #print $resp_init->decoded_content;
        $xml_mac_resp = XMLin($resp_init->decoded_content);
        if(exists $xml_mac_resp->{'s:Body'}->{'s:Fault'}){
                if($xml_mac_resp->{'s:Body'}->{'s:Fault'}->{detail}->{UPnPError}->{errorCode} eq "714"){
                        print "Mac $mac not found in FritzBox Database!\n";
                }

        }
        if(exists $xml_mac_resp->{'s:Body'}->{'u:GetSpecificHostEntryResponse'}){
                if($xml_mac_resp->{'s:Body'}->{'u:GetSpecificHostEntryResponse'}->{NewActive} eq "1"){
                        my $name = $xml_mac_resp->{'s:Body'}->{'u:GetSpecificHostEntryResponse'}->{NewHostName};
                        my $ip = $xml_mac_resp->{'s:Body'}->{'u:GetSpecificHostEntryResponse'}->{NewIPAddress};
                        my $iftype =  $xml_mac_resp->{'s:Body'}->{'u:GetSpecificHostEntryResponse'}->{NewInterfaceType};
                        print "Mac $mac ($name) is online with IP $ip on $iftype\n";
                }
                if($xml_mac_resp->{'s:Body'}->{'u:GetSpecificHostEntryResponse'}->{NewActive} eq "0"){
            my $name = $xml_mac_resp->{'s:Body'}->{'u:GetSpecificHostEntryResponse'}->{NewHostName};
                        print "Mac $mac ($name) is offline\n";
        }


        }
}
