package BatchProcessor;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IBatchProcessor extends Remote {
    String processBatch(String batch) throws RemoteException;
}
