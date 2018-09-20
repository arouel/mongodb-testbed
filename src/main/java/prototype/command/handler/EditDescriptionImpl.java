package prototype.command.handler;

import static prototype.command.Futures.*;

import com.google.auto.service.AutoService;
import com.google.common.base.Optional;
import io.vertx.core.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.immutables.mongo.concurrent.FluentFuture;
import prototype.command.EditDescription;
import prototype.command.Todo;

@AutoService(CommandHandler.class)
public class EditDescriptionImpl extends CommandHandler<EditDescription, Todo> {

    private static final Logger logger = LogManager.getLogger(EditDescriptionImpl.class);

    @Override
    public Class<EditDescription> commandType() {
        return EditDescription.class;
    }

    @Override
    public Future<Todo> handle(EditDescription command) {
        FluentFuture<Optional<Todo>> delete = todoRepository()
                .findById(command.todoId())
                .andModifyFirst()
                .setDescription(command.description())
                .update();
        return toVertxFuture(delete, logger, "edited description").map(Optional::get);
    }
}
