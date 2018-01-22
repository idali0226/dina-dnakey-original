package se.nrm.dina.dnakey.logic.metadata;
 
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML; 

/**
 *
 * @author idali
 */
@Slf4j
public class MetadataDataFactory implements Serializable {
  
    public static MetadataDataFactory getInstance() {
        return new MetadataDataFactory();
    }
 
    public BlastMetadata buildBlastMetadataByJson(String metadata) {
        log.info("buildBlastMetadataByJson");

        List<BlastSubjectMetadata> subjectMetadataList = new ArrayList<>();
        List<BlastSubjectMetadata> lowMatchList = new ArrayList<>();
         
        try {
            JSONObject blastJson = XML.toJSONObject(metadata).getJSONObject("BlastOutput");
          
            String version = blastJson.getString("BlastOutput_version");
            String program = blastJson.getString("BlastOutput_program");
            String reference = blastJson.getString("BlastOutput_reference");
            String blastDatabase = blastJson.getString("BlastOutput_db");
            String query = blastJson.getString("BlastOutput_query-ID") + " " + blastJson.optString("BlastOutput_query-def");;
            int queryLength = blastJson.getInt("BlastOutput_query-len");

            JSONObject itrJson = blastJson.getJSONObject("BlastOutput_iterations").getJSONObject("Iteration");
            JSONObject statJson = itrJson.getJSONObject("Iteration_stat").getJSONObject("Statistics");

            int statisticDbNumber = statJson.getInt("Statistics_db-num");
            int statisticDbLength = statJson.getInt("Statistics_db-len");

            boolean hasHit = false;
            JSONObject itrHitsJson = itrJson.optJSONObject("Iteration_hits");
            if (itrHitsJson != null) {
                JSONArray hitsJsonArray = itrHitsJson.getJSONArray("Hit");
                hasHit = true;
                for (int i = 0; i < hitsJsonArray.length(); i++) {
                    JSONObject hitJson = hitsJsonArray.getJSONObject(i);
                    

                    JSONObject hspJson = hitJson.getJSONObject("Hit_hsps").getJSONObject("Hsp");
         
                    BlastSubjectHsp subHsp = buildSubHits(hspJson); 
                    List<BlastSubjectHsp> subjectHspList = new ArrayList<>();
                    subjectHspList.add(subHsp);

                    BlastSubjectMetadata subjectMetadata = buildSubMetadata(hitJson, subjectHspList);

 
                    if (subHsp.getPercentage() < 99) {
                        lowMatchList.add(subjectMetadata);
                    } else {
                        subjectMetadataList.add(subjectMetadata);
                    }
                } 
            }
            return new BlastMetadata(program, version, reference, blastDatabase, query, queryLength, statisticDbNumber,
                                            statisticDbLength, subjectMetadataList, lowMatchList, false, hasHit);
            
            
            //                    String genbankId = StringUtils.substringBetween(hitDef, "gi|", "|gb|");
//                    String genbankAccession = StringUtils.substringBetween(hitDef, "|gb|", "|bold|");
//                    String boldId = StringUtils.substringBetween(hitDef, "|bold|", "|gene|");
//                    String targetMarker = StringUtils.substringBetween(hitDef, "|gene|", "|latlon|");
//                    String coordinates;
//                    String catalogNumber;
//                    String scientificName;
//
//                    if (hitDef.contains("|catnr|")) {
//                        coordinates = StringUtils.substringBetween(hitDef, "|latlon|", "|catnr|");
//                        catalogNumber = StringUtils.substringBetween(hitDef, "|catnr|", " ");
//                        scientificName = StringUtils.substringAfter(hitDef, "|catnr|" + catalogNumber);
//                    } else {
//                        catalogNumber = "";
//                        coordinates = StringUtils.substringBetween(hitDef, "|latlon|", " ");
//                        scientificName = StringUtils.substringAfter(hitDef, "|latlon|" + coordinates);
//                    }

//                    subjectMetadata = new BlastSubjectMetadata(hitNumber, genbankId, genbankAccession, boldId, targetMarker, coordinates,
//                                            catalogNumber, scientificName, hitLen, subjectHspList, catalogNumber != null);
                   

//                    double hspScore = hspJson.getDouble("Hsp_score");
//                    double bitScore = hspJson.getDouble("Hsp_bit-score");
//
//                    double evalue = calculateEvalue(hspJson.getDouble("Hsp_evalue"));
//
//                    int hspIdentity = hspJson.getInt("Hsp_identity");
//                    String qseq = hspJson.getString("Hsp_qseq");
//                    String hseq = hspJson.getString("Hsp_hseq");
//                    String midline = hspJson.getString("Hsp_midline");
//                    int hspQueryFrom = hspJson.getInt("Hsp_query-from");
//                    int hspQueryTo = hspJson.getInt("Hsp_query-to");
//                    int hspHitFrom = hspJson.getInt("Hsp_hit-from");
//                    int hspHitTo = hspJson.getInt("Hsp_hit-to");
//                    int hspPositive = hspJson.getInt("Hsp_positive");
//                    int hspGaps = hspJson.getInt("Hsp_gaps");
//                    int hspAlignLen = hspJson.getInt("Hsp_align-len");
//                    int hspQueryFrame = hspJson.getInt("Hsp_query-frame");
//                    int hspHitFrame = hspJson.getInt("Hsp_hit-frame");
//
////                    double identities = (double) hspIdentity;
////                    double alignLength = (double) hspAlignLen;
//                    long percentage = calculatePercentage(hspIdentity, hspAlignLen);
//
//                    BlastSubjectHsp subHsp = new BlastSubjectHsp(hspScore, bitScore, evalue, hspQueryFrom, hspQueryTo,
//                                                hspHitFrom, hspHitTo, hspIdentity, hspPositive, hspGaps,
//                                                hspAlignLen, percentage, qseq, hseq, midline, hspQueryFrame, hspHitFrame);

        } catch (JSONException ex) {
            log.error(ex.getMessage());
        }
        return new BlastMetadata();
    }

