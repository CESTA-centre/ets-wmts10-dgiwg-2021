package org.opengis.cite.wmts10.core.domain;

/**
 * Wraps the results from interactive tests.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class InteractiveTestResult {

    

    private final boolean getFeatureInfoExceptionInEnglishLanguage;

   

    public InteractiveTestResult( 
                                  boolean getFeatureInfoExceptionInEnglishLanguage
                                  ) {
        
        this.getFeatureInfoExceptionInEnglishLanguage = getFeatureInfoExceptionInEnglishLanguage;
  
    }


    /**
     * @return <code>true</code> if the test (GetFeatureInfo exception in english language) passed, <code>false</code>
     *         otherwise
     */
    public boolean isGetFeatureInfoExceptionInEnglishLanguage() {
        return getFeatureInfoExceptionInEnglishLanguage;
    }



}
