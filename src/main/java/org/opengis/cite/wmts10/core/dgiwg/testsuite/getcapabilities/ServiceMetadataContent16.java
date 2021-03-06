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
public class ServiceMetadataContent16 extends AbstractBaseGetCapabilitiesFixture {
	/**
	 * If legend is present (see req 14), the LegendURL element shall specify a
	 * URL to allow access to an image of the legend. Note : This URL will relate to
	 * the source system and may not be resolvable on all connected/unconnected
	 * systems or applications. This requirement is conditional on the LegendURL
	 * being relevant to the generated service.
	 */
	
	private static final String GROUPE_NAME = "If legend is present (see req 14), the <LegendURL> element shall specify a URL to allow access to an image of the legend. \n"
			+ "Note : This URL will relate to the source system and may not be resolvable on all connected/unconnected systems or applications. This requirement is conditional on the <LegendURL> being relevant to the generated service.";

	@Test(groups = GROUPE_NAME,description = "Verifies if Capabilities exists", dependsOnMethods = "verifyGetCapabilitiesSupported")
	public void wmtsCapabilitiesExists() {
		// --- base test
		assertXPath(".", wmtsCapabilities, NS_BINDINGS);
	}

	@Test(groups = GROUPE_NAME,description = "Verifies if capabilities service indetification exists", dependsOnMethods = "wmtsCapabilitiesExists")
	public void wmtsCapabilitiesServiceIdentificationExists() {
		// --- Test Method: 1 (The response has all required service metadata elements)
		assertXPath("//ows:ServiceIdentification", wmtsCapabilities, NS_BINDINGS);
	}

	@Test(groups = GROUPE_NAME,description = "Verifies if capabilities service provider exists", dependsOnMethods = "wmtsCapabilitiesExists")
	public void wmtsCapabilitiesServiceProviderExists() {
		// --- Test Method: 1 (The response has all required service metadata elements)
		assertXPath("//ows:ServiceProvider", wmtsCapabilities, NS_BINDINGS);
	}

	@Test(groups = GROUPE_NAME,description = "Verifies if capabilities operation metada exists", dependsOnMethods = "wmtsCapabilitiesExists")
	public void wmtsCapabilitiesOperationsMetadataExists() {
		// --- Test Method: 1 (The response has all required service metadata elements)
		assertXPath("//ows:OperationsMetadata", wmtsCapabilities, NS_BINDINGS);
	}

	@Test(groups = GROUPE_NAME,description = "Verifies if capabilities contents exists", dependsOnMethods = "wmtsCapabilitiesExists")
	public void wmtsCapabilitiesContentsExists() {
		// --- Test Method: 1 (The response has all required service metadata elements)
		assertXPath("//wmts:Contents", wmtsCapabilities, NS_BINDINGS);
	}


	@Test(groups = GROUPE_NAME,description = "Verifies capabilities layer style legend", dependsOnMethods = "wmtsCapabilitiesContentsExists")
	public void wmtsCapabilitiesLayerStyleLegends()
			throws XPathExpressionException, XPathFactoryConfigurationException {
		// --- Test Method: 7 (The response provides an associated legend in at least
		// one of the following formats: PNG,
		// GIF, JPEG)
		// --- Test Method: 8 (The provided LegendURL is accessible online)

		XPath xPath = createXPath();
		if ((layerInfo == null) || (layerInfo.size() <= 0)) {
			throw new SkipException("There are no Layers identified");
		}

		SoftAssert sa = new SoftAssert();

		for (int i = 0; i < layerInfo.size(); i++) {
			LayerInfo layer = layerInfo.get(i);

			String exprPath = "//wmts:Contents/wmts:Layer[ows:Identifier = '" + layer.getLayerName() + "']/wmts:Style";
			// --- will soft assess in order to go thru all layers // assertXPath(exprPath,
			// wmtsCapabilities,
			// NS_BINDINGS);


			NodeList layerStyles = (NodeList) xPath.evaluate(exprPath, wmtsCapabilities, XPathConstants.NODESET);
			sa.assertTrue((layerStyles != null) && (layerStyles.getLength() > 0),
					"There are no <Style> elements for <Layer>:  " + layer.getLayerName());

			if ((layerStyles != null) && (layerStyles.getLength() > 0)) {
				for (int si = 0; si < layerStyles.getLength(); si++) {
					Node style = layerStyles.item(si);
					String styleIdentifier = ServiceMetadataUtils.parseNodeElementName(xPath, style);


					NodeList legendList = (NodeList) xPath.evaluate("./wmts:LegendURL", style, XPathConstants.NODESET);
					sa.assertTrue((legendList != null) && (legendList.getLength() > 0),
							"There is no Legend for <Style>: " + styleIdentifier + " under <Layer>: "
									+ layer.getLayerName());

					if ((legendList != null) && (legendList.getLength() > 0)) {
						boolean foundPreferredFormat = false;
						for (int li = 0; li < legendList.getLength(); li++) {
							Node legend = legendList.item(li);

							String format = (String) xPath.evaluate("@format", legend, XPathConstants.STRING);
							String url = (String) xPath.evaluate("@xlink:href", legend, XPathConstants.STRING);


							sa.assertTrue((!Strings.isNullOrEmpty(format)) && (!Strings.isNullOrEmpty(url)),
									"Legend for Style: " + styleIdentifier + " under Layer: " + layer.getLayerName()
											+ " is not properly defined.");

							// -- Test for formats (Test Method 7)
							foundPreferredFormat |= (format.equals(DGIWGWMTS.IMAGE_PNG)
									|| format.equals(DGIWGWMTS.IMAGE_GIF) || format.equals(DGIWGWMTS.IMAGE_JPEG));

							// -- Test for valid url (Test Method 8)
							WmtsAssertion.assertUrl(sa, url);
							WmtsAssertion.assertUriIsResolvable(sa, url);
							//System.out.println("....url : " + url);

						}
						sa.assertTrue(foundPreferredFormat, "<Style>: " + styleIdentifier + " under <Layer>: "
								+ layer.getLayerName() + " does not use a preferred Legend image format.");
					}
				}
			}
		}
		sa.assertAll();
	}

	private XPath createXPath() throws XPathFactoryConfigurationException {
		XPathFactory factory = XPathFactory.newInstance(XPathConstants.DOM_OBJECT_MODEL);
		XPath xpath = factory.newXPath();
		xpath.setNamespaceContext(NS_BINDINGS);
		return xpath;
	}

}