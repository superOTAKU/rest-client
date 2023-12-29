package org.sky.restclient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sky.restclient.testdata.CreatePostCommand;
import org.sky.restclient.testdata.PostDTO;

import java.time.Duration;
import java.util.List;

public class RestClientTest {

    @Test
    public void test_getPosts() {
        List<PostDTO> posts = RestClients.getDefault()
                .get("https://jsonplaceholder.typicode.com/posts")
                .asObject(new TypeReference<List<PostDTO>>() {})
                .body();
        System.out.println("posts: " + posts);
        Assertions.assertNotNull(posts, "posts can't be null");
        Assertions.assertFalse(posts.isEmpty(), "posts must not be empty");
    }

    @Test
    public void test_getPostById() {
        PostDTO post = RestClients.getDefault()
                .get("https://jsonplaceholder.typicode.com/posts/{}", "1")
                .asObject(PostDTO.class)
                .body();
        Assertions.assertNotNull(post);
        System.out.println("post: " + post);
    }

    @Test
    public void test_addPost() {
        CreatePostCommand command = new CreatePostCommand();
        command.setTitle("One Piece");
        command.setBody("One Piece is wonderful!");
        command.setUserId(1L);
        PostDTO post = RestClients.getDefault()
                .post("https://jsonplaceholder.typicode.com/posts")
                .body(command)
                .asObject(PostDTO.class)
                .body();
        Assertions.assertNotNull(post.getBody());
        System.out.println("post added: " + post);
    }

    @Test
    public void test_setBaseUrl() {
        RestClient client = RestClients.newClient();
        client.config().setBaseUrl("https://jsonplaceholder.typicode.com");
        PostDTO post = client.get("/posts/{}", "1")
                .asObject(PostDTO.class)
                .body();
        Assertions.assertNotNull(post);
        System.out.println("post: " + post);
    }

    @Test
    public void test_500_throw_exception() {
        try {
            RestClients.newClient().get("https://httpbin.org/status/500")
                    .asEmpty()
                    .onError(e -> {
                        throw new RuntimeException("response error, code: " + e.code());
                    });
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("response error"));
        }
    }

    @Test
    public void test_500() {
        HttpResponse<Void> response = RestClients.newClient().get("https://httpbin.org/status/500")
                .asEmpty();
        Assertions.assertTrue(response.isError());
    }

    @Test
    public void test_config() {
        RestClients.newClient()
                .config()
                .setBaseUrl("https://httpbin.org")
                .setConnectTimeout(Duration.ofSeconds(30))
                .setOkHttpClient(new OkHttpClient())
                .setObjectMapper(new ObjectMapper());
    }

}
