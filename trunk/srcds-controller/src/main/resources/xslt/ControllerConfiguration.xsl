<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:html="http://www.w3.org/1999/xhtml"
	xmlns="http://www.w3.org/1999/xhtml" exclude-result-prefixes="html">

	<xsl:output method="html"
		doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"
		doctype-public="-//W3C//DTD XHTML 1.1//EN" />

	<xsl:template match="/ControllerConfiguration">
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
                <div id="header">Source Dedicated Server Controller @ ${hostname} - Edit Controller Configuration</div>
                <br/>
                <img src="${img:header_index.png}" />
                <br/>
				<h2 class="response">Edit Controller Configuration</h2>
				<form method="get">
					<table class="response" border="0">
						<tr class="tableHeaders">
							<th>Entry</th>
							<th>Value</th>
						</tr>
						<xsl:for-each select="Entry">
							<tr>
								<td class="tableData">
									<xsl:value-of select="@description" />
								</td>
								<td>
									<xsl:choose>
										<xsl:when test="Key = '${srcds-executable-key}'">
											<xsl:value-of select="Value" />
										</xsl:when>
										<xsl:when test="@type = 'Boolean'">
											<select name="{Key}">
												<xsl:choose>
													<xsl:when test="Value = 'true'">
														<option value="true" selected="selected">Enabled*</option>
														<option value="false">Disabled</option>
													</xsl:when>
													<xsl:otherwise>
														<option value="true">Enabled</option>
														<option value="false" selected="selected">Disabled*</option>
													</xsl:otherwise>
												</xsl:choose>
											</select>
										</xsl:when>
										<xsl:when test="@enumeration = 'true'">
											<xsl:variable name="Value" select="Value" />
											<xsl:variable name="EnumType" select="@type" />
											<select name="{Key}">
												<xsl:for-each select="/ControllerConfiguration/Metadata/Enumeration[@name=$EnumType]/Value">
													<xsl:choose>
														<xsl:when test="$Value = .">
															<option value="{.}" selected="selected"><xsl:value-of select="." />*</option>
														</xsl:when>
														<xsl:otherwise>
															<option value="{.}"><xsl:value-of select="." /></option>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:for-each>
											</select>
										</xsl:when>	
										<xsl:when test="@type = 'Password'">
											<xsl:variable name="minFieldLength">20</xsl:variable>
											<xsl:variable name="maxFieldLength">50</xsl:variable>
											<xsl:choose>
												<xsl:when test="string-length(Value) &lt; $minFieldLength">
													<input type="password" name="{Key}" value="{Value}" size="{$minFieldLength}" />
												</xsl:when>
												<xsl:when test="string-length(Value) &gt; $maxFieldLength - 1">
													<input type="password" name="{Key}" value="{Value}" size="{$maxFieldLength}" />
												</xsl:when>
												<xsl:otherwise>
													<input type="password" name="{Key}" value="{Value}" size="{string-length(Value) + 1}" />
												</xsl:otherwise>
											</xsl:choose>
										</xsl:when>
										<xsl:otherwise>
											<xsl:variable name="minFieldLength">30</xsl:variable>
											<xsl:variable name="maxFieldLength">80</xsl:variable>
											<xsl:choose>
												<xsl:when test="string-length(Value) &lt; $minFieldLength">
													<input type="text" name="{Key}" value="{Value}" size="{$minFieldLength}" />
												</xsl:when>
												<xsl:when test="string-length(Value) &gt; $maxFieldLength - 1">
													<input type="text" name="{Key}" value="{Value}" size="{$maxFieldLength}" />
												</xsl:when>
												<xsl:otherwise>
													<input type="text" name="{Key}" value="{Value}" size="{string-length(Value) + 1}" />
												</xsl:otherwise>
											</xsl:choose>
										</xsl:otherwise>
									</xsl:choose>
								</td>
							</tr>
						</xsl:for-each>
					</table>
					<br />
					<p class="response">
                    <input type="submit" value="Save" />
					<input type="button" value="Cancel" onclick="javascript:window.location.href='/'" />
					</p>
                </form>
			</body>
		</html>
	</xsl:template>

</xsl:stylesheet>