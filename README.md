# Rest Client

Rest Client is an easy-to-use http client library.

Rest Client is based on OkHttpClient and Jackson, it is fully configurable.

## Getting Started

### New Client Instance

Get global instance:
```java
RestClients.getDefault()
```

Create new instance:
```java
RestClients.newClient();
```

### Client Configuration

Each rest client instance expose a config object.

you can control all necessary details based on config object.
```java
RestClients.newClient()
    .config()
    .setBaseUrl("https://httpbin.org")
    .setConnectTimeout(Duration.ofSeconds(30))
    .setOkHttpClient(new OkHttpClient())
    .setObjectMapper(new ObjectMapper());
```

As you can see, OkHttpClient && ObjectMapper is directly exposed, for detail config such
as TSL.

### Send Request

#### Simple get request
```java
 List<PostDTO> posts = RestClients.getDefault()
                .get("https://jsonplaceholder.typicode.com/posts")
                .asObject(new TypeReference<List<PostDTO>>() {})
                .body();
```

#### Get request with url variables
```java
PostDTO post = RestClients.getDefault()
                .get("https://jsonplaceholder.typicode.com/posts/{}", "1")
                .asObject(PostDTO.class)
                .body();
```

#### Post request
```java
CreatePostCommand command = new CreatePostCommand();
command.setTitle("One Piece");
command.setBody("One Piece is wonderful!");
command.setUserId(1L);
PostDTO post = RestClients.getDefault()
        .post("https://jsonplaceholder.typicode.com/posts")
        .body(command)
        .asObject(PostDTO.class)
        .body();
```

#### Error Handling

throws custom exception
```java
RestClients.newClient().get("https://httpbin.org/status/500")
                    .asEmpty()
                    .onError(e -> {
                        throw new RuntimeException("response error, code: " + e.code());
                    });
```

detect through response object
```java
HttpResponse<Void> response = RestClients.newClient().get("https://httpbin.org/status/500")
                .asEmpty();
assert response.isError();
```

map error object
```java
ErrorDTO errDTO = RestClients.newClient().get("https://example.com/not-exists")
    .asEmpty()
    .errObject(ErrorDTO.class);
```

## Thanks

Some API design is inspired by Unirest: 

[https://github.com/Kong/unirest-java/tree/main][Unirest Java]

[Unirest Java]: https://github.com/Kong/unirest-java/tree/main
