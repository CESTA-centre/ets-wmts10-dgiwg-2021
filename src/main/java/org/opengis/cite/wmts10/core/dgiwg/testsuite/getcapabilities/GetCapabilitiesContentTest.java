package org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities;

import static de.latlon.ets.core.assertion.ETSAssert.assertXPath;

import javax.xml.soap.SOAPException;

import org.testng.annotations.Test;

/**
 * Tests if the capabilities provides all mandatory and optional service metadata elements.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class GetCapabilitiesContentTest extends AbstractBaseGetCapabilitiesFixture {

    @Test(description = "DGIWG WMTS 1.0, Requirement 8")
    public void wmsCapabilitiesNameExists()
                    throws SOAPException {
    	//String xPathXml = "//ows:ServiceProvider/ows:ServiceContact/text() != ''";
    	String xPathXml = "//ows:ServiceProvider/ows:ServiceContact != ''";
    	System.out.println("....GetCapabilitiesContentTest : " + xPathXml);
        assertXPath( xPathXml, wmtsCapabilities, NS_BINDINGS );
    }
    @Test(description = "DGIWG WMTS 1.0, Requirement 8")
    public void wmsCapabilitiesTitleExists()
                    throws SOAPException {
        //String xPathXml = "//ows:ServiceProvider/ows:Keywords != ''";
        String xPathXml = "//ows:ServiceIdentification/ows:Keywords != ''";
        assertXPath( xPathXml, wmtsCapabilities, NS_BINDINGS );
    }

}