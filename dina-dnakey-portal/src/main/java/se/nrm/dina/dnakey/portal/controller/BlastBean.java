package se.nrm.dina.dnakey.portal.controller;
   
import java.io.IOException; 
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList; 
import java.util.HashMap;
import java.util.List;   
import java.util.Map;  
import javax.annotation.PostConstruct; 
import javax.enterprise.context.SessionScoped; 
import org.primefaces.event.FileUploadEvent; 
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;  
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils; 
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker; 
import se.nrm.dina.dnakey.logic.BlastQueue; 
import se.nrm.dina.dnakey.logic.GenbankBlaster;
import se.nrm.dina.dnakey.logic.metadata.BlastMetadata;
import se.nrm.dina.dnakey.logic.metadata.BlastSubjectHsp;
import se.nrm.dina.dnakey.logic.metadata.BlastSubjectMetadata; 
import se.nrm.dina.dnakey.logic.util.HelpClass;
import se.nrm.dina.dnakey.logic.vo.MorphyBankImage;
import se.nrm.dina.dnakey.portal.beans.MessageBean;
import se.nrm.dina.dnakey.portal.vo.ResultBean; 
import se.nrm.dina.dnakey.portal.logic.SequenceBuilder; 
import se.nrm.dina.dnakey.portal.util.ConstantString;
import se.nrm.dina.dnakey.portal.logic.SequenceValidation;
import se.nrm.dina.dnakey.portal.solr.SolrClient;
import se.nrm.dina.dnakey.portal.vo.SolrRecord; 
import se.nrm.dina.dnakey.portal.util.FastaFiles;

/** 
 *
 * @author idali
 */
@SessionScoped 
@Named("blast")
@Slf4j
public class BlastBean implements Serializable {
      
    private String sequenceList;                                                    // sequence or sequences are listed in tab1
    private String testSequences;                                                   // list of sequences in tab3 
    
    
    private List<UploadedFile> uploadedFiles = new ArrayList<>();                   // upload fasta files in tab2
    private Map<String, List<String>> sequencesMap = new HashMap();
    
    private List<String> sequences;
    
    private int numOfTestSeqs = 0;  
    private final int MAX_UPLOADED_FILES = 5; 
    private int activeIndex = 0;                                                    // active tab in sequence page
    
    
//    private String dbVersion;
//    private int statisticDbNumber;
//    private int statisticDbLength;
    
    
     
    private Map<BlastMetadata, String> ridMap = new HashMap<>(); 
    private String database;    
    
    // Result
    private BlastMetadata metadata;
    private List<ResultBean> resultBeans; 
    private int totalSequences = 0;
    // End of result
    
    
    @Inject
    private Languages languages;    
       
    @Inject
    private Navigator navigator;
    
    @Inject
    private MessageBean msg;
    
    @Inject
    private SequenceBuilder sequenceBuilder;
    
    @Inject
    private FileHandler fileHandler;
      
    @Inject
    private SequenceValidation validation;
     
    @Inject
    private SolrClient solr; 
    
    @Inject
    private BlastQueue serviceQueue;
    
    @Inject
    private GenbankBlaster blaster;
    
    
    
    
    
    
    
    
    
    
    
    
    
        
        
        
        
        
        
        
        
        
        
        
        
        
    

     
    
   
     

     
    private BlastMetadata blastMetadata;
    private BlastSubjectMetadata selectedMetadata;
    private BlastSubjectMetadata selectedSubMetadata;
    private List<BlastSubjectHsp> selectedHsps;
    
    private List<BlastMetadata> listMetadata = new ArrayList<>();

    
    private String feedMsgUrlsv;
    private String feedMsgUrlen;
    
    private MapModel advancedModel;
    
    private final String urlEncode;

     
    
    private double centLat = 0;
    private double centLnt = 0;
    private String coordinates = "";
    private int zoom = 6;
 
    
     
    private SolrRecord selectedRecord;
    private Map<String, SolrRecord> map = new HashMap<>();
    private List<MorphyBankImage> images = new ArrayList<>();
    
//    private String servername; 
    private SolrRecord record;
    private boolean solrAvailble = true;
     
    
     

   
    private FacesContext context;
    RequestContext requestContext; 
  

    

     
    

    

    

     
    
