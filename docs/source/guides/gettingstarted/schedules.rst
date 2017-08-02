#########
Schedules
#########

A Schedule is the configuration that tells davos when to run, where to connect, what
to look for, and what to do once it has finished downloading. Schedules are the heart
of davos and are powered by its workflow engine.

To create a new Schedule, go to **Settings -> New Schedule**. Schedules are split into
multiple sections, each with their own part to play in the process.

*******
General
*******

This defines the metadata and connection information of the Schedule. The **General** section
allows you to name the Schedule, as well as define how often it should run, and where files
should be managed.

    Name *[REQUIRED]*
        The name of the Schedule. This should be relevant to the task this schedule
        is performing. E.g. "Nightly Feed"

    Interval
        How often the schedule should run. The rate at which the schedule runs begins
        when the schedule is started for the first time. So, if it is started at 14:05,
        with an interval of "Every 30 minutes", it will run again at 14:35, then 15:05, and
        so on.

.. note:: If you change the interval for an already running Schedule, you'll need to restart it before the change takes effect.
..

    Host
        The Host configuration to use for this Schedule. It will default to the first
        Host in the list. You cannot create a Schedule if no Hosts have been created.

    Host Directory *[REQUIRED]*
        This is the directory on the host (relative to the connection entry point) that
        the Schedule should use for file scanning. Absolute paths are also compliant.

    Local Directory *[REQUIRED]*
        The directory where this schedule should place file downloads.

.. note :: The local directory must be relative to the container's filesystem, so should be under ``/download``.
..

    Transfer Type
        This setting will inform the Schedule whether or not it should only download
        matching files (``FILE``), or if it should also scan matching directories (``RECURSIVE``). This can be useful
        if the server contains sub-directories that may match in a scan, but should not be
        downloaded.

    Start Automatically
        If checked, the Schedule will automatically start when davos is started. Useful if
        you have a restart policy enabled in Docker and your machine requires a restart.

*********
Filtering
*********

This is a process that allows you to narrow down file scanning so only relevant
files are processed. Filters can be exceptionally useful for host directories that
are used by multiple processes or contain large numbers of files.

    Mandatory
        If checked, the Schedule will only consider scanning files if at least one filter has been
        defined. If checked and no filters are defined, nothing will be scanned, so nothing
        will be downloaded.

    Invert
        The default behaviour is to match all files on the host with the defined filters. Checking
        this option will invert that behaviour, so all files *not* matching the defined filters
        will be downloaded.

    Filters
        A list of strings that will be used to scan the host directory. Each file on the host is compared to
        this list - if it matches at least one filter, it will be downloaded. Filters can also be wildcarded
        using ``?`` (single character) and ``*`` (multiple characters).

        For example, for a file called "my_file_name.txt":

        .. code-block:: text

            my?file?name.txt = MATCH
            my*name.txt      = MATCH
            my_file.name.txt = NO MATCH
            *file_name*      = MATCH
            *file_name       = NO MATCH

***************
File Management
***************

davos also provides a way to tidy processed files upon completion. You can choose to
either delete the file remotely once downloaded (effectively making it a *move* operation),
and you can also move the file locally.

    Delete from Host
        If checked, all matched and downloaded files will be deleted from the Host. This
        logic will run after each individual download has completed.

.. warning :: If the FTP user does not have permission to delete files on the Host, this step will fail and the Schedule will cancel the current run. A future run of the Schedule will skip all files previously scanned.
..

    Move Downloaded File
        The location to move each successfully downloaded file. This will occur after each individual
        download has completed. A common use-case for this feature is to separate in-progress files with
        completed files (i.e. ``/download/doing`` and ``/download/done``).

.. note :: The "move to" directory must be relative to the container's filesystem, so should be under ``/download``. Advanced users may create additional volume mappings if need be.

.. note :: If davos is unable to move the file, it will remain in its originating directory, and will continue on to the Schedule's next step without failure.

******************
Downstream Actions
******************

One of the unique aspects of davos in respect to FTP management is its ability to create hooks in to other
applications that may be interested in the downloaded files. This may be useful when
the download action is part of a wider workflow that must be continued outside of the scope
of davos.

Actions defined against a Schedule will run for each individually downloaded file *after*
the File Management step previously mentioned has run.

There are two types of Downstream Action: *Notifications* and *API Calls*.

Notifications
=============

Notifications are useful if you'd like to know whenever davos has successfully downloaded
a file. Generally speaking, no further action is taken after a notification is sent,
but SNS may be configured to include a subscriber to a topic that performs a further action.

.. note:: There is no limit to the number of notifications you can have.

Pushbullet
----------

You will need an account with `Pushbullet <https://www.pushbullet.com/>`_ in order to use this feature.
In your Pushbullet account, create an Access Token.

    Access Token
        Your Pushbullet account's access token. This will be used to authenticate
        notification push requests to the Pushbullet API.

Amazon SNS
-------------------------

You will need an `Amazon AWS <https://aws.amazon.com/>`_ account to use this feature.

    Topic Arn
        The Amazon Resource Name for an SNS Topic created under your AWS account. This
        will be the topic that notifications are sent to.

    Region
        The region that the topic was created under. While regions are not mandatory for
        Topic Arns, this will be used to authenticate your account and create an SNS
        client in the correct region.

    Access Key
        The access key for an IAM User under your AWS account.

    Secret Access Key
        The second half of authentication with AWS. This is the secret key for the same
        IAM User.

.. warning:: Be careful with IAM User permissions! You should create a new IAM User with permissions only to publish messages to your notification topic, nothing more! See :doc:`../../faq/index` for more details on best practice regarding IAM Users.

API Calls
=========

API Calls are a great way to create hooks in to other applications via their own HTTP API.

    URL
        The URL of the API you wish to call

    Method
        Available options are *GET*, *POST*, *PUT* and *DELETE*

    Content-Type
        Informs the target API what type of body you're sending (if any), e.g. "application/json"

    Message Body
        The request payload being sent to the target API

.. note:: If you need to reference the downloaded file in an HTTP request, use **$filename**. This will resolve to the file or folder that was matched and subsequently downloaded.
