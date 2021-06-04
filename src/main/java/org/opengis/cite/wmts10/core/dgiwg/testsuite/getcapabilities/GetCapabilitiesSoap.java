package org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities;

import static de.latlon.ets.core.assertion.ETSAssert.assertUrl;
import static de.latlon.ets.core.assertion.ETSAssert.assertXPath;
import static org.testng.Assert.assertTrue;

import java.net.URI;

import javax.xml.soap.SOAPMessage;

import org.opengis.cite.wmts10.core.domain.ProtocolBinding;
import org.opengis.cite.wmts10.core.domain.DGIWGWMTS;
import org.opengis.cite.wmts10.core.domain.WmtsNamespaces;
import org.opengis.cite.wmts10.core.util.ServiceMetadataUtils;
import  org.opengis.cite.wmts10.core.util.WmtsSoapContainer;
import org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.AbstractBaseGetCapabilitiesFixture;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

/**
 *
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class GetCapabilitiesSoap extends AbstractBaseGetCapabilitiesFixture {
    /**
     * --- NSG Requirement 17: If a WMTS server offers its functionality via the SOAP protocol, it shall do so in compliance 
     * with the Messaging Service SIP [NCIA TR/2012/SPW008000/30, 2012] which defines general requirements that apply 
     * to all services in the NNEC environment that make use of SOAP. 
     */
    private URI soapURI;

    GetCapabilitiesSoap() {
    }

    @Test(description = "DGIWG WMTS 1.0, Requirement 17", dependsOnMethods = "verifyGetCapabilitiesSupported")
    public void wmtsCapabilitiesSoapSupported() {
        soapURI = ServiceMetadataUtils.getOperationEndpoint_SOAP( wmtsCapabilities, DGIWGWMTS.GET_CAPABILITIES,
                                                                  ProtocolBinding.POST );
        if ( this.soapURI == null )
            throw new SkipException(
                                     "GetCapabilities (POST) endpoint not found in ServiceMetadata capabilities document or WMTS does not support SOAP." );
    }

    @Test(description = "DGIWG WMTS 1.0, Requirement 17", dependsOnMethods = "wmtsCapabilitiesSoapSupported")
    public void wmtsCapabilitiesSoapReponseTest() {
        assertTrue( soapURI != null, "There is no SOAP URL to test against" );

        String soapURIstr = soapURI.toString();
        assertUrl( soapURIstr );

        WmtsSoapContainer soap = new WmtsSoapContainer( DGIWGWMTS.GET_CAPABILITIES, soapURIstr );

        soap.addParameterWithChild( WmtsNamespaces.serviceOWS, DGIWGWMTS.ACCEPT_VERSIONS_PARAM,
        		DGIWGWMTS.VERSION_PARAM, DGIWGWMTS.VERSION );
        soap.addParameterWithChild( WmtsNamespaces.serviceOWS, DGIWGWMTS.ACCEPT_FORMAT_PARAM,
        		DGIWGWMTS.OUTPUT_PARAM, DGIWGWMTS.SOAP_XML );

        SOAPMessage soapResponse = soap.getSoapResponse( true );
        assertTrue( soapResponse != null, "SOAP reposnse came back null" );

        Document soapDocument = soap.getResponseDocument();
        assertXPath( "//wmts:Capabilities/@version = '1.0.0'", soapDocument, NS_BINDINGS );

    }

}