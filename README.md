![http://linuxserver.io](http://www.linuxserver.io/wp-content/uploads/2015/06/linuxserver_medium.png)

davos - An FTP client for your server
===

*davos* is being designed from the ground up to provide a seamless and clean solution to managing FTP transfers to and from your Linux server. Unlike other applications, which generally only provide a web frontend allowing transfers from a given host to the client accessing the web, davos is designed to expose a UI from a server, giving the browser control over what's going on.

Why?
---
The use-case in mind is that home or small business server environments don't have an easy way to manage the file traffic they handle via FTP. Usually, one would have to SSH in and manually connect to an FTP server and download via the command line. Likewise, some people would install a desktop environment and install a client application and run that either through Xorg remotely or at the desk. *davos* will remove this requirement by allowing you to remotely access your server's filesystem via the web using a neat UI, while also allowing you to manage your file transfers in one place. Nowadays, FTP is being phased out for other protocols, however some people still prefer to use FTP, which is why we feel they shouldn't be left out of a good user experience. 

What does davos intend on doing?
---
There are two main features *davos* has in mind: simple manual file transfers, and automated scheduled transfers. The manual transfer screen will act just like any de-facto client application, in that you'll have a local and remote view when connected. The schedule screen will allow you to set up a connection that connects at intervals to a given host in order to periodically download files based on a given configurable filter list.

Will it be secure?
---
Yes, *davos* will run via HTTPS and will require user authentication whenever you wish to access the application. The current view is to use standard user/pass authentication which will need to be manually set up when *davos* is first run. Naturally, giving an application access to your server's filesystem is a bit daunting, which is why the application will be running under a predefined user, with particular access to a set directory purely for transfers. This will be fully detailed once design has been completed.
