package se.nrm.dina.dnakey.portal.solr;

import se.nrm.dina.dnakey.portal.vo.SolrRecord;
import java.io.IOException;
import java.io.Serializable; 
import java.util.List;  
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;  
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList; 
import se.nrm.dina.dnakey.logic.config.ConfigProperties;
import se.nrm.dina.dnakey.logic.exception.DinaException; 

/**
 *
 * @author idali
 */
@Slf4j
public class SolrClient implements Serializable {
      
    private HttpSolrClient solr;
    private SolrQuery query;
    
    @Inject
    private ConfigProperties config;
    
    public SolrClient() {   
    }
    
    @PostConstruct
    public void init() { 
        log.info("init");
        solr = new HttpSolrClient.Builder(config.getSolrPath()).build();
        query = new SolrQuery(); 
    }
    
     
    public SolrRecord getRecordByCollectionObjectCatalognumber(String catalognumber) { 
        log.info("getRecordByCollectionObjectCatalognumber : {}", catalognumber);

        try { 
            query = new SolrQuery();
            query.setQuery("cn:" + catalognumber);
    
            QueryResponse queryResponse = solr.query(query);
           
            SolrDocumentList documents = queryResponse.getResults();
            long numFound = documents.getNumFound(); 

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
