package prototype.cqrs;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;

public class CardSummaryProjection {

    private final List<CardSummary> cardSummaries = new CopyOnWriteArrayList<>();

    @QueryHandler
    public List<CardSummary> fetch(FetchCardSummariesQuery query) {
        return cardSummaries.stream()
                .skip(query.offset())
                .limit(query.size())
                .collect(Collectors.toList());
    }

    @EventHandler
    public void on(IssuedEvent evt) {
        CardSummary cardSummary = CardSummary.of(evt.id(), evt.amount(), evt.amount());
        cardSummaries.add(cardSummary);
    }

    @EventHandler
    public void on(RedeemedEvent evt) {
        cardSummaries.stream()
                .filter(cs -> evt.id().equals(cs.id()))
                .findFirst()
                .ifPresent(cardSummary -> {
                    CardSummary updatedCardSummary = cardSummary.deductAmount(evt.amount());
                    cardSummaries.remove(cardSummary);
                    cardSummaries.add(updatedCardSummary);
                });
    }
}
