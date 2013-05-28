/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JerseyBreeze.Servlets;

import JerseyBreeze.Entities.Customer;
import JerseyBreeze.Entities.JerseyBreezeContext;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
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
     * Retrieves representation of an instance of JerseyBreeze.Servlets.ContextResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of ContextResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
    
    @GET
    @Path("metadata")
    public String Metadata(){
        return "gimmeh metadaters";
    }
    
    @GET
    @Path("customers")
    @Produces("application/json")
    public List<Customer> Customers(){
        SessionFactory factory = JerseyBreezeContext.getSessionFactory();
        Session openSession = factory.openSession();
        List<Customer> results = openSession.createQuery("FROM Customer").list();
        openSession.close();
        return results;
        
    }
}
