package se.nrm.dina.dnakey.portal.util;

/**
 *
 * @author idali
 */
public class CSSName {
      
    public final String DIV_VISIBLE = "divvisible";
    public final String DIV_INVISIBLE = "divinvisible";  
    public final String TEXT_GREEN = "greentext";
    public final String TEXT_RED = "redtext";
    
    public final String TEXT_BLACK = "blacktext";
    public final String TEXT_GRAY = "graytext";
    
    public final String ACTIVE_ROW = "activeRow";
    public final String TRANSPARENT_ROW = "transparentRow";
    public final String GRAY_BLUE_ROW = "grayBlueRow";
    
    
    public final String ACTIVE_SV_FLAG = "swedishbuttonact";
    public final String ACTIVE_EN_FLAG = "enghishbuttonact"; 
    public final String INACTIVE_SV_FLAG = "swedishbutton";
    public final String INACTIVE_EN_FLAG = "enghishbutton"; 

    
    public final String ACTIVE_LINK = "activelink";
    public final String INACTIVE_LINK = "inactivelink"; 
    
    private final String CURRENT_TAB = "current";
    private final String DEFAULT_TAB = "";
    
    
    private static CSSName instance = null;
    
    public static synchronized CSSName getInstance() {
        if (instance == null) {
            instance = new CSSName();
        }
        return instance;
    } 
    
    public String getCurrentTab() {
        return CURRENT_TAB;
    }
    
    public String getDefaultTab() {
        return DEFAULT_TAB;
    }
}
