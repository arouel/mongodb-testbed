package prototype.cqrs;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;

public class GiftCard {

    @AggregateIdentifier
    private String _id;
    private int _remainingValue;

    public GiftCard() {
    }

    @CommandHandler(commandName = "prototype.cqrs.ImmutableIssueCommand")
    public GiftCard(IssueCommand cmd) {
        if (cmd.amount() <= 0)
            throw new IllegalArgumentException("amount <= 0");
        AggregateLifecycle.apply(IssuedEvent.of(cmd.id(), cmd.amount()));
    }

    @EventSourcingHandler
    public void on(IssuedEvent evt) {
        _id = evt.id();
        _remainingValue = evt.amount();
    }

    @CommandHandler(commandName = "prototype.cqrs.ImmutableRedeemCommand")
    public void handle(RedeemCommand cmd) {
        if (cmd.amount() <= 0)
            throw new IllegalArgumentException("amount <= 0");
        if (cmd.amount() > _remainingValue)
            throw new IllegalStateException("amount > remaining value");
        AggregateLifecycle.apply(RedeemedEvent.of(_id, cmd.amount()));
    }

    @EventSourcingHandler
    public void on(RedeemedEvent evt) {
        _remainingValue -= evt.amount();
    }

}
