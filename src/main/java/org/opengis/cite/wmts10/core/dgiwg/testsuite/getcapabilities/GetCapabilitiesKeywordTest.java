package org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.xml.xpath.XPathException;

import org.opengis.cite.wmts10.core.domain.ProtocolBinding;
import org.opengis.cite.wmts10.core.domain.DGIWGWMTS;
import org.opengis.cite.wmts10.core.util.ServiceMetadataUtils;
import org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.AbstractBaseGetCapabilitiesFixture;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.latlon.ets.core.util.TestSuiteLogger;

/**
 *
 * @author Jim Beatty (Jun/Jul-2017 for WMTS; based on original work of:
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 *
 */
public class GetCapabilitiesKeywordTest extends AbstractBaseGetCapabilitiesFixture {
	/**
	 * DGIWG WMTS 1.0, Requirement 5
	 * If a WMTS server is providing services to a coalition mission federated
	 * network, in support of operations or an exercise, it shall provide a minimum
	 * keyword list, based on ISO 19115 Topic Categories. It's recommended to
	 * provide additional details based on the DGIF groups. The provision of these
	 * keywords elements are optional for a WMTS server which is providing services
	 * across one single non-mission network.
	 */
	private URI getCapabilitiesURI;

	private static final String KEYWORD_FILE = "nas.keywords";

	@Test(description = "DGIWG WMTS 1.0, Requirement 5", dependsOnMethods = "verifyGetCapabilitiesSupported")
	public void wmtsGetCapabilitiesURLExists() {
		getCapabilitiesURI = ServiceMetadataUtils.getOperationEndpoint_KVP(this.wmtsCapabilities,
				DGIWGWMTS.GET_CAPABILITIES, ProtocolBinding.GET);
		assertTrue(getCapabilitiesURI != null,
				"GetCapabilities (GET) endpoint not found or KVP is not supported in ServiceMetadata capabilities document.");
	}

	@Test(description = "DGIWG WMTS 1.0, Requirement 5", dependsOnMethods = "wmtsGetCapabilitiesURLExists")
	public void wmtsCapabilitiesKeywordTest() {
		verifyNASkeywords(true, "WMTS ServiceMetadata Capabilities document", wmtsCapabilities,
				"//ows:ServiceIdentification/ows:Keywords");
	}

	public static void verifyNASkeywords(boolean mandatory, String keywordLocationDescription, Node xmlNode,
			String keywordLocation) {
		try {
			Node keywordsElement = (Node) ServiceMetadataUtils.getNode(xmlNode, keywordLocation);
			if (mandatory) {
				assertFalse((keywordsElement == null),
						keywordLocationDescription + " contains no mandatory <Keywords> Element.");
				assertFalse(
						((keywordsElement.getFirstChild() == null)
								|| (keywordsElement.getChildNodes().getLength() <= 0)),
						keywordLocationDescription + " contains no <Keyword> Elements under the <Keywords>.");
			} else {
				if (keywordsElement == null) {
					throw new SkipException(
							"There is no <Keywords> Element to compare under " + keywordLocationDescription);
				} else if ((keywordsElement.getFirstChild() == null)
						|| (keywordsElement.getChildNodes().getLength() <= 0)) {
					throw new SkipException("There are no <Keyword> Elements under the <Keywords> to compare under "
							+ keywordLocationDescription);
				}
			}

			NodeList keywords = (NodeList) keywordsElement.getChildNodes();

			List<String> keywordsToCheck = new ArrayList<String>();
			for (int keywordNodeIndex = 0; keywordNodeIndex < keywords.getLength(); keywordNodeIndex++) {
				Node keywordNode = keywords.item(keywordNodeIndex);
				String keyword = keywordNode.getTextContent();
				System.out.println("....verifyNASkeywords  keyword : " + keyword);
				if (keyword != null)
					keywordsToCheck.add(keyword.toLowerCase().trim());
			}
			assertFalse((keywordsToCheck == null) || (keywordsToCheck.isEmpty()) || (keywordsToCheck.size() < 1),
					"Error creating or corrupt Keyword list");

			boolean anyFound = false;

			try (BufferedReader br = new BufferedReader(new InputStreamReader(
					GetCapabilitiesKeywordTest.class.getResourceAsStream(KEYWORD_FILE), "UTF-8"))) {
				String nasKeyword;
				while ((nasKeyword = br.readLine()) != null) {
					nasKeyword = nasKeyword.toLowerCase().trim();
					System.out.println("....verifyNASkeywords  nasKeyword : " + nasKeyword);
					if (!nasKeyword.isEmpty()) {
						if (keywordsToCheck.contains(nasKeyword)) {
							anyFound = true;
							break;
						}
					}
				}
				br.close();
			} catch (IOException e) {
				TestSuiteLogger.log(Level.WARNING, "Keywords file " + KEYWORD_FILE + " could not be parsed.", e);
				assertTrue(false, "Keywords file " + KEYWORD_FILE + " could not be parsed.");
			}

			assertTrue(anyFound, "No valid NAS keywords found in: " + keywordLocationDescription);
		} catch (XPathException xpe) {
			xpe.printStackTrace();
		}
	}

}