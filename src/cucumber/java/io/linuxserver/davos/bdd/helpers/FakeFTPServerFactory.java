package io.linuxserver.davos.bdd.helpers;

import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;

public class FakeFTPServerFactory {

    private static FakeFtpServer server;

    public static int getPort() {
        return server.getServerControlPort();
    }

    public static FakeFtpServer setup() {

        server = new FakeFtpServer();
        server.addUserAccount(new UserAccount("user", "password", "/tmp"));
        server.setServerControlPort(0);

        FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new DirectoryEntry("/tmp"));
        fileSystem.add(new FileEntry("/tmp/file1.txt", "hello world"));
        fileSystem.add(new FileEntry("/tmp/file2.txt", "hello world"));
        fileSystem.add(new FileEntry("/tmp/file3.txt", "hello world"));

        server.setFileSystem(fileSystem);
        server.start();

        return server;
    }

    public static boolean checkFileExists(String filePath) {
        return server.getFileSystem().exists(filePath);
    }

    public static void addDirectoryWithNameAndNumberOfFiles(String name, int numberOfFiles) {

        server.getFileSystem().add(new DirectoryEntry("/tmp/" + name));

        int i;
        for (i = 0; i < numberOfFiles; i++)
            server.getFileSystem().add(new FileEntry("/tmp/" + name + "/file" + i));
    }

    public static void stop() {
        server.stop();
    }
}
