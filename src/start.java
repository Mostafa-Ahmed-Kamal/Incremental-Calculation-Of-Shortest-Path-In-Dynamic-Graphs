import BatchProcessor.BatchGenerator;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

public class start {
    private List<Thread> createClientThreads(int servicePort, String serviceName, int clientsCount){
        List<Thread> clients = new LinkedList<>();
        for (int i = 0; i < clientsCount; i++) {
            clients.add(new Thread(()->{
                try {
                    Client client = new Client(servicePort,serviceName);
                    BatchGenerator batchGenerator = new BatchGenerator();
                    for (int j=0 ; j<5 ; j++){
//                        System.out.println("c1 Batch "+j+":\n"+client.sendBatch(batchGenerator.generateRandomBatch(2,4,10)));
                        System.out.println("c1 Batch "+j+":\n"+client.sendBatch(batchGenerator.generateCustomBatch()));
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

    public static void main(String[] args) throws InterruptedException {
        start start = new start();
        BatchGenerator mainBatchGenerator = new BatchGenerator();
        int servicePort = 1099;
        String serviceName = "BatchProcessor";
//        String initialGraph = mainBatchGenerator.generateRandomGraphInitializer(10,10);
        String initialGraph = mainBatchGenerator.generateCustomGraphInitializer();
        Thread serverThread = new Thread(() ->{
            try {
                Server.start(initialGraph,servicePort,serviceName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
        Thread.sleep(1000);
        List<Thread> clients = start.createClientThreads(servicePort,serviceName,2);
        for (Thread client:clients) {
            client.start();
        }
        for (Thread client:clients){
            client.join();
        }
        serverThread.join();
    }
}
