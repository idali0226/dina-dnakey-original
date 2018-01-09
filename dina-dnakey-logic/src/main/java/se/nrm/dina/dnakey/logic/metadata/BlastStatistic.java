package se.nrm.dina.dnakey.logic.metadata;
  
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import se.nrm.dina.dnakey.logic.metadata.xml.DomCarrier;

/**
 *
 * @author idali
 */
@Slf4j
public class BlastStatistic {
      
    private final DomCarrier domCarrier; 

    public BlastStatistic(DomCarrier domCarrier) { 
        this.domCarrier = domCarrier;
    }

    public String getDbNum() {  
        String xPathExpression = "/BlastOutput/BlastOutput_iterations/Iteration/Iteration_stat/Statistics/Statistics_db-num"; 
        return getValueByXPathExpression(xPathExpression);
    }
    
    public String getDbLen() { 
        String xPathExpression = "/BlastOutput/BlastOutput_iterations/Iteration/Iteration_stat/Statistics/Statistics_db-len"; 
        return getValueByXPathExpression(xPathExpression);
    }

    private String getValueByXPathExpression(String xPathExpression) {
        return domCarrier.getStringByXpath(xPathExpression);
    }
}
