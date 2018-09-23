package prototype.command;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.immutables.mongo.repository.RepositorySetup;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.MoreExecutors;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.ClientSession;

import prototype.command.event.EventId;
import prototype.command.event.EventRepository;
import prototype.command.event.Events;
import prototype.command.event.TodoCreated;

@Fork(value = 1)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class MongoInsertBenchmark implements Events {

    RepositorySetup _configuration;
    CounterRepository _counterRepository;
    EventRepository _eventRepository;
    TodoRepository _todoRepository;
    MongoClient _mongoClient;
    int _index;

    @Benchmark
    public Integer insertTodo_withoutTransaction() throws InterruptedException, ExecutionException {
        _index++;

        // insert todo
        TodoId todoId = TodoId.of((long) _index);
        Todo document = Todo
                .builder()
                .id(todoId)
                .title("Todo" + _index)
                .description("Desc" + _index)
                .version(1)
                .build();
        Integer todoInserted = _todoRepository.insert(document).get();

        // insert event
        EventId eventId = EventId.of((long) _index);
        TodoCreated todoCreated = todoCreated(eventId, todoId, Optional.absent(), document.title(), document.description());
        Integer eventInserted = _eventRepository.insert(todoCreated).get();

        return todoInserted + eventInserted;
    }

    @Benchmark
    public Integer insertTodo_withTransaction() throws InterruptedException, ExecutionException {
        _index++;

        try (ClientSession session = _mongoClient.startSession()) {
            try {
                session.startTransaction();

                // insert todo
                TodoId todoId = TodoId.of((long) _index);
                Todo document = Todo
                        .builder()
                        .id(todoId)
                        .title("Todo" + _index)
                        .description("Desc" + _index)
                        .version(1)
                        .build();
                Integer todoInserted = _todoRepository.insert(document).get();

                // insert event
                EventId eventId = EventId.of((long) _index);
                TodoCreated todoCreated = todoCreated(eventId, todoId, Optional.absent(), document.title(), document.description());
                Integer eventInserted = _eventRepository.insert(todoCreated).get();

                session.commitTransaction();

                return todoInserted + eventInserted;
            } catch (Exception e) {
                session.abortTransaction();
                throw e;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Setup(Level.Iteration)
    public void setup() throws Exception {
        MongoClientURI uri = new MongoClientURI("mongodb://localhost/test");

        // set up
        _mongoClient = new MongoClient(uri);
        _configuration = RepositorySetup.builder()
                .database(_mongoClient.getDatabase(uri.getDatabase()))
                .executor(MoreExecutors.listeningDecorator(MoreExecutors.newDirectExecutorService()))
                .gson(new Components().gson())
                .build();
        _counterRepository = new CounterRepository(_configuration);
        _eventRepository = new EventRepository(_configuration);
        _todoRepository = new TodoRepository(_configuration);
        _index = 0;

        // clean database
        try (ClientSession session = _mongoClient.startSession()) {
            try {
                session.startTransaction();
                _counterRepository.findAll().deleteAll().get();
                _eventRepository.findAll().deleteAll().get();
                _todoRepository.findAll().deleteAll().get();
                session.commitTransaction();
                session.close();
                return;
            } catch (Exception e) {
                session.abortTransaction();
                throw e;
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
