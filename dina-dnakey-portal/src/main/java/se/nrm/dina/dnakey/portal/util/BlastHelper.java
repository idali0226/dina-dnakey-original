package se.nrm.dina.dnakey.portal.util;
 
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer; 
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;  
import se.nrm.dina.dnakey.logic.exception.DnaKeyException;

/**
 *
 * @author idali
 */
@Slf4j
public class BlastHelper { 
      
    private static Timestamp timestamp;
    
    public BlastHelper() { 
    }
    
//    private static String createFastaFile(String seq) {
//        try {  
//            return FileHandler.createFastaFile(seq);
//        } catch (IOException ex) {
//            return null;
//        }
//    }
 
    public static List<String> prepareSequenceList1(String sequence) {
        List<String> sequences = new ArrayList<>();

        int count = 1;
        StringTokenizer tokens = new StringTokenizer(sequence, ">", true); 
        if (tokens.countTokens() <= 1) {                            // one sequence
            List<String> strings = getTextList(sequence);
            
            for(String string : strings) { 
                if(!string.contains(">")) {                         // if sequence has no head
                    string = addSequenceHeader(string);
                }   
                sequences.add(string);
                count++;
                if(count == 100) {
                    break;
                }
            } 
            return sequences; 
        }
        
        // multiple sequences
        StringBuilder sb = new StringBuilder();
        List<String> strings;
        while (tokens.hasMoreTokens()) {  
            sb.append(tokens.nextToken());
            sb.append(tokens.nextToken()); 
            
            strings = getTextList(sb.toString()); 
            sb = new StringBuilder();
            for(String string : strings) { 
                if (!StringUtils.isEmpty(string)) {
                     if(!string.contains(">")) {                         // if sequence has no head
                        string = addSequenceHeader(string);
                    }  
                    sequences.add(string); 
                } 
            } 
        }
        
        
        return sequences;
    }
    
    
     
    private static String addSequenceHeader(String string) {
        timestamp = new Timestamp(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder(">");
        sb.append(timestamp);
        sb.append("\n");
        sb.append(string);
        return sb.toString().trim();
    }
    
    private static List<String> getTextList(String value) {
        List<String> textlist = new ArrayList<>();
 
        try {
            InputStream is = new ByteArrayInputStream(value.getBytes());
            BufferedReader input = new BufferedReader(new InputStreamReader(is));
            String line; 
            StringBuilder sbLine = new StringBuilder();
            String stLine = "";
            while (null != (line = input.readLine())) {
                if(!isEmpty(line)) {
                    sbLine.append(line); 
                    if (line.contains(">")) {
                        sbLine.append("\n"); 
                    }  
                }
                stLine = sbLine.toString().trim();
                if (isEmpty(line) && (!StringUtils.isEmpty(stLine))) { 
                    textlist.add(stLine); 
                    stLine = "";
                    sbLine = new StringBuilder();
                }    
            } 
            textlist.add(stLine);
        } catch (UnsupportedEncodingException e) {
             throw new DnaKeyException(e.getMessage());
        } catch (IOException e) { 
            throw new DnaKeyException(e.getMessage());
        }
        return textlist;
    }
    
    private static boolean isEmpty(String line) {

        int start = 0;
        while (start < line.length()) {
            if (ConstantString.getInstance().getWhiteSpaceChars().indexOf(line.charAt(start)) == -1) {
                break;
            }
            start++;
        }
        line = line.substring(start);  
        return line.length() == 0;
    }
    
    public static String isLocal(String hostname) {
        
        log.info("hostname : {}", hostname);
        return hostname.toLowerCase().contains("ida") ? "http://localhost:8983/solr/" : "http://dina-db:8983/solr/";
    }
 }
