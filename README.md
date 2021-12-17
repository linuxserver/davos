# davos

[![Build Status](http://ci.linuxserver.io/buildStatus/icon?job=Software/Davos/davos_10_Unit_Tests)](http://ci.linuxserver.io/job/Software/job/Davos/job/davos_10_Unit_Tests/) [![Documentation Status](https://readthedocs.org/projects/davos/badge/?version=latest)](http://davos.readthedocs.io/en/latest)

davos is an FTP download automation tool that allows you to scan various FTP servers for files you are interested in. This can be used to configure file feeds as part of a wider workflow.

# Why use davos?

A fair number of services still rely on "file-drops" to transport data from place to place. A common practice is to configure a cron job to periodically trigger FTP/SFTP programs to download those files. _davos_ is relatively similar, only it also adds a web UI to the whole process, making the management of these schedules easier.

# How it works

## Hosts

All periodic scans (Schedules) require a host to connect to. These can be added individually:

![https://raw.githubusercontent.com/linuxserver/davos/master/docs/host.png](https://raw.githubusercontent.com/linuxserver/davos/master/docs/host.png)

## Schedules

Each schedule contains all of the required information pertaining to the files it is interested in. This includes the host it needs to connect to, where to look for the files, where to download them, and how often:

![https://raw.githubusercontent.com/linuxserver/davos/master/docs/schedule1.png](https://raw.githubusercontent.com/linuxserver/davos/master/docs/schedule1.png)

It is also possible to limit what the schedule downloads by applying filters to each scan. _davos_ will only download files that match its list of given filters. If no filters are applied to a schedule, all files will be downloaded. Each schedule also keeps an internal record of what it scanned in the previous run, so it won't download the same file twice.

![https://raw.githubusercontent.com/linuxserver/davos/master/docs/schedule2.png](https://raw.githubusercontent.com/linuxserver/davos/master/docs/schedule2.png)

Once each file has been downloaded, _davos_ can also notify you via Pushbullet, as well as sending downstream requests to other services. This is particularly useful if another service makes use of the file _davos_ has just downloaded.

![https://raw.githubusercontent.com/linuxserver/davos/master/docs/schedule3.png](https://raw.githubusercontent.com/linuxserver/davos/master/docs/schedule3.png)

## Running

Finally, schedules can be started or stopped at any point, using the schedules listing UI:


![https://raw.githubusercontent.com/linuxserver/davos/master/docs/list.PNG](https://raw.githubusercontent.com/linuxserver/davos/master/docs/list.PNG)

# Changelog
- **2.2.2**
  - Updated log4j dependency to 2.16.0, accounting for CVE-2021-44228

- **2.2.1**
  - Fixed bug where lastRunTime got reset whenever a change was made to a schedule.
  - General refactoring of code, plus added unit tests.
  - Allow $filename resolution in URLs of API calls.

- **2.2.0**
  - The filter pattern matcher now resolves '*' to zero or more characters, rather than one or more.
  - The scanned items list can now be cleared.
  - Added a Last Run field to the scanned items modal.
  - Included [readthedocs](https://davos.readthedocs.io) documentation!
  - Added SNS capability to notifications area
  - Updated FTPS connections to run over Explicit TLS, rather than Implicit SSL
    - This may or may not break existing schedules that use FTPS prior to 2.2.0.
  - Improved some areas of DEBUG logging
  - Schedules page now automatically updates when files are downloading
  - Added identity file authentication for SFTP connections
  - Included a version checker to help prompt users when a new version is available
    - **Full disclosure**: This makes a GET request to GitHub to ascertain the latest release version.

- **2.1.2**
  - Fixed NaN bug caused by empty files (Div/0)
  - Fixed recursive delete issue for directories in FTP and SFTP connections.

- **2.1.1**
  - Fixed primitive issue on Schedule model for new fields

- **2.1.0**
  - Mandatory filtering allows schedules to only download files when at least one filter has been set.
  - Form validation on Hosts and Schedule pages
  - New theme
  - Inverse filtering allows schedules to download files that DO NOT match provided filters.
  - "Test Connection" button added to Hosts page
  - Schedules can now delete the remote copy of each file once the download has completed. This is separate to the Post-download actions.
  - New intervals: "Every minute" and "Every 5 minutes"
