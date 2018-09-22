package prototype;

import org.immutables.value.Value.Parameter;

/**
 * Base wrapper type
 *
 * @param <T> type to wrap
 */
public abstract class Wrapper<T> {

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + value() + ")";
    }

    @Parameter
    public abstract T value();
}
