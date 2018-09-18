package prototype.vertx;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.net.MediaType;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

/**
 * Simplistic Vertx HTTP showcase. Access defined end points by visiting http://localhost:8080/.
 */
public class VertxMain {

    private static final Logger LOG = LogManager.getLogger(VertxMain.class);

    public static void main(String[] args) {

        Router router = Router.router(Vertx.vertx());

        // Simple route
        router.route(HttpMethod.GET, "/").handler(context -> context.response().end("Hi there, this is Vertx!"));

        // Simple route with parameterized URL
        router.route(HttpMethod.GET, "/user/:username")
                .produces(MediaType.PLAIN_TEXT_UTF_8.type())
                .handler(context -> context.response().end("Hi " + context.request().getParam("username")));

        // Route blocking response handler
        router.route(HttpMethod.GET, "/blocking").blockingHandler(context -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            context.response().end("I slept, i'm blocking...");
        });

        HttpServer httpServer = Vertx.vertx()
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(8080);
        LOG.info("Started HTTP server on port " + httpServer.actualPort());
    }
}
