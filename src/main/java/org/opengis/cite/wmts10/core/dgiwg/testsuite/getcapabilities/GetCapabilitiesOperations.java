package org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities;

import static de.latlon.ets.core.assertion.ETSAssert.assertXPath;
import static org.testng.Assert.assertTrue;

import java.net.URI;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.opengis.cite.wmts10.core.domain.ProtocolBinding;
//import org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants;
import org.opengis.cite.wmts10.core.domain.DGIWGWMTS;
import org.opengis.cite.wmts10.core.util.ServiceMetadataUtils;
import org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.AbstractBaseGetCapabilitiesFixture;
import org.testng.annotations.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class GetCapabilitiesOperations extends AbstractBaseGetCapabilitiesFixture {
    /**
     * DGIWG WMTS requirement 2
     * A WMTS Server shall support HTTP GET operation using KVP (clause 8 of OGC WMS) and RESTful (clause 10 of OGC WMTS 1.0) encodings.
     */
    @Test(groups="A WMTS Server shall support HTTP GET operation using KVP (clause 8 of OGC WMS) and RESTful (clause 10 of OGC WMTS 1.0) encodings.",description="Checks wmts capabilities.", dependsOnMethods = "verifyGetCapabilitiesSupported")
    public void wmtsCapabilitiesExists() {
        assertTrue( this.wmtsCapabilities != null, "No ServerMetadata Capabilities document" );
    }

    @Test(groups="A WMTS Server shall support HTTP GET operation using KVP (clause 8 of OGC WMS) and RESTful (clause 10 of OGC WMTS 1.0) encodings.",description="Checks wmts capabilities metadata.", dependsOnMethods = "wmtsCapabilitiesExists")
    public void wmtsCapabilitiesOperationsMetadataExists() {
        String xPathExpr = "//wmts:Capabilities/ows:OperationsMetadata != ''";
        assertXPath( xPathExpr, wmtsCapabilities, NS_BINDINGS );
    }

    @Test(groups="A WMTS Server shall support HTTP GET operation using KVP (clause 8 of OGC WMS) and RESTful (clause 10 of OGC WMTS 1.0) encodings.",description="Checks wmts capabilities metadata operation.", dependsOnMethods = "wmtsCapabilitiesOperationsMetadataExists")
    public void wmtsCapabilitiesOperationsMetadataOperationExists() {
        String xPathExpr = "//wmts:Capabilities/ows:OperationsMetadata/ows:Operation != ''";
        assertXPath( xPathExpr, wmtsCapabilities, NS_BINDINGS );
    }

    @Test(groups="A WMTS Server shall support HTTP GET operation using KVP (clause 8 of OGC WMS) and RESTful (clause 10 of OGC WMTS 1.0) encodings.",description="Checks wmts capabilities KVP request.", dependsOnMethods = "wmtsCapabilitiesOperationsMetadataOperationExists")
    public void wmtsCapabilitiesKVPRequestsExists() {
        URI uri = ServiceMetadataUtils.getOperationEndpoint_KVP( this.wmtsCapabilities,
        		DGIWGWMTS.GET_CAPABILITIES, ProtocolBinding.GET );
        assertTrue( uri != null,
                    "GetCapabilities (GET) endpoint not found or KVP is not supported in ServiceMetadata capabilities document." );
    }

    @Test(groups="A WMTS Server shall support HTTP GET operation using KVP (clause 8 of OGC WMS) and RESTful (clause 10 of OGC WMTS 1.0) encodings.",description="Checks wmts capabilities validated.", dependsOnMethods = "wmtsCapabilitiesKVPRequestsExists")
    public void wmtsCapabilitiesValidated()
                            throws XPathFactoryConfigurationException, XPathExpressionException {
        XPath xpath = createXPath();
        NodeList nodes = (NodeList) xpath.evaluate( "//wmts:Capabilities/ows:OperationsMetadata/ows:Operation",
                                                    wmtsCapabilities, XPathConstants.NODESET );

        for ( int ni = 0; ni < nodes.getLength(); ni++ ) {
            Node operation = (Node) nodes.item( ni );

            NodeList methods = (NodeList) xpath.evaluate( "./ows:DCP/ows:HTTP/*", operation, XPathConstants.NODESET );
            for ( int mi = 0; mi < methods.getLength(); mi++ ) {
                Node method = methods.item( mi );

                URI endPoint = URI.create( (String) xpath.evaluate( "@xlink:href", method, XPathConstants.STRING ) );
                if ( endPoint.getQuery() != null ) {
                    String uri = endPoint.toString();
                    endPoint = URI.create( uri.substring( 0, uri.indexOf( '?' ) ) );
                }
                /* -- ? is this the right test or should each link be tested for a return ? -- */
                assertTrue( endPoint != null, "Invalid endpoint (" + endPoint.toString()
                                              + ") located in ServiceMetadata capabilities document" );
            }
        }
    }

    private XPath createXPath()
                            throws XPathFactoryConfigurationException {
        XPathFactory factory = XPathFactory.newInstance( XPathConstants.DOM_OBJECT_MODEL );
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext( NS_BINDINGS );
        return xpath;
    }
}