package services;

import data.User;
import db.TransactionDao;
import db.UserDao;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * Service that handles reading and updating bank user information
 * @author nilstes
 */
@Path("user")
public class UserService {
    
    private static final Logger log = Logger.getLogger(UserService.class.getName());

    private UserDao userDao = new UserDao();
    private TransactionDao transactionDao = new TransactionDao();

    @Context
    private HttpServletRequest request;

    @PUT
    @Path("/{email}")
    @Consumes("application/json")
    public void update(User user) {       
        try {
            userDao.updateUser(user);
            log.info("Updated user!");        
        } catch(SQLException e) {
            log.log(Level.SEVERE, "Failed to update user", e);        
            throw new ServerErrorException("Failed to update user", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    @GET
    @Path("/{email}")
    @Produces("application/json")
    public User get(@PathParam("email") String currentUserEmail) {
        try {
            return userDao.getUser(currentUserEmail);
        } catch(SQLException e) {
            log.log(Level.SEVERE, "Failed to get user", e);        
            throw new ServerErrorException("Failed to get user", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }
}
