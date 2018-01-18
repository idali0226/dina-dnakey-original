/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dnakey.logic.vo;

import java.io.Serializable;

/**
 *
 * @author idali
 */
public class BlastDBVo implements Serializable {
    private final String totalNrmSequences;
    private final String totalBoldSequences;
    private final String totalGenbankSequences;
    
    public BlastDBVo(String totalNrmSequences, String totalBoldSequences, String totalGenbankSequences) {
        this.totalNrmSequences = totalNrmSequences;
        this.totalBoldSequences = totalBoldSequences;
        this.totalGenbankSequences = totalGenbankSequences;
    }

    public String getTotalNrmSequences() {
        return totalNrmSequences;
    }

    public String getTotalBoldSequences() {
        return totalBoldSequences;
    }

    public String getTotalGenbankSequences() {
        return totalGenbankSequences;
    } 
}
