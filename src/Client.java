import BatchProcessor.IBatchProcessor;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    private final IBatchProcessor service;
    public Client(int servicePort, String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
        service = (IBatchProcessor) Naming.lookup("rmi://localhost:"+servicePort+"/"+serviceName);
    }
    public String sendBatch(String batch) throws RemoteException {
        return service.processBatch(batch);
    }
}
