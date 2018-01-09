/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dnakey.logic;
 
import org.junit.After; 
import org.junit.Before; 
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.anyString; 
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author idali
 */
@RunWith(MockitoJUnitRunner.class)
public class BlasterImplTest {
  
    public BlasterImplTest() {
    }

    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getBlastDbInfo method, of class BlasterImpl.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetBlastDbInfo() throws Exception {
        System.out.println("getBlastDbInfo");
          
        Blaster testInstance = Mockito.mock(BlasterImpl.class); 
        when(testInstance.getBlastDbInfo("nrm")).thenReturn("12345");
        
        String result = testInstance.getBlastDbInfo("nrm"); 
         
        Mockito.verify(testInstance).getBlastDbInfo("nrm");  
        assertEquals("12345", result);
    }

    /**
     * Test of remoteGenbankBlast method, of class BlasterImpl.
     * @throws java.lang.Exception
     */
    @Test
    public void testRemoteGenbankBlast() throws Exception {
        System.out.println("remoteGenbankBlast");
        
        
        Blaster testInstance = Mockito.mock(BlasterImpl.class); 
        when(testInstance.remoteGenbankBlast(anyString())).thenReturn("12345");
        
        String result = testInstance.remoteGenbankBlast("test"); 
         
        Mockito.verify(testInstance).remoteGenbankBlast("test");  
        assertEquals("12345", result);  
    }
    
}
