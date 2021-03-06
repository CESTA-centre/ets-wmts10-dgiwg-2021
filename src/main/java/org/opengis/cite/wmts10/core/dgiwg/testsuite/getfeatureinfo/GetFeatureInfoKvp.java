package org.opengis.cite.wmts10.core.dgiwg.testsuite.getfeatureinfo;

import static org.testng.Assert.assertTrue;

import java.net.URI;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.opengis.cite.wmts10.core.domain.ProtocolBinding;
import org.opengis.cite.wmts10.core.domain.DGIWGWMTS;
import org.opengis.cite.wmts10.core.util.ServiceMetadataUtils;
import org.opengis.cite.wmts10.core.dgiwg.testsuite.getfeatureinfo.AbstractBaseGetFeatureInfoFixture;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.jersey.api.client.ClientResponse;

import de.latlon.ets.core.assertion.ETSAssert;
import de.latlon.ets.core.error.ErrorMessage;
import de.latlon.ets.core.error.ErrorMessageKey;

/**
 *
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class GetFeatureInfoKvp extends AbstractBaseGetFeatureInfoFixture {
	/**
	 * DGIWG WMTS requirement 2 A WMTS Server shall support HTTP GET operation using
	 * KVP (clause 8 of OGC WMS) and RESTful (clause 10 of OGC WMTS 1.0) encodings.
	 */

	private URI getFeatureInfoURI;

	private boolean _debug = false;

	@Test(groups = {"A WMTS Server shall support HTTP GET operation using KVP (clause 8 of OGC WMS) and RESTful (clause 10 of OGC WMTS 1.0) encodings."}, 
			description = "Checks wmtsGetFeatureInfoExists.", dependsOnMethods = "verifyGetFeatureInfoSupported")
	public void wmtsGetFeatureInfoExists() {
		//System.out.println("....wmtsGetFeatureInfoExists start : " );
		getFeatureInfoURI = ServiceMetadataUtils.getOperationEndpoint_KVP(this.wmtsCapabilities,
				DGIWGWMTS.GET_FEATURE_INFO, ProtocolBinding.GET);
		assertTrue(getFeatureInfoURI != null,
				"GetFeatureInfo (GET) endpoint not found or KVP is not supported in ServiceMetadata capabilities document.");
		//System.out.println("....wmtsGetFeatureInfoExists end : " );
	}

	@Test(groups = {"A WMTS Server shall support HTTP GET operation using KVP (clause 8 of OGC WMS) and RESTful (clause 10 of OGC WMTS 1.0) encodings."}, 
			description = "Checks wmtsGetFeatureInfoRequestParameters.", dependsOnMethods = "wmtsGetFeatureInfoExists")
	public void wmtsGetFeatureInfoRequestParameters(ITestContext testContext) {
		if (getFeatureInfoURI == null) {
			getFeatureInfoURI = ServiceMetadataUtils.getOperationEndpoint_KVP(this.wmtsCapabilities,
					DGIWGWMTS.GET_FEATURE_INFO, ProtocolBinding.GET);
		}
		String requestFormat = null;
		

		
		
		try {
			XPath xPath = createXPath();
			// this function does not work
			// modify code because this.reqEntity can be null
			String layerName = null;
			if (this.reqEntity != null) {
				layerName = this.reqEntity.getKvpValue(DGIWGWMTS.LAYER_PARAM);
			}

			if (layerName == null) {
				NodeList layers = ServiceMetadataUtils.getNodeElements(xPath, wmtsCapabilities,
						"//wmts:Contents/wmts:Layer/ows:Identifier");
				if (layers.getLength() > 0) {
					layerName = ((Node) layers.item(0)).getTextContent().trim();
				}
			}
			// NodeList imageFormats = ServiceMetadataUtils.getNodeElements( xPath,
			// wmtsCapabilities,
			// "//wmts:Contents/wmts:Layer[ows:Identifier = '" + layerName +
			// "']/wmts:Format");

			/*--
			String pixelI = this.reqEntity.getKvpValue(WMTS_Constants.I_PARAM);
			String pixelJ = this.reqEntity.getKvpValue(WMTS_Constants.J_PARAM);
			--*/

			// this function does not work
			String infoFormat = null;
			if (this.reqEntity != null) {
				infoFormat = this.reqEntity.getKvpValue(DGIWGWMTS.INFO_FORMAT_PARAM);
			}

			
			// CESTA supersedes TILE_COL_PARAM, TILE_ROW_PARAM and TILE_MATRIX_PARAM to be sure that the tile really exists
			this.reqEntity.removeKvp(DGIWGWMTS.TILE_COL_PARAM);
			this.reqEntity.addKvp(DGIWGWMTS.TILE_COL_PARAM, "1");
			this.reqEntity.removeKvp(DGIWGWMTS.TILE_ROW_PARAM);
			this.reqEntity.addKvp(DGIWGWMTS.TILE_ROW_PARAM, "1");
			this.reqEntity.removeKvp(DGIWGWMTS.TILE_MATRIX_PARAM);
			this.reqEntity.addKvp(DGIWGWMTS.TILE_MATRIX_PARAM, "2");
			
			
			// SoftAssert sa = new SoftAssert();

			// for(int i=0; i<imageFormats.getLength(); i++)
			// {
			// this.reqEntity.removeKvp( WMTS_Constants.FORMAT_PARAM );

			// requestFormat = imageFormats.item(i).getTextContent().trim();
			// this.reqEntity.addKvp( WMTS_Constants.FORMAT_PARAM, requestFormat );
			ClientResponse rsp = null;
			if (this.reqEntity != null) {
				rsp = wmtsClient.submitRequest(this.reqEntity, getFeatureInfoURI);
			}
			// storeResponseImage( rsp, "Requirement5", "simple", requestFormat );
			/*--
			 sa.assertTrue( rsp.hasEntity(), ErrorMessage.get( ErrorMessageKey.MISSING_XML_ENTITY ) );
			 WmtsAssertion.assertStatusCode(  sa, rsp.getStatus(),  200 );
			 WmtsAssertion.assertContentType( sa, rsp.getHeaders(), requestFormat );
			 --*/
			if (rsp != null) {
				assertTrue(rsp.hasEntity(), ErrorMessage.get(ErrorMessageKey.MISSING_XML_ENTITY));
				ETSAssert.assertStatusCode(rsp.getStatus(), 200);
				ETSAssert.assertContentType(rsp.getHeaders(), infoFormat);
			}
			// }
			// sa.assertAll();
		} catch (XPathExpressionException | XPathFactoryConfigurationException xpe) {
			System.out.println(xpe.getMessage());
			if (this._debug) {
				xpe.printStackTrace();
			}
			assertTrue(false, "Invalid or corrupt XML or KVP structure:  " + xpe.getMessage());
		}
	}

	private XPath createXPath() throws XPathFactoryConfigurationException {
		XPathFactory factory = XPathFactory.newInstance(XPathConstants.DOM_OBJECT_MODEL);
		XPath xpath = factory.newXPath();
		xpath.setNamespaceContext(NS_BINDINGS);
		return xpath;
	}

}