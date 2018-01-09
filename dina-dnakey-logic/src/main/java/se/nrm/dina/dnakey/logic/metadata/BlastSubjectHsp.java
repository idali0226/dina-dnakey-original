package se.nrm.dina.dnakey.logic.metadata;

import java.util.ArrayList;
import java.util.List; 
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  
//import se.nrm.dina.dnakey.common.util.HelpClass;

/**
 *
 * @author idali
 */
public final class BlastSubjectHsp implements Comparable {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    public static final String TEXT_BLACK = "blacktext";
    public static final String TEXT_GRAY = "graytext";
    
    private final String hspScore;
    private final String hspBitScore;
    private final String hspEvalue;
    private final int hspQueryFrom;
    private final int hspQueryTo;
    private final int hspHitFrom;
    private final int hspHitTo;
    private final int hspIdentity;
    private final String hspPositive;
    private final int hspGaps;
    private final int hspAlignLen; 
    private final String hspQseq;
    private final String hspHseq;
    private final String hspMidline;
    private final int hspQueryFrame;
    private final int hspHitFrame;
    private long percentage; 
     
    private static final String NUMBER_FORMAT_ERROR = "Not available.";
 
    public BlastSubjectHsp(final String hspScore, final String hspBitScore, final String hspEvalue, 
                           final int hspQueryFrom, final int hspQueryTo, final int hspHitFrom, 
                           final int hspHitTo, final int hspIdentity, final String hspPositive, 
                           final int hspGaps, final int hspAlignLen, final long percentage, 
                           final String hspQseq, final String hspHseq, final String hspMidline, 
                           final int hspQueryFrame, final int hspHitFrame) {
        this.hspScore = hspScore;
        this.hspBitScore = hspBitScore;
        this.hspEvalue = hspEvalue;
        this.hspQueryFrom = hspQueryFrom;
        this.hspQueryTo = hspQueryTo;
        this.hspHitFrom = hspHitFrom;
        this.hspHitTo = hspHitTo;
        this.hspIdentity = hspIdentity;
        this.hspPositive = hspPositive;
        this.hspGaps = hspGaps;
        this.hspAlignLen = hspAlignLen;
        this.hspQseq = hspQseq;
        this.hspHseq = hspHseq;
        this.hspMidline = hspMidline;
        this.hspQueryFrame = hspQueryFrame;
        this.hspHitFrame = hspHitFrame;
        this.percentage = percentage;
    }

    public static String getNUMBER_FORMAT_ERROR() {
        return NUMBER_FORMAT_ERROR;
    }

    public int getHspAlignLen() {
        return hspAlignLen;
    }

    public String getHspBitScore() {
        return hspBitScore;
    }

    public String getHspEvalue() {
        return hspEvalue;
    }

    public int getHspGaps() {
        return hspGaps;
    }

    public int getHspHitFrame() {
        return hspHitFrame;
    }

    public int getHspHitFrom() {
        return hspHitFrom;
    }

    public int getHspHitTo() {
        return hspHitTo;
    }

    public String getHspHseq() {
        return hspHseq;
    }

    public int getHspIdentity() {
        return hspIdentity;
    }

    public String getHspMidline() {
        return hspMidline;
    }

    public String getHspPositive() {
        return hspPositive;
    }

    public String getHspQseq() {
        return hspQseq;
    }

    public int getHspQueryFrame() {
        return hspQueryFrame;
    }

    public int getHspQueryFrom() {
        return hspQueryFrom;
    }

    public int getHspQueryTo() {
        return hspQueryTo;
    }

    public String getHspScore() {
        return hspScore;
    }

    public Logger getLogger() {
        return logger;
    }

   
    public String getTextColor() {   
        return percentage >= 99 ? TEXT_BLACK : TEXT_GRAY;
    }
 
    
    public void setPercentage() {
        double identities = Double.valueOf(hspIdentity);
        double alignLength = Double.valueOf(hspAlignLen); 
        this.percentage = (long) Math.floor((identities / alignLength) * 100 + 0.5d);
    }
    
    
    public Long getPercentage() {
        return percentage;
    }
    
    public String getIdentitiesPercentage() {

        String identitiesPercentage = "";
        try { 
            identitiesPercentage = String.valueOf(percentage) + "%";
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
        }
        return identitiesPercentage;
    }

    public String getGapsPercentage() {

        String gapsPercentage = "";
        try {
            double gaps = Double.valueOf(hspGaps);
            double alignLength = Double.valueOf(hspAlignLen);
            gapsPercentage = String.valueOf((long) Math.floor((gaps / alignLength) * 100)) + "%";
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
        }
        return gapsPercentage;
    }
 
    public String getHspStrand() { 
        String qFrame = "";
        String hFrame = "";
        try {
            qFrame = hspQueryFrame >= 0 ? "Plus" : "Minus";
            hFrame = hspHitFrame >= 0 ? "Plus" : "Minus";
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
        }
        return "Strand = " + qFrame + "/" + hFrame;
    }

    public List<AlignSequence> getSequencesAlignment() {

        List<AlignSequence> alignSequences = new ArrayList<AlignSequence>();

        String qrySeq = hspQseq;
        String sbjSeq = hspHseq;
        String midLine = hspMidline;


        int length = hspAlignLen;

        int qstart = hspQueryFrom;
        int qend = hspHitTo;

        int hstart = hspHitFrom;
        int hend = hspHitTo;

        int count = length / 60;
        int remainder = length % 60;

        if (count <= 0) {
            alignSequences.add(new AlignSequence(String.valueOf(qstart), String.valueOf(qend), qrySeq, "q"));
            alignSequences.add(new AlignSequence("", "", midLine, "m"));
            alignSequences.add(new AlignSequence(String.valueOf(hstart), String.valueOf(hend), sbjSeq, "s"));
        } else {
            for (int i = 1; i <= count; i++) {
                String q = StringUtils.substring(qrySeq, 0, 60);
                String h = StringUtils.substring(sbjSeq, 0, 60);
                String m = StringUtils.substring(midLine, 0, 60);

                qrySeq = StringUtils.substring(qrySeq, 60);
                sbjSeq = StringUtils.substring(sbjSeq, 60);
                midLine = StringUtils.substring(midLine, 60);

                int numOfChartq = getNumOfChart(q);
                int numOfCharth = getNumOfChart(h);

                int qnumEnd = qstart + 59 - numOfChartq;
                int hnumEnd = hstart + 59 - numOfCharth;

                alignSequences.add(new AlignSequence(String.valueOf(qstart), String.valueOf(qnumEnd), q, "q"));
                alignSequences.add(new AlignSequence("", "", m, "m"));
                alignSequences.add(new AlignSequence(String.valueOf(hstart), String.valueOf(hnumEnd), h, "s"));

                alignSequences.add(new AlignSequence("", "", "", "e"));

                qstart = qnumEnd + 1;
                hstart = hnumEnd + 1;
            }

            if (remainder > 0) { 
                alignSequences.add(new AlignSequence(String.valueOf(qstart), String.valueOf(qend), qrySeq, "q"));
                alignSequences.add(new AlignSequence("", "", midLine, "m"));
                alignSequences.add(new AlignSequence(String.valueOf(hstart), String.valueOf(hend), sbjSeq, "s"));
            }
        }
        return alignSequences;
    }

    private int getNumOfChart(String sequence) {
        return StringUtils.contains(sequence, "-") ? StringUtils.countMatches(sequence, "-") : 0;
    }

    @Override
    public int compareTo(Object o1) {
        return (this.getIdentitiesPercentage().compareTo(((BlastSubjectHsp) o1).getIdentitiesPercentage()) );
    }
}
