package Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
    public static void createFolder(String path){
        File folder = new File(path);
        if (folder.exists()) {
            deleteFolder(folder);
        }
        boolean folderCreated = folder.mkdirs();
        if (!folderCreated) {
            System.out.println("Failed to create directory: " + folder.getAbsolutePath());
        }
    }
    public static void appendToFile(String filePath, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(content);
            writer.newLine();
        }
        catch (IOException e) {
            System.out.println("An error occurred while appending to the file: " + filePath);
            e.printStackTrace();
        }
    }
    private static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolder(file);
                } else {
                    file.delete();
                }
            }
        }
        folder.delete();
    }
}
