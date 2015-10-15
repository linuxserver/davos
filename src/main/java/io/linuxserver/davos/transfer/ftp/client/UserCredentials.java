package io.linuxserver.davos.transfer.ftp.client;

public class UserCredentials {

    public static final UserCredentials ANONYMOUS = new UserCredentials("anonymous", "stark@linuxserver.io");

    private String username;
    private String password;

    public UserCredentials(final String username, final String password) {

        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
