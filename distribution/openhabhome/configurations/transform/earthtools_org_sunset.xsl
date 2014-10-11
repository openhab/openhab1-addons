<?xml version="1.0"?>
<xsl:stylesheet 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:java="http://xml.apache.org/xslt/java"	
	exclude-result-prefixes="java"
	version="1.0">

	<xsl:output indent="yes" method="xml" encoding="UTF-8" omit-xml-declaration="yes" />
	
	<xsl:template match="/">
	<xsl:value-of select="java:format(java:java.text.SimpleDateFormat.new('yyyy-MM-dd'), java:java.util.Date.new())" />T<xsl:value-of select="//sun/evening/twilight/civil" />
	</xsl:template>

</xsl:stylesheet>
