package prototype.todoapp;

import org.immutables.mongo.repository.RepositorySetup;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.google.common.util.concurrent.MoreExecutors;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.ClientSession;

import prototype.todoapp.event.EventRepository;

class MongoEnv
        implements
        AfterEachCallback,
        BeforeAllCallback {

    private RepositorySetup _configuration;
    private CounterRepository _counterRepository;
    private EventRepository _eventRepository;
    private TodoRepository _todoRepository;
    private MongoClient _mongoClient;

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
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

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        MongoClientURI uri = new MongoClientURI("mongodb://localhost/test");
        _mongoClient = new MongoClient(uri);
        _configuration = RepositorySetup.builder()
                .database(_mongoClient.getDatabase(uri.getDatabase()))
                .executor(MoreExecutors.listeningDecorator(MoreExecutors.newDirectExecutorService()))
                .gson(new Components().gson())
                .build();
        _counterRepository = new CounterRepository(_configuration);
        _eventRepository = new EventRepository(_configuration);
        _todoRepository = new TodoRepository(_configuration);
    }

    RepositorySetup configuration() {
        return _configuration;
    }

    CounterRepository counterRepository() {
        return _counterRepository;
    }

    EventRepository eventRepository() {
        return _eventRepository;
    }

    MongoClient mongoClient() {
        return _mongoClient;
    }

    TodoRepository todoRepository() {
        return _todoRepository;
    }
}
