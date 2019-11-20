package repositories;

public class Transaction {
    private long from;
    private long to;
    private int amount;
    
    public Transaction(long from, long to, int amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;

    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
