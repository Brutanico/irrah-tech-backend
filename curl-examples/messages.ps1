$ClientId = if ($env:CLIENT_ID) { $env:CLIENT_ID } else { "client-uuid-returned-by-api" }
$ConversationId = if ($env:CONVERSATION_ID) { $env:CONVERSATION_ID } else { "conversation-123" }
$RecipientId = if ($env:RECIPIENT_ID) { $env:RECIPIENT_ID } else { "recipient-456" }
$MessageId = if ($env:MESSAGE_ID) { $env:MESSAGE_ID } else { "msg-uuid-returned-by-api" }

# POST /messages
curl.exe -X POST "http://localhost:8080/messages" `
  -H "Content-Type: application/json" `
  -H "X-Client-Id: $ClientId" `
  -d "{
    `"conversationId`": `"$ConversationId`",
    `"recipientId`": `"$RecipientId`",
    `"content`": `"Hello from curl`",
    `"priority`": `"normal`"
  }"

# GET /messages
curl.exe -X GET "http://localhost:8080/messages" `
  -H "X-Client-Id: $ClientId"

# GET /messages?status=queued&priority=urgent
curl.exe -X GET "http://localhost:8080/messages?status=queued&priority=urgent" `
  -H "X-Client-Id: $ClientId"

# GET /messages/{id}
curl.exe -X GET "http://localhost:8080/messages/$MessageId" `
  -H "X-Client-Id: $ClientId"

# GET /messages/{id}/status
curl.exe -X GET "http://localhost:8080/messages/$MessageId/status" `
  -H "X-Client-Id: $ClientId"
