/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JerseyBreeze.Server;

import java.util.ArrayList;
import java.util.Map;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author Josh
 */
public class JerseyBreezeContext {

    private static final SessionFactory sessionFactory;
    private static final HBreezeMetadata metaGenerator;
    
    static {
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.
            Configuration config = new Configuration().configure("northwindIB.cfg.xml");
            sessionFactory = config.buildSessionFactory();
            
            // Initialize the metadata provider
            metaGenerator = new HBreezeMetadata(sessionFactory, config);
        } catch (Throwable ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    /**
     *
     * @return
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    /**
     *
     * @return
     * @throws Exception
     */
    public static Map<String,Object> generateMetadata() throws Exception{
        return metaGenerator.BuildMetadata();
    }
    
    public static Object saveChanges(ArrayList<Object> contents) throws Exception{
        SessionFactory factory = JerseyBreezeContext.getSessionFactory();
        Session openSession = factory.openSession();
        Transaction tx = null;
        for (Object o : contents) {
            try {
                tx = openSession.beginTransaction();
                openSession.merge(o);
                //openSession.flush();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
        openSession.flush();
        openSession.close();
        return null;
    }
}
