# KB ADAA API Java SDK

#### Links
* [ADAA API technical manual](https://github.com/komercka/adaa-client/wiki)
* [KB API Developer portal](https://api.kb.cz/open/apim/store)
* [ADAA API technical manual (for sandbox version)](https://www.kb.cz/getmedia/3662e39f-04af-4872-bf02-eda9c05a0c11/API_Sandbox_Account-Direct-Access-API-Manual_EN.pdf.aspx)

---

The purpose of this SDK is to provide Java developers a simple interface
to connect to the KB ADAA API.
Developer can either use provided implementation of the interface or use it to build own implementation.

#### Structure
This SDK contains two modules:
```
api - contains the interfaces for calling KB ADAA API and the current version of the API's Swagger documentation
|--- exception - contains custom exceptions for this SDK
|--- model - domain model objects
|--- search - Fluent API based interfaces that serve for searching desired resources
jersey-impl - JAX-RS Jersey implementation of the interfaces, provided by api module
spring-boot-auto-configuration - Spring Boot auto-configuration that uses the Jersey implementation under the hood and serves
as easy integration of this SDK to your Spring Boot application
```
#### Usage
You can use this SDK as a Maven dependency or as a external JAR.

##### As Maven dependencies
```xml
...
    <dependency>
        <groupId>cz.kb.openbanking.adaa.client</groupId>
        <artifactId>api</artifactId>
        <version>1.0.0</version>
    </dependency>
    <dependency>
        <groupId>cz.kb.openbanking.adaa.client</groupId>
        <artifactId>jersey-impl</artifactId>
        <version>1.0.0</version>
    </dependency>
...
```

##### KB ADAA API call
There is only one main interface:
* `AccountApi` - provides information about account's balances and allows you to get transaction history.

This interface is based on the Fluent API, thus it could be customized by calling particular
Fluent API methods, e.g.:
```java
PageSlice<AccountTransactions> result = accoutApi.transactions(iban, currency)
        .fromDate(OffsetDateTime.of(LocalDateTime.of(2019, 10, 3, 23, 40), ZoneOffset.UTC))
        .size(3)
        .page(0)
        .find();
```
, where `fromDate()`, `size()`, `page()` are methods of theFluent API interfaces `DateSearch` and `PageSearch`

To be able to call ADAA API you should get the account ID first (unique identifier of IBAN with currency).
For more convenient usage, this SDK encapsulates the `accounts` endpoint call,
thus you need to provide only client's IBAN with currency - the SDK will do the rest.

