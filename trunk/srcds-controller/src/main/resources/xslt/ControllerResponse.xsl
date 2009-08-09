<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:html="http://www.w3.org/1999/xhtml"
	xmlns="http://www.w3.org/1999/xhtml" exclude-result-prefixes="html">

	<xsl:output method="html"
		doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"
		doctype-public="-//W3C//DTD XHTML 1.1//EN" />

	<xsl:template match="/ControllerResponse">
		<html>
 			<head>
				<title>Source Dedicated Server Controller @ ${hostname}</title>
				<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/> 
				<style type="text/css">
				  @import url("${css:textmode.css}") braille, embossed, handheld, speech, tty;
				  @import url("${css:screen.css}") screen, print, projection, tv;
				</style>
			</head>
			<body>
                <div id="header">Source Dedicated Server Controller @ ${hostname} - Response</div>
                <br/>
                <img src="${img:header_index.png}" />
                <br/>
				<h2>ControllerResponse</h2>
				Status: <xsl:value-of select="/ControllerResponse/ResponseCode" /><br/>
				<table border="1">
					<tr class="tableHeaders">
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
                <br />
                <a href="/">Back to main page</a>
			</body>
		</html>
	</xsl:template>

</xsl:stylesheet>