/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dnakey.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import se.nrm.dina.dnakey.logic.config.ConfigProperties;
import se.nrm.dina.dnakey.logic.vo.BlastDBVo;

/**
 *
 * @author idali
 */
@ApplicationScoped
@Startup 
@Slf4j
public class BlastDbInfo implements Serializable {
    
    private BlastDBVo blastDbVo;
    private final String NRM_DB = "nrm";
    private final String BOLD_DB = "bold";
    private final String GENBANK_DB = "genbank";
    
    @Inject
    private ConfigProperties config;
    
    public BlastDbInfo() { 
    } 
    
    @PostConstruct
    public void init() {
        log.info("init");
        
        if(blastDbVo == null) {
            String nrmCount = getTotal(NRM_DB);
            String boldCount = getTotal(BOLD_DB);
            String genbankCount = getTotal(GENBANK_DB);

            blastDbVo = new BlastDBVo(nrmCount, boldCount, genbankCount);
        } 
    } 

    public BlastDBVo getBlastDbVo() {
        return blastDbVo;
    }
     
    private String getTotal(String db) {
       
        Process process = null;
        Runtime runtime = Runtime.getRuntime();
        try {
            process = runtime.exec(buildDbInfoCommand(db));
            try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));) {
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.contains("sequences")) {
                        return StringUtils.substringBefore(line, " sequences").trim();
                    }
                }
            }
        } catch (IOException ex) {
            log.error(ex.getMessage());
        } finally {
            if (process != null && process.isAlive()) {
                process.destroy();
            }
        }
        return null; 
    } 
    
    private String buildDbInfoCommand(String db) {
        StringBuilder sb = new StringBuilder();
        sb.append(config.getDbinfoPath());
        sb.append(" -db ");
        sb.append(config.getDbPath());
        sb.append(db);
        sb.append(" -info");
        return sb.toString().trim();
    }
    
}
