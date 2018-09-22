package prototype.command;

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

import core.Result;

@Fork(value = 1)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class TodoAppBenchmark {

    TodoApp _app;
    int _index;

    @Benchmark
    public Result<TodoId> commandCreateTodo() {
        _index++;
        return _app.commandCreateTodo("Todo" + _index, "Desc" + _index);
    }

    @Setup(Level.Iteration)
    public void setup() {
        _app = TodoApp
                .builder()
                .mongoClientUri("mongodb://localhost/test")
                .build();
        _app.command(ResetCollections.of());
        _index = 0;
    }
}
