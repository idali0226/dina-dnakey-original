package se.nrm.dina.dnakey.logic.metadata;

import org.apache.commons.lang.StringUtils;


/**
 *
 * @author idali
 */
public class AlignSequence {
       
    private String start = "";
    private String end;
    private String seq;
      
    public AlignSequence() {
        
    }
        
    public AlignSequence(String start, String end, String seq, String type) {
        
        if(StringUtils.contains(type, "q")) {
            this.start = "Query  " + start;  
        } else if(StringUtils.contains(type, "s")) {
            this.start = "Subjc  " + start;
        } 
        
        this.end = end;
        this.seq = seq;
    }

    public String getEnd() {
        return end;
    }

    public String getSeq() {
        return seq;
    }

    public String getStart() {
        return start;
    } 
}
