package se.nrm.dina.dnakey.portal.controller;
  
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
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
import javax.servlet.http.HttpServletRequest;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  
import se.nrm.dina.dnakey.logic.BlastQueue;
import se.nrm.dina.dnakey.logic.Blaster; 
import se.nrm.dina.dnakey.logic.metadata.BlastMetadata;
import se.nrm.dina.dnakey.logic.metadata.BlastSubjectHsp;
import se.nrm.dina.dnakey.logic.metadata.BlastSubjectMetadata; 
import se.nrm.dina.dnakey.logic.util.HelpClass;
import se.nrm.dina.dnakey.logic.vo.MorphyBankImage;
import se.nrm.dina.dnakey.portal.beans.MessageBean;
import se.nrm.dina.dnakey.portal.beans.ResultBean; 
import se.nrm.dina.dnakey.portal.util.BlastHelper;
import se.nrm.dina.dnakey.portal.util.ConstantString;
import se.nrm.dina.dnakey.portal.logic.SequenceValidation;
import se.nrm.dina.dnakey.portal.solr.SolrClient;
import se.nrm.dina.dnakey.portal.solr.SolrRecord; 
import se.nrm.dina.dnakey.portal.util.FastaFiles;
import se.nrm.dina.dnakey.portal.util.FileHandler; 

/**
 *
 * @author idali
 */
@SessionScoped 
@Named("blast")
public class BlastBean implements Serializable {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private static final String MAP_MARK_PATH = "http://maps.google.com/mapfiles/ms/micons/blue-dot.png";
    private static final String URL_ENCODE = "param=dnakey&catalogNumber=";
    private static final String DEFAULT_BLASTDB = "nrm";
    
 
    private String genbankUrl;
    
    private String dbVersion;
    private String statisticDbNumber;
    private String statisticDbLength;
    
    
    private String sequence;
    private String testSequences;
    private List<String> sequences;
    private Map<String, List<String>> sequencesMap;
    
    private int totalSequences = 0;
     
    private String database; 
     
    private BlastMetadata blastMetadata;
    private BlastSubjectMetadata selectedMetadata;
    private BlastSubjectMetadata selectedSubMetadata;
    private List<BlastSubjectHsp> selectedHsps;
    
    private List<BlastMetadata> listMetadata = new ArrayList<>();
    private List<ResultBean> resultBeans = new ArrayList<>(); 
    
    private String feedMsgUrlsv;
    private String feedMsgUrlen;
    
    private MapModel advancedModel;
    
    private final String urlEncode;
    private final int MAX_UPLOADED_FILES = 5;
     
    
    private double centLat = 0;
    private double centLnt = 0;
    private String coordinates = "";
    private int zoom = 6;
 
    private int numOfTestSeqs = 0;  
    
    private List<String> filelist = new ArrayList<>();  
    
    private List<UploadedFile> uploadedFiles = new ArrayList<>(); 
    
    private String boldTotalSequence;
    private String genBankTotalSequence;
    private String nrmTotalSequence; 
    private SolrRecord selectedRecord;
    private Map<String, SolrRecord> map = new HashMap<>();
    private List<MorphyBankImage> images = new ArrayList<>();
    
    private String servername; 
    private SolrRecord record;
    private boolean solrAvailble = true;
     
    private int activeTab = 1;
     
    private Map<BlastMetadata, String> ridMap = new HashMap<>();
   
    private FacesContext context;
    RequestContext requestContext; 
  
    @Inject
    private Blaster blaster;
    
    @Inject
    private BlastQueue serviceQueue;
     
    
    @Inject
    private Languages languages;
    
    @Inject
    private SequenceValidation validation;
     
    @Inject
    private SolrClient solr; 
    
    @Inject
    private Navigator navigator;
    
    @Inject
    private MessageBean msg;
     
    
    public BlastBean() {
        logger.info("BlastBean");
        
        ridMap = new HashMap<>();
        map = new HashMap<>();
        sequences = new ArrayList<>();
        sequencesMap = new HashMap();
        database = DEFAULT_BLASTDB; 
        advancedModel = new DefaultMapModel();
        solrAvailble = true;
        urlEncode =  URL_ENCODE;
        centLat = 0;
        centLnt = 0;
        coordinates = "";  
        totalSequences = 0;
      activeTab = 1;
    }
    
    
    @PostConstruct
    public void init() {
//        logger.info("init : {}", searchQuery);
        context = FacesContext.getCurrentInstance();  
        boolean isPostBack = context.isPostback();        // false if page is start up or reloaded by browser  
              
        requestContext = RequestContext.getCurrentInstance();  
        
        if(servername == null) {
            servername = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerName();
        }
        
//        serverport  = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerPort();
        
        logger.info("isPostBack : {}", isPostBack);
        if(!isPostBack) {     
            map = new HashMap<>();
            sequences = new ArrayList<>();
            sequencesMap = new HashMap();
            sequence = "";
            testSequences = "";
            database = DEFAULT_BLASTDB; 
            advancedModel = new DefaultMapModel();
            solrAvailble = true;
            coordinates = "";
            centLat = 0;
            centLnt = 0;
              
            totalSequences = 0;
 
            clear(); 

            nrmTotalSequence = blaster.getBlastDbInfo("nrm");
            boldTotalSequence = blaster.getBlastDbInfo("bold");
            genBankTotalSequence = blaster.getBlastDbInfo("genbank");
            
            activeTab = 1;
            
            requestContext.update("sequenceform:mainpanel"); 
        }  
    }
     
