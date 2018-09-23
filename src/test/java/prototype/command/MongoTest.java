package prototype.command;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.google.common.base.Optional;

import prototype.command.TodoRepository.Criteria;

class MongoTest {

    @RegisterExtension
    static MongoEnv _env = new MongoEnv();

    @Test
    void testInsertAndDelete() throws InterruptedException, ExecutionException {

        // set up
        TodoId id = TodoId.of(1L);
        Todo document = Todo
                .builder()
                .id(id)
                .title("Todo1")
                .description("Desc1")
                .version(1)
                .build();
        _env.todoRepository().insert(document).get();

        // to test
        _env.todoRepository()
                .findById(id)
                .deleteFirst()
                .get();

        // verify
        Optional<Todo> find = _env.todoRepository()
                .findById(id)
                .fetchFirst()
                .get();
        assertThat(find).isEqualTo(Optional.absent());
    }

    @Test
    void testInsertAndFind_byDescription() throws InterruptedException, ExecutionException {

        // set up
        TodoId id = TodoId.of(1L);
        Todo document = Todo
                .builder()
                .id(id)
                .title("Todo1")
                .description("Desc1")
                .version(1)
                .build();
        _env.todoRepository()
                .insert(document)
                .get();

        // to test
        TodoRepository repository = _env.todoRepository();
        Criteria criteria = repository
                .criteria()
                .description(document.description());
        Optional<Todo> found = repository
                .find(criteria)
                .fetchFirst()
                .get();

        // verify
        assertThat(found).isEqualTo(Optional.of(document));
    }

    @Test
    void testInsertAndFind_byDescriptionAndTitle() throws InterruptedException, ExecutionException {

        // set up
        TodoId id = TodoId.of(1L);
        Todo document = Todo
                .builder()
                .id(id)
                .title("Todo1")
                .description("Desc1")
                .version(1)
                .build();
        _env.todoRepository()
                .insert(document)
                .get();

        // to test
        TodoRepository repository = _env.todoRepository();
        Criteria criteria = repository
                .criteria()
                .description(document.description())
                .title(document.title());
        Optional<Todo> found = repository
                .find(criteria)
                .fetchFirst()
                .get();

        // verify
        assertThat(found).isEqualTo(Optional.of(document));
    }

    @Test
    void testInsertAndFind_byId() throws InterruptedException, ExecutionException {

        // set up
        TodoId id = TodoId.of(1L);
        Todo document = Todo
                .builder()
                .id(id)
                .title("Todo1")
                .description("Desc1")
                .version(1)
                .build();

        // to test
        _env.todoRepository()
                .insert(document)
                .get();

        // verify
        Optional<Todo> find = _env.todoRepository()
                .findById(id)
                .fetchFirst()
                .get();
        assertThat(find).isEqualTo(Optional.of(document));
    }

    @Test
    void testInsertAndUpdate() throws InterruptedException, ExecutionException {

        // set up
        TodoId id = TodoId.of(1L);
        Todo document = Todo
                .builder()
                .id(id)
                .title("Todo1")
                .description("Desc1")
                .version(1)
                .build();
        _env.todoRepository().insert(document).get();

        // to test
        _env.todoRepository()
                .findById(id)
                .andModifyFirst()
                .setTitle("Title1")
                .setDescription("Test1")
                .incrementVersion(3)
                .update()
                .get();

        // verify
        Optional<Todo> find = _env.todoRepository().findById(id).fetchFirst().get();
        assertThat(find).isEqualTo(Optional.of(Todo
                .builder()
                .id(id)
                .title("Title1")
                .description("Test1")
                .version(4)
                .build()));
    }

    @Test
    void testUpsert_insert() throws InterruptedException, ExecutionException {

        // set up
        TodoId id = TodoId.of(1L);
        Todo document = Todo
                .builder()
                .id(id)
                .title("Todo1")
                .description("Desc1")
                .version(1)
                .build();

        // to test
        _env.todoRepository()
                .findById(id)
                .andModifyFirst()
                .setDescription(document.description())
                .setTitle(document.title())
                .incrementVersion(1)
                .upsert()
                .get();

        // verify
        Optional<Todo> find = _env.todoRepository()
                .findById(id)
                .fetchFirst()
                .get();
        assertThat(find).isEqualTo(Optional.of(document));
    }

    @Test
    void testUpsert_update() throws InterruptedException, ExecutionException {

        // set up
        TodoId id = TodoId.of(1L);
        Todo document = Todo
                .builder()
                .id(id)
                .title("Todo1")
                .description("Desc1")
                .version(1)
                .build();
        _env.todoRepository().insert(document).get();

        // to test
        _env.todoRepository()
                .findById(id)
                .andModifyFirst()
                .setDescription("Test1")
                .setTitle("Title1")
                .incrementVersion(1)
                .upsert()
                .get();

        // verify
        Optional<Todo> find = _env.todoRepository().findById(id).fetchFirst().get();
        assertThat(find).isEqualTo(Optional.of(Todo
                .builder()
                .id(TodoId.of(1L))
                .title("Title1")
                .description("Test1")
                .version(2)
                .build()));
    }

}
