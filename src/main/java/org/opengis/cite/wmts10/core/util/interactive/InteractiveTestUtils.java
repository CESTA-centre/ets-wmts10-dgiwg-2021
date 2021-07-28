package org.opengis.cite.wmts10.core.util.interactive;

import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.GET_FEATURE_INFO;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.GET_CAPABILITIES;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.GET_TILE;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.LAYER_PARAM;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.SERVICE_PARAM;
import static org.opengis.cite.wmts10.core.domain.ProtocolBinding.GET;
import static org.opengis.cite.wmts10.core.util.ServiceMetadataUtils.parseLayerInfo;
import static org.opengis.cite.wmts10.core.util.ServiceMetadataUtils.getOperationEndpoint;
import static org.opengis.cite.wmts10.core.util.request.WmtsRequestBuilder.buildGetFeatureInfoRequest;
import static org.opengis.cite.wmts10.core.util.request.WmtsRequestBuilder.buildGetCapabilitiesRequest;
import static org.opengis.cite.wmts10.core.util.request.WmtsRequestBuilder.buildGetTileRequest;
import static org.testng.Assert.assertNotNull;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.UriBuilder;
import javax.xml.xpath.XPathExpressionException;

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
	private static final String NOT_FOUND = " | ! No suitable request found ";

	private InteractiveTestUtils() {
	}

	/**
	 * Creates a GetFeatureInfo request.
	 * 
	 * @param wmtsCapabilitiesUrl the url of the WMTS capabilities, never
	 *                            <code>null</code>
	 * @return a GetFeatureInfo request, never <code>null</code>
	 */
	public static String retrieveGetFeatureInfoRequest(String wmtsCapabilitiesUrl) {
		Document wmtsCapabilities = readCapabilities(wmtsCapabilitiesUrl);
		URI getFeatureInfoEndpoint = getOperationEndpoint(wmtsCapabilities, GET_FEATURE_INFO, GET);
		List<LayerInfo> layerInfos = parseLayerInfo(wmtsCapabilities);
		try {
			WmtsKvpRequest getFeatureInfoRequest = buildGetFeatureInfoRequest(wmtsCapabilities, layerInfos, false);
			if(getFeatureInfoRequest.getKvpValue(NOT_FOUND)!=null){
				return (getFeatureInfoRequest.getKvpValue(NOT_FOUND)+ NOT_FOUND);
			}else {
				return createUri(getFeatureInfoEndpoint, getFeatureInfoRequest);
			}
		} catch (XPathExpressionException e) {
			assertNotNull(null, "GetFeatureInfo is not supported by this WMTS");
			return ("GetFeatureInfo operation is not supported by the server !");
		}
	}

	/**
	 * Creates a GetCapabilities request with unsupported parameter.
	 * 
	 * @param wmtsCapabilitiesUrl the url of the WMTS capabilities, never
	 *                            <code>null</code>
	 * @return a GetFeatureInfo request with unsupported parameter, never
	 *         <code>null</code>
	 */
	public static String retrieveInvalidGetCapabilitiesRequest(String wmtsCapabilitiesUrl) {
		Document wmtsCapabilities = readCapabilities(wmtsCapabilitiesUrl);
		URI getCapabilitiesEndpoint = getOperationEndpoint(wmtsCapabilities, GET_CAPABILITIES, GET);
		List<LayerInfo> layerInfos = parseLayerInfo(wmtsCapabilities);

		WmtsKvpRequest getCapabilitiesRequest = buildGetCapabilitiesRequest(wmtsCapabilities, layerInfos);
		getCapabilitiesRequest.addKvp(SERVICE_PARAM, "dummy");

		return createUri(getCapabilitiesEndpoint, getCapabilitiesRequest);

	}

	/**
	 * Creates a GetFeatureInfo request with unsupported layer.
	 * 
	 * @param wmtsCapabilitiesUrl the url of the WMTS capabilities, never
	 *                            <code>null</code>
	 * @return a GetFeatureInfo request with unsupported layer, never
	 *         <code>null</code>
	 */
	public static String retrieveInvalidGetTileRequest(String wmtsCapabilitiesUrl) {
		Document wmtsCapabilities = readCapabilities(wmtsCapabilitiesUrl);
		URI getTileEndpoint = getOperationEndpoint(wmtsCapabilities, GET_TILE, GET);
		List<LayerInfo> layerInfos = parseLayerInfo(wmtsCapabilities);

		try {
			WmtsKvpRequest getTileRequest = buildGetTileRequest(wmtsCapabilities, layerInfos);
			if(getTileRequest.getKvpValue(NOT_FOUND)!=null){
				return (getTileRequest.getKvpValue(NOT_FOUND)+ NOT_FOUND);
			}else {
				getTileRequest.addKvp(LAYER_PARAM, UNKNOWN_LAYER_FOR_TESTING);
				return createUri(getTileEndpoint, getTileRequest);
			}
		} catch (XPathExpressionException e) {
			assertNotNull(null, "Could not find suitable parameter for GetTile request.");
			return ("Could not find suitable parameter for GetTile request.");
		}
	}

	/**
	 * Creates a GetTile request with unsupported layer.
	 * 
	 * @param wmtsCapabilitiesUrl the url of the WMTS capabilities, never
	 *                            <code>null</code>
	 * @return a GetTile request with unsupported layer, never <code>null</code>
	 */
	public static String retrieveInvalidGetFeatureInfoRequest(String wmtsCapabilitiesUrl) {
		Document wmtsCapabilities = readCapabilities(wmtsCapabilitiesUrl);
		URI getFeatureInfoEndpoint = getOperationEndpoint(wmtsCapabilities, GET_FEATURE_INFO, GET);
		List<LayerInfo> layerInfos = parseLayerInfo(wmtsCapabilities);
		try {
			WmtsKvpRequest getFeatureInfoRequest = buildGetFeatureInfoRequest(wmtsCapabilities, layerInfos, true);
			if(getFeatureInfoRequest.getKvpValue(NOT_FOUND)!=null){
				return (getFeatureInfoRequest.getKvpValue(NOT_FOUND)+ NOT_FOUND);
			}else {
				getFeatureInfoRequest.addKvp(LAYER_PARAM, UNKNOWN_LAYER_FOR_TESTING);
				return createUri(getFeatureInfoEndpoint, getFeatureInfoRequest);
			}
		} catch (XPathExpressionException e) {
			assertNotNull(null, "GetFeatureInfo is not supported by this WMTS");
			return ("GetFeatureInfo operation is not supported by the server !");
		}
	}

	/**
	 * Creates a GetCapabilities request.
	 * 
	 * @param wmtsCapabilitiesUrl the url of the WMTS capabilities, never
	 *                            <code>null</code>
	 * @return a GetCapabilities request, never <code>null</code>
	 */
	public static String retrieveGetCapabilitiesRequest(String wmtsCapabilitiesUrl) {
		Document wmtsCapabilities = readCapabilities(wmtsCapabilitiesUrl);
		URI getCapabilitiesEndpoint = getOperationEndpoint(wmtsCapabilities, GET_CAPABILITIES, GET);
		List<LayerInfo> layerInfos = parseLayerInfo(wmtsCapabilities);
		WmtsKvpRequest getCapabilitiesRequest = buildGetCapabilitiesRequest(wmtsCapabilities, layerInfos);
		return createUri(getCapabilitiesEndpoint, getCapabilitiesRequest);

	}

	private static String createUri(URI getFeatureInfoEndpoint, WmtsKvpRequest getFeatureInfoRequest) {
		String queryString = getFeatureInfoRequest.asQueryString();
		URI requestURI = UriBuilder.fromUri(getFeatureInfoEndpoint).replaceQuery(queryString).build();
		return requestURI.toString();
	}

	private static Document readCapabilities(String wmtsCapabilitiesUrl) {
		URI wmtsURI = URI.create(wmtsCapabilitiesUrl);
		Document doc = null;
		try {
			doc = URIUtils.resolveURIAsDocument(wmtsURI);
			if (!doc.getDocumentElement().getLocalName().equals(DGIWGWMTS.WMTS_CAPABILITIES)) {
				throw new RuntimeException(
						"Did not receive WMTS capabilities document: " + doc.getDocumentElement().getNodeName());
			}
		} catch (Exception ex) {
			throw new RuntimeException("Failed to parse resource located at " + wmtsURI, ex);
		}
		return doc;
	}

}
