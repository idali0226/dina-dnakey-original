/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.dnakey.logic;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;  
import org.apache.commons.io.IOUtils;
import se.nrm.dina.dnakey.logic.metadata.BlastMetadata; 
import se.nrm.dina.dnakey.logic.metadata.MetadataDataFactory;

/**
 * Run Blast
 * 
 * @author idali
 */
@Slf4j
public class BlastCallableTask implements Callable<BlastMetadata> {
 
    private final String outputStyle2 = " -task blastn -dust no -outfmt 5 -num_alignments 10 -num_descriptions 10 -parse_deflines";       // xml output
    
    private String BLAST_PATH = "/home/admin/wildfly-8.1.0-0/dnakey/bin/blastn";
    private String BLAST_DB_PATH = "/home/admin/wildfly-8.1.0-0/dnakey/db/";
    
    private final String fastafilePath;
    private final String dbName;
    
    private final String blastCommand;
    
    
    public BlastCallableTask(final String fastafilePath, final String dbName, boolean isLocal) { 
        this.dbName = dbName;
        this.fastafilePath = fastafilePath;
        if(isLocal) {
            BLAST_PATH = "/usr/local/blast/bin/blastn";
            BLAST_DB_PATH = "/usr/local/blast/db/";
        }
        blastCommand = buildBlastCommand();
    }
    
    
    /**
     * Call to process blast
     * @return BlastMetadata
     * @throws Exception 
     */
    @Override
    public BlastMetadata call() throws Exception {
        
//        logger.info("call : {}", dbName);
        
        InputStream is = null;
        try {
            Process process = Runtime.getRuntime().exec(blastCommand);
            is = process.getInputStream(); 
            String s = IOUtils.toString(is, "UTF-8"); 
       
            return MetadataDataFactory.getInstance().buildBlastMetadataByString(s); 
        } catch (IOException ex) { 
            log.error(ex.getMessage());
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                log.info(ex.getMessage());
            }
        }
        return null;
    }
    
    
    private String buildBlastCommand() {

        StringBuilder command = new StringBuilder();
        command.append(BLAST_PATH);
        command.append(" -query ");
        command.append(fastafilePath);
        command.append(" -db ");
        command.append(BLAST_DB_PATH);
        command.append(dbName);
        command.append(outputStyle2);
 
        return command.toString();
    }
}
