package prototype.todoapp;

import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Stopwatch;

import core.Result;
import core.Unit;

public class CommandMain {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // Log4j JDK Logging Adapter
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");

        Logger logger = LogManager.getLogger(CommandMain.class);

        Stopwatch stopwatch = Stopwatch.createStarted();

        TodoApp app = TodoApp
                .builder()
                .mongoClientUri("mongodb://localhost/test")
                .build();

        Result<Unit> result = app.commandCreateTodo("test1", "do something")
                .flatMap(todoId -> {
                    return app.commandEditDescription(todoId, "do something else");
                })
                .flatMap(todoId -> {
                    return app.commandEditDescription(todoId, "do something totally different");
                })
                .flatMap(todoId -> {
                    return app.commandDeleteTodo(todoId);
                });

        logger.info(stopwatch + ": " + result);
    }
}
