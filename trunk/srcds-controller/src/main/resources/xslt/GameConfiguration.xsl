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
				<link rel="icon" href="${img:favicon.ico}" type="image/x-icon"></link>
        		<style type="text/css">
				  @import url(${css:textmode.css}) braille, embossed, handheld, speech, tty;
				  @import url(${css:screen.css}) screen, print, projection, tv;
				</style>
			</head>		
			<body>
                <div id="header">Source Dedicated Server Controller @ ${hostname} - Edit Game Configuration</div>
                <br/>
                <img src="${img:header_index.png}" />
                <br/>
   				<h2 class="response">Edit Game Configuration</h2>
				<form method="post">
						Configuration&#160;File:&#160;
						<select name="id" onchange="window.location.href='?id=' + this.selectedIndex" class="response">
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
						<br/><br/>
						<textarea name="content" class="response"><xsl:value-of select="/GameConfiguration/FileContent" /></textarea>
						<br />
						<br />
						<input type="submit" value="Save" class="response"/>
						<input type="button" value="Cancel" onclick="javascript:window.location.href='/'" />
				</form>
			</body>
		</html>
	</xsl:template>

</xsl:stylesheet>