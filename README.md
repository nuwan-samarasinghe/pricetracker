# 📉 Price Tracker API Documentation

## API Endpoint
```
http://localhost:8080/api/track
```

## Request Body

```json
{
  "url": "https://example.com/product1",
  "desiredPrice": 30.00,
  "checkFrequency": "SECONDLY"
}
```

## Response Body

```json
{
  "url": "https://example.com/product1",
  "desiredPrice": 30.00,
  "checkFrequency": "SECONDLY"
}