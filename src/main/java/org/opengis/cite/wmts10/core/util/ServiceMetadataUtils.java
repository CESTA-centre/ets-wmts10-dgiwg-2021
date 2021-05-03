package org.opengis.cite.wmts10.core.util;

import java.net.URI;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.DatatypeConverter;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.ISOPeriodFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.latlon.ets.core.util.NamespaceBindings; //TODO : java class does not exist here
import de.latlon.ets.core.util.TestSuiteLogger; //TODO : java class does not exist here
import org.opengis.cite.wmts10.core.domain.BoundingBox;
import org.opengis.cite.wmts10.core.domain.Dimension;
import org.opengis.cite.wmts10.core.domain.LayerInfo;
import org.opengis.cite.wmts10.core.domain.ProtocolBinding;
import org.opengis.cite.wmts10.core.domain.WmtsNamespaces;
import org.opengis.cite.wmts10.core.domain.dimension.DimensionUnitValue;
import org.opengis.cite.wmts10.core.domain.dimension.RequestableDimension;
import org.opengis.cite.wmts10.core.domain.dimension.RequestableDimensionList;
import org.opengis.cite.wmts10.core.domain.dimension.date.DateTimeDimensionInterval;
import org.opengis.cite.wmts10.core.domain.dimension.date.DateTimeRequestableDimension;
import org.opengis.cite.wmts10.core.domain.dimension.number.NumberDimensionInterval;
import org.opengis.cite.wmts10.core.domain.dimension.number.NumberRequestableDimension;

