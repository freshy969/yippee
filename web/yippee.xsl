<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- Edited by XMLSpy® -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">

<html>
<body>
<h2>Yippee Search: 

        &quot;<xsl:value-of select="documentcollection/query"/>&quot;

</h2> <br />

<xsl:for-each select="documentcollection/document">
	<h3><a>
	<xsl:attribute name="href">
        	<xsl:value-of select="link"/>
	</xsl:attribute>
        
        <xsl:value-of select="title"/>
        </a></h3>     
        <xsl:value-of select="description"/><br />     
<br />
      </xsl:for-each>
  </body>
  </html>
</xsl:template>
</xsl:stylesheet>