    public void submit() {
        if(activeTab == 1) {
            submitSequences();
        } else if(activeTab == 2) {
            submitupload();
        } else if(activeTab == 3) {
            submitTestSequence();
        }
    }  

    private void run() {
        boolean isSequenceValid = validation.validate(sequences);
        if (isSequenceValid) {
            blast();
            navigator.result();
//            setTopMenuStyle(4);
        } else {
            if (sequences != null && sequences.size() > 1) {
                msg.createErrorMsgs(ConstantString.getInstance().getText("validationfailed_" + languages.getLocale()), validation.getErrorMsgs());
            }
            msg.addError(ConstantString.getInstance().getText("validationfailed_" + languages.getLocale()), validation.getErrorMsg());
        }
    }

    private void submitSequences() {
        logger.info("submitSequences : {}", database);
          
        resultBeans = new ArrayList<>(); 
        sequences = new ArrayList<>(); 
        if(StringUtils.isEmpty(sequence)) {
            msg.addError(ConstantString.getInstance().getText("blastfailed_" + languages.getLocale()), ConstantString.getInstance().getText("inputdata_" + languages.getLocale()));
        } else {  
            sequences = BlastHelper.prepareSequenceList(sequence);
            run();
        }
    }
     

    private void submitupload() {
        logger.info("submitupload : {}", database);

        resultBeans = new ArrayList<>();
        sequences = new ArrayList<>();
        if (sequencesMap == null || sequencesMap.isEmpty()) {
            msg.addError(ConstantString.getInstance().getText("blastfailed_" + languages.getLocale()), ConstantString.getInstance().getText("inputdata_" + languages.getLocale()));
        } else {
            sequencesMap.entrySet().stream().forEach((entry) -> {
                sequences.addAll(entry.getValue()); 
            });
            
            if (sequences.size() > 100) {
                sequences = sequences.subList(0, 99);
            }
//            for (UploadedFile file : uploadedFiles) {
//                if (convertUploadFile(file)) {
//                    break;
//                }
//            }
            run();
        }
    }
    
    public void submitTestSequence() {
        logger.info("submitTestSequence : {}", database);
          
        resultBeans = new ArrayList<>(); 
        sequences = new ArrayList<>(); 
        if(StringUtils.isEmpty(testSequences)) {
            msg.addError(ConstantString.getInstance().getText("blastfailed_" + languages.getLocale()), ConstantString.getInstance().getText("inputdata_" + languages.getLocale()));
        } else {
            sequences = BlastHelper.prepareSequenceList(testSequences);
            run();
        }
    }
//    
//    private boolean convertUploadFile(UploadedFile file) {
//        
//        logger.info("file is null ? {}", file);
//        if (file != null) {
//            try {
//                InputStream inputstream = file.getInputstream();
//                String seq = BlastHelper.converInputStreamIntoString(inputstream); 
//                sequences.addAll(BlastHelper.prepareSequenceList(seq)); 
//                if(sequences.size() > 100) {
//                    sequences = sequences.subList(0, 99);
//                    return true;
//                }
//            } catch (IOException ex) {
//                logger.error(ex.getMessage());
//            }
//        } 
//        return false;
//    }
    
    private void convertUploadFile(UploadedFile file) {
        
        logger.info("file is null ? {}", file);
        if (file != null) {
            try {
                InputStream inputstream = file.getInputstream();
                String seq = BlastHelper.converInputStreamIntoString(inputstream); 
                sequencesMap.put(file.getFileName(), BlastHelper.prepareSequenceList(seq)); 
//                sequences.addAll(BlastHelper.prepareSequenceList(seq)); 
//                if(sequences.size() > 100) {
//                    sequences = sequences.subList(0, 99);
//                    return true;
//                }
            } catch (IOException ex) {
                logger.error(ex.getMessage());
            }
        }  
    }

