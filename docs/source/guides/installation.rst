############
Installation
############

.. note :: davos has been written with `Docker <https://www.docker.com/>`_ at the forefront regarding installation and deployment. This means that you should consider using the pre-built Docker image that `LinuxServer have provided <https://github.com/linuxserver/docker-davos>`_ for this application.

Install Docker
**************

Firstly, you'll need to install `Docker <https://www.docker.com/>`_, a container engine that is used to
fire up user-space virtual containers. I recommend using `Docker's official guide <https://docs.docker.com/engine/installation/>`_ on installing the latest version of Docker CE on your machine,
as the steps differ depending on your platform.

Build the container
*******************

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
*****************

Once the container has been created, you can run it.

.. code-block:: text

    docker start davos

After about 30 seconds, the application will be running and will be accessible on ``http://localhost:8080``. If you are running
davos on a remote server, substitute ``localhost`` with the server's IP address.
