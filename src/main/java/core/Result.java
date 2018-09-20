package core;

import static com.google.common.base.Preconditions.*;
import static com.google.common.base.Verify.*;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.base.Functions;
import com.google.common.base.Optional;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableList;

/**
 * A container object that represents the result of an operation which may not contain a non-null value. If a result is present, {@code isPresent()} will return {@code true}. Be
 * careful because a present result could be a {@link Failure failure} or {@link Success success} case. If a value is successfully resolved, {@code isSuccess()} will return
 * {@code true} and {@code get()} will return the value. An absent result can be checked with {@link #isAbsent()}.
 *
 * <p>
 * Additional methods that depend on the presence or absence, success or failure, of a result are provided, such as {@link #orElse(java.lang.Object) orElse()},
 * {@link #resolveIfAbsent(Object)}, {@link #recover(Function)} etc.
 *
 * <p>
 * This is a <a href="https://docs.oracle.com/javase/8/docs/api/java/lang/doc-files/ValueBased.html"> value-based</a> class; use of identity-sensitive operations (including
 * reference equality ( {@code ==}), identity hash code, or synchronization) on instances of {@code Result} may have unpredictable results and should be avoided.
 *
 * @param <T> the type of instance that can be contained. {@code Result} is naturally covariant on this type, so it is safe to cast an {@code Result<T>} to {@code Result<S>} for
 * any supertype {@code S} of {@code T}.
 */
public abstract class Result<T> {

    private Result() {
        // avoid custom sub types of this class by keeping the constructor private
    }

    /**
     * Returns an {@code Result} with no contained reference.
     *
     * @param <T> the parametric type in case of success
     * @return an {@code Result} without a value
     */
    @SuppressWarnings("unchecked")
    public static <T> Result<T> absent() {
        return (Result<T>) Absent.instance;
    }

    /**
     * Substitutes each {@code %s} in {@code template} with an argument. These are matched by position: the first {@code %s} gets {@code args[0]}, etc. If there are more arguments
     * than placeholders, the unmatched arguments will be appended to the end of the formatted message in square braces.
     *
     * @param template a non-null string containing 0 or more {@code %s} placeholders.
     * @param args the arguments to be substituted into the message template. Arguments are converted to strings using {@link String#valueOf(Object)}. Arguments can be null.
     * @return formatted message
     */
    private static String format(String template, @Nullable final Object... args) {
        template = String.valueOf(template); // null -> "null"

        // start substituting the arguments into the '%s' placeholders
        StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;
        int i = 0;
        while (i < args.length) {
            int placeholderStart = template.indexOf("%s", templateStart);
            if (placeholderStart == -1) {
                break;
            }
            builder.append(template.substring(templateStart, placeholderStart));
            builder.append(args[i++]);
            templateStart = placeholderStart + 2;
        }
        builder.append(template.substring(templateStart));

        // if we run out of placeholders, append the extra args in square braces
        if (i < args.length) {
            builder.append(" [");
            builder.append(args[i++]);
            while (i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }
            builder.append(']');
        }

        return builder.toString();
    }

    /**
     * If {@code nullableReference} is non-null, returns an {@code Result} instance containing that reference, otherwise returns {@link #absent}.
     *
     * @param nullableReference
     * @return new result instance
     */
    public static <T> Result<T> fromNullable(@Nullable T nullableReference) {
        return (nullableReference == null)
                ? Result.<T>absent()
                : Result.<T>success(nullableReference);
    }

    /**
     * Returns a function that applies {@code function} to a {@link #success(Object) present} input value. An {@link #absent() absent} input value will be passed through without
     * applying the given {@code function}.
     *
     * @param function function to apply on a present value
     * @return function
     */
    public static <F, T> Function<Result<F>, Result<T>> mapIfSuccess(Function<? super F, T> function) {
        return new Function<Result<F>, Result<T>>() {
            @Override
            @SuppressWarnings("unchecked")
            public Result<T> apply(Result<F> input) {
                if (input.isSuccess()) {
                    return Result.success(function.apply(input.get()));
                }
                return (Result<T>) input;
            }
        };
    }

    /**
     * Returns a {@code Result} instance which represents that a entity cannot be found. No value is present for this {@code Result}.
     *
     * @param failure {@link NotFoundOrNotAccessibleFailure} which represents the content
     * @param <T> Type of the non-existent value
     * @return a {@code Result} that represents a entity that cannot be found
     */
    public static <T> Result<T> notFoundOrNotAccessible(RuntimeException failure) {
        return new NotFoundOrNotAccessible<>(failure);
    }

