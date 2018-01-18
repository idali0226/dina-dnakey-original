package se.nrm.dina.dnakey.portal.beans;

import java.io.Serializable; 
import javax.enterprise.context.SessionScoped; 
import javax.inject.Named; 
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.dnakey.portal.util.CSSName;

/**
 *
 * @author idali
 */
@SessionScoped
@Named
@Slf4j
public class StyleBean implements Serializable {
    
    private String tab1 = CSSName.getInstance().getCurrentTab();
    private String tab2;
    private String tab3;
    private String tab4;
      
    private String svbtn;
    private String enbtn; 
      
    public StyleBean() { 
        log.info("StyleBean");
    }
 

    public String getEnbtn() {
        return enbtn;
    }

    public void setEnbtn(String enbtn) {
        this.enbtn = enbtn;
    }

    public String getSvbtn() {
        return svbtn;
    }

    public void setSvbtn(String svbtn) {
        this.svbtn = svbtn;
    }

    public String getTab1() {
        return tab1;
    }

    public void setTab1(String tab1) {
        this.tab1 = tab1;
    }

    public String getTab2() {
        return tab2;
    }

    public void setTab2(String tab2) {
        this.tab2 = tab2;
    }

    public String getTab3() {
        return tab3;
    }

    public void setTab3(String tab3) {
        this.tab3 = tab3;
    }

    public String getTab4() {
        return tab4;
    }

    public void setTab4(String tab4) {
        this.tab4 = tab4;
    }
      
    public void resetTabStyle(int tabIndex) {
         
        tab1 = CSSName.getInstance().getDefaultTab();
        tab2 = CSSName.getInstance().getDefaultTab();
        tab3 = CSSName.getInstance().getDefaultTab();
        tab4 = CSSName.getInstance().getDefaultTab();
        
        switch (tabIndex) {
            case 0:
                tab1 = CSSName.getInstance().getCurrentTab();
                break;
            case 1:
                tab2 = CSSName.getInstance().getCurrentTab();
                break;
            case 2:
                tab3 = CSSName.getInstance().getCurrentTab();
                break;
            case 3:
                tab4 = CSSName.getInstance().getCurrentTab();
                break;
            case 4: break;
            default:
                tab1 = CSSName.getInstance().getCurrentTab();
                break;
        }
    }
}
