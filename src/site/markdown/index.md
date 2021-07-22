# DGIWG – Web Map Tile Service 1.0 Profile Conformance Test Suite
# STANAG 6523 Ed.1 revision WMS Harmonized profile

## Scope

This executable test suite (ETS) verifies the conformance of the implementation under 
test (IUT) with respect to DGIWG – Web Map Service 1.0 Profile STANAG 6523 Ed.1 revision, [DGIWG-124](https://portal.dgiwg.org/files/68271).
Conformance testing is a kind of "black box" testing that examines the externally 
visible characteristics or behaviors of the IUT while disregarding any implementation details.


## What is tested

  - All requirements described in harmonized "DGIWG – Web Map Tile Service 1.0 Profile", STANAG 6523 Ed.1 revision.


## What is not tested
  - WMTS 1.0 Standard. As the DGIWG profile complements the WMTS standard, the [WMTS 1.0](https://portal.ogc.org/files/?artifact_id=35326) test suite must first be run to check the conformity of the service (can be found on [GitHub](https://github.com/opengeospatial/ets-wmts10)).
  - Requirements 17 and 18 (NCIA SIP for Map Rendering Services).


## Test requirements

The documents listed below stipulate requirements that must be satisfied by a 
conforming implementation.

1. Harmonized [DGIWG – Web Map Tile Service 1.0 Profile (STD-DP-18-001)](https://portal.dgiwg.org/files/68271)
2. [Web Map Tile Server Implementation Specification, Version 1.0.0 (07-057r7 )](https://portal.ogc.org/files/?artifact_id=35326)

If any of the following preconditions are not satisfied then all tests in the 
suite will be marked as skipped.

1. WMTS capabilities document must be available.


## Test suite structure

The test suite definition file (testng.xml) is located in the root package, 
`org.opengis.cite.wmts10.dgiwg`. A group corresponds to a &lt;test&gt; element, each 
of which includes a set of test classes that contain the actual test methods. 
The general structure of the test suite is shown in Table 1.

<table>
  <caption>Table 1 - Test suite structure</caption>
  <thead>
    <tr style="text-align: left; background-color: LightCyan">
      <th>Group</th>
      <th>Test classes</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Preconditions</td>
      <td>org.opengis.cite.wmts10.core.dgiwg.testsuite.Prerequisites</td>
    </tr>
    <tr>
      <td>DGIWG - Web Map Tile Service 1.0 Harmonized Profile, Requirement 2</td>
      	<td>
	        DGWIG WMTS requirement 2 A WMTS Server shall support HTTP GET operation 
			using KVP (clause 8 of OGC WMS) and RESTful (clause 10 of OGC WMTS 1.0) encodings.
	        <ul>
	          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesOperations</li>
	          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesRest</li>
	          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.gettile.GetTileParametersKvp</li>
	          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.gettile.GetTileParametersRest</li>
	          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.getfeatureinfo.GetFeatureInfoKvp</li>
	          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.getfeatureinfo.GetFeatureInfoRest</li>
	        </ul>
		</td>
    </tr>
    <tr>
      <td>DGIWG - Web Map Tile Service 1.0 Harmonized Profile, Requirement 3</td>
      <td>
        A WMTS server shall support at least on the folowwing 
		WKKS : see DGIWG ANNEX B.1 (EPSG:3395), B.2 (EPSG:4326 and CRS 84) and B.3 
		(UPS Tiles EPSG:5041 and EPSG:5042).
        <ul>
          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesCrsTest</li>
          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesProjectionTest</li>
          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesWellKnownScaleTest</li>
        </ul>
      </td>
    </tr>
    <tr>
      <td>DGIWG - Web Map Tile Service 1.0 Harmonized Profile, Requirement 4</td>
      <td>
        DGWIG requirement 4 A WMTS server shall provide tiles in at least one 
		of the following raster formats :
			<br>1. image/png (Portable Network Graphics)</br>
			<br>2. image/gif (Graphics Interchange Format)</br>
			<br>3. image/jpeg (Joint Photographics Expert Group).</br>				
        <ul>
          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.gettile.GetTileOfferings</li>      
        </ul>
      </td>
    </tr>
    <tr>
      <td>DGIWG - Web Map Tile Service 1.0 Harmonized Profile, Requirement 5</td>
      <td>
        If a WMTS server is providing services to a coalition mission federated 
		network, in support of operations or an exercise, it shall provide a minimum 
		keyword list, based on ISO 19115 Topic Categories. It's recommended to provide 
		additional details based on the DGIF groups. The provision of these keywords 
		elements are optional for a WMTS server which is providing services across 
		one single non-mission network.
        <ul>
          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesKeywordTest</li>
        </ul>
      </td>
    </tr>
    <tr>
      <td>DGIWG - Web Map Tile Service 1.0 Harmonized Profile, Requirement 6</td>
      <td>
        A WMTS server shall provide the service exceptions in the English language. 
		Exception text content may also be provided in additional languages, but 
		English must always be included.
        <ul>
          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.getfeatureinfo.interactive.GetFeatureInfoExceptionInEnglishLanguageTest</li>
          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.interactive.GetCapabilitiesExceptionInEnglishLanguageTest</li>
          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.gettile.interactive.GetTileExceptionInEnglishLanguageTest</li>
        </ul>
      </td>
    </tr>
    <tr>
      <td>DGIWG - Web Map Tile Service 1.0 Harmonized Profile, Requirement 7</td>
      <td>
        A WMTS server shall provide an "Abstract" at the service level, in 
		the in the GetCapabilities response document.
        <ul>
          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesAbstractTest</li>
        </ul>
      </td>
    </tr>
    <tr>
      <td>DGIWG - Web Map Tile Service 1.0 Harmonized Profile, Requirement 8</td>
      <td>
        If a WMS server is providing services to a coalition mission federated 
		network, in support of operations or an exercise, it shall provide "ServiceContact", 
		"AccessConstraints" and "KeywordList" elements. The provision of these metadata 
		elements are optional for a WMTS server which is providing services across 
		one single non-mission network.
        <ul>
          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesAccessConstraintTest</li>
          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesContentTest</li>
        </ul>
      </td>
    </tr>
    <tr>
      <td>DGIWG - Web Map Tile Service 1.0 Harmonized Profile, Requirement 9</td>
      <td>
        A WMTS server SHALL use the "AccessContraints" element to hold the 
		classification information for this web service instance.
        <ul>
          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesAccessConstraintTest9</li>
        </ul>
      </td>
    </tr>
    <tr>
      <td>DGIWG - Web Map Tile Service 1.0 Harmonized Profile, Requirement 10</td>
      <td>
        If a WMTS server is providing services to a coalition mission federated 
		network, in support of operations or an exercise, it shall include the following 
		information in the "abstract" element of the service metadata: "This service 
		implements the WMTS 1.0 STANAG 6523 Ed.2 profile". The provision of these 
		metadata elements are optional for a WMTS server which is providing services 
		across one single non-mission network.
        <ul>
          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesAbstractTest10</li>
        </ul>
      </td>
    </tr>
    <tr>
      <td>DGIWG - Web Map Tile Service 1.0 Harmonized Profile, Requirement 11</td>
      <td>
        A RESTFULL WMTS server shall provide tile experiation date, in an appropriate 
		HTTP header ("Expires" header in HTTP 1.0, "Cache-control" header for HTTP 
		1.1 and for HTTP 2).
        <ul>
          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.gettile.GetTileCachingInfo</li>
        </ul>
      </td>
    </tr>
    <tr>
      <td>DGIWG - Web Map Tile Service 1.0 Harmonized Profile, Requirement 12</td>
      <td>
        A WMTS server shall provide the service metadata document (GetCapabilities response or ServiceMetadata resource document) and the featureInfo document (if supported, GetFeatureInfo response or FeatureInfo resource document) in the English language.  Metadata content may also be provided in additional languages, but English must always be included.
        <ul>
          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.getfeatureinfo.interactive.GetFeatureInfoInEnglishLanguageTest</li>
          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.interactive.GetCapabilitiesInEnglishLanguageTest</li>
        </ul>
      </td>
    </tr>
    <tr>
      <td>DGIWG - Web Map Tile Service 1.0 Harmonized Profile, Requirement 13</td>
      <td>
        A WMTS server shall provide a "title" element for each supported style
        <ul>
          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.ServiceMetadataContent13</li>
        </ul>
      </td>
    </tr>
    <tr>
      <td>DGIWG - Web Map Tile Service 1.0 Harmonized Profile, Requirement 14</td>
      <td>
        Each layer's style shall have an associated legend (using the "legendURL" element) if the data being provisioned is symbolized/portrayed (i.e. not imagery).
        <ul>
          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.ServiceMetadataContent</li>
        </ul>
      </td>
    </tr>
    <tr>
      <td>DGIWG - Web Map Tile Service 1.0 Harmonized Profile, Requirement 15</td>
      <td>
        Legends shall be available as an image in at least one of the following 
		formats: PNG (image/png), GIF (image/gif) or JPEG (image/jpeg).
        <ul>
          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.ServiceMetadataContent15</li>
        </ul>
      </td>
    </tr>
    <tr>
      <td>DGIWG - Web Map Tile Service 1.0 Harmonized Profile, Requirement 16</td>
      <td>
        If legend is present (see req 14), the "LegendURL" element shall specify 
		a URL to allow access to an image of the legend. Note : This URL will relate 
		to the source system and may not be resolvable on all connected/unconnected 
		systems or applications. This requirement is conditional on the "LegendURL" 
		being relevant to the generated service.
        <ul>
          <li>org.opengis.cite.wmts10.core.dgiwg.testsuite.getcapabilities.ServiceMetadataContent16</li>
        </ul>
      </td>
    </tr>
    <tr>
      <td>DGIWG - Web Map Tile Service 1.0 Harmonized Profile, Requirement 17</td>
      <td>
        If a WMTS server offers its functionality via the SOAP protocol, it 
		shall do so in compliance with the Messaging Service SIP [NCIA TR/2012/SPW008000/30, 
		2012] which defines general requirements that apply to all services in the 
		NNEC environment that make use of SOAP.
        <br> -> Not tested yet. </br>
      </td>
    </tr>
    <tr>
      <td>DGIWG - Web Map Tile Service 1.0 Harmonized Profile, Requirement 18</td>
      <td>
        If the WMTS server responds with an exception for a request containing duplicated parameters with conflicting values, it shall use the exception code “DuplicatedParameterInRequest”.
        <br> -> Not tested yet. </br>
      </td>
    </tr>
   </tbody>
</table>



The Javadoc documentation provides more detailed information about the test 
methods that constitute the suite.


## Test run arguments

The test run arguments are summarized in Table 2. The  _Obligation_ descriptor can 
have the following values: M (mandatory), O (optional), or C (conditional).

<table>
  <caption>Table 2 -Test run arguments</caption>
  <thead>
    <tr style="text-align: left; background-color: LightCyan">
      <th>Name</th>
      <th>Value domain</th>
      <th>Obligation</th>
  	  <th>Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>wmts</td>
      <td>URI</td>
      <td>M</td>
	  <td>A URI that refers to the implementation under test or metadata about it.
      Ampersand '&amp;' characters must be followed with 'amp;' expression.</td>
    </tr>
	<tr>
      <td>vector</td>
      <td>Boolean</td>
      <td>M</td>
      <td>Controls if tests targeting layers which base on vector data are executed.</td>
    </tr>
  </tbody>
</table>



##  License

[Apache License, Version 2.0](http://opensource.org/licenses/Apache-2.0 "Apache License")
