/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JerseyBreeze.Server;

import JerseyBreeze.Entities.Customer;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;

/**
 * REST Web Service
 *
 * @author Josh
 */
@Path("context")
public class ContextResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ContextResource
     */
    public ContextResource() {
    }
   
    /**
     *
     * @return
     */
    @GET
    @Path("Metadata")
    @Produces("application/json")
    public Map<String,Object> getMetadata(){
        try {
            return JerseyBreezeContext.generateMetadata();
        } catch (Exception ex) {
            Logger.getLogger(ContextResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     *
     * @return
     */
    @GET
    @Path("Customers")
    @Produces("application/json")
    public List<Customer> getCustomers(){
        SessionFactory factory = JerseyBreezeContext.getSessionFactory();
        Session openSession = factory.openSession();
        List<Customer> results = openSession.createQuery("FROM Customer").list();
        openSession.close();
        return results;
        
    }
    
    /**
     *
     * @param content
     */
    @POST
    @Path("SaveChanges")
    @Consumes({ "application/json" })
    public void saveChanges(JerseyBreezeSaveRequest content) {
        SessionFactory factory = JerseyBreezeContext.getSessionFactory();
        Session openSession = factory.openSession();

        openSession.close();
    }
}
