# Testcontainers03

Example of using Testcontainers library and TestNG, using OkHttp to connect to a started container
that holds an echo server, and using GSON as a simple implementation of JsonObjects.

The echo server is ealen/echo-server.

To use OkHttp, we create an OkHttpClient which is used for all requests.

For each test, we create a Request object, then execute it.

The Request object has a body built from a JSON object.

The Response object provides a JSON object as text, which is then parsed with JsonParser.

These tests carry out a Get request, then a Post request with a String, then two Post requests that
use JSON objects.
