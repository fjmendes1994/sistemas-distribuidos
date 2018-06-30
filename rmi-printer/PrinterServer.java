import java.rmi.Naming;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class PrinterServer {
    PrinterServer() {
        try {
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            PrinterManager printerManager = new PrinterManagerImple();
            System.setProperty("java.rmi.server.hostname", "ip-172-31-31-120.ec2.internal");

            Registry registry = LocateRegistry.createRegistry(2443);
            registry.rebind("print_service", printerManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new PrinterServer();
    }

}
