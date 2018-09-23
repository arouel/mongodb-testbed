package core;

import org.immutables.gson.Gson.TypeAdapters;
import org.immutables.value.Value.Immutable;

import prototype.Wrapped;
import prototype.Wrapper;

@Immutable
@TypeAdapters
@Wrapped
abstract class _EventId extends Wrapper<Long> {
}
