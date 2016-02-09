# Introduction #

In order to control a SRCDS (Source Dedicated Server) instance remotely one is able to trigger certain actions by invocation of the GET interface of the controller component.
The controller is most likely listening on the default TCP port 8888 - but can also be re-configured to use any possible port (depending on the user's permissions).
The following chapter gives a quick overview on all commands which are currently available.

# Available Commands #

  * **start**: Starts the SRCDS instance (e.g. http://127.0.0.1:8888/start)
  * **stop**: Stops the SRCDS instance (e.g. http://127.0.0.1:8888/stop)
  * **status**: Provides information on the current state of the SRCDS instance (e.g. http://127.0.0.1:8888/status)
  * **setConfig**: Sets a configuration _key_ to a specified _value_ (e.g. http://127.0.0.1:8888/setConfig?key=srcds.controller.srcds.gametype&value=LEFT4DEAD2)
  * **shutdown**: Shuts the controller down (e.g. http://127.0.0.1:8888/shutdown)

# Configuration #

The controller configuration is stored in a XML properties file called _srcds-controller-config.xml_.<br />
Following enumeration shows all available configuration keys:

  * **srcds.controller.networking.httpserver.port**
  * **srcds.controller.networking.httpserver.username**
  * **srcds.controller.networking.httpserver.password**
  * **srcds.controller.srcds.autostart**
  * **srcds.controller.srcds.path**
  * **srcds.controller.srcds.executable**
  * **srcds.controller.srcds.parameters**
  * **srcds.controller.srcds.gametype**
  * **srcds.controller.srcds.server.port**
  * **srcds.controller.srcds.rcon.password**