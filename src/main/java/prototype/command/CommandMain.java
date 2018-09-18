package prototype.command;

import java.util.concurrent.ExecutionException;

import com.google.common.base.Stopwatch;
import io.vertx.core.Future;
import prototype.command.handler.CommandHandler;

public class CommandMain {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Stopwatch stopwatch = Stopwatch.createStarted();

        CreateTodo createTodoCommand = Command.createTodo("test1", "do something");
        Future<Todo> createTodo = CommandHandler.resolve(createTodoCommand);
        Future<Unit> deleteTodo = createTodo
                .compose(todo -> {
                    EditDescription editDescription = Command.editDescription(todo.id(), "do something else");
                    return CommandHandler.resolve(editDescription);
                })
                .compose(todo -> {
                    EditDescription editDescription = Command.editDescription(todo.id(), "do something totally different");
                    return CommandHandler.resolve(editDescription);
                })
                .compose(todo -> {
                    DeleteTodo deleteTodoCommand = Command.deleteTodo(todo.id());
                    return CommandHandler.resolve(deleteTodoCommand);
                });

        do {
            Thread.sleep(1000);
            System.out.println("successfully completed");
        } while (!deleteTodo.isComplete());

        if (deleteTodo.failed()) {
            deleteTodo.cause().printStackTrace();
        }

        System.out.println(stopwatch);
    }
}
