package se.nrm.dina.dnakey.logic.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List; 
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList; 
import se.nrm.dina.dnakey.logic.metadata.xml.DomCarrier;
import se.nrm.dina.dnakey.logic.util.HelpClass;

/**
 *
 * @author idali
 */
public class BlastXMLData {

    private final DomCarrier domCarrier;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final static String BLASTOUTPUT_PROGRAM = "/BlastOutput/BlastOutput_program";
    private final static String BLASTOUTPUT_VISION = "/BlastOutput/BlastOutput_version";
    private final static String BLASTOUTPUT_REFERENCE = "/BlastOutput/BlastOutput_reference";
    private final static String BLASTOUTPUT_DATABASE = "/BlastOutput/BlastOutput_db";
    private final static String BLASTOUTPUT_QUERY_ID = "/BlastOutput/BlastOutput_query-ID";
    private final static String BLASTOUTPUT_QUERY_DEF = "/BlastOutput/BlastOutput_query-def";
    private final static String BLASTOUTPUT_QUERY_LEN = "/BlastOutput/BlastOutput_query-len";
    private final static String BLASTOUTPUT_STATISTIC_NUMBER = "/BlastOutput/BlastOutput_iterations/Iteration/Iteration_stat/Statistics/Statistics_db-num";
    private final static String BLASTOUTPUT_STATISTIC_LENGTH = "/BlastOutput/BlastOutput_iterations/Iteration/Iteration_stat/Statistics/Statistics_db-len";
    private final static String BLASTOUTPUT_HITS = "/BlastOutput/BlastOutput_iterations/Iteration/Iteration_hits/Hit";
    private final static String BLASTOUTPUT_HITS_NUM = "Hit_num";
    private final static String BLASTOUTPUT_HITS_ID = "Hit_id";
    private final static String BLASTOUTPUT_HITS_DEF = "Hit_def";
    private final static String BLASTOUTPUT_HITS_ACCESSION = "Hit_accession";
    private final static String BLASTOUTPUT_HITS_LEN = "Hit_len";
    private final static String BLASTOUTPUT_HITS_HSPS = "Hit_hsps/Hsp";
    private final static String BLASTOUTPUT_HITS_HSP_SCORE = "Hsp_score";
    private final static String BLASTOUTPUT_HITS_BIT_SCORE = "Hsp_bit-score";
    private final static String BLASTOUTPUT_HITS_EVALUE = "Hsp_evalue";
    private final static String BLASTOUTPUT_HITS_QUERY_FROM = "Hsp_query-from";
    private final static String BLASTOUTPUT_HITS_QUERY_TO = "Hsp_query-to";
    private final static String BLASTOUTPUT_HITS_HIT_FROM = "Hsp_hit-from";
    private final static String BLASTOUTPUT_HITS_HIT_TO = "Hsp_hit-to";
    private final static String BLASTOUTPUT_HITS_IDENTIFY = "Hsp_identity";
    private final static String BLASTOUTPUT_HITS_POSITIVE = "Hsp_positive";
    private final static String BLASTOUTPUT_HITS_GAPS = "Hsp_gaps";
    private final static String BLASTOUTPUT_HITS_ALIGN_LEN = "Hsp_align-len";
    private final static String BLASTOUTPUT_HITS_QSEQ = "Hsp_qseq";
    private final static String BLASTOUTPUT_HITS_HSEQ = "Hsp_hseq";
    private final static String BLASTOUTPUT_HITS_MIDLINE = "Hsp_midline";
    private final static String BLASTOUTPUT_HITS_QUERY_FRAME = "Hsp_query-frame";
    private final static String BLASTOUTPUT_HITS_HIT_FRAME = "Hsp_hit-frame";
    List<BlastSubjectMetadata> subjects = new ArrayList<BlastSubjectMetadata>();
    List<BlastSubjectMetadata> lowMatches = new ArrayList<BlastSubjectMetadata>();

    public BlastXMLData(Document document) {
        this.domCarrier = new DomCarrier(document);
    }

    public BlastXMLData(DomCarrier domCarrier) {
        this.domCarrier = domCarrier;
    }

