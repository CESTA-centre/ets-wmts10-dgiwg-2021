package org.opengis.cite.wmts10.core.dgiwg.testsuite.gettile.interactive;

import static org.testng.Assert.assertTrue;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.opengis.cite.wmts10.core.domain.InteractiveTestResult;
import org.opengis.cite.wmts10.core.domain.SuiteAttribute;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.Test;

public class GetTileExceptionInEnglishLanguageTest {
	@Test(groups= {"A WMTS server shall provide the service exceptions in the English language.  Exception text content may also be provided in additional languages, but English must always be included."}, description = "Asks the user if the presented request returns an exception that contains English language.")
    public void getTileExceptionInEnglishLanguage( ITestContext context )
                    throws XPathExpressionException, XPathFactoryConfigurationException {
        if ( context == null )
            throw new SkipException( "Context is null!" );
        Object attribute = context.getSuite().getAttribute( SuiteAttribute.INTERACTIVE_TEST_RESULT.getName() );
        if ( attribute == null )
            throw new SkipException( "Missing testresult!" );

        InteractiveTestResult interactiveTestResult = (InteractiveTestResult) attribute;
        boolean getTileExceptionResponseInEnglishLanguage = interactiveTestResult.isGetTileExceptionInEnglishLanguage();
        assertTrue( getTileExceptionResponseInEnglishLanguage,
                    "Content of the GetTile exception does not include English language." );
    }
	
}
