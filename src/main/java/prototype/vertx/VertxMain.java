package prototype.vertx;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.net.MediaType;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * Simplistic Vertx HTTP showcase. Access defined end points by visiting http://localhost:8080/.
 */
public class VertxMain {

    private static final Logger LOG = LogManager.getLogger(VertxMain.class);

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();
        Router router = Router.router(vertx);

        StaticHandler staticHandler = StaticHandler.create("src/main/webapp");
        router.route(HttpMethod.GET, "/").handler(context -> context.reroute("/static"));
        router.route("/static/*").handler(staticHandler);

        // Simple route
        router.route(HttpMethod.GET, "/welcome").handler(context -> context.response().end("Hi there, this is Vertx!"));

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

        Handler<ServerWebSocket> websocketHandler = new Handler<ServerWebSocket>() {
            @Override
            public void handle(ServerWebSocket websocket) {
                if (!websocket.path().equals("/websocket")) {
                    websocket.reject();
                    return;
                }

                websocket.accept();

                vertx.setPeriodic(1000, timerId -> {
                    try {
                        websocket.writeTextMessage(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                    } catch (Exception writeException) {
                        vertx.cancelTimer(timerId);
                    }
                });
            }
        };
        HttpServer httpServer = Vertx.vertx()
                .createHttpServer()
                .requestHandler(router::accept)
                .websocketHandler(websocketHandler)
                .listen(8080);
        LOG.info("Started HTTP server on port " + httpServer.actualPort());
    }
}
