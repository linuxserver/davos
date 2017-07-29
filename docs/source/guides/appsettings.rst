############
App Settings
############

Under **Settings -> App Settings**, you can configure the log level that davos
will output to its log file.

*******
Logging
*******

All logs are written to ``davos.log``, located in the ``/config/logs`` directory.
When mapping the ``/config`` directory in the container to a host directory, logs
will be made available in that host directory.

The log level can be changed at any time while the application is running. The available
levels are:

* DEBUG
* INFO
* WARN
* ERROR

The higher the level (``DEBUG`` is lowest, ``ERROR`` is highest), the fewer logs will be
written. By default, davos logs at ``INFO`` level. If you are experiencing issues
with davos and wish to understand the area of failure, change the level to ``DEBUG``.
Under this setting, the most logs will be written.

.. warning:: When setting the log level to ``DEBUG``, any secure credentials used in connections to the FTP host, or notification systems **will** be logged.
