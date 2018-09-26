package prototype.testsupport;

import static org.assertj.core.api.Assertions.fail;

import org.junit.jupiter.api.extension.ExtensionContext;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.codec.BodyCodec;
import prototype.testsupport.VertexTestComponent.HttpEndpointsModule;

public class HttpServerResource extends ExternalResource {

    private final HttpServer _httpServer;
    private WebClient _webClient;

    private HttpServerResource(HttpEndpointsModule endpointsModule) {
        _httpServer = VertexTestComponent.builder()
                .httpRouteModule(endpointsModule)
                .build()
                .httpServer();
    }

    public static HttpServerResource create(HttpEndpointsModule endpointsModule) {
        return new HttpServerResource(endpointsModule);
    }

    @Override
    protected void before(ExtensionContext context) throws Exception {
        AsyncResult<HttpServer> result = Sync.<HttpServer>await(handler -> _httpServer.listen(0, handler));
        if (result.failed()) {
            fail("Failed to start HTTP server", result.cause());
        }
        _webClient = WebClient.create(Vertx.vertx(), new WebClientOptions().setDefaultPort(port()));
    }

    @Override
    protected void after(ExtensionContext context) throws Exception {
        _webClient.close();
        _httpServer.close();
    }

    public int port() {
        return _httpServer.actualPort();
    }

    public WebClient webClient() {
        return _webClient;
    }

    public <T> HttpResponse<T> executeGetRequest(String uri, BodyCodec<T> bodyCodec) throws InterruptedException {
        AsyncResult<HttpResponse<T>> httpResult = Sync.<HttpResponse<T>>await(result -> _webClient.get(uri).as(bodyCodec).send(result));
        if (httpResult.failed()) {
            fail("Failed to call " + uri, httpResult.cause());
        }
        return httpResult.result();
    }

}
