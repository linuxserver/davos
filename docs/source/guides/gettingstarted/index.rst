###############
Getting Started
###############

This section aims to help you understand how davos is pieced together, and shows
you how it can be configured to meet your needs. It is recommended that you follow
the below guides.

.. toctree::
   :maxdepth: 1

   hosts
   schedules

************
How it works
************

The Schedules in davos are powered by a basic workflow engine that runs a series
of steps to ensure each run processes files properly. The order of this
workflow is as follows:

1. Connect to the host.
2. List all files in the provided remote directory.
3. Filter all files in the remote directory so only the relevant ones remain.
4. Remove any files that have been previously scanned.
5. For each matched file, download it. Once downloaded, run any actions required by the schedule.
6. Store the list of scanned files against the Schedule.
7. Disconnect.

There is no theoretical limit to the number of schedules you can have running at
the same time, however it is advised you keep it below 10, as memory usage can
become quite high.
