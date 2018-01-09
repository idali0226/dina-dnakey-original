package se.nrm.dina.dnakey.logic.metadata.xml;
   
import java.io.IOException;  
import javax.xml.parsers.ParserConfigurationException;  
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;       
import se.nrm.dina.dnakey.logic.exception.InvalidMetadataException;
import se.nrm.dina.dnakey.logic.vo.MorphybankImageMetadata;
import se.nrm.dina.dnakey.logic.xml.XmlHandler;
import se.nrm.dina.dnakey.logic.xml.XmlHandlerImpl;

/** 
 *
 * @author idali
 */
public class DataFactory {

    final Logger logger = LoggerFactory.getLogger(this.getClass()); 
    private final XmlHandler xmlHandler = new XmlHandlerImpl();

    public static DataFactory getInstance() {
        return new DataFactory();
    } 
 
//    public Document getDocumentByInputstream(InputStream inputStream) {
//        try {
//            return xmlHandler.parseXml(inputStream);
//        } catch (ParserConfigurationException | SAXException ex) {
//            logger.error(ex.getMessage());
//        } catch (IOException ex) {
//            logger.info(ex.getMessage());
//        }
//        return null;
//    }
     
     
//    public Document getDocumentByFileName(String filePath, String fileName) {
//        logger.info("getDocumentByFileName - filePath : {}, fileName : {}", filePath, fileName);
//        
//        Document dom = null;
//        InputStream inputStream = null;
//        try {  
//            File file = new File(filePath, fileName); 
//              
//            inputStream = new FileInputStream(file);
//
//            dom = xmlHandler.parseXml(inputStream);
//        } catch (ParserConfigurationException | SAXException | IOException ex) {
//            logger.error(ex.getMessage());
//        } finally {
//            try {
//                if(inputStream != null) {
//                    inputStream.close();
//                } 
//            } catch (IOException ex) {
//                 logger.error(ex.getMessage());
//            }
//        }  
//        return dom;
//    }
    
     
    public MorphybankImageMetadata getMorphybankImages(String xmlString) {

        Document dom = null;
        try {
            dom = xmlHandler.parseXml(xmlString);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new InvalidMetadataException("Parse xml failed ", e);
        }

        return new MorphybankImageMetadata(dom);
    } 
}
