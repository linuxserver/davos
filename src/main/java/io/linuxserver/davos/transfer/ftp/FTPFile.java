package io.linuxserver.davos.transfer.ftp;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

public class FTPFile {

    private String name;
    private long size;
    private String path;
    private DateTime lastModified;
    private boolean directory;

    public FTPFile(String name, long size, String path, long mTime, boolean directory) {
        
        this.name = name;
        this.size = size;
        this.path = path;
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
        return path;
    }

    public DateTime getLastModified() {
        return lastModified;
    }

    public boolean isDirectory() {
        return directory;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
