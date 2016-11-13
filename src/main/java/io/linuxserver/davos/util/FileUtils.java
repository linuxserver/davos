package io.linuxserver.davos.util;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    public File getFile(String filePath) {
        return new File(filePath);
    }

    public void moveFileToDirectory(String oldPath, String newPath) throws IOException {
        org.apache.commons.io.FileUtils.moveToDirectory(getFile(oldPath), getFile(newPath), true);
    }

    public void createLocalDirectory(String directoryPath) {
        boolean directoryCreated = new File(directoryPath).mkdirs();

        if (!directoryCreated)
            LOGGER.debug("Directory was not created!");
    }

    public static String ensureTrailingSlash(String path) {

        if (!path.endsWith("/"))
            return path + "/";

        return path;
    }
}
