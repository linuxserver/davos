##############################
davos: FTP Download Automation
##############################

This is the documentation for `davos <https://github.com/linuxserver/davos>`_, a web-based tool for
automating and managing file downloads over FTP, FTPS and SFTP. Davos was born from the idea that
even today, FTP still has relevance in many different markets, but there weren't many web-based
solutions that provided an easy way to manage the movement of files (outside of a command line cron job)
from one place to another.

For those new to davos, look through the :doc:`guides/installation` and :doc:`guides/gettingstarted/index` guides.
They will run you though how to get and set up davos for the first time.

davos also provides a basic HTTP API that can be used to hook in to the application to manage things like
schedules, hosts, filters, and even to stop or start individual schedules.

.. toctree::
   :maxdepth: 2
   :caption: Contents

   guides/index
   reference/index
   faq/index
   developers/index
