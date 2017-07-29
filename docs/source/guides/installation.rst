############
Installation
############

.. note :: davos has been written with `Docker <https://www.docker.com/>`_ at the forefront regarding installation and deployment. This means that you should consider using the pre-built Docker image that `LinuxServer have provided <https://github.com/linuxserver/docker-davos>`_ for this application.

***********
With Docker
***********

This is the recommended method of installation and deployment.

Install Docker
--------------

Firstly, you'll need to install `Docker <https://www.docker.com/>`_, a container engine that is used to
fire up user-space virtual containers. I recommend using `Docker's official guide <https://docs.docker.com/engine/installation/>`_ on installing the latest version of Docker CE on your machine,
as the steps differ depending on your platform.

Build the container
===================

Create a new container from LinuxServer's image.

.. code-block:: text

    docker create \
    --name=davos \
    -v <path to config>:/config \
    -v <path to downloads folder>:/download
    -e PGID=<gid> -e PUID=<uid> \
    -p 8080:8080 \
    linuxserver/davos

Params
------

* ``<path to config>``
    The folder on your machine where davos will place its configuration and log files.
    Typically this will be somewhere like ``/home/me/davos``, but it can be anywhere.
* ``<path to downloads folder>``
    The folder on your machine that davos can download files to. This is the volume mount
    point that davos is aware of for all file downloads.
* ``<uid>``
    The id of the user you'd like davos to run as. All files downloaded by davos will be owned by this user.
* ``<gid>``
    The id of the group you'd like to attribute to the user davos runs as. All files downloaded by davos will be owned by this group.

.. warning:: Docker will run all containers as ``root`` by default. Omitting ``PUID`` and ``PGID`` is not recommended.

Run the container
=================

Once the container has been created, you can run it.

.. code-block:: text

    docker start davos

After about 30 seconds, the application will be running and will be accessible on ``http://localhost:8080``. If you are running
davos on a remote server, substitute ``localhost`` with the server's IP address.

**************
Without Docker
**************

This is not the recommended method of installation and deployment, but has the potential for being the most configurable and flexible.
Davos does not have any prebuilt binaries, so you'll need to get the source and build it yourself (another reason to use Docker instead).

Get the source
--------------

.. code-block:: text

    wget https://github.com/linuxserver/davos/archive/LatestRelease.zip
    unzip LatestRelease.zip -d davos

Configure the application
-------------------------

By default, davos is configured to place all of its configuration in ``/config``, which may
not be preferable if you're running the application on bare metal. Firstly, reconfigure davos
to use your own defined directory for its database.

In ``conf/release/application.properties``, change ``spring.datasource.url``, e.g.:

.. code-block:: text

    spring.datasource.url=jdbc:h2:file:/home/me/davos

You'll also need to do the same in ``conf/release/log4j2.xml``, this time for the appender:

.. code-block:: xml

    <RollingFile name="File" fileName="/home/me/davos/logs/davos.log" filePattern="/config/logs/${date:yyyy-MM}/app-%d{yyyy-MM-dd-HH}-%i.log">

Build davos
-----------

.. note:: davos requires the `Java 8 SDK <http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html>`_ to build.

Once you've updated the configuration locations, you can build the binary.

.. code-block:: text

    ./gradlew build -Penv=release

This will create "davos-|release|.jar" in ``build/libs``. You should move this somewhere more fitting for an executable (``/var/lib``, for example).
It may also be worth renaming the .jar to "davos.jar", although this is not necessary.

Run davos
---------

.. note:: davos requires the `Java 8 JRE <http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html>`_ to build. This is not required if you already have the SDK installed.


To run the application, run the following command:

.. code-block:: text

    java -jar davos.jar
