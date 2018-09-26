package prototype.webstack;

import javax.inject.Singleton;

import dagger.Component;
import io.vertx.core.http.HttpServer;

/**
 * Main component of the vertx server.
 */
@Component(modules = { VertxModule.class, CqrsEndpointModule.class })
@Singleton
public interface VertxComponent {

    HttpServer httpServer();

    public static Builder builder() {
        return DaggerVertxComponent.builder();
    }

    @Component.Builder
    interface Builder {

        VertxComponent build();
    }
}
