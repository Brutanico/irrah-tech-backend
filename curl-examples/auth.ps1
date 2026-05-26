# POST /auth
curl.exe -X POST "http://localhost:8080/auth" `
  -H "Content-Type: application/json" `
  -d '{
    "documentId": "12345678901"
  }'
