# 📡 IP Lens API – Technical Internal Documentation

**Author:** María Berenice Lozano Mercado  
**Title:** Computer Engineer  
**Email:** [bere.lozano.m@gmail.com](mailto:bere.lozano.m@gmail.com)


## Description
This is a Springboot 4.0 and Java 17 service which consumes Single IP Lookup API from **ipregistry.co** in order to retrieve geolocalization, domain and security data associated with an IP address.

### Starting the IP Lens API
You can start this service by executing this command:
```bash
Windows:

.\mvnw.cmd clean compile spring-boot:run
```
```bash
Linux, Mac:

mvnw clean compile spring-boot:run
```
You can generate IP Lens JAR by executing this command:
```bash
mvnw clean compile install
```
Please find the user documentation in the following endpoint when you start this service in your local workstation:

[IP Lens local swagger documentation](http://localhost:9090/swagger-ui/index.html)

---


## About internal call to Single IP Lookup API from ipregistry

### Single IP Lookup API Parameters

| Parameter | Type   | Description |
|----------|--------|-------------|
| `ip`     | String | IP address to look up |

### 🔑 Query Parameters

| Parameter | Type   | Required | Description |
|----------|--------|-------------|-------------|
| `key`    | String | ✅ Yes | API Key provided by ipregistry |
| `fields` | String | ❌ No | List of fields to retrieve |

### List of `fields` utilized from ipregistry

- type,
- connection.domain,
- connection.type,
- location.continent.code,
- location.continent.name,
- location.country.code,
- location.country.name,
- location.latitude,
- location.longitude,
- security.is_threat

### 🧪 ipregistry HTTP request sample

```bash
curl --request GET --location 'https://api.ipregistry.co/187.235.78.78?key=API_KEY&fields=type%2Cconnection.domain%2Cconnection.type%2Clocation.continent.code%2Clocation.continent.name%2Clocation.country.code%2Clocation.country.name%2Clocation.latitude%2Clocation.longitude%2Csecurity.is_threat'
```

### 📦 ipregistry Response structure

API returns JSON object with some inner nested objects

### ipregistry Response sample

```json
{
  "type": "IPv4",
  "connection": {
    "domain": "telmex.com",
    "type": "isp"
  },
  "location": {
    "continent": {
      "code": "NA",
      "name": "North America"
    },
    "country": {
      "code": "MX",
      "name": "Mexico"
    },
    "latitude": 19.26025,
    "longitude": -99.59078
  },
  "security": {
    "is_threat": false
  }
}
```
--- 

## Reference Documentation
For further reference, please consider the following sections:

* [Single IP Lookup documentation](https://https://ipregistry.co/docs/endpoints#single-ip)
* [Swagger API specification generator](https://swagger.io)
* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/4.0.2/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.0.2/maven-plugin/build-image.html)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/4.0.2/reference/using/devtools.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/4.0.2/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/4.0.2/reference/web/reactive.html)

## Guides
The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)

## Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

