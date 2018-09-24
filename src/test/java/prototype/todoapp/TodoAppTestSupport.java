package prototype.todoapp;

import java.util.List;

import org.junit.jupiter.api.extension.RegisterExtension;

import core.Command;
import core.Event;
import core.Query;
import core.Result;
import prototype.todoapp.event.Events;

public interface TodoAppTestSupport extends Events, Operations {

    @RegisterExtension
    static final TodoAppEnv _env = new TodoAppEnv();

    @Override
    default <C extends Command<R>, R> Result<R> command(C command) {
        return _env.app().commandBus().command(command);
    }

    default List<Event> events() {
        return _env.app().eventStore().events();
    }

    @Override
    default <Q extends Query<R>, R> Result<R> query(Q query) {
        return _env.app().queryBus().query(query);
    }

}
