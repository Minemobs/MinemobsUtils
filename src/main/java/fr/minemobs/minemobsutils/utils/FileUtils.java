package fr.minemobs.minemobsutils.utils;

import com.google.common.base.Charsets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtils {

    private FileUtils() {}

    public static void createFile(File file) throws IOException {
        if(file.exists()) return;
        if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
        file.createNewFile();
    }

    public static void recreateFile(File file) throws IOException {
        file.delete();
        file.createNewFile();
    }

    public static void save(File file, String json) {
        try {
            createFile(file);
            final FileWriter fw = new FileWriter(file);
            fw.write(json);
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String loadContent(File file) {
        if(!file.exists()) return null;
        try {
            final BufferedReader reader = Files.newBufferedReader(file.toPath(), Charsets.UTF_8);
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            return builder.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
