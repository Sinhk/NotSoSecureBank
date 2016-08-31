package db;

import data.Transaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author nilstes
 * TransactioDAO
 */
public class TransactionDao {

    private static final Logger log = Logger.getLogger(TransactionDao.class.getName());

    public List<Transaction> getTransactions(String email) throws SQLException {
        Connection connection = Db.instance().getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM transaction WHERE from_email=? OR to_email=?");
            statement.setString(1, email);
            statement.setString(2, email);           
            ResultSet rs = statement.executeQuery();
            List<Transaction> transactions = new ArrayList<Transaction>();
            while(rs.next()) {
                Transaction t = new Transaction();
                t.setFromEmail(rs.getString("from_email"));
                t.setToEmail(rs.getString("to_email"));
                t.setText(rs.getString("text"));
                t.setAmount(rs.getDouble("amount"));   
                t.setTransactionTime(rs.getTimestamp("transaction_time"));
                transactions.add(t);
            }               
            rs.close();
            statement.close();
            log.info("Found " + transactions.size() + " transactions for " + email);
            return transactions;
        } finally {
            connection.close();
        }
    }
    
    public boolean addTransaction(Transaction transaction) throws SQLException {
        Connection connection = Db.instance().getConnection();
        try {
            PreparedStatement s = connection.prepareStatement("INSERT INTO transaction (from_email,to_email,text,amount,transaction_time) VALUES(?,?,?,?,?)");
            s.setString(1, transaction.getFromEmail());
            s.setString(2, transaction.getToEmail());
            s.setString(3, transaction.getText());
            s.setDouble(4, transaction.getAmount()); 
            s.setTimestamp(5, new Timestamp(transaction.getTransactionTime().getTime()));
            int result = s.executeUpdate();
            s.close();
            log.info("Add transaction " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }        
    }
}