    /**
     * Returns a {@code Result} instance which represents that a entity cannot be found. No value is present for this {@code Result}.
     *
     * @param messageTemplate message template why <code>T</code> cannot be found or is not accessible
     * @param messageArgs the arguments to be substituted into the message template. Arguments are converted to strings using {@link String#valueOf(Object)}.
     * @param <T> Type of the non-existent value
     * @return a {@code Result} that represents a entity that cannot be found
     */
    public static <T> Result<T> notFoundOrNotAccessible(String messageTemplate, final Object... messageArgs) {
        return notFoundOrNotAccessible(new RuntimeException(format(messageTemplate, messageArgs)));
    }

    /**
     * Returns a {@code Result} instance which represents that a entity is not modifiable. No value is present for this {@code Result}.
     *
     * @param failure {@link NotModifiableFailure} which represents the content
     * @param <T> Type of the non-existent value
     * @return a {@code Result} that represents a entity that is not modifiable
     */
    public static <T> Result<T> notModifiable(RuntimeException failure) {
        return new NotModifiable<>(failure);
    }

    /**
     * Returns a {@code Result} instance which represents that a entity is not modifiable. No value is present for this {@code Result}.
     *
     * @param messageTemplate message template why <code>T</code> is not modifiable
     * @param messageArgs the arguments to be substituted into the message template. Arguments are converted to strings using {@link String#valueOf(Object)}.
     * @param <T> Type of the non-existent value
     * @return a {@code Result} that represents a entity that is not modifiable
     */
    public static <T> Result<T> notModifiable(String messageTemplate, final Object... messageArgs) {
        return notModifiable(new RuntimeException(format(messageTemplate, messageArgs)));
    }

    /**
     * Returns a {@code Result} instance which represents that an operation cannot be performed, because a requirement is not met.
     *
     * @param failure {@link RequirementNotMetFailure} which represents the content
     * @param <T> Type of the non-existent value
     * @return a {@code Result} that represents that a requirement is not met
     */
    public static <T> Result<T> requirementNotMet(RuntimeException failure) {
        return new RequirementNotMet<>(failure);
    }

    /**
     * Returns a {@code Result} instance which represents that an operation cannot be performed, because a requirement is not met.
     *
     * @param messageTemplate message template why <code>T</code> cannot be found or is not accessible
     * @param messageArgs the arguments to be substituted into the message template. Arguments are converted to strings using {@link String#valueOf(Object)}.
     * @param <T> Type of the non-existent value
     * @return a {@code Result} that represents a entity that cannot be found
     */
    public static <T> Result<T> requirementNotMet(String messageTemplate, final Object... messageArgs) {
        return requirementNotMet(new RuntimeException(format(messageTemplate, messageArgs)));
    }

    /**
     * Creates a new {@code Result} which provides the successful results of any given {@code Result} instances or the first failure result. Any {@link #absent() absent} result
     * will be skipped.
     *
     * @param results
     * @return a {@code Result} instance with all successfully resolved values or the first failure result
     */
    @SuppressWarnings("unchecked")
    public static <E> Result<Iterable<E>> sequence(Iterable<? extends Result<E>> results) {
        checkNotNull(results, "Argument 'tasks' must not be null");
        ImmutableList.Builder<E> values = ImmutableList.builder();
        for (Result<E> r : results) {
            if (r.isSuccess()) {
                values.add(r.get());
            }
            if (r instanceof Failure) {
                return (Result<Iterable<E>>) r;
            }
        }
        return Result.<Iterable<E>>success(values.build());
    }

    /**
     * Creates a new {@code Result} which provides the successful results of any given {@code Result} instances or the first failure result. Any {@link #absent() absent} result
     * will be skipped.
     *
     * @param results
     * @return a {@code Result} instance with all successfully resolved values or the first failure result
     */
    @SafeVarargs
    public static <E> Result<Iterable<E>> sequence(Result<E>... results) {
        return sequence(ImmutableList.copyOf(results));
    }

