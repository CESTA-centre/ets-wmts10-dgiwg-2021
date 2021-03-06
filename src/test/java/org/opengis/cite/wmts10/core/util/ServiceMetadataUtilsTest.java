package org.opengis.cite.wmts10.core.util;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.testng.Assert.assertTrue;
import static org.opengis.cite.wmts10.core.util.ServiceMetadataUtils.parseLayers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.opengis.cite.wmts10.core.domain.LayerInfo;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.opengis.cite.wmts10.core.domain.ProtocolBinding;


/**
 * Unit tests for service metadata on GetCapabilities document obtained from Geoportail server.
 * Refer to the following document: DGIWG 124 (STD-DP-18-001) Service Metadata Content p25.
 * 
 * @author <a href="mailto:"goltz@lat-lon.de">Lyn Goltz></a>
 * @author <a href="mailto:"O-LG@github">O. Le Gall></a>
 * @author <a href="mailto:"P-MAUGE@github">P. Maugé></a>
 */

public class ServiceMetadataUtilsTest {
	// TODO : requirement 2
	@Test
	public void testGetOperationBindings() throws Exception {
		Set<ProtocolBinding> globalBindings = ServiceMetadataUtils.getOperationBindings(wmtsCapabilities(), "GetTile");
		assertThat(globalBindings.size(), is(1));
		assertThat(globalBindings, hasItems(ProtocolBinding.GET));
	}
	
	@Test
    public void testGetOperationEndpoint()
                            throws Exception {
        URI endpointUri = ServiceMetadataUtils.getOperationEndpoint_KVP( wmtsCapabilities(), "GetTile",
                                                                         ProtocolBinding.GET );
        assertThat( endpointUri, is( new URI( "https://wxs.ign.fr/pratique/geoportail/wmts" ) ) );
    }
	
	@Test
    public void testGetOperationEndpointUnsupportedProtocol()
                            throws Exception {
        URI endpointUri = ServiceMetadataUtils.getOperationEndpoint_REST( wmtsCapabilities(), "GetTile",
                                                                          ProtocolBinding.POST );
        assertThat( endpointUri, is( nullValue() ) );
    }
	
	
	//TODO : Service metadata content b)7. p 25 of STD-DP-18-001
	@Test
    public void testP()
                    throws Exception {
        List<String> supportedFormats = ServiceMetadataUtils.parseSupportedFormats( wmtsCapabilities());
        assertThat( supportedFormats.size(), is( 2 ) );
        assertTrue( supportedFormats.stream().allMatch( Arrays.asList("image/png", "image/gif", "image/jpeg")::contains) );
    }
	
	
	// SOAP Encodings : Optional recommendation n°1 but wanted (see STD-DP-18-001 p 11)
	@Test
    public void testGetOperationEndpoint_Soap()
                            throws Exception {
        URI soapEndpoint = ServiceMetadataUtils.getOperationEndpoint_SOAP( wmtsCapabilities(),
                                                                             "GetFeatureInfo",
                                                                             ProtocolBinding.POST );
        assertThat( soapEndpoint, is( new URI( "http://ips.terrapixel.com/terrapixel/cubeserv.cgi" ) ) );
    }
	
	// 2 layers GEOGRAPHICALGRIDSYSTEMS.PLANIGNV2 and ORTHOIMAGERY.ORTHOPHOTOS that are available
	// with the PRATIQUE key
	@Test
    public void testParseLayerInfo()
                            throws Exception {
        List<LayerInfo> layerInfos = ServiceMetadataUtils.parseLayerInfo( wmtsCapabilities() );
        assertThat( layerInfos.size(), is( 2 ) );
    }
	
	// TODO
	@Test
    public void testParseAllLayerNodes()
                            throws Exception {
        NodeList allLayerNodes = parseLayers( wmtsCapabilities() );
        assertThat( allLayerNodes.getLength(), is( 2 ) );
    }
	
	@Test(expected = Exception.class)
    public void testParseAllLayerNodesWithNullShouldThrowException()
                            throws Exception {
        parseLayers( null );
    }
	
	@Test(expected = Exception.class)
    public void testParseRequestableLayerNodesWithNullShouldThrowException()
                            throws Exception {
        parseLayers( null );
    }

	private Document wmtsCapabilities() throws SAXException, IOException, ParserConfigurationException {
		// Warning : the file comes from IGN service (Geoportail).
		// In order to check SOAP encoding ability (optional in recommendations of WMTS DGIWG profile), lines n°107 to n°119 have been added manually.
		// THIS IS NOT ORIGINAL DATA
		return capabilities("../wmtsGetCapabilities_ignresponse.xml");
	}

	private Document capabilities(String resource) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputStream wmtsCapabilities = ServiceMetadataUtilsTest.class.getResourceAsStream(resource);
		return builder.parse(new InputSource(wmtsCapabilities));
	}

}
