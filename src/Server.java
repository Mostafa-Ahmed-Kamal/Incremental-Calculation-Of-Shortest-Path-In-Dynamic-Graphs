import BatchProcessor.BatchProcessor;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void start(String initialGraph, int servicePort, String serviceName, String workingDirectory) throws RemoteException {
        BatchProcessor batchProcessor = new BatchProcessor();
        batchProcessor.chooseWorkingDirectory(workingDirectory);
        batchProcessor.initializeGraph(initialGraph);
        Registry registry = LocateRegistry.createRegistry(servicePort);
        registry.rebind(serviceName,batchProcessor);
    }
}
