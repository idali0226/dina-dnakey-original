package se.nrm.dina.dnakey.portal.solr;

import java.io.IOException;
import java.io.Serializable; 
import java.util.List; 
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;  
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
//import org.apache.solr.client.solrj.SolrQuery;
//import org.apache.solr.client.solrj.SolrServer;
//import org.apache.solr.client.solrj.SolrServerException;
//import org.apache.solr.client.solrj.impl.HttpSolrServer;
//import org.apache.solr.client.solrj.response.QueryResponse;
//import org.apache.solr.common.SolrDocumentList;  
import se.nrm.dina.dnakey.logic.exception.DinaException; 

/**
 *
 * @author idali
 */
@Slf4j
public class SolrClient implements Serializable {
     
    
//    private SolrQuery solrQuery;
//    private final SolrServer solrServer;  
    
    private final HttpSolrClient solr;
    private SolrQuery query;
    
    public SolrClient() { 
         
        
        String urlString = "http://localhost:8983/solr";
        solr = new HttpSolrClient.Builder(urlString).build();
        query = new SolrQuery();
         
//        InetAddress inetAddress;
//        try {
//            inetAddress = InetAddress.getLocalHost(); 
//            solrUrl = BlastHelper.isLocal(inetAddress.getHostName());  
//            
//            logger.info("solr url : {}", solrUrl);
//        } catch (UnknownHostException ex) {
//            logger.error(ex.getMessage());
//        }    
//        solrServer = new HttpSolrServer(solrUrl);
    }

    public SolrRecord getRecordByCollectionObjectCatalognumber(String catalognumber) {
        
        log.info("getRecordByCollectionObjectCatalognumber : {}", catalognumber);

        try { 
            query = new SolrQuery();
            query.setQuery("cn:" + catalognumber);
    
            QueryResponse queryResponse = solr.query(query);
           
            SolrDocumentList documents = queryResponse.getResults();
            long numFound = documents.getNumFound();
            log.info("num found : {}", numFound);

            if(numFound > 0) {  
                List<SolrRecord> resultList = queryResponse.getBeans(SolrRecord.class); 
                return resultList.get(0);
            } else {
                return null;
            }
         } catch (IOException | SolrServerException ex) {
             throw new DinaException(ex);
        }
    } 
}
