package org.opengis.cite.wmts10.core.assertion;

//In ets-wms13-dgiwg, this method is in ets-dgiwg-core dependency (de.latlon.ets.wms13.core.assertion.WmsAssertion.assertVersion130)
import static org.opengis.cite.wmts10.core.assertion.WmtsAssertion.assertVersion100;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.opengis.cite.wmts10.core.util.ServiceMetadataUtilsTest;

/**
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class WmtsAssertTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
/*
    @Test
    public void testAssertVersion100()
                    throws Exception {
        assertVersion100( wmtsCapabilities() );
    }
*/
    private Document wmtsCapabilities()
                    throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware( true );
        DocumentBuilder builder = factory.newDocumentBuilder();
        System.out.println("!!!!!wmtsCapabilities  : ");
        InputStream wmtsCapabilities = ServiceMetadataUtilsTest.class.getResourceAsStream( "../wmtsGetCapabilities_OGCresponse.xml" );
        System.out.println("!!!!!wmtsCapabilities  : " + wmtsCapabilities);
        return builder.parse( new InputSource( wmtsCapabilities ) );
    }

}
