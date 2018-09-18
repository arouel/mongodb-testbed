package prototype.vertx_plus_dagger;

import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

@FunctionalInterface
public interface RouteDefinition {

    Route define(Router router);

}
