package prototype.command;

import com.google.common.util.concurrent.FutureCallback;
import io.vertx.core.Future;
import org.apache.logging.log4j.Logger;
import org.immutables.mongo.concurrent.FluentFuture;

public final class Futures {

    public static <T> Future<T> toVertxFuture(FluentFuture<T> f, Logger logger, String successMessage) {
        Future<T> future = Future.future();
        f.addCallback(new FutureCallback<T>() {

            @Override
            public void onFailure(Throwable t) {
                logger.error(t);
                future.fail(t);
            }

            @Override
            public void onSuccess(T result) {
                logger.info(successMessage + ": " + result);
                future.complete(result);
            }
        });
        return future;
    }
}
