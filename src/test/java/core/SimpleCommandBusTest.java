package core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

class SimpleCommandBusTest {

    SimpleCommandBus _commandBus;
    ImmutableList<Command<?>> _commands;

    @BeforeEach
    public void setup() {
        Random random = new Random(1);

        List<Command<?>> commands = new ArrayList<>();
        commands.add(new TestCommand01());
        commands.add(new TestCommand02());
        commands.add(new TestCommand03());
        commands.add(new TestCommand04());
        Collections.shuffle(commands, random);
        _commands = ImmutableList.copyOf(commands);

        @SuppressWarnings("rawtypes")
        List<CommandHandler> commandHandlers = new ArrayList<>();
        commandHandlers.add(new TestCommand01Handler());
        commandHandlers.add(new TestCommand02Handler());
        commandHandlers.add(new TestCommand03Handler());
        commandHandlers.add(new TestCommand04Handler());
        Collections.shuffle(commandHandlers, random);
        _commandBus = new SimpleCommandBus(commandHandlers);
    }

    @Test
    void testCommand() {
        Command<?> command = _commands.get(1);

        assertThat(_commandBus.command(command)).isEqualTo(Result.success(Unit.VALUE));

        // from cache
        assertThat(_commandBus.command(command)).isEqualTo(Result.success(Unit.VALUE));
    }

    @Test
    void testCommand_noHandler() {
        Command<?> command = new Command<Object>() {
        };

        Result<?> result = _commandBus.command(command);
        assertThat(result.cause())
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageStartingWith("Cannot find implementation for 'core.SimpleCommandBus");
    }

    private static class TestCommand01 implements Command<Unit> {
    }

    private static class TestCommand01Handler extends TestCommandHandler<TestCommand01> {
        TestCommand01Handler() {
            super(TestCommand01.class);
        }
    }

    private static class TestCommand02 implements Command<Unit> {
    }

    private static class TestCommand02Handler extends TestCommandHandler<TestCommand02> {
        TestCommand02Handler() {
            super(TestCommand02.class);
        }
    }

    private static class TestCommand03 implements Command<Unit> {
    }

    private static class TestCommand03Handler extends TestCommandHandler<TestCommand03> {
        TestCommand03Handler() {
            super(TestCommand03.class);
        }
    }

    private static class TestCommand04 implements Command<Unit> {
    }

    private static class TestCommand04Handler extends TestCommandHandler<TestCommand04> {
        TestCommand04Handler() {
            super(TestCommand04.class);
        }
    }

    private static abstract class TestCommandHandler<C extends Command<Unit>> implements CommandHandler<C, Unit> {
        final Class<C> _commandType;

        TestCommandHandler(Class<C> commandType) {
            _commandType = commandType;
        }

        @Override
        public Class<C> commandType() {
            return _commandType;
        }

        @Override
        public Result<Unit> handle(C command) {
            return Result.success(Unit.VALUE);
        }
    }

}
