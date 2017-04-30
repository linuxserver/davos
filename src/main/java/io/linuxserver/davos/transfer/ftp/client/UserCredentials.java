package io.linuxserver.davos.transfer.ftp.client;

public class UserCredentials {

    public static final UserCredentials ANONYMOUS = new UserCredentials("anonymous", "stark@linuxserver.io");

    private String username;
    private String password;
    private Identity identity;

    public UserCredentials(final String username, final String password) {

        this.username = username;
        this.password = password;
    }

    public UserCredentials(final String username, final Identity identity) {
        
        this.username = username;
        this.identity = identity;
    }
    
    public Identity getIdentity() {
        return identity;
    }
    
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public static class Identity {
        
        private final String identityFile;
        
        public Identity(String identityFile) {
            this.identityFile = identityFile;
        }
        
        public String getIdentityFile() {
            return identityFile;
        }
    }
}
