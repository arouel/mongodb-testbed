package prototype.command;

import java.util.ArrayList;
import java.util.List;
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

import com.google.common.util.concurrent.MoreExecutors;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.ClientSession;

import prototype.command.event.EventRepository;
import prototype.command.event.Events;

@Fork(value = 1)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class MongoFindBenchmark implements Events {

    RepositorySetup _configuration;
    CounterRepository _counterRepository;
    EventRepository _eventRepository;
    TodoRepository _todoRepository;
    MongoClient _mongoClient;
    int _index;
    final int _todoCount = 100_000;

    @Benchmark
    public Todo findTodo_withoutTransaction() throws InterruptedException, ExecutionException {
        if (_index < _todoCount) {
            _index++;
        } else {
            _index = 0;
        }

        // find todo
        return _todoRepository.findById(TodoId.of((long) _index)).fetchFirst().get().get();
    }

    @Benchmark
    public Todo findTodo_withTransaction() throws InterruptedException, ExecutionException {
        if (_index < _todoCount) {
            _index++;
        } else {
            _index = 0;
        }

        try (ClientSession session = _mongoClient.startSession()) {
            try {
                session.startTransaction();

                // find todo
                Todo todo = _todoRepository.findById(TodoId.of((long) _index)).fetchFirst().get().get();

                session.commitTransaction();

                return todo;
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
            } catch (Exception e) {
                session.abortTransaction();
                throw e;
            }
        } catch (Exception e) {
            throw e;
        }

        // insert todos
        List<Todo> documents = new ArrayList<>(_todoCount);
        for (int i = 0; i < _todoCount; i++) {
            TodoId todoId = TodoId.of((long) i);
            Todo document = Todo
                    .builder()
                    .id(todoId)
                    .title("Todo" + i)
                    .description("Desc" + i)
                    .version(1)
                    .build();
            documents.add(document);
        }
        _todoRepository.insert(documents).get();
    }
}
