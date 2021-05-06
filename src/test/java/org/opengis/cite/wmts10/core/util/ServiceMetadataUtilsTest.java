package org.opengis.cite.wmts10.core.util;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.opengis.cite.wmts10.core.domain.ProtocolBinding;

/**
 * @author <a href="mailto:"></a>
 */
//Official resources found on : http://schemas.opengis.net/wmts/1.0

public class ServiceMetadataUtilsTest {

	@Test
	public void testGetOperationBindings() throws Exception {
		Set<ProtocolBinding> globalBindings = ServiceMetadataUtils.getOperationBindings(wmtsCapabilities(), "GetTile");

		assertThat(globalBindings.size(), is(1));
		assertThat(globalBindings, hasItems(ProtocolBinding.GET));
	}

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
