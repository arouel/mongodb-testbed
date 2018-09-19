package prototype.cqrs;

import org.immutables.value.Value.Immutable;

@Immutable
public abstract class IssueCommand {

    public static IssueCommand of(String id, int amount) {
        return ImmutableIssueCommand.builder().id(id).amount(amount).build();
    }

    public abstract int amount();

    public abstract String id();

}
