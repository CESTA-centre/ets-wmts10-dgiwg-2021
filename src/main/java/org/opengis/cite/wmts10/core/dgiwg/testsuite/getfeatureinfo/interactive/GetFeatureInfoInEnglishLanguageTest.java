package org.opengis.cite.wmts10.core.dgiwg.testsuite.getfeatureinfo.interactive;

import static org.testng.Assert.assertTrue;


import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.opengis.cite.wmts10.core.domain.InteractiveTestResult;
import org.opengis.cite.wmts10.core.domain.SuiteAttribute;


import static org.opengis.cite.wmts10.core.util.interactive.InteractiveTestUtils.retrieveGetFeatureInfoRequest;


/**
 * Checks the result of the interactive test for the language of the metadata
 * content.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class GetFeatureInfoInEnglishLanguageTest {
	/**
	 * DGIWG WMTS 1.0, Requirement 12 A WMTS server shall provide the service metadata document (GetCapabilities response 
	 * or ServiceMetadata resource document) and the featureInfo document (if supported, GetFeatureInfo response or FeatureInfo 
	 * resource document) in the English language.  Metadata content may also be provided in additional languages, 
	 * but English must always be included.
	 */
	 @Test(groups= {"A WMTS server shall provide the service metadata document (GetCapabilities response or ServiceMetadata resource document) and the featureInfo document (if supported, GetFeatureInfo response or FeatureInfo resource document) in the English language.  Metadata content may also be provided in additional languages, but English must always be included."}, description = "Asks the user if the presented request returns a response that contains English language.")
	public void getFeatureInfoInEnglishLanguage(ITestContext context)
			throws XPathExpressionException, XPathFactoryConfigurationException {

		 
		if (context == null)
			throw new SkipException("Context is null!");
		Object attribute = context.getSuite().getAttribute(SuiteAttribute.INTERACTIVE_TEST_RESULT.getName());
		if (attribute == null)
			throw new SkipException("Missing testresult!");

		InteractiveTestResult interactiveTestResult = (InteractiveTestResult) attribute;
		boolean getFeatureInfoResponseInEnglishLanguage = interactiveTestResult
				.isGetFeatureInfoResponseInEnglishLanguage();
		assertTrue(getFeatureInfoResponseInEnglishLanguage,
				"Content of the GetFeatureInfo response is not in English language.");
	}

}