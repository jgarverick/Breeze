/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JerseyBreeze.Server;

import java.util.Map;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.SessionFactory;

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
            AnnotationConfiguration config = new AnnotationConfiguration();
            sessionFactory = config.configure().buildSessionFactory();
            
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
}
