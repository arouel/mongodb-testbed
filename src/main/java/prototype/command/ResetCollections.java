package prototype.command;

import org.immutables.value.Value.Immutable;

import core.Command;
import core.Unit;

@Immutable(singleton = true)
public abstract class ResetCollections implements Command<Unit> {
    public static ResetCollections of() {
        return ImmutableResetCollections.singleton();
    }
}