    public BlastBean() {
        log.info("BlastBean");
        
        ridMap = new HashMap<>();
        map = new HashMap<>();
        sequences = new ArrayList<>();
        sequencesMap = new HashMap();
        database = ConstantString.getInstance().getDefaultBlastDb();
        advancedModel = new DefaultMapModel();
        solrAvailble = true;
        urlEncode =  ConstantString.getInstance().getNaturarvUrlEncode();
        centLat = 0;
        centLnt = 0;
        coordinates = "";  
        totalSequences = 0; 
    }
    
    
    @PostConstruct
    public void init() {
        log.info("init");
         
        context = FacesContext.getCurrentInstance();  
        boolean isPostBack = context.isPostback();        // false if page is start up or reloaded by browser  
              
        requestContext = RequestContext.getCurrentInstance();  
 
        log.info("isPostBack : {}", isPostBack);
      
        if(!isPostBack) {     
            map = new HashMap<>();
            sequences = new ArrayList<>();
            sequencesMap = new HashMap();
            sequenceList = "";
            testSequences = "";
            database = ConstantString.getInstance().getDefaultBlastDb();
            advancedModel = new DefaultMapModel();
            solrAvailble = true;
            coordinates = "";
            centLat = 0;
            centLnt = 0;
              
            totalSequences = 0;
 
            clear();  
        }  
    }
    
    /**
     * onTabChange event fires when user click tab. 
     * 
     * @param event 
     */
    public void onTabChange(TabChangeEvent event) {
        log.info("onTabChange : {} ",   activeIndex); 
         
        if(testSequences == null || testSequences.trim().isEmpty()) {
            numOfTestSeqs = 0;
        }   
    }

    /**
     * Upload file
     *
     * @param event
     */
    public void handleFileUpload(FileUploadEvent event) { 
        log.info("handleFileUpload" ); 
        UploadedFile uploadedFile = event.getFile(); 
        if (uploadedFile != null) { 
            if(!sequencesMap.containsKey(uploadedFile.getFileName())) {
                uploadedFiles.add(uploadedFile); 
                convertUploadFile(uploadedFile);
            } else {
                msg.addWarning(ConstantString.getInstance().getText("file_uploaded_" + languages.getLocale()), ConstantString.getInstance().getText("file_uploaded_" + languages.getLocale()));
            } 
        }  
    }

    private void convertUploadFile(UploadedFile uploadedFile) {
        log.info("convertUploadFile");
        
        if (uploadedFile != null) { 
            try {
                String seq = IOUtils.toString(uploadedFile.getInputstream(), StandardCharsets.UTF_8.name()); 
                sequencesMap.put(uploadedFile.getFileName(), sequenceBuilder.prepareSequenceList(seq));  
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }   
        }
    }

    /**
     * remove uploaded file
     * @param file 
     */
    public void removefile(UploadedFile file) { 
        log.info("removefile : {}", file.getFileName()); 
        uploadedFiles.remove(file);  
        sequencesMap.remove(file.getFileName());
    }    
     
