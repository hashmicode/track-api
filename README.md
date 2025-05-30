# Scalable Tracking Number Generator API

A RESTful Spring Boot API which generates **unique, regex compliant, and scalable tracking numbers** for shipping based on country, customer, and order.

---

## Features

- Endpoint: `GET /api/next-tracking-number`
- Tracking number format: `^[A-Z0-9]{1,16}$`
- Efficient and thread-safe generation logic
- Supports ISO 3166-1 alpha-2 country codes, UUIDs, RFC 3339 timestamps
- Ready to deploym on Google App Engine (GAE)

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

## Response

```json
{
  "tracking_number": "MYIDRED1672350D",
  "created_at": "2025-05-30T08:30:00Z"
}

1. Clone the Repository