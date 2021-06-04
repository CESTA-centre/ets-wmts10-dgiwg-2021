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

    @Test(groups="A WMTS Server shall support HTTP GET operation using KVP (clause 8 of OGC WMS) and RESTful (clause 10 of OGC WMTS 1.0) encodings.",description = "Checks wmts get tile KVP capability", dependsOnMethods = "verifyGetTileSupported")
    public void wmtsGetTileKVPRequestsExists() {
    	System.out.println("....wmtsGetTileKVPRequestsExists start " );
        getTileURI = ServiceMetadataUtils.getOperationEndpoint_KVP( this.wmtsCapabilities, DGIWGWMTS.GET_TILE,
                                                                    ProtocolBinding.GET );
        System.out.println("....wmtsGetTileKVPRequestsExists getTileURI : " + getTileURI);
        assertTrue( getTileURI != null,
                    "GetTile (GET) endpoint not found or KVP is not supported in ServiceMetadata capabilities document." );
        System.out.println("....wmtsGetTileKVPRequestsExists end " );
    }

    @Test(groups="A WMTS Server shall support HTTP GET operation using KVP (clause 8 of OGC WMS) and RESTful (clause 10 of OGC WMTS 1.0) encodings.",description = "Checks wmts get tile KVP parameters", dependsOnMethods = "wmtsGetTileKVPRequestsExists")
    public void wmtsGetTileRequestFormatParameters( ITestContext testContext ) {
    	System.out.println("....wmtsGetTileRequestFormatParameters start :  " + getTileURI);
        if ( getTileURI == null ) {
            getTileURI = ServiceMetadataUtils.getOperationEndpoint_KVP( this.wmtsCapabilities, DGIWGWMTS.GET_TILE,
                                                                        ProtocolBinding.GET );
        }
        String requestFormat = null;

        try {
            XPath xPath = createXPath();
            System.out.println("....wmtsGetTileRequestFormatParameters xPath :  " + xPath);
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
            System.out.println("....wmtsGetTileRequestFormatParameters imageFormats :  " + imageFormats.getLength());

            SoftAssert sa = new SoftAssert();

            for ( int i = 0; i < imageFormats.getLength(); i++ ) {
                this.reqEntity.removeKvp( DGIWGWMTS.FORMAT_PARAM );

                requestFormat = imageFormats.item( i ).getTextContent().trim();
                this.reqEntity.addKvp( DGIWGWMTS.FORMAT_PARAM, requestFormat );
                System.out.println("....wmtsGetTileRequestFormatParameters requestFormat :  " + requestFormat);
                ClientResponse rsp = wmtsClient.submitRequest( this.reqEntity, getTileURI );
                System.out.println("....wmtsGetTileRequestFormatParameters rsp 1 :  " + rsp);
                storeResponseImage( rsp, "Requirement5", "simple", requestFormat );
                System.out.println("....wmtsGetTileRequestFormatParameters rsp 2 :  " + rsp);

                sa.assertTrue( rsp.hasEntity(), ErrorMessage.get( ErrorMessageKey.MISSING_XML_ENTITY ) );
                WmtsAssertion.assertStatusCode( sa, rsp.getStatus(), 200 );
                WmtsAssertion.assertContentType( sa, rsp.getHeaders(), requestFormat );
                System.out.println("....wmtsGetTileRequestFormatParameters sa :  " + sa);
            }
            sa.assertAll();
        } catch ( XPathExpressionException | XPathFactoryConfigurationException xpe ) {
            assertTrue( false, "Invalid or corrupt XML or KVP structure:  " + xpe.getMessage() );
        }
        System.out.println("....wmtsGetTileRequestFormatParameters end" );
    }

    private XPath createXPath()
                            throws XPathFactoryConfigurationException {
        XPathFactory factory = XPathFactory.newInstance( XPathConstants.DOM_OBJECT_MODEL );
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext( NS_BINDINGS );
        return xpath;
    }
    

}