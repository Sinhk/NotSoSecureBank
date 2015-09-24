package data;

import java.util.Date;

/**
 * @author nilstes
 */
public class Transaction {
    private String fromEmail;
    private String toEmail;
    private String text;
    private Double amount;
    private Date transactionTime;

    public Transaction() {
    }

    public Transaction(String fromEmail, String toEmail, String text, Double amount, Date transactionTime) {
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.text = text;
        this.amount = amount;
        this.transactionTime = transactionTime;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }    
}
