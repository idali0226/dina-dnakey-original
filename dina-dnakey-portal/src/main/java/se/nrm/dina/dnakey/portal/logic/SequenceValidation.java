package se.nrm.dina.dnakey.portal.logic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer; 
import javax.ejb.Stateless;
import org.apache.commons.lang.StringUtils; 
import org.biojava3.core.exceptions.CompoundNotFoundError;
import org.biojava3.core.sequence.io.FastaReaderHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author idali
 */ 
@Stateless
public class SequenceValidation implements Serializable {
    
    private final Logger logger = LoggerFactory.getLogger(SequenceValidation.class);
    
    private String errorMsg;
    private List<String> errorMsgs = new ArrayList<>();
    
    public SequenceValidation() {
        
    }
    
    public boolean validate(String sequence) {
        InputStream stream = null;
        try {
            sequence = StringUtils.replaceChars(sequence, "UuRrYySsWwKkMmBbDdHhVv", "n");
            stream = new ByteArrayInputStream(sequence.getBytes("UTF-8"));
            FastaReaderHelper.readFastaDNASequence(stream);  
            return true;
        } catch (Exception ex) { 
            return false;
        } finally {
            try {
                if(stream != null) {
                    stream.close();
                } 
            } catch (IOException ex) {
                logger.error(ex.getMessage());
            }
        } 
    }
 
    
    
    public boolean validate(List<String> seqs) {
        if (seqs != null && !seqs.isEmpty()) {
            if (seqs.size() == 1) {
                return isSequenceValid(seqs.get(0));
            } else {
                return isMultipleSequencesValid(seqs);
            }
        }
        return false;
    }
    
        /**
     * Validates sequence
     * 
     * @param singleSeq - a single fasta sequence
     * 
     * @return true if it is a valid fasta sequence
     */
    private boolean isSequenceValid(String singleSeq) {
         
        errorMsg = null;
        if (singleSeq == null) {
            errorMsg = "Invalid fasta sequence!";   
        } else {
            try {
                validate(singleSeq.trim()); 
            } catch (CompoundNotFoundError ex) {
                errorMsg = ex.getMessage(); 
            } 
        } 
        return errorMsg == null;
    }
    
    
        /**
     * Validates multiple fasta sequences 
     *  
     * @return true is all the fasta sequences are valid
     */
    private boolean isMultipleSequencesValid(List<String> seqs) {
        
        errorMsgs = new ArrayList<>();

        boolean isValid = true; 
        int count = 0; 
        for (String seq : seqs) {
            StringBuilder sb = new StringBuilder();
            count++; 
            
            if (!isSequenceValid(seq)) {
                sb.append("The sequence number ");
                sb.append(count);
                sb.append(" is invalid. ");
                sb.append(errorMsg);
                sb.append("\n");
                isValid = false;
                errorMsgs.add(sb.toString());
            }  
        } 
        return isValid;
    }
    
    public List<String> getErrorMsgs() {
        return errorMsgs;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
    
    
    public static void main(String[] args) {

        String dnaSequence = ">gnl|alu|HSU14574 ***ALU WARNING: Human Alu-Sx subfamily consensus sequence.\n"
                + "GGCCGGGCGCGGTGGCTCACGCCTGTAATCCCAGCACTTTGGGAGGCCGAGGCGGGCGGA\n"
                + "TCACCTGAGGTCAGGAGTTCGAGACCAGCCTGGCCAACATGGTGAAACCCCGTCTCTACT\n"
                + "AAAAATACAAAAATTAGCCGGGCGTGGTGGCGCGCGCCTGTAATCCCAGCTACTCGGGAG\n"
                + "GCTGAGGCAGGAGAATCGCTTGAACCCGGGAGGCGGAGGTTGCAGTGAGCCGAGATCGCG\n"
                + "CCACTGCACTCCAGCCTGGGCGACAGAGCGAGACTCCGTCTCAAAAAAAA\n"
                + ">gnl|alu|HSU14573 ***ALU WARNING: Human Alu-Sq subfamily consensus sequence.\n"
                + "GGCCGGGCGCGGTGGCTCACGCCTGTAATCCCAGCACTTTGGGAGGCCGAGGCGGGTGGA\n"
                + "TCACCTGAGGTCAGGAGTTCGAGACCAGCCTGGCCAACATGGTGAAACCCCGTCTCTACT\n"
                + "AAAAATACAAAAATTAGCCGGGCGTGGTGGCGGGCGCCTGTAATCCCAGCTACTCGGGAG\n"
                + "GCTGAGGCAGGAGAATCGCTTGAACCCGGGAGGCGGAGGTTGCAGTGAGCCGAGATCGCG\n"
                + "CCACTGCACTCCAGCCTGGGCAACAAGAGCGAAACTCCGTCTCAAAAAAAA\n"
                + ">gnl|alu|HSU14572 ***ALU WARNING: Human Alu-Sp subfamily consensus sequence.\n"
                + "GGCCGGGCGCGGTGGCTCACGCCTGTAATCCCAGCACTTTGGGAGGCCGAGGCGGGCGGA\n" 
                + "TCACCTGAGGTCGGGAGTTCGAGACCAGCCTGACCAACATGGAGAAACCCCGTCTCTACT\n"
                + "AAAAATACAAAAATTAGCCGGGCGTGGTGGCGCATGCCTGTAATCCCAGCTACTCGGGAG\n"
                + "GCTGAGGCAGGAGAATCGCTTGAACCCGGGAGGCGGAGGTTGCGGTGAGCCGAGATCGCG\n"
                + "CCATTGCACTCCAGCCTGGGCAACAAGAGCGAAACTCCGTCTCAAAAAAAA";
        
        
        String s = "GGCCGGGCGCGGTGGCTCACGCCTGTAATCCCAGCACTTTGGGAGGCCGAGGCGGGCGGA\n" 
                + "TCACCTGAGGTCGGGAGTTCGAGACCAGCCTGACCAACATGGAGAAACCCCGTCTCTACT\n"
                + "AAAAATACAAAAATTAGCCGGGCGTGGTGGCGCATGCCTGTAATCCCAGCTACTCGGGAG\n"
                + "GCTGAGGCAGGAGAATCGCTTGAACCCGGGAGGCGGAGGTTGCGGTGAGCCGAGATCGCG\n\n"
                + "CCATTGCACTCCAGCCTGGGCAACAAGAGCGAAACTCCGTCTCAAAAAAAA";
        
        StringTokenizer strs = new StringTokenizer(dnaSequence, ">", true);
        
        System.out.println("count: " + strs.countTokens());
        
        while(strs.hasMoreTokens()) {
            
            StringBuilder sb = new StringBuilder();
            sb.append(strs.nextToken());
            sb.append(strs.nextToken());
            System.out.println("strs: " + sb.toString());
        } 
        
         
        
//        String[] list  = StringUtils.splitPreserveAllTokens(dnaSequence, ">");
  
        SequenceValidation validator = new SequenceValidation(); 
        validator.validate(dnaSequence); 
    }
}
