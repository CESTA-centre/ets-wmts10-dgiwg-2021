# Test Suite for the DGIWG WMTS Profile - 2021

This test suite verifies that a Web Map Tile Service (WMTS) component meets the requirements of the 
DGIWG WMTS application profile, [DGIWG-124](https://portal.dgiwg.org/files/68271).
A conforming implementation must also satisfy the constraints of the **Queryable WMTS** conformance 
class as defined in the base [WMTS 1.0 standard](http://www.ogc.org/standards/wmts).

This test suite has been designed based on the existing [ets-wms13-dgiwg](https://github.com/opengeospatial/ets-wms13-dgiwg) project.


## How to build the project

Before building it you need to build ets-dgiwg-core. Do the follwoing:

`git clone https://github.com/opengeospatial/ets-dgiwg-core`

`cd ets-dgiwg-core`

`mvn clean install`

Now you can build the project:

`git clone https://github.com/CESTA-centre/ets-wmts10-dgiwg-2021.git`

`cd ets-wmts10-dgiwg-2021`

`mvn clean install`

It will creat the jars to be installed in TEAM Engine.


## How to run the tests

### Integrated development environment (IDE)

You can use a Java IDE such as Eclipse to develop and run the test suite. 
Clone the repository and build the project. The runtime configuration is summarized below.
It is more common practice to use the TEAM Engine simulation engine to run the tests.

__Main class__: `org.opengis.cite.wmts10.dgiwg.WmtsDgiwgTestNGController`

__Arguments__: The first argument must refer to an XML properties file containing the 
required test run argument (a reference to a WMTS 1.0 capabilities document). If not specified, 
the default location of the file at `${user.home}/test-run-props.xml` will be used.

You can modify the default settings in the sample [test-run-props.xml](src/main/config/test-run-props.xml) 
file, which is shown below. The value of the **wmts** argument must be an absolute URI that 
conforms to the 'http' or 'file' schemes. The **tes** argument specifies the location of 
a service that provides a RESTful API for running the base WMTS 1.0 test suite; by default 
the OGC teamengine installation--the 'beta' facility--will be used, but an alternative 
installation may be used instead (for example: http://localhost:8080/teamengine).

**Test run arguments** 
_Do not forget to indicate an absolute URI for **wmts** argument_.

|Argument  |Description  |Default value  |
|:---------|:------------|:---------------|
|wmts  |Location of WMTS capabilities document  |
|vector  |Are any layers derived from vector data?  |false
|tes |Endpoint for test execution service  |http://cite.opengeospatial.org/te2/


```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties version="1.0">
  <comment>Test run arguments (ets-wmts10-dgiwg-2021)</comment>
  <entry key="wmts"></entry>
  <entry key="vector">false</entry>
  <entry key="tes">http://cite.opengeospatial.org/te2/</entry>
</properties>
```

The TestNG results file (testng-results.xml) will be written to a subdirectory 
in ${user.home}/testng/ having a UUID value as its name.


### OGC test harness

Use [TEAM Engine](https://github.com/opengeospatial/teamengine), the official 
OGC test harness. The latest test suite release should be available at the 
[beta testing facility](http://cite.opengeospatial.org/te2/). You can also 
[build and deploy](https://github.com/opengeospatial/teamengine) the test 
harness yourself and use a local installation.


## How to contribute

If you would like to get involved, you can:

* [Report an issue](https://github.com/CESTA-centre/ets-wmts10-dgiwg-2021/issues) such as a defect or an 
enhancement request
* Help to resolve an [open issue](https://github.com/CESTA-centre/ets-wmts10-dgiwg-2021/issues?q=is%3Aopen)
* Fix a bug: Fork the repository, apply the fix, and create a pull request
* Add new tests: Fork the repository, implement (and verify) the tests on a new topic branch, 
and create a pull request
