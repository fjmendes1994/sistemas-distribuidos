import java.util.*;
import java.lang.*;
import java.util.concurrent.ThreadLocalRandom;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Printer extends Thread {
    private int id;
    private boolean paused;
    private Queue<PrintJob> printQueue;
    private PrintJob currentJob;
    private int printTime;

    public Printer(int id, Queue<PrintJob> printQueue) {
        this.id = id;
        this.paused = false;
        synchronized (printQueue) {
            this.printQueue = printQueue;
        }

    }

    private void print(PrintJob printJob) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("out/" + printJob.getOwner() + "_" + printJob.getId() + "_" + printJob.getDate()))) {
            String content = printJob.toString();
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void consume() throws InterruptedException {
        synchronized (printQueue) {
            if (printQueue.size() == 0) {
                System.out.println("Impressora " + id + " em espera.");
                this.currentJob = null;
                this.setPaused(true);
            } else {
                this.currentJob = printQueue.poll();
                this.printTime = ThreadLocalRandom.current().nextInt(10, 30 + 1) * 100;
                this.currentJob.setTimeToPrint(printTime);
                this.currentJob.setPrinter(this.id);
                this.sleep(printTime);
            }
        }


    }

    private synchronized void checkPaused() throws InterruptedException {
        if (paused) {
            wait();
        }
    }

    public synchronized void setPaused(boolean paused) {
        this.paused = paused;
        if (!this.paused)
            notify();
    }

    private void printLog(){
        System.out.println("Imprimindo Job " + currentJob.getId() + " do cliente " + currentJob.getOwner() + " na impressora " + id + ", tamanho da fila " + printQueue.size() + ", tempo de impressao " + this.printTime + "ms");
    }

    public void run() {
        do {
            try {
                checkPaused();
                consume();
                if (currentJob != null) {
                    print(currentJob);
                    printLog();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } while (true);
    }
}