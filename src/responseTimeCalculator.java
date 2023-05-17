import Utils.FileManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class responseTimeCalculator {
    public static void main(String[] args) {
        HashMap<String,String> systemProperties = FileManager.readKeyValuePairs("system.properties");
        int clientCount = Integer.parseInt(systemProperties.get("GSP.numberOfnodes"));
        List<Integer> allResponses = new LinkedList<>();
        for (int i = 0; i < clientCount; i++) {
            long sum = 0;
            String filePath = "logs/clientNode_"+i+"_logs/logs.txt";
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    allResponses.add(Integer.parseInt(line));
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        float averageTime = 0;
        for (int response:allResponses) {
            averageTime+=((float)response/allResponses.size());
        }
        int p90 = (int)(allResponses.size()*0.9);
        Collections.sort(allResponses);
        System.out.println("90 percentile = " + allResponses.get(p90));
        System.out.println("average = "+averageTime);
    }
}
