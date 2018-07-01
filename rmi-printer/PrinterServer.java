import java.rmi.Naming;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class PrinterServer {
    PrinterServer() {
        try {
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            PrinterManager printerManager = new PrinterManagerImple();
            // PrinterManager stubPrinterManager = (PrinterManager) UnicastRemoteObject.exportObject(printerManager, 0);

            System.setProperty("java.rmi.server.hostname", "34.239.245.237");

            Registry registry = LocateRegistry.createRegistry(2443);
            registry.rebind("PrintService", printerManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new PrinterServer();
    }

}
