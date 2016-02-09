A simple HTTP server which provides remote controlling abilities for Source Dedicated Servers (SRCDS).

---

This component is supposed to be deployed on a server machine which is running a SRCDS instance. It then provides a HTTP(S) GET interface which will in future support various authentication mechanisms to provide a secure way to control the SRCDS instance remotely.

The above described interface provides a rather simple [API](http://code.google.com/p/srcds-controller/wiki/HttpGetApi) to e.g. start, stop or restart the SRCDS instance or even modify its configuration settings. In a later development phase this project will include integration components for different client applications to make the usage of the controller even more simple.

This website is for developers and bug control. For installation instructions and downloads<br />
visit the project's homepage at http://www.earthquake-clan.de/srcds/!


Other interesting places:


User mailing list: http://groups.google.com/group/srcds-controller-users<br />
Developer mailing list: http://groups.google.com/group/srcds-controller-developers