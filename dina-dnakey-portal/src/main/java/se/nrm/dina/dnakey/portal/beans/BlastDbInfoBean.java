/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dnakey.portal.beans;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.dnakey.logic.BlastDbInfo;
import se.nrm.dina.dnakey.logic.vo.BlastDBVo; 

/**
 *
 * @author idali
 */  
@Named("dbInfo")
@SessionScoped 
@Slf4j
public class BlastDbInfoBean implements Serializable {
    
    private BlastDBVo dbVo;
     
    @Inject
    private BlastDbInfo dbInfo;
    
    public BlastDbInfoBean() { 
    }
    
    @PostConstruct
    public void init() {
        getBlastDbInfo();
    }
    
    private void getBlastDbInfo() {
        dbVo = dbInfo.getBlastDbVo();
    }

    public String getBoldTotalSequence() {
        return dbVo.getTotalBoldSequences();
    }

    public String getGenBankTotalSequence() {
        return dbVo.getTotalGenbankSequences();
    }

    public String getNrmTotalSequence() {
        return dbVo.getTotalNrmSequences();
    }
 
    
}
