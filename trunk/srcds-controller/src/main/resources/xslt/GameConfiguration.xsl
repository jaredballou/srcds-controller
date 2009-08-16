<?xml version="1.0" encoding="utf-8"?>
<!--

    This file is part of the Source Dedicated Server Controller project.

    This program is free software; you can redistribute it and/or modify it
    under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU General Public License for more details.
    
    You should have received a copy of the GNU General Public License
    along with this program; if not, see <http://www.gnu.org/licenses>.
    
    Additional permission under GNU GPL version 3 section 7
    
    If you modify this Program, or any covered work, by linking or
    combining it with srcds-controller (or a modified version of that library),
    containing parts covered by the terms of GNU General Public License,
    the licensors of this Program grant you additional permission to convey
    the resulting work. {Corresponding Source for a non-source form of such a
    combination shall include the source code for the parts of srcds-controller
    used as well as that of the covered work.}

    For more information, please consult:
       <http://www.earthquake-clan.de/srcds/>
       <http://code.google.com/p/srcds-controller/>

-->
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
						<span class="response">
							Configuration&#160;File:&#160;
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
						</span>
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