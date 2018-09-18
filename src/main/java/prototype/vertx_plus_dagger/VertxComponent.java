package prototype.vertx_plus_dagger;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import io.vertx.core.http.HttpServer;
import prototype.vertx_plus_dagger.VertxServerModule.HttpServerPort;
import prototype.vertx_plus_dagger.http.CommonHttpEndpoints;
import prototype.vertx_plus_dagger.http.UserHttpEndpoints;

/**
 * Main component of the vertx server.
 */
@Component(modules = { VertxServerModule.class, CommonHttpEndpoints.class, UserHttpEndpoints.class })
@Singleton
public interface VertxComponent {

    HttpServer httpServer();

    public static Builder builder() {
        return DaggerVertxComponent.builder();
    }

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder httpServerPort(@HttpServerPort int httpServerPort);

        VertxComponent build();
    }
}