    public BlastMetadata getGenbankMetadata() {
        String program = getValueByXPathExpression(BLASTOUTPUT_PROGRAM);
        String version = getValueByXPathExpression(BLASTOUTPUT_VISION);
        String reference = getValueByXPathExpression(BLASTOUTPUT_REFERENCE);
        String database = getValueByXPathExpression(BLASTOUTPUT_DATABASE);
        String query = "";
        String queryLen = getValueByXPathExpression(BLASTOUTPUT_QUERY_LEN);
        String statisticDbNumber = getValueByXPathExpression(BLASTOUTPUT_STATISTIC_NUMBER);
        String statisticDbLength = getValueByXPathExpression(BLASTOUTPUT_STATISTIC_LENGTH);

        getBlastSubjectMetadataList();
 
        return new BlastMetadata(program, version, reference, database, query, queryLen, 
                                    statisticDbNumber, statisticDbLength, subjects, lowMatches, false);

    }

    public BlastMetadata getMetadata() {
        String program = getValueByXPathExpression(BLASTOUTPUT_PROGRAM);
        String version = getValueByXPathExpression(BLASTOUTPUT_VISION);
        String reference = getValueByXPathExpression(BLASTOUTPUT_REFERENCE);
        String database = getValueByXPathExpression(BLASTOUTPUT_DATABASE);
        String query = getQuery();
        String queryLen = getValueByXPathExpression(BLASTOUTPUT_QUERY_LEN);
        String statisticDbNumber = getValueByXPathExpression(BLASTOUTPUT_STATISTIC_NUMBER);
        String statisticDbLength = getValueByXPathExpression(BLASTOUTPUT_STATISTIC_LENGTH);

        getBlastSubjectMetadataList();
 
        return new BlastMetadata(program, version, reference, database, query, queryLen, 
                                    statisticDbNumber, statisticDbLength, subjects, lowMatches, false);
    }
    

    public String getQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append(getValueByXPathExpression(BLASTOUTPUT_QUERY_ID));
        sb.append(" ");
        sb.append(getValueByXPathExpression(BLASTOUTPUT_QUERY_DEF));
        return sb.toString().trim();
    }

    public void getBlastSubjectMetadataList() {

        subjects = new ArrayList<>();
        lowMatches = new ArrayList<>();

        NodeList subjectNodeList;

        subjectNodeList = domCarrier.getListByXpath(BLASTOUTPUT_HITS);

        Node node;
        Node shNode;
        int hitNumber = 0;
        String hitDef;
        String hitLen;
        String hspScore;
        String hspBitScore = "";
        String hspEvalue = "";
        Double bitScore = 0.0;
        int hspQueryFrom = 0;
        int hspQueryTo = 0;
        int hspHitFrom = 0;
        int hspHitTo = 0;
        int hspIdentity = 0;
        String hspPositive;
        int hspGaps = 0;
        int hspAlignLen = 0;
        String hspQseq;
        String hspHseq;
        String hspMidline;
        int hspQueryFrame;
        int hspHitFrame;

        String genbankId;
        String genbankAccession;
        String boldId;
        String targetMarker;
        String coordinates;
        String catalogNumber;
        String scientificName;

        double identities = 0;
        double alignLength = 0;
        long percentage = 0;

        List<BlastSubjectHsp> subjectHsps;

        NodeList subjectHspNodeList;

        for (int i = 0; i < subjectNodeList.getLength(); i++) {
            hitNumber = HelpClass.stringToInteger(getValueByXPathExpression(BLASTOUTPUT_HITS_NUM));

            node = subjectNodeList.item(i);
            node.getParentNode().removeChild(node);

            hitDef = getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_DEF, node);
            hitLen = getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_LEN, node);

            subjectHsps = new ArrayList<>();
            subjectHspNodeList = domCarrier.getListByXpathAndNode(BLASTOUTPUT_HITS_HSPS, node);

            for (int j = 0; j < subjectHspNodeList.getLength(); j++) {
                shNode = subjectHspNodeList.item(j);
                shNode.getParentNode().removeChild(shNode);

                hspScore = getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_HSP_SCORE, shNode);
                try {
                    bitScore = Double.valueOf(getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_BIT_SCORE, shNode));
                    hspBitScore = String.valueOf((long) Math.floor(bitScore + 0.5d));
                } catch (NumberFormatException e) {
                    logger.error(e.getMessage());
                }

                hspEvalue = HelpClass.stringToPercentageString(getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_EVALUE, shNode));

                hspQueryFrom = HelpClass.stringToInteger(getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_QUERY_FROM, shNode));
                hspQueryTo = HelpClass.stringToInteger(getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_QUERY_TO, shNode));
                hspHitFrom = HelpClass.stringToInteger(getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_HIT_FROM, shNode));
                hspHitTo = HelpClass.stringToInteger(getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_HIT_TO, shNode));
                hspIdentity = HelpClass.stringToInteger(getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_IDENTIFY, shNode));
                hspPositive = getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_POSITIVE, shNode);

                hspGaps = HelpClass.stringToInteger(getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_GAPS, shNode));
                hspAlignLen = HelpClass.stringToInteger(getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_ALIGN_LEN, shNode));
                hspQseq = getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_QSEQ, shNode);
                hspHseq = getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_HSEQ, shNode);
                hspMidline = getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_MIDLINE, shNode);
                hspQueryFrame = HelpClass.stringToInteger(getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_QUERY_FRAME, shNode));
                hspHitFrame = HelpClass.stringToInteger(getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_HIT_FRAME, shNode));

                identities = Double.valueOf(hspIdentity);
                alignLength = Double.valueOf(hspAlignLen);
                percentage = (long) Math.floor((identities / alignLength) * 100 + 0.5d);

                BlastSubjectHsp blastSubjectHsp = new BlastSubjectHsp(hspScore, hspBitScore, hspEvalue, hspQueryFrom, hspQueryTo,
                        hspHitFrom, hspHitTo, hspIdentity, hspPositive, hspGaps, hspAlignLen, percentage, hspQseq, hspHseq,
                        hspMidline, hspQueryFrame, hspHitFrame);
                subjectHsps.add(blastSubjectHsp);
            }
            Collections.sort(subjectHsps);

            logger.info("hitdef : {}", hitDef);

