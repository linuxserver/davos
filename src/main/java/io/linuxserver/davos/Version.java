package io.linuxserver.davos;

public class Version {

    private int major;
    private int minor;
    private int patch;

    public Version(int major, int minor, int patch) {

        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public Version(String version) {

        String[] bits = version.split("\\.");

        this.major = Integer.parseInt(bits[0]);
        this.minor = Integer.parseInt(bits[1]);
        this.patch = Integer.parseInt(bits[2]);
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public boolean isNewerThan(Version version) {

        if (this.major > version.major)
            return true;

        if (this.minor > version.minor)
            return true;
        else if (this.minor < version.minor)
            return false; 
        
        if (this.patch > version.patch)
            return true;

        return false;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(major).append(".").append(minor).append(".").append(patch).toString();
    }
}
