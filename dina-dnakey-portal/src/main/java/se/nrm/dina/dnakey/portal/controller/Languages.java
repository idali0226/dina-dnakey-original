package se.nrm.dina.dnakey.portal.controller;

import java.io.Serializable;  
import javax.enterprise.context.SessionScoped;    
import javax.inject.Named; 
import lombok.extern.slf4j.Slf4j; 
/**
 *
 * @author idali
 */
@SessionScoped
@Named
@Slf4j
public class Languages implements Serializable { 
    
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
        log.info("changelanguage - locale: {}", locale);
         
        setLocale(locale);
    }
  
    public String getLanguage() {
        return locale.equals("sv") ? "English" : "Svenska";
    }
      
    public boolean isIsSwedish() {
        return locale.equals("sv");
    }
}
