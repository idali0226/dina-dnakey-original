package se.nrm.dina.dnakey.logic.metadata;

import java.util.List;  
import se.nrm.dina.dnakey.logic.vo.MorphybankImageMetadata;

/**
 *
 * @author idali
 */
public class BlastTabularMetadata {

    private final String blast;
    private final String database;
    private final String query; 
    private final String reference;
    private final List<BlastTabular> list;
//    private Collectionobject collectionobject = null;
    private MorphybankImageMetadata imageMetadata = null;
    private BlastTabular selectedData;

    BlastTabularMetadata(final String blast, final String database, final String query, 
                         final String reference, final List<BlastTabular> list) {
        this.blast = blast;
        this.database = database;
        this.query = query; 
        this.reference = reference;
        this.list = list;
    }

    public String getBlast() {
        return blast;
    }

    public String getDatabase() {
        return database;
    }

    public String getQuery() {
        return query;
    }
  
    public String getReference() {
        return reference;
    }
 
    
    public List<BlastTabular> getList() {
        return list;
    }

//    public boolean isIsCollectionObject() {
//        return collectionobject == null ? false : true;
//    }

//    public Collectionobject getCollectionobject() {
//        return collectionobject;
//    }
//
//    public void setCollectionobject(Collectionobject collectionobject) {
//        this.collectionobject = collectionobject;
//    }

    public MorphybankImageMetadata getImageMetadata() {
        return imageMetadata;
    }

    public void setImageMetadata(MorphybankImageMetadata imageMetadata) {
        this.imageMetadata = imageMetadata;
    }

    public BlastTabular getSelectedData() {
        return selectedData == null ? list.get(0) :  selectedData;
    }

    public void setSelectedData(BlastTabular selectedData) {
        this.selectedData = selectedData;
    }
    
    
    
//    public String getCoResultDiv() {
//        return collectionobject == null ? CSSName.getInstance().DIV_INVISIBLE : CSSName.getInstance().DIV_VISIBLE;
//    }
}
