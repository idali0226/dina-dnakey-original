package se.nrm.dina.dnakey.portal.util;

import java.io.File; 
import java.io.IOException;
import java.io.PrintWriter;  
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author idali
 */
public class FileHandler {

    private final static Logger logger = LoggerFactory.getLogger(FileHandler.class);
      
    private static String BLAST_DB_TEMP_PATH;
    private static final String FASTA_FILE_TYPE = ".fa";

    public static String createFastaFile(String sequence, String servername) throws IOException {
        
        logger.info("createFastaFile - servername : {}", servername);

        String tempFileName = String.valueOf(UUIDGenerator.generateUUID()) + FASTA_FILE_TYPE;
        if(servername.toLowerCase().contains("ida") || servername.toLowerCase().contains("localhost")) {
            BLAST_DB_TEMP_PATH = "/usr/local/blast/tempfastafiles";
        } else {
            BLAST_DB_TEMP_PATH = "/home/admin/wildfly-8.1.0-0/dnakey/data/tempfastafiles";
        } 
        File fastaFile = new File(BLAST_DB_TEMP_PATH, tempFileName);
        
        logger.info("file : " + fastaFile.getAbsolutePath());
  
        try (PrintWriter out = new PrintWriter(fastaFile)) {   
            out.println(sequence);
            out.flush();
        } 
        return fastaFile.getPath();
    } 
}
