package prototype.cqrs;

import org.immutables.value.Value.Immutable;

@Immutable
public abstract class CardSummary {

    public CardSummary deductAmount(int toBeDeducted) {
        return of(id(), initialAmount(), remainingAmount() - toBeDeducted);
    }

    public abstract String id();

    public abstract int initialAmount();

    public static CardSummary of(String id, int initialAmount, int remainingAmount) {
        return ImmutableCardSummary
                .builder()
                .id(id)
                .initialAmount(initialAmount)
                .remainingAmount(remainingAmount)
                .build();
    }

    public abstract int remainingAmount();
}
