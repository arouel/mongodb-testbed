package prototype.todoapp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

import core.Event;
import core.Result;
import core.Unit;
import prototype.todoapp.command.CreateTodo;
import prototype.todoapp.event.TodoCreated;

class TodoAppTest implements TodoAppTestSupport {

    @Test
    void testCreateTodo() {

        // set up
        CreateTodo command = createTodo("Lunch", "at the italian place");

        // to test
        Result<TodoId> result = command(command);

        // verify
        assertThat(result.isSuccess()).as(result.toString()).isTrue();
        TodoId todoId = result.get();
        Result<Todo> query = queryShowTodo(todoId);
        assertThat(query.get().title()).isEqualTo(command.title());
        assertThat(query.get().description()).isEqualTo(command.description());
        assertThat(events()).extracting(Event::eventType).containsExactly(TodoCreated.class.getName());
    }

    @Test
    void testDeleteTodo() {

        // set up
        TodoId todoId = commandCreateTodo("Lunch", "at the italian place").get();

        // to test
        Result<Unit> result = commandDeleteTodo(todoId);

        // verify
        assertThat(result).isEqualTo(Result.success(Unit.VALUE));
    }

    @Test
    void testEditDescription() {

        // set up
        TodoId todoId = commandCreateTodo("Lunch", "at the italian place").get();

        // to test
        Result<TodoId> result = commandEditDescription(todoId, "at the korean place");

        // verify
        assertThat(result).isEqualTo(Result.success(todoId));
        Result<Todo> query = queryShowTodo(result.get());
        assertThat(query.get().description()).isEqualTo("at the korean place");
    }

    @Test
    void testShowTodo() {

        // set up
        TodoId todoId = commandCreateTodo("Lunch", "at the italian place").get();

        // to test
        Result<Todo> result = queryShowTodo(todoId);

        // verify
        assertThat(result.get().title()).isEqualTo("Lunch");
        assertThat(result.get().description()).isEqualTo("at the italian place");
    }

    @Test
    void testShowTodoChildren() {

        // set up
        TodoId todoId1 = commandCreateTodo("Lunch", "at the italian place").get();
        TodoId todoId2 = commandCreateTodo("Lunch", "at the italian place", todoId1).get();
        TodoId todoId3 = commandCreateTodo("Lunch", "at the italian place", todoId1).get();

        // to test
        Result<ImmutableList<Todo>> result = queryShowTodoChildren(todoId1);

        // verify
        assertThat(result.get()).containsOnly(
                queryShowTodo(todoId2).get(),
                queryShowTodo(todoId3).get());
    }

    @Test
    void testShowTodoTree() {

        // set up
        TodoId todoId1 = commandCreateTodo("Todo 1", "desc 1").get();
        TodoId todoId2 = commandCreateTodo("Todo 2", "desc 2", todoId1).get();
        TodoId todoId3 = commandCreateTodo("Todo 3", "desc 3", todoId1).get();
        TodoId todoId4 = commandCreateTodo("Todo 4", "desc 4", todoId2).get();
        TodoId todoId5 = commandCreateTodo("Todo 5", "desc 5", todoId3).get();
        TodoId todoId6 = commandCreateTodo("Todo 6", "desc 6", todoId4).get();
        TodoId todoId7 = commandCreateTodo("Todo 7", "desc 7", todoId3).get();

        // to test
        Result<Node<Todo>> result = queryShowTodoTree(todoId1);

        // verify
        Node<TodoId> node = result.get().map(Todo::id);

        assertThat(node.allValues()).containsExactly(todoId1, todoId2, todoId4, todoId6, todoId3, todoId5, todoId7);
        assertThat(node.allChildValues()).containsExactly(todoId2, todoId4, todoId6, todoId3, todoId5, todoId7);
        assertThat(node.childValues()).containsExactly(todoId2, todoId3);
    }

}
