package prototype.todoapp;

import org.junit.jupiter.api.extension.RegisterExtension;

import core.Command;
import core.Query;
import core.Result;

public interface TodoAppTestSupport extends Operations {

    @RegisterExtension
    static final TodoAppEnv _env = new TodoAppEnv();

    @Override
    default <C extends Command<R>, R> Result<R> command(C command) {
        return _env.app().commandBus().command(command);
    }

    @Override
    default <Q extends Query<R>, R> Result<R> query(Q query) {
        return _env.app().queryBus().query(query);
    }

}