    /**
     * Upload file 
     * 
     * @param event 
     */
    public void handleFileUpload(FileUploadEvent event) {
        
        logger.info("handleFileUpload");
        
        if (uploadedFiles.size() < MAX_UPLOADED_FILES) {
            final UploadedFile uploadedFile = event.getFile(); 
            if (uploadedFile != null) {
                uploadedFiles.add(uploadedFile);
                
                convertUploadFile(uploadedFile);
                filelist.add(uploadedFile.getFileName()); 
            }
        }
    }

    /**
     * Blasts a single sequence
     * 
     * @param seq
     * 
     * @param output 
     */
    private void blast() {
        logger.info("blast : {}", sequences.size());
        
        ridMap = new HashMap<>();
        
        totalSequences = sequences.size();
        map = new HashMap<>();
        listMetadata = new ArrayList<>();
        
        List<String> fastaFilesPath = new ArrayList<>();
        
        try { 
            for (String seq : sequences) {
                fastaFilesPath.add(FileHandler.createFastaFile(seq, servername));
            }

            listMetadata = serviceQueue.run(fastaFilesPath, database);
            
//            listMetadata = blaster.multipleBlast(fastaFilesPath, database);
            
            BlastMetadata metadata = listMetadata.get(0);
            dbVersion = metadata.getVersion();
            statisticDbLength = metadata.getStatisticDbLength();
            statisticDbNumber = metadata.getStatisticDbNumber();
   
            String query;
            String catalogNumber;
            String scientificName;
            String genbankAcc;
            String boldId;
            String identity;
            String score;
            String evalue;
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
                if(!bm.isHasResult() ) { 
                    hasResult = false;
                    bm.setSequence(sequences.get(count));
                } 
                
                if(hasResult) {
                    query = bm.getQuery();
                    if(!bm.isHasHighMatach()) {
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
 
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        } finally {
            if(!fastaFilesPath.isEmpty()) {
                File f;
                for(String path : fastaFilesPath) {
                    f = new File(path);
                    if(f.exists()) {
                        f.delete();
                    }
                }
            }
        }  
    }
    
    public void openLowMatchList(BlastMetadata metadata) {
        logger.info("openLowMatchList : {}", metadata);
        metadata.setOpenLowMatch(true);
    }
    
    public void remotBlast(BlastMetadata metadata) {
        logger.info("remotBlast");
        
        String seq = metadata.getSequence(); 
        String rid = blaster.remoteGenbankBlast(seq); 
        ridMap.remove(metadata);
        ridMap.put(metadata, rid);
    }
    
    /**
     * changeTestNumber -- event when number of test sequences changed
     */
    public void changeTestNumber() {
        logger.info("changeTestNumber : {}", numOfTestSeqs); 
        
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i <= numOfTestSeqs; i++) { 
            sb.append(FastaFiles.getInstance().getSequence(i)); 
        } 
        testSequences = sb.toString();
    }    
    
    public void getSingleMap(BlastSubjectMetadata subMeta) {
        
        logger.info("getSingleMap : {}", subMeta.getCoordinates());
        
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
                advancedModel.addOverlay(new Marker(coord, record.getFullname(), record, MAP_MARK_PATH));   
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
            advancedModel.addOverlay(new Marker(coord, centLat + " N " + centLnt + " E", null, MAP_MARK_PATH));   
        } 
        
//        RequestContext.getCurrentInstance().openDialog("gmappage");
    }

    public MapModel getAdvancedModel() {
        return advancedModel;
    }
    
        
    public void onMarkerSelect(OverlaySelectEvent event) {
        
        logger.info("onMark selected ");
        Marker marker = (Marker) event.getOverlay(); 
    }
    
    
    public void getImages(BlastSubjectMetadata subMeta) {
        logger.info("getImages : {}", subMeta);
        
        selectedRecord = map.get(subMeta.getCatalogNumber());  
        images = selectedRecord.getImages();
    }
    
    
    
    
 

    public List<MorphyBankImage> getImages() {
        return images;
    }
    
     
    public void getAlignment(BlastSubjectMetadata subjectMetadata) {
        logger.info("getAlignment {}", subjectMetadata);
         
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
        logger.info("getFeedback : {}", subMeta);
    }
    
    public void createsequences() { 
        logger.info("createsequences : {}", numOfTestSeqs);
         
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i <= numOfTestSeqs; i++) { 
            sb.append(FastaFiles.getInstance().getSequence(i)); 
        } 
        sequence = sb.toString();
    }
    
    public void onRowToggleSingle(ToggleEvent event) {
        logger.info("onRowToggleSingle : {}", event);
         
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
        logger.info("databaseChanged : {}", database);  
    }

    
    public void removefile(UploadedFile file) {

        logger.info("removefile : {}", file.getFileName());

        logger.info("sequences map size before remove : {}", sequencesMap.size());
        uploadedFiles.remove(file);
        sequencesMap.remove(file.getFileName());
        logger.info("sequences map size after remove : {}", sequencesMap.size());
        
        filelist.remove(file.getFileName());
    }    
       
    public void newblast() {
        logger.info("newblast");
         
        totalSequences = 0; 
        map.clear();
        
        ridMap.clear();
        
        sequencesMap.clear();
        filelist.clear();
        uploadedFiles.clear();
//        setTopMenuStyle(0);
    }
 
    public void clear() {
        sequence = null;
        uploadedFiles = new ArrayList<>();
        testSequences = null;
        
        numOfTestSeqs = 0;
        
        database = "nrm"; 
        ridMap.clear(); 
    }
 
