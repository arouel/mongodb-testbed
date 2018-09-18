package prototype.command.handler;

import static prototype.command.Futures.*;

import com.google.auto.service.AutoService;
import io.vertx.core.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.immutables.mongo.concurrent.FluentFuture;
import prototype.command.CreateTodo;
import prototype.command.Todo;

@AutoService(CommandHandler.class)
public class CreateTodoImpl extends CommandHandler<CreateTodo, Todo> {

    private static final Logger logger = LogManager.getLogger(CreateTodoImpl.class);

    @Override
    public Class<CreateTodo> commandType() {
        return CreateTodo.class;
    }

    @Override
    public Future<Todo> handle(CreateTodo command) {
        return nextTodoId()
                .map(id -> Todo
                        .builder()
                        .description(command.description())
                        .id(id)
                        .title(command.title())
                        .version(1)
                        .build())
                .compose(document -> {
                    FluentFuture<Integer> insert = todoRepository().insert(document);
                    return toVertxFuture(insert, logger, "created todo").map(document);
                });
    }
}
