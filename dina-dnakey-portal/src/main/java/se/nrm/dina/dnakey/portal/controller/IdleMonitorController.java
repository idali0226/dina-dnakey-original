package se.nrm.dina.dnakey.portal.controller;

import java.io.IOException; 
import javax.annotation.ManagedBean;  
import javax.faces.context.FacesContext; 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author idali
 */
@ManagedBean
public class IdleMonitorController {

    public void idleListener() throws IOException {
        FacesContext ctx = FacesContext.getCurrentInstance();
     
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        session.invalidate();

        ctx.getExternalContext().redirect("/");
    }
}
