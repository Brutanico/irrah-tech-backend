$ClientId = if ($env:CLIENT_ID) { $env:CLIENT_ID } else { "client-uuid-returned-by-api" }

# GET /clients
curl.exe -X GET "http://localhost:8080/clients"

# POST /clients
curl.exe -X POST "http://localhost:8080/clients" `
  -H "Content-Type: application/json" `
  -d '{
    "name": "Maria Silva",
    "documentId": "12345678901",
    "documentType": "CPF",
    "planType": "prepaid",
    "balance": 25.00,
    "limit": 0.00,
    "active": true
  }'

# GET /clients/{id}
curl.exe -X GET "http://localhost:8080/clients/$ClientId"

# PUT /clients/{id}
curl.exe -X PUT "http://localhost:8080/clients/$ClientId" `
  -H "Content-Type: application/json" `
  -d '{
    "name": "Maria Silva Atualizada",
    "documentId": "12345678901",
    "documentType": "CPF",
    "planType": "prepaid",
    "balance": 50.00,
    "limit": 0.00,
    "active": true
  }'

# GET /clients/{id}/balance
curl.exe -X GET "http://localhost:8080/clients/$ClientId/balance"
