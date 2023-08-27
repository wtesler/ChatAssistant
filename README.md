Add `google-services.json` inside app folder.

Add `secure-strings.xml` to values folder and make it look like this:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>

    <!-- Database connection strings -->
    <string name="development_server_url">LOCALHOST_HTTPS_URL/</string>
    <string name="production_server_url">PRODUCTION_HTTPS_URL/</string>

    <!--  OAuth  -->
    <string name="server_client_oauth_id">OAUTH_CLIENT_ID_RETRIEVED_FROM_CREDENTIALS</string>
</resources>
```

Internal Test URL:

https://play.google.com/apps/internaltest/4701648025114892834