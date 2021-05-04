package org.opengis.cite.wmts10.core.util;

import static org.opengis.cite.wmts10.core.util.ServiceMetadataUtils.parseAllLayerNodes;
import static org.opengis.cite.wmts10.core.util.ServiceMetadataUtils.parseRequestableDimension;
import static org.opengis.cite.wmts10.core.util.ServiceMetadataUtils.parseRequestableLayerNodes;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.opengis.cite.wmts10.core.domain.BoundingBox;
import org.opengis.cite.wmts10.core.domain.Dimension;
import org.opengis.cite.wmts10.core.domain.LayerInfo;
import org.opengis.cite.wmts10.core.domain.WmtsNamespaces;
import org.opengis.cite.wmts10.core.domain.ProtocolBinding;
import org.opengis.cite.wmts10.core.domain.dimension.DimensionUnitValue;
import org.opengis.cite.wmts10.core.domain.dimension.RequestableDimension;
import org.opengis.cite.wmts10.core.domain.dimension.RequestableDimensionList;
import org.opengis.cite.wmts10.core.domain.dimension.date.DateTimeDimensionInterval;
import org.opengis.cite.wmts10.core.domain.dimension.date.DateTimeRequestableDimension;
import org.opengis.cite.wmts10.core.domain.dimension.number.NumberDimensionInterval;
import org.opengis.cite.wmts10.core.domain.dimension.number.NumberRequestableDimension;

/**
 * @author <a href="mailto:"></a>
 */
//Official resources found on : http://schemas.opengis.net/wmts/1.0
//TODO : se référer au ServiceMetadataUtils édité dans ets-wmts10-nsg

public class ServiceMetadataUtilsTest {
/*
	@Test
	public void testGetOperationBindings() throws Exception {
		Set<ProtocolBinding> globalBindings = ServiceMetadataUtils.getOperationBindings(wmtsCapabilities(), "GetTile");

		assertThat(globalBindings.size(), is(1));
		assertThat(globalBindings, hasItems(ProtocolBinding.GET));
	}

	@Test
	public void testParseLayerInfo() throws Exception {
		List<LayerInfo> layerInfos = ServiceMetadataUtils.parseLayerInfo(wmtsCapabilities());

		assertThat(layerInfos.size(), is(1));
	}
*/
	private Document wmtsCapabilities() throws SAXException, IOException, ParserConfigurationException {
		return capabilities("../wmtsGetCapabilities_OGCresponse.xml");
	}

	private Document capabilities(String resource) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputStream wmtsCapabilities = ServiceMetadataUtilsTest.class.getResourceAsStream(resource);
		return builder.parse(new InputSource(wmtsCapabilities));
	}

}
