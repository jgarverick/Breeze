/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JerseyBreeze.Server;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import org.codehaus.jackson.map.ObjectMapper;
import JerseyBreeze.Entities.*;
import org.codehaus.jackson.JsonNode;
/**
 *
 * @author jgarverick
 */
@Provider
@Consumes("application/json")
public class JerseyBreezeMessageBodyReader implements MessageBodyReader {

    public boolean isReadable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
       return true;
    }

    public Object readFrom(Class type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode readTree = mapper.readTree(entityStream);
            for(JsonNode node: readTree){
                String name = node.asToken().name();
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, null, name);
            }
        JerseyBreezeSaveRequest output = mapper.readValue(entityStream, JerseyBreezeSaveRequest.class);
        if(output.getEntities().size()>0){
            for (Iterator<LinkedHashMap> it = output.getEntities().iterator(); it.hasNext();) {
                LinkedHashMap entity = it.next();
                LinkedHashMap aspect = (LinkedHashMap)entity.get("entityAspect");
                String entry = aspect.values().toArray()[0].toString();
                String[] split = entry.split(":#");
                String newEntry = split[1] + "." + split[0];
                Class<?> loadClass = Class.forName(newEntry);
                //Class<?> clValue = Class.forName(entry);
                Object newObj = mapper.readValue(entity.toString(), loadClass);
                return newObj;
            }
        }
        return output;
        } catch(Exception e){
            try {
                throw e;
            } catch (Exception ex) {
                Logger.getLogger(JerseyBreezeMessageBodyReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return new ObjectMapper().readValue(entityStream, type);
    }
    
}
