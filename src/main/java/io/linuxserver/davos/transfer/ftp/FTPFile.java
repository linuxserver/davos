package io.linuxserver.davos.transfer.ftp;

import org.joda.time.DateTime;

public class FTPFile {

    private String name;
    private long size;
    private String absolutePath;
    private DateTime lastModified;
    private boolean directory;

    public FTPFile(String name, long size, String absolutePath, long mTime, boolean directory) {
        
        this.name = name;
        this.size = size;
        this.absolutePath = absolutePath;
        this.lastModified = new DateTime(mTime);
        this.directory = directory;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public String getPath() {
        return absolutePath;
    }

    public DateTime getLastModified() {
        return lastModified;
    }

    public boolean isDirectory() {
        return directory;
    }
}
