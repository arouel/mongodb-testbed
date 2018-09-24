package prototype.todoapp.command.handler;

import static java.util.Objects.requireNonNull;

import com.mongodb.MongoClient;
import com.mongodb.client.ClientSession;

import core.CommandHandler;
import core.Result;
import core.Unit;
import prototype.todoapp.CounterRepository;
import prototype.todoapp.TodoRepository;
import prototype.todoapp.command.ResetCollections;
import prototype.todoapp.event.EventRepository;

public class ResetCollectionsHandler implements CommandHandler<ResetCollections, Unit> {

    private final MongoClient _mongoClient;
    private final CounterRepository _counterRepository;
    private final EventRepository _eventRepository;
    private final TodoRepository _todoRepository;

    public ResetCollectionsHandler(
            MongoClient mongoClient,
            CounterRepository counterRepository,
            EventRepository eventRepository,
            TodoRepository todoRepository) {
        _mongoClient = requireNonNull(mongoClient);
        _counterRepository = requireNonNull(counterRepository);
        _eventRepository = requireNonNull(eventRepository);
        _todoRepository = requireNonNull(todoRepository);
    }

    @Override
    public Class<ResetCollections> commandType() {
        return ResetCollections.class;
    }

    @Override
    public Result<Unit> handle(ResetCollections command) {
        try (ClientSession session = _mongoClient.startSession()) {
            try {
                session.startTransaction();
                _counterRepository.findAll().deleteAll().get();
                _eventRepository.findAll().deleteAll().get();
                _todoRepository.findAll().deleteAll().get();
                session.commitTransaction();
                session.close();
                return Result.success(Unit.VALUE);
            } catch (Exception e) {
                session.abortTransaction();
                return Result.unknownFailure(e);
            }
        } catch (Exception e) {
            return Result.unknownFailure(e);
        }
    }
}
