package se.nrm.dina.dnakey.logic.vo;

/**
 *
 * @author idali
 */
public class MorphyBankImage {
    
    private int morphybankImageId;
    private String morphybankImageLink;
    private String name;
    private String mbview;
    private String catalogNumber;
    private String thumb;

    MorphyBankImage() {
    }

    public MorphyBankImage(int morphybankImageId, String morphybankImageLink, String catalogNumber, 
                            String name, String mbview, String thumb) {
        this.morphybankImageId = morphybankImageId;
        this.morphybankImageLink = morphybankImageLink;
        this.name = name;
        this.mbview = mbview;
        this.catalogNumber = catalogNumber;
        this.thumb = thumb;
    }

    public int getMorphybankImageId() {
        return morphybankImageId;
    }

    public String getMorphybankImageLink() {
        return morphybankImageLink;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }
     
    public String getName() {
        return name;
    }

    public String getThumb() {
        return thumb; 
    }

    public String getMbview() {
        return mbview;
    }
     
    @Override
    public String toString() {
        return "MorphBankImage : [" + morphybankImageLink + " - " + thumb;
    }
}
