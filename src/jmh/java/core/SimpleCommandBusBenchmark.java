package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import com.google.common.collect.ImmutableList;

@Fork(value = 1)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class SimpleCommandBusBenchmark {

    private static final Result<Unit> SUCCESS = Result.success(Unit.VALUE);

    SimpleCommandBus _commandBus;
    ImmutableList<Command<?>> _commands;
    int _index;

    @Setup(Level.Iteration)
    public void setup() {
        Random random = new Random(1);

        List<Command<?>> commands = new ArrayList<>();
        commands.add(new TestCommand01());
        commands.add(new TestCommand02());
        commands.add(new TestCommand03());
        commands.add(new TestCommand04());
        commands.add(new TestCommand05());
        commands.add(new TestCommand06());
        commands.add(new TestCommand07());
        commands.add(new TestCommand08());
        commands.add(new TestCommand09());
        commands.add(new TestCommand10());
        commands.add(new TestCommand11());
        commands.add(new TestCommand12());
        commands.add(new TestCommand13());
        commands.add(new TestCommand14());
        commands.add(new TestCommand15());
        commands.add(new TestCommand16());
        commands.add(new TestCommand17());
        commands.add(new TestCommand18());
        commands.add(new TestCommand19());
        commands.add(new TestCommand20());
        Collections.shuffle(commands, random);
        _commands = ImmutableList.copyOf(commands);

        List<CommandHandler<?, ?>> commandHandlers = new ArrayList<>();
        commandHandlers.add(new TestCommand01Handler());
        commandHandlers.add(new TestCommand02Handler());
        commandHandlers.add(new TestCommand03Handler());
        commandHandlers.add(new TestCommand04Handler());
        commandHandlers.add(new TestCommand05Handler());
        commandHandlers.add(new TestCommand06Handler());
        commandHandlers.add(new TestCommand07Handler());
        commandHandlers.add(new TestCommand08Handler());
        commandHandlers.add(new TestCommand09Handler());
        commandHandlers.add(new TestCommand10Handler());
        commandHandlers.add(new TestCommand11Handler());
        commandHandlers.add(new TestCommand12Handler());
        commandHandlers.add(new TestCommand13Handler());
        commandHandlers.add(new TestCommand14Handler());
        commandHandlers.add(new TestCommand15Handler());
        commandHandlers.add(new TestCommand16Handler());
        commandHandlers.add(new TestCommand17Handler());
        commandHandlers.add(new TestCommand18Handler());
        commandHandlers.add(new TestCommand19Handler());
        commandHandlers.add(new TestCommand20Handler());
        Collections.shuffle(commandHandlers, random);
        @SuppressWarnings("unchecked")
        Iterable<CommandHandler<Command<?>, ?>> handlers = Iterable.class.cast(ImmutableList.builder().addAll(commandHandlers).build());
        _commandBus = new SimpleCommandBus(handlers);
        _index = 0;
    }

    @Benchmark
    public Result<?> command() {
        if (_index >= _commands.size()) {
            _index = 0;
        }
        return _commandBus.command(_commands.get(_index));
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

    private static class TestCommand05 implements Command<Unit> {
    }

    private static class TestCommand05Handler extends TestCommandHandler<TestCommand05> {
        TestCommand05Handler() {
            super(TestCommand05.class);
        }
    }

    private static class TestCommand06 implements Command<Unit> {
    }

    private static class TestCommand06Handler extends TestCommandHandler<TestCommand06> {
        TestCommand06Handler() {
            super(TestCommand06.class);
        }
    }

    private static class TestCommand07 implements Command<Unit> {
    }

    private static class TestCommand07Handler extends TestCommandHandler<TestCommand07> {
        TestCommand07Handler() {
            super(TestCommand07.class);
        }
    }

    private static class TestCommand08 implements Command<Unit> {
    }

    private static class TestCommand08Handler extends TestCommandHandler<TestCommand08> {
        TestCommand08Handler() {
            super(TestCommand08.class);
        }
    }

    private static class TestCommand09 implements Command<Unit> {
    }

    private static class TestCommand09Handler extends TestCommandHandler<TestCommand09> {
        TestCommand09Handler() {
            super(TestCommand09.class);
        }
    }

    private static class TestCommand10 implements Command<Unit> {
    }

    private static class TestCommand10Handler extends TestCommandHandler<TestCommand10> {
        TestCommand10Handler() {
            super(TestCommand10.class);
        }
    }

    private static class TestCommand11 implements Command<Unit> {
    }

    private static class TestCommand11Handler extends TestCommandHandler<TestCommand11> {
        TestCommand11Handler() {
            super(TestCommand11.class);
        }
    }

    private static class TestCommand12 implements Command<Unit> {
    }

    private static class TestCommand12Handler extends TestCommandHandler<TestCommand12> {
        TestCommand12Handler() {
            super(TestCommand12.class);
        }
    }

    private static class TestCommand13 implements Command<Unit> {
    }

    private static class TestCommand13Handler extends TestCommandHandler<TestCommand13> {
        TestCommand13Handler() {
            super(TestCommand13.class);
        }
    }

    private static class TestCommand14 implements Command<Unit> {
    }

    private static class TestCommand14Handler extends TestCommandHandler<TestCommand14> {
        TestCommand14Handler() {
            super(TestCommand14.class);
        }
    }

    private static class TestCommand15 implements Command<Unit> {
    }

    private static class TestCommand15Handler extends TestCommandHandler<TestCommand15> {
        TestCommand15Handler() {
            super(TestCommand15.class);
        }
    }

    private static class TestCommand16 implements Command<Unit> {
    }

    private static class TestCommand16Handler extends TestCommandHandler<TestCommand16> {
        TestCommand16Handler() {
            super(TestCommand16.class);
        }
    }

    private static class TestCommand17 implements Command<Unit> {
    }

    private static class TestCommand17Handler extends TestCommandHandler<TestCommand17> {
        TestCommand17Handler() {
            super(TestCommand17.class);
        }
    }

    private static class TestCommand18 implements Command<Unit> {
    }

    private static class TestCommand18Handler extends TestCommandHandler<TestCommand18> {
        TestCommand18Handler() {
            super(TestCommand18.class);
        }
    }

    private static class TestCommand19 implements Command<Unit> {
    }

    private static class TestCommand19Handler extends TestCommandHandler<TestCommand19> {
        TestCommand19Handler() {
            super(TestCommand19.class);
        }
    }

    private static class TestCommand20 implements Command<Unit> {
    }

    private static class TestCommand20Handler extends TestCommandHandler<TestCommand20> {
        TestCommand20Handler() {
            super(TestCommand20.class);
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
            return SUCCESS;
        }
    }

}
