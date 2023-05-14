import BatchProcessor.BatchProcessor;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void start(String initialGraph, int servicePort, String serviceName) throws RemoteException {
        BatchProcessor batchProcessor = new BatchProcessor();
        batchProcessor.initializeGraph(initialGraph);
        Registry registry = LocateRegistry.createRegistry(servicePort);
        registry.rebind(serviceName,batchProcessor);
    }
}