//            String[] strArray = StringUtils.split(hitDef, "|");
//            String[] strArray = hitDef.split("|"); 
            genbankId = StringUtils.substringBetween(hitDef, "gi|", "|gb|");
            genbankAccession = StringUtils.substringBetween(hitDef, "|gb|", "|bold|");
            boldId = StringUtils.substringBetween(hitDef, "|bold|", "|gene|");
            targetMarker = StringUtils.substringBetween(hitDef, "|gene|", "|latlon|");

            if (hitDef.contains("catnr")) {
                coordinates = StringUtils.substringBetween(hitDef, "|latlon|", "|catnr|");

                catalogNumber = StringUtils.substringBetween(hitDef, "|catnr|", " ");
                scientificName = StringUtils.substringAfter(hitDef, "|catnr|" + catalogNumber);
            } else {
                coordinates = StringUtils.substringBetween(hitDef, "|latlon|", " ");
                catalogNumber = "";
                scientificName = StringUtils.substringAfter(hitDef, "|latlon|" + coordinates);
            }

//            coordinates = StringUtils.substringBefore(strArray[9], " ");
//            scientificName = StringUtils.substringAfter(strArray[9], " "); 
            logger.info("coordinates : {}", coordinates);
            logger.info("scientific name : {}", scientificName);

            BlastSubjectMetadata blastSubjectMetadata = new BlastSubjectMetadata(hitNumber, genbankId, genbankAccession, boldId, targetMarker,
                    coordinates, catalogNumber, scientificName, hitLen, subjectHsps, catalogNumber != null);

            if (hspIdentity < 99) {
                lowMatches.add(blastSubjectMetadata);
            } else {
                subjects.add(blastSubjectMetadata);
            }
        }

    }

    private List<BlastSubjectMetadata> getGenbankBlastSubjectMetadataList() {

        List<BlastSubjectMetadata> subjects = new ArrayList<BlastSubjectMetadata>();
        NodeList subjectNodeList;
        subjectNodeList = domCarrier.getListByXpath(BLASTOUTPUT_HITS);
 

        Node node;
        Node shNode;
        int hitNumber = 0;
        String hitId;
        String hitDef;
        String hitLen;
        String hspScore;
        String hspBitScore = "";
        String hspEvalue = "";
        Double bitScore = 0.0;
        int hspQueryFrom = 0;
        int hspQueryTo = 0;
        int hspHitFrom = 0;
        int hspHitTo = 0;
        int hspIdentity = 0;
        String hspPositive;
        int hspGaps = 0;
        int hspAlignLen = 0;
        String hspQseq;
        String hspHseq;
        String hspMidline;
        int hspQueryFrame;
        int hspHitFrame;

        String genbankId;
        String genbankAccession;
        String boldId;
        String targetMarker = "";
        String coordinates = "";
        String catalogNumber = "";
        String scientificName = "";

        List<BlastSubjectHsp> subjectHsps;
        NodeList subjectHspNodeList;
        
        double identities = 0;
        double alignLength = 0;
        long percentage = 0;

        for (int i = 0; i < 10; i++) {
            hitNumber = HelpClass.stringToInteger(getValueByXPathExpression(BLASTOUTPUT_HITS_NUM));

            node = subjectNodeList.item(i);
            node.getParentNode().removeChild(node);

            hitId = getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_ID, node);
            hitDef = getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_DEF, node);
            hitLen = getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_LEN, node);

            genbankId = StringUtils.substringBetween(hitId, "gi|", "|gb|");

            genbankAccession = getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_ACCESSION, node);
            boldId = genbankAccession;

            subjectHsps = new ArrayList<>();
            subjectHspNodeList = domCarrier.getListByXpathAndNode(BLASTOUTPUT_HITS_HSPS, node);
 

            for (int j = 0; j < subjectHspNodeList.getLength(); j++) {
                shNode = subjectHspNodeList.item(j);
                shNode.getParentNode().removeChild(shNode);


                hspScore = getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_HSP_SCORE, shNode);
                try {
                    bitScore = Double.valueOf(getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_BIT_SCORE, shNode));
                    hspBitScore = String.valueOf((long) Math.floor(bitScore + 0.5d));
                } catch (NumberFormatException e) {
                    logger.error(e.getMessage());
                }


                hspEvalue = HelpClass.stringToPercentageString(getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_EVALUE, shNode));

                hspQueryFrom = HelpClass.stringToInteger(getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_QUERY_FROM, shNode));
                hspQueryTo = HelpClass.stringToInteger(getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_QUERY_TO, shNode));
                hspHitFrom = HelpClass.stringToInteger(getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_HIT_FROM, shNode));
                hspHitTo = HelpClass.stringToInteger(getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_HIT_TO, shNode));
                hspIdentity = HelpClass.stringToInteger(getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_IDENTIFY, shNode));
                hspPositive = getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_POSITIVE, shNode);

                hspGaps = HelpClass.stringToInteger(getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_GAPS, shNode));
                hspAlignLen = HelpClass.stringToInteger(getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_ALIGN_LEN, shNode));
                hspQseq = getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_QSEQ, shNode);
                hspHseq = getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_HSEQ, shNode);
                hspMidline = getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_MIDLINE, shNode);
                hspQueryFrame = HelpClass.stringToInteger(getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_QUERY_FRAME, shNode));
                hspHitFrame = HelpClass.stringToInteger(getValueByXPathExpressionAndNode(BLASTOUTPUT_HITS_HIT_FRAME, shNode));
                
                identities = Double.valueOf(hspIdentity);
                alignLength = Double.valueOf(hspAlignLen);
                percentage = (long) Math.floor((identities / alignLength) * 100 + 0.5d);

                BlastSubjectHsp blastSubjectHsp = new BlastSubjectHsp(hspScore, hspBitScore, hspEvalue, hspQueryFrom, hspQueryTo,
                        hspHitFrom, hspHitTo, hspIdentity, hspPositive, hspGaps, hspAlignLen, percentage, hspQseq, hspHseq,
                        hspMidline, hspQueryFrame, hspHitFrame);
                subjectHsps.add(blastSubjectHsp);
            }
            Collections.sort(subjectHsps);

            BlastSubjectMetadata blastSubjectMetadata = new BlastSubjectMetadata(hitNumber, genbankId, genbankAccession, boldId, targetMarker,
                    coordinates, catalogNumber, scientificName, hitLen, subjectHsps, catalogNumber.isEmpty());
            subjects.add(blastSubjectMetadata);
        }
        return subjects;
    }

    private String getValueByXPathExpression(String xPathExpression) {
        return domCarrier.getStringByXpath(xPathExpression); 
    }

    private String getValueByXPathExpressionAndNode(String xPathExpression, Node node) {
        return domCarrier.getStringByXpathAndNode(xPathExpression, node); 
    }
}
