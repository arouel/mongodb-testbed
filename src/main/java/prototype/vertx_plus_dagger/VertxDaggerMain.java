package prototype.vertx_plus_dagger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.vertx.core.http.HttpServer;

public class VertxDaggerMain {

    private static final Logger LOG = LogManager.getLogger(VertxDaggerMain.class);

    public static void main(String[] args) {
        VertxComponent vertxComponent = VertxComponent.builder()
                .httpServerPort(8080)
                .build();
        HttpServer httpServer = vertxComponent.httpServer();
        LOG.info("Started http server on port " + httpServer.actualPort());
    }
}
