package se.nrm.dina.dnakey.logic;
 
/**
 *
 * @author idali
 */
public interface Blaster {
 
    /**
     * Blast from genbank
     * @param fastSequence
     * @return String
     */
    public String remoteGenbankBlast(String fastSequence);
    
}
