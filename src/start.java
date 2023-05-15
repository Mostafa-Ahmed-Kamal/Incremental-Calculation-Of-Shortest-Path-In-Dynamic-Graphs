import BatchProcessor.BatchGenerator;
import Utils.FileManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.time.LocalTime;

public class start {

    private List<Thread> createClientThreads(int servicePort, String serviceName, int clientsCount, String workingDirectory){
        List<Thread> clients = new LinkedList<>();
        for (int i = 0; i < clientsCount; i++) {
            String logsDirectory = workingDirectory+"/"+"clientNode_"+i+"_logs";
            String logsFile = logsDirectory+"/logs.txt";
            FileManager.createFolder(logsDirectory);
            clients.add(new Thread(()->{
                try {
                    Client client = new Client(servicePort,serviceName);
                    BatchGenerator batchGenerator = new BatchGenerator();
                    for (int j=0 ; j<5 ; j++){
                        String randomBatch = batchGenerator.generateRandomBatch(2,4,10);
                        LocalTime sendTime = LocalTime.now();
                        String batchResult = client.sendBatch(randomBatch);
                        LocalTime receiveTime = LocalTime.now();
                        FileManager.appendToFile(logsFile,"sendTime: "+sendTime + "\nreceivedTime: "+receiveTime);
                        FileManager.appendToFile(logsFile,"batch sent:\n"+randomBatch+"\nresult:\n"+batchResult);
                        Thread.sleep(1000);
                    }
                }
                catch (NotBoundException | RemoteException | MalformedURLException | InterruptedException e) {
                    e.printStackTrace();
                }
            }));
        }
        return clients;
    }
    private HashMap<String,String> readSystemProperties(String filePath){
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
    public static void main(String[] args) throws InterruptedException {
        start start = new start();
        String workingDirectory = "logs";
        HashMap<String,String> systemProperties = start.readSystemProperties("system.properties");
        BatchGenerator mainBatchGenerator = new BatchGenerator();
        FileManager.createFolder(workingDirectory);
        int servicePort = Integer.parseInt(systemProperties.getOrDefault("GSP.rmiregistry.port","1099"));
        int nodeCount = Integer.parseInt(systemProperties.getOrDefault("GSP.numberOfnodes", "2"));
        String serviceName = "BatchProcessor";
        String initialGraph = mainBatchGenerator.generateRandomGraphInitializer(10,10);
        Thread serverThread = new Thread(() ->{
            String serverLogsFolder = workingDirectory+"/server_logs";
            FileManager.createFolder(serverLogsFolder);
            try {
                Server.start(initialGraph,servicePort,serviceName,serverLogsFolder);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
        Thread.sleep(1000);
        System.out.println("server started at port: "+servicePort);
        List<Thread> clients = start.createClientThreads(servicePort,serviceName,nodeCount,workingDirectory);
        for (Thread client:clients) {
            client.start();
        }
        for (Thread client:clients){
            client.join();
        }
        System.out.println(clients.size() + " clients started");
        serverThread.join();
    }
}
