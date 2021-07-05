package org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities;

import static de.latlon.ets.core.assertion.ETSAssert.assertXPath;
import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.GET_CAPABILITIES;
import static org.testng.Assert.assertTrue;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.testng.annotations.Test;
import org.w3c.dom.Document;

import com.sun.jersey.api.client.ClientResponse;

import org.opengis.cite.wmts10.core.domain.ProtocolBinding;
import org.opengis.cite.wmts10.core.util.ServiceMetadataUtils;

/**
 * Tests if the capabilities contains a valid value for AccessConstraint.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class GetCapabilitiesAccessConstraintTest9 extends AbstractBaseGetCapabilitiesFixture {

    private static final List<String> EXPECTED_ACCESS_CONSTRAINTS = Arrays.asList( "unclassified", "restricted",
            "confidential", "secret", "topSecret" );

    @Test(description = "DGIWG WMTS 1.0, Requirement 9")
    public void wmsCapabilitiesAccessConstraintsExists() {
        //String xPathXml = "//wms:WMS_Capabilities/wms:Service/wms:AccessConstraints != ''";
        String xPathXml = "//ows:ServiceIdentification/ows:AccessConstraints != ''";
        assertXPath( xPathXml, wmtsCapabilities, NS_BINDINGS );
    }

    @Test(description = "DGIWG WMTS 1.0, Requirement 9", dependsOnMethods = "wmsCapabilitiesAccessConstraintsExists")
    public
                    void wmsCapabilitiesAccessConstraintsContainsValueFromDMF()
                                    throws XPathFactoryConfigurationException, XPathExpressionException {
    	//MGE attention on remplace getOperationEndpoint par getOperationEndpoint_KVP
        URI endpoint = ServiceMetadataUtils.getOperationEndpoint_KVP( this.wmtsCapabilities, GET_CAPABILITIES,
                                                                  ProtocolBinding.GET );
        ClientResponse rsp = wmtsClient.submitRequest( this.reqEntity, endpoint );

        String accessConstraints = parseAccessConstraints( rsp );
        assertTrue( EXPECTED_ACCESS_CONSTRAINTS.contains( accessConstraints ),
                "AccessConstraints are not valid, must be one of " + EXPECTED_ACCESS_CONSTRAINTS + " but was "
                                + accessConstraints );
       
    }

    private String parseAccessConstraints( ClientResponse rsp )
                    throws XPathFactoryConfigurationException, XPathExpressionException {
        //String xPathAccessConstraints = "//wms:WMS_Capabilities/wms:Service/wms:AccessConstraints";
        String xPathAccessConstraints = "//ows:ServiceIdentification/ows:AccessConstraints";
        XPathFactory factory = XPathFactory.newInstance( XPathConstants.DOM_OBJECT_MODEL );
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext( NS_BINDINGS );
        return (String) xpath.evaluate( xPathAccessConstraints, rsp.getEntity( Document.class ), XPathConstants.STRING );
    }

}