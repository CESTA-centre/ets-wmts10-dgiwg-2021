package org.opengis.cite.wmts10.core.util;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import de.latlon.ets.core.util.NamespaceBindings; //TODO : java class does not exist here
import org.opengis.cite.wmts10.core.domain.ProtocolBinding;
import org.opengis.cite.wmts10.core.domain.WmtsNamespaces;

/**
 * Provides various utility methods for accessing service metadata.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public final class ServiceMetadataUtils {

    private static final NamespaceBindings NS_BINDINGS = WmtsNamespaces.withStandardBindings();
    private ServiceMetadataUtils() {
    }
    
    /**
     * Determines which protocol bindings are supported for a given operation.
     * 
     * @param wmtsMetadata
     *            the capabilities document (wmts:Capabilities), never <code>null</code>
     * @param opName
     *            the name of the WMTS operation
     * @return A Set of protocol bindings supported for the operation. May be empty but never <code>null</code>.
     */
    public static Set<ProtocolBinding> getOperationBindings( final Document wmtsMetadata, String opName ) {
        Set<ProtocolBinding> protoBindings = new HashSet<>();
        if ( isOperationBindingSupported( wmtsMetadata, opName, ProtocolBinding.GET ) )
            protoBindings.add( ProtocolBinding.GET );
        if ( isOperationBindingSupported( wmtsMetadata, opName, ProtocolBinding.POST ) )
            protoBindings.add( ProtocolBinding.POST );

        return protoBindings;
    }

    private static boolean isOperationBindingSupported( Document wmtsMetadata, String opName, ProtocolBinding binding ) {

        String exprTemplate = "count(/wmts:Capabilities/ows:OperationsMetadata/ows:Operation[@name='%s']/ows:DCP/ows:HTTP/ows:%s)";
        String xPathExpr = String.format( exprTemplate, opName, binding.getElementName() );
        try {
            XPath xPath = createXPath();
            Double bindings = (Double) xPath.evaluate( xPathExpr, wmtsMetadata, XPathConstants.NUMBER );
            if ( bindings > 0 ) {
                return true;
            }
        } catch ( XPathExpressionException xpe ) {
            throw new RuntimeException( "Error evaluating XPath expression against capabilities doc. ", xpe );
        }
        return false;
    }
    
    private static XPath createXPath() {
        XPath xPath = XPathFactory.newInstance().newXPath();
        xPath.setNamespaceContext( NS_BINDINGS );
        return xPath;
    }
    
    
}