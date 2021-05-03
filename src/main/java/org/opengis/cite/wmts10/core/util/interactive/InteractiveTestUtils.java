package org.opengis.cite.wmts10.core.util.interactive;

import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.GET_FEATURE_INFO;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.GET_MAP;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.LAYERS_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.QUERY_LAYERS_PARAM;
import static org.opengis.cite.wmts10.core.domain.ProtocolBinding.GET;
import static org.opengis.cite.wmts10.core.util.ServiceMetadataUtils.getOperationEndpoint;
import static org.opengis.cite.wmts10.core.util.ServiceMetadataUtils.parseLayerInfo;
import static org.opengis.cite.wmts10.core.util.request.WmtsRequestBuilder.buildGetFeatureInfoRequest;
import static org.opengis.cite.wmts10.core.util.request.WmtsRequestBuilder.buildGetMapRequest;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.w3c.dom.Document;

import de.latlon.ets.core.util.URIUtils;
import org.opengis.cite.wmts10.core.client.WmtsKvpRequest;
import org.opengis.cite.wmts10.core.domain.DGIWGWMTS;
import org.opengis.cite.wmts10.core.domain.LayerInfo;
import org.opengis.cite.wmts10.core.util.request.WmtsRequestBuilder;

/**
 * Contains methods useful for interactive ctl tests.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public final class InteractiveTestUtils {

    private static final String UNKNOWN_LAYER_FOR_TESTING = "UNKNOWN_LAYER_FOR_TESTING";

    private InteractiveTestUtils() {
    }

    /**
     * Creates a GetFeatureInfo request.
     * 
     * @param wmtsCapabilitiesUrl
     *            the url of the WMTS capabilities, never <code>null</code>
     * @return a GetFeatureInfo request, never <code>null</code>
     */
    public static String retrieveGetFeatureInfoRequest( String wmtsCapabilitiesUrl ) {
        Document wmtsCapabilities = readCapabilities( wmtsCapabilitiesUrl );
        URI getFeatureInfoEndpoint = getOperationEndpoint( wmtsCapabilities, GET_FEATURE_INFO, GET );
        List<LayerInfo> layerInfos = parseLayerInfo( wmtsCapabilities );

        WmtsKvpRequest getFeatureInfoRequest = WmtsRequestBuilder.buildGetFeatureInfoRequest( wmtsCapabilities, layerInfos );
        return createUri( getFeatureInfoEndpoint, getFeatureInfoRequest );
    }

    /**
     * Creates a GetFeatureInfo request with unsupported layer.
     * 
     * @param wmtsCapabilitiesUrl
     *            the url of the WMTS capabilities, never <code>null</code>
     * @return a GetFeatureInfo request with unsupported layer, never <code>null</code>
     */
    public static String retrieveInvalidGetFeatureInfoRequest( String wmtsCapabilitiesUrl ) {
        Document wmtsCapabilities = readCapabilities( wmtsCapabilitiesUrl );
        URI getFeatureInfoEndpoint = getOperationEndpoint( wmtsCapabilities, GET_FEATURE_INFO, GET );
        List<LayerInfo> layerInfos = parseLayerInfo( wmtsCapabilities );

        WmtsKvpRequest getFeatureInfoRequest = buildGetFeatureInfoRequest( wmtsCapabilities, layerInfos );
        getFeatureInfoRequest.addKvp( LAYERS_PARAM, UNKNOWN_LAYER_FOR_TESTING );
        getFeatureInfoRequest.addKvp( QUERY_LAYERS_PARAM, UNKNOWN_LAYER_FOR_TESTING );
        return createUri( getFeatureInfoEndpoint, getFeatureInfoRequest );
    }

    /**
     * Creates a GetMap request with unsupported layer.
     * 
     * @param wmtsCapabilitiesUrl
     *            the url of the WMTS capabilities, never <code>null</code>
     * @return a GetMap request with unsupported layer, never <code>null</code>
     */
    public static String retrieveInvalidGetMapRequest( String wmtsCapabilitiesUrl ) {
        Document wmtsCapabilities = readCapabilities( wmtsCapabilitiesUrl );
        URI getFeatureInfoEndpoint = getOperationEndpoint( wmtsCapabilities, GET_MAP, GET );
        List<LayerInfo> layerInfos = parseLayerInfo( wmtsCapabilities );

        WmtsKvpRequest getFeatureInfoRequest = buildGetMapRequest( wmtsCapabilities, layerInfos );
        getFeatureInfoRequest.addKvp( LAYERS_PARAM, UNKNOWN_LAYER_FOR_TESTING );
        return createUri( getFeatureInfoEndpoint, getFeatureInfoRequest );
    }

    private static String createUri( URI getFeatureInfoEndpoint, WmtsKvpRequest getFeatureInfoRequest ) {
        String queryString = getFeatureInfoRequest.asQueryString();
        URI requestURI = UriBuilder.fromUri( getFeatureInfoEndpoint ).replaceQuery( queryString ).build();
        return requestURI.toString();
    }

    private static Document readCapabilities( String wmtsCapabilitiesUrl ) {
        URI wmtsURI = URI.create( wmtsCapabilitiesUrl );
        Document doc = null;
        try {
            doc = URIUtils.resolveURIAsDocument( wmtsURI );
            if ( !doc.getDocumentElement().getLocalName().equals( DGIWGWMTS.WMTS_CAPABILITIES ) ) {
                throw new RuntimeException( "Did not receive WMTS capabilities document: "
                                            + doc.getDocumentElement().getNodeName() );
            }
        } catch ( Exception ex ) {
            throw new RuntimeException( "Failed to parse resource located at " + wmtsURI, ex );
        }
        return doc;
    }

}
