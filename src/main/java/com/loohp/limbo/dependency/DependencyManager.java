package com.loohp.limbo.dependency;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DependencyManager {

    private final File directory = new File("dependencies");

    public DependencyManager() {
        checkFolderExists();
    }

    /**
     * Creates the dependencies folder if it does not exist
     */
    public void checkFolderExists() {
        if (!this.directory.exists())
            this.directory.mkdirs();
    }

    public List<URL> getDependencyUrls() {
        return getDependencyFiles().stream().map(file -> {
            try {
                return file.toURI().toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    /**
     * @return all jar files in the directories folder
     */
    public List<File> getDependencyFiles() {
        return Arrays.stream(Objects.requireNonNull(this.directory.listFiles()))
                .filter(File::isFile)
                .filter(file -> file.getName().endsWith(".jar"))
                .toList();
    }

}
