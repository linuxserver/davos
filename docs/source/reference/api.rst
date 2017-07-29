###
API
###

davos provides an HTTP API that exposes Schedules and Hosts so they can be managed
outside the scope of the web application. This API is also used by the web application's
AJAX calls.

.. warning:: This API is completely unauthenticated, so anyone on your network can use this

*********
/schedule
*********


POST
----

Creates a single Schedule.

.. code-block:: text

    POST /api/v2/schedule HTTP 1.0
    Host: localhost
    Content-Type: application/json
    Accept: application/json

    {
        "name": String,
        "interval": Integer,
        "host": Integer,
        "hostDirectory": String,
        "localDirectory": String,
        "transferType": String [ FILE | RECURSIVE ],
        "automatic": Boolean,
        "moveFileTo": String,
        "filtersMandatory": Boolean,
        "invertFilters": Boolean,
        "deleteHostFile": Boolean,
        "filters": [
            {
                "value": String
            }
        ],
        "notifications": {
            "pushbullet": [
                {
                    "apiKey": String
                }
            ],
            "sns": [
                {
                    "topicArn": String,
                    "region": String,
                    "accessKey": String,
                    "secretAccessKey": String
                }
            ]
        },
        "apis": [
            {
                "url": String,
                "method": String [ POST | GET | PUT | DELETE ],
                "contentType": String,
                "body": String
            }
        ]
    }

Response
========

See: :ref:`Schedule Response Syntax <schedule-response>`.

**************
/schedule/{id}
**************

GET
---

Retrieves a single Schedule based on the supplied ``{id}``.

.. code-block:: text

    GET /api/v2/schedule/{id} HTTP 1.0
    Host: localhost
    Accept: application/json

Response
========

See: :ref:`Schedule Response Syntax <schedule-response>`.

PUT
---

Updates a single Schedule based on the given ``{id}``. All fields must be supplied, even if only a subset is
being updated. Use a GET to first obtain the most up-to-date payload before performing
a PUT.

.. code-block:: text

    PUT /api/v2/schedule/{id} HTTP 1.0
    Host: localhost
    Content-Type: application/json
    Accept: application/json

    {
        "name": String,
        "interval": Integer,
        "host": Integer,
        "hostDirectory": String,
        "localDirectory": String,
        "transferType": String [ FILE | RECURSIVE ],
        "automatic": Boolean,
        "moveFileTo": String,
        "filtersMandatory": Boolean,
        "invertFilters": Boolean,
        "deleteHostFile": Boolean,
        "filters": [
            {
                "id": Integer,
                "value": String
            }
        ],
        "notifications": {
            "pushbullet": [
                {
                    "id": Integer,
                    "apiKey": String
                }
            ],
            "sns": [
                {
                    "id": Integer,
                    "topicArn": String,
                    "region": String,
                    "accessKey": String,
                    "secretAccessKey": String
                }
            ]
        },
        "apis": [
            {
                "url": String,
                "method": String [ POST | GET | PUT | DELETE ],
                "contentType": String,
                "body": String
            }
        ]
    }

.. note:: If you are updating a listed object, you must provide the object's ``id``. If you do not, the API will remove the old reference and create a new one. To add a new item to the list, provide the new item (without an ``id``) alongside the existing one.

Response
========

See: :ref:`Schedule Response Syntax <schedule-response>`.

DELETE
------

Deletes a single Schedule with the given ``{id}``.

.. code-block:: text

    DELETE /api/v2/schedule/{id} HTTP 1.0
    Host: localhost
    Accept: application/json

Response
========

.. code-block:: javascript

    {
        "status":  String [ OK | Failed ],
        "body": String
    }

***************************
/schedule/{id}/scannedFiles
***************************

DELETE
------

Clears all items in the given Schedule's ``lastScannedFiles``.

.. code-block:: text

    DELETE /api/v2/schedule/{id}/scannedFiles HTTP 1.0
    Host: localhost
    Accept: application/json

Response
========

.. code-block:: javascript

    {
        "status":  String [ OK | Failed ],
        "body": String
    }

*****
/host
*****

POST
----

Creates a new Host.

.. code-block:: text

    POST /api/v2/host
    Host: localhost
    Content-Type: application/json
    Accept: application/json

    {
        "name": String,
        "address": String,
        "port": Integer,
        "protocol": String [ FTP | FTPS | SFTP ],
        "username": String,
        "password": String,
        "identityFile": String,
        "identityFileEnabled": Boolean
    }

.. note:: If ``identityFileEnabled`` is set to TRUE, you must also provide ``identityFile``, otherwise provide ``password``.

