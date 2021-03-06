package services;

import data.LoginData;
import data.Session;
import data.Transaction;
import data.User;
import db.TransactionDao;
import db.UserDao;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * Service to handle bank logon and logout using the web-session
 * @author nilstes
 */
@Path("session")
public class SessionService {
    
    private static final Logger log = Logger.getLogger(SessionService.class.getName());

    private UserDao userDao = new UserDao();
    private TransactionDao transactionDao = new TransactionDao();

    @Context
    private HttpServletRequest request;

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Session create(LoginData data) {
        log.info("Trying to logon or register new user");
        try {
            User user = userDao.getUser(data.getEmail());
            if(user == null && data.getEmail() != null && data.getPassword() != null) {
                // Register new user
                userDao.addUser(new User(data.getEmail(), "", "", data.getPassword()));
                transactionDao.addTransaction(new Transaction("nissen@ikkesaasikkerbanken.no", data.getEmail(), "Startgave", 1000d, new Date()));
                log.info("Registered new user");
            } else {
                // Existing user. Check that password is correct.
                if(!data.getPassword().equals(user.getPassword())) {
                    throw new NotAuthorizedException("Feil brukernavn eller passord");
                }
            }
        } catch(SQLException e) {
            log.log(Level.SEVERE, "Failed to check or register user", e);        
            throw new ServerErrorException("DB error", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
        Session session = new Session();
        session.setEmail(data.getEmail());
        session.setLoggedOn(new Date());
        request.getSession().invalidate();
        request.getSession().setAttribute("session", session);
        log.info("Logged on!");
        return session;
    }

    @GET
    @Produces("application/json")
    public Session get() {
        Session session = (Session)request.getSession().getAttribute("session");
        if(session == null) {
            log.info("Session not found");
            throw new NotFoundException();        
        }
        log.info("Returning session info!");
        return session;
    }
    
    @DELETE
    public void delete() {
        request.getSession().invalidate();
        log.info("Logged out!");
    }
}
