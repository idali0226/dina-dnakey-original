package se.nrm.dina.dnakey.logic;

//import java.util.List;
//import se.nrm.dina.dnakey.logic.metadata.BlastMetadata;

/**
 *
 * @author idali
 */
public interface Blaster {
    
//    public String blast(String filepath, String dbname);
//    
//    public BlastMetadata blastXML(String fastafilepath, String dbname);
//    
//    public List<BlastMetadata> multipleBlast(List<String> filePathList, String dbname);
    
    /**
     * Get blast db data
     * @param db
     * @return String
     */
    public String getBlastDbInfo(String db);
    
//    public BlastMetadata remoteBlast(String fastaSequence);
    
    /**
     * Blast from genbank
     * @param fastSequence
     * @return String
     */
    public String remoteGenbankBlast(String fastSequence);
    
}
