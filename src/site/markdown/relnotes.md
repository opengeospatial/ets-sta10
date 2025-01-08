SensorThings API Conformance Test Release Notes
==================================================

1.5 (2025-01-08)
---------------------

Attention: Java 17 and Tomcat 10.1 are required.

- [#69](https://github.com/opengeospatial/ets-sta10/issues/69) - Migrate test suite to TEAM Engine 6 (Java 17)

1.4 (2023-11-24)
---------------------
- [#64](https://github.com/opengeospatial/ets-sta10/issues/64) - Orderby tests broken
- [#62](https://github.com/opengeospatial/ets-sta10/issues/62) - Upgrade json dependency to v20231013

1.3 (2022-10-28)
---------------------
- [#61](https://github.com/opengeospatial/ets-sta10/pull/61) - Update url to license and integration into reporting
- [#29](https://github.com/opengeospatial/ets-sta10/pull/29) - * EntityType refactoring
- [#44](https://github.com/opengeospatial/ets-sta10/issues/44) - Add detailed error messages
- [#52](https://github.com/opengeospatial/ets-sta10/issues/52) - Add template to get an XML/JSON response via rest endpoint
- [#57](https://github.com/opengeospatial/ets-sta10/issues/57) - Tests compare times using String.equals
- [#59](https://github.com/opengeospatial/ets-sta10/pull/59) - Add Dockerfile and configure Maven to use Docker
- [#54](https://github.com/opengeospatial/ets-sta10/pull/54) - Bump httpclient from 4.5 to 4.5.13

1.2 (2018-04-30)
---------------------
-  Fix the problem that the test suite failed if a server implements MultiDatastream.
-  Remove Tests that are for testing HTTP PUT as implementing PUT is not mandatory in SensorThings specification.

1.1 (2018-02-26)
---------------------
-  Small fix to make the Test Suite workable with latest version of TEAMEngine.

1.0 (2017-03-12)
---------------------
-  [#36](https://github.com/opengeospatial/ets-sta10/issues/36) Production release after TC /PC approval.

0.7 (2016-10-04)
-------------------
- [#23](https://github.com/opengeospatial/ets-sta10/pull/23) - Merge PULL request to fix a castException bug
- [#24](https://github.com/opengeospatial/ets-sta10/issues/24) - Merge PULL request for adding additional tests for NotEqual operator

0.6 (2016-09-09)
-------------------
- [#13](https://github.com/opengeospatial/ets-sta10/pull/13) - Name properties - Merge PULL request to add checks for "name" property.
- [#18](https://github.com/opengeospatial/ets-sta10/pull/18) - Added missing tests for NotEqual
- [#16](https://github.com/opengeospatial/ets-sta10/issues/16) - Invalid encoding type values

0.5 (2016-07-08)
-------------------
- Merge PULL request to compare time properties correctly.
- Fix some typos.

0.4 (2016-07-07)
-------------------
- Add sample request for creating entities before starting the test.
- Clarifying the preconditions and postconditions for the test.
- Merge PULL requests to fix a few bugs.
- [#10](https://github.com/opengeospatial/ets-sta10/issues/10) - Timestamps should not be quoted in URLs
- [#9](https://github.com/opengeospatial/ets-sta10/issues/9) - Server-driven paging neglected
- [#7](https://github.com/opengeospatial/ets-sta10/issues/7) - Orderby tests test using String compare.
- [#5](https://github.com/opengeospatial/ets-sta10/issues/5) - GeoJSON encoding type

0.3 (2016-01-25)
-------------------
- Add more tests to "A.2 Filtering Extension" and "A.1 Sensing Core" conformance classes.
- Clean the code and complete the API doc.

0.2 (2015-11-05)
-------------------
- Update release notes claryfing that the test data will be deleted.

0.1 (2015-10-30)
----------------------
- First release. It contains test for conformance classes "A.1 Sensing Core", "A.3 Create Update Delete", and "A.2 Filtering Extension".
