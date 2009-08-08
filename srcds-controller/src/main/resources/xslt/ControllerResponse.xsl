<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:html="http://www.w3.org/1999/xhtml"
	xmlns="http://www.w3.org/1999/xhtml" exclude-result-prefixes="html">

	<xsl:output method="html"
		doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"
		doctype-public="-//W3C//DTD XHTML 1.1//EN" />

	<xsl:template match="/ControllerResponse">
		<html>
			<body>
				<h2>ControllerResponse</h2>
				Status: <xsl:value-of select="/ControllerResponse/ResponseCode" /><br/>
				<table border="1">
					<tr bgcolor="#9acd32">
						<th>Message</th>
					</tr>
					<xsl:for-each select="Message/Item">
						<tr>
							<td>
								<xsl:value-of select="." />
							</td>
						</tr>
					</xsl:for-each>
				</table>
			</body>
		</html>
	</xsl:template>

</xsl:stylesheet>