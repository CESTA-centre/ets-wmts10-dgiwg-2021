package org.opengis.cite.wmts10.core.dgiwg.testsuite.getfeatureinfo.interactive;

import static org.testng.Assert.assertTrue;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.Test;

import org.opengis.cite.wmts10.core.domain.InteractiveTestResult;
import org.opengis.cite.wmts10.core.domain.SuiteAttribute;

/**
 * Checks the result of the interactive test for the language of the metadata
 * content.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class GetFeatureInfoExceptionInEnglishLanguageTest {
	/**
	 * DGIWG WMTS 1.0, Requirement 6 A WMTS server shall provide the service
	 * exceptions in the English language. Exception text content may also be
	 * provided in additional languages, but English must always be included.
	 * @param context
	 * 	The context of the suite.
	 * @throws XPathExpressionException
	 * @throws XPathFactoryConfigurationException
	 */
	@Test(description = "DGIWG WMTS 1.0, Requirement 6")
	public void getFeatureInfoExceptionInEnglishLanguage(ITestContext context)
			throws XPathExpressionException, XPathFactoryConfigurationException {
		if (context == null)
			throw new SkipException("Context is null!");
		Object attribute = context.getSuite().getAttribute(SuiteAttribute.INTERACTIVE_TEST_RESULT.getName());
		if (attribute == null)
			throw new SkipException("Missing testresult!");

		InteractiveTestResult interactiveTestResult = (InteractiveTestResult) attribute;
		boolean getFeatureInfoExceptopmResponseInEnglishLanguage = interactiveTestResult
				.isGetFeatureInfoExceptionInEnglishLanguage();
		assertTrue(getFeatureInfoExceptopmResponseInEnglishLanguage,
				"Content of the GetFeatureInfo exception is not in English language.");
	}

}