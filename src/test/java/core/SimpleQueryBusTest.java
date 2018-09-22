package core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

class SimpleQueryBusTest {

    SimpleQueryBus _queryBus;
    ImmutableList<Query<?>> _querys;

    @BeforeEach
    public void setup() {
        Random random = new Random(1);

        List<Query<?>> querys = new ArrayList<>();
        querys.add(new TestQuery01());
        querys.add(new TestQuery02());
        querys.add(new TestQuery03());
        querys.add(new TestQuery04());
        Collections.shuffle(querys, random);
        _querys = ImmutableList.copyOf(querys);

        @SuppressWarnings("rawtypes")
        List<QueryHandler> queryHandlers = new ArrayList<>();
        queryHandlers.add(new TestQuery01Handler());
        queryHandlers.add(new TestQuery02Handler());
        queryHandlers.add(new TestQuery03Handler());
        queryHandlers.add(new TestQuery04Handler());
        Collections.shuffle(queryHandlers, random);
        _queryBus = new SimpleQueryBus(queryHandlers);
    }

    @Test
    void testQuery() {
        Query<?> query = _querys.get(1);

        assertThat(_queryBus.query(query)).isEqualTo(Result.success(Unit.VALUE));

        // from cache
        assertThat(_queryBus.query(query)).isEqualTo(Result.success(Unit.VALUE));
    }

    @Test
    void testQuery_noHandler() {
        Query<?> query = new Query<Object>() {
        };

        Result<?> result = _queryBus.query(query);
        assertThat(result.cause())
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageStartingWith("Cannot find implementation for 'core.SimpleQueryBus");
    }

    private static class TestQuery01 implements Query<Unit> {
    }

    private static class TestQuery01Handler extends TestQueryHandler<TestQuery01> {
        TestQuery01Handler() {
            super(TestQuery01.class);
        }
    }

    private static class TestQuery02 implements Query<Unit> {
    }

    private static class TestQuery02Handler extends TestQueryHandler<TestQuery02> {
        TestQuery02Handler() {
            super(TestQuery02.class);
        }
    }

    private static class TestQuery03 implements Query<Unit> {
    }

    private static class TestQuery03Handler extends TestQueryHandler<TestQuery03> {
        TestQuery03Handler() {
            super(TestQuery03.class);
        }
    }

    private static class TestQuery04 implements Query<Unit> {
    }

    private static class TestQuery04Handler extends TestQueryHandler<TestQuery04> {
        TestQuery04Handler() {
            super(TestQuery04.class);
        }
    }

    private static abstract class TestQueryHandler<C extends Query<Unit>> implements QueryHandler<C, Unit> {
        final Class<C> _queryType;

        TestQueryHandler(Class<C> queryType) {
            _queryType = queryType;
        }

        @Override
        public Result<Unit> handle(C query) {
            return Result.success(Unit.VALUE);
        }

        @Override
        public Class<C> queryType() {
            return _queryType;
        }
    }

}
