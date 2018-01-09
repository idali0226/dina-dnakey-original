package se.nrm.dina.dnakey.portal.controller;

import java.io.Serializable;  
import javax.enterprise.context.SessionScoped;    
import javax.inject.Named; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;      

/**
 *
 * @author idali
 */
@SessionScoped
@Named
public class Languages implements Serializable {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
  
    
    private String locale = "sv";  

    public Languages() {
    } 
 
    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
      
    public void changelanguage(String locale) {
        
        logger.info("changelanguage - locale: {}", locale);
         
        setLocale(locale);
    }
  
    public String getLanguage() {
        return locale.equals("sv") ? "English" : "Svenska";
    }
      
    public boolean isIsSwedish() {
        return locale.equals("sv");
    }
}
