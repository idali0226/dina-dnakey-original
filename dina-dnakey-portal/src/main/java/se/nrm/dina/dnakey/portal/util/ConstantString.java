package se.nrm.dina.dnakey.portal.util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author idali
 */
public class ConstantString {
    
    private static ConstantString instance = null;
     
    private static Map<String, String> map;
    
    private final String MAP_MARK_PATH = "http://maps.google.com/mapfiles/ms/micons/blue-dot.png";
    private final String NATURARV_URL_ENCODE = "param=dnakey&catalogNumber=";
    private final String DEFAULT_BLASTDB = "nrm";
    private final String UTF_8 = "UTF-8";

    /**
     * white space chars - " \t\r\n\f"
     */
    private final String WHITE_SPACE_CHARS = "[ \t\r\n\f]";
    
    
    
    
    
    public final String XML_OUTPUT = "xml";
    public final String TABULAR_OUTPUT = "tabular";
     
    public static synchronized ConstantString getInstance() {
        if (instance == null) {
            instance = new ConstantString();
        }
        
        map = new HashMap<>();
        map.put("feedback_en", "Feedback");
        map.put("feedback_sv", "Återkoppling");
        map.put("catalognum_en", "Catalog #");
        map.put("catalognum_sv", "Katalognr"); 
        map.put("commenname_en", "Swedish name");
        map.put("commenname_sv", "Svenskt namn");
        map.put("latinname_en", "Scientific name");
        map.put("latinname_sv", "Vetenskapligt namn"); 
        map.put("institution_en", "Institution");
        map.put("institution_sv", "Institution");
        map.put("collection_en", "Collection");
        map.put("collection_sv", "Samling");
        
        
        map.put("remarks_en", "Remarks");
        map.put("remarks_sv", "Änmärkningar");
        map.put("locality_en", "Locality");
        map.put("locality_sv", "Lokal"); 
        map.put("coordinates_en", "Coordinates");
        map.put("coordinates_sv", "Koordinater");
        map.put("date_en", "Date");
        map.put("date_sv", "Datum");
        
        map.put("collector_en", "Collector");
        map.put("collector_sv", "Insamlare");
        
        map.put("description_en", "Description");
        map.put("description_sv", "Beskrivning");
        
        map.put("sendmail_en", "Mail has been sent");
        map.put("sendmail_sv", "Post har skickats");
        map.put("sendmail_es", "Mail has been sent");
        
        map.put("validationfailed_en", "Sequence validation failed: ");
        map.put("validationfailed_sv", "Sekvens validering misslyckades: ");
        map.put("inputdata_en", "Input sequence(s) in FASTA format or upload file(s) in FASTA format.");
        map.put("inputdata_sv", "Mata in sekvens(er) i FASTA - format eller ladda upp fil(er) i FASTA - format.");
        map.put("blastfailed_en", "Blast failed:");
        map.put("blastfailed_sv", "Blast misslyckades"); 
        map.put("invalidSequence_en", "Invalid sequence or fasta file");
        map.put("invalidSequence_sv", "Ogiltig sekvens eller FASTA fil");
        map.put("blastnoresult_en", "Unable to match any records in the selected database");
        map.put("blastnoresult_sv", "Det går inte att matcha några poster i den valda databasen");
        map.put("searchanothdb_en", "Search in ");
        map.put("searchanothdb_sv", "Sök i "); 
 
        map.put("file_uploaded_en", "File is uploaded");
        map.put("file_uploaded_sv", "Filen är uppladdad");
        
        map.put("query_sv", "Fråga");
        map.put("query_en", "Query");
        
        map.put("subject_sv", "Subjk");
        map.put("subject_en", "Subjc");
        return instance;
    }
    
    public String getText(String key) {
        String text = map.get(key);
        if(text == null) {
            return "";
        }
        return text;
    }
    
        
    public String getMapMarkPath() {
        return MAP_MARK_PATH;
    }
    
    public String getNaturarvUrlEncode() {
        return NATURARV_URL_ENCODE;
    }
    
    public String getDefaultBlastDb() {
        return DEFAULT_BLASTDB;
    }
    
    public String getWhiteSpaceChars() {
        return WHITE_SPACE_CHARS;
    }
    
    public String getUtf8() {
        return UTF_8;
    }
    
}
