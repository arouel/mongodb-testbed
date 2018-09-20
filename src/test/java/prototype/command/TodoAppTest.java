package prototype.command;

import static org.assertj.core.api.Assertions.*;

import core.Result;
import core.Unit;
import org.junit.jupiter.api.Test;

class TodoAppTest implements TodoAppTestSupport {

    @Test
    void testCreateTodo() {

        // set up
        CreateTodo command = createTodo("Lunch", "at the italian place");

        // to test
        Result<Long> result = command(command);

        // verify
        assertThat(result.isSuccess()).as(result.toString()).isTrue();
        long todoId = result.get();
        Result<Todo> query = queryShowTodo(todoId);
        assertThat(query.get().title()).isEqualTo(command.title());
        assertThat(query.get().description()).isEqualTo(command.description());
    }

    @Test
    void testDeleteTodo() {

        // set up
        long todoId = commandCreateTodo("Lunch", "at the italian place").get();

        // to test
        Result<Unit> result = commandDeleteTodo(todoId);

        // verify
        assertThat(result).isEqualTo(Result.success(Unit.VALUE));
    }

    @Test
    void testEditDescription() {

        // set up
        long todoId = commandCreateTodo("Lunch", "at the italian place").get();

        // to test
        Result<Long> result = commandEditDescription(todoId, "at the korean place");

        // verify
        assertThat(result).isEqualTo(Result.success(todoId));
        Result<Todo> query = queryShowTodo(result.get());
        assertThat(query.get().description()).isEqualTo("at the korean place");
    }

}
