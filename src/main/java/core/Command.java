package core;

/**
 * Defines a WRITE operation
 * <p>
 * First of all, firing command is the only way to change the state of our system. Commands are responsible for introducing <b>all</b> changes to the system. If there was no
 * command, the state of the system remains unchanged! Command should not return any value. It is implemented as a pair of classes: Command and CommandHandler. Command is just a
 * plain object that is used by CommandHandler as input value (parameters) for some operation it represents. In this vision command is simply invoking particular operations in
 * Domain Model (not necessarily one operation per command).
 *
 * @param <R> result type
 */
public interface Command<R> {
}
