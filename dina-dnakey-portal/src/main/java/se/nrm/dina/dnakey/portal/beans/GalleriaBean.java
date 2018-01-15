package se.nrm.dina.dnakey.portal.beans;

import java.io.Serializable; 
import java.util.ArrayList; 
import java.util.List; 
import javax.enterprise.context.SessionScoped;   
import javax.inject.Named; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;    
import se.nrm.dina.dnakey.logic.metadata.BlastSubjectMetadata;
import se.nrm.dina.dnakey.logic.vo.MorphyBankImage;
/**
 *
 * @author idali
 */
@Named("galleriaBean")
@SessionScoped 
public class GalleriaBean implements Serializable {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass()); 
        
    private List<MorphyBankImage> groupImages;
    private String mbid;
    private String catalogNumber;
    private String scientificName;
    
    public GalleriaBean() {   
        groupImages = new ArrayList<>();
    }
      
    public void imageSwitch(BlastSubjectMetadata metadata) {
        
        logger.info("imageSwitch : {}", metadata);
          
        groupImages = metadata.getMbImages();
        groupImages.stream().forEach((image) -> {
            logger.info("image : {}", image);
        });
        
        
        
        mbid = metadata.getMbid();
        logger.info("mbid : {}", mbid);
        
        
        catalogNumber = metadata.getCatalogNumber();
        logger.info("catalogNumber : {}", catalogNumber);
        
        
        scientificName = metadata.getScientificName();
        logger.info("scientificName : {}", scientificName);
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
