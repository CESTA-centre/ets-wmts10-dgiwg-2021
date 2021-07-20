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


    public InteractiveTestResult( 
                                  boolean getFeatureInfoExceptionInEnglishLanguage,
                                  boolean getCapabilitiesExceptionInEnglishLanguage,
                                  boolean getTileExceptionInEnglishLanguage
                                  ) {
        
        this.getFeatureInfoExceptionInEnglishLanguage = getFeatureInfoExceptionInEnglishLanguage;
        this.getCapabilitiesExceptionInEnglishLanguage = getCapabilitiesExceptionInEnglishLanguage;
        this.getTileExceptionInEnglishLanguage = getTileExceptionInEnglishLanguage;
  
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



}
