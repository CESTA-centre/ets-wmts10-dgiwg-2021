package org.opengis.cite.wmts10.core.domain;

/**
 * Wraps the results from interactive tests.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class InteractiveTestResult {

    private final boolean getFeatureInfoExceptionInEnglishLanguage;
    private final boolean getCapabilitiesExceptionInEnglishLanguage;
    private final boolean getTileExceptionInEnglishLanguage;
    private final boolean getCapabilitiesInEnglishLanguage;
    private final boolean getFeatureInfoResponseInEnglishLanguage;


    public InteractiveTestResult( 
                                  boolean getFeatureInfoExceptionInEnglishLanguage,
                                  boolean getCapabilitiesExceptionInEnglishLanguage,
                                  boolean getTileExceptionInEnglishLanguage,
                                  boolean getCapabilitiesInEnglishLanguage,
                                  boolean getFeatureInfoResponseInEnglishLanguage
                                  ) {
        
        this.getFeatureInfoExceptionInEnglishLanguage = getFeatureInfoExceptionInEnglishLanguage;
        this.getCapabilitiesExceptionInEnglishLanguage = getCapabilitiesExceptionInEnglishLanguage;
        this.getTileExceptionInEnglishLanguage = getTileExceptionInEnglishLanguage;
        this.getCapabilitiesInEnglishLanguage = getCapabilitiesInEnglishLanguage;
        this.getFeatureInfoResponseInEnglishLanguage = getFeatureInfoResponseInEnglishLanguage;
  
    }


    /**
     * @return <code>true</code> if the test (GetFeatureInfo exception in english language) passed, <code>false</code>
     *         otherwise
     */
    public boolean isGetFeatureInfoExceptionInEnglishLanguage() {
        return getFeatureInfoExceptionInEnglishLanguage;
    }
    
    /**
     * @return <code>true</code> if the test (GetCapabilities exception in english language) passed, <code>false</code>
     *         otherwise
     */
    public boolean isGetCapabilitiesExceptionInEnglishLanguage() {
    	return getCapabilitiesExceptionInEnglishLanguage;
    }
    
    /**
     * @return <code>true</code> if the test (GetTile exception in english language) passed, <code>false</code>
     *         otherwise
     */
    public boolean isGetTileExceptionInEnglishLanguage() {
    	return getTileExceptionInEnglishLanguage;
    }
    
    /**
     * @return <code>true</code> if the test (GetFeatureInfo in english language) passed, <code>false</code>
     *         otherwise
     */
    public boolean isGetFeatureInfoResponseInEnglishLanguage() {
    	return getFeatureInfoResponseInEnglishLanguage;
    }
    
    /**
     * @return <code>true</code> if the test (Getcapabilities in english language) passed, <code>false</code>
     *         otherwise
     */
    public boolean isGetCapabilitiesInEnglishLanguage() {
    	return getCapabilitiesInEnglishLanguage;
    }



}