/**
 * Provides various utility methods for accessing service metadata.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public final class ServiceMetadataUtils {

    private static final Logger LOGR = Logger.getLogger( ServiceMetadataUtils.class.getName() );

    private static final NamespaceBindings NS_BINDINGS = WmtsNamespaces.withStandardBindings();

    private ServiceMetadataUtils() {
    }

    /**
     * Extracts a request endpoint from a WMTS capabilities document. If the request URI contains a query component it is
     * removed (but not from the source document).
     *
     * @param wmtsMetadata
     *            the document node containing service metadata (OGC capabilities document).
     * @param opName
     *            the operation (request) name
     * @param binding
     *            the message binding to use (if {@code null} any supported binding will be used)
     * @return the URI referring to a request endpoint, <code>null</code> if no matching endpoint is found
     */
    public static URI getOperationEndpoint( final Document wmtsMetadata, String opName, ProtocolBinding binding ) {
        if ( null == binding || binding.equals( ProtocolBinding.ANY ) ) {
            binding = getOperationBindings( wmtsMetadata, opName ).iterator().next();
        }
        if ( binding == null )
            return null;

        String expr = "//wmts:Request/wmts:%s/wmts:DCPType/wmts:HTTP/wmts:%s/wmts:OnlineResource/@xlink:href";
        String xPathExpr = String.format( expr, opName, binding.getElementName() );

        String href = null;
        try {
            XPath xPath = createXPath();
            href = xPath.evaluate( xPathExpr, wmtsMetadata );
        } catch ( XPathExpressionException ex ) {
            TestSuiteLogger.log( Level.INFO, ex.getMessage() );
        }

        return createEndpoint( href );
    }

    /**
     * Determines which protocol bindings are supported for a given operation.
     *
     * @param wmtsMetadata
     *            the capabilities document (wmts:WMTS_Capabilities), never <code>null</code>
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

    /**
     * Parses the configured formats for the given operation.
     *
     * @param wmtsCapabilities
     *            the capabilities document (wmts:WMTS_Capabilities), never <code>null</code>
     * @param opName
     *            the name of the WMTS operation
     * @return a list of the supported formats by the operation, never <code>null</code>
     */
    public static List<String> parseSupportedFormats( Document wmtsCapabilities, String opName ) {
        ArrayList<String> supportedFormats = new ArrayList<>();

        String expr = "//wmts:WMTS_Capabilities/wmts:Capability/wmts:Request/wmts:%s/wmts:Format";
        String xPathExpr = String.format( expr, opName );

        try {
            XPath xPath = createXPath();
            NodeList formatNodes = (NodeList) xPath.evaluate( xPathExpr, wmtsCapabilities, XPathConstants.NODESET );
            for ( int formatNodeIndex = 0; formatNodeIndex < formatNodes.getLength(); formatNodeIndex++ ) {
                Node formatNode = formatNodes.item( formatNodeIndex );
                String format = formatNode.getTextContent();
                if ( format != null && !format.isEmpty() )
                    supportedFormats.add( format );
            }
        } catch ( XPathExpressionException ex ) {
            TestSuiteLogger.log( Level.INFO, ex.getMessage() );
        }

        return supportedFormats;
    }

    /**
     * Parses all named layers from the capabilities document.
     *
     * @param wmtsCapabilities
     *            the capabilities document (wmts:WMTS_Capabilities), never <code>null</code>
     * @return a list of {@link LayerInfo}s supported by the WMTS, never <code>null</code>
     */
    public static List<LayerInfo> parseLayerInfo( Document wmtsCapabilities ) {
        ArrayList<LayerInfo> layerInfos = new ArrayList<>();
        try {
            String layerExpr = "//wmts:Layer[wmts:Name/text() != '']";

            XPath xPath = createXPath();
            NodeList layerNodes = (NodeList) xPath.evaluate( layerExpr, wmtsCapabilities, XPathConstants.NODESET );

            for ( int layerNodeIndex = 0; layerNodeIndex < layerNodes.getLength(); layerNodeIndex++ ) {
                Node layerNode = layerNodes.item( layerNodeIndex );
                LayerInfo layerInfo = parseLayerInfo( xPath, layerNode );
                layerInfos.add( layerInfo );
            }

        } catch ( XPathExpressionException xpe ) {
            throw new RuntimeException( "Error evaluating XPath expression against capabilities doc. ", xpe );
        } catch ( ParseException e ) {
            throw new RuntimeException( "Error parsing layer infos from doc. ", e );
        }
        return layerInfos;
    }

    /**
     * Parses the updateSequence value from the capabilities document.
     *
     * @param wmtsCapabilities
     *            the capabilities document (wmts:WMTS_Capabilities), never <code>null</code>
     * @return the value of the {@link ServiceMetadataUtils} attribute, <code>null</code> if the attribute is missing or
     *         the value empty
     */
    public static String parseUpdateSequence( Document wmtsCapabilities ) {
        try {
            String layerExpr = "//wmts:WMTS_Capabilities/@updateSequence ";
            XPath xPath = createXPath();
            String updateSequence = (String) xPath.evaluate( layerExpr, wmtsCapabilities, XPathConstants.STRING );
            return updateSequence == null || updateSequence.isEmpty() ? null : updateSequence;
        } catch ( XPathExpressionException xpe ) {
            throw new RuntimeException( "Error evaluating XPath expression against capabilities doc. ", xpe );
        }
    }

    /**
     * Parses the EX_GeographicBoundingBox from the layer.
     *
     * @param layerNode
     *            node of the layer, never <code>null</code>
     * @return the {@link BoundingBox} - crs is CRS:84, <code>null</code> if missing
     */
    public static BoundingBox parseGeographicBoundingBox( Node layerNode ) {
        XPath xPath = createXPath();
        String bboxesExpr = "ancestor-or-self::wmts:Layer/wmts:EX_GeographicBoundingBox";
        try {
            NodeList bboxNodes = (NodeList) xPath.evaluate( bboxesExpr, layerNode, XPathConstants.NODESET );
            Node bboxNode = bboxNodes.item( bboxNodes.getLength() - 1 );
            double minX = asDouble( bboxNode, "wmts:westBoundLongitude", xPath );
            double minY = asDouble( bboxNode, "wmts:southBoundLatitude", xPath );
            double maxX = asDouble( bboxNode, "wmts:eastBoundLongitude", xPath );
            double maxY = asDouble( bboxNode, "wmts:northBoundLatitude", xPath );
            return new BoundingBox( "CRS:84", minX, minY, maxX, maxY );
        } catch ( XPathExpressionException xpe ) {
            throw new RuntimeException(
                                        "Error evaluating XPath expression against capabilities doc while parsing geographic BBOX of layer. ",
                                        xpe );
        }
    }

    /**
     * Parses all Layer elements of a capabilities document.
     *
     * @param wmtsCapabilities
     *            capabilities document, never <code>null</code>
     * @return node list containing all layer elements of the capabilities document
     * @throws XPathExpressionException
     *             if the <code>expression</code> cannot be evaluated
     */
    public static NodeList parseAllLayerNodes( Document wmtsCapabilities )
                            throws XPathExpressionException {
        String xPathAbstract = "//wmts:Layer";
        return createNodeList( wmtsCapabilities, xPathAbstract );
    }

    /**
     * Parses all requestable Layer elements of a capabilities document. Requestable layers are identified by the
     * existence of a Name sub element.
     *
     * @param wmtsCapabilities
     *            capabilities document, never <code>null</code>
     * @return node list containing all requestable layer elements of the capabilities document
     * @throws XPathExpressionException
     *             if the <code>expression</code> cannot be evaluated
     */
    public static NodeList parseRequestableLayerNodes( Document wmtsCapabilities )
                            throws XPathExpressionException {
        String xPathAbstract = "//wmts:Layer[wmts:Name]";
        return createNodeList( wmtsCapabilities, xPathAbstract );
    }

    static RequestableDimension parseRequestableDimension( String units, String value )
                            throws ParseException {
        if ( value.contains( "," ) )
            return parseListRequestableDimension( units, value );
        return parseSingleRequestableDimension( units, value );
    }

    private static NodeList createNodeList( Document wmtsCapabilities, String xPathAbstract )
                            throws XPathExpressionException {
        XPath xpath = createXPath();
        return (NodeList) xpath.evaluate( xPathAbstract, wmtsCapabilities, XPathConstants.NODESET );
    }

    private static RequestableDimension parseListRequestableDimension( String units, String value )
                            throws ParseException {
        List<RequestableDimension> requestableDimensions = new ArrayList<>();
        String[] singleValues = value.split( "," );
        for ( String singleValue : singleValues ) {
            requestableDimensions.add( parseSingleRequestableDimension( units, singleValue ) );
        }
        return new RequestableDimensionList( requestableDimensions );
    }

    private static RequestableDimension parseSingleRequestableDimension( String units, String singleValue )
                            throws ParseException {
        if ( singleValue.contains( "/" ) )
            return parseInterval( units, singleValue );
        return parseSingleValue( units, singleValue );
    }

    private static RequestableDimension parseInterval( String units, String token )
                            throws ParseException {
        LOGR.fine( String.format( "Parsing temporal interval with units %s: %s", units, token ) );
        String[] minMaxRes = token.split( "/" );
        if ( "ISO8601".equals( units ) ) {
            DateTime min = parseDateTime( minMaxRes[0] );
            DateTime max = parseDateTime( minMaxRes[1] );
            String period = ( minMaxRes.length > 2 ) ? minMaxRes[2] : "";
            Period resolution = parseResolution( period );
            return new DateTimeDimensionInterval( min, max, resolution );
        }
        Number min = parseNumber( minMaxRes[0] );
        Number max = parseNumber( minMaxRes[1] );
        Number resolution = parseNumber( minMaxRes[2] );
        return new NumberDimensionInterval( min, max, resolution );
    }

    private static Period parseResolution( String resolution ) {
        if ( "0".equals( resolution ) || resolution.isEmpty() )
            return null;
        return ISOPeriodFormat.standard().parsePeriod( resolution );
    }

    private static RequestableDimension parseSingleValue( String units, String value )
                            throws ParseException {
        if ( "ISO8601".equals( units ) ) {
            DateTime dateTime = parseDateTime( value );
            return new DateTimeRequestableDimension( dateTime );
        }
        Number number = parseNumber( value );
        return new NumberRequestableDimension( number );
    }

    private static Number parseNumber( String token )
                            throws ParseException {
        NumberFormat instance = NumberFormat.getInstance( Locale.ENGLISH );
        instance.setParseIntegerOnly( false );
        return instance.parse( token );
    }

    private static DateTime parseDateTime( String token ) {
        Calendar dateTime = DatatypeConverter.parseDateTime( token );
        return new DateTime( dateTime.getTimeInMillis() );
    }

    private static LayerInfo parseLayerInfo( XPath xPath, Node layerNode )
                            throws XPathExpressionException, ParseException {
        String layerName = (String) xPath.evaluate( "wmts:Name", layerNode, XPathConstants.STRING );
        boolean isQueryable = parseQueryable( xPath, layerNode );
        List<BoundingBox> bboxes = parseBoundingBoxes( xPath, layerNode );
        List<Dimension> dimensions = parseDimensions( xPath, layerNode );
        BoundingBox geographicBbox = parseGeographicBoundingBox( layerNode );
        return new LayerInfo( layerName, isQueryable, bboxes, dimensions, geographicBbox );
    }

    private static boolean parseQueryable( XPath xPath, Node layerNode )
                            throws XPathExpressionException {
        String queryableAttribute = (String) xPath.evaluate( "@queryable", layerNode, XPathConstants.STRING );
        return queryableAttribute != null
               && ( "1".equals( queryableAttribute ) ? true : false || Boolean.parseBoolean( queryableAttribute ) );
    }

    private static List<BoundingBox> parseBoundingBoxes( XPath xPath, Node layerNode )
                            throws XPathExpressionException {
        Map<String, BoundingBox> bboxes = new HashMap<>();
        String bboxesExpr = "ancestor-or-self::wmts:Layer/wmts:BoundingBox";
        NodeList bboxNodes = (NodeList) xPath.evaluate( bboxesExpr, layerNode, XPathConstants.NODESET );
        for ( int bboxNodeIndex = 0; bboxNodeIndex < bboxNodes.getLength(); bboxNodeIndex++ ) {
            Node bboxNode = bboxNodes.item( bboxNodeIndex );
            BoundingBox bbox = parseBoundingBox( bboxNode );
            bboxes.put( bbox.getCrs(), bbox );
        }
        return new ArrayList<>( bboxes.values() );
    }

    private static List<Dimension> parseDimensions( XPath xPath, Node layerNode )
                            throws XPathExpressionException, ParseException {
        ArrayList<Dimension> dimensions = new ArrayList<>();
        String dimensionExpr = "wmts:Dimension";
        NodeList dimensionNodes = (NodeList) xPath.evaluate( dimensionExpr, layerNode, XPathConstants.NODESET );
        for ( int dimensionNodeIndex = 0; dimensionNodeIndex < dimensionNodes.getLength(); dimensionNodeIndex++ ) {
            Node dimensionNode = dimensionNodes.item( dimensionNodeIndex );
            Dimension dimension = parseDimension( xPath, dimensionNode );
            if ( dimension != null )
                dimensions.add( dimension );
        }
        return dimensions;
    }

    private static BoundingBox parseBoundingBox( Node bboxNode )
                            throws XPathExpressionException {
        XPath xPath = createXPath();
        double minx = asDouble( bboxNode, "@minx", xPath );
        double miny = asDouble( bboxNode, "@miny", xPath );
        double maxx = asDouble( bboxNode, "@maxx", xPath );
        double maxy = asDouble( bboxNode, "@maxy", xPath );
        String crs = (String) xPath.evaluate( "@CRS", bboxNode, XPathConstants.STRING );
        return new BoundingBox( crs, minx, miny, maxx, maxy );
    }

    private static Dimension parseDimension( XPath xPath, Node dimensionNode )
                            throws XPathExpressionException, ParseException {
        String name = (String) xPath.evaluate( "@name", dimensionNode, XPathConstants.STRING );
        if ( name != null ) {
            String units = (String) xPath.evaluate( "@units", dimensionNode, XPathConstants.STRING );
            String value = (String) xPath.evaluate( "text()", dimensionNode, XPathConstants.STRING );
            RequestableDimension requestableDimension = parseRequestableDimension( units, value );
            DimensionUnitValue unitValue = new DimensionUnitValue( units, requestableDimension );
            return new Dimension( name, unitValue );
        }
        return null;
    }

    private static double asDouble( Node node, String xPathExpr, XPath xPath )
                            throws XPathExpressionException {
        String content = (String) xPath.evaluate( xPathExpr, node, XPathConstants.STRING );
        return Double.parseDouble( content );
    }

    private static boolean isOperationBindingSupported( Document wmtsMetadata, String opName, ProtocolBinding binding ) {
        String exprTemplate = "count(/wmts:WMTS_Capabilities/wmts:Capability/wmts:Request/wmts:%s/wmts:DCPType/wmts:HTTP/wmts:%s)";
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

    private static URI createEndpoint( String href ) {
        if ( href == null || href.isEmpty() )
            return null;
        URI endpoint = URI.create( href );
        if ( null != endpoint.getQuery() ) {
            String uri = endpoint.toString();
            endpoint = URI.create( uri.substring( 0, uri.indexOf( '?' ) ) );
        }
        return endpoint;
    }

    private static XPath createXPath() {
        XPath xPath = XPathFactory.newInstance().newXPath();
        xPath.setNamespaceContext( NS_BINDINGS );
        return xPath;
    }

    public static URI getSoapOperationEndpoint( Document wmtsCapabilities, String operation ) {
        if ( isSoapSupported( wmtsCapabilities, operation ) ) {
            String href = parseSoapEndpoint( wmtsCapabilities );
            return createEndpoint( href );
        }
        return null;
    }

    private static String parseSoapEndpoint( Document wmtsCapabilities ) {
        final String xPathExpr = "//wmts:WMTS_Capabilities/wmts:Capability/soapwmts:ExtendedCapabilities/soapwmts:SOAP/wmts:OnlineResource/@xlink:href";
        String href = null;
        try {
            XPath xPath = createXPath();
            href = xPath.evaluate( xPathExpr, wmtsCapabilities );
        } catch ( XPathExpressionException ex ) {
            TestSuiteLogger.log( Level.INFO, ex.getMessage() );
        }
        return href;
    }

    private static boolean isSoapSupported( Document wmtsCapabilities, String operation ) {
        final String exprTemplate = "//wmts:WMTS_Capabilities/wmts:Capability/soapwmts:ExtendedCapabilities/soapwmts:SOAP/soapwmts:SupportedOperations/soapwmts:Operation[@name='%s']";
        String xPathExpr = String.format( exprTemplate, operation );
        XPath xPath = createXPath();
        try {
            return (Boolean) xPath.evaluate( xPathExpr, wmtsCapabilities, XPathConstants.BOOLEAN );
        } catch ( XPathExpressionException xpe ) {
            throw new RuntimeException( "Error evaluating XPath expression against capabilities doc. ", xpe );
        }
    }

}
