package org.opengis.cite.wmts10.core.dgiwg.testsuite.gettile;

import static org.opengis.cite.wmts10.core.assertion.WmtsAssertion.assertContentType;
import static org.opengis.cite.wmts10.core.assertion.WmtsAssertion.assertStatusCode;
import static org.testng.Assert.assertTrue;

import java.net.URI;

import org.opengis.cite.wmts10.core.domain.ProtocolBinding;
import org.opengis.cite.wmts10.core.domain.DGIWGWMTS;
import org.opengis.cite.wmts10.core.util.ServiceMetadataUtils;
import org.opengis.cite.wmts10.core.dgiwg.testsuite.gettile.AbstractBaseGetTileFixture;
import org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesKvpFormatTest;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.sun.jersey.api.client.ClientResponse;

import de.latlon.ets.core.error.ErrorMessage;
import de.latlon.ets.core.error.ErrorMessageKey;

//import org.testng.asserts.SoftAssert;

/**
 *
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class GetTileOfferings extends AbstractBaseGetTileFixture {
	/**
	 * --- DGIWG WMTS requirement 4 A WMTS server shall provide tiles in at least
	 * one of the following raster formats : • image/png (Portable Network Graphics)
	 * • image/gif (Graphics Interchange Format) • image/jpeg (Joint Photographics
	 * Expert Group)... ---
	 */

	private URI getTileURI = null;

	@Test(groups="A WMTS server shall provide tiles in at least one of the following raster formats  : • image/png (Portable Network Graphics) • image/gif (Graphics Interchange Format) • image/jpeg (Joint Photographics Expert Group).",description = "Checks wmtsGetCapabilitiiesWithXML")
	public void wmtsGetCapabilitiiesWithXML(ITestContext testContext) {
		GetCapabilitiesKvpFormatTest gck = new GetCapabilitiesKvpFormatTest();
		gck.init(testContext);

		gck.TestXML();
	}

	@Test(groups="A WMTS server shall provide tiles in at least one of the following raster formats  : • image/png (Portable Network Graphics) • image/gif (Graphics Interchange Format) • image/jpeg (Joint Photographics Expert Group).",description = "Checks wmtsGetCapabilitiiesWithHTML")
	public void wmtsGetCapabilitiiesWithHTML(ITestContext testContext) {
		GetCapabilitiesKvpFormatTest gck = new GetCapabilitiesKvpFormatTest();
		gck.init(testContext);

		gck.TestHTML();
	}

	/*
	@Test(groups="A WMTS server shall provide tiles in at least one of the following raster formats  : • image/png (Portable Network Graphics) • image/gif (Graphics Interchange Format) • image/jpeg (Joint Photographics Expert Group).",description = "DGIWG WMTS 1.0, Requirement 4")
	public void wmtsGetCapabilitiiesInEnglish() {
		throw new SkipException("Test for results in English not implemented.");
	}
	*/

	@Test(groups="A WMTS server shall provide tiles in at least one of the following raster formats  : • image/png (Portable Network Graphics) • image/gif (Graphics Interchange Format) • image/jpeg (Joint Photographics Expert Group).",description = "Checks GetTileKVPRequests", dependsOnMethods = "verifyGetTileSupported")
	public void wmtsGetTileKVPRequestsExists() {
		getTileURI = ServiceMetadataUtils.getOperationEndpoint_KVP(this.wmtsCapabilities, DGIWGWMTS.GET_TILE,
				ProtocolBinding.GET);
		assertTrue(getTileURI != null,
				"GetTile (GET) endpoint not found or KVP is not supported in ServiceMetadata capabilities document.");
	}

	@Test(groups="A WMTS server shall provide tiles in at least one of the following raster formats  : • image/png (Portable Network Graphics) • image/gif (Graphics Interchange Format) • image/jpeg (Joint Photographics Expert Group).",description = "Checks PNG format", dependsOnMethods = "wmtsGetTileKVPRequestsExists")
	public void wmtsGetTileOfferingsTestPNG() {
		checkGetTileWithImageFormat(DGIWGWMTS.IMAGE_PNG);
	}

	@Test(groups="A WMTS server shall provide tiles in at least one of the following raster formats  : • image/png (Portable Network Graphics) • image/gif (Graphics Interchange Format) • image/jpeg (Joint Photographics Expert Group).",description = "Checks JPEG format", dependsOnMethods = "wmtsGetTileKVPRequestsExists")
	public void wmtsGetTileOfferingsTestJPG() {
		checkGetTileWithImageFormat(DGIWGWMTS.IMAGE_JPEG);
	}

	@Test(groups="A WMTS server shall provide tiles in at least one of the following raster formats  : • image/png (Portable Network Graphics) • image/gif (Graphics Interchange Format) • image/jpeg (Joint Photographics Expert Group).",description = "Checks GIF format", dependsOnMethods = "wmtsGetTileKVPRequestsExists")
	public void wmtsGetTileOfferingsTestGIF() {
		checkGetTileWithImageFormat(DGIWGWMTS.IMAGE_GIF);
	}

	private void checkGetTileWithImageFormat(String requestFormat) {
		System.out.println("....checkGetTileWithImageFormat  1: " + requestFormat);
		if (getTileURI == null) {
			getTileURI = ServiceMetadataUtils.getOperationEndpoint_KVP(this.wmtsCapabilities, DGIWGWMTS.GET_TILE,
					ProtocolBinding.GET);
		}
		System.out.println("....checkGetTileWithImageFormat  2: " + getTileURI);
		this.reqEntity.removeKvp(DGIWGWMTS.FORMAT_PARAM);
		this.reqEntity.addKvp(DGIWGWMTS.FORMAT_PARAM, requestFormat);
		System.out.println("....checkGetTileWithImageFormat  3: ");
		ClientResponse rsp = wmtsClient.submitRequest(this.reqEntity, getTileURI);
		System.out.println("....checkGetTileWithImageFormat  4 : " + rsp);
		storeResponseImage(rsp, "Requirement4", "simple", requestFormat);

		assertTrue(rsp.hasEntity(), ErrorMessage.get(ErrorMessageKey.MISSING_XML_ENTITY));
		assertStatusCode(rsp.getStatus(), 200);
		assertContentType(rsp.getHeaders(), requestFormat);
	}

}