    /**
     * changeTestNumber -- event when number of test sequences changed
     */
    public void changeTestNumber() {
        log.info("changeTestNumber : {}", numOfTestSeqs); 
        
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i <= numOfTestSeqs; i++) { 
            sb.append(FastaFiles.getInstance().getSequence(i)); 
        } 
        testSequences = sb.toString();
    }    
     
        
    /**
     * submit sequences to blast 
     */
    public void submit() {
        log.info("submit : {}", activeIndex);
        
        switch (activeIndex) {
            case 0:
                submitSequences();
                break;
            case 1:
                submitupload();
                break;
            case 2:
                submitTestSequence();
                break;
            default:
                break;
        }
    }  
    
    private void submitSequences() {
        log.info("submitSequences : {}", database);
          
        resultBeans = new ArrayList<>(); 
        sequences = new ArrayList<>(); 
        if(StringUtils.isEmpty(sequenceList.trim())) {
            msg.addError(ConstantString.getInstance().getText("blastfailed_" + languages.getLocale()), ConstantString.getInstance().getText("inputdata_" + languages.getLocale()));
        } else {  
            sequences = sequenceBuilder.prepareSequenceList(sequenceList);
            run();
        }
    }
     
    private void submitupload() {
        log.info("submitupload : {}", database); 

        resultBeans = new ArrayList<>();
        sequences = new ArrayList<>(); 
        if (sequencesMap == null || sequencesMap.isEmpty()) {
            msg.addError(ConstantString.getInstance().getText("blastfailed_" + languages.getLocale()), ConstantString.getInstance().getText("inputdata_" + languages.getLocale()));
        } else {
            sequencesMap.entrySet().stream()
                    .forEach((entry) -> {
                        sequences.addAll(entry.getValue()); 
                    });
            
            if (sequences.size() > 100) {
                sequences = sequences.subList(0, 99);
            } 
            run();
        }
    }
    
    private void submitTestSequence() {
        log.info("submitTestSequence : {}", database ); 
        resultBeans = new ArrayList<>(); 
        sequences = new ArrayList<>(); 
        if(StringUtils.isEmpty(testSequences.trim())) {
            msg.addError(ConstantString.getInstance().getText("blastfailed_" + languages.getLocale()), ConstantString.getInstance().getText("inputdata_" + languages.getLocale()));
        } else {
            sequences = sequenceBuilder.prepareSequenceList(testSequences);
            run();
        }
    }
    
 
    /**
     * run blast
     */ 
    private void run() {
        boolean isSequenceValid = validation.validate(sequences);
        if (isSequenceValid) {
            blast();
            navigator.result(); 
        } else {
            if (sequences != null) {
                if(sequences.size() > 1) {
                    msg.createErrorMsgs(ConstantString.getInstance().getText("validationfailed_" + languages.getLocale()), validation.getErrorMsgs());
                } else {
                    msg.addError(ConstantString.getInstance().getText("validationfailed_" + languages.getLocale()), validation.getErrorMsg());
                }
            }
        }
    }
    
    private List<String> buildFastaFilePath() {
        List<String> fastaFilesPath = new ArrayList<>();
        sequences.stream()
                .forEach(seq -> {
                    fastaFilesPath.add(fileHandler.createFastaFile(seq));

                });
        return fastaFilesPath;
    }
    
    /**
     * Blasts a single sequence 
     */
    private void blast() {
        log.info("blast : {}", sequences.size());
        
        ridMap = new HashMap<>();
        
        totalSequences = sequences.size();                                      // display in result page
        map = new HashMap<>();
//        listMetadata = new ArrayList<>();                                  

        List<String> fastaFilesPath = buildFastaFilePath(); 
        listMetadata = serviceQueue.run(fastaFilesPath, database); 
        fileHandler.deleteTempFiles(fastaFilesPath);                            // remove temp fasta files
        
        
        
        
        
        
        
        
        
        
        
        
        
        

//            listMetadata = blaster.multipleBlast(fastaFilesPath, database);
        metadata = listMetadata.get(0);
//        dbVersion = metadata.getVersion();
//        statisticDbLength = metadata.getStatisticDbLength();
//        statisticDbNumber = metadata.getStatisticDbNumber();

        String query;
        String catalogNumber;
        String scientificName;
        String genbankAcc;
        String boldId;
        String identity;
        double score;
        double evalue;
        String collection = null;
        String swedishName = null;
        String locality = null;
        String latLnt;
        String date = null;
        String collector = null;
        String mark;

        int count = 0;
        boolean hasResult;
        for (BlastMetadata bm : listMetadata) {
            hasResult = true;
            if (!bm.isHasHit()) {
                hasResult = false;
                bm.setSequence(sequences.get(count));
            }

            if (hasResult) {
                query = bm.getQuery();
                if (!bm.isHasHighMatach()) {
                    bm.setSequence(sequences.get(count));
                }
                for (BlastSubjectMetadata subMetadata : bm.getSubjectMetadataList()) {
                    catalogNumber = subMetadata.getCatalogNumber();
                    scientificName = subMetadata.getScientificName();
                        genbankAcc = subMetadata.getGenbankAccession();
                        boldId = subMetadata.getBoldId();
                        latLnt = subMetadata.getCoordinates();
                        mark = subMetadata.getTargetMarker();
                         
                        for (BlastSubjectHsp hsp : subMetadata.getSubjectHspList()) {
                            identity = String.valueOf(hsp.getPercentage());
                            score = hsp.getHspScore();
                            evalue = hsp.getHspEvalue();

                            if (!catalogNumber.isEmpty()) {
                                try { 
                                    record = solr.getRecordByCollectionObjectCatalognumber(catalogNumber); 
                                    if (!map.containsKey(catalogNumber)) {
                                        map.put(catalogNumber, record);
                                    }
                                    solrAvailble = true;
                                } catch (Exception e) {
                                    solrAvailble = false;
                                }
                            }
                            
                            if(record != null) {
                                collection = record.getCollectionName();
                                swedishName = record.getCommonName();
                                locality = record.getLocality();
                                date = HelpClass.dateToString(record.getStartDate());
                                latLnt = record.getLat() + " " + record.getLnt();
                                collector = record.getCollector();
                                if(record.isImageExist()) {
                                    subMetadata.setMbImages(record.getImages());
                                    subMetadata.setMbid(record.getMbid());
                                } 
                            }
                            ResultBean bean = new ResultBean(query, catalogNumber, scientificName,
                                                                genbankAcc, boldId, identity, mark,
                                                                score, evalue, collection, swedishName,
                                                                locality, latLnt, date, collector);
                            resultBeans.add(bean);
                        }
                    }
                    
                    for (BlastSubjectMetadata subMetadata : bm.getLowMatchsubjectMetadataList()) {
                        catalogNumber = subMetadata.getCatalogNumber();
                        scientificName = subMetadata.getScientificName();
                        genbankAcc = subMetadata.getGenbankAccession();
                        boldId = subMetadata.getBoldId();
                        latLnt = subMetadata.getCoordinates();
                        mark = subMetadata.getTargetMarker();
                         
                        for (BlastSubjectHsp hsp : subMetadata.getSubjectHspList()) {
                            identity = String.valueOf(hsp.getPercentage());
                            score = hsp.getHspScore();
                            evalue = hsp.getHspEvalue();

                            if (!catalogNumber.isEmpty()) {
                                try { 
                                    record = solr.getRecordByCollectionObjectCatalognumber(catalogNumber);
                                    if (!map.containsKey(catalogNumber)) {
                                        map.put(catalogNumber, record);
                                    }
                                    solrAvailble = true;
                                } catch (Exception e) {
                                    solrAvailble = false;
                                }
                            }
                            
                            if(record != null) {
                                collection = record.getCollectionName();
                                swedishName = record.getCommonName();
                                locality = record.getLocality();
                                date = HelpClass.dateToString(record.getStartDate());
                                latLnt = record.getLat() + " " + record.getLnt();
                                collector = record.getCollector();
                                if(record.isImageExist()) {
                                    subMetadata.setMbImages(record.getImages());
                                    subMetadata.setMbid(record.getMbid());
                                } 
                            }
                            ResultBean bean = new ResultBean(query, catalogNumber, scientificName,
                                    genbankAcc, boldId, identity, mark,
                                    score, evalue, collection, swedishName,
                                    locality, latLnt, date, collector);
                            resultBeans.add(bean);
                        }
                    }
                }
                count++;
            }
 
  
         
    }
    
        
  
    /**
     * Clear all the sequences in tabs
     */
    public void clear() {
        sequenceList = null;
        uploadedFiles = new ArrayList<>();
        testSequences = null;
        
        sequencesMap.clear();
        
        numOfTestSeqs = 0; 
        database = ConstantString.getInstance().getDefaultBlastDb(); 
        ridMap.clear(); 
    }
    
    /**
     * newblast
     */
    public void newblast() {
        log.info("newblast : {}", activeIndex);
          
        totalSequences = 0; 
        map.clear();
        
        ridMap.clear();
        
        sequencesMap.clear(); 
        uploadedFiles.clear();
        
        navigator.home();
 
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
    }
 
    public String getSequenceList() {
        return sequenceList;
    }

    public void setSequenceList(String sequenceList) {
        this.sequenceList = sequenceList;
    }

    public boolean isIsMax() {
        return uploadedFiles.size() >= MAX_UPLOADED_FILES;
    }

    public BlastMetadata getMetadata() {
        return metadata;
    }
 
    /**
     * Description of blast database
     * @return 
     */
    public String getDbFullName() { 
        switch (database) {
            case "nrm":
                return languages.isIsSwedish() ? "Svenska ryggradsdjur (COI, 16S)" : "Swedish vertebrate animals (COI, 16S)";
            case "bold":
                return languages.isIsSwedish() ? "Barkodsekvenser för svenska organismer (COI, matK, rbcL, 16S*)" : "Barcode sequences for Swedish organisms (COI, matK, rbcL, 16S*)";
            default:
                return languages.isIsSwedish() ? "Barkodsekvenser från Genbank (COI, matK, rbcL, 16S*)" : "Barcode sequences from Genbank (COI, matK, rbcL, 16S*)";
        }
    } 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    




    

 




    
    public void openLowMatchList(BlastMetadata metadata) {
        log.info("openLowMatchList : {}", metadata);
        metadata.setOpenLowMatch(true);
    }
    
    public void remotBlast(BlastMetadata metadata) {
        log.info("remotBlast");
        
        String seq = metadata.getSequence(); 
        String rid = blaster.remoteGenbankBlast(seq); 
    
        ridMap.remove(metadata);
        ridMap.put(metadata, rid);
    }
    

    
    public void getSingleMap(BlastSubjectMetadata subMeta) {
        
        log.info("getSingleMap : {}", subMeta.getCoordinates());
        
        advancedModel = new DefaultMapModel();
        centLat = 0;
        centLnt = 0;
        zoom = 6;
        
        String strLat;
        String strLng;
                 
        selectedRecord = null;
        
        if(subMeta.getCatalogNumber() != null && subMeta.getCatalogNumber().length() > 0) {
            selectedRecord = map.get(subMeta.getCatalogNumber()); 
        
            if(selectedRecord != null && selectedRecord.getLat() != null && selectedRecord.getLnt() != null) {
                
                centLat = record.getLat1();
                centLnt = record.getLnt1();
                LatLng coord = new LatLng(record.getLat1(), record.getLnt1()); 
                advancedModel.addOverlay(new Marker(coord, record.getFullname(), record, ConstantString.getInstance().getMapMarkPath()));   
            }
        } else { 
            String strCoordinates = subMeta.getCoordinates();
            
            if(strCoordinates.contains("S")) {
                strLat = StringUtils.substringBefore(strCoordinates, "_S_"); 
                centLat = HelpClass.isNumeric(strLat) ? Double.valueOf(strLat) * (-1) : 0;
                
                if(strCoordinates.contains("W")) {
                    strLng = StringUtils.substringBetween(strCoordinates, "_S_", "_W"); 
                    centLnt = HelpClass.isNumeric(strLng) ? Double.valueOf(strLng) * (-1) : 0;
                } else if(strCoordinates.contains("E")){
                    strLng = StringUtils.substringBetween(strCoordinates, "_S_", "_E");
                    centLnt = HelpClass.isNumeric(strLng) ? Double.valueOf(strLng) : 0;
                } 
            } else if(strCoordinates.contains("N")){
                strLat = StringUtils.substringBefore(strCoordinates, "_N_");
                centLat = HelpClass.isNumeric(strLat) ? Double.valueOf(strLat) : 0;
                
                if(strCoordinates.contains("W")) {
                    strLng = StringUtils.substringBetween(strCoordinates, "_N_", "_W"); 
                    centLnt = HelpClass.isNumeric(strLng) ? Double.valueOf(strLng) * (-1) : 0;
                } else if(strCoordinates.contains("E")){
                    strLng = StringUtils.substringBetween(strCoordinates, "_N_", "_E");
                    centLnt = HelpClass.isNumeric(strLng) ? Double.valueOf(strLng) : 0;
                }
            } 
            LatLng coord = new LatLng(centLat, centLnt);  
            advancedModel.addOverlay(new Marker(coord, centLat + " N " + centLnt + " E", null, ConstantString.getInstance().getMapMarkPath()));   
        } 
        
//        RequestContext.getCurrentInstance().openDialog("gmappage");
    }

    public MapModel getAdvancedModel() {
        return advancedModel;
    }
    
        
    public void onMarkerSelect(OverlaySelectEvent event) {
        
        log.info("onMark selected ");
        Marker marker = (Marker) event.getOverlay(); 
    }
    
    
    public void getImages(BlastSubjectMetadata subMeta) {
        log.info("getImages : {}", subMeta);
        
        selectedRecord = map.get(subMeta.getCatalogNumber());  
        images = selectedRecord.getImages();
    }
    
    
    
    
 

    public List<MorphyBankImage> getImages() {
        return images;
    }
    
     
    public void getAlignment(BlastSubjectMetadata subjectMetadata) {
        log.info("getAlignment {}", subjectMetadata);
         
        selectedSubMetadata = subjectMetadata;
        selectedHsps = selectedSubMetadata.getSubjectHspList(); 
    }

    public List<BlastSubjectHsp> getSelectedHsps() {
        return selectedHsps;
    }

    public BlastSubjectMetadata getSelectedMetadata() {
        return selectedMetadata;
    }

    public BlastSubjectMetadata getSelectedSubMetadata() {
        return selectedSubMetadata;
    }

    public List<ResultBean> getResultBeans() { 
        return resultBeans;
    }
  
    
    
    
    
     
    public void getFeedback(BlastSubjectMetadata subMeta) {
        log.info("getFeedback : {}", subMeta);
    }
    
    public void createsequences() { 
        log.info("createsequences : {}", numOfTestSeqs);
         
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i <= numOfTestSeqs; i++) { 
            sb.append(FastaFiles.getInstance().getSequence(i)); 
        } 
        sequenceList = sb.toString();
    }
    
    public void onRowToggleSingle(ToggleEvent event) {
        log.info("onRowToggleSingle : {}", event);
         
        record = null;
        
        selectedMetadata =(BlastSubjectMetadata) event.getData();
         
        String catalognumber = selectedMetadata.getCatalogNumber(); 
        solrAvailble = false;
        
        if(!StringUtils.isEmpty(catalognumber)) {
            try {
                if(map.containsKey(catalognumber)) {
                    record = map.get(catalognumber);
                } else {
                    record = solr.getRecordByCollectionObjectCatalognumber(catalognumber); 
                    map.put(catalognumber, record);
                } 
                solrAvailble = true;
            } catch(Exception e) {
                solrAvailble = false;
            } 
        } else {
            String strCoordinates = selectedMetadata.getCoordinates();
            coordinates = StringUtils.replaceChars(strCoordinates, "_", " ");
        }
    }
 
    
    public void databaseChanged(final AjaxBehaviorEvent event) {
        log.info("databaseChanged : {}", database);  
    }

  
    
 

    public boolean isSolrAvailble() {
        return solrAvailble;
    }
  

    public List<BlastMetadata> getListMetadata() {
        return listMetadata;
    }

    public String getUrlEncode() {
        return urlEncode;
    }

    public BlastMetadata getBlastMetadata() {
        return blastMetadata;
    }

    public String getBlastportalUrl() {
        return "https://blast.ncbi.nlm.nih.gov/Blast.cgi?PROGRAM=blastn&PAGE_TYPE=BlastSearch&LINK_LOC=blasthome"; 
    }

     
    public int getNumOfTestSeqs() {
        return numOfTestSeqs;
    }

    public void setNumOfTestSeqs(int numOfTestSeqs) {
        this.numOfTestSeqs = numOfTestSeqs;
    }
 
    public List<UploadedFile> getUploadedFiles() {
        return uploadedFiles;
    }

    public void setUploadedFiles(List<UploadedFile> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }

    public SolrRecord getRecord() {
        return record;
    }
 
    public SolrRecord getSelectedRecord() {
        return selectedRecord;
    }

