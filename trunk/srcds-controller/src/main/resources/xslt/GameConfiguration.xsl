<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:html="http://www.w3.org/1999/xhtml"
	xmlns="http://www.w3.org/1999/xhtml" exclude-result-prefixes="html">

	<xsl:output method="html"
		doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"
		doctype-public="-//W3C//DTD XHTML 1.1//EN" />

	<xsl:template match="/GameConfiguration">
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
				<h2>Game Configuration @ ${hostname}</h2>
				<form method="post">
					<select name="id" onchange="window.location.href='?id=' + this.selectedIndex">
						<xsl:for-each select="ConfigurationFiles/ConfigurationFile">
							<xsl:choose>
								<xsl:when test="@id = /GameConfiguration/FileContent/@id">
									<option value="{@id}" selected="selected"><xsl:value-of select="@name" />*</option>
								</xsl:when>
								<xsl:otherwise>
									<option value="{@id}"><xsl:value-of select="@name" /></option>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
					</select>
					<br/>
					<textarea name="content"><xsl:value-of select="/GameConfiguration/FileContent" /></textarea>
					<br />
					<input type="submit" value="Save" />
					<input type="button" value="Cancel" onclick="javascript:window.location.href='/'" />
				</form>
			</body>
		</html>
	</xsl:template>

</xsl:stylesheet>