    private BlastSubjectMetadata buildSubMetadata(JSONObject hitJson, List<BlastSubjectHsp> subjectHspList) {

        int hitNumber = hitJson.getInt("Hit_num");
        String hitDef = hitJson.getString("Hit_def");
        int hitLen = hitJson.getInt("Hit_len");

        String genbankId = StringUtils.substringBetween(hitDef, "gi|", "|gb|");
        String genbankAccession = StringUtils.substringBetween(hitDef, "|gb|", "|bold|");
        String boldId = StringUtils.substringBetween(hitDef, "|bold|", "|gene|");
        String targetMarker = StringUtils.substringBetween(hitDef, "|gene|", "|latlon|");
        String coordinates;
        String catalogNumber;
        String scientificName;

        if (hitDef.contains("|catnr|")) {
            coordinates = StringUtils.substringBetween(hitDef, "|latlon|", "|catnr|");
            catalogNumber = StringUtils.substringBetween(hitDef, "|catnr|", " ");
            scientificName = StringUtils.substringAfter(hitDef, "|catnr|" + catalogNumber);
        } else {
            catalogNumber = "";
            coordinates = StringUtils.substringBetween(hitDef, "|latlon|", " ");
            scientificName = StringUtils.substringAfter(hitDef, "|latlon|" + coordinates);
        }
 
        return new BlastSubjectMetadata(hitNumber, genbankId, genbankAccession, boldId, targetMarker, coordinates,
                                        catalogNumber, scientificName, hitLen, subjectHspList, catalogNumber != null);
    }
    
    private BlastSubjectHsp buildSubHits(JSONObject hspJson) {
        double hspScore = hspJson.getDouble("Hsp_score");
        double bitScore = hspJson.getDouble("Hsp_bit-score");
 
        int hspIdentity = hspJson.getInt("Hsp_identity");
        String qseq = hspJson.getString("Hsp_qseq");
        String hseq = hspJson.getString("Hsp_hseq");
        String midline = hspJson.getString("Hsp_midline");
        int hspQueryFrom = hspJson.getInt("Hsp_query-from");
        int hspQueryTo = hspJson.getInt("Hsp_query-to");
        int hspHitFrom = hspJson.getInt("Hsp_hit-from");
        int hspHitTo = hspJson.getInt("Hsp_hit-to");
        int hspPositive = hspJson.getInt("Hsp_positive");
        int hspGaps = hspJson.getInt("Hsp_gaps");
        int hspAlignLen = hspJson.getInt("Hsp_align-len");
        int hspQueryFrame = hspJson.getInt("Hsp_query-frame");
        int hspHitFrame = hspJson.getInt("Hsp_hit-frame");
  

        return new BlastSubjectHsp(hspScore, bitScore, calculateEvalue(hspJson.getDouble("Hsp_evalue")), hspQueryFrom, 
                                    hspQueryTo, hspHitFrom, hspHitTo, hspIdentity, hspPositive, hspGaps, hspAlignLen, 
                                    calculatePercentage(hspIdentity, hspAlignLen), qseq, hseq, midline, hspQueryFrame, hspHitFrame);

    }


    private long calculatePercentage(int hspIdentity, int hspAlignLen) {
        double identities = (double) hspIdentity;
        double alignLength = (double) hspAlignLen;
        return (long) Math.floor((identities / alignLength) * 100 + 0.5d);
    }

    private double calculateEvalue(double dEvalue) {
        try { 
            return Math.round(dEvalue * 100) / 100.0d; 
        } catch (NumberFormatException e) {
            return 0;
        }
    } 
}
