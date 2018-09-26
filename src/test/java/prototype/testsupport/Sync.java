package prototype.testsupport;

import java.util.concurrent.Exchanger;
import java.util.function.Consumer;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public class Sync {

    public static <T> AsyncResult<T> await(Consumer<Handler<AsyncResult<T>>> consumer) throws InterruptedException {
        Exchanger<AsyncResult<T>> resultExchanger = new Exchanger<>();
        consumer.accept(result -> {
            try {
                resultExchanger.exchange(result);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        return resultExchanger.exchange(null);
    }
}
