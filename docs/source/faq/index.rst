###
FAQ
###

**********************************
Can davos be used to upload files?
**********************************

No, davos only downloads files. There are currently no plans on implementing the ability
to upload files as this will require a rework of the schedule workflow engine.

******************************
How many schedules can I have?
******************************

There is no theoretical limit to the number of schedules you can have. davos creates
an initial thread pool of 10 worker threads, but this gets extended if more than 10
schedules are created.

**************************
How many hosts can I have?
**************************

Unlimited.

********************************************
Are host credentials hashed in the database?
********************************************

No, all host usernames and passwords are stored in plain text in the H2 database. This
is because the application needs to query the hosts table every time a schedule runs,
and would have no way to compare a hash with a valid password.

***************************************************
How do I use an identity file for SFTP connections?
***************************************************

On the Host configuration page for your Host, make sure **Use Identity File** is checked. Then
enter the absolute path of the identity file. If you're running davos in a Docker container (recommended),
the value of this should be some thing like "/config/id_rsa", assuming you are using an SSH private key called
"id_rsa" and have placed it in your mapped host directory on your machine.

Any form of private identity is applicable, for example if your host server uses .pem files
for authentication, use "/config/my_identity.pem".

.. note:: Remember, davos can't see files outside of its ``/download`` and ``/config`` directories when running in a Docker container. So remember to place your identity file(s) in the mapped directory on the host (e.g. ``/home/user/davos``).

****************************************
How can I use SNS to notify me by email?
****************************************

To use SNS, you'll need an Amazon AWS Account. Once set up, you should go to **Services -> Simple Notification Service**,
then **Create topic**. For Topic name, enter something like "davos-notifications", and click **Create topic**. The first
thing you'll notice is that it has generated a **Topic ARN**. You'll need this for the notification configuration later.

Now create a subscription to your topic by clicking on **Create subscription**, chosing "Email" as the Protocol, and your
preferred email address as the Endpoint. Click **Create subscription**. You'll receive an email asking you to confirm
the subscription request.

Once your topic has been configured, you should create an IAM User that can publish messages to it. It is this user's
credentials that davos needs to perform the publish.

Go to **Services -> IAM**, then **Users**. Click **Add user**. For User name, enter something sensible, then select "Programmatic access"
as the Access Type. Click **Next: Permissions**. This user should only have permission to publish to this topic,
nothing more. So, under "Add user to group", click **Create group**, and then **Create policy**.

.. note:: A user can be in many groups. Groups can have many policies. A policy is a set of permissions for access to various things in AWS.

You should be directed to the policy creation tool. Select the Policy Generator and set the following:

    Effect
        Allow

    AWS Service
        Amazon SNS

    Actions
        Publish

    Amazon Resource Name (ARN)
        {YOUR_TOPIC_ARN}

Then click **Add Statement**. You should see it added underneath. Click **Next Step**. The generated policy will be shown
to you on screen (it's formatted as JSON, and contains a ``Statement`` array). Update the Policy Name to something
sensible (e.g. "DavosTopicPublishAccess") then click **Create Policy**. You'll be redirected back to the IAM
console, but you can close this.

Go back to the previous tab and under the Filter, type in the name of the policy you just created. Select it. Now, for the
Group name, give it a sensible name (e.g. DavosNotifications), and click **Create group**. The group should now be selected under
the IAM user console. Click **Next: Review**, make sure you're happy, and then click **Create user**.

You should see a table showing the user's Access key ID and Secret access key. You'll need these for the SNS configuration
in davos, so keep them safe somewhere (you can download a .csv with the credentials in).

.. warning:: The Secret access key will only be shown once in the console, so make sure you store it somewhere safe.
