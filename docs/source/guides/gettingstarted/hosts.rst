#####
Hosts
#####

A Host configuration provides one or more Schedules with information pertaining
to the FTP server to connect to when scanning for files. They are separate to the
Schedule configuration to allow multiple Schedules to use the same Host configuration
without the need for having to input the same data multiple times.

Under **Settings -> New Host**, you will be prompted to enter all of the relevant
information.

    Name *[REQUIRED]*
        The friendly name for this Host. This is what will be visible when creating a
        schedule, so make it indicative of the Host you're making.

    Protocol
        Which type of connection to be made. This has no bearing on how you configure
        the host, but will direct davos to build the specific client when connecting.

    Host Address *[REQUIRED]*
        The IP address (or hostname) of the server.

    Port
        FTP and FTPS are usually on ``21``, while SFTP is usually on ``22``. If your
        server has been configured to run on a separate port, this is where you
        reference it.

    Username *[REQUIRED]*
        Name of the user to connect as.

    Password
        Password of the user to connect as.

    Use Identity File
        Only available when ``SFTP`` is selected. Choose this if the SFTP server
        requires an identity file to authenticate the user.

    Identity File
        Displayed when ``Use Identity File`` is checked, replacing the ``Password`` field. Enter
        the location of the file.

.. note:: The location of the identity file will be relative to the container's filesystem, so should ideally be under ``/config`` as this is the directory exposed by the Docker volume mapping.

It is also possible to create, manage, and delete a Host via the HTTP API. See :doc:`../../reference/api` for more details.
