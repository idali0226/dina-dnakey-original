package se.nrm.dina.dnakey.portal.controller;

import java.io.File; 
import java.io.FileNotFoundException; 
import java.io.PrintWriter;  
import java.io.Serializable; 
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;  
import se.nrm.dina.dnakey.logic.config.ConfigProperties;
import se.nrm.dina.dnakey.portal.util.UUIDGenerator;

/**
 *
 * @author idali
 */
@Slf4j
public class FileHandler implements Serializable {
  
    private String fastaTempPath;
    private final String FASTA_FILE_TYPE = ".fa";
    
    @Inject
    private ConfigProperties config;
    
    public FileHandler() { 
        log.info("FileHandler");
    }
    
    @PostConstruct
    public void init() {  
        fastaTempPath = config.getFastaFilePath();
    }
    

    public String createFastaFile(String sequence)  { 
        log.info("createFastaFile");

        String tempFileName = String.valueOf(UUIDGenerator.generateUUID()) + FASTA_FILE_TYPE; 
        File fastaFile = new File(fastaTempPath, tempFileName);
 
        try (PrintWriter out = new PrintWriter(fastaFile)) {
            out.println(sequence);
            out.flush();
        } catch (FileNotFoundException ex) {
            log.error(ex.getMessage());
        }  
        return fastaFile.getPath();
    }

    public void deleteTempFiles(List<String> filesPath) {
        if (!filesPath.isEmpty()) {
            filesPath.stream()
                    .forEach(path -> {
                        File file = new File(path); 
                        if (file.exists()) {
                            file.delete();
                        } 
                    });
        }
    }
}
