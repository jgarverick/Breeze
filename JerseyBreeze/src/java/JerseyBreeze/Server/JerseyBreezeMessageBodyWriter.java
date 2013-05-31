/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JerseyBreeze.Server;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import org.codehaus.jackson.map.ObjectMapper;


/**
 *
 * @author jgarverick
 */
@Provider
@Produces("application/json")
public class JerseyBreezeMessageBodyWriter implements MessageBodyWriter {

    
    @Override
    public boolean isWriteable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return Object.class.isAssignableFrom(type);
    }
    @Override
    public long getSize(Object t, Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }
    @Override
    public void writeTo(Object t, Class type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        if(t.getClass() == ArrayList.class || t.getClass().getAnnotation(Entity.class) != null){
            ArrayList arr = new ArrayList();
            int counter = 1;
            for (Iterator it = ((ArrayList)t).iterator(); it.hasNext();) {
                Object item = it.next();
                
                Map<String,Object> entity = getEntity(item, counter);
                if(entity == null){
                } else {
                    arr.add(entity);               
                    counter++;
                }
            }
            if(arr.size() < 1) arr = (ArrayList)t;
            new ObjectMapper().writeValue(entityStream, arr);
            return;
        }
        new ObjectMapper().writeValue(entityStream, t);
    }
    
    private Map<String,Object> getEntity(Object item, int counter){
        if(item.getClass().getAnnotation(Entity.class) != null){
             Map<String,Object> entity = new HashMap<String,Object>();
                entity.put("$id", counter);
                entity.put("$type", item.getClass().getName() + ", " + item.getClass().getPackage().getName());
                Field[] fields = item.getClass().getDeclaredFields();
                for(int i=0;i<fields.length;i++){
                    try {
                        fields[i].setAccessible(true);
                        entity.put(fields[i].getName(), fields[i].get(item));
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(JerseyBreezeMessageBodyWriter.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(JerseyBreezeMessageBodyWriter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                return entity;
        }
        return null;
    }
    
}

