package prototype.vertx_plus_dagger;

import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

@FunctionalInterface
public interface RouteSetup {

    Route define(Router router);

}
