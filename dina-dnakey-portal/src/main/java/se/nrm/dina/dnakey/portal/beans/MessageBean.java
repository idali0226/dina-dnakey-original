/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dnakey.portal.beans;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author idali
 */
@Named("message")
@SessionScoped 
@Slf4j
public class MessageBean implements Serializable {
    
    public MessageBean() {
        log.info("MessageBean");
    }
    
    public void createErrorMsgs(String errorTitle, List<String> errors) {
        errors.stream().forEach((error) -> {
            addError(errorTitle, error);
        });
    }
    
    public void addError(String errorTitle, String errorMsg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorTitle, errorMsg));
    }

    public void addInfo(String infoTitle, String infoMsg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, infoTitle, infoMsg));
    }
    
    public void addWarning(String warnTitle, String warnMsg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, warnTitle, warnMsg));
    }
    
}
