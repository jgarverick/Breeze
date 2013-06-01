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
import java.util.ArrayList;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
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
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            JsonNode readTree = mapper.readTree(entityStream);
            ArrayList<Object> list = new ArrayList<Object>();
            for(JsonNode node: readTree){
                for(Iterator<JsonNode> it = node.iterator();it.hasNext();){
                    JsonNode child = it.next();
                    JsonNode entity = child.get("entityAspect");
                    String entry = entity.get("entityTypeName").asText();
                String[] split = entry.split(":#");
                String newEntry = split[1] + "." + split[0];
                Class<?> loadClass = Class.forName(newEntry);
                    Object newObj = mapper.readValue(child, loadClass);
                list.add(newObj);
                }
            }
          return list;      
        }

         catch(Exception e){
            try {
                throw e;
            } catch (Exception ex) {
                Logger.getLogger(JerseyBreezeMessageBodyReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return new ObjectMapper().readValue(entityStream, type);
    }
    
}
