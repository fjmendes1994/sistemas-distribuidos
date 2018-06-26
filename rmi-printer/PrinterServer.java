import java.rmi.Naming;

public class PrinterServer {
    PrinterServer() {
        try {
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            PrinterManagerImple printerManager = new PrinterManagerImple();
            Naming.rebind("rmi://ip-172-31-31-120.ec2.internal:2443/PrintService", printerManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new PrinterServer();
    }

} 
