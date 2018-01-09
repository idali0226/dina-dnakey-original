package se.nrm.dina.dnakey.logic.metadata;
  
import java.util.List;     

/**
 *
 * @author idali
 */
public class BlastMetadata { 
    
    private String program;
    private String version;
    private String reference; 
    private String database;
    private String query;
    private String queryLen;
    private String statisticDbNumber;
    private String statisticDbLength; 
    private String sequence;
    private boolean openLowMatch = false; 
    
//    private BlastQueryMetadata query;
//    private BlastStatistic statistic;
    private List<BlastSubjectMetadata> subjectMetadataList;
    private List<BlastSubjectMetadata> lowMatchsubjectMetadataList;
     
    
    public BlastMetadata() {
        
    }
    
    public BlastMetadata(String program, String version, String reference, String database, String query, String queryLen, 
                         String statisticDbNumber, String statisticDbLength, List<BlastSubjectMetadata> subjectMetadataList, 
                         List<BlastSubjectMetadata> lowMatchSubjectMetadataList, boolean openLowMatch) {
        this.program = program;
        this.version = version;
        this.reference = reference;
        this.database = database;
        this.query = query;
        this.queryLen = queryLen;
        this.statisticDbNumber = statisticDbNumber;
        this.statisticDbLength = statisticDbLength;
//        this.statistic = statistic;
        this.subjectMetadataList = subjectMetadataList;
        this.lowMatchsubjectMetadataList = lowMatchSubjectMetadataList; 
        this.openLowMatch = openLowMatch;
    }
  
    public String getDatabase() {
        return database;
    }  
    
    public String getQuery() {
        return query;
    }

    public String getQueryLen() {
        return queryLen;
    } 
    
    public String getReference() {
        return reference;
    }

    public String getStatisticDbLength() {
        return statisticDbLength;
    }

    public String getStatisticDbNumber() {
        return statisticDbNumber;
    }
 
    public List<BlastSubjectMetadata> getSubjectMetadataList() {
        return subjectMetadataList;
    }
 
    public String getVersion() {
        return version;
    } 
    
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getSequence() {
        return sequence;
    }

    public List<BlastSubjectMetadata> getLowMatchsubjectMetadataList() {
        return lowMatchsubjectMetadataList;
    }

    public boolean isOpenLowMatch() {
        return openLowMatch;
    }
     
    public void setOpenLowMatch(boolean openLowMatch) {
        this.openLowMatch = openLowMatch;
    }

    public boolean isHasHighMatach() {
        return !subjectMetadataList.isEmpty();
    }

    public boolean isHasLowMatch() {
        return !lowMatchsubjectMetadataList.isEmpty();
    }

    public boolean isHasResult() {
        return !subjectMetadataList.isEmpty() || !lowMatchsubjectMetadataList.isEmpty();
    }
  
    public String getGenbankUrl() { 
        return subjectMetadataList.isEmpty() ? "http://www.ncbi.nlm.nih.gov/blast/Blast.cgi?QUERY=" + sequence + 
                                    "&DATABASE=nr&HITLIST_SIZE=10&FILTER=L&EXPECT=10&FORMAT_TYPE=HTML" +
                                    "&PROGRAM=blastn&CLIENT=web&SERVICE=plain&NCBI_GI=on&PAGE=Nucleotides&CMD=Put" : null;
    }
}
