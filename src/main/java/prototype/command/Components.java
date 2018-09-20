package prototype.command;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;
import java.util.function.Supplier;

import javax.inject.Qualifier;
import javax.inject.Singleton;

import core.Command;
import core.CommandBus;
import core.CommandHandler;
import core.Query;
import core.QueryBus;
import core.QueryHandler;
import core.Result;
import core.SimpleCommandBus;
import core.SimpleQueryBus;
import core.Unit;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import org.immutables.mongo.repository.RepositorySetup;
import prototype.command.handler.CreateTodoHandler;
import prototype.command.handler.DeleteTodoHandler;
import prototype.command.handler.EditDescriptionHandler;
import prototype.command.handler.ShowTodoHandler;

/**
 * Describes all components of this application
 */
@Module
class Components {

    private static Result<Unit> createCounter(CounterRepository repository, String collection) {
        try {
            repository
                    .insert(Counter.of(collection, 0L))
                    .get();
            return Result.success(Unit.VALUE);
        } catch (Exception e) {
            return Result.unknownFailure(e);
        }
    }

    private static Result<Long> nextId(CounterRepository repository, String collection) {
        return nextSequence(repository, collection)
                .recover(failure -> createCounter(repository, collection)
                        .flatMap(unit -> nextId(repository, collection))
                        .get());
    }

    private static Result<Long> nextSequence(CounterRepository repository, String collection) {
        try {
            Long incrementSequence = repository
                    .findByCollection(collection)
                    .andModifyFirst()
                    .incrementSequence(1)
                    .update()
                    .transform(c -> c.get().sequence())
                    .get();
            return Result.success(incrementSequence);
        } catch (Exception e) {
            return Result.unknownFailure(e);
        }
    }

    @Provides
    @Singleton
    public CommandBus commandBus(Set<CommandHandler<Command<?>, ?>> handlers) {
        return new SimpleCommandBus(handlers);
    }

    @Provides
    @Singleton
    CounterRepository counterRepository(RepositorySetup configuration) {
        return new CounterRepository(configuration);
    }

    @Provides
    @Singleton
    @IntoSet
    @SuppressWarnings("unchecked")
    CommandHandler<Command<?>, ?> createTodoHandler(Supplier<Long> nextTodoId, TodoRepository repository) {
        return CommandHandler.class.cast(new CreateTodoHandler(nextTodoId, repository));
    }

    @Provides
    @Singleton
    @IntoSet
    @SuppressWarnings("unchecked")
    CommandHandler<Command<?>, ?> deleteTodoHandler(TodoRepository repository) {
        return CommandHandler.class.cast(new DeleteTodoHandler(repository));
    }

    @Provides
    @Singleton
    @IntoSet
    @SuppressWarnings("unchecked")
    CommandHandler<Command<?>, ?> editDescriptionHandler(TodoRepository repository) {
        return CommandHandler.class.cast(new EditDescriptionHandler(repository));
    }

    @Provides
    @Singleton
    Supplier<Long> nextTodoId(CounterRepository counterRepository) {
        return new Supplier<Long>() {
            @Override
            public Long get() {
                return nextId(counterRepository, Todo.class.getSimpleName()).get();
            }
        };
    }

    @Provides
    @Singleton
    public QueryBus queryBus(Set<QueryHandler<Query<?>, ?>> handlers) {
        return new SimpleQueryBus(handlers);
    }

    @Provides
    @Singleton
    RepositorySetup repositorySetup(@MongoClientUri String mongoClientUri) {
        return RepositorySetup.forUri(mongoClientUri);
    }

    @Provides
    @Singleton
    @IntoSet
    @SuppressWarnings("unchecked")
    public QueryHandler<Query<?>, ?> showTodoHandler(TodoRepository repository) {
        return QueryHandler.class.cast(new ShowTodoHandler(repository));
    }

    @Provides
    @Singleton
    TodoRepository todoRepository(RepositorySetup configuration) {
        return new TodoRepository(configuration);
    }

    @Qualifier
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface MongoClientUri {
    }
}
