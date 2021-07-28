package org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities;

import static de.latlon.ets.core.assertion.ETSAssert.assertXPath;

import javax.xml.soap.SOAPException;

import org.testng.annotations.Test;

/**
 * Tests if the capabilities provides all mandatory and optional service
 * metadata elements.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class GetCapabilitiesContentTest extends AbstractBaseGetCapabilitiesFixture {

	/*
	 * DGIWG WMTS 1.0, Requirement 8 If a WMS server is providing services to a
	 * coalition mission federated network, in support of operations or an exercise,
	 * it shall provide "ServiceContact", "AccessConstraints" and "KeywordList"
	 * elements. The provision of these metadata elements are optional for a WMTS
	 * server which is providing services across one single non-mission network.
	 */

	@Test(groups = {
			"If a WMS server is providing services to a coalition mission federated network, in support of operations or an exercise, it shall provide \"ServiceContact\", \"AccessConstraints\" and \"KeywordList\" elements. The provision of these metadata elements are optional for a WMTS server which is providing services across one single non-mission network." }, 
			description = "Checks ServiceContact.")
	//public void wmsCapabilitiesNameExists() throws SOAPException {
	public void wmsCapabilitiesServiceContactExists() throws SOAPException {
		// String xPathXml = "//ows:ServiceProvider/ows:ServiceContact/text() != ''";
		String xPathXml = "//ows:ServiceProvider/ows:ServiceContact != ''";
		assertXPath(xPathXml, wmtsCapabilities, NS_BINDINGS);
	}

	@Test(groups = {
			"If a WMS server is providing services to a coalition mission federated network, in support of operations or an exercise, it shall provide \"ServiceContact\", \"AccessConstraints\" and \"KeywordList\" elements. The provision of these metadata elements are optional for a WMTS server which is providing services across one single non-mission network." }, 
			description = "Checks Keywords.")
	public void wmsCapabilitiesKeywordsExists() throws SOAPException {
		// String xPathXml = "//ows:ServiceProvider/ows:Keywords != ''";
		String xPathXml = "//ows:ServiceIdentification/ows:Keywords != ''";
		//System.out.println("....wmtsCapabilities : " + wmtsCapabilities);
		//System.out.println("....xPathXml : " + xPathXml);
		assertXPath(xPathXml, wmtsCapabilities, NS_BINDINGS);
	}

}