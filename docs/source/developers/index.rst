##########
Developers
##########

If you wish to contribute to davos (and help me tidy up some of its rather messy code!), you
will need to be able to build and run it locally. davos is written almost completely in
Java using the Spring Framework, utilising the Thymeleaf rendering engine. The project is
unit and integration tested using jUnit and Cucumber JVM, respectively.

*****
Setup
*****

Download and install the `Java 8 JDK <http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html>`_.
I'd also recommend using `Spring Tool Suite (STS) <https://spring.io/tools/sts/all>`_ as it is a prebuilt version of Eclipse IDE with all of the necessary
plugins installed for working with a Spring application.

********
Building
********

.. note:: You do not need to pre-install Gradle for this application as it comes with Gradle Wrapper, which does all the work for you.

To build the application, use Gradle:

.. code-block:: text

    ./gradlew clean build -Penv={release|local}

This will download all necessary dependencies, run tests, then package up the application.
The resulting .jar file will be in ``build/libs``. If you pass through a ``-Penv=release`` when
running this command, the packaged application will use the config under ``conf/release``, which
tells davos to use a file-based database. By default (i.e. if you do not pass this switch
through), it will use the ``conf/local`` configuration, which makes use of an in-memory database.

***************
Running the app
***************

It is recommended to build the app first before running, so you know your latest
changes are built:

.. code-block:: text

    ./gradlew clean build && java -jar build/libs/davos-2.2.0.jar

***********
Development
***********

Classpath
---------

When using Eclipse (or STS), a separate Gradle command is required in order to update
the project's classpath files so Eclipse is aware of the downloaded dependencies:

.. code-block:: text

    ./gradlew cleanEcipse eclipse

Code Structure
--------------

The code of davos is split in to four main sections:

    ``src/main/java``
        The core functional code. This contains all logic for the workflow, API,
        connectivity, and object persistence (database).

    ``src/main/resources``
        The front-end code, including all JavaScript, CSS, images, and Thymeleaf
        templates.

    ``src/test/java``
        All unit tests for the core code

    ``src/cucumber/java``
        Integration test code. This is separate to the main project code and
        does not get packaged in to the released application.

Running Tests
-------------

To run all unit tests, use Gradle:

.. code-block:: text

    ./gradlew test

To run all integration tests:

.. code-block:: text

    ./gradlew cucumber

Managing the version
--------------------

The version of the application is referenced in three files:

* ``version.txt`` in the project root directory
* ``conf/local/application.properties`` as a property called ``davos.version``
* ``conf/release/application.properties`` as a property called ``davos.version``

All three of these need to be updated if you are changing the version number.
