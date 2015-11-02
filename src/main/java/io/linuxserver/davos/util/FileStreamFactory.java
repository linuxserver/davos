package io.linuxserver.davos.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileStreamFactory {

    public FileInputStream createInputStream(String filePath) throws FileNotFoundException {
        return new FileInputStream(new File(filePath));
    }

    public FileOutputStream createOutputStream(String filePath) throws FileNotFoundException {
        return new FileOutputStream(new File(filePath));
    }
}
