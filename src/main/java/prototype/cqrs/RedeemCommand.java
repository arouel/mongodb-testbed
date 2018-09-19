package prototype.cqrs;

import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.immutables.value.Value.Immutable;

@Immutable
public abstract class RedeemCommand {

    public static RedeemCommand of(String id, int amount) {
        return ImmutableRedeemCommand.builder().id(id).amount(amount).build();
    }

    public abstract int amount();

    @TargetAggregateIdentifier
    public abstract String id();

}
