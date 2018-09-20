package prototype.command;

import static prototype.command.Futures.*;

import java.util.function.Function;

import io.vertx.core.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.immutables.mongo.concurrent.FluentFuture;
import org.immutables.mongo.repository.RepositorySetup;

public abstract class Handler {

    private static final Logger logger = LogManager.getLogger(Handler.class);

    private CounterRepository counterRepository() {
        return new CounterRepository(repositorySetup());
    }

    private Future<Unit> createCounter(String collection) {
        FluentFuture<Integer> insert = counterRepository().insert(Counter.of(collection, 0L));
        return toVertxFuture(insert, logger, "created counter '" + collection + "'").map(Unit.VALUE);
    }

    private Future<Long> nextId(String collection) {
        return Counters
                .nextSequence(repositorySetup(), collection)
                .recover(new Function<Throwable, Future<Long>>() {
                    @Override
                    public Future<Long> apply(Throwable t) {
                        return createCounter(collection).compose(unit -> nextId(collection));
                    }
                });
    }

    protected Future<Long> nextTodoId() {
        return nextId(Todo.class.getSimpleName());
    }

    private RepositorySetup repositorySetup() {
        String uri = "mongodb://localhost/test";
        return RepositorySetup.forUri(uri);
    }

    protected TodoRepository todoRepository() {
        return new TodoRepository(repositorySetup());
    }

    Future<Unit> truncate() {
        return toVertxFuture(todoRepository().findAll().deleteAll(), logger, "truncated todo collection")
                .compose(i -> toVertxFuture(counterRepository().findAll().deleteAll(), logger, "truncated counter collection"))
                .map(Unit.VALUE);
    }
}