//    public void start() {
//        logger.info("start");
//        section = 0; 
//        setTopMenuStyle(section);
//    }
    
    

//    public void dnaidentification() {
//        logger.info("dnyidentification");
//        section = 1; 
////        setTopMenuStyle(section);
//        requestContext = RequestContext.getCurrentInstance();  
//        requestContext.update("headform:topmenupanel");  
//    }
    
//    public void about() {
//        logger.info("about");
//        section = 2; 
////        setTopMenuStyle(section);  
//        requestContext = RequestContext.getCurrentInstance();  
//        requestContext.update("headform:topmenupanel");  
//    }
    
//    public void contact() {
//        section = 6;
////        setTopMenuStyle(4); 
//         
//        requestContext = RequestContext.getCurrentInstance();  
//        requestContext.update("headform:topmenupanel"); 
//    }
    
//    public void streckkodsgenDef() {
//        section = 2;
////        setTopMenuStyle(2);
//        requestContext = RequestContext.getCurrentInstance();  
//        requestContext.update("headform:topmenupanel");   
//        
//        requestContext.scrollTo("streckkodsgen");
//    }
    
//    public void blastDefinition() {
//        
//        section = 2;
////        setTopMenuStyle(2);
//        requestContext = RequestContext.getCurrentInstance();  
//        requestContext.update("headform:topmenupanel");   
//        
//        requestContext.scrollTo("blast");
//    }
    
    
    
//    public void genbankdesc() {
//        section = 2;
////        setTopMenuStyle(2);
//        requestContext = RequestContext.getCurrentInstance();  
//        requestContext.update("headform:topmenupanel"); 
//        
//        requestContext.scrollTo("genbank");
//    }
        
//    public void bolddesc() {
//        section = 2;
////        setTopMenuStyle(2);
//        requestContext = RequestContext.getCurrentInstance();  
//        requestContext.update("headform:topmenupanel");  
//        
//        requestContext.scrollTo("bold");
//    }

    public boolean isSolrAvailble() {
        return solrAvailble;
    }
 

    
//    public void links() {
//        logger.info("links");
//        section = 3;
////        setTopMenuStyle(section);
//    }
 
//    public void pastesequence() {
//        logger.info("pastesequence");
//        section = 9;
//    }
    
//    public void uploadfile() {
//        logger.info("uploadfile");
//        section = 10;
//    }
//
//    public int getSection() {
//        return section;
//    }



    

        
    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
  
    public String getBoldTotalSequence() {
        return boldTotalSequence == null ? blaster.getBlastDbInfo("bold") : boldTotalSequence;
    }
 
    public String getGenBankTotalSequence() {
        return genBankTotalSequence == null ? blaster.getBlastDbInfo("genbank") : genBankTotalSequence; 
    }
  
    public String getNrmTotalSequence() {
        return nrmTotalSequence == null ? blaster.getBlastDbInfo("nrm") : nrmTotalSequence;  
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
 
    public void onTabChange(TabChangeEvent event) {
        logger.info("onTabChange : {}", event.getTab().getId()); 
        
        String tab = event.getTab().getId();
        switch (tab) {
            case "tab3":
                if(testSequences == null || testSequences.isEmpty()) {
                    numOfTestSeqs = 0;
                }   activeTab = 3;
                break;
            case "tab1":
                activeTab = 1;
                break;
            case "tab2":
                activeTab = 2;
                break;
        }
    }
     
    public int getNumOfTestSeqs() {
        return numOfTestSeqs;
    }

    public void setNumOfTestSeqs(int numOfTestSeqs) {
        this.numOfTestSeqs = numOfTestSeqs;
    }
 

    public List<String> getFilelist() {
        return filelist;
    }

    public void setFilelist(List<String> filelist) {
        this.filelist = filelist;
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
 
    public String getGenbankUrl() {
        return genbankUrl;
    }

    public SolrRecord getSelectedRecord() {
        return selectedRecord;
    }

    public String getDbVersion() {
        return dbVersion;
    }

    public String getStatisticDbLength() {
        return statisticDbLength;
    }

    public String getStatisticDbNumber() {
        return statisticDbNumber;
    }

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
}