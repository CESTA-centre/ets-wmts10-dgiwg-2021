package org.opengis.cite.wmts10.core.dgiwg.testsuite;

import static org.opengis.cite.wmts10.core.assertion.WmtsAssertion.assertSimpleWMTSCapabilities;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.ITestContext;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.opengis.cite.wmts10.core.client.WmtsClient;
import org.opengis.cite.wmts10.core.domain.DGIWGWMTS;
import org.opengis.cite.wmts10.core.domain.SuiteAttribute;
import org.opengis.cite.wmts10.core.domain.WmtsNamespaces;

/**
 * Confirms the readiness of the SUT to undergo testing. If any of these
 * configuration methods fail then all remaining tests in the suite are skipped.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class Prerequisites {

    /**
     * Verifies that the service capabilities description is a WMTS Capabilities
     * document.
     * 
     * @param testContext
     *            the test run context, never <code>null</code>
     */
    @Test
    public void verifyServiceDescription(ITestContext testContext) {
        Document wmtsMetadata = (Document) testContext.getSuite().getAttribute(SuiteAttribute.TEST_SUBJECT.getName());
        assertSimpleWMTSCapabilities(wmtsMetadata);
    }

    /**
     * Confirms that the SUT is available and produces a service description in response to a basic GetCapabilities
     * request. The document element is expected to have the following infoset properties:
     * <ul>
     * <li>[local name] = "WMTS_Capabilities"</li>
     * <li>[namespace name] = "http://www.opengis.net/wmts"</li>
     * </ul>
     *
     * @param testContext
     *            the test run context, never <code>null</code>
     */
    @Test(dependsOnMethods = { "verifyServiceDescription" })
    public void serviceIsAvailable(ITestContext testContext) {
        Document wmtsMetadata = (Document) testContext.getSuite().getAttribute(SuiteAttribute.TEST_SUBJECT.getName());
        WmtsClient wmtsClient = new WmtsClient(wmtsMetadata);
        Document capabilities = wmtsClient.getCapabilities();
        assertNotNull(capabilities, "No GetCapabilities response from SUT.");
        Element docElement = capabilities.getDocumentElement();
        assertEquals(docElement.getLocalName(), DGIWGWMTS.WMTS_CAPABILITIES,
                "Capabilities document element has unexpected [local name].");
        assertEquals(docElement.getNamespaceURI(), WmtsNamespaces.WMTS,
                "Capabilities document element has unexpected [namespace name].");
    }

}
