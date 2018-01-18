package se.nrm.dina.dnakey.portal.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named; 
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author idali
 */
@Named("imageBeans")
@SessionScoped
@Slf4j
public class ImageBeans implements Serializable {
      
    private final static List<String> IMAGES;   
    
    static { 
        IMAGES = new ArrayList<>();  
        IMAGES.add("/resources/images/randomImages/IMG_3150.JPG");  
        IMAGES.add("/resources/images/randomImages/IMG_3155.JPG");  
        IMAGES.add("/resources/images/randomImages/IMG_3163.JPG");  
        IMAGES.add("/resources/images/randomImages/clytra.jpg");   
        IMAGES.add("/resources/images/randomImages/faglar.jpg"); 
        IMAGES.add("/resources/images/randomImages/grashoppor.jpg");  
        IMAGES.add("/resources/images/randomImages/machaon.jpg");  
        IMAGES.add("/resources/images/randomImages/smaskrake.jpg");  
        IMAGES.add("/resources/images/randomImages/sommargylling_blakraka.jpg");   
        IMAGES.add("/resources/images/randomImages/tNRM50926.jpg");
        IMAGES.add("/resources/images/randomImages/tNRM52466b.jpg");
        IMAGES.add("/resources/images/randomImages/tNRM55280_T4912.jpg");
        IMAGES.add("/resources/images/randomImages/vattenrall.jpg");
    }
    
    public ImageBeans() { 
        log.info("ImageBeans");
    }

    public String getImage() {
        return IMAGES.get(getRandomNumber());
    }
    
    private int getRandomNumber() {
        int low = 0;
        int high = 12;
        return (int)(Math.random() * (high - low)) + low;
    }

    public List<String> getImages() {
        return IMAGES;
    } 
}
