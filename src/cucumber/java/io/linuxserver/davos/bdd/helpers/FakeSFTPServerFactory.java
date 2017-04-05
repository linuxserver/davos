package io.linuxserver.davos.bdd.helpers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import org.apache.commons.io.FileUtils;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.common.session.Session;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.scp.ScpCommandFactory;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;

public class FakeSFTPServerFactory {

    private static final String TMP = FileUtils.getTempDirectoryPath();
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";

    private static SshServer sshd;

    public static void setup() throws IOException {

        SftpSubsystemFactory factory = new SftpSubsystemFactory.Builder().build();

        sshd = SshServer.setUpDefaultServer();
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
        sshd.setShellFactory(new ProcessShellFactory(new String[] { "/bin/sh", "-i", "-l" }));
        sshd.setCommandFactory(new ScpCommandFactory());
        sshd.setSubsystemFactories(Collections.<NamedFactory<Command>> singletonList(factory));
        sshd.setPasswordAuthenticator(new PasswordAuthenticator() {

            @Override
            public boolean authenticate(String username, String password, ServerSession session)
                    throws PasswordChangeRequiredException {
                return USERNAME.equals(username) && PASSWORD.equals(password);
            }
        });
        
        sshd.setFileSystemFactory(new VirtualFileSystemFactory() {
            @Override
            protected Path computeRootDir(Session session) throws IOException  {
                return Paths.get(TMP);
            }
        });

        sshd.start();
    }

    public static void stop() throws IOException {
        sshd.stop();
    }

    public static void addDirectoryWithNameAndNumberOfFiles(String name, int numberOfFiles) throws IOException {

        File directory = new File(TMP + "/" + name);
        directory.mkdirs();

        int i;
        for (i = 0; i < numberOfFiles; i++)
            new File(TMP + "/" + name + "/file" + i).createNewFile();
    }

    public static int getPort() {
        return sshd.getPort();
    }
}
