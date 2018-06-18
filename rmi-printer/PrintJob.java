import java.io.Serializable;
//import java.util.Date;
import java.time.*;


public class PrintJob implements Serializable {

    private int id;
    private long owner;
    private String content;
    private LocalDateTime date;
    private int printer;
    private long timeToPrint;

    PrintJob(String content, int id, long owner) {
        this.content = content;
        this.date = LocalDateTime.now();
        this.id = id;
        this.owner = owner;

    }

    public void setPrinter(int printer) { this.printer = printer; }

    public void setTimeToPrint(long timeToPrint) { this.timeToPrint = timeToPrint; }

    public int getPrinter() { return printer; }

    public long getTimeToPrint() { return timeToPrint; }

    public String getContent() {
        return this.content;
    }

    public int getId() {
        return this.id;
    }

    public long getOwner() {
        return this.owner;
    }

    public LocalDateTime getDate() { return date; }

    @java.lang.Override
    public java.lang.String toString() {
        return "PrintJob{" +
                "id=" + id +

                ", owner=" + owner +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", printer=" + printer +
                ", timeToPrint=" + timeToPrint +
                '}';
    }
}