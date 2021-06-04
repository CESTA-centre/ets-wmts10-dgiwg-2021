package org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities;

import static org.opengis.cite.wmts10.core.assertion.WmtsAssertion.assertContentType;
import static org.opengis.cite.wmts10.core.assertion.WmtsAssertion.assertStatusCode;
import static org.testng.Assert.assertTrue;

import java.net.URI;

import org.opengis.cite.wmts10.core.domain.ProtocolBinding;
import org.opengis.cite.wmts10.core.domain.DGIWGWMTS;
import org.opengis.cite.wmts10.core.util.ServiceMetadataUtils;
import org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.AbstractBaseGetCapabilitiesFixture;
import org.testng.ITestContext;

import com.sun.jersey.api.client.ClientResponse;

import de.latlon.ets.core.error.ErrorMessage;
import de.latlon.ets.core.error.ErrorMessageKey;

/**
 *
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class GetCapabilitiesKvpFormatTest extends AbstractBaseGetCapabilitiesFixture {
	/**
	 * --- NSG Requirement 15: An NSG WMTS server shall support negotiation of the
	 * standard version used for client-server interactions (KVP encoding). ---
	 */
	private URI getCapabilitiesURI;

	public void init(ITestContext testContext) {
		this.initBaseFixture(testContext);
		this.initParser();

		getCapabilitiesURI = ServiceMetadataUtils.getOperationEndpoint_KVP(this.wmtsCapabilities,
				DGIWGWMTS.GET_CAPABILITIES, ProtocolBinding.GET);
		assertTrue(getCapabilitiesURI != null,
				"GetCapabilities (GET) endpoint not found or KVP is not supported in ServiceMetadata capabilities document.");

		this.buildGetCapabilitiesRequest();

	}

	public void TestXML() {
		verifyFormatResponse(DGIWGWMTS.TEXT_XML);
	}

	public void TestHTML() {
		verifyFormatResponse(DGIWGWMTS.TEXT_HTML);
	}

	private void verifyFormatResponse(String requestFormat) {
		if (getCapabilitiesURI == null) {
			getCapabilitiesURI = ServiceMetadataUtils.getOperationEndpoint_KVP(this.wmtsCapabilities,
					DGIWGWMTS.GET_CAPABILITIES, ProtocolBinding.GET);
		}

		this.reqEntity.removeKvp(DGIWGWMTS.FORMAT_PARAM);
		this.reqEntity.addKvp(DGIWGWMTS.FORMAT_PARAM, requestFormat);

		ClientResponse rsp = wmtsClient.submitRequest(this.reqEntity, getCapabilitiesURI);
		assertTrue(rsp.hasEntity(), ErrorMessage.get(ErrorMessageKey.MISSING_XML_ENTITY));
		assertStatusCode(rsp.getStatus(), 200);
		assertContentType(rsp.getHeaders(), requestFormat);

	}

}