# di-ipv-atp-dcs

The attribute provider for DCS (Document Checking Service).

## Running locally

First generate some test keys if not using DCS:

```shell
./scripts/generate-key-pair-for-dev.sh
```

Then build or run the service with gradle by either executing `bootRun` task or `build`.

## Paths

| Path | Description |
|-|-|
| `/process` | Expects a `POST` request with some JSON data. This will pack and wrap the data and send it off to DCS. Then it will return an outcome of DCS response. |
| `/checks/passport` | A mock endpoint to emulate DCS whilst we await to set up certificates with DCS. |

## How it works

#### Request

The service expects a `POST` request to `/process` path,
it expects the following data in the request body:

```shell
POST /process
{
  passportNumber: string,
  surname: string,
  forenames: [string],
  dateOfBirth: string(format: yyyy-MM-dd'T'HH:mm:ss),
  expiryDate: string(format: yyyy-MM-dd'T'HH:mm:ss)
}
```

#### Internal

This will then proceed to wrap the data as defined in [DCS](#dcs).
Currently, this service emulates the DCS endpoint due to not having certificates
set up with DCS.

The service calls an internal endpoint `/checks/passport`, which proceeds to unwrap the packet,
extract data and then wrap a new data packet in the same fashion as DCS would.

#### Response

The ATP service will respond with a JWS, where the ATP signs the data with its private key
for the IPV.

Sample JWS payload below:
```shell
{
  passportValid: boolean,
  errorMessages: [string] | null,
}
```

Currently, the response is returned as a plain text response with the JWS.

## DCS

This service takes a JSON input, and wraps it in a JWS packet consisting of signed,
encrypted and signed again data.

```shell
+-------------+   1    +-------------+   2    +-----------------+   3    +-----------------+
| JSON Object | +----> | JSON Object | +----> | +-------------+ | +----> | +-------------+ |
+-------------+  JWS   +-------------+  JWE   | | JSON Object | |  JWS   | | JSON Object | |
                       |   Signed    |        | +-------------+ |        | +-------------+ |
                       +-------------+        | |   Signed    | |        | |   Signed    | |
                                              | +-------------+ |        | +-------------+ |
                                              |    Encrypted    |        |    Encrypted    |
                                              +-----------------+        +-----------------+
                                                                         |     Signed      |
                                                                         +-----------------+
```

## Useful Links

- [https://dcs-pilot-docs.cloudapps.digital/#the-document-checking-service-pilot](https://dcs-pilot-docs.cloudapps.digital/#the-document-checking-service-pilot)