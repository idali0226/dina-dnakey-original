package se.nrm.dina.dnakey.portal.beans;

import java.io.Serializable; 
import java.util.ArrayList; 
import java.util.List; 
import javax.enterprise.context.SessionScoped;   
import javax.inject.Named; 
import lombok.extern.slf4j.Slf4j;    
import se.nrm.dina.dnakey.logic.metadata.BlastSubjectMetadata;
import se.nrm.dina.dnakey.logic.vo.MorphyBankImage;

/**
 *
 * @author idali
 */
@Named("galleriaBean")
@SessionScoped 
@Slf4j
public class GalleriaBean implements Serializable {
     
    private List<MorphyBankImage> groupImages;
    private String mbid;
    private String catalogNumber;
    private String scientificName;
    
    public GalleriaBean() {   
        log.info("GalleriaBean");
    }
      
    public void imageSwitch(BlastSubjectMetadata metadata) { 
        log.info("imageSwitch : {}", metadata);
          
        groupImages = new ArrayList<>();
        groupImages = metadata.getMbImages(); 
        mbid = metadata.getMbid();  
        catalogNumber = metadata.getCatalogNumber();  
        scientificName = metadata.getScientificName(); 
    }

    public List<MorphyBankImage> getGroupImages() { 
        return groupImages;
    }

    public String getMbid() {
        return mbid;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public String getScientificName() {
        return scientificName;
    }
    
    
}
