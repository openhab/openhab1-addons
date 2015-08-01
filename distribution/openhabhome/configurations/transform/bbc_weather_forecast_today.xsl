<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
  <xsl:output method="text" omit-xml-declaration="yes" indent="no"      />
<xsl:template match="/">
  <xsl:apply-templates select="/rss/channel/item[1]/description" />
<xsl:value-of select="description" />
</xsl:template>
</xsl:stylesheet>



