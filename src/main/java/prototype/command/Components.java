package prototype.command;

import static com.google.common.base.Preconditions.checkArgument;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.Nullable;
import javax.inject.Qualifier;
import javax.inject.Singleton;

import org.immutables.mongo.repository.RepositorySetup;
import org.immutables.mongo.types.TypeAdapters;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

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
import prototype.command.handler.CreateTodoHandler;
import prototype.command.handler.DeleteTodoHandler;
import prototype.command.handler.EditDescriptionHandler;
import prototype.command.handler.ResetCollectionsHandler;
import prototype.command.handler.ShowTodoChildrenHandler;
import prototype.command.handler.ShowTodoHandler;
import prototype.command.handler.ShowTodoTreeHandler;

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

    private static ListeningExecutorService newDirectExecutor() {
        return MoreExecutors.listeningDecorator(MoreExecutors.newDirectExecutorService());
    }

    private static Gson newGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        // there are no longer auto-registered from class-path, but from here or if added manually
        gsonBuilder.registerTypeAdapterFactory(new TypeAdapters());
        for (TypeAdapterFactory factory : ServiceLoader.load(TypeAdapterFactory.class)) {
            gsonBuilder.registerTypeAdapterFactory(factory);
        }
        return gsonBuilder.create();
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
    MongoClient mongoClient(MongoClientURI uri) {
        return new MongoClient(uri);
    }

    @Provides
    @Singleton
    MongoClientURI mongoClientUri(@MongoClientUri String mongoClientUri) {
        return new MongoClientURI(mongoClientUri);
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
    RepositorySetup repositorySetup(MongoClient mongoClient, MongoClientURI uri) {
        @Nullable
        String databaseName = uri.getDatabase();
        checkArgument(databaseName != null, "URI should contain database path segment");
        return RepositorySetup
                .builder()
                .database(mongoClient.getDatabase(databaseName))
                .executor(newDirectExecutor())
                .gson(newGson())
                .build();
    }

    @Provides
    @Singleton
    @IntoSet
    @SuppressWarnings("unchecked")
    CommandHandler<Command<?>, ?> resetCollectionsHandler(
            MongoClient mongoClient,
            CounterRepository counterRepository,
            TodoRepository todoRepository) {
        return CommandHandler.class.cast(new ResetCollectionsHandler(mongoClient, counterRepository, todoRepository));
    }

    @Provides
    @Singleton
    @IntoSet
    @SuppressWarnings("unchecked")
    public QueryHandler<Query<?>, ?> showTodoChildrenHandler(TodoRepository repository) {
        return QueryHandler.class.cast(new ShowTodoChildrenHandler(repository));
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
    @IntoSet
    @SuppressWarnings("unchecked")
    public QueryHandler<Query<?>, ?> showTodoTreeHandler(TodoRepository repository) {
        return QueryHandler.class.cast(new ShowTodoTreeHandler(repository));
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
