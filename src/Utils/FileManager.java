package Utils;

import java.io.*;
import java.util.HashMap;

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
    public static HashMap<String,String> readKeyValuePairs(String filePath){
        HashMap<String,String> systemProperties = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] keyValuePair = line.split("\\s*=\\s*");
                if (keyValuePair.length!=2)continue;
                systemProperties.put(keyValuePair[0],keyValuePair[1]);
            }
        }
        catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + filePath);
            e.printStackTrace();
        }
        return systemProperties;
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
