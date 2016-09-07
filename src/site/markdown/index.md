#  SensorThings API (STA) 1.0 Conformance Test Suite
## Scope
This test suite verifies conformance with SensorThings API (STA) 1.0. It is based on the following OGC specifications:

  * _OGC SensorThings API_, Version 1.0 [OGC 15-078](https://portal.opengeospatial.org/files/?artifact_id=64146) (Has not been released. This the the draft specification)

## Test Coverage by Conformance Class

The following table provides information about the conformance class that are
implemented in the test and reference to the section in the specification.


| Conformance Class                     | Reference | Test Class                                                 |
|---------------------------------------|-----------|------------------------------------------------------------|
| SensorThings API Sensing Core         | A.1       | org.opengis.cite.sta10.sensingCore.Capability1Tests        |
| SensorThings API Filtering Extension  | A.2       | org.opengis.cite.sta10.createUpdateDelete.Capability2Tests |
| SensorThings API Create-Update-Delete | A.3       | org.opengis.cite.sta10.filteringExtension.Capability3Tests |

Classes A.4 to A.8 have not been implemented.

## Preconditions and Postconditions

Here are the preconditions and postconditions for running SensorThings Test Suite on a service.

### Preconditions
The service under test should have a small number of each entity type in SensorThings. In other words, the service under test needs to have at least one entity for each entity type and the number of entities for each entityType must be less that the pagination limit. It means that we can access the entities of each entity type without the need to follow the @iot.nextLink.
If your service supports "Deep Insert" You can POST the following JSON to create the required entities before starting the test:
```
{
    "description": "thing 1",
    "name": "thing name 1",
    "properties": {
        "reference": "first"
    },
    "Locations": [
        {
            "description": "location 1",
            "name": "location name 1",
            "location": {
                "type": "Point",
                "coordinates": [
                    -117.05,
                    51.05
                ]
            },
            "encodingType": "application/vnd.geo+json"
        }
    ],
    "Datastreams": [
        {
            "unitOfMeasurement": {
                "name": "Lumen",
                "symbol": "lm",
                "definition": "http://www.qudt.org/qudt/owl/1.0.0/unit/Instances.html/Lumen"
            },
            "description": "datastream 1",
            "name": "datastream name 1",
            "observationType": "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement",
            "ObservedProperty": {
                "name": "Luminous Flux",
                "definition": "http://www.qudt.org/qudt/owl/1.0.0/quantity/Instances.html/LuminousFlux",
                "description": "observedProperty 1"
            },
            "Sensor": {
                "description": "sensor 1",
                "name": "sensor name 1",
                "encodingType": "application/pdf",
                "metadata": "Light flux sensor"
            },
            "Observations":[
                {
                    "phenomenonTime": "2015-03-03T00:00:00Z",
                    "result": 3 
                },
                {
                    "phenomenonTime": "2015-03-04T00:00:00Z",
                    "result": 4 
                }
            ]
        },
        {
            "unitOfMeasurement": {
                "name": "Centigrade",
                "symbol": "C",
                "definition": "http://www.qudt.org/qudt/owl/1.0.0/unit/Instances.html/Lumen"
            },
            "description": "datastream 2",
            "name": "datastream name 2",
            "observationType": "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement",
            "ObservedProperty": {
                "name": "Tempretaure",
                "definition": "http://www.qudt.org/qudt/owl/1.0.0/quantity/Instances.html/Tempreture",
                "description": "observedProperty 2"
            },
            "Sensor": {
                "description": "sensor 2",
                "name": "sensor name 2",
                "encodingType": "application/pdf",
                "metadata": "Tempreture sensor"
            },
            "Observations":[
                {
                    "phenomenonTime": "2015-03-05T00:00:00Z",
                    "result": 5
                },
                {
                    "phenomenonTime": "2015-03-06T00:00:00Z",
                    "result": 6 
                }
            ]
        }
    ]
}
```

### Postconditions
If you are testing only <b>"Sensing core"</b> there will be no postcondition.<br/>
If you are testing <b>"Create-Update-Delete"</b> or <b>"Filtering extension"</b>, after finishing the test process, <b>all the data in the service under test will be DELETED</b>.

## Release Notes

Release notes are available from the [relnotes.html](relnotes.html).
