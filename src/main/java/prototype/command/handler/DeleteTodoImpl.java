package prototype.command.handler;

import static prototype.command.Futures.*;

import com.google.auto.service.AutoService;
import io.vertx.core.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.immutables.mongo.concurrent.FluentFuture;
import prototype.command.DeleteTodo;
import prototype.command.Unit;

@AutoService(CommandHandler.class)
public class DeleteTodoImpl extends CommandHandler<DeleteTodo, Unit> {

    private static final Logger logger = LogManager.getLogger(DeleteTodoImpl.class);

    @Override
    public Class<DeleteTodo> commandType() {
        return DeleteTodo.class;
    }

    @Override
    public Future<Unit> handle(DeleteTodo command) {
        FluentFuture<Integer> delete = todoRepository().findById(command.todoId()).deleteAll();
        return toVertxFuture(delete, logger, "deleted todo").map(Unit.VALUE);
    }
}
