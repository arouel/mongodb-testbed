package prototype.command;

import javax.inject.Singleton;

import core.Command;
import core.CommandBus;
import core.Query;
import core.QueryBus;
import core.Result;
import dagger.BindsInstance;
import dagger.Component;
import prototype.command.Components.MongoClientUri;

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
     * @return dispatches WRITE operations expressed as {@link Command} instances
     */
    CommandBus commandBus();

    @Override
    default <Q extends Query<R>, R> Result<R> query(Q query) {
        return queryBus().query(query);
    }

    /**
     * @return dispatches READ operations expressed as {@link Query} instances
     */
    QueryBus queryBus();

    @Component.Builder
    interface Builder {

        TodoApp build();

        @BindsInstance
        Builder mongoClientUri(@MongoClientUri String mongoServerUri);
    }
}
