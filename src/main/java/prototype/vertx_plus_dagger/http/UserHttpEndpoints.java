package prototype.vertx_plus_dagger.http;

import javax.inject.Singleton;

import com.google.common.net.MediaType;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.vertx.core.http.HttpMethod;
import prototype.vertx_plus_dagger.RouteSetup;

@Module
public class UserHttpEndpoints {

    @Provides
    @Singleton
    @IntoSet
    RouteSetup user() {
        return (router) -> router.route(HttpMethod.GET, "/user/:username")
                .produces(MediaType.PLAIN_TEXT_UTF_8.type())
                .handler(context -> context.response().end("Hi " + context.request().getParam("username")));
    }
}
