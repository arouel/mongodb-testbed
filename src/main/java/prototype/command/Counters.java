package prototype.command;

import io.vertx.core.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.immutables.mongo.concurrent.FluentFuture;
import org.immutables.mongo.repository.RepositorySetup;

/**
 * Provides an auto-incrementing sequence for a specified collection.
 * <p>
 * MongoDB has no auto-increment functionality as various relational databases.
 */
public class Counters {

    private static final Logger logger = LogManager.getLogger(Counters.class);

    public static Future<Long> nextSequence(RepositorySetup configuration, String collection) {
        CounterRepository repository = new CounterRepository(configuration);
        FluentFuture<Long> incrementSequence = repository
                .findByCollection(collection)
                .andModifyFirst()
                .incrementSequence(1)
                .update()
                .transform(c -> c.get().sequence());
        return Futures.toVertxFuture(incrementSequence, logger, "next sequence for " + collection);
    }
}
