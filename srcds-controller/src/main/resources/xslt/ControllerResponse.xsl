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
    <link rel="icon" href="${img:favicon.ico}" type="image/x-icon">
    </link>
    <style type="text/css">
@import url(${css:textmode.css}) braille, embossed, handheld, speech, tty;
 @import url(${css:screen.css}) screen, print, projection, tv;
</style>
    <script type="text/javascript">
        <!--
		function CheckRcon() 
		{
			if (getURLParam("command").length > 0)
			{
				document.getElementById('rconsubmit').style.display = 'block';		
			}
		}
		
		function getURLParam(strParamName)
		{
 		 	var strReturn = "";
  			var strHref = window.location.href;
  			if ( strHref.indexOf("?") > -1 )
			{
    			var strQueryString = strHref.substr(strHref.indexOf("?")).toLowerCase();
    			var aQueryString = strQueryString.split("&");
    			for ( var iParam = 0; iParam < aQueryString.length; iParam++ )
				{
      				if (aQueryString[iParam].indexOf(strParamName.toLowerCase() + "=") > -1 )
					{
        				var aParam = aQueryString[iParam].split("=");
        				strReturn = aParam[1];
        				break;
      				}
    			}
  			}
  			return unescape(strReturn);
		} 		
        // -->
        </script>
    </head>
    <body onload="CheckRcon()">
    <div id="header">Source Dedicated Server Controller @ ${hostname} - Response</div>
    <br/>
    <img src="${img:header_index.png}" /> <br/>
    <h2 class="response">Response from Controller</h2>
    <table border="0" class="response">
      <tr class="tableHeaders">
        <xsl:choose>
          <xsl:when test="ResponseCode = 'OK'">
            <th class="messageInfo">Information</th>
          </xsl:when>
          <xsl:otherwise>
            <th class="messageError">Error</th>
          </xsl:otherwise>
        </xsl:choose>
      </tr>
      <xsl:for-each select="Message/Item">
        <tr>
          <td class="tableData"><xsl:value-of select="." /> </td>
        </tr>
      </xsl:for-each>
    </table>
    <br />
    <br />
    <div name="rconsubmit" id="rconsubmit" style="display: block">
    <form action="rcon" method="get"><input name="command" type="text" size="40" class="rcon" /><input value="Send" type="submit" /></form>    
    </div>
    <a href="/" class="response">Back to main page</a>
    </body>
    </html>
  </xsl:template>
</xsl:stylesheet>