**********
/host/{id}
**********

GET
---

Retrieves a single Host based on the given ``{id}``.

.. code-block:: text

    GET /api/v2/host/{id}
    Host: localhost
    Accept: application/json

Response
========

See: :ref:`Host Response Syntax <host-response>`.

PUT
---

Updates a Host with the given ``{id}``.

.. code-block:: text

    POST /api/v2/host/{id}
    Host: localhost
    Content-Type: application/json
    Accept: application/json

    {
        "name": String,
        "address": String,
        "port": Integer,
        "protocol": String [ FTP | FTPS | SFTP ],
        "username": String,
        "password": String,
        "identityFile": String,
        "identityFileEnabled": Boolean
    }

.. note:: If ``identityFileEnabled`` is set to TRUE, you must also provide ``identityFile``, otherwise provide ``password``.

Response
========

See: :ref:`Host Response Syntax <host-response>`.

DELETE
------

Deletes a single Host with the given ``{id}``.

.. code-block:: text

    DELETE /api/v2/host/{id} HTTP 1.0
    Host: localhost
    Accept: application/json

Response
========

.. code-block:: javascript

    {
        "status":  String [ OK | Failure ],
        "body": String
    }

.. warning:: If the Host you are attempting to delete is being used by an active Schedule, the DELETE call will fail.

***************
/testConnection
***************

POST
----

Allows you to assert whether or not the provided payload contains valid Host information.

.. code-block:: text

    POST /api/v2/testConnection
    Host: localhost
    Content-Type: application/json

    {
        "id": Integer,
        "name": String,
        "address": String,
        "port": Integer,
        "protocol": String [ FTP | FTPS | SFTP ],
        "username": String,
        "password": String,
        "identityFile": String,
        "identityFileEnabled": Boolean
    }

Response
========

.. code-block:: javascript

    {
        "status":  String [ OK | Failed ],
        "body": String
    }

*********
Responses
*********

.. _schedule-response:

Schedule Response Syntax
------------------------

.. code-block:: javascript

    {
        "status": String [ OK ],
        "body": {
            "id": Integer,
            "name": String,
            "interval": Integer,
            "host": Integer,
            "hostDirectory": String,
            "localDirectory": String,
            "transferType": String [ FILE | RECURSIVE ],
            "automatic": Boolean,
            "moveFileTo": String,
            "running": Boolean,
            "filtersMandatory": Boolean,
            "invertFilters": Boolean,
            "lastRunTime": String,
            "deleteHostFile": Boolean,
            "lastScannedFiles": [
                String
            ],
            "filters": [
                {
                    "id": Integer,
                    "value": String
                }
            ],
            "notifications": {
                "pushbullet": [
                    {
                        "id": Integer,
                        "apiKey": String
                    }
                ],
                "sns": [
                    {
                        "topicArn": String,
                        "region": String,
                        "accessKey": String,
                        "secretAccessKey": String
                    }
                ]
            },
            "transfers": [
                {
                    "fileName": String,
                    "fileSize": Integer,
                    "directory": Boolean,
                    "progress": {
                        "percentageComplete": Double,
                        "transferSpeed": Double
                    },
                    "status": String [ DOWNLOADING | SKIPPED | PENDING | FINISHED ]
                }
            ],
            "apis": [
                {
                    "id": Integer,
                    "url": String,
                    "method": String [ POST | GET | PUT | DELETE ],
                    "contentType": String,
                    "body": String
                }
            ]
        }
    }

.. note:: ``running``, ``lastScannedFiles``, ``lastRunTime`` and ``transfers`` are immutable metadata fields and can't be used in PUT or POST requests. If supplied, they will be ignored.
..

    host
        References the ``id`` of the linked host.

    running
        Descibes whether or not the Schedule is running.

    lastRunTime
        The time recorded when the Schedule last *finished* running.

    lastScannedFiles
        A list of Strings that represent the files/folders found in the last run of the
        schedule.

    transfers
        A list of transfer objects that describe all files being actioned. This list
        will only be populated when the Schedule is running and is actively downloading.

.. _host-response:

Host Response Syntax
--------------------

Success
=======

.. code-block:: javascript

    {
        "status": String [ OK ],
        "body": {
            "id": Integer,
            "name": String,
            "address": String,
            "port": Integer,
            "protocol": String [ FTP | FTPS | SFTP ],
            "username": String,
            "password": String,
            "identityFile": String,
            "identityFileEnabled": Boolean
        }
    }

Failure
=======

.. code-block:: javascript

    {
        "status": String [ Failed ],
        "body": String
    }
