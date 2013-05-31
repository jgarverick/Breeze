/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JerseyBreeze.Server;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author jgarverick
 */
public class JerseyBreezeSaveRequest {
    private List<LinkedHashMap> entities;
    private Object saveOptions;
    
    public void setEntities(List<LinkedHashMap> newValues){
        entities = newValues;
    }
    
    public List<LinkedHashMap> getEntities(){
        return this.entities;
    }
    
    public void setSaveOptions(Object options){
        this.saveOptions = options;
    }
    
    public Object getSaveOptions(){
        return this.saveOptions;
    }
    
    public JerseyBreezeSaveRequest(){
        this.entities = new ArrayList<LinkedHashMap>();
    }
}


