package prototype.cqrs;

import java.util.concurrent.ExecutionException;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.Configuration;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.responsetypes.ResponseTypes;

public class CqrsMain {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // Configuration
        CardSummaryProjection projection = new CardSummaryProjection();
        EventHandlingConfiguration eventHandlingConfiguration = new EventHandlingConfiguration();
        eventHandlingConfiguration.registerEventHandler(c -> projection);
        Configuration configuration = DefaultConfigurer.defaultConfiguration()
                .configureAggregate(GiftCard.class)
                .configureEventStore(c -> new EmbeddedEventStore(new InMemoryEventStorageEngine()))
                .registerModule(eventHandlingConfiguration)
                .registerQueryHandler(c -> projection)
                .buildConfiguration();
        configuration.start();

        // Running the application
        CommandGateway commandGateway = configuration.commandGateway();
        QueryGateway queryGateway = configuration.queryGateway();

        commandGateway.sendAndWait(IssueCommand.of("gc1", 100));
        commandGateway.sendAndWait(IssueCommand.of("gc2", 50));
        commandGateway.sendAndWait(RedeemCommand.of("gc1", 10));
        commandGateway.sendAndWait(RedeemCommand.of("gc2", 20));

        queryGateway.query(FetchCardSummariesQuery.of(2, 0), ResponseTypes.multipleInstancesOf(CardSummary.class))
                .get()
                .forEach(System.out::println);
    }

}
