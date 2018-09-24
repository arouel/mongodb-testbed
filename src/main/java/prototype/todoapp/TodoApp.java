package prototype.todoapp;

import javax.inject.Singleton;

import com.google.gson.Gson;

import core.Command;
import core.CommandBus;
import core.Query;
import core.QueryBus;
import core.Result;
import dagger.BindsInstance;
import dagger.Component;
import prototype.todoapp.Components.MongoClientUri;
import prototype.todoapp.event.TodoEventStore;

@Component(modules = Components.class)
@Singleton
public interface TodoApp extends Operations {

    static Builder builder() {
        return DaggerTodoApp.builder();
    }

    @Override
    default <C extends Command<R>, R> Result<R> command(C command) {
        return commandBus().command(command);
    }

    /**
     * @return gateway to dispatch WRITE operations
     */
    CommandBus commandBus();

    /**
     * @return event store
     */
    TodoEventStore eventStore();

    /**
     * @return instance for serializing/deserializing a domain model to/from JSON
     */
    Gson gson();

    @Override
    default <Q extends Query<R>, R> Result<R> query(Q query) {
        return queryBus().query(query);
    }

    /**
     * @return gateway to dispatch READ operations
     */
    QueryBus queryBus();

    @Component.Builder
    interface Builder {

        TodoApp build();

        @BindsInstance
        Builder mongoClientUri(@MongoClientUri String mongoServerUri);
    }
}
