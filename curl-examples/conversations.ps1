$ClientId = if ($env:CLIENT_ID) { $env:CLIENT_ID } else { "client-uuid-returned-by-api" }
$ConversationId = if ($env:CONVERSATION_ID) { $env:CONVERSATION_ID } else { "conversation-123" }

# GET /conversations
curl.exe -X GET "http://localhost:8080/conversations" `
  -H "X-Client-Id: $ClientId"

# GET /conversations/{id}
curl.exe -X GET "http://localhost:8080/conversations/$ConversationId" `
  -H "X-Client-Id: $ClientId"

# GET /conversations/{id}/messages
curl.exe -X GET "http://localhost:8080/conversations/$ConversationId/messages" `
  -H "X-Client-Id: $ClientId"
