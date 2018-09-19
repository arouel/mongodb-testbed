package prototype.cqrs;

import org.immutables.value.Value.Immutable;

@Immutable
public abstract class FetchCardSummariesQuery {

    public static FetchCardSummariesQuery of(int size, int offset) {
        return ImmutableFetchCardSummariesQuery.builder().size(size).offset(offset).build();
    }

    public abstract int offset();

    public abstract int size();
}
