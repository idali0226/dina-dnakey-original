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
 
    private String blastCommand;
    private StringBuilder command;
     
    public BlastCallableTask() {
        
    }
     
    public BlastCallableTask(final String fastafilePath, final String dbName, String blastPath, String blastDbPath) { 
        log.info("BlastCallableTask");
  
        blastCommand = buildBlastCommand(fastafilePath, dbName, blastPath, blastDbPath);   
    } 
    
    
    /**
     * Call to process blast
     * @return BlastMetadata 
     */
    @Override
    public BlastMetadata call() {
        
//        logger.info("call : {}", dbName);
 
        InputStream is = null;
        try {
            Process process = Runtime.getRuntime().exec(blastCommand);
            is = process.getInputStream(); 
            String s = IOUtils.toString(is, "UTF-8"); 
              
            return MetadataDataFactory.getInstance().buildBlastMetadataByJson(s); 
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
 
    private String buildBlastCommand(String fastafilePath, String dbName, String blastPath, String blastDbPath) {
        
        command = new StringBuilder();
        command.append(blastPath);
        command.append(" -query ");
        command.append(fastafilePath);
        command.append(" -db ");
        command.append(blastDbPath);
        command.append(dbName);
        command.append(outputStyle2);
 
        return command.toString();
    }
}
