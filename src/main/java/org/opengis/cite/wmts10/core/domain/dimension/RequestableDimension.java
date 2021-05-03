package org.opengis.cite.wmts10.core.domain.dimension;

//TODO: check if usefull (GetMap is for WMS13)

/**
 * The parsed text value of a Dimension element, prepared to retrieve a value for a GetMap request.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public interface RequestableDimension {

    /**
     * @return a string representation of a requestable value, never <code>null</code>
     */
    String retrieveRequestableValue();

}
