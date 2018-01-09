package se.nrm.dina.dnakey.logic.xml;

import java.io.ByteArrayOutputStream;
import java.io.File; 
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;  
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document; 
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import se.nrm.dina.dnakey.logic.exception.DinaException;

/**
 * Implementation of XmlHandler, containing helper methods for loading and
 * parsing mainly xml data
 */
public class XmlHandlerImpl implements XmlHandler {

    private final Logger logger = LoggerFactory.getLogger(XmlHandlerImpl.class);

    /**
     * Reads a java.io.File and returns it as a byte array
     *
     * @param xmlFile, the java.io.File to be read
     * @return the file as a byte array 
     */
    @Override
    public byte[] readFile(File xmlFile) {

        try {
            return FileUtils.readFileToByteArray(xmlFile);
        } catch (IOException ex) {
        }
        return null;

//        FileInputStream is = null;
//        ByteBuffer buffer = null;
//        try {
//            logger.info("readFile");
//            is = new FileInputStream(xmlFile);
//            FileChannel channel = is.getChannel();
//
//            buffer = ByteBuffer.allocate((int) channel.size()); 
//            channel.read(buffer);
//            return buffer.array();
//        } catch (FileNotFoundException ex) {
//        } catch (IOException ex) { 
//        } finally {
//            try {
//                if (is != null) {
//                    is.close();
//                }
//                if(buffer != null) {
//                    buffer.clear();
//                }
//            } catch (IOException ex) { 
//            }
//        }
//        return null;
        
    }

    /**
     * Writes a byte array to the java.io.File supplied
     *
     * @param xmlFile the file to write to
     * @param bytes the data to be written 
     */
    @Override
    public void writeFile(File xmlFile, byte[] bytes) {
        logger.info("writeFile: {}", xmlFile.getAbsolutePath());

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(xmlFile); 
            try (FileChannel channel = os.getChannel()) {
                channel.write(ByteBuffer.wrap(bytes));
            } catch (IOException ex) { 
            }
        } catch (FileNotFoundException ex) { 
        } finally {
            if(os != null) {
                try {
                    os.close();
                } catch (IOException ex) { 
                }
            }
        }
    }
 

    /**
     * Creates a w3c DOM Document from an input stream
     *
     * @param inputStream the input stream to be parsed
     * @return an w3c DOM Document
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    @Override
    public Document parseXml(InputStream inputStream)
            throws ParserConfigurationException, SAXException, IOException {

        logger.info("parseXml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();

        docBuilder.setEntityResolver((String publicId, String systemId) -> new InputSource(new StringReader("")));

        return docBuilder.parse(inputStream);
    }

    /**
     * Creates a w3c DOM Document from String
     * 
     *
     * @param xmlString
     * @return an w3c DOM Document
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    @Override
    public Document parseXml(String xmlString)
            throws ParserConfigurationException, SAXException, IOException {

        logger.info("parseXml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();

        docBuilder.setEntityResolver((String publicId, String systemId) -> new InputSource(new StringReader("")));

        return docBuilder.parse(new InputSource(new StringReader(xmlString)));
    }

    /**
     * Creates a byte array from a w3c DOM Document
     *
     * @param document the w3c DOM Document to be serialized
     * @return the document as a byte array
     */
    @Override
    public byte[] serializeXml(Document document) {
        logger.info("serializeXml");

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        
        ByteArrayOutputStream byteArrayOutputStream = null; 
        try {
            Transformer idTrans = transformerFactory.newTransformer();
            byteArrayOutputStream = new ByteArrayOutputStream();
            Source sourceOut = new DOMSource(document);
            Result targetOut = new StreamResult(byteArrayOutputStream);

            idTrans.transform(sourceOut, targetOut); 
            return byteArrayOutputStream.toByteArray();
        } catch (TransformerConfigurationException ex) {
            throw new DinaException();
        } catch (TransformerException ex) {
            throw new DinaException();
        } finally { 
            if(byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException ex) { 
                }
            } 
        } 
    }
}
