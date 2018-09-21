package prototype.command;

import org.immutables.gson.Gson.TypeAdapters;
import org.immutables.mongo.Mongo.Id;
import org.immutables.mongo.Mongo.Repository;
import org.immutables.value.Value.Immutable;

@Immutable
@Repository
@TypeAdapters
public abstract class Counter {

    static Counter of(String collection, long sequence) {
        return ImmutableCounter.builder().collection(collection).sequence(sequence).build();
    }

    @Id
    abstract String collection();

    abstract long sequence();

}
