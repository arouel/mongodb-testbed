package prototype.todoapp;

import org.immutables.value.Value.Immutable;

import com.google.common.collect.ImmutableList;

import core.Query;

@Immutable
public abstract class ShowTodoChildren implements Query<ImmutableList<Todo>> {

    public abstract TodoId parentTodoId();

}
