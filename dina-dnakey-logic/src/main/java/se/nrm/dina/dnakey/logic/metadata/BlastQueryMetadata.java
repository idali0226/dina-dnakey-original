package se.nrm.dina.dnakey.logic.metadata;
  
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nrm.dina.dnakey.logic.metadata.xml.DomCarrier;
 

/**
 *
 * @author idali
 */
public class BlastQueryMetadata {
     
    private final DomCarrier domCarrier; 
    
     
    
    public BlastQueryMetadata(DomCarrier domCarrier) { 
        this.domCarrier = domCarrier;
    }
    
    public String getQuery() {
        final String xPathExpression_1 = "/BlastOutput/BlastOutput_query-ID";
        final String xPathExpression_2 = "/BlastOutput/BlastOutput_query-def";
        return getValueByXPathExpression(xPathExpression_1) + " " + getValueByXPathExpression(xPathExpression_2);
    }
    
    public String getQueryLen() {
        final String xPathExpression = "/BlastOutput/BlastOutput_query-len";
        return getValueByXPathExpression(xPathExpression);
    } 
    
    private String getValueByXPathExpression(String xPathExpression) {
        return domCarrier.getStringByXpath(xPathExpression);
//        try {
//            
//        } catch (XPathExpressionException ex) { 
//            logger.error(ex.getMessage());
//            return "";
//        }
    } 
}
