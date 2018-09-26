package prototype.webstack;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import prototype.testsupport.HttpServerResource;
import prototype.testsupport.VertexTestComponent.HttpEndpointsModule;

@ExtendWith(VertxExtension.class)
class VertxIntegrationTest {

    private static final String PING_URI = "/ping";

    @RegisterExtension
    static HttpServerResource _httpServer = HttpServerResource.create(new HttpEndpointsModule()
            .route(router -> router.route(HttpMethod.GET, PING_URI)
                    .handler(context -> {
                        context.response().end("pong");
                    })));

    /*
     * Typical async vertex tests below.
     */
    @Test
    void testNotFound_Async(VertxTestContext testContext) throws InterruptedException {
        _httpServer.webClient().get("/not-a-address")
                .as(BodyCodec.string())
                .send(testContext.succeeding(response -> {
                    testContext.verify(() -> {
                        assertThat(response.statusCode()).isEqualTo(404);
                        testContext.completeNow();
                    });
                }));
    }

    @Test
    void testGet_Async(VertxTestContext testContext) throws InterruptedException {
        _httpServer.webClient().get(PING_URI)
                .as(BodyCodec.string())
                .send(testContext.succeeding(response -> {
                    testContext.verify(() -> {
                        assertThat(response.statusCode()).isEqualTo(200);
                        assertThat(response.body()).isEqualTo("pong");
                        testContext.completeNow();
                    });
                }));
    }

    /*
     * Synced version of the tests below.
     */

    @Test
    void testNotFound_Sync() throws InterruptedException {
        HttpResponse<String> response = _httpServer.executeGetRequest("/not-a-address", BodyCodec.string());
        assertThat(response.statusCode()).isEqualTo(404);
    }

    @Test
    void testGet_Sync() throws InterruptedException {
        HttpResponse<String> response = _httpServer.executeGetRequest(PING_URI, BodyCodec.string());
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).isEqualTo("pong");
    }

}
