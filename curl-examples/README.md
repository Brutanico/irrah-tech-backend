# Curl examples

These examples use `curl.exe` so they work reliably from PowerShell on Windows.

Run the app first:

```powershell
mvn spring-boot:run
```

Files:

- `auth.ps1`
- `clients.ps1`
- `conversations.ps1`
- `messages.ps1`
- `queue.ps1`

For endpoints that require authentication, set:

```powershell
$ClientId = "client-uuid-returned-by-api"
```
