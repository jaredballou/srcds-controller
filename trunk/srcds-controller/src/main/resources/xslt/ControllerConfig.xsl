<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:html="http://www.w3.org/1999/xhtml"
	xmlns="http://www.w3.org/1999/xhtml" exclude-result-prefixes="html">

	<xsl:output method="html"
		doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"
		doctype-public="-//W3C//DTD XHTML 1.1//EN" />

	<xsl:template match="/ControllerConfig">
		<html>
			<body>
				<h2>Controller Configuration</h2>
				<form method="get">
					<table border="1">
						<tr bgcolor="#9acd32">
							<th>Key</th>
							<th>Value</th>
						</tr>
						<xsl:for-each select="Entry">
							<tr>
								<td>
									<xsl:value-of select="Key" />
								</td>
								<td>
								<xsl:choose>
									<xsl:when test="Key = '${srcds-executable-key}'">
										<xsl:value-of select="Value" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:variable name="minFieldLength">20</xsl:variable>
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
					<input type="button" value="Cancel" onclick="javascript:history.back();" />
					<input type="submit" value="Save" />
				</form>
			</body>
		</html>
	</xsl:template>

</xsl:stylesheet>