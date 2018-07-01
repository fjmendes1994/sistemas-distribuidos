import java.awt.HeadlessException;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.rmi.Naming;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;



public class PrinterClient {

    public static void main(String[] args) {
        try {
            if(System.getSecurityManager() == null){
                System.setSecurityManager(new SecurityManager());
            }
            String serverIP = "54.172.102.230"; // or localhost if client and server on same machine.
            int serverPort = 2443;
            Registry registry = LocateRegistry.getRegistry(serverIP, serverPort);
            System.out.println("Achei o Registry");
            PrinterManager printerManger = (PrinterManager) registry.lookup("PrintService");
            System.out.println("Achei o PrinterManager");
            PrintJob printJob;
            for (int i = 0; i < 10; i++) {
                printJob = new PrintJob("Conteudo a ser impresso...", i, ProcessHandle.current().pid());
                printerManger.submitJob(printJob);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
