package org.opengis.cite.wmts10.core;

import static org.opengis.cite.wmts10.core.util.ServiceMetadataUtils.parseLayerInfo;


import java.io.File;
import java.net.URI;
import java.util.Map;
import java.util.logging.Level;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.Reporter;
import org.w3c.dom.Document;

import org.opengis.cite.wmts10.core.domain.DGIWGWMTS;
import org.opengis.cite.wmts10.core.domain.InteractiveTestResult;
import org.opengis.cite.wmts10.core.domain.SuiteAttribute;

// From ets-dgiwg-core
import de.latlon.ets.core.util.TestSuiteLogger;
import de.latlon.ets.core.util.URIUtils;
import org.opengis.cite.wmts10.core.TestRunArg;


/**
 * A listener that performs various tasks before and after a test suite is run, usually concerned with maintaining a
 * shared test suite fixture. Since this listener is loaded using the ServiceLoader mechanism, its methods will be
 * called before those of other suite listeners listed in the test suite definition and before any annotated
 * configuration methods.
 * 
 * Attributes set on an ISuite instance are not inherited by constituent test group contexts (ITestContext). However,
 * suite attributes are still accessible from lower contexts.
 * 
 * @see org.testng.ISuite ISuite interface
 */
public class SuiteFixtureListener implements ISuiteListener {

    @Override
    public void onStart( ISuite suite ) {
        processWmtsParameter( suite );
        Reporter.clear(); // clear output from previous test runs
        StringBuilder str = new StringBuilder( "Initial test run parameters:\n" );
        str.append( suite.getXmlSuite().getAllParameters().toString() );
        Reporter.log( str.toString() );
        TestSuiteLogger.log( Level.CONFIG, str.toString() );
    }

    @Override
    public void onFinish( ISuite suite ) {
        Reporter.log( "Success? " + !suite.getSuiteState().isFailed() );
        String reportDir = suite.getOutputDirectory();
        String msg = String.format( "Test run directory: %s",
                                    reportDir.substring( 0, reportDir.lastIndexOf( File.separatorChar ) ) );
        Reporter.log( msg );
    }

    /**
     * Processes the "wmts" test suite parameter that specifies a URI reference for the service description
     * (capabilities document). The URI is dereferenced and the entity is parsed; the resulting Document object is set
     * as the value of the {@link SuiteAttribute#TEST_SUBJECT testSubject} suite attribute.
     * 
     * @param suite
     *            An ISuite object representing a TestNG test suite.
     */
    void processWmtsParameter( ISuite suite ) {
        Map<String, String> params = suite.getXmlSuite().getParameters();
        String wmtsRef = params.get( TestRunArg.WMTS.toString() );
        if ( ( null == wmtsRef ) || wmtsRef.isEmpty() ) {
            throw new IllegalArgumentException( "Required parameter not found" );
        }
        URI wmtsURI = URI.create( wmtsRef );
        Document doc = null;
        try {
            doc = URIUtils.resolveURIAsDocument( wmtsURI );
            if ( !DGIWGWMTS.WMTS_CAPABILITIES.equals( doc.getDocumentElement().getLocalName() ) ) {
                throw new RuntimeException( "Did not receive WMTS ServeiceMetadata capabilities document: "
                                            + doc.getDocumentElement().getNodeName() );
            }
        } catch ( Exception ex ) {
            // push exception up through TestNG ISuiteListener interface
            throw new RuntimeException( "Failed to parse resource located at " + wmtsURI, ex );
        }
        if ( null != doc ) {
            suite.setAttribute( SuiteAttribute.TEST_SUBJECT.getName(), doc );
            suite.setAttribute( SuiteAttribute.LAYER_INFO.getName(), parseLayerInfo( doc ) );
            suite.setAttribute( SuiteAttribute.IS_VECTOR.getName(), parseBoolean( params, TestRunArg.VECTOR ) );
            suite.setAttribute( SuiteAttribute.INTERACTIVE_TEST_RESULT.getName(), parseInteractiveTestResults( params ) );
        }
    }
    
    private Object parseInteractiveTestResults( Map<String, String> params ) {
        
        boolean getFeatureInfoExceptionInEnglishLanguage = parseBoolean( params,
                                                                         TestRunArg.GETFEATUREINFO_EXCEPTION_IN_ENGLISH );
        boolean getCapabilitiesExceptionInEnglishLanguage = parseBoolean( params, TestRunArg.GETCAPABILITIES_EXCEPTION_IN_ENGLISH);
        boolean getTileExceptionInEnglishLanguage = parseBoolean( params, TestRunArg.GETTILE_EXCEPTION_IN_ENGLISH);
        boolean getCapabilitiesInEnglishLanguage = parseBoolean( params, TestRunArg.GETCAPABILITIES_IN_ENGLISH);
        boolean getFeatureInfoResponseInEnglishLanguage = parseBoolean( params, TestRunArg.GETFEATUREINFO_IN_ENGLISH);

        return new InteractiveTestResult( 
        		getFeatureInfoExceptionInEnglishLanguage,
        		getCapabilitiesExceptionInEnglishLanguage,
        		getTileExceptionInEnglishLanguage,
        		getCapabilitiesInEnglishLanguage,
        		getFeatureInfoResponseInEnglishLanguage
        		);
    }

    private boolean parseBoolean( Map<String, String> params, TestRunArg arg ) {
        String key = arg.toString();
        if ( params.containsKey( key ) ) {
            String vectorParam = params.get( key );
            return Boolean.parseBoolean( vectorParam );
        }
        return false;
    }

}