    /**
     * Returns an {@code Result} with the specified present non-null value.
     *
     * @param <T> the class of the value
     * @param value the value to be present, which must be non-null
     * @return an {@code Result} with the value present
     * @throws NullPointerException if value is null
     */
    public static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    /**
     * Returns the value of each present instance from the supplied {@code Result}, in order, skipping over occurrences of
     * {@link #notFoundOrNotAccessible(NotFoundOrNotAccessibleFailure)} or {@link #unknownFailure(Throwable)} etc. Iterators are unmodifiable and are evaluated lazily.
     *
     * @param tries {@link Iterable} of {@link Result} instances
     * @return success instances
     */
    public static final <T> Iterable<T> successInstances(Iterable<? extends Result<? extends T>> tries) {
        checkNotNull(tries);
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new AbstractIterator<T>() {
                    private final Iterator<? extends Result<? extends T>> _iterator = checkNotNull(tries.iterator());

                    @Override
                    protected T computeNext() {
                        while (_iterator.hasNext()) {
                            Result<? extends T> t = _iterator.next();
                            if (t.isSuccess()) {
                                return t.get();
                            }
                        }
                        return endOfData();
                    }
                };
            }
        };
    }

    /**
     * Function that converts a {@code true} to {@link Unit#VALUE} and {@code false} to {@link #absent()}
     *
     * @return function
     */
    public static Function<Boolean, Result<Unit>> trueToUnit() {
        return input -> input ? Result.success(Unit.VALUE) : Result.<Unit>absent();
    }

    /**
     * Returns a {@code Result} instance which represents an unknown failure. No value is present for this {@code Result}.
     *
     * @param throwable {@link Throwable} which represents the content of the failure
     * @param <T> Type of the non-existent value
     * @return a {@code Result} that holds an unknown failure
     */
    public static <T> Result<T> unknownFailure(Throwable throwable) {
        return new UnknownFailure<>(throwable);
    }

    /**
     * @deprecated This function is only there for migration purposes until we removed all <code>Result&lt;Optional&lt;T&gt;&gt;</code> occurrences in our code base.
     * @return function that unwraps a result with an {@link Optional} instance
     */
    @Deprecated
    public static <T> Function<Result<Optional<T>>, Result<T>> unwrapOptional() {
        return new Function<Result<Optional<T>>, Result<T>>() {
            @Override
            @SuppressWarnings("unchecked")
            public Result<T> apply(Result<Optional<T>> input) {
                if (input.isNotSuccess()) {
                    return Result.class.cast(input);
                }
                if (input.get().isPresent()) {
                    return Result.success(input.get().get());
                }
                return Result.absent();
            }
        };
    }

    /**
     * Casts a {@link Failure} instance to be compatible with the given <code>type</code>.
     *
     * @param type
     * @return this, if is an instance of {@link Failure}
     * @throws IllegalStateException if this is an instance of {@link Success}
     */
    public abstract <U> Result<U> cast(Class<U> type);

    /**
     * Returns the contained {@link Throwable} of a failure or created {@link RuntimeException} in case of an {@link #absent() absent} result. If the instance might be present an
     * exception will be thrown.
     *
     * @throws IllegalStateException if the {@link Throwable} instance is absent ({@link #isSuccess} returns {@code true})
     * @return exception
     */
    public abstract Throwable cause();

    /**
     * Indicates whether some other object is "equal to" this Result. The other object is considered equal if:
     * <ul>
     * <li>it is also an {@code Result} and;
     * <li>both instances have no value present or;
     * <li>the present values are "equal to" each other via {@code equals()}.
     * </ul>
     *
     * @param obj an object to be tested for equality
     * @return {code true} if the other object is "equal to" this object otherwise {@code false}
     */
    @Override
    public abstract boolean equals(Object obj);

    /**
     * If a value is present, apply the provided {@code Result}-bearing mapping function to it, return that result, otherwise return an empty {@code Result}. This method is similar
     * to {@link #map(Function)}, but the provided mapper is one whose result is already an {@code Result}, and if invoked, {@code flatMap} does not wrap it with an additional
     * {@code Result}.
     *
     * @param <U> The type parameter to the {@code Result} returned by
     * @param mapper a mapping function to apply to the value, if present the mapping function
     * @return the result of applying an {@code Result}-bearing mapping function to the value of this {@code Result}, if a value is present, otherwise an empty {@code Result}
     * @throws NullPointerException if the mapping function is null or returns a null result
     */
    public abstract <U> Result<U> flatMap(Function<? super T, Result<U>> mapper);

    /**
     * If a value is present in this {@code Result}, returns the value, otherwise throws {@code NoSuchElementException}.
     *
     * @return the non-null value held by this {@code Result}
     * @throws NoSuchElementException if there is no value present
     *
     * @see #isSuccess()
     */
    public abstract T get();

    /**
     * Returns the hash code value of the underlying instance. The cases are implemented as follows
     * <ul>
     * <li>if {@link Absent}, the identity hash code of the singleton instance will be returned</li>
     * <li>if {@link Success}, the hash code of the containing value will be returned</li>
     * <li>if {@link Failure}, the hash code computed of the case class name and the containing throwable will be returned</li>
     * </ul>
     *
     * @return hash code value
     */
    @Override
    public abstract int hashCode();

    /**
     * Return {@code true} if the result is absent, otherwise {@code false}.
     *
     * @return {@code true} if the result is absent, otherwise {@code false}
     */
    public final boolean isAbsent() {
        return this instanceof Absent;
    }

    /**
     * Return {@code true} if no matching entity could be found or the accessibility is restricted, otherwise {@code false}.
     *
     * @return {@code true} if not found or not accessible, otherwise {@code false}
     */
    public boolean isNotFoundOrNotAccessible() {
        return false;
    }

    /**
     * Return {@code true} if the entity cannot be modified, otherwise {@code false}.
     *
     * @return {@code true} if the entity cannot be modified, otherwise {@code false}
     */
    public boolean isNotModifiable() {
        return false;
    }

    /**
     * Return {@code true} if there is a failure or the result is {@link #absent() absent}, otherwise {@code false}.
     * <p>
     * All kinds of non-successful results must return {@code true} when calling this method.
     *
     * @return {@code true} if there is a failure, otherwise {@code false}
     */
    public final boolean isNotSuccess() {
        return !isSuccess();
    }

    /**
     * Return {@code true} if the result is of type {@link RequirementNotMet}, otherwise {@code false}.
     *
     * @return {@code true} if a requirement is not met, otherwise {@code false}
     */
    public boolean isRequirementNotMet() {
        return false;
    }

    /**
     * Return {@code true} if there is a value present, otherwise {@code false}.
     *
     * @return {@code true} if there is a value present, otherwise {@code false}
     */
    public abstract boolean isSuccess();

    /**
     * Maps the given function to the value from this {@link Success} otherwise returns this instance.
     *
     * @param <U> The type of the result of the mapping function
     * @param mapper a mapping function to apply to the value, if {@link Success}
     * @return a {@code Result} instance
     * @throws NullPointerException if the mapping function is null
     */
    public abstract <U> Result<U> map(Function<? super T, ? extends U> mapper);

    /**
     * Return the value if present, otherwise return {@code other}.
     *
     * @param other the value to be returned if there is no value present, may be null
     * @return the value, if present, otherwise {@code other}
     */
    public T orElse(T other) {
        return other;
    }

    /**
     * Return the value if present, otherwise invoke {@code other} and return the result of that invocation.
     *
     * @param other a {@code Supplier} whose result is returned if no value is present
     * @return the value if present otherwise the result of {@code other.get()}
     * @throws NullPointerException if value is not present and {@code other} is null
     */
    public T orElseGet(Supplier<? extends T> other) {
        return other.get();
    }

    /**
     * Return the contained value, if present, otherwise throw an exception to be created by the provided supplier.
     *
     * @apiNote A method reference to the exception constructor with an empty argument list can be used as the supplier. For example, {@code IllegalStateException::new}
     *
     * @param <X> Type of the exception to be thrown
     * @param exceptionSupplier The supplier which will return the exception to be thrown
     * @return the present value
     * @throws X if there is no value present
     * @throws NullPointerException if no value is present and {@code exceptionSupplier} is null
     */
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        throw exceptionSupplier.get();
    }

    /**
     * Applies the given function {@code recover} if this is a {@link Failure}, otherwise returns this if this is a {@link Success} or {@link Absent}. This is like
     * {@link #map(Function) map} for the failure case.
     *
     * @param recover a recover function
     * @return a {@code Result} instance
     * @throws NullPointerException if the mapping function is null
     */
    public abstract Result<T> recover(Function<? super Throwable, T> recover);

    /**
     * Resolves a {@link Failure} to a given {@link Result}.
     *
     * @param result
     * @return a {@code Result} instance
     */
    public final Result<T> recover(Result<T> result) {
        return recoverWith(Functions.constant(result));
    }

    /**
     * Resolves a {@link Failure} instance to {@link Success}.
     *
     * @param value
     * @return a {@code Result} instance
     */
    public final Result<T> recover(T value) {
        return recover(Functions.constant(value));
    }

    /**
     * Applies the given function {@code recover} if this is a {@link Failure}, otherwise returns this if this is a {@link Success} or {@link Absent}. This is like
     * {@link #map(Function) map} for the failure case.
     *
     * @param recover a recover function
     * @return a {@code Result} instance
     * @throws NullPointerException if the mapping function is null
     */
    public abstract Result<T> recoverWith(Function<? super Throwable, Result<T>> recover);

    /**
     * Resolves an {@link #absent() absent} instance to a given {@link Result}.
     * <p>
     * It simply maps an absent case to another instance of {@code Result<T>}, where the method {@link #orElse(Object)} attempts to return a value or uses the given alternative
     * value.
     *
     * @param result
     * @return a {@code Result} instance
     */
    public abstract Result<T> resolveIfAbsent(Result<T> result);

    /**
     * Resolves an {@link #absent() absent} instance to a given {@link Result}.
     * <p>
     * It simply maps an absent case to another instance of {@code Result<T>}, where the method {@link #orElse(Object)} attempts to return a value or uses the given alternative
     * value.
     *
     * @param value supplier
     * @return a {@code Result} instance
     */
    public abstract Result<T> resolveIfAbsent(Supplier<Result<T>> value);

    /**
     * Resolves an {@link #absent() absent} instance to a {@link Success} of the given value.
     * <p>
     * It simply maps an absent case to an instance of {@code Success}, where the method {@link #orElse(Object)} attempts to return a value or uses the given alternative value.
     *
     * @param value
     * @return a {@code Result} instance
     */
    public abstract Result<T> resolveIfAbsent(T value);

    /**
     * Returns a non-empty string representation of this Result suitable for debugging. The exact presentation format is unspecified and may vary between implementations and
     * versions.
     *
     * @implSpec If a value is present the result must include its string representation in the result. All kinds of {@code Result}s must be unambiguously differentiable.
     *
     * @return the string representation of this instance
     */
    @Override
    public abstract String toString();

    private static class Absent<T> extends NotSuccess<T> {
        static final Absent<?> instance = new Absent<>();
        final int _hashCode = System.identityHashCode(this);

        @Override
        public final Throwable cause() {
            return AbsentResult.instance;
        }

        @Override
        public final boolean equals(Object obj) {
            return this == obj;
        }

        @Override
        public T get() {
            throw new IllegalStateException("Result.get() cannot be called on an absent result");
        }

        @Override
        public final int hashCode() {
            return _hashCode;
        }

        @Override
        public Result<T> recover(Function<? super Throwable, T> recover) {
            return this;
        }

        @Override
        public Result<T> recoverWith(Function<? super Throwable, Result<T>> recover) {
            return this;
        }

        @Override
        public Result<T> resolveIfAbsent(Result<T> result) {
            return checkNotNull(result, "Argument 'result' must not be null");
        }

        @Override
        public Result<T> resolveIfAbsent(Supplier<Result<T>> value) {
            try {
                return value.get();
            } catch (Exception e) {
                return Result.unknownFailure(e);
            }
        }

        @Override
        public Result<T> resolveIfAbsent(T value) {
            return success(value);
        }

        @Override
        public final String toString() {
            return String.format(getClass().getSimpleName());
        }
    }

    private static class AbsentResult extends RuntimeException {
        static final AbsentResult instance = new AbsentResult();
        static final long serialVersionUID = 1L;

        AbsentResult() {
            super("result is absent");
        }
    }

    private static abstract class Failure<T> extends NotSuccess<T> {
        final Throwable _throwable;

        Failure(Throwable throwable) {
            _throwable = checkNotNull(throwable, "Argument 'throwable' must not be null");
        }

        @Override
        public final Throwable cause() {
            return _throwable;
        }

        @Override
        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!getClass().isInstance(obj)) {
                return false;
            }
            Failure<?> other = (Failure<?>) obj;
            return Objects.equals(_throwable, other._throwable);
        }

        @Override
        public T get() {
            if (_throwable instanceof RuntimeException) {
                throw (RuntimeException) _throwable;
            }
            throw new RuntimeException(_throwable);
        }

        @Override
        public final int hashCode() {
            return Objects.hash(getClass().getName(), _throwable);
        }

        @Override
        public Result<T> recover(Function<? super Throwable, T> recover) {
            checkNotNull(recover, "Argument 'recover' must not be null");
            try {
                return success(recover.apply(_throwable));
            } catch (Exception e) {
                return Result.unknownFailure(e);
            }
        }

        @Override
        public Result<T> recoverWith(Function<? super Throwable, Result<T>> recover) {
            checkNotNull(recover, "Argument 'recover' must not be null");
            try {
                return recover.apply(_throwable);
            } catch (Exception e) {
                return Result.unknownFailure(e);
            }
        }

        @Override
        public final Result<T> resolveIfAbsent(Result<T> result) {
            return this;
        }

        @Override
        public final Result<T> resolveIfAbsent(Supplier<Result<T>> result) {
            return this;
        }

        @Override
        public final Result<T> resolveIfAbsent(T value) {
            return this;
        }

        @Override
        public final String toString() {
            return String.format(getClass().getSimpleName() + "[%s]", _throwable);
        }
    }

    static class NotFoundOrNotAccessible<T> extends Failure<T> {
        NotFoundOrNotAccessible(RuntimeException failure) {
            super(failure);
        }

        @Override
        public final boolean isNotFoundOrNotAccessible() {
            return true;
        }
    }

    static class NotModifiable<T> extends Failure<T> {
        NotModifiable(RuntimeException failure) {
            super(failure);
        }

        @Override
        public final boolean isNotModifiable() {
            return true;
        }
    }

    private static abstract class NotSuccess<T> extends Result<T> {
        @Override
        @SuppressWarnings("unchecked")
        public final <U> Result<U> cast(Class<U> type) {
            return (Result<U>) this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public final <U> Result<U> flatMap(Function<? super T, Result<U>> mapper) {
            return (Result<U>) this;
        }

        @Override
        public final boolean isSuccess() {
            return false;
        }

        @Override
        @SuppressWarnings("unchecked")
        public final <U> Result<U> map(Function<? super T, ? extends U> mapper) {
            return (Result<U>) this;
        }
    }

    static class RequirementNotMet<T> extends Failure<T> {
        RequirementNotMet(RuntimeException failure) {
            super(failure);
        }

        @Override
        public final boolean isRequirementNotMet() {
            return true;
        }
    }

    static final class Success<T> extends Result<T> {
        private final T _value;

        private Success(T value) {
            _value = checkNotNull(value, "Argument 'value' must not be null");
        }

        @Override
        public <U> Result<U> cast(Class<U> type) {
            return Result.success(type.cast(get()));
        }

        @Override
        public Throwable cause() {
            throw new IllegalStateException("Result.cause() cannot be called on a successful result");
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Success)) {
                return false;
            }
            Success<?> other = (Success<?>) obj;
            return Objects.equals(_value, other._value);
        }

        @Override
        public <U> Result<U> flatMap(Function<? super T, Result<U>> mapper) {
            checkNotNull(mapper, "Argument 'mapper' must not be null");
            try {
                return verifyNotNull(mapper.apply(_value), "function must not return a null reference");
            } catch (Exception e) {
                return unknownFailure(e);
            }
        }

        @Override
        public T get() {
            return _value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(_value);
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public <U> Result<U> map(Function<? super T, ? extends U> mapper) {
            checkNotNull(mapper, "Argument 'mapper' must not be null");
            try {
                U value = verifyNotNull(mapper.apply(_value), "function must not return a null reference");
                return success(value);
            } catch (Exception e) {
                return unknownFailure(e);
            }
        }

        @Override
        public T orElse(T other) {
            return _value;
        }

        @Override
        public T orElseGet(Supplier<? extends T> other) {
            return _value;
        }

        @Override
        public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
            return _value;
        }

        @Override
        public Result<T> recover(Function<? super Throwable, T> recover) {
            return this;
        }

        @Override
        public Result<T> recoverWith(Function<? super Throwable, Result<T>> recover) {
            return this;
        }

        @Override
        public Result<T> resolveIfAbsent(Result<T> result) {
            return this;
        }

        @Override
        public final Result<T> resolveIfAbsent(Supplier<Result<T>> result) {
            return this;
        }

        @Override
        public Result<T> resolveIfAbsent(T value) {
            return this;
        }

        @Override
        public String toString() {
            return String.format("Success[%s]", _value);
        }
    }

    static class UnknownFailure<T> extends Failure<T> {
        private UnknownFailure(Throwable throwable) {
            super(throwable);
        }
    }
}
