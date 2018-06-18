import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class PrinterManagerImple extends UnicastRemoteObject implements PrinterManager {

    private Printer printerOne;
    private Printer printerTwo;

    private Queue<PrintJob> printQueue;

    protected PrinterManagerImple() throws RemoteException {
        super();
        this.printQueue = new LinkedList<PrintJob>();
        this.printerOne = new Printer(1, printQueue);
        this.printerTwo = new Printer(2, printQueue);
        synchronized (printerOne) {
            printerOne.start();
        }
        synchronized (printerTwo) {
            printerTwo.start();
        }
    }


    public void submitJob(PrintJob printerJob) {

        synchronized (printQueue) {
            if(printQueue.size() < 3){
                printQueue.add(printerJob);
                printerOne.setPaused(false);
                printerTwo.setPaused(false);
            } else {
                System.out.println("Fila de impressão cheia, job " + printerJob.getId() + " do cliente " + printerJob.getOwner() + " descartado. Tente novamente.");
            }

        }



    }
}