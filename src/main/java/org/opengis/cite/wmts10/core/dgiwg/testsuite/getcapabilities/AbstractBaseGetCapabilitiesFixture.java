package org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities;

import static org.testng.Assert.assertNotNull;

import javax.xml.xpath.XPathExpressionException;

import org.opengis.cite.wmts10.core.util.ServiceMetadataUtils;
//import org.opengeospatial.cite.wmts10.ets.core.util.request.WmtsKvpRequestBuilder;
import org.opengis.cite.wmts10.core.util.request.WmtsRequestBuilder;
import org.opengis.cite.wmts10.core.dgiwg.testsuite.AbstractBaseGetFixture;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Node;

/**
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a> (original)
 * @author Jim Beatty (modified/fixed May/Jun/Jul-2017 for WMS and/or WMTS)
 */
public abstract class AbstractBaseGetCapabilitiesFixture extends AbstractBaseGetFixture {
    /**
     * Builds a (WmtsKvpRequest} representing a GetCapabilities request for a complete service metadata document.
     */
    @BeforeClass
    public void buildGetCapabilitiesRequest() {
        this.reqEntity = WmtsRequestBuilder.buildGetCapabilitiesRequest( wmtsCapabilities, layerInfo );
    }

    @Test
    public void verifyGetCapabilitiesSupported() {
    	//System.out.println("....verifyGetCapabilitiesSupported : " );
        Node getCapabilitiesEntry = null;
        try {
            getCapabilitiesEntry = (Node) ServiceMetadataUtils.getNode( wmtsCapabilities,
                                                                        "//ows:OperationsMetadata/ows:Operation[@name = 'GetCapabilities']" );
            //System.out.println("....verifyGetCapabilitiesSupported 2 getCapabilitiesEntry : getCapabilitiesEntry" );
        } catch ( XPathExpressionException e ) {
        }
        assertNotNull( getCapabilitiesEntry, "GetCapabilities is not supported by this WMTS TOTO" );
        //System.out.println("....verifyGetCapabilitiesSupported 3 getCapabilitiesEntry : getCapabilitiesEntry" );
    } 

}