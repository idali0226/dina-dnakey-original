/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dnakey.logic.config;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

/**
 *
 * @author idali
 */
@ApplicationScoped 
@Slf4j
public class ConfigProperties {
    
    private final static String CONFIG_INITIALLISING_ERROR = "config property not initialised";
    
     private String geoDataFilePath; 
     private String blastBasePath;
     private String dbPath;
     private String binPath;
     private String dbinfoPath;
     private String blastnPath; 
     private String fastaFilePath;
     private String solrPath;
     
     public ConfigProperties() { 
     }
     
    @Inject
    public ConfigProperties(@ConfigurationValue("swarm.geodata") String geoDataFilePath,
                            @ConfigurationValue("swarm.blast.base.path") String blastBasePath,
                            @ConfigurationValue("swarm.blast.base.db") String dbPath,
                            @ConfigurationValue("swarm.blast.base.bin.path") String binPath,
                            @ConfigurationValue("swarm.blast.base.bin.dbinfo") String dbinfoPath,
                            @ConfigurationValue("swarm.blast.base.bin.blastn") String blastnPath,
                            @ConfigurationValue("swarm.blast.tempfile.path") String fastaFilePath,
                            @ConfigurationValue("swarm.solr.path") String solrPath) {
        this.geoDataFilePath = geoDataFilePath;
        this.blastBasePath = blastBasePath;
        this.dbPath = dbPath;
        this.binPath = binPath;
        this.dbinfoPath = dbinfoPath;
        this.blastnPath = blastnPath;
        this.fastaFilePath = fastaFilePath;
        this.solrPath = solrPath;
        log.info("test injection : {}", solrPath);
    }
    
    public String getGeoDataFilePath() {
        if(geoDataFilePath == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return geoDataFilePath;
    }
    
    public String getBlastBasePath() {
        if(blastBasePath == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return blastBasePath;
    }
    
    public String getDbPath() {
        if(dbPath == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return dbPath;
    }

    public String getBinPath() {
        if(binPath == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return binPath;
    }

    public String getDbinfoPath() {
        if(dbinfoPath == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return dbinfoPath;
    }

    public String getBlastnPath() {
        if(blastnPath == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return blastnPath;
    }  
    
    public String getFastaFilePath() {
        if(fastaFilePath == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return fastaFilePath;        
    }

    public String getSolrPath() {
        if(solrPath == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return solrPath;
    }
    
    
}
