
# DGIWG – Web Map Service 1.3 Profile Conformance Test Suite

## Scope

This executable test suite (ETS) verifies the conformance of the implementation under 
test (IUT) with respect to DGIWG – Web Map Service 1.3 Profile, [DGIWG-112](https://portal.dgiwg.org/files/?artifact_id=11514).
Conformance testing is a kind of "black box" testing that examines the externally 
visible characteristics or behaviors of the IUT while disregarding any implementation details.


## What is tested

  - All requirements described in "DGIWG – Web Map Service 1.3 Profile".


## What is not tested

  - All recommendations described in "DGIWG – Web Map Service 1.3 Profile".


## Test requirements

The documents listed below stipulate requirements that must be satisfied by a 
conforming implementation.

1. [DGIWG – Web Map Service 1.3 Profile (09-102r3)](https://portal.opengeospatial.org/files/?artifact_id=66915)
2. [Web Map Server Implementation Specification, Version 1.3.0 (06-042)](http://portal.opengeospatial.org/files/?artifact_id=14416)

If any of the following preconditions are not satisfied then all tests in the 
suite will be marked as skipped.

1. WMS capabilities document must be available.


## Test suite structure

The test suite definition file (testng.xml) is located in the root package, 
`de.latlon.ets.wms13.dgiwg`. A group corresponds to a &lt;test&gt; element, each 
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
      <td>de.latlon.ets.wms13.core.dgiwg.testsuite.Prerequisites</td>
    </tr>
    <tr>
      <td>WMS Basic</td>
      <td>Not tested yet</td>
    </tr>
    <tr>
      <td>Queryable WMS</td>
      <td>de.latlon.ets.wms13.core.dgiwg.testsuite.QueryableWMS</td>
    </tr>
    <tr>
      <td>DGIWG WMS</td>
      <td>
        Basic service elements - 6.5
        <ul>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getcapabilities.ConfiguredOutputFormatTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesOutputFormatTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getfeatureinfo.GetFeatureInfoOutputFormatTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getcapabilities.interactive.GetCapabilitiesInEnglishLanguageTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getfeatureinfo.interactive.GetFeatureInfoInEnglishLanguageTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getmap.GetMapOutputFormatTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesLayerCrsTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getmap.GetMapLayerCrsTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getfeatureinfo.interactive.GetFeatureInfoExceptionInEnglishLanguageTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getmap.interactive.GetMapExceptionInEnglishLanguageTest</li>
        </ul>
        GetCapabilities Operation - 6.6.1, 6.6.2
        <ul>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesRequestParameterTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesContentTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesAccessConstraintTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesAbstractTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesKeywordTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesStyleTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesLayerStyleTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesMaxExtendTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesLegendFormatTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesLegendUrlTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesLayerScaleDenominatorsTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesMinMaxScaleDenominatorsTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesFeatureListUrlTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesDataUrlTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getcapabilities.GetCapabilitiesLayerAttributesTest</li>
        </ul>
        GetMap Operation - 6.6.3, 6.6.4
        <ul>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getmap.GetMapRequestParametersTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getmap.GetMapTransparencyTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getmap.GetMapInImageExceptionsTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getmap.GetMapBlankExceptionsTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getmap.GetMapMultiDimensionalDataTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getmap.GetMapDimensionsTest</li>
        </ul>
        GetFeatureInfo Operation - 6.6.5, 6.6.6
        <ul>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getfeatureinfo.GetFeatureInfoFeatureCountTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getfeatureinfo.GetFeatureInfoExceptionsTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getfeatureinfo.GetFeatureInfoInfoFormatTest</li>
          <li>de.latlon.ets.wms13.core.dgiwg.testsuite.getfeatureinfo.GetFeatureInfoUomTest</li>
        </ul>
      </td>
    </tr>
  </tbody>
</table>

The Javadoc documentation provides more detailed information about the test 
methods that constitute the suite.


## Test run arguments

The test run arguments are summarized in Table 2. The _Obligation_ descriptor can 
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
      <td>wms</td>
      <td>URI</td>
      <td>M</td>
	  <td>A URI that refers to the implementation under test or metadata about it.
      Ampersand ('&amp;') characters must be percent-encoded as '%26'.</td>
    </tr>
	<tr>
      <td>vector</td>
      <td>Boolean</td>
      <td>M</td>
      <td>Controls if tests targeting layers which base on vector data are executed.</td>
    </tr>
  </tbody>
</table>


## Test Lead

  - Dirk Stenger (latlon)


##  Contributors

  - Dirk Stenger (latlon)
  - Richard Martell (Galdos)


##  License

[Apache License, Version 2.0](http://opensource.org/licenses/Apache-2.0 "Apache License")
