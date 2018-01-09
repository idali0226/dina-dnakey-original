package se.nrm.dina.dnakey.logic.vo;

import java.util.ArrayList;
import java.util.List;   
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;  
import se.nrm.dina.dnakey.logic.exception.DinaException;
import se.nrm.dina.dnakey.logic.metadata.xml.DomCarrier;
import se.nrm.dina.dnakey.logic.util.HelpClass;

/**
 *
 * @author idali
 */
public final class MorphybankImageMetadata {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final DomCarrier domCarrier;
    
    private List<MorphyBankImage> images;
     
    public MorphybankImageMetadata(Document document) {
        this.domCarrier = new DomCarrier(document);
        createMorphybankImages();
    }
    
    public List<MorphyBankImage> getImages() {
        return images;
    }
    
    
    

    void createMorphybankImages() {
        images = new ArrayList<>();
        String keyword = getKeyword();
        List<String> imageIds = getMorphybankImageId();
         
        for(String imageId : imageIds) { 
            String morphybankImageUrl = HelpClass.getMorphybankImageURLById(imageId);
            String morphyBankThumbUrl = HelpClass.getMorphybankThumbURLById(imageId);
            images.add(new MorphyBankImage(Integer.parseInt(imageId), morphybankImageUrl, "", "", keyword, morphyBankThumbUrl));
        }    
    }
    
    public String getKeyword() {
        try {
            final String xPathExpression = "/mbresponse/keywords";
            return domCarrier.getStringByXpath(xPathExpression);
        } catch (DinaException ex) { 
            return "";
        }
    }

    public List<String> getMorphybankImageId() {

        List<String> imageIds = new ArrayList<>();
        try {
            NodeList list = domCarrier.getListByXpath("/mbresponse/id");
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                imageIds.add(node.getTextContent());
            }
        } catch (DinaException ex) { 
            return null;
        }
        return imageIds;
    } 
}
