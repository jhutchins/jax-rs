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

    // TEST ALL THE METHODS ANNOTATIONS

    @Test
    public void testMethodGet() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/methods")
                .build();

        Response response = client.newCall(request).execute();
        assertThat(response.body().string()).contains("get");
        assertThat(response.code()).isEqualTo(200);
    }

    @Test
    public void testMethodPost() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/methods")
                .post(RequestBody.create(MediaType.parse("text/plain"), "data"))
                .build();

        Response response = client.newCall(request).execute();
        assertThat(response.body().string()).contains("post");
        assertThat(response.code()).isEqualTo(200);
    }

    @Test
    public void testMethodPut() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/methods")
                .put(RequestBody.create(MediaType.parse("text/plain"), "data"))
                .build();

        Response response = client.newCall(request).execute();
        assertThat(response.body().string()).contains("put");
        assertThat(response.code()).isEqualTo(200);
    }

    @Test
    public void testMethodHead() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/methods")
                .head()
                .build();

        Response response = client.newCall(request).execute();
        assertThat(response.body().string()).isEmpty();
        assertThat(response.code()).isEqualTo(200);
    }

    @Test
    public void testMethodDelete() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/methods")
                .delete()
                .build();

        Response response = client.newCall(request).execute();
        assertThat(response.body().string()).contains("delete");
        assertThat(response.code()).isEqualTo(200);
    }

    @Test
    public void testMethodOptions() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/methods")
                .method("OPTIONS", null)
                .build();

        Response response = client.newCall(request).execute();
        assertThat(response.body().string()).contains("options");
        assertThat(response.code()).isEqualTo(200);
    }

    // TEST CUSTOM ANNOTATIONS

    @Test
    public void testAnnotationNonStandard() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/annotations")
                .method("OTHER", null)
                .build();

        Response response = client.newCall(request).execute();
        assertThat(response.body().string()).contains("other");
        assertThat(response.code()).isEqualTo(200);
    }

    @Test
    public void testAnnotationsCustomStandard() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/annotations")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        assertThat(response.body().string()).contains("get");
        assertThat(response.code()).isEqualTo(200);
    }

    // TEST RESPONSE TYPES

    @Test
    public void testResponseString() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/response/string")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        assertThat(response.body().string()).isEqualTo(ResponsesTestResource.RESPONSE);
        assertThat(response.code()).isEqualTo(200);
    }

    @Test
    public void testResponseStringNull() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/response/string/null")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        assertThat(response.body().string()).isEmpty();
        assertThat(response.code()).isEqualTo(204);
    }

    @Test
    public void testResponseVoid() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/response/void")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        assertThat(response.body().string()).isEmpty();
        assertThat(response.code()).isEqualTo(204);
    }

    @Test
    public void testResponseResponse() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/response/response")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        assertThat(response.body().string()).isEqualTo(ResponsesTestResource.RESPONSE);
        assertThat(response.code()).isEqualTo(200);
    }

    @Test
    public void testResponseResponseNull() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/response/response/null")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        assertThat(response.body().string()).isEmpty();
        assertThat(response.code()).isEqualTo(204);
    }

    @Test
    public void testResponseResponseNone() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/response/response/none")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        assertThat(response.body().string()).isEmpty();
        assertThat(response.code()).isEqualTo(200);
    }

    @Test
    public void testResponseGeneric() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/response/generic")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        assertThat(response.body().string()).isEqualTo(ResponsesTestResource.RESPONSE);
        assertThat(response.code()).isEqualTo(200);
    }

    @Test
    public void testResponseGenericNull() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8080/response/generic/null")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        assertThat(response.body().string()).isEmpty();
        assertThat(response.code()).isEqualTo(204);
    }
}
