package org.opengis.cite.wmts10.core.dgiwg.testsuite.gettile;

import static org.testng.Assert.assertTrue;

import java.net.URI;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.opengis.cite.wmts10.core.assertion.WmtsAssertion;
import org.opengis.cite.wmts10.core.domain.ProtocolBinding;
import org.opengis.cite.wmts10.core.domain.DGIWGWMTS;
import org.opengis.cite.wmts10.core.util.ServiceMetadataUtils;
import org.opengis.cite.wmts10.core.dgiwg.testsuite.gettile.AbstractBaseGetTileFixture;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.jersey.api.client.ClientResponse;

import de.latlon.ets.core.error.ErrorMessage;
import de.latlon.ets.core.error.ErrorMessageKey;

/**
 *
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class GetTileParametersKvp extends AbstractBaseGetTileFixture {
    /**
     * DGIWG WMTS requirement 2
     * A WMTS Server shall support HTTP GET operation using KVP (clause 8 of OGC WMS) and RESTful (clause 10 of OGC WMTS 1.0) encodings
     */

    private URI getTileURI = null;

    @Test(groups={"A WMTS Server shall support HTTP GET operation using KVP (clause 8 of OGC WMS) and RESTful (clause 10 of OGC WMTS 1.0) encodings."},
    		description = "Checks wmts get tile KVP capability", dependsOnMethods = "verifyGetTileSupported")
    public void wmtsGetTileKVPRequestsExists() {
        getTileURI = ServiceMetadataUtils.getOperationEndpoint_KVP( this.wmtsCapabilities, DGIWGWMTS.GET_TILE,
                                                                    ProtocolBinding.GET );
        assertTrue( getTileURI != null,
                    "GetTile (GET) endpoint not found or KVP is not supported in ServiceMetadata capabilities document." );
    }

    @Test(groups={"A WMTS Server shall support HTTP GET operation using KVP (clause 8 of OGC WMS) and RESTful (clause 10 of OGC WMTS 1.0) encodings."},
    		description = "Checks wmts get tile KVP parameters", dependsOnMethods = "wmtsGetTileKVPRequestsExists")
    public void wmtsGetTileRequestFormatParameters( ITestContext testContext ) {
        if ( getTileURI == null ) {
            getTileURI = ServiceMetadataUtils.getOperationEndpoint_KVP( this.wmtsCapabilities, DGIWGWMTS.GET_TILE,
                                                                        ProtocolBinding.GET );
        }
        String requestFormat = null;

		// CESTA supersedes TILE_COL_PARAM, TILE_ROW_PARAM and TILE_MATRIX_PARAM to be sure that the tile really exists
		this.reqEntity.removeKvp(DGIWGWMTS.TILE_COL_PARAM);
		this.reqEntity.addKvp(DGIWGWMTS.TILE_COL_PARAM, "1");
		this.reqEntity.removeKvp(DGIWGWMTS.TILE_ROW_PARAM);
		this.reqEntity.addKvp(DGIWGWMTS.TILE_ROW_PARAM, "1");
		this.reqEntity.removeKvp(DGIWGWMTS.TILE_MATRIX_PARAM);
		this.reqEntity.addKvp(DGIWGWMTS.TILE_MATRIX_PARAM, "2");
        
        
        try {
            XPath xPath = createXPath();
            String layerName = this.reqEntity.getKvpValue( DGIWGWMTS.LAYER_PARAM );
            if ( layerName == null ) {
                NodeList layers = ServiceMetadataUtils.getNodeElements( xPath, wmtsCapabilities,
                                                                        "//wmts:Contents/wmts:Layer/ows:Identifier" );
                if ( layers.getLength() > 0 ) {
                    layerName = ( (Node) layers.item( 0 ) ).getTextContent().trim();
                }
            }
            NodeList imageFormats = ServiceMetadataUtils.getNodeElements( xPath, wmtsCapabilities,
                                                                          "//wmts:Contents/wmts:Layer[ows:Identifier = '"
                                                                                                  + layerName
                                                                                                  + "']/wmts:Format" );

            SoftAssert sa = new SoftAssert();

            for ( int i = 0; i < imageFormats.getLength(); i++ ) {
                this.reqEntity.removeKvp( DGIWGWMTS.FORMAT_PARAM );

                requestFormat = imageFormats.item( i ).getTextContent().trim();
                this.reqEntity.addKvp( DGIWGWMTS.FORMAT_PARAM, requestFormat );
                ClientResponse rsp = wmtsClient.submitRequest( this.reqEntity, getTileURI );
                storeResponseImage( rsp, "Requirement5", "simple", requestFormat );

                sa.assertTrue( rsp.hasEntity(), ErrorMessage.get( ErrorMessageKey.MISSING_XML_ENTITY ) );
                WmtsAssertion.assertStatusCode( sa, rsp.getStatus(), 200 );
                WmtsAssertion.assertContentType( sa, rsp.getHeaders(), requestFormat );
            }
            sa.assertAll();
        } catch ( XPathExpressionException | XPathFactoryConfigurationException xpe ) {
            assertTrue( false, "Invalid or corrupt XML or KVP structure:  " + xpe.getMessage() );
        }
    }

    private XPath createXPath()
                            throws XPathFactoryConfigurationException {
        XPathFactory factory = XPathFactory.newInstance( XPathConstants.DOM_OBJECT_MODEL );
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext( NS_BINDINGS );
        return xpath;
    }
    

}