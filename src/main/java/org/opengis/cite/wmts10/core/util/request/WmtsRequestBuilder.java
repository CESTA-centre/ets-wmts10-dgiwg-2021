package org.opengis.cite.wmts10.core.util.request;

import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.GET_CAPABILITIES;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.REQUEST_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.SERVICE_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.SERVICE_TYPE_CODE;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.VERSION;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.VERSION_PARAM;
import static org.testng.Assert.assertNotNull;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.FORMAT_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.GET_TILE;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.LAYER_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.STYLE_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.TILE_COL_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.TILE_MATRIX_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.TILE_MATRIX_SET_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.TILE_ROW_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.GET_FEATURE_INFO;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.INFO_FORMAT_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.I_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.J_PARAM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.opengis.cite.wmts10.core.domain.DGIWGWMTS;
import org.opengis.cite.wmts10.core.client.WmtsKvpRequest;
import org.opengis.cite.wmts10.core.domain.LayerInfo;
import org.opengis.cite.wmts10.core.util.ServiceMetadataUtils;


/**
 * Creates WMTS requests
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */

public final class WmtsRequestBuilder {

	private static final Random RANDOM = new Random();
	private static final String NOT_FOUND = " | ! No suitable request found ";

	private WmtsRequestBuilder() {
	}

	public static WmtsKvpRequest buildGetCapabilitiesRequest(Document wmtsCapabilities, List<LayerInfo> layerInfos) {
		WmtsKvpRequest reqEntity = new WmtsKvpRequest();
		reqEntity.addKvp(SERVICE_PARAM, SERVICE_TYPE_CODE);
		reqEntity.addKvp(REQUEST_PARAM, GET_CAPABILITIES);
		reqEntity.addKvp(VERSION_PARAM, VERSION);

		return reqEntity;
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
		//assertNotNull(layerInfo, "Could not find suitable layer for GetFeatureInfo request.");
		String layerName=null;		
		if (layerInfo != null) {
			layerName = layerInfo.getLayerName();
			reqEntity.addKvp(LAYER_PARAM, layerName);
		}else {
			String pb = "Could not find suitable layer for GetTile request.";
			WmtsKvpRequest reqNotFound = new WmtsKvpRequest();
			reqNotFound.addKvp(NOT_FOUND, pb);
			return reqNotFound;
		}

		String style = getRandomLayerStyle(wmtsCapabilities, layerName);
		//assertNotNull(style, "Could not find style for GetFeatureInfo request for <Layer>: " + layerName);
		if (style != null)
			reqEntity.addKvp(STYLE_PARAM, style);
		else {
			String pb = "Could not find style for GetTile request for <Layer>: " + layerName;
			WmtsKvpRequest reqNotFound = new WmtsKvpRequest();
			reqNotFound.addKvp(NOT_FOUND, pb);
			return reqNotFound;
		}
		

		String format = getRandomLayerFormat(wmtsCapabilities, layerInfo.getLayerName(), FORMAT_PARAM);
		//assertNotNull(format, "Could not find request format for GetFeatureInfo request for <Layer>: " + layerName);
		if (format != null)
			reqEntity.addKvp(FORMAT_PARAM, format);
		else {
			String pb = "Could not find request format for GetTile request for <Layer>: " + layerName;
			WmtsKvpRequest reqNotFound = new WmtsKvpRequest();
			reqNotFound.addKvp(NOT_FOUND, pb);
			return reqNotFound;
		}
			

		String tileMatrixSetName = getRandomLayerTileMatrixSetName(wmtsCapabilities, layerName);
		//assertNotNull(tileMatrixSetName,
		//		"Could not find tilematrix set for GetFeatureInfo request for <Layer>: " + layerName);		
		if (tileMatrixSetName != null)
			reqEntity.addKvp(TILE_MATRIX_SET_PARAM, tileMatrixSetName);
		else {
			String pb = "Could not find tilematrix set for GetTile request for <Layer>: " + layerName;
			WmtsKvpRequest reqNotFound = new WmtsKvpRequest();
			reqNotFound.addKvp(NOT_FOUND, pb);
			return reqNotFound;
		}
		
		
		String tileMatrixName = getRandomTileMatrix(wmtsCapabilities, tileMatrixSetName);
		//assertNotNull(tileMatrixName,
		//		"Could not find tilematrix for GetFeatureInfo request for <TileMatrixSet>: " + tileMatrixSetName);
		if (tileMatrixName != null)
			reqEntity.addKvp(TILE_MATRIX_PARAM, tileMatrixName);
		else {
			String pb = "Could not find tilematrix for GetTile request for <TileMatrixSet>: " + tileMatrixSetName;
			WmtsKvpRequest reqNotFound = new WmtsKvpRequest();
			reqNotFound.addKvp(NOT_FOUND, pb);
			return reqNotFound;
		}
		

		int[] tiles = getRandomLayerTiles(wmtsCapabilities, tileMatrixSetName, tileMatrixName);
		//assertNotNull(tiles, "Could not find tiles for GetFeatureInfo request for <TileMatrix> " + tileMatrixName
		//		+ " under <TileMatrixSet>: " + tileMatrixSetName);
		if (tiles != null) {
			reqEntity.addKvp(TILE_COL_PARAM, Integer.toString(tiles[0]));
			reqEntity.addKvp(TILE_ROW_PARAM, Integer.toString(tiles[1]));
		} else {
			String pb = "Could not find tiles for GetTile request for <TileMatrix> " + tileMatrixName
					+ " under <TileMatrixSet>: " + tileMatrixSetName;
			WmtsKvpRequest reqNotFound = new WmtsKvpRequest();
			reqNotFound.addKvp(NOT_FOUND, pb);
			return reqNotFound;
		}
		

        return reqEntity;
	}

