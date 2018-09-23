package prototype.todoapp;

import org.immutables.gson.Gson.TypeAdapters;
import org.immutables.value.Value.Immutable;

import prototype.Wrapped;
import prototype.Wrapper;

@Immutable
@TypeAdapters
@Wrapped
abstract class _TodoId extends Wrapper<Long> {
}
