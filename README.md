# DMN + Quarkus example

## Description

A simple DMN service to determine release date for Dutch Prison Service

Demonstrates DMN on Kogito capabilities, including REST interface code generation.

## Installing and Running

### Prerequisites

You will need:
  - Java 17+ installed
  - Environment variable JAVA_HOME set accordingly
  - Maven 3.9.6+ installed

When using native image compilation, you will also need:
  - [GraalVM 19.3.1](https://github.com/oracle/graal/releases/tag/vm-19.3.1) installed
  - Environment variable GRAALVM_HOME set accordingly
  - Note that GraalVM native image compilation typically requires other packages (glibc-devel, zlib-devel and gcc) to be installed too.  You also need 'native-image' installed in GraalVM (using 'gu install native-image'). Please refer to [GraalVM installation documentation](https://www.graalvm.org/docs/reference-manual/aot-compilation/#prerequisites) for more details.

### Compile and Run in Local Dev Mode

```
mvn clean compile quarkus:dev
```

### Package and Run in JVM mode

```
mvn clean package
java -jar target/quarkus-app/quarkus-run.jar
```

or on Windows

```
mvn clean package
java -jar target\quarkus-app\quarkus-run.jar
```

### Package and Run using Local Native Image
Note that this requires GRAALVM_HOME to point to a valid GraalVM installation

```
mvn clean package -Pnative
```

To run the generated native executable, generated in `target/`, execute

```
./target/dmn-quarkus-example-runner
```

Note: This does not yet work on Windows, GraalVM and Quarkus should be rolling out support for Windows soon.

## OpenAPI (Swagger) documentation
[Specification at swagger.io](https://swagger.io/docs/specification/about/)

You can take a look at the [OpenAPI definition](http://localhost:8080/openapi?format=json) - automatically generated and included in this service - to determine all available operations exposed by this service. For easy readability you can visualize the OpenAPI definition file using a UI tool like for example available [Swagger UI](https://editor.swagger.io).

In addition, various clients to interact with this service can be easily generated using this OpenAPI definition.

When running in either Quarkus Development or Native mode, we also leverage the [Quarkus OpenAPI extension](https://quarkus.io/guides/openapi-swaggerui#use-swagger-ui-for-development) that exposes [Swagger UI](http://localhost:8080/swagger-ui/) that you can use to look at available REST endpoints and send test requests.

## Test DMN Model using Maven

Validate the functionality of DMN models before deploying them into a production environment by defining test scenarios in Test Scenario Editor. 

To define test scenarios you need to create a .scesim file inside your project and link it to the DMN model you want to be tested. Run all Test Scenarios, executing:

```sh
mvn clean test
```
See results in surefire test report `target/surefire-reports` 

## Example Usage

Once the service is up and running, you can use the following example to interact with the service.

### POST /DetentionCalculation

Returns penalty information from the given inputs -- driver and violation:

Given inputs:

```json
{
  "Prisoner": {
    "id": "123456",
    "detentions": [
      {
        "titles": [
          {
            "name": "IVS",
            "decisionDate": "2024-01-24",
            "totalDays": 3
          },
          {
            "name": "IVS",
            "decisionDate": "2024-01-23",
            "totalDays": 3
          },
          {
            "name": "bewaring",
            "decisionDate": "2024-01-25",
            "totalDays": 10
          }
        ],
        "startDate": "2024-01-24"
      }
    ]
  }
}
```

Curl command (using the JSON object above):

```sh
curl -X 'POST' \
  'http://localhost:8080/DetentionCalculation' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "Prisoner": {
    "id": "123456",
    "detentions": [
      {
        "titles": [
          {
            "name": "IVS",
            "decisionDate": "2024-01-24",
            "totalDays": 3
          },
          {
            "name": "IVS",
            "decisionDate": "2024-01-23",
            "totalDays": 3
          },
          {
            "name": "bewaring",
            "decisionDate": "2024-01-25",
            "totalDays": 10
          }
        ],
        "startDate": "2024-01-24"
      }
    ]
  }
}
'


As response, detention information is returned.

Example response:

```json
{
  "CalculatedTitles": [
    {
      "totalDays": 3,
      "endDate": "2024-01-27",
      "daysExecuted": 3,
      "name": "IVS",
      "startDate": "2024-01-24",
      "decisionDate": "2024-01-23"
    },
    {
      "totalDays": 3,
      "endDate": "2024-01-30",
      "daysExecuted": 3,
      "name": "IVS",
      "startDate": "2024-01-27",
      "decisionDate": "2024-01-24"
    },
    {
      "totalDays": 10,
      "endDate": "2024-02-09",
      "daysExecuted": 10,
      "name": "bewaring",
      "startDate": "2024-01-30",
      "decisionDate": "2024-01-25"
    }
  ],
  "Prisoner": {
    "detentions": [
      {
        "endDate": null,
        "titles": [
          {
            "totalDays": 3,
            "name": "IVS",
            "priority": null,
            "decisionDate": "2024-01-24"
          },
          {
            "totalDays": 3,
            "name": "IVS",
            "priority": null,
            "decisionDate": "2024-01-23"
          },
          {
            "totalDays": 10,
            "name": "bewaring",
            "priority": null,
            "decisionDate": "2024-01-25"
          }
        ],
        "startDate": "2024-01-24",
        "status": null
      }
    ],
    "id": "123456"
  },
  "SetTitles": "function SetTitles( titles, startDate, updatedTitles )",
  "Detention": {
    "endDate": null,
    "titles": [
      {
        "totalDays": 3,
        "name": "IVS",
        "priority": null,
        "decisionDate": "2024-01-24"
      },
      {
        "totalDays": 3,
        "name": "IVS",
        "priority": null,
        "decisionDate": "2024-01-23"
      },
      {
        "totalDays": 10,
        "name": "bewaring",
        "priority": null,
        "decisionDate": "2024-01-25"
      }
    ],
    "startDate": "2024-01-24",
    "status": null
  },
  "Sort Priority": "function Sort Priority( title1, title2 )",
  "UpdatedPrisonerTitles": {
    "endDate": "2024-02-09",
    "titles": [
      {
        "totalDays": 3,
        "endDate": "2024-01-27",
        "daysExecuted": 3,
        "name": "IVS",
        "startDate": "2024-01-24",
        "decisionDate": "2024-01-23"
      },
      {
        "totalDays": 3,
        "endDate": "2024-01-30",
        "daysExecuted": 3,
        "name": "IVS",
        "startDate": "2024-01-27",
        "decisionDate": "2024-01-24"
      },
      {
        "totalDays": 10,
        "endDate": "2024-02-09",
        "daysExecuted": 10,
        "name": "bewaring",
        "startDate": "2024-01-30",
        "decisionDate": "2024-01-25"
      }
    ],
    "startDate": "2024-01-24",
    "status": "executed"
  },
  "Get Priority": "function Get Priority( name )",
  "SortedTitles": [
    {
      "totalDays": 3,
      "name": "IVS",
      "priority": 5,
      "decisionDate": "2024-01-23"
    },
    {
      "totalDays": 3,
      "name": "IVS",
      "priority": 5,
      "decisionDate": "2024-01-24"
    },
    {
      "totalDays": 10,
      "name": "bewaring",
      "priority": 10,
      "decisionDate": "2024-01-25"
    }
  ]
}
```
