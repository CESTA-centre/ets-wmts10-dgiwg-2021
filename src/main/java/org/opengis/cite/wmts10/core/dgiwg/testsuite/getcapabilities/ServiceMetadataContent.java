package org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities;

import static de.latlon.ets.core.assertion.ETSAssert.assertXPath;
import static org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesKeywordTest.verifyNASkeywords;
import static org.testng.Assert.assertTrue;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.opengis.cite.wmts10.core.assertion.WmtsAssertion;
import org.opengis.cite.wmts10.core.domain.LayerInfo;
//import org.opengeospatial.cite.wmts10.ets.core.domain.WMTS_Constants;
import org.opengis.cite.wmts10.core.domain.DGIWGWMTS;
import org.opengis.cite.wmts10.core.util.ServiceMetadataUtils;
import org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.AbstractBaseGetCapabilitiesFixture;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.testng.util.Strings;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class ServiceMetadataContent extends AbstractBaseGetCapabilitiesFixture {
    /**
     * DGIWG WMTS 1.0, Requirement 4 
     * Each layer's style shall have an associated legend (using the legendURL element) if the data being provisioned 
     * is symbolized/portrayed (i.e. not imagery).
     */


    
    @Test(groups = {"Each layer's style shall have an associated legend (using the <legendURL >element) if the data being provisioned is symbolized/portrayed (i.e. not imagery)."},description = "Verifies if Capabilities exists", dependsOnMethods = "verifyGetCapabilitiesSupported")
    public void wmtsCapabilitiesExists() {
        // --- base test
        assertXPath( ".", wmtsCapabilities, NS_BINDINGS );
    }

    @Test(groups = {"Each layer's style shall have an associated legend (using the <legendURL >element) if the data being provisioned is symbolized/portrayed (i.e. not imagery)."},description = "Verifies if capabilities service indetification exists", dependsOnMethods = "wmtsCapabilitiesExists")
    public void wmtsCapabilitiesServiceIdentificationExists() {
        // --- Test Method: 1 (The response has all required service metadata elements)
        assertXPath( "//ows:ServiceIdentification", wmtsCapabilities, NS_BINDINGS );
    }

    @Test(groups = {"Each layer's style shall have an associated legend (using the <legendURL >element) if the data being provisioned is symbolized/portrayed (i.e. not imagery)."},description = "Verifies if capabilities service provider exists", dependsOnMethods = "wmtsCapabilitiesExists")
    public void wmtsCapabilitiesServiceProviderExists() {
        // --- Test Method: 1 (The response has all required service metadata elements)
        assertXPath( "//ows:ServiceProvider", wmtsCapabilities, NS_BINDINGS );
    }

    @Test(groups = {"Each layer's style shall have an associated legend (using the <legendURL >element) if the data being provisioned is symbolized/portrayed (i.e. not imagery)."},description = "Verifies if capabilities operation metada exists", dependsOnMethods = "wmtsCapabilitiesExists")
    public void wmtsCapabilitiesOperationsMetadataExists() {
        // --- Test Method: 1 (The response has all required service metadata elements)
        assertXPath( "//ows:OperationsMetadata", wmtsCapabilities, NS_BINDINGS );
    }

    @Test(groups = {"Each layer's style shall have an associated legend (using the <legendURL >element) if the data being provisioned is symbolized/portrayed (i.e. not imagery)."},description = "Verifies if capabilities contents exists", dependsOnMethods = "wmtsCapabilitiesExists")
    public void wmtsCapabilitiesContentsExists() {
        // --- Test Method: 1 (The response has all required service metadata elements)
        assertXPath( "//wmts:Contents", wmtsCapabilities, NS_BINDINGS );
    }


    @Test(groups = {"Each layer's style shall have an associated legend (using the <legendURL >element) if the data being provisioned is symbolized/portrayed (i.e. not imagery)."},description = "Verifies capabilities layer style legend", dependsOnMethods = "wmtsCapabilitiesContentsExists")
    public void wmtsCapabilitiesLayerStyleLegends()
                            throws XPathExpressionException, XPathFactoryConfigurationException {

        XPath xPath = createXPath();
        if ( ( layerInfo == null ) || ( layerInfo.size() <= 0 ) ) {
            throw new SkipException( "There are no Layers identified" );
        }

        SoftAssert sa = new SoftAssert();

        for ( int i = 0; i < layerInfo.size(); i++ ) {
            LayerInfo layer = layerInfo.get( i );

            String exprPath = "//wmts:Contents/wmts:Layer[ows:Identifier = '" + layer.getLayerName() + "']/wmts:Style";
            // --- will soft assess in order to go thru all layers // assertXPath(exprPath, wmtsCapabilities,
            // NS_BINDINGS);
            
            
            NodeList layerStyles = (NodeList) xPath.evaluate( exprPath, wmtsCapabilities, XPathConstants.NODESET );
            sa.assertTrue( ( layerStyles != null ) && ( layerStyles.getLength() > 0 ),
                           "There are no <Style> elements for <Layer>:  " + layer.getLayerName() );

            if ( ( layerStyles != null ) && ( layerStyles.getLength() > 0 ) ) {
                for ( int si = 0; si < layerStyles.getLength(); si++ ) {
                    Node style = layerStyles.item( si );
                    String styleIdentifier = ServiceMetadataUtils.parseNodeElementName( xPath, style );
                    

                    NodeList legendList = (NodeList) xPath.evaluate( "./wmts:LegendURL", style, XPathConstants.NODESET );
                    sa.assertTrue( ( legendList != null ) && ( legendList.getLength() > 0 ),
                                   "There is no Legend for <Style>: " + styleIdentifier + " under <Layer>: "
                                                           + layer.getLayerName() );
                }
                    
            }
        }
        sa.assertAll();
    }


    private XPath createXPath()
                            throws XPathFactoryConfigurationException {
        XPathFactory factory = XPathFactory.newInstance( XPathConstants.DOM_OBJECT_MODEL );
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext( NS_BINDINGS );
        return xpath;
    }

}