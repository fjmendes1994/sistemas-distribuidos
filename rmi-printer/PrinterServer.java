import java.rmi.Naming;

public class PrinterServer {
    PrinterServer() {
        try {
            PrinterManagerImple printerManager = new PrinterManagerImple();
            Naming.rebind("rmi://127.0.0.1:2443/PrintService", printerManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new PrinterServer();
    }

} 
