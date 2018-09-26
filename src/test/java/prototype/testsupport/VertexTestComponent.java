package prototype.testsupport;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import io.vertx.core.http.HttpServer;
import prototype.testsupport.VertexTestComponent.HttpEndpointsModule;
import prototype.webstack.RouteDefinition;
import prototype.webstack.VertxModule;

@Component(modules = { VertxModule.class, HttpEndpointsModule.class })
@Singleton
public interface VertexTestComponent {

    HttpServer httpServer();

    public static Builder builder() {
        return DaggerVertexTestComponent.builder();
    }

    @Component.Builder
    interface Builder {

        Builder httpRouteModule(HttpEndpointsModule httpRouteModule);

        VertexTestComponent build();
    }

    @Module
    public static class HttpEndpointsModule {

        private final Set<RouteDefinition> _routeDefinitions = new HashSet<>();

        public HttpEndpointsModule route(RouteDefinition routeDefinition) {
            _routeDefinitions.add(routeDefinition);
            return this;
        }

        @Provides
        @Singleton
        @ElementsIntoSet
        public Set<RouteDefinition> routeDefinitions() {
            return _routeDefinitions;
        }
    }

}
