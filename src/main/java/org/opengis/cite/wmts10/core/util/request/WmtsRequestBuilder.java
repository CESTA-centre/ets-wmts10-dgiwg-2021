package org.opengis.cite.wmts10.core.util.request;

import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.BBOX_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.CRS_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.FORMAT_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.GET_FEATURE_INFO;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.GET_MAP;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.HEIGHT_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.IMAGE_GIF;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.IMAGE_PNG;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.INFO_FORMAT_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.I_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.J_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.LAYERS_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.QUERY_LAYERS_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.REQUEST_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.SERVICE_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.SERVICE_TYPE_CODE;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.STYLES_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.VERSION;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.VERSION_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.WIDTH_PARAM;
import static java.util.Arrays.asList;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.w3c.dom.Document;

import org.opengis.cite.wmts10.core.client.WmtsKvpRequest;
import org.opengis.cite.wmts10.core.domain.BoundingBox;
import org.opengis.cite.wmts10.core.domain.LayerInfo;
import org.opengis.cite.wmts10.core.util.ServiceMetadataUtils;

/**
 * Creates WMTS requests
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public final class WmtsRequestBuilder {

    private static final Random RANDOM = new Random();

    private static final List<String> TRANSPARENT_IMG_FORMATS = asList( IMAGE_PNG, IMAGE_GIF );

    private WmtsRequestBuilder() {
    }

    /**
     * Creates a GetFatureInfo request with random parameters from the WMTS Capabilities.
     * 
     * @param wmtsCapabilities
     *            the capabilities of the WMTS, never <code>null</code>
     * @param layerInfos
     *            the parsed layerInfos, never <code>null</code>
     * @return a GetFeatureInfo request with random parameters, never <code>null</code>
     */
    public static WmtsKvpRequest buildGetFeatureInfoRequest( Document wmtsCapabilities, List<LayerInfo> layerInfos ) {
        String format = getSupportedFormat( wmtsCapabilities, GET_FEATURE_INFO );
        return buildGetFeatureInfoRequest( wmtsCapabilities, layerInfos, format );
    }

    /**
     * Creates a GetFatureInfo request with random parameters from the WMTS Capabilities.
     * 
     * @param wmtsCapabilities
     *            the capabilities of the WMTS, never <code>null</code>
     * @param layerInfos
     *            the parsed layerInfos, never <code>null</code>
     * @param format
     *            the format to use, never <code>null</code>, if the format is not supported by the WMTS, the assertion
     *            fails
     * @return a GetFeatureInfo request with random parameters, never <code>null</code>
     */
    public static WmtsKvpRequest buildGetFeatureInfoRequest( Document wmtsCapabilities, List<LayerInfo> layerInfos,
                                                            String format ) {
        boolean isFormatSupported = ServiceMetadataUtils.parseSupportedFormats( wmtsCapabilities, GET_FEATURE_INFO ).contains( format );
        assertTrue( isFormatSupported, "The requested format is not supported for GetFEatureInfo requests." );
        return buildGetFeatureInfoRequestWithFormat( layerInfos, format );
    }

    /**
     * Creates a GetMap request with random parameters from the WMTS Capabilities.
     * 
     * @param wmtsCapabilities
     *            the capabilities of the WMTS, never <code>null</code>
     * @param layerInfos
     *            the parsed layerInfos, never <code>null</code>
     * @return a GetMap request with random parameters, never <code>null</code>
     */
    public static WmtsKvpRequest buildGetMapRequest( Document wmtsCapabilities, List<LayerInfo> layerInfos ) {
        WmtsKvpRequest reqEntity = new WmtsKvpRequest();
        reqEntity.addKvp( SERVICE_PARAM, SERVICE_TYPE_CODE );
        reqEntity.addKvp( VERSION_PARAM, VERSION );
        reqEntity.addKvp( REQUEST_PARAM, GET_MAP );

        LayerInfo layerInfo = findSuitableLayerInfo( layerInfos );
        assertNotNull( layerInfo, "Could not find suitable layer for GetMap request." );

        String format = getSupportedFormat( wmtsCapabilities, GET_MAP );
        assertNotNull( format, "Could not find request format for GetMap request." );

        reqEntity.addKvp( LAYERS_PARAM, layerInfo.getLayerName() );
        reqEntity.addKvp( STYLES_PARAM, "" );

        BoundingBox bbox = findBoundingBox( layerInfo );

        reqEntity.addKvp( CRS_PARAM, bbox.getCrs() );
        reqEntity.addKvp( BBOX_PARAM, bbox.getBboxAsString() );
        reqEntity.addKvp( WIDTH_PARAM, "100" );
        reqEntity.addKvp( HEIGHT_PARAM, "100" );
        reqEntity.addKvp( FORMAT_PARAM, format );

        return reqEntity;
    }

    private static WmtsKvpRequest buildGetFeatureInfoRequestWithFormat( List<LayerInfo> layerInfos, String format ) {
        WmtsKvpRequest reqEntity = new WmtsKvpRequest();
        reqEntity.addKvp( SERVICE_PARAM, SERVICE_TYPE_CODE );
        reqEntity.addKvp( VERSION_PARAM, VERSION );
        reqEntity.addKvp( REQUEST_PARAM, GET_FEATURE_INFO );

        LayerInfo layerInfo = findSuitableLayerInfo( layerInfos );
        assertNotNull( layerInfo, "Could not find suitable layer for GetMap requests." );

        assertNotNull( format, "Could not find request format for GetFeatureInfo." );

        String layerName = layerInfo.getLayerName();
        BoundingBox bbox = findBoundingBox( layerInfo );

        reqEntity.addKvp( LAYERS_PARAM, layerName );
        reqEntity.addKvp( STYLES_PARAM, "" );
        reqEntity.addKvp( CRS_PARAM, bbox.getCrs() );
        reqEntity.addKvp( BBOX_PARAM, bbox.getBboxAsString() );
        reqEntity.addKvp( WIDTH_PARAM, "1" );
        reqEntity.addKvp( HEIGHT_PARAM, "1" );
        reqEntity.addKvp( QUERY_LAYERS_PARAM, layerName );
        reqEntity.addKvp( I_PARAM, "0" );
        reqEntity.addKvp( J_PARAM, "0" );
        reqEntity.addKvp( INFO_FORMAT_PARAM, format );
        return reqEntity;
    }

    /**
     * @param wmtsCapabilities
     *            the capabilities of the WMTS, never <code>null</code>
     * @param opName
     *            /tegeoinfoGetMapTp-116 the name of the operation, never <code>null</code>
     * @return one of the supported formats of the operation, <code>null</code> if no format is specified
     */
    public static String getSupportedFormat( Document wmtsCapabilities, String opName ) {
        List<String> supportedFormats = ServiceMetadataUtils.parseSupportedFormats( wmtsCapabilities, opName );
        if ( supportedFormats.size() > 0 ) {
            int randomIndex = RANDOM.nextInt( supportedFormats.size() );
            return supportedFormats.get( randomIndex );
        }
        return null;
    }

    /**
     * @param wmtsCapabilities
     *            the capabilities of the WMTS, never <code>null</code>
     * @param opName
     *            the name of the operation, never <code>null</code>
     * @return one of the supported formats of the operation, supports transparency, <code>null</code> if no format is
     *         specified or no format supporting transparency is configured
     */
    public static String getSupportedTransparentFormat( Document wmtsCapabilities, String opName ) {
        List<String> supportedFormats = ServiceMetadataUtils.parseSupportedFormats( wmtsCapabilities, opName );
        for ( String transparentFormat : TRANSPARENT_IMG_FORMATS ) {
            if ( supportedFormats.contains( transparentFormat ) )
                return transparentFormat;
        }
        return null;
    }

    /**
     * @param layerInfo
     *            to retrieve the bbox from, never <code>null</code>
     * @return one if the {@link BoundingBox} of the layer, never <code>null</code>
     */
    public static BoundingBox findBoundingBox( LayerInfo layerInfo ) {
        List<BoundingBox> bboxes = layerInfo.getBboxes();
        int randomIndex = RANDOM.nextInt( bboxes.size() );
        return bboxes.get( randomIndex );
    }

    private static LayerInfo findSuitableLayerInfo( List<LayerInfo> layerInfos ) {
        List<LayerInfo> shuffledLayerInfos = new ArrayList<>( layerInfos );
        Collections.shuffle( shuffledLayerInfos );
        for ( LayerInfo layerInfo : shuffledLayerInfos ) {
            if ( layerHasBboxes( layerInfo ) && layerIsQueryable( layerInfo ) )
                return layerInfo;
        }
        return null;
    }

    private static boolean layerIsQueryable( LayerInfo layerInfo ) {
        return layerInfo.isQueryable();
    }

    private static boolean layerHasBboxes( LayerInfo layerInfo ) {
        return layerInfo.getBboxes().size() > 0;
    }

}
