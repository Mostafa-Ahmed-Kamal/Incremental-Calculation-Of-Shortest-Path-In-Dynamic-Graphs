import BatchProcessor.BatchProcessor;
import Utils.FileManager;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void start(String initialGraph, int rmiRegistryPort, String serviceName, String workingDirectory) throws RemoteException {
        BatchProcessor batchProcessor = new BatchProcessor();
        batchProcessor.setWorkingDirectory(workingDirectory);
        batchProcessor.initializeGraph(initialGraph);
        System.out.println("R");
        FileManager.appendToFile(workingDirectory+"/logs.txt","Initial Graph:\n"+initialGraph);
        Registry registry = LocateRegistry.createRegistry(rmiRegistryPort);
        registry.rebind(serviceName,batchProcessor);
    }
}
