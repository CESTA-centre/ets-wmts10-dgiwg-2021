package org.opengis.cite.wmts10.core.util.request;

import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.GET_CAPABILITIES;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.REQUEST_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.SERVICE_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.SERVICE_TYPE_CODE;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.VERSION;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.VERSION_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.FORMAT_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.GET_TILE;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.LAYER_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.REQUEST_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.SERVICE_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.SERVICE_TYPE_CODE;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.STYLE_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.TILE_COL_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.TILE_MATRIX_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.TILE_MATRIX_SET_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.TILE_ROW_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.VERSION;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.VERSION_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.FORMAT_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.GET_FEATURE_INFO;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.GET_TILE;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.IMAGE_GIF;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.IMAGE_PNG;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.INFO_FORMAT_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.I_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.J_PARAM;

import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.REQUEST_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.SERVICE_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.SERVICE_TYPE_CODE;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.VERSION;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.VERSION_PARAM;

import static java.util.Arrays.asList;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
//import org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants;
import org.opengis.cite.wmts10.core.domain.DGIWGWMTS;
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

	private static final List<String> TRANSPARENT_IMG_FORMATS = asList(IMAGE_PNG, IMAGE_GIF);

	private WmtsRequestBuilder() {
	}

	/**
	 * Creates a GetFatureInfo request with random parameters from the WMTS
	 * Capabilities.
	 * 
	 * @param wmtsCapabilities the capabilities of the WMTS, never <code>null</code>
	 * @param layerInfos       the parsed layerInfos, never <code>null</code>
	 * @return a GetFeatureInfo request with random parameters, never
	 *         <code>null</code>
	 */
	public static WmtsKvpRequest buildGetFeatureInfoRequest(Document wmtsCapabilities, List<LayerInfo> layerInfos,
			boolean invalid) {
		System.out.println("....buildGetFeatureInfoRequest begin ");
		String format = getSupportedFormat(wmtsCapabilities, GET_FEATURE_INFO);
		System.out.println("....buildGetFeatureInfoRequest format : " + format);
		return buildGetFeatureInfoRequest(wmtsCapabilities, layerInfos, format, invalid);

	}

	public static WmtsKvpRequest buildGetCapabilitiesRequest(Document wmtsCapabilities, List<LayerInfo> layerInfos) {
		WmtsKvpRequest reqEntity = new WmtsKvpRequest();
		reqEntity.addKvp(SERVICE_PARAM, SERVICE_TYPE_CODE);
		reqEntity.addKvp(REQUEST_PARAM, GET_CAPABILITIES);
		reqEntity.addKvp(VERSION_PARAM, VERSION);

		return reqEntity;
	}

	/**
	 * Creates a GetFatureInfo request with random parameters from the WMTS
	 * Capabilities.
	 * 
	 * @param wmtsCapabilities the capabilities of the WMTS, never <code>null</code>
	 * @param layerInfos       the parsed layerInfos, never <code>null</code>
	 * @param format           the format to use, never <code>null</code>, if the
	 *                         format is not supported by the WMTS, the assertion
	 *                         fails
	 * @return a GetFeatureInfo request with random parameters, never
	 *         <code>null</code>
	 */
	public static WmtsKvpRequest buildGetFeatureInfoRequest(Document wmtsCapabilities, List<LayerInfo> layerInfos,
			String format,boolean invalid) {
		System.out.println("....WmtsKvpRequest : " + wmtsCapabilities + " format : " + format);
		boolean isFormatSupported = ServiceMetadataUtils.parseSupportedFormats_v2(wmtsCapabilities, GET_FEATURE_INFO)
				.contains(format);
		System.out.println("....WmtsKvpRequest isFormatSupported : " + isFormatSupported);
		assertTrue(isFormatSupported, "The requested format is not supported for GetFEatureInfo requests.");
		return buildGetFeatureInfoRequestWithFormat(layerInfos, format, invalid);
	}

	/**
	 * Creates a GetMap request with random parameters from the WMTS Capabilities.
	 * 
	 * @param wmtsCapabilities the capabilities of the WMTS, never <code>null</code>
	 * @param layerInfos       the parsed layerInfos, never <code>null</code>
	 * @return a GetMap request with random parameters, never <code>null</code>
	 */
	public static WmtsKvpRequest buildGetMapRequest(Document wmtsCapabilities, List<LayerInfo> layerInfos) {
		/*
		 * WmtsKvpRequest reqEntity = new WmtsKvpRequest(); reqEntity.addKvp(
		 * SERVICE_PARAM, SERVICE_TYPE_CODE ); reqEntity.addKvp( VERSION_PARAM, VERSION
		 * ); reqEntity.addKvp( REQUEST_PARAM, GET_TILE );
		 * 
		 * LayerInfo layerInfo = findSuitableLayerInfo( layerInfos ); assertNotNull(
		 * layerInfo, "Could not find suitable layer for GetMap request." );
		 * 
		 * String format = getSupportedFormat( wmtsCapabilities, GET_TILE );
		 * assertNotNull( format, "Could not find request format for GetMap request." );
		 * 
		 * reqEntity.addKvp( LAYERS_PARAM, layerInfo.getLayerName() ); reqEntity.addKvp(
		 * STYLES_PARAM, "" );
		 * 
		 * BoundingBox bbox = findBoundingBox( layerInfo );
		 * 
		 * reqEntity.addKvp( CRS_PARAM, bbox.getCrs() ); reqEntity.addKvp( BBOX_PARAM,
		 * bbox.getBboxAsString() ); reqEntity.addKvp( WIDTH_PARAM, "100" );
		 * reqEntity.addKvp( HEIGHT_PARAM, "100" ); reqEntity.addKvp( FORMAT_PARAM,
		 * format );
		 * 
		 * return reqEntity;
		 */return null;
	}

	private static WmtsKvpRequest buildGetFeatureInfoRequestWithFormat(List<LayerInfo> layerInfos, String format, boolean invalid) {

		WmtsKvpRequest reqEntity = new WmtsKvpRequest();
		reqEntity.addKvp(SERVICE_PARAM, SERVICE_TYPE_CODE);
		reqEntity.addKvp(VERSION_PARAM, VERSION);
		reqEntity.addKvp(REQUEST_PARAM, GET_FEATURE_INFO);

		//LayerInfo layerInfo = findSuitableLayerInfo(layerInfos);
		//assertNotNull(layerInfo, "Could not find suitable layer for GetMap requests.");

		//assertNotNull(format, "Could not find request format for GetFeatureInfo.");

		//String layerName = layerInfo.getLayerName();
		String layerName = "ORTHOIMAGERY.ORTHOPHOTOS";
		// BoundingBox bbox = findBoundingBox(layerInfo);

		if ( !invalid) reqEntity.addKvp(LAYER_PARAM, layerName);
		reqEntity.addKvp(TILE_MATRIX_SET_PARAM, "PM");
		reqEntity.addKvp(TILE_MATRIX_PARAM, "14");
		reqEntity.addKvp(TILE_ROW_PARAM, "0");
		reqEntity.addKvp(TILE_COL_PARAM, "0");
		reqEntity.addKvp(FORMAT_PARAM, "image/jpeg");
		reqEntity.addKvp(STYLE_PARAM, "normal");
		// reqEntity.addKvp(CRS_PARAM, bbox.getCrs());
		// reqEntity.addKvp(BBOX_PARAM, bbox.getBboxAsString());
		// reqEntity.addKvp(WIDTH_PARAM, "1");
		// reqEntity.addKvp(HEIGHT_PARAM, "1");
		// reqEntity.addKvp(QUERY_LAYERS_PARAM, layerName);
		reqEntity.addKvp(INFO_FORMAT_PARAM, "text/xml");
		reqEntity.addKvp(I_PARAM, "0");
		reqEntity.addKvp(J_PARAM, "0");

		return reqEntity;

	}


	/**
	 * @param wmtsCapabilities the capabilities of the WMTS, never <code>null</code>
	 * @param opName           /tegeoinfoGetMapTp-116 the name of the operation,
	 *                         never <code>null</code>
	 * @return one of the supported formats of the operation, <code>null</code> if
	 *         no format is specified
	 */
	public static String getSupportedFormat(Document wmtsCapabilities, String opName) {
		System.out.println("....getSupportedFormat : " + opName);
		List<String> supportedFormats = ServiceMetadataUtils.parseSupportedFormats_v2(wmtsCapabilities, opName);
		if (supportedFormats.size() > 0) {
			int randomIndex = RANDOM.nextInt(supportedFormats.size());
			return supportedFormats.get(randomIndex);
		}

		return null;
	}

	/**
	 * @param wmtsCapabilities the capabilities of the WMTS, never <code>null</code>
	 * @param opName           the name of the operation, never <code>null</code>
	 * @return one of the supported formats of the operation, supports transparency,
	 *         <code>null</code> if no format is specified or no format supporting
	 *         transparency is configured
	 */
	public static String getSupportedTransparentFormat(Document wmtsCapabilities, String opName) {
		/*
		 * List<String> supportedFormats = ServiceMetadataUtils.parseSupportedFormats(
		 * wmtsCapabilities, opName ); for ( String transparentFormat :
		 * TRANSPARENT_IMG_FORMATS ) { if ( supportedFormats.contains( transparentFormat
		 * ) ) return transparentFormat; }
		 */
		return null;
	}

	/**
	 * @param layerInfo to retrieve the bbox from, never <code>null</code>
	 * @return one if the {@link BoundingBox} of the layer, never <code>null</code>
	 */
	public static BoundingBox findBoundingBox(LayerInfo layerInfo) {
		List<BoundingBox> bboxes = layerInfo.getBboxes();
		int randomIndex = RANDOM.nextInt(bboxes.size());
		return bboxes.get(randomIndex);
	}

	private static LayerInfo findSuitableLayerInfo(List<LayerInfo> layerInfos) {
		List<LayerInfo> shuffledLayerInfos = new ArrayList<>(layerInfos);
		Collections.shuffle(shuffledLayerInfos);
		for (LayerInfo layerInfo : shuffledLayerInfos) {
			if (layerHasBboxes(layerInfo) && layerIsQueryable(layerInfo))
				return layerInfo;
		}
		return null;
	}

	private static boolean layerIsQueryable(LayerInfo layerInfo) {
		// return layerInfo.isQueryable();
		return false;
	}

	private static boolean layerHasBboxes(LayerInfo layerInfo) {
		return layerInfo.getBboxes().size() > 0;
	}

	private static LayerInfo getRandomLayerInfo(List<LayerInfo> layerInfos) {
		if (layerInfos.size() > 0) {
			List<LayerInfo> shuffledLayerInfos = new ArrayList<>(layerInfos);
			Collections.shuffle(shuffledLayerInfos);
			return shuffledLayerInfos.get(0);
		}
		return null;
	}

	private static String getRandomLayerStyle(Document wmtsCapabilities, String layerName)
			throws XPathExpressionException {
		NodeList styles = ServiceMetadataUtils.parseLayerChildElements(wmtsCapabilities, layerName, STYLE_PARAM);
		// WMTS_Constants.STYLE_PARAM);
		if (styles.getLength() > 0) {
			int randomIndex = RANDOM.nextInt(styles.getLength());
			Node style = styles.item(randomIndex);

			return ServiceMetadataUtils.parseNodeElementName(style);
		}
		return null;
	}

	/**
	 * Creates a GetTile request with random parameters from the WMTS Capabilities.
	 * 
	 * @param wmtsCapabilities the capabilities of the WMTS, never <code>null</code>
	 * @param layerInfos       the parsed layerInfos, never <code>null</code>
	 * @return a GetTile request with random parameters, never <code>null</code>
	 * 
	 * @throws XPathExpressionException in case of a bad XPath
	 */
	public static WmtsKvpRequest buildGetTileRequest(Document wmtsCapabilities, List<LayerInfo> layerInfos)
			throws XPathExpressionException {
		WmtsKvpRequest reqEntity = new WmtsKvpRequest();
		reqEntity.addKvp(SERVICE_PARAM, SERVICE_TYPE_CODE);
		reqEntity.addKvp(VERSION_PARAM, VERSION);
		reqEntity.addKvp(REQUEST_PARAM, GET_TILE);

		LayerInfo layerInfo = getRandomLayerInfo(layerInfos);
		assertNotNull(layerInfo, "Could not find suitable layer for GetTile request.");
		String layerName = layerInfo.getLayerName();
		reqEntity.addKvp(LAYER_PARAM, layerName);

		String style = getRandomLayerStyle(wmtsCapabilities, layerName);
		assertNotNull(style, "Could not find style for GetTile request for <Layer>: " + layerName);
		reqEntity.addKvp(STYLE_PARAM, style);

		String format = getRandomLayerFormat(wmtsCapabilities, layerInfo.getLayerName(), FORMAT_PARAM);
		assertNotNull(format, "Could not find request format for GetTile request for <Layer>: " + layerName);
		reqEntity.addKvp(FORMAT_PARAM, format);

		String tileMatrixSetName = getRandomLayerTileMatrixSetName(wmtsCapabilities, layerName);
		assertNotNull(tileMatrixSetName, "Could not find tilematrix set for GetTile request for <Layer>: " + layerName);
		reqEntity.addKvp(TILE_MATRIX_SET_PARAM, tileMatrixSetName);

		String tileMatrixName = getRandomTileMatrix(wmtsCapabilities, tileMatrixSetName);
		assertNotNull(tileMatrixName,
				"Could not find tilematrix for GetTile request for <TileMatrixSet>: " + tileMatrixSetName);
		reqEntity.addKvp(TILE_MATRIX_PARAM, tileMatrixName);

		int[] tiles = getRandomLayerTiles(wmtsCapabilities, tileMatrixSetName, tileMatrixName);
		assertNotNull(tiles, "Could not find tiles for GetTile request for <TileMatrix> " + tileMatrixName
				+ " under <TileMatrixSet>: " + tileMatrixSetName);
		reqEntity.addKvp(TILE_COL_PARAM, Integer.toString(tiles[0]));
		reqEntity.addKvp(TILE_ROW_PARAM, Integer.toString(tiles[1]));

		return reqEntity;
	}

	/**
	 * @param wmtsCapabilities the capabilities of the WMTS, never <code>null</code>
	 * @param layerName        the name of the selected layer
	 * @param formatParam      the name of which format type for the WMTS operation
	 * @return one of the supported formats of the operation, <code>null</code> if
	 *         no format is specified
	 * @throws XPathExpressionException
	 */

	private static String getRandomLayerFormat(Document wmtsCapabilities, String layerName, String formatParam)
			throws XPathExpressionException {
		NodeList formats = ServiceMetadataUtils.parseLayerChildElements(wmtsCapabilities, layerName, formatParam);
		if (formats.getLength() > 0) {
			int randomIndex = RANDOM.nextInt(formats.getLength());
			Node format = formats.item(randomIndex);

			return format.getTextContent().trim();
		}
		return null;
	}

	private static String getRandomLayerTileMatrixSetName(Document wmtsCapabilities, String layerName)
			throws XPathExpressionException {
		NodeList tileMatrixSetLinks = ServiceMetadataUtils.parseLayerChildElements(wmtsCapabilities, layerName,
				"TileMatrixSetLink");
		if (tileMatrixSetLinks.getLength() > 0) {
			int randomIndex = RANDOM.nextInt(tileMatrixSetLinks.getLength());
			Node tileMatrixSetLink = tileMatrixSetLinks.item(randomIndex);
			return ServiceMetadataUtils.getNodeText(tileMatrixSetLink, "wmts:TileMatrixSet");
		}
		return null;
	}

	private static String getRandomTileMatrix(Document wmtsCapabilities, String tileMatrixSetName)
			throws XPathExpressionException {
		NodeList tileMatrices = ServiceMetadataUtils.getNodeElements(wmtsCapabilities,
				"//wmts:Contents/wmts:TileMatrixSet[ows:Identifier = '" + tileMatrixSetName + "']/wmts:TileMatrix");
		if (tileMatrices.getLength() > 0) {
			int randomIndex = RANDOM.nextInt(tileMatrices.getLength());
			Node tileMatrixSet = tileMatrices.item(randomIndex);

			return ServiceMetadataUtils.parseNodeElementName(tileMatrixSet);
		}
		return null;
	}

	private static int[] getRandomLayerTiles(Document wmtsCapabilities, String tileMatrixSetName, String tileMatrixName)
			throws XPathExpressionException {
		NodeList tileMatrices = ServiceMetadataUtils.getNodeElements(wmtsCapabilities,
				"//wmts:Contents/wmts:TileMatrixSet[ows:Identifier = '" + tileMatrixSetName
						+ "']/wmts:TileMatrix[ows:Identifier = '" + tileMatrixName + "']");
		if (tileMatrices.getLength() > 0) {
			Node tileMatrix = tileMatrices.item(0);

			NodeList widths = ServiceMetadataUtils.getNodeElements(tileMatrix, "./wmts:MatrixWidth");
			NodeList heights = ServiceMetadataUtils.getNodeElements(tileMatrix, "./wmts:MatrixHeight");

			Node widthNode = widths.item(0);
			Node heightNode = heights.item(0);

			String widthStr = widthNode.getTextContent().trim();
			String heightStr = heightNode.getTextContent().trim();

			int numWidth = Integer.parseInt(widthStr);
			int numHeight = Integer.parseInt(heightStr);

			int randomWidth = RANDOM.nextInt(numWidth);
			int randomHeight = RANDOM.nextInt(numHeight);

			int[] values = new int[2];

			values[0] = randomWidth;
			values[1] = randomHeight;

			return values;
		}
		return null;
	}

}
