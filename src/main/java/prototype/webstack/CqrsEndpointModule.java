package prototype.webstack;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.vertx.core.http.HttpMethod;
import prototype.vertx_plus_dagger.RouteDefinition;

@Module
public class CqrsEndpointModule {

    @Provides
    @Singleton
    @IntoSet
    RouteDefinition root() {
        return router -> router.route(HttpMethod.GET, "/")
                .handler(context -> context.response().end("Hi there, this is Dagger/Vertx!"));
    }
}
