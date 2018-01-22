package se.nrm.dina.dnakey.portal.logic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;  
import java.util.concurrent.atomic.AtomicInteger; 
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;  
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
//import org.biojava3.core.exceptions.CompoundNotFoundError;
//import org.biojava3.core.sequence.io.FastaReaderHelper; 
import se.nrm.dina.dnakey.portal.util.ConstantString;

/**
 *
 * @author idali
 */ 
@Slf4j
public class SequenceValidation implements Serializable {
     
    private final String REPLACE_CHARS = "UuRrYySsWwKkMmBbDdHhVv";
    private final String REPLACE_CHAR = "n";
    private String errorMsg;
    private List<String> errorMsgs = new ArrayList<>();
    
    public SequenceValidation() {
        log.info("SequenceValidation");
    }
    
    public boolean validate(String sequence) {
        InputStream stream = null;
        try {
            sequence = StringUtils.replaceChars(sequence, REPLACE_CHARS, REPLACE_CHAR);
            stream = new ByteArrayInputStream(sequence.getBytes(ConstantString.getInstance().getUtf8()));
            FastaReaderHelper.readFastaDNASequence(stream);  
            return true;
        } catch (IOException ex) { 
            return false;
        } finally {
            try {
                if(stream != null) {
                    stream.close();
                } 
            } catch (IOException ex) {
                log.error(ex.getMessage());
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
        if (singleSeq == null || singleSeq.trim().isEmpty()) {
            errorMsg = "Invalid fasta sequence!";   
        } else {
//            try {
                validate(singleSeq.trim()); 
//            } catch (CompoundNotFoundError ex) {
//                errorMsg = ex.getMessage(); 
//            } 
        } 
        return errorMsg == null;
    }
    
    
    private String buildErrorMessage(int index) {
        StringBuilder sb = new StringBuilder();
        sb.append("The sequence number ");
        sb.append(index);
        sb.append(" is invalid. ");
        sb.append(errorMsg);
        sb.append("\n");

        return sb.toString();
    }

    /**
     * Validates multiple fasta sequences 
     *  
     * @return true is all the fasta sequences are valid
     */
    private boolean isMultipleSequencesValid(List<String> seqs) {

        errorMsgs = new ArrayList<>();
        
        AtomicInteger atomicInteger = new AtomicInteger(1);
 
        seqs.stream().forEach(s -> {
            int count = atomicInteger.getAndIncrement(); 
                
            if(!isSequenceValid(s)) { 
                errorMsgs.add(buildErrorMessage(count));  
            }
        }); 
        return errorMsgs.isEmpty();
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
