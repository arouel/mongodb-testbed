package prototype.todoapp.handler;

import static java.util.Objects.requireNonNull;

import com.mongodb.MongoClient;
import com.mongodb.client.ClientSession;

import core.CommandHandler;
import core.Result;
import core.Unit;
import prototype.todoapp.CounterRepository;
import prototype.todoapp.ResetCollections;
import prototype.todoapp.TodoRepository;

public class ResetCollectionsHandler implements CommandHandler<ResetCollections, Unit> {

    private final MongoClient _mongoClient;
    private final CounterRepository _counterRepository;
    private final TodoRepository _todoRepository;

    public ResetCollectionsHandler(
            MongoClient mongoClient,
            CounterRepository counterRepository,
            TodoRepository todoRepository) {
        _mongoClient = requireNonNull(mongoClient);
        _counterRepository = requireNonNull(counterRepository);
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
