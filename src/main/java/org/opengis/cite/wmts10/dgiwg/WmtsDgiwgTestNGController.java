package org.opengis.cite.wmts10.dgiwg;

import java.io.File;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;

import org.w3c.dom.Document;

import org.opengis.cite.wmts10.core.AbstractWmtsTestNGController;

/**
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class WmtsDgiwgTestNGController extends AbstractWmtsTestNGController {

    /**
     * Default constructor uses the location given by the "user.home" system property as the root output directory.
     */
    public WmtsDgiwgTestNGController() {
        super();
    }

    /**
     * Construct a controller that writes results to the given output directory.
     * 
     * @param outputDirUri
     *            A file URI that specifies the location of the directory in which test results will be written. It will
     *            be created if it does not exist.
     */
    public WmtsDgiwgTestNGController( String outputDirUri ) {
        super( outputDirUri );
    }

    @Override
    protected URL getTestNgConfiguration() {
        return WmtsDgiwgTestNGController.class.getResource( "testng.xml" );
    }

    /**
     * A convenience method to facilitate test development.
     * 
     * @param args
     *            Test run arguments (optional). The first argument must refer to an XML properties file containing the
     *            expected set of test run arguments. If no argument is supplied, the file located at
     *            ${user.home}/test-run-props.xml will be used.
     * @throws Exception
     *             If the test run cannot be executed (usually due to unsatisfied pre-conditions).
     */
    public static void main( String[] args )
                    throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        File xmlArgs = findXmlArgs( args );
        Document testRunArgs = db.parse( xmlArgs );
        AbstractWmtsTestNGController controller = new WmtsDgiwgTestNGController();
        Source testResults = controller.doTestRun( testRunArgs );
        System.out.println( "Test results: " + testResults.getSystemId() );
    }

}