//    public String getDbVersion() {
//        return dbVersion;
//    }
//
//    public String getStatisticDbLength() {
//        return statisticDbLength;
//    }
//
//    public String getStatisticDbNumber() {
//        return statisticDbNumber;
//    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public double getCentLat() {
        return centLat;
    }

    public void setCentLat(double centLat) {
        this.centLat = centLat;
    }

    public double getCentLnt() {
        return centLnt;
    }

    public void setCentLnt(double centLnt) {
        this.centLnt = centLnt;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public int getZoom() {
        return zoom;
    } 
    
    public String getTestSequences() {
        return testSequences;
    }

    public void setTestSequences(String testSequences) {
        this.testSequences = testSequences;
    }

    public String getBlastdocument() {
        return "http://blast.ncbi.nlm.nih.gov/Blast.cgi?CMD=Web&PAGE_TYPE=BlastDocs";
    }
    
    public String getBlastPlus() {
        return "http://blast.ncbi.nlm.nih.gov/Blast.cgi?CMD=Web&PAGE_TYPE=BlastDocs&DOC_TYPE=Download";
    }
     
 
    public String getGenbankRidUrl() {
        return "http://www.ncbi.nlm.nih.gov/blast/Blast.cgi?CMD=Get&RID=";
    }
 
    public String ridByMetadata(BlastMetadata metadata) {
        log.info("ridByMetadata : {}", metadata);
        return ridMap.get(metadata);
    }
    
    
    public String getFeedMsgUrlen() {
        return feedMsgUrlen;
    }
 

    public String getFeedMsgUrlsv() {
        return feedMsgUrlsv;
    }

    public int getTotalSequences() {
        return totalSequences;
    }


}