	// ---

	/**
	 * Creates a GetFatureInfo request with random parameters from the WMTS
	 * Capabilities.
	 * 
	 * @param wmtsCapabilities the capabilities of the WMTS, never <code>null</code>
	 * @param layerInfos       the parsed layerInfos, never <code>null</code>
	 * @return a GetFeatureInfo request with random parameters, never
	 *         <code>null</code>
	 * 
	 * @throws XPathExpressionException in case of a bad XPath
	 */
	public static WmtsKvpRequest buildGetFeatureInfoRequest(Document wmtsCapabilities, List<LayerInfo> layerInfos,
			boolean invalid) throws XPathExpressionException {
		WmtsKvpRequest reqEntity = new WmtsKvpRequest();
		reqEntity.addKvp(SERVICE_PARAM, SERVICE_TYPE_CODE);
		reqEntity.addKvp(VERSION_PARAM, VERSION);
		reqEntity.addKvp(REQUEST_PARAM, GET_FEATURE_INFO);

		
		LayerInfo layerInfo = getRandomLayerInfo(layerInfos);
		//assertNotNull(layerInfo, "Could not find suitable layer for GetFeatureInfo request.");
		String layerName=null;		
		if (layerInfo != null) {
			layerName = layerInfo.getLayerName();
			if (!invalid) {
				reqEntity.addKvp(LAYER_PARAM, layerName);
			}
		}else {
			String pb = "Could not find suitable layer for GetFeatureInfo request.";
			WmtsKvpRequest reqNotFound = new WmtsKvpRequest();
			reqNotFound.addKvp(NOT_FOUND, pb);
			return reqNotFound;
		}

		String style = getRandomLayerStyle(wmtsCapabilities, layerName);
		//assertNotNull(style, "Could not find style for GetFeatureInfo request for <Layer>: " + layerName);
		if (style != null)
			reqEntity.addKvp(STYLE_PARAM, style);
		else {
			String pb = "Could not find style for GetFeatureInfo request for <Layer>: " + layerName;
			WmtsKvpRequest reqNotFound = new WmtsKvpRequest();
			reqNotFound.addKvp(NOT_FOUND, pb);
			return reqNotFound;
		}
		

		String format = getRandomLayerFormat(wmtsCapabilities, layerInfo.getLayerName(), FORMAT_PARAM);
		//assertNotNull(format, "Could not find request format for GetFeatureInfo request for <Layer>: " + layerName);
		if (format != null)
			reqEntity.addKvp(FORMAT_PARAM, format);
		else {
			String pb = "Could not find request format for GetFeatureInfo request for <Layer>: " + layerName;
			WmtsKvpRequest reqNotFound = new WmtsKvpRequest();
			reqNotFound.addKvp(NOT_FOUND, pb);
			return reqNotFound;
		}
			

		String tileMatrixSetName = getRandomLayerTileMatrixSetName(wmtsCapabilities, layerName);
		//assertNotNull(tileMatrixSetName,
		//		"Could not find tilematrix set for GetFeatureInfo request for <Layer>: " + layerName);		
		if (tileMatrixSetName != null)
			reqEntity.addKvp(TILE_MATRIX_SET_PARAM, tileMatrixSetName);
		else {
			String pb = "Could not find tilematrix set for GetFeatureInfo request for <Layer>: " + layerName;
			WmtsKvpRequest reqNotFound = new WmtsKvpRequest();
			reqNotFound.addKvp(NOT_FOUND, pb);
			return reqNotFound;
		}
		
		
		String tileMatrixName = getRandomTileMatrix(wmtsCapabilities, tileMatrixSetName);
		//assertNotNull(tileMatrixName,
		//		"Could not find tilematrix for GetFeatureInfo request for <TileMatrixSet>: " + tileMatrixSetName);
		if (tileMatrixName != null)
			reqEntity.addKvp(TILE_MATRIX_PARAM, tileMatrixName);
		else {
			String pb = "Could not find tilematrix for GetFeatureInfo request for <TileMatrixSet>: " + tileMatrixSetName;
			WmtsKvpRequest reqNotFound = new WmtsKvpRequest();
			reqNotFound.addKvp(NOT_FOUND, pb);
			return reqNotFound;
		}
		

		int[] tiles = getRandomLayerTiles(wmtsCapabilities, tileMatrixSetName, tileMatrixName);
		//assertNotNull(tiles, "Could not find tiles for GetFeatureInfo request for <TileMatrix> " + tileMatrixName
		//		+ " under <TileMatrixSet>: " + tileMatrixSetName);
		if (tiles != null) {
			reqEntity.addKvp(TILE_COL_PARAM, Integer.toString(tiles[0]));
			reqEntity.addKvp(TILE_ROW_PARAM, Integer.toString(tiles[1]));
		} else {
			String pb = "Could not find tiles for GetFeatureInfo request for <TileMatrix> " + tileMatrixName
					+ " under <TileMatrixSet>: " + tileMatrixSetName;
			WmtsKvpRequest reqNotFound = new WmtsKvpRequest();
			reqNotFound.addKvp(NOT_FOUND, pb);
			return reqNotFound;
		}
		

		int[] pixelNum = getRandomLayerTilePixels(wmtsCapabilities, tileMatrixSetName, tileMatrixName);
		//assertNotNull(tiles, "Could not find tile pixel counts for GetFeatureInfo request for <TileMatrix> "
		//		+ tileMatrixName + " under <TileMatrixSet>: " + tileMatrixSetName);
		if (pixelNum != null) {
			reqEntity.addKvp(I_PARAM, Integer.toString(pixelNum[0]));
			reqEntity.addKvp(J_PARAM, Integer.toString(pixelNum[1]));
		} else {
			String pb = "Could not find tile pixel counts for GetFeatureInfo request for <TileMatrix> " + tileMatrixName
					+ " under <TileMatrixSet>: " + tileMatrixSetName;
			WmtsKvpRequest reqNotFound = new WmtsKvpRequest();
			reqNotFound.addKvp(NOT_FOUND, pb);
			return reqNotFound;
		}

	        
	        String info_format = getRandomLayerFormat(wmtsCapabilities, layerInfo.getLayerName(), INFO_FORMAT_PARAM);
	        // assertNotNull( format, "Could not find request info-format for GetFeatureInfo
			// request for <Layer>: "+ layerName );
			
			if (info_format != null)
				reqEntity.addKvp(INFO_FORMAT_PARAM, info_format);
			else {
				String pb = "Could not find request info-format for GetFeatureInfo request for <Layer>: " + layerName;
				WmtsKvpRequest reqNotFound = new WmtsKvpRequest();
				reqNotFound.addKvp(NOT_FOUND, pb);
				return reqNotFound;
			}
	        
		

		return reqEntity;
	}

	private static LayerInfo getRandomLayerInfo(List<LayerInfo> layerInfos) {
		if (layerInfos.size() > 0) {
			List<LayerInfo> shuffledLayerInfos = new ArrayList<>(layerInfos);
			Collections.shuffle(shuffledLayerInfos);
			return shuffledLayerInfos.get(0);
		}
		return null;
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

	private static String getRandomLayerStyle(Document wmtsCapabilities, String layerName)
			throws XPathExpressionException {
		NodeList styles = ServiceMetadataUtils.parseLayerChildElements(wmtsCapabilities, layerName,
				DGIWGWMTS.STYLE_PARAM);
		if (styles.getLength() > 0) {
			int randomIndex = RANDOM.nextInt(styles.getLength());
			Node style = styles.item(randomIndex);

			return ServiceMetadataUtils.parseNodeElementName(style);
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

	private static int[] getRandomLayerTilePixels(Document wmtsCapabilities, String tileMatrixSetName,
			String tileMatrixName) throws XPathExpressionException {
		NodeList tileMatrices = ServiceMetadataUtils.getNodeElements(wmtsCapabilities,
				"//wmts:Contents/wmts:TileMatrixSet[ows:Identifier = '" + tileMatrixSetName
						+ "']/wmts:TileMatrix[ows:Identifier = '" + tileMatrixName + "']");
		if (tileMatrices.getLength() > 0) {
			Node tileMatrix = tileMatrices.item(0);

			NodeList widths = ServiceMetadataUtils.getNodeElements(tileMatrix, "./wmts:TileWidth");
			NodeList heights = ServiceMetadataUtils.getNodeElements(tileMatrix, "./wmts:TileHeight");

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