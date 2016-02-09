# Prerequisites #

  * Eclipse IDE (http://www.eclipse.org)
  * Subversion (e.g. http://subversion.tigris.org)
  * Apache Maven (http://maven.apache.org)

# Get the project into Eclipse #

  * Go to your Eclipse workspace directory and do the following: `svn checkout https://srcds-controller.googlecode.com/svn/trunk/ srcds-controller --username <your username>`
  * Go to the new created _srcds-controller_ directory and create the project files using: `mvn eclipse:eclipse`
  * After following this steps you should be able to use the project in Eclipse IDE