package org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities;

import static org.opengis.cite.wmts10.core.domain.DGIWGWMTS.GET_CAPABILITIES;
import static org.testng.Assert.assertTrue;

import java.net.URI;

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
 * Tests if the capabilities contains a valid value for Abstract.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class GetCapabilitiesAbstractTest10 extends AbstractBaseGetCapabilitiesFixture {

	/**
	 * If a WMTS server is providing services to a coalition mission federated network, in support of operations or an exercise, 
	 * it shall include the following information in the <abstract> element of the service metadata: 
	 * "This service implements the WMTS 1.0 STANAG 6523 Ed.2 profile". 
	 * The provision of these metadata elements are optional for a WMTS server which is providing services across 
	 * one single non-mission network.
	 */
    private static final String EXPECTED_ABSTRACT = "This service implements the WMTS 1.0 STANAG 6523 Ed.2 profile";

    @Test(description = "DGIWG WMTS 1.0, Requirement 10")
    public void wmsCapabilitiesAbstractContainsProfile()
                    throws XPathFactoryConfigurationException, XPathExpressionException {
    	//MGE attention on remplace getOperationEndpoint par getOperationEndpoint_KVP
        URI endpoint = ServiceMetadataUtils.getOperationEndpoint_KVP( this.wmtsCapabilities, GET_CAPABILITIES,
                                                                  ProtocolBinding.GET );
        ClientResponse rsp = wmtsClient.submitRequest( this.reqEntity, endpoint );

        String abstractValue = parseAbstract( rsp );
        
        // MGE voir pour ecrire dans testng-results.xml
       
        assertTrue( abstractValue.contains( EXPECTED_ABSTRACT ), "Abstract is not valid, must contain the string '"
                                                                + EXPECTED_ABSTRACT + " but is '" + abstractValue + "'" );
                                                                
        //assertTrue( false, "Abstract is : '" + abstractValue + "'" );
    }

    private String parseAbstract( ClientResponse rsp )
                    throws XPathFactoryConfigurationException, XPathExpressionException {
        //String xPathAbstract = "//wms:WMS_Capabilities/wms:Title/wms:Abstract";
        String xPathAbstract = "//ows:ServiceIdentification/ows:Abstract";
        XPathFactory factory = XPathFactory.newInstance( XPathConstants.DOM_OBJECT_MODEL );
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext( NS_BINDINGS );
        return (String) xpath.evaluate( xPathAbstract, rsp.getEntity( Document.class ), XPathConstants.STRING );
    }

}