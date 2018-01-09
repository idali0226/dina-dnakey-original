package se.nrm.dina.dnakey.logic.metadata;

import com.ximpleware.AutoPilot;
import com.ximpleware.EOFException;
import com.ximpleware.EncodingException;
import com.ximpleware.EntityException;
import com.ximpleware.NavException;
import com.ximpleware.ParseException;
import com.ximpleware.VTDGen; 
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException; 
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author idali
 */
public class MetadataDataFactory implements Serializable {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private final static String BLASTOUTPUT_PROGRAM = "/BlastOutput/BlastOutput_program";
    private final static String BLASTOUTPUT_VISION = "/BlastOutput/BlastOutput_version";
    private final static String BLASTOUTPUT_REFERENCE = "/BlastOutput/BlastOutput_reference";
    private final static String BLASTOUTPUT_DATABASE = "/BlastOutput/BlastOutput_db";
    private final static String BLASTOUTPUT_QUERY_ID = "/BlastOutput/BlastOutput_query-ID";
    private final static String BLASTOUTPUT_QUERY_DEF = "/BlastOutput/BlastOutput_query-def";
    private final static String BLASTOUTPUT_QUERY_LEN = "/BlastOutput/BlastOutput_query-len";
    
    private final static String BLASTOUTPUT_HITS = "/BlastOutput/BlastOutput_iterations/Iteration/Iteration_hits/Hit";
//    private final static String BLASTOUTPUT_HITS_ACCESSION = "Hit_accession"; 
    private final static String BLASTOUTPUT_HITS_NUM = "Hit_num";
    private final static String BLASTOUTPUT_HITS_DEF = "Hit_def";
    private final static String BLASTOUTPUT_HITS_LEN = "Hit_len";
    private final static String BLASTOUTPUT_HITS_HSPS = "Hit_hsps";
    private final static String BLASTOUTPUT_HITS_HSP = "Hsp";
    private final static String BLASTOUTPUT_HITS_SCORE = "Hsp_score";
    private final static String BLASTOUTPUT_HITS_BITSCORE = "Hsp_bit-score";
    private final static String BLASTOUTPUT_HITS_EVALUE = "Hsp_evalue";
    private final static String BLASTOUTPUT_HITS_IDENTITY = "Hsp_identity";
    private final static String BLASTOUTPUT_HITS_ALIGN_LEN= "Hsp_align-len";
    private final static String BLASTOUTPUT_HITS_QSEQ = "Hsp_qseq";
    private final static String BLASTOUTPUT_HITS_HSEQ = "Hsp_hseq";
    private final static String BLASTOUTPUT_HITS_MIDLINE = "Hsp_midline"; 
    private final static String BLASTOUTPUT_HITS_POSITIVE = "Hsp_positive";
    private final static String BLASTOUTPUT_HITS_GAPS = "Hsp_gaps"; 
            
    
    private final static String BLASTOUTPUT_HITS_QUERY_FROM = "Hsp_query-from";
    private final static String BLASTOUTPUT_HITS_QUERY_TO = "Hsp_query-to";
    private final static String BLASTOUTPUT_HITS_HIT_FROM = "Hsp_hit-from";
    private final static String BLASTOUTPUT_HITS_HIT_TO = "Hsp_hit-to";
    
    private final static String BLASTOUTPUT_HITS_QUERY_FRAME = "Hsp_query-frame";
    private final static String BLASTOUTPUT_HITS_HIT_FRAME = "Hsp_hit-frame";
    
    private final static String BLASTOUTPUT_STATISTIC_NUMBER = "/BlastOutput/BlastOutput_iterations/Iteration/Iteration_stat/Statistics/Statistics_db-num"; 
    private final static String BLASTOUTPUT_STATISTIC_LENGTH = "/BlastOutput/BlastOutput_iterations/Iteration/Iteration_stat/Statistics/Statistics_db-len"; 
    
    
    private AutoPilot ap; 
    
    public static MetadataDataFactory getInstance() {
        return new MetadataDataFactory();
    }
    
    private String getValueByPath(VTDNav vn, String path) throws XPathParseException, XPathEvalException, NavException {
        ap = new AutoPilot(vn);
        ap.selectXPath(path);
        while (ap.evalXPath() != -1) {
            int t = vn.getText();                                               // get the index of the text (char data or CDATA)
            if (t != -1) {
                return vn.toNormalizedString(t);
            }
        }
        return "";
    }

    private String getValueByNode(VTDNav vn, String child) throws NavException {
        
        if (vn.toElement(VTDNav.FIRST_CHILD, child)) {
            do {
                int val = vn.getText();
                if (val != -1) {
                    return vn.toNormalizedString(val); 
                }
            } while (vn.toElement(VTDNav.NEXT_SIBLING, child));
        }
        return "";
    }
    
    private String calculateEvalue(String evalue) { 
        try {
            Double dEvalue = Double.valueOf(evalue);
            dEvalue = Math.round(dEvalue * 100) / 100.0d;
            return String.valueOf(dEvalue);
        } catch (NumberFormatException e) {
            return "0";
        }
    }

    /**
     * This method take a String for all the metadata and return BlastMetadata object
     * @param metadata
     * @return BlastMetadata
     */
    public BlastMetadata buildBlastMetadataByString(String metadata) {
        logger.info("buildBlastMetadataByString");
        
        VTDGen vg = new VTDGen();
        try { 
            byte[] bytes = metadata.getBytes("UTF-8");
            
            vg.setDoc(bytes);
            vg.parse(true);

            VTDNav vn = vg.getNav();
            String program = getValueByPath(vn, BLASTOUTPUT_PROGRAM);
            String version = getValueByPath(vn, BLASTOUTPUT_VISION);
            String reference = getValueByPath(vn, BLASTOUTPUT_REFERENCE);
            String blastDatabase = getValueByPath(vn, BLASTOUTPUT_DATABASE);
             
            String query = getValueByPath(vn, BLASTOUTPUT_QUERY_ID) + " " + getValueByPath(vn, BLASTOUTPUT_QUERY_DEF);
            String queryLength = getValueByPath(vn, BLASTOUTPUT_QUERY_LEN);
            
            String statisticDbNumber = getValueByPath(vn, BLASTOUTPUT_STATISTIC_NUMBER);
            String statisticDbLength = getValueByPath(vn, BLASTOUTPUT_STATISTIC_LENGTH);
            
//            String accession;
            int hitNumber;
            String hitDef;
            String hitLen;
            String hspScore;
            String bitScore;
            String evalue;
            int hspIdentity; 
            String qseq;
            String hseq;
            String midline;
            int hspQueryFrom;
            int hspQueryTo;
            int hspHitFrom;
            int hspHitTo;
            String hspPositive;
            int hspGaps;
            int hspAlignLen;
            int hspQueryFrame; 
            int hspHitFrame;
            
            String genbankId;
            String genbankAccession;
            String boldId;
            String targetMarker;
            String coordinates;
            String catalogNumber;
            String scientificName;
              
            List<BlastSubjectMetadata> subjectMetadataList = new ArrayList<>(); 
            List<BlastSubjectMetadata> lowMatchList = new ArrayList<>(); 
            BlastSubjectMetadata subjectMetadata;
            ap.selectXPath(BLASTOUTPUT_HITS); 
            
            double identities;
            double alignLength; 
            long percentage = 0;
            
            
            List<BlastSubjectHsp> subjectHspList;
            while (ap.evalXPath() != -1) { 
//                accession = getValueByNode(vn, BLASTOUTPUT_HITS_ACCESSION); 
//                vn.toElement(VTDNav.PARENT);
                
                hitNumber = Integer.parseInt(getValueByNode(vn, BLASTOUTPUT_HITS_NUM)); 
                vn.toElement(VTDNav.PARENT);
                
                hitDef = getValueByNode(vn, BLASTOUTPUT_HITS_DEF);
                vn.toElement(VTDNav.PARENT);

                hitLen = getValueByNode(vn, BLASTOUTPUT_HITS_LEN); 
                vn.toElement(VTDNav.PARENT);
                
                subjectHspList = new ArrayList<>();
                if (vn.toElement(VTDNav.FIRST_CHILD, BLASTOUTPUT_HITS_HSPS)) {
                    
                    if (vn.toElement(VTDNav.FIRST_CHILD, BLASTOUTPUT_HITS_HSP)) {
                        
                        hspScore = getValueByNode(vn, BLASTOUTPUT_HITS_SCORE); 
                        vn.toElement(VTDNav.PARENT);
                        
                        bitScore = getValueByNode(vn, BLASTOUTPUT_HITS_BITSCORE);
                        vn.toElement(VTDNav.PARENT);

                        evalue = getValueByNode(vn, BLASTOUTPUT_HITS_EVALUE); 
                        evalue = calculateEvalue(evalue); 
                        vn.toElement(VTDNav.PARENT);
                        
                        hspIdentity = Integer.parseInt(getValueByNode(vn, BLASTOUTPUT_HITS_IDENTITY)); 
                        vn.toElement(VTDNav.PARENT);
                        
                        hspAlignLen = Integer.parseInt(getValueByNode(vn, BLASTOUTPUT_HITS_ALIGN_LEN)); 
                        vn.toElement(VTDNav.PARENT);
                        
                        qseq = getValueByNode(vn, BLASTOUTPUT_HITS_QSEQ); 
                        vn.toElement(VTDNav.PARENT);
                        
                        hseq = getValueByNode(vn, BLASTOUTPUT_HITS_HSEQ); 
                        vn.toElement(VTDNav.PARENT);
                        
                        midline = getValueByNode(vn, BLASTOUTPUT_HITS_MIDLINE); 
                        vn.toElement(VTDNav.PARENT);
                        
                        hspQueryFrom = Integer.parseInt(getValueByNode(vn, BLASTOUTPUT_HITS_QUERY_FROM)); 
                        vn.toElement(VTDNav.PARENT);
                         
                        hspQueryTo = Integer.parseInt(getValueByNode(vn, BLASTOUTPUT_HITS_QUERY_TO)); 
                        vn.toElement(VTDNav.PARENT);
                        
                        hspHitFrom = Integer.parseInt(getValueByNode(vn, BLASTOUTPUT_HITS_HIT_FROM)); 
                        vn.toElement(VTDNav.PARENT);
                        
                        hspHitTo = Integer.parseInt(getValueByNode(vn, BLASTOUTPUT_HITS_HIT_TO)); 
                        vn.toElement(VTDNav.PARENT);
                        
                        hspPositive = getValueByNode(vn, BLASTOUTPUT_HITS_POSITIVE); 
                        vn.toElement(VTDNav.PARENT); 
                         
                        hspGaps = Integer.parseInt(getValueByNode(vn, BLASTOUTPUT_HITS_GAPS)); 
                        vn.toElement(VTDNav.PARENT); 
                        
                        hspQueryFrame = Integer.parseInt(getValueByNode(vn, BLASTOUTPUT_HITS_QUERY_FRAME)); 
                        vn.toElement(VTDNav.PARENT); 
                        
                        hspHitFrame = Integer.parseInt(getValueByNode(vn, BLASTOUTPUT_HITS_HIT_FRAME)); 
                        vn.toElement(VTDNav.PARENT); 
                         
                        identities = (double) hspIdentity;
                        alignLength = (double) hspAlignLen; 
                        percentage = (long) Math.floor((identities / alignLength) * 100 + 0.5d); 
                        
                        
                        BlastSubjectHsp subHsp = new BlastSubjectHsp(hspScore, bitScore, evalue, hspQueryFrom, hspQueryTo, 
                                                                     hspHitFrom, hspHitTo, hspIdentity, hspPositive, hspGaps, 
                                                                     hspAlignLen, percentage, qseq, hseq, midline, hspQueryFrame, hspHitFrame);
                        subjectHspList.add(subHsp);
                    }
                    while (vn.toElement(VTDNav.NS, "Hsp")) {
                        vn.toElement(VTDNav.P); // go up one level
                    } 
                } 
                vn.toElement(VTDNav.PARENT);
                vn.toElement(VTDNav.PARENT);  
                 
                
                genbankId = StringUtils.substringBetween(hitDef, "gi|", "|gb|");
                genbankAccession = StringUtils.substringBetween(hitDef, "|gb|", "|bold|");
                boldId = StringUtils.substringBetween(hitDef, "|bold|", "|gene|");
                targetMarker = StringUtils.substringBetween(hitDef, "|gene|", "|latlon|");
                
                if(hitDef.contains("|catnr|")) {
                    coordinates = StringUtils.substringBetween(hitDef, "|latlon|", "|catnr|"); 
                    catalogNumber = StringUtils.substringBetween(hitDef, "|catnr|", " ");
                    scientificName = StringUtils.substringAfter(hitDef, "|catnr|" + catalogNumber);
                } else {
                    catalogNumber = "";
                    coordinates = StringUtils.substringBetween(hitDef, "|latlon|", " ");  
                    scientificName = StringUtils.substringAfter(hitDef, "|latlon|" + coordinates);
                }
                
                
     
                subjectMetadata = new BlastSubjectMetadata(hitNumber, genbankId, genbankAccession, boldId, targetMarker, coordinates, 
                                                catalogNumber, scientificName, hitLen, subjectHspList, catalogNumber != null); 
                  
                if(percentage < 99) {
                    lowMatchList.add(subjectMetadata);
                } else {
                    subjectMetadataList.add(subjectMetadata);
                } 
            }   
   
            return new BlastMetadata(program, version, reference, blastDatabase, query, queryLength, statisticDbNumber, 
                                        statisticDbLength, subjectMetadataList, lowMatchList, false); 
        } catch (XPathEvalException | NavException | XPathParseException | UnsupportedEncodingException ex) {
            logger.error(ex.getMessage());
        } catch (EncodingException ex) {
            logger.error(ex.getMessage());
        } catch (EOFException ex) {
            logger.error(ex.getMessage());
        } catch (EntityException ex) {
            logger.error(ex.getMessage());
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }
}
