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
	 *A RESTFULL WMTS server shall provide tile experiation date, in an appropriate  HTTP header ("Expires" header in HTTP 1.0,  
	 *"Cache-control" header for HTTP 1.1 and for HTTP 2).
	 */
	
	private static final String GROUPE_NAME = "A RESTFULL WMTS server shall provide tile experiation date, in an appropriate  HTTP header (\"Expires\" header in HTTP 1.0,  \"Cache-control\" header for HTTP 1.1 and for HTTP 2).";
	

    private URI getTileURI = null;

    private ClientResponse response = null;

    private List<String> cacheControls = null;

    private List<String> expires = null;

    
    @Test(groups = GROUPE_NAME,description = "Checks if tile request exist", dependsOnMethods = "verifyGetTileSupported")
    public void wmtsGetTileRESTequestsExists() {
    	//System.out.println("....wmtsGetTileRESTequestsExists : ");
        getTileURI = ServiceMetadataUtils.getOperationEndpoint_REST( this.wmtsCapabilities, DGIWGWMTS.GET_TILE,
                                                                    ProtocolBinding.GET );
        //System.out.println("....wmtsGetTileRESTequestsExists getTileURI : " + getTileURI);
        assertTrue( getTileURI != null,
                    "GetTile (GET) endpoint not found or REST is not supported in ServiceMetadata capabilities document." );
        
    }

    @Test(groups = GROUPE_NAME,description = "Checks if tile caching information exist", dependsOnMethods = "wmtsGetTileRESTequestsExists")
    public void wmtsGetTileCachingInformationExists() {
        if ( getTileURI == null ) {
            getTileURI = ServiceMetadataUtils.getOperationEndpoint_REST( this.wmtsCapabilities, DGIWGWMTS.GET_TILE,
                                                                        ProtocolBinding.GET );
        }

        /*
        this.reqEntity.removeKvp( DGIWGWMTS.FORMAT_PARAM );
        String requestFormat = DGIWGWMTS.IMAGE_PNG;
        this.reqEntity.addKvp( DGIWGWMTS.FORMAT_PARAM, requestFormat );
        */

        
        /*
        response = wmtsClient.submitRequest( this.reqEntity, getTileURI );
        storeResponseImage( response, "Requirement11", "simple", requestFormat );

        assertTrue( response.hasEntity(), ErrorMessage.get( ErrorMessageKey.MISSING_XML_ENTITY ) );
        assertStatusCode( response.getStatus(), 200 );
        assertContentType( response.getHeaders(), requestFormat );
        */

        cacheControls = response.getHeaders().get( "Cache-control" );
        expires = response.getHeaders().get( "Expires" );

        boolean anyCacheControls = ( ( cacheControls != null ) && ( cacheControls.size() > 0 ) );
        boolean anyExpires = ( ( expires != null ) && ( expires.size() > 0 ) );
        assertTrue( anyCacheControls || anyExpires, "WMTS does not provide appropriate caching information" );
    }

    @Test(groups = GROUPE_NAME,description = "Checks if tile expiration exist", dependsOnMethods = "wmtsGetTileCachingInformationExists")
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