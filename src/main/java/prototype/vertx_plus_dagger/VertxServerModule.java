package prototype.vertx_plus_dagger;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;

import javax.inject.Qualifier;
import javax.inject.Singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dagger.Module;
import dagger.Provides;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

@Module
public class VertxServerModule {

    private static final Logger LOG = LogManager.getLogger(VertxServerModule.class);

    @Provides
    @Singleton
    static Vertx vertx() {
        return Vertx.vertx();
    }

    @Provides
    @Singleton
    static Router router(Vertx vertx, Set<RouteSetup> routeDefinitions) {
        Router router = Router.router(Vertx.vertx());
        LOG.info("Defining routes:");
        for (RouteSetup routeSetup : routeDefinitions) {
            Route route = routeSetup.define(router);
            LOG.info("\t" + route.getPath());
        }
        return router;
    }

    @Provides
    @Singleton
    static HttpServer httpServer(Vertx vertx, @HttpServerPort int port, Router router) {
        return vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(port);
    }

    @Qualifier
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface HttpServerPort {
    }
}
