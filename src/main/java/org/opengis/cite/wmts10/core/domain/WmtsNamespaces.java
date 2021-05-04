package org.opengis.cite.wmts10.core.domain;

import javax.xml.XMLConstants;

//From ets-dgiwg-core dependency
import de.latlon.ets.core.util.NamespaceBindings;

/**
 * XML namespace names.
 * 
 * @see <a href="http://www.w3.org/TR/xml-names/">Namespaces in XML 1.0</a>
 * 
 * @author <a href="mailto:"></a>
 */
public final class WmtsNamespaces {

    private WmtsNamespaces() {
    }

    /** OGC WMTS 1.0 */
    public static final String WMTS = "http://schemas.opengis.net/wmts/1.0/wmtsGetCapabilities_response.xsd";

    /** W3C XLink */
    public static final String XLINK = "http://www.w3.org/1999/xlink";

    /** GML */
    public static final String GML = "http://www.opengis.net/gml";

    /** ExtendedCapabilities Namespace used for SOAP binding */
    public static final String SOAPWMTS = "http://schemas.opengis.net/wmts/1.0";

    /**
     * Creates a NamespaceBindings object that declares the following namespace bindings:
     * 
     * <ul>
     * <li>wmts: {@value org.opengis.cite.wmts10.core.domain.WmtsNamespaces#WMTS}</li>
     * <li>xlink: {@value org.opengis.cite.wmts10.core.domain.WmtsNamespaces#XLINK}</li>
     * </ul>
     * 
     * @return A NamespaceBindings object.
     */
    public static NamespaceBindings withStandardBindings() {
        NamespaceBindings nsBindings = new NamespaceBindings();
        nsBindings.addNamespaceBinding( WmtsNamespaces.WMTS, "wmts" );
        nsBindings.addNamespaceBinding( WmtsNamespaces.XLINK, "xlink" );
        nsBindings.addNamespaceBinding( XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "xsi" );
        nsBindings.addNamespaceBinding( WmtsNamespaces.GML, "gml" );
        nsBindings.addNamespaceBinding( WmtsNamespaces.SOAPWMTS, "soapwmts" );
        return nsBindings;
    }
    
}
