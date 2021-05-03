package org.opengis.cite.wmts10.core.util.request;

import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.GET_MAP;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.IMAGE_PNG;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class WmtsRequestBuilderTest {

    @Test
    public void testGetSupportedTransparentFormat()
                    throws Exception {
        String format = WmtsRequestBuilder.getSupportedTransparentFormat( WmtsCapabilities(), GET_MAP );

        assertThat( format, is( IMAGE_PNG ) );
    }

    private Document WmtsCapabilities()
                    throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware( true );
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream WmtsCapabilities = WmtsRequestBuilderTest.class.getResourceAsStream( "../../capabilities_Wmts100.xml" );
        return builder.parse( new InputSource( WmtsCapabilities ) );
    }

}
