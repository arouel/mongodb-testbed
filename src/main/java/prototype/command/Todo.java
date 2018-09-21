package prototype.command;

import com.google.common.base.Optional;
import org.immutables.gson.Gson.TypeAdapters;
import org.immutables.mongo.Mongo.Id;
import org.immutables.mongo.Mongo.Repository;
import org.immutables.value.Value.Auxiliary;
import org.immutables.value.Value.Immutable;

@Immutable
@Repository
@TypeAdapters
public abstract class Todo {

    public static Builder builder() {
        return new Builder();
    }

    public abstract String description();

    @Id
    public abstract long id();

    public abstract Optional<Long> parentId();

    public abstract String title();

    @Auxiliary
    public abstract int version();

    public static final class Builder extends ImmutableTodo.Builder {
    }
}
