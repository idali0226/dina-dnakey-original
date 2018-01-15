/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dnakey.portal.controller;

import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.context.RequestContext;
import se.nrm.dina.dnakey.portal.beans.StyleBean;

/**
 *
 * @author idali
 */
@Named("navigate")
@SessionScoped
@Slf4j
public class Navigator implements Serializable  {
    
    private RequestContext requestContext; 
    private ExternalContext externalContext; 
    
    private static final String HOME_PATH = "/faces/pages/sequence.xhtml";
    private static final String DNAKEY_PATH = "/faces/pages/dnakey.xhtml";
    private static final String ABOUT_PATH = "/faces/pages/about.xhtml";
    private static final String EXTERNAL_LINK_PATH = "/faces/pages/externalLinks.xhtml";
    private static final String CONTACT_PATH = "/faces/pages/contact.xhtml";
    private static final String RESULT_PATH = "/faces/pages/result.xhtml";
    
    private String clientId;
    
    @Inject
    private StyleBean style;
    
    private void redirectPage(String path) {
        externalContext = FacesContext.getCurrentInstance().getExternalContext(); 
        try {
            externalContext.redirect(externalContext.getRequestContextPath() + path);
        } catch (IOException ex) { 
        }
    }

    public void home() {
        log.info("home");  
        
        style.resetTabStyle(0);
        redirectPage(HOME_PATH); 
    }
    
    public void dnakey() {
        log.info("dnakey");  
        
        style.resetTabStyle(1);
        redirectPage(DNAKEY_PATH); 
    }
    
    public void about() {
        log.info("about");  
        
        style.resetTabStyle(2);
        redirectPage(ABOUT_PATH); 
    }
    
    public void externalLinks() {
        
        style.resetTabStyle(3);
        redirectPage(EXTERNAL_LINK_PATH);
    }
    
        
    public void contact() { 
        style.resetTabStyle(4);
        redirectPage(CONTACT_PATH);
    }
    
    public void result() {
        style.resetTabStyle(0);
        redirectPage(RESULT_PATH);
    }
    
    public void scrollToSection() {
        log.info("scrollToSection : {}", clientId);
 
        requestContext = RequestContext.getCurrentInstance();
 
        if (clientId != null) {
            requestContext.scrollTo(clientId);
            clientId = null;
        }
    }
    
    public void scrollToClientId(String id) {
        style.resetTabStyle(2);
        requestContext = RequestContext.getCurrentInstance();   
        requestContext.update("topMenuForm:topmenupanel");  
        clientId = id; 
        redirectPage(ABOUT_PATH);
    }
    
    public void resetTopMenuCSS(int tabIndex) {
        style.resetTabStyle(tabIndex);
        requestContext = RequestContext.getCurrentInstance();   
        requestContext.update("topMenuForm:topmenupanel");  
    } 
}
