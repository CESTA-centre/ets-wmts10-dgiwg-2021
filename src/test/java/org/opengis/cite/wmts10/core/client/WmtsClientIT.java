package org.opengis.cite.wmts10.core.client;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import org.opengis.cite.wmts10.core.domain.DGIWGWMTS;
import org.opengis.cite.wmts10.core.domain.WmtsNamespaces;

/**
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class WmtsClientIT {

    public void testgetCapabilities()
                    throws Exception {
        WmtsClient wmtsClient = new WmtsClient( wmtsCapabilities() );

        Document capabilities = wmtsClient.getCapabilities();
        assertThat( capabilities.getLocalName(), is( DGIWGWMTS.WMTS_CAPABILITIES ) );
        assertThat( capabilities.getNamespaceURI(), is( WmtsNamespaces.WMTS ) );
    }

    private Document wmtsCapabilities()
                    throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware( true );
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream wmtsCapabilities = WmtsClientIT.class.getResourceAsStream( "wmtsGetCapabilities_OGCresponse.xml" );
        return builder.parse( new InputSource( wmtsCapabilities ) );
    }

}
