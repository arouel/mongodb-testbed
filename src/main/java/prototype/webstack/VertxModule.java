package prototype.webstack;

import java.util.Collections;
import java.util.Set;

import javax.inject.Singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

@Module
public final class VertxModule {

    private static final Logger LOG = LogManager.getLogger(VertxModule.class);

    @Provides
    @Singleton
    static Vertx vertx() {
        return Vertx.vertx();
    }

    @Provides
    @Singleton
    static Router router(Vertx vertx, Set<RouteDefinition> routeDefinitions) {
        Router router = Router.router(Vertx.vertx());
        LOG.info("Defining routes:");
        for (RouteDefinition routeDefinition : routeDefinitions) {
            Route route = routeDefinition.define(router);
            LOG.info("\t" + route.getPath());
        }
        return router;
    }

    @Provides
    @Singleton
    static HttpServerOptions httpServerOptions() {
        return new HttpServerOptions()
                .setCompressionSupported(true);
    }

    @Provides
    @Singleton
    static HttpServer httpServer(Vertx vertx, HttpServerOptions httpServerOptions, Router router) {
        return vertx
                .createHttpServer(httpServerOptions)
                .requestHandler(router::accept);
    }

    @Provides
    @Singleton
    @ElementsIntoSet
    static Set<RouteDefinition> defaultRoutes() {
        // by default we have no routes
        return Collections.emptySet();
    }
}
