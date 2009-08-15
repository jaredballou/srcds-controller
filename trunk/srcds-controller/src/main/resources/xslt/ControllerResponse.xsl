<?xml version="1.0" encoding="utf-8"?>
<!--

    This file is part of the Source Dedicated Server Controller project.
    It is distributed under GPL 3 license.

    The srcds-controller is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    You should have received a copy of the GNU General Public License
    along with the srcds-controller. If not, see <http://www.gnu.org/licenses/>.

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
	<xsl:template match="/ControllerResponse">
		<html>
			<head>
				<title>Source Dedicated Server Controller @ ${hostname}</title>
				<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
				<link rel="icon" href="${img:favicon.ico}" type="image/x-icon">
				</link>
				<style type="text/css">
					@import url(${css:textmode.css}) braille,
					embossed, handheld, speech, tty;
					@import url(${css:screen.css})
					screen, print, projection, tv;
				</style>
			</head>
			<body onload="CheckRcon()">
				<div id="header">Source Dedicated Server Controller @ ${hostname} -
					Response</div>
				<br />
				<img src="${img:header_index.png}" />
				<br />
				<h2 class="response">Response from Controller</h2>
				<table border="0" class="response">
					<tr class="tableHeaders">
						<xsl:choose>
							<xsl:when test="ResponseCode = 'INFORMATION'">
								<th class="messageInfo">Information</th>
							</xsl:when>
							<xsl:when test="ResponseCode = 'RCON_RESPONSE'">
								<th class="messageInfo">RCON Response</th>
							</xsl:when>
							<xsl:otherwise>
								<th class="messageError">Error</th>
							</xsl:otherwise>
						</xsl:choose>
					</tr>
					<xsl:for-each select="Message/Item">
						<tr>
							<td class="tableData">
								<xsl:value-of select="." />
							</td>
						</tr>
					</xsl:for-each>
				</table>
				<xsl:choose>
					<xsl:when test="ResponseCode = 'RCON_RESPONSE'">
						<br />
						<div name="rconsubmit" id="rconsubmit">
							<form action="rcon" method="get">
								<input name="command" type="text" size="40" />
								<input value="Send" type="submit" />
							</form>
						</div>
					</xsl:when>
				</xsl:choose>
				<br />
				<a href="/" class="response">Back to main page</a>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
