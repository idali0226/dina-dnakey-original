package se.nrm.dina.dnakey.portal.solr;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import javax.ejb.Stateless;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import se.nrm.dina.dnakey.logic.exception.DinaException;
import se.nrm.dina.dnakey.portal.util.BlastHelper;

/**
 *
 * @author idali
 */
@Stateless
public class SolrClient implements Serializable {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private SolrQuery solrQuery;
    private final SolrServer solrServer;  
    
    public SolrClient() { 
        
        String solrUrl = "http://localhost:8983/solr";
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getLocalHost(); 
            solrUrl = BlastHelper.isLocal(inetAddress.getHostName());  
            
            logger.info("solr url : {}", solrUrl);
        } catch (UnknownHostException ex) {
            logger.error(ex.getMessage());
        }    
        solrServer = new HttpSolrServer(solrUrl);
    }

    public SolrRecord getRecordByCollectionObjectCatalognumber(String catalognumber) {
        
        logger.info("getRecordByCollectionObjectCatalognumber : {}", catalognumber);

        try {
            solrQuery = new SolrQuery();
            solrQuery.setQuery("cn:" + catalognumber);
    
            QueryResponse queryResponse = solrServer.query(solrQuery); 
            SolrDocumentList documents = queryResponse.getResults();
            long numFound = documents.getNumFound();

            if(numFound > 0) {  
                List<SolrRecord> resultList = queryResponse.getBeans(SolrRecord.class); 
                return resultList.get(0);
            } else {
                return null;
            }
        } catch (SolrServerException ex) { 
            throw new DinaException(ex);
        }
    }
    
}
