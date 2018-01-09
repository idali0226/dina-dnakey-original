package se.nrm.dina.dnakey.logic.metadata;
 
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  
import se.nrm.dina.dnakey.logic.vo.MorphybankImageMetadata;

/**
 *
 * @author idali
 */
public class BlastTabular {
    
    final Logger logger = LoggerFactory.getLogger(this.getClass()); 
 
    private final String subjectId;
    private final String percentage;  
    private final String evalue;
    private final String bitScore;
    private final String[] subjectString; 
    private boolean isActive = false; 
    
    private MorphybankImageMetadata imageMetadata = null; 

    public BlastTabular(final String subjectId, final String percentage, final String evalue, final String bitScore) { 
        this.subjectId = subjectId;
        this.percentage = percentage;  
        this.evalue = evalue;
        this.bitScore = bitScore;  
        this.subjectString = StringUtils.contains(subjectId, "::") ? StringUtils.split(subjectId, "::") : StringUtils.split(subjectId, "|"); 
    } 
    
    public String getTextColor() {
        return Integer.parseInt(percentage) >= 99 ? "black" : "gray";
    }

    public String getAccessionNumber() {
        if(subjectString.length >= 5) {
            return StringUtils.replace(subjectString[0], "null", "");
        }  
        return subjectString.length >= 4  ?  subjectString[3] : "";
    }
 
    public String getTargetMarker() {
        return subjectString.length >= 5 ? subjectString[1] : subjectString[2]; 
    }

    public String getCollectionObjectId() {
        return subjectString[2];
    }

    public String getCatelogNumber() {  
        if(subjectString.length >= 4) {
            return subjectString[3]; 
        }
        return ""; 
    }

    public String getFullName() {
        
        if(subjectString.length >= 5) {
            if(subjectString[4].contains(" | ")) {
                int index = subjectString[4].indexOf("|");
                return subjectString[4].substring(0, index).trim(); 
            } else {
                return subjectString[4];
            } 
        } else {
            return subjectString[1]; 
        } 
    }
    
    public String getBoldId() {
        return subjectString[0] + subjectString[2]; 
    }

//    public String getSwedishName() { 
//        if(subjectString.length >= 5) {
//            if (subjectString[4].contains(" | ")) {
//                int index = subjectString[4].indexOf("|");
//                return HelpClass.swedishCharacterReveaseConvert(subjectString[4].substring(index + 2).trim());
//            }
//        }
//        return "";
//    } 

    public String getBitScore() {
        return bitScore;
    }

    public String getEvalue() {
        return evalue;
    } 
    
    public Long getPercentagelong() {
        return Long.parseLong(percentage.trim());
    }

    public String getPercentage() {
        return percentage;
    }   

    public String getSubjectId() {
        return subjectId;
    }

//    public Collectionobject getCollectionobject() {
//        return collectionobject;
//    }
//
//    public void setCollectionobject(Collectionobject collectionobject) {
//        this.collectionobject = collectionobject;
//    }

    public MorphybankImageMetadata getImageMetadata() {
        return imageMetadata;
    }

    public void setImageMetadata(MorphybankImageMetadata imageMetadata) {
        this.imageMetadata = imageMetadata;
    } 
    
    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
//    public String getActiveLink(String indexCount) {
//        int n = Integer.parseInt(indexCount); 
//        return isActive ? CSSName.getInstance().ACTIVE_ROW : n % 2 == 0 ? CSSName.getInstance().TRANSPARENT_ROW : CSSName.getInstance().GRAY_BLUE_ROW;
//    }
}
