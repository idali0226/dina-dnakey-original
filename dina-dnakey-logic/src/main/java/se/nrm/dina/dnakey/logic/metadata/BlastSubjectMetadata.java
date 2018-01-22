package se.nrm.dina.dnakey.logic.metadata;
 
import java.util.ArrayList;
import java.util.List;   
import org.apache.commons.lang.StringUtils; 
import se.nrm.dina.dnakey.logic.vo.MorphyBankImage;

/**
 *
 * @author idali
 */
public final class BlastSubjectMetadata { 
    
//    gi|292389084|gb|GU571382|bold|BON074-06.COI-5P|gene|COI|latlon|59.183_N_9.617_E Erithacus rubecula
    
    private final int hitNumber;
    private final String genbankId;
    private final String genbankAccession;
    private final String boldId;
    private final String targetMarker;
    private final String coordinates;
    private final String catalogNumber;
    private final String scientificName; 
    private final int hitLen;  
    private final boolean nrm;
    
    public static final String TEXT_BLACK = "blacktext";
    public static final String TEXT_GRAY = "graytext"; 
    
    private List<MorphyBankImage> mbImages = new ArrayList<>();
    private String mbid;
     
    private final List<BlastSubjectHsp> subjectHspList;
    
    public BlastSubjectMetadata(final int hitNumber, final String genbankId, 
                                final String genbankAccession, final String boldId, 
                                final String targetMarker, final String coordinates, 
                                final String catalogNumber, final String scientificName, 
                                final int hitLen, final List<BlastSubjectHsp> subjectHspList, 
                                final boolean nrm) {
        this.hitNumber = hitNumber;
        this.genbankId = genbankId;
        this.genbankAccession = genbankAccession;
        this.boldId = boldId;
        this.targetMarker = targetMarker;
        this.coordinates = coordinates;
        this.catalogNumber = catalogNumber;
        this.scientificName = scientificName;
        this.hitLen = hitLen;
        this.subjectHspList = subjectHspList; 
        this.nrm = nrm;
    }

    public String getBoldId() {
        return boldId;
    }
    
    public String getBoldIdWithType() {
        return boldId.contains(".") ? StringUtils.substringBefore(boldId, ".") : boldId;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public String getGenbankAccession() {
        return genbankAccession;
    }

    public String getGenbankId() {
        return genbankId;
    }

    public int getHitNumber() {
        return hitNumber;
    }
 
    public String getScientificName() {
        return scientificName;
    }

    public List<BlastSubjectHsp> getSubjectHspList() {
        return subjectHspList;
    }

    public String getTargetMarker() {
        return targetMarker;
    } 
    
    public int getHitLen() {
        return hitLen; 
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public boolean isNrm() {
        return nrm;
    }

    public List<MorphyBankImage> getMbImages() {
        return mbImages;
    }

    public void setMbImages(List<MorphyBankImage> mbImages) {
        this.mbImages = mbImages;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }
 
    public String getTextColor() {
        String color = TEXT_GRAY;
        
        for(BlastSubjectHsp sbjHsp : subjectHspList) {  
            if(sbjHsp.getTextColor().equals(TEXT_BLACK)) {
                return TEXT_BLACK;
            }
        }
        return color;
    }
    
    
    public String getStyle() { 
        for(BlastSubjectHsp sbjHsp : subjectHspList) {  
            if(sbjHsp.getTextColor().equals(TEXT_BLACK)) {
                return TEXT_BLACK;
            }
        }
        return null;
    } 
}
