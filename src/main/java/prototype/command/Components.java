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

import core.CommandBus;
import core.CommandHandler;
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
    public CommandBus commandBus(@SuppressWarnings("rawtypes") Set<CommandHandler> handlers) {
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
    @SuppressWarnings("rawtypes")
    CommandHandler createTodoHandler(Supplier<TodoId> nextTodoId, TodoRepository repository) {
        return new CreateTodoHandler(nextTodoId, repository);
    }

    @Provides
    @Singleton
    @IntoSet
    @SuppressWarnings("rawtypes")
    CommandHandler deleteTodoHandler(TodoRepository repository) {
        return new DeleteTodoHandler(repository);
    }

    @Provides
    @Singleton
    @IntoSet
    @SuppressWarnings("rawtypes")
    CommandHandler editDescriptionHandler(TodoRepository repository) {
        return new EditDescriptionHandler(repository);
    }

    @Provides
    @Singleton
    Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapterFactory(new TypeAdapters());
        for (TypeAdapterFactory factory : ServiceLoader.load(TypeAdapterFactory.class)) {
            gsonBuilder.registerTypeAdapterFactory(factory);
        }
        return gsonBuilder.create();
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
    Supplier<TodoId> nextTodoId(CounterRepository counterRepository) {
        return new Supplier<TodoId>() {
            @Override
            public TodoId get() {
                return nextId(counterRepository, Todo.class.getSimpleName())
                        .map(TodoId::of)
                        .get();
            }
        };
    }

    @Provides
    @Singleton
    public QueryBus queryBus(@SuppressWarnings("rawtypes") Set<QueryHandler> handlers) {
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
                .gson(gson())
                .build();
    }

    @Provides
    @Singleton
    @IntoSet
    @SuppressWarnings("rawtypes")
    CommandHandler resetCollectionsHandler(
            MongoClient mongoClient,
            CounterRepository counterRepository,
            TodoRepository todoRepository) {
        return new ResetCollectionsHandler(mongoClient, counterRepository, todoRepository);
    }

    @Provides
    @Singleton
    @IntoSet
    @SuppressWarnings("rawtypes")
    public QueryHandler showTodoChildrenHandler(TodoRepository repository) {
        return new ShowTodoChildrenHandler(repository);
    }

    @Provides
    @Singleton
    @IntoSet
    @SuppressWarnings("rawtypes")
    public QueryHandler showTodoHandler(TodoRepository repository) {
        return new ShowTodoHandler(repository);
    }

    @Provides
    @Singleton
    @IntoSet
    @SuppressWarnings("rawtypes")
    public QueryHandler showTodoTreeHandler(TodoRepository repository) {
        return new ShowTodoTreeHandler(repository);
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
