import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PrinterManager extends Remote {
    public void submitJob(PrintJob printJob) throws RemoteException;
}
