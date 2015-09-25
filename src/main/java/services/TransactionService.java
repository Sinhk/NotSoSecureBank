package services;

import data.Session;
import data.Transaction;
import db.TransactionDao;
import db.UserDao;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * Service handling user transactions in the bank
 * @author nilstes
 */
@Path("user/{email}/transaction")
public class TransactionService {
    
    private static final Logger log = Logger.getLogger(TransactionService.class.getName());

    private UserDao userDao = new UserDao();
    private TransactionDao transactionDao = new TransactionDao();

    @Context
    private HttpServletRequest request;

    @POST
    @Consumes("application/json")
    public void add(Transaction transaction) {       
        // Check that the transaction sender is the logged user
        Session session = (Session)request.getSession().getAttribute("session");
        if(!transaction.getFromEmail().equals(session.getEmail())) {
            throw new NotAuthorizedException("Cannot access this account", Response.Status.FORBIDDEN);
        }
        
        // Add transaction
        try {
            transactionDao.addTransaction(transaction);
            log.info("Added transaction!");        
        } catch(SQLException e) {
            log.log(Level.SEVERE, "Failed to add transaction", e);        
            throw new ServerErrorException("Failed to add transaction", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    @GET
    @Produces("application/json")
    public List<Transaction> get(@PathParam("email") String currentUserEmail) {       
        // Check that it is the logged user's account that is being accessed
        Session session = (Session)request.getSession().getAttribute("session");
        if(!currentUserEmail.equals(session.getEmail())) {
            throw new NotAuthorizedException("Cannot access this account", Response.Status.FORBIDDEN);
        }
        
        // Get transaction
        try {
            return transactionDao.getTransactions(currentUserEmail);
        } catch(SQLException e) {
            log.log(Level.SEVERE, "Failed to read user transactions", e);        
            throw new ServerErrorException("Failed to read user transactions", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }
}
