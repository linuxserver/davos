package io.linuxserver.davos.util;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public File getFile(String filePath) {
        return new File(filePath);
    }

    public void moveFileToDirectory(String oldPath, String newPath) throws IOException {
        org.apache.commons.io.FileUtils.moveToDirectory(getFile(oldPath), getFile(newPath), true);
    }
    
    public void createLocalDirectory(String directoryPath) {
        new File(directoryPath).mkdirs();
    }

    public static String ensureTrailingSlash(String path) {

        if (!path.endsWith("/"))
            return path + "/";

        return path;
    }
}
