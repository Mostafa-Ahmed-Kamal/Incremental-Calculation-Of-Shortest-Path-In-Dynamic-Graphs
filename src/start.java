import BatchProcessor.BatchGenerator;
import Utils.FileManager;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.time.LocalTime;
import java.util.Random;

public class start {
    private static int rmiRegistryPort,batchSize,clientsCount,graphSize;
    private static String serviceName;
    private static float writePercentage;
    private static List<Thread> createClientThreads(String workingDirectory){
        List<Thread> clients = new LinkedList<>();
        for (int i = 0; i < clientsCount; i++) {
            String logsDirectory = workingDirectory+"/"+"clientNode_"+i+"_logs";
            String logsFile = logsDirectory+"/logs.txt";
            int clientId = i;
            FileManager.createFolder(logsDirectory);
            clients.add(new Thread(()->{
                try {
                    Random random = new Random();
                    Client client = new Client(rmiRegistryPort,serviceName);
                    BatchGenerator batchGenerator = new BatchGenerator();
                    for (int j=0 ; j<5 ; j++){
                        String randomBatch = batchGenerator.generateRandomBatch(writePercentage,batchSize,graphSize);
//                        String randomBatch = batchGenerator.generateCustomBatch();
                        LocalTime sendTime = LocalTime.now();
                        String batchResult = client.sendBatch(randomBatch);
                        LocalTime receiveTime = LocalTime.now();
                        long timeTaken = receiveTime.toNanoOfDay()-sendTime.toNanoOfDay();
                        FileManager.appendToFile(logsFile,"sendTime: "+sendTime + "\nreceivedTime: "+receiveTime+"\ntimeTaken: "+timeTaken/1000000+"ms");
                        FileManager.appendToFile(logsFile,"batch sent:\n"+randomBatch+"\nresult:\n"+batchResult);
//                        FileManager.appendToFile(logsFile,""+timeTaken/1000000);
                        Thread.sleep(1000);
                    }
                    System.out.println("client_"+clientId+" finished");
                }
                catch (NotBoundException | RemoteException | MalformedURLException | InterruptedException e) {
                    e.printStackTrace();
                }
            }));
        }
        return clients;
    }

    public static void main(String[] args) throws InterruptedException {
        String workingDirectory = "logs";
        HashMap<String,String> systemProperties = FileManager.readKeyValuePairs("system.properties");
        BatchGenerator mainBatchGenerator = new BatchGenerator();
        FileManager.createFolder(workingDirectory);
        rmiRegistryPort = Integer.parseInt(systemProperties.getOrDefault("GSP.rmiregistry.port","1099"));
        clientsCount = Integer.parseInt(systemProperties.getOrDefault("GSP.numberOfnodes", "2"));
        graphSize = Integer.parseInt(systemProperties.getOrDefault("GSP.graphSize","10"));
        batchSize = Integer.parseInt(systemProperties.getOrDefault("GSP.clientBatchSize","5"));
        writePercentage = Float.parseFloat(systemProperties.getOrDefault("GSP.writePercentage","0.5"));
        serviceName = systemProperties.getOrDefault("GSP.serviceName","update");
        String initialGraph = mainBatchGenerator.generateRandomGraphInitializer(graphSize,graphSize);
//        String initialGraph = mainBatchGenerator.generateCustomGraphInitializer();
        Thread serverThread = new Thread(() ->{
            String serverLogsFolder = workingDirectory+"/server_logs";
            FileManager.createFolder(serverLogsFolder);
            try {
                Server.start(initialGraph, rmiRegistryPort,serviceName,serverLogsFolder);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
        Thread.sleep(1000);
        System.out.println("server started at port: "+ rmiRegistryPort);
        List<Thread> clients = createClientThreads(workingDirectory);
        for (Thread client:clients) {
            client.start();
        }
        System.out.println(clients.size() + " clients started");
        for (Thread client:clients){
            client.join();
        }
        serverThread.join();
    }
}
