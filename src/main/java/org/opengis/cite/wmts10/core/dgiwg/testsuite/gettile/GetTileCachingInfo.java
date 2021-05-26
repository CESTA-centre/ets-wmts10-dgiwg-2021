package org.opengis.cite.wmts10.core.dgiwg.testsuite.gettile;

import static org.opengis.cite.wmts10.core.assertion.WmtsAssertion.assertContentType;
import static org.opengis.cite.wmts10.core.assertion.WmtsAssertion.assertStatusCode;
import static org.testng.Assert.assertTrue;

import java.net.URI;
import java.util.List;

import org.opengis.cite.wmts10.core.domain.ProtocolBinding;
import org.opengis.cite.wmts10.core.domain.DGIWGWMTS;
import org.opengis.cite.wmts10.core.util.ServiceMetadataUtils;
import org.testng.annotations.Test;

import com.sun.jersey.api.client.ClientResponse;

import de.latlon.ets.core.error.ErrorMessage;
import de.latlon.ets.core.error.ErrorMessageKey;

/**
 *
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class GetTileCachingInfo extends AbstractBaseGetTileFixture {
    /**
     * --- DGWIG requirement 2 A WMTS Server shall support HTTP GET operation using KVP (clause 8 of OGC WMS) and RESTful (clause 10 of OGC WMTS 1.0) encodings.
     * ---
     */

    private URI getTileURI = null;

    private ClientResponse response = null;

    private List<String> cacheControls = null;

    private List<String> expires = null;

    @Test(description = "DGWIG requirement 2 A WMTS Server shall support HTTP GET operation using KVP (clause 8 of OGC WMS) and RESTful (clause 10 of OGC WMTS 1.0) encodings.", dependsOnMethods = "verifyGetTileSupported")
    public void wmtsGetTileKVPRequestsExists() {
        getTileURI = ServiceMetadataUtils.getOperationEndpoint_KVP( this.wmtsCapabilities, DGIWGWMTS.GET_TILE,
                                                                    ProtocolBinding.GET );
        assertTrue( getTileURI != null,
                    "GetTile (GET) endpoint not found or KVP is not supported in ServiceMetadata capabilities document." );
    }

    @Test(description = "DGWIG requirement 2 A WMTS Server shall support HTTP GET operation using KVP (clause 8 of OGC WMS) and RESTful (clause 10 of OGC WMTS 1.0) encodings.", dependsOnMethods = "wmtsGetTileKVPRequestsExists")
    public void wmtsGetTileCachingInformationExists() {
    	System.out.println("....wmtsGetTileCachingInformationExists : " + getTileURI);
        if ( getTileURI == null ) {
            getTileURI = ServiceMetadataUtils.getOperationEndpoint_KVP( this.wmtsCapabilities, DGIWGWMTS.GET_TILE,
                                                                        ProtocolBinding.GET );
        }

        this.reqEntity.removeKvp( DGIWGWMTS.FORMAT_PARAM );
        String requestFormat = DGIWGWMTS.IMAGE_PNG;
        this.reqEntity.addKvp( DGIWGWMTS.FORMAT_PARAM, requestFormat );

        response = wmtsClient.submitRequest( this.reqEntity, getTileURI );
        System.out.println("....wmtsGetTileCachingInformationExists response : " + response);
        storeResponseImage( response, "Requirement2", "simple", requestFormat );

        assertTrue( response.hasEntity(), ErrorMessage.get( ErrorMessageKey.MISSING_XML_ENTITY ) );
        assertStatusCode( response.getStatus(), 200 );
        assertContentType( response.getHeaders(), requestFormat );

        cacheControls = response.getHeaders().get( "Cache-control" );
        expires = response.getHeaders().get( "Expires" );

        boolean anyCacheControls = ( ( cacheControls != null ) && ( cacheControls.size() > 0 ) );
        boolean anyExpires = ( ( expires != null ) && ( expires.size() > 0 ) );
        System.out.println("....wmtsGetTileCachingInformationExists bool : " + anyCacheControls + " " + anyExpires);
        assertTrue( anyCacheControls || anyExpires, "WMTS does not provide appropriate caching information" );
    }

    @Test(description = "DGWIG requirement 2 A WMTS Server shall support HTTP GET operation using KVP (clause 8 of OGC WMS) and RESTful (clause 10 of OGC WMTS 1.0) encodings.", dependsOnMethods = "wmtsGetTileCachingInformationExists")
    public void wmtsGetTileExpirationExists() {
        boolean hasExpiration = false;
        if ( ( expires != null ) && ( expires.size() > 0 ) ) {
            hasExpiration = true;
        }

        if ( ( cacheControls != null ) && ( cacheControls.size() > 0 ) ) {
            String cacheControl = cacheControls.get( 0 );
            hasExpiration |= ( cacheControl.contains( "max-age" ) || cacheControl.contains( "maxage" ) );
        }

        assertTrue( hasExpiration,
                    "WMTS has cache-control or expiration headers, but no expiration time or date is found." );
    }

}