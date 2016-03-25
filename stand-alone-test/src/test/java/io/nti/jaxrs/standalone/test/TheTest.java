package io.nti.jaxrs.standalone.test;

import io.nti.jaxrs.standalone.StandAloneServer;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Jeff Hutchins
 */
public class TheTest {

    private static OkHttpClient client;
    private static StandAloneServer server = new StandAloneServer();

    @BeforeClass
    public static void setupClass() throws Exception {
        client = new OkHttpClient();
        server.start();
    }

    @AfterClass
    public static void teardownClass() throws Exception {
        server.stop();
    }

    @Test
    public void testGet() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/testing")
                .build();

        Response response = client.newCall(request).execute();
        assertThat(response.body().string()).contains("get");
        assertThat(response.code()).isEqualTo(200);
    }

    @Test
    public void testPost() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/testing")
                .post(RequestBody.create(MediaType.parse("text/plain"), "data"))
                .build();

        Response response = client.newCall(request).execute();
        assertThat(response.body().string()).contains("post");
        assertThat(response.code()).isEqualTo(200);
    }

    @Test
    public void testPut() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/testing")
                .put(RequestBody.create(MediaType.parse("text/plain"), "data"))
                .build();

        Response response = client.newCall(request).execute();
        assertThat(response.body().string()).contains("put");
        assertThat(response.code()).isEqualTo(200);
    }

    @Test
    public void testHead() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/testing")
                .head()
                .build();

        Response response = client.newCall(request).execute();
        assertThat(response.body().string()).isEmpty();
        assertThat(response.code()).isEqualTo(200);
    }

    @Test
    public void testDelete() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/testing")
                .delete()
                .build();

        Response response = client.newCall(request).execute();
        assertThat(response.body().string()).isEmpty();
        assertThat(response.code()).isEqualTo(204);
    }
}
