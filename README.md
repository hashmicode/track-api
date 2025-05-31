# Scalable Tracking Number Generator API

A RESTful Spring Boot API which generates **unique, regex compliant, and scalable tracking numbers** for shipping based on country, customer, and order.

---

## Features

- Endpoint: `GET /api/next-tracking-number`
- Tracking number format: `^[A-Z0-9]{1,16}$`
- Efficient and thread-safe generation logic
- Supports ISO 3166-1 alpha-2 country codes, UUIDs, RFC 3339 timestamps
- Ready to deploy on Google App Engine (GAE)

---

## 📦 Query Parameters

| Parameter              | Type        | Description                                               |
|------------------------|-------------|-----------------------------------------------------------|
| `origin_country_id`     | String      | Origin country code (ISO 3166-1 alpha-2), e.g. `MY`       |
| `destination_country_id`| String      | Destination country code, e.g. `ID`                       |
| `weight`                | BigDecimal  | Parcel weight in kg (up to 3 decimal places), e.g. `1.234`|
| `created_at`            | String      | Timestamp (RFC 3339 format), e.g. `2025-05-30T10:30:00+08:00` |
| `customer_id`           | UUID        | Customer UUID                                             |
| `customer_name`         | String      | Customer’s name, e.g. `RedBox Logistics`                  |
| `customer_slug`         | String      | Slug version of customer name, e.g. `redbox-logistics`    |

---

##  Deployed API

 [Live URL](https://track-api-461423.ew.r.appspot.com/api/next-tracking-number)
 https://track-api-461423.ew.r.appspot.com/api/next-tracking-number?origin_country_id=MY&destination_country_id=ID&weight=1.234&created_at=2025-05-30T10:30:00%2B08:00&customer_id=de619854-b59b-425e-9db4-943979e1bd49&customer_name=RedBox%20Logistics&customer_slug=redbox-logistics

## To run locally

1. **Prerequisites**\
   Java 17+  \
   Maven 3.8+ \
   Google Cloud SDK (for deployment)

2. **Clone and run**
   https://github.com/hashmicode/track-api.git  \
   `cd track-api`\
   `./mvnw clean install`\
   `./mvnw spring-boot:run`

3. **Running Tests** \
   `./mvnw clean test`


4. **Testing the API locally**

http://localhost:8080/api/next-tracking-number?...params...



**Open  browser or use Postman:**

http://localhost:8080/api/next-tracking-number?origin_country_id=MY&destination_country_id=ID&weight=1.234&created_at=2025-05-30T10:30:00%2B08:00&customer_id=de619854-b59b-425e-9db4-943979e1bd49&customer_name=RedBox%20Logistics&customer_slug=redbox-logistics

## Response

```json
{
  "tracking_number": "MYIDRED1672350D",
  "created_at": "2025-05-30T08:30:00Z"
}
```

## Deploying on Google App Engine

1. [x] **Authenticate with Google Cloud CLI:**
    `gcloud init`
2. [x] **Set project & region:**\
   `gcloud config set project track-api-461423`\
   `gcloud app create --region=asia-southeast1`
3. [x] **Deploy:**\
   `gcloud app deploy`

