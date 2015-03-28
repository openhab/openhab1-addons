<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
 <html><head></head><body>
<xsl:apply-templates select="/vbusSpecification/packet" />
 </body></html>
</xsl:template>


<xsl:template match="packet">
<xsl:variable name="packetsource" select="source" />
<xsl:variable name="packetdest" select="destination" />
<h1><xsl:value-of select="/vbusSpecification/device[child::address=$packetsource]/name" /> </h1>
<h2><xsl:text>Source: </xsl:text><xsl:value-of select="$packetsource" /></h2>
<h2><xsl:text>Destination: </xsl:text><xsl:value-of select="$packetdest" /> &#160; <xsl:value-of select="/vbusSpecification/device[child::address=$packetdest]/name" /></h2>
<h4><xsl:text>Command: </xsl:text><xsl:value-of select="command" /></h4>
<table border="1"> 
<tr>
<th>Name</th>
<th>offset</th>
<th>size</th>
<th>mask</th>
<th>factor</th>
<th>unit</th>
<th>bitPos</th>
<th>format</th>
<th>timeRef</th>


</tr>
<xsl:apply-templates select="field" />
</table>
</xsl:template>

<xsl:template match="field">
<tr>
<td>&#160;<xsl:value-of select="name" /> </td>
<td>&#160;<xsl:value-of select="offset" /> </td>
<td>&#160;<xsl:value-of select="size" /> </td>
<td>&#160;<xsl:value-of select="mask" /> </td>
<td>&#160;<xsl:value-of select="factor" /> </td>
<td>&#160;<xsl:value-of select="unit" /> </td>
<td>&#160;<xsl:value-of select="bitPos" /> </td>
<td>&#160;<xsl:value-of select="format" /> </td>
<td>&#160;<xsl:value-of select="timeRef" /> </td>
</tr>

</xsl:template>
</xsl:stylesheet>