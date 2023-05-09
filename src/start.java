import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class start {
    private String initialGraphTest(){
        return """
                1 2
                2 3
                3 1
                4 1
                2 4
                S
                """;
    }
    private String randomBatchTest(){
        return """
                A 4 5
                A 5 3
                Q 1 3
                D 2 3
                Q 1 3
                F
                """;
    }
    public static void main(String[] args) throws InterruptedException {
        start start = new start();
        int servicePort = 1099;
        String serviceName = "BatchProcessor";
        String initialGraph = start.initialGraphTest();
        Thread serverThread = new Thread(() ->{
            try {
                Server.start(initialGraph,servicePort,serviceName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
        Thread.sleep(1000);
        Thread clientThread = new Thread(()->{
            try {
                Client client = new Client(servicePort,serviceName);
                for (int i = 0; i < 5; i++) {
                    System.out.println("Batch "+i+":\n"+client.sendBatch(start.randomBatchTest()));
                    Thread.sleep(1000);
                    }
                }
            catch (NotBoundException | RemoteException | MalformedURLException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        clientThread.start();

        serverThread.join();
        clientThread.join();
    }
}
