package se.nrm.dina.dnakey.logic.metadata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;    
import se.nrm.dina.dnakey.logic.exception.DnaKeyException; 
import se.nrm.dina.dnakey.logic.xml.XmlHandler;
import se.nrm.dina.dnakey.logic.xml.XmlHandlerImpl;
/**
 *
 * @author idali
 */
public class DataFactory {
    
    private final String BLASTN = "BLASTN";
    private final String BLAST_N = "blastn";
    private final String DATABASE = "Database";
    private final String QUERY = "Query=";
    private final String LENGTH = "Length=";
    private final String REFERENCE = "Reference:";

    final Logger logger = LoggerFactory.getLogger(this.getClass()); 
    private final XmlHandler xmlHandler = new XmlHandlerImpl();

    public static DataFactory getInstance() {
        return new DataFactory();
    }
    
    public BlastMetadata getGenbankBlastMetadataByInputStream(InputStream inputStream) {
        logger.info("getGenbankBlastMetadataByInputStream: {}", inputStream);

        Document dom = null;
        try {
            dom = xmlHandler.parseXml(inputStream);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new DnaKeyException("Parse xml failed ", e);
        }
        BlastXMLData data = new BlastXMLData(dom);
        return data.getGenbankMetadata();
    }

    /**
     * This method take an InputStream for all the metadata and return BlastMetadata object
     * @param inputStream
     * @return BlastMetadata
     */
    public BlastMetadata getBlastMetadataByInputStream(InputStream inputStream) {

        logger.info("inputstream: {}", inputStream);

        Document dom = null;
        try {
            dom = xmlHandler.parseXml(inputStream);
        } catch (ParserConfigurationException e) {
            throw new DnaKeyException("Parse xml failed ", e);
        } catch (SAXException e) {
            throw new DnaKeyException("Parse xml failed ", e);
        } catch (IOException e) {
            throw new DnaKeyException("Parse xml failed ", e);
        }
        BlastXMLData data = new BlastXMLData(dom);
        return data.getMetadata();
    }

    /**
     * This method take a document for all the metadata and return XPathPackageMetadata object
     * @param document
     * @return XPathPackageMetadata
     */
    public BlastMetadata getBlastMetadataByDocument(Document document) {

        logger.info("getBlastMetadataByDocument");

        BlastXMLData data = new BlastXMLData(document);
        return data.getMetadata();
    }

    public Document getDocumentByInputstream(InputStream inputStream) {
        try {
            return xmlHandler.parseXml(inputStream);
        } catch (ParserConfigurationException | SAXException ex) {
            logger.error(ex.getMessage());
        } catch (IOException ex) {
            logger.info(ex.getMessage());
        }
        return null;
    }

    /**
     * Convert InputStream into BlastTabularMetadata object
     * 
     * @param is
     * 
     * @return BlastTabularMetadata
     * @throws java.io.IOException
     */
    public BlastTabularMetadata getBlastTabularMetadataByInputStream(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        List<BlastTabular> resultList = new ArrayList<>();

        String database = "";
        String query = "";
        String blast = "";
        String reference = "";
        String subjectId = "";
        String score = "";
        String evalue = "";
        String percentage = "";
        boolean isNewResult = false;
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            if(StringUtils.startsWith(line, ">")) { 
                isNewResult = true;
                subjectId = StringUtils.substringAfter(line, "> "); 
            }
            if(StringUtils.contains(line, "Score = ")) {
                score = StringUtils.substringBetween(line, "Score = ", " bits");
                evalue = StringUtils.substringAfterLast(line, "= ");
            }
            if(StringUtils.contains(line, "Identities = ")) {
                percentage = StringUtils.substringBetween(line, "(", "%");              
            }
            if (StringUtils.contains(line, BLASTN)) {
                blast = line;
            }
            if (StringUtils.contains(line, DATABASE)) {
                database = StringUtils.substringAfter(line, ": ");
            }
            if (StringUtils.contains(line, QUERY)) {
                query = StringUtils.substringAfter(line, "= ");
            }
            if (StringUtils.contains(line, REFERENCE)) {
                reference = line;
            }
            if(StringUtils.startsWith(line, "Sbjct") && isNewResult) {
                BlastTabular tabular = new BlastTabular(subjectId, percentage, evalue, score);
                resultList.add(tabular);
                isNewResult = false;
            }
        }
        
        
        return new BlastTabularMetadata(blast, database, query, reference, resultList);
    } 
}
