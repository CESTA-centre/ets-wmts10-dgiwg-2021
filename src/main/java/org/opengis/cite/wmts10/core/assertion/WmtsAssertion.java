package org.opengis.cite.wmts10.core.assertion;

//From ets-dgiwg-core dependency
import static de.latlon.ets.core.assertion.ETSAssert.assertQualifiedName;
import static de.latlon.ets.core.assertion.ETSAssert.assertXPath;

import javax.xml.namespace.QName;

import org.w3c.dom.Document;

import org.opengis.cite.wmts10.core.domain.DGIWGWMTS;
import org.opengis.cite.wmts10.core.domain.WmtsNamespaces;

/**
 * Provides WMTS 1.0.0 specific test assertion methods
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public final class WmtsAssertion {

    private WmtsAssertion() {
    }

    /**
     * Asserts that the given DOM document has the expected root element 'WMTS_Capabilities' in namespace
     * {http://www.opengis.net/wmts}.
     * 
     * @param doc
     *            A Document node having {http://www.opengis.net/wmts} {@value DGIWGWMTS#WMTS_CAPABILITIES} as the root
     *            element.
     */
    public static void assertSimpleWMTSCapabilities( Document doc ) {
        assertQualifiedName( doc.getDocumentElement(), new QName( WmtsNamespaces.WMTS, DGIWGWMTS.WMTS_CAPABILITIES ) );
    }

    /**
     * Asserts that the actual content type matches the expected content type.
     *
     * @param response
     *            A Document node having {http://www.opengis.net/wmts} {@value DGIWGWMTS#WMTS_CAPABILITIES} as the root
     *            element.
     */
    public static void assertVersion100( Document response ) {
        assertXPath( "//wmts:Capabilities/@version = '1.0.0'", response, WmtsNamespaces.withStandardBindings() );
    }

}
