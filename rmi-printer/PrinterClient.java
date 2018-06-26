import java.awt.HeadlessException;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.rmi.Naming;


public class PrinterClient {

    public static void main(String[] args) {
        try {
            PrinterManager printerManger = (PrinterManager) Naming.lookup("rmi://54.172.102.230:2443/PrintService");
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