package prototype;

import java.util.Optional;
import java.util.Set;

import org.immutables.gson.Gson;
import org.immutables.mongo.Mongo;
import org.immutables.value.Value;

@Value.Immutable
@Mongo.Repository("items")
@Gson.TypeAdapters
abstract class Item {

    @Mongo.Id
    abstract long id();

    abstract String name();

    abstract Set<Integer> values();

    abstract Optional<String> comment();
}
