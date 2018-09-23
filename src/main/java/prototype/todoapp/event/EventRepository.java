package prototype.todoapp.event;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.CheckReturnValue;
import javax.annotation.Generated;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;

import org.immutables.mongo.concurrent.FluentFuture;
import org.immutables.mongo.repository.Repositories;
import org.immutables.mongo.repository.RepositorySetup;
import org.immutables.mongo.repository.internal.Constraints;
import org.immutables.mongo.repository.internal.Support;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Range;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import core.Event;
import core.EventId;

/**
 * A {@code EventRepository} provides type-safe access for storing and retrieving documents from the MongoDB collection {@code "event"}.
 */
@SuppressWarnings("all")
@ParametersAreNonnullByDefault
@Generated("org.immutables.processor.ProxyProcessor")
@org.immutables.value.Generated(from = "Event", generator = "Repositories")
@ThreadSafe
public final class EventRepository extends Repositories.Repository<Event> {
    private static final String DOCUMENT_COLLECTION_NAME = "event";

    private final Serialization serialization;
    private final Criteria anyCriteria;

    /**
     * Constructs a {@link Event} repository using {@link RepositorySetup configuration}.
     *
     * @param configuration The repository configuration
     */
    public EventRepository(RepositorySetup configuration) {
        super(configuration, DOCUMENT_COLLECTION_NAME, Event.class);
        this.serialization = new Serialization(getGson());
        this.anyCriteria = new Criteria(this.serialization, Constraints.nilConstraint());
    }

    /**
     * Inserts a single document into the collection.
     *
     * @param document The event to insert
     * @return A future representing the number of inserted documents (1) if WriteConcern allows the insertion.
     */
    public FluentFuture<Integer> insert(Event document) {
        return super.doInsert(ImmutableList.of(document));
    }

    /**
     * Insert documents into the collection.
     *
     * @param documents The documents to insert
     * @return A future representing the number of inserted documents if WriteConcern allows the insertion.
     */
    public FluentFuture<Integer> insert(Iterable<? extends Event> documents) {
        return super.doInsert(ImmutableList.copyOf(documents));
    }

    /**
     * Finds all documents. Use the returned {@link Finder} object to complete {@link Finder#fetchAll() fetch all} or other operations.
     *
     * @return A finder object used to complete operations
     */
    @CheckReturnValue
    public Finder findAll() {
        return find(criteria());
    }

    /**
     * Find documents by the criteria expressed as a JSON string. Use the returned {@link Finder} object to complete {@link Finder#fetchAll() fetch} or {@link Finder#fetchFirst()
     * fetch} operations.
     *
     * @param jsonCriteria A JSON string for native criteria
     * @return A finder object used to complete operations
     */
    @CheckReturnValue
    public Finder find(String jsonCriteria) {
        return new Finder(this, Support.jsonQuery(jsonCriteria));
    }

    /**
     * Find a document by the given {@link EventRepository#criteria() criteria}. Use the returned {@link Finder} object to complete {@link Finder#fetchAll() fetch} operations. You can
     * also use {@link Finder#andModifyFirst() modify} or {@link Finder#deleteFirst() delete} operations to update / delete the document(s).
     *
     * @param criteria The search criteria
     * @return A finder object used to complete operations
     */
    @CheckReturnValue
    public Finder find(Criteria criteria) {
        return new Finder(this, criteria.constraint);
    }

    /**
     * The finder object used to proceed with find operations via the {@link Finder#fetchAll()}, {@link Finder#fetchFirst()}, {@link Finder#andModifyFirst()}, or
     * {@link Finder#deleteFirst()} methods. Configure exclusion and sort ordering for results using the family of {@code exclude*()} and {@code orderBy*()} attribute-specific methods.
     *
     * @see EventRepository#find(Criteria)
     */
    @NotThreadSafe
    public static final class Finder extends Repositories.FinderWithDelete<Event, Finder> {
        private final Serialization serialization;

        private Finder(EventRepository repository, Constraints.ConstraintHost criteria) {
            super(repository);
            this.criteria = criteria;
            this.serialization = repository.serialization;
        }

        /**
         * Order by {@link Event#eventId() eventId} in the ascending direction. Specify that the next attribute to sort will be the {@link Event#eventId() eventId} attribute using
         * ascending order in the the chain of comparisons performed to sort results.
         *
         * @return {@code this} finder for use in a chained invocation
         */
        public Finder orderByEventId() {
            ordering = ordering.equal(serialization.eventIdName, false, 1);
            return this;
        }

        /**
         * Order by {@link Event#eventId() eventId} in the descending direction. Specify that the next attribute to sort will be the {@link Event#eventId() eventId} attribute using
         * descending order in the the chain of comparisons performed to sort results.
         *
         * @return {@code this} finder for use in a chained invocation
         */
        public Finder orderByEventIdDesceding() {
            ordering = ordering.equal(serialization.eventIdName, false, -1);
            return this;
        }

        /**
         * Order by {@link Event#eventTime() eventTime} in the ascending direction. Specify that the next attribute to sort will be the {@link Event#eventTime() eventTime} attribute using
         * ascending order in the the chain of comparisons performed to sort results.
         *
         * @return {@code this} finder for use in a chained invocation
         */
        public Finder orderByEventTime() {
            ordering = ordering.equal(serialization.eventTimeName, false, 1);
            return this;
        }

        /**
         * Order by {@link Event#eventTime() eventTime} in the descending direction. Specify that the next attribute to sort will be the {@link Event#eventTime() eventTime} attribute using
         * descending order in the the chain of comparisons performed to sort results.
         *
         * @return {@code this} finder for use in a chained invocation
         */
        public Finder orderByEventTimeDesceding() {
            ordering = ordering.equal(serialization.eventTimeName, false, -1);
            return this;
        }

        /**
         * Order by {@link Event#eventType() eventType} in the ascending direction. Specify that the next attribute to sort will be the {@link Event#eventType() eventType} attribute using
         * ascending order in the the chain of comparisons performed to sort results.
         *
         * @return {@code this} finder for use in a chained invocation
         */
        public Finder orderByEventType() {
            ordering = ordering.equal(serialization.eventTypeName, false, 1);
            return this;
        }

        /**
         * Order by {@link Event#eventType() eventType} in the descending direction. Specify that the next attribute to sort will be the {@link Event#eventType() eventType} attribute using
         * descending order in the the chain of comparisons performed to sort results.
         *
         * @return {@code this} finder for use in a chained invocation
         */
        public Finder orderByEventTypeDesceding() {
            ordering = ordering.equal(serialization.eventTypeName, false, -1);
            return this;
        }

        /**
         * Turn a find operation into an atomic {@link DBCollection#findAndModify(DBObject, DBObject, DBObject, boolean, DBObject, boolean, boolean) findAndModify} operation. Use the
         * family of {@code set*()}, {@code unset*()}, {@code add*()}, {@code remove*()}, {@code put*()}m and {@code init*()} (and other attribute-specific) methods to describe the
         * modification.
         *
         * @return A modifier object to complete the {@code findAndModify} operation
         */
        @CheckReturnValue
        public Modifier andModifyFirst() {
            return new Modifier((EventRepository) repository, criteria, ordering, exclusion);
        }

        /**
         * Used to replace in-place existing version of the document
         */
        @CheckReturnValue
        public Replacer andReplaceFirst(Event document) {
            return new Replacer((EventRepository) repository, document, criteria, ordering);
        }
    }

    /**
     * Update the set of {@code "event"} documents.
     *
     * @param criteria The search criteria for update
     * @return An updater object that will be used to complete the update.
     */
    @CheckReturnValue
    public Updater update(Criteria criteria) {
        return new Updater(this, criteria);
    }

    /**
     * {@link #update(Criteria) Given} the criteria updater describes how to perform update operations on sets of documents.
     */
    @NotThreadSafe
    public static final class Updater extends Repositories.Updater<Event> {
        private final Serialization serialization;

        private Updater(EventRepository repository, Criteria criteria) {
            super(repository);
            this.criteria = criteria.constraint;
            this.serialization = repository.serialization;
        }

        /**
         * Specify a new value for the {@code eventId} attribute.
         * <p>
         * Corresponds to the MongoDB {@code $set} operator.
         *
         * @param value A new value for the {@code eventId} attribute
         * @return {@code this} updater to be used to complete the update operation
         */
        public Updater setEventId(EventId value) {
            setFields = setFields.equal(serialization.eventIdName, false, Support.writable(serialization.eventIdTypeAdapter, value));
            return this;
        }

        /**
         * Specify an initial value for the {@code eventId} attribute. The value will be used if the document is to be inserted. If one or more documents are found for an update, this
         * value will not be used.
         * <p>
         * Corresponds to the MongoDB {@code $setOnInsert} operator.
         *
         * @param value The {@code eventId} value for an insert.
         * @return {@code this} updater to be used to complete the update operation
         */
        public Updater initEventId(EventId value) {
            setOnInsertFields = setOnInsertFields.equal(serialization.eventIdName, false, Support.writable(serialization.eventIdTypeAdapter, value));
            return this;
        }

        /**
         * Specify a new value for the {@code eventTime} attribute.
         * <p>
         * Corresponds to the MongoDB {@code $set} operator.
         *
         * @param value A new value for the {@code eventTime} attribute
         * @return {@code this} updater to be used to complete the update operation
         */
        public Updater setEventTime(ZonedDateTime value) {
            setFields = setFields.equal(serialization.eventTimeName, false, Support.writable(serialization.eventTimeTypeAdapter, value));
            return this;
        }

        /**
         * Specify an initial value for the {@code eventTime} attribute. The value will be used if the document is to be inserted. If one or more documents are found for an update, this
         * value will not be used.
         * <p>
         * Corresponds to the MongoDB {@code $setOnInsert} operator.
         *
         * @param value The {@code eventTime} value for an insert.
         * @return {@code this} updater to be used to complete the update operation
         */
        public Updater initEventTime(ZonedDateTime value) {
            setOnInsertFields = setOnInsertFields.equal(serialization.eventTimeName, false, Support.writable(serialization.eventTimeTypeAdapter, value));
            return this;
        }

        /**
         * Specify a new value for the {@code eventType} attribute.
         * <p>
         * Corresponds to the MongoDB {@code $set} operator.
         *
         * @param value A new value for the {@code eventType} attribute
         * @return {@code this} updater to be used to complete the update operation
         */
        public Updater setEventType(java.lang.String value) {
            setFields = setFields.equal(serialization.eventTypeName, false, Support.writable(value));
            return this;
        }

        /**
         * Specify an initial value for the {@code eventType} attribute. The value will be used if the document is to be inserted. If one or more documents are found for an update, this
         * value will not be used.
         * <p>
         * Corresponds to the MongoDB {@code $setOnInsert} operator.
         *
         * @param value The {@code eventType} value for an insert.
         * @return {@code this} updater to be used to complete the update operation
         */
        public Updater initEventType(java.lang.String value) {
            setOnInsertFields = setOnInsertFields.equal(serialization.eventTypeName, false, Support.writable(value));
            return this;
        }

    }

    @NotThreadSafe
    public static final class Modifier extends Repositories.Modifier<Event, Modifier> {
        private final Serialization serialization;

        private Modifier(
                EventRepository repository,
                Constraints.ConstraintHost criteria,
                Constraints.Constraint ordering,
                Constraints.Constraint exclusion) {
            super(repository);
            this.serialization = repository.serialization;
            this.criteria = criteria;
            this.ordering = ordering;
            this.exclusion = exclusion;
        }

        /**
         * Specify a new value for the {@code eventId} attribute.
         * <p>
         * Corresponds to the MongoDB {@code $set} operator.
         *
         * @param value A new value for the {@code eventId} attribute
         * @return {@code this} modifier to be used to complete the update operation
         */
        public Modifier setEventId(EventId value) {
            setFields = setFields.equal(serialization.eventIdName, false, Support.writable(serialization.eventIdTypeAdapter, value));
            return this;
        }

        /**
         * Specify an initial value for the {@code eventId} attribute. The value will be used if the document is to be inserted. If one or more documents are found for an update, this
         * value will not be used.
         * <p>
         * Corresponds to the MongoDB {@code $setOnInsert} operator.
         *
         * @param value The {@code eventId} value for an insert.
         * @return {@code this} modifier to be used to complete the update operation
         */
        public Modifier initEventId(EventId value) {
            setOnInsertFields = setOnInsertFields.equal(serialization.eventIdName, false, Support.writable(serialization.eventIdTypeAdapter, value));
            return this;
        }

        /**
         * Specify a new value for the {@code eventTime} attribute.
         * <p>
         * Corresponds to the MongoDB {@code $set} operator.
         *
         * @param value A new value for the {@code eventTime} attribute
         * @return {@code this} modifier to be used to complete the update operation
         */
        public Modifier setEventTime(ZonedDateTime value) {
            setFields = setFields.equal(serialization.eventTimeName, false, Support.writable(serialization.eventTimeTypeAdapter, value));
            return this;
        }

        /**
         * Specify an initial value for the {@code eventTime} attribute. The value will be used if the document is to be inserted. If one or more documents are found for an update, this
         * value will not be used.
         * <p>
         * Corresponds to the MongoDB {@code $setOnInsert} operator.
         *
         * @param value The {@code eventTime} value for an insert.
         * @return {@code this} modifier to be used to complete the update operation
         */
        public Modifier initEventTime(ZonedDateTime value) {
            setOnInsertFields = setOnInsertFields.equal(serialization.eventTimeName, false, Support.writable(serialization.eventTimeTypeAdapter, value));
            return this;
        }

        /**
         * Specify a new value for the {@code eventType} attribute.
         * <p>
         * Corresponds to the MongoDB {@code $set} operator.
         *
         * @param value A new value for the {@code eventType} attribute
         * @return {@code this} modifier to be used to complete the update operation
         */
        public Modifier setEventType(java.lang.String value) {
            setFields = setFields.equal(serialization.eventTypeName, false, Support.writable(value));
            return this;
        }

        /**
         * Specify an initial value for the {@code eventType} attribute. The value will be used if the document is to be inserted. If one or more documents are found for an update, this
         * value will not be used.
         * <p>
         * Corresponds to the MongoDB {@code $setOnInsert} operator.
         *
         * @param value The {@code eventType} value for an insert.
         * @return {@code this} modifier to be used to complete the update operation
         */
        public Modifier initEventType(java.lang.String value) {
            setOnInsertFields = setOnInsertFields.equal(serialization.eventTypeName, false, Support.writable(value));
            return this;
        }

    }

    @NotThreadSafe
    public static final class Replacer extends Repositories.Replacer<Event, Replacer> {
        protected Replacer(EventRepository repository, Event document, Constraints.ConstraintHost criteria, Constraints.Constraint ordering) {
            super(repository, document, criteria, ordering);
        }
    }

    /**
     * {@link DBCollection#createIndex(DBObject, DBObject) Ensure an index} on collection event by one or more attributes using the family of {@code with*()} attribute-specific
     * methods. While indexes will usually be maintained by special administration scripts, for simple cases it is convenient to ensure an index on application startup.
     *
     * @see Indexer#named(String)
     * @see Indexer#unique()
     * @return An indexer object to be completed with the {@link Indexer#ensure()} operation.
     */
    @CheckReturnValue
    public Indexer index() {
        return new Indexer(this);
    }

    /**
     * An indexer used to create an index on the {@code "event"} collection if it does not exist by one or more attributes.
     *
     * @see DBCollection#ensure(DBObject, DBObject)
     */
    @NotThreadSafe
    public static final class Indexer extends Repositories.Indexer<Event, Indexer> {
        private final Serialization serialization;

        private Indexer(EventRepository repository) {
            super(repository);
            this.serialization = repository.serialization;
        }

        /**
         * Specify that the next attribute to index will be {@link Event#eventId() eventId}, in the ascending direction.
         *
         * @return {@code this} indexer for use in a chained invocation
         */
        public Indexer withEventId() {
            fields = fields.equal(serialization.eventIdName, false, 1);
            return this;
        }

        /**
         * Specify that the next attribute to index will be {@link Event#eventId() eventId}, in the descending direction.
         *
         * @return {@code this} indexer for use in a chained invocation
         */
        public Indexer withEventIdDesceding() {
            fields = fields.equal(serialization.eventIdName, false, -1);
            return this;
        }

        /**
         * Specify that the next attribute to index will be {@link Event#eventTime() eventTime}, in the ascending direction.
         *
         * @return {@code this} indexer for use in a chained invocation
         */
        public Indexer withEventTime() {
            fields = fields.equal(serialization.eventTimeName, false, 1);
            return this;
        }

        /**
         * Specify that the next attribute to index will be {@link Event#eventTime() eventTime}, in the descending direction.
         *
         * @return {@code this} indexer for use in a chained invocation
         */
        public Indexer withEventTimeDesceding() {
            fields = fields.equal(serialization.eventTimeName, false, -1);
            return this;
        }

        /**
         * Specify that the next attribute to index will be {@link Event#eventType() eventType}, in the ascending direction.
         *
         * @return {@code this} indexer for use in a chained invocation
         */
        public Indexer withEventType() {
            fields = fields.equal(serialization.eventTypeName, false, 1);
            return this;
        }

        /**
         * Specify that the next attribute to index will be {@link Event#eventType() eventType}, in the descending direction.
         *
         * @return {@code this} indexer for use in a chained invocation
         */
        public Indexer withEventTypeDesceding() {
            fields = fields.equal(serialization.eventTypeName, false, -1);
            return this;
        }
    }

    /**
     * Search criteria. Returns an initial object to create criteria by invoking methods that describe attribute specific constraints.
     *
     * @return An empty immutable criteria
     */
    public Criteria criteria() {
        return anyCriteria;
    }

    /**
     * {@code EventRepository.Criteria} is a Event document search query. Call methods on the criteria to add constraints for search queries.
     */
    @Immutable
    @SuppressWarnings("unchecked")
    public static final class Criteria extends Repositories.Criteria {
        private final Constraints.Constraint constraint;
        private final Serialization serialization;

        Criteria(Serialization serialization, Constraints.Constraint constraint) {
            this.constraint = constraint;
            this.serialization = serialization;
        }

        public Criteria eventId(EventId value) {
            return new Criteria(serialization, constraint.equal(serialization.eventIdName, false, Support.writable(serialization.eventIdTypeAdapter, value)));
        }

        public Criteria eventIdNot(EventId value) {
            return new Criteria(serialization, constraint.equal(serialization.eventIdName, true, Support.writable(serialization.eventIdTypeAdapter, value)));
        }

        public Criteria eventIdIn(Iterable<EventId> values) {
            List<Object> wrappedValues = new ArrayList<>();
            for (EventId value : values) {
                wrappedValues.add(Support.writable(serialization.eventIdTypeAdapter, value));
            }
            return new Criteria(serialization, constraint.in(serialization.eventIdName, false, wrappedValues));
        }

        public Criteria eventIdIn(EventId first, EventId second, EventId... rest) {
            List<Object> values = new ArrayList<>(2 + rest.length);
            values.add(Support.writable(serialization.eventIdTypeAdapter, first));
            values.add(Support.writable(serialization.eventIdTypeAdapter, second));
            for (EventId value : rest) {
                values.add(Support.writable(serialization.eventIdTypeAdapter, value));
            }
            return new Criteria(serialization, constraint.in(serialization.eventIdName, false, values));
        }

        public Criteria eventIdNotIn(Iterable<EventId> values) {
            List<Object> wrappedValues = new ArrayList<>();
            for (EventId value : values) {
                wrappedValues.add(Support.writable(serialization.eventIdTypeAdapter, value));
            }
            return new Criteria(serialization, constraint.in(serialization.eventIdName, true, wrappedValues));
        }

        public Criteria eventIdNotIn(EventId first, EventId second, EventId... rest) {
            List<Object> values = new ArrayList<>(2 + rest.length);
            values.add(Support.writable(serialization.eventIdTypeAdapter, first));
            values.add(Support.writable(serialization.eventIdTypeAdapter, second));
            for (EventId value : rest) {
                values.add(Support.writable(serialization.eventIdTypeAdapter, value));
            }
            return new Criteria(serialization, constraint.in(serialization.eventIdName, true, values));
        }

        public Criteria eventTime(ZonedDateTime value) {
            return new Criteria(serialization, constraint.equal(serialization.eventTimeName, false, Support.writable(serialization.eventTimeTypeAdapter, value)));
        }

        public Criteria eventTimeNot(ZonedDateTime value) {
            return new Criteria(serialization, constraint.equal(serialization.eventTimeName, true, Support.writable(serialization.eventTimeTypeAdapter, value)));
        }

        public Criteria eventTimeIn(Iterable<ZonedDateTime> values) {
            List<Object> wrappedValues = new ArrayList<>();
            for (ZonedDateTime value : values) {
                wrappedValues.add(Support.writable(serialization.eventTimeTypeAdapter, value));
            }
            return new Criteria(serialization, constraint.in(serialization.eventTimeName, false, wrappedValues));
        }

        public Criteria eventTimeIn(ZonedDateTime first, ZonedDateTime second, ZonedDateTime... rest) {
            List<Object> values = new ArrayList<>(2 + rest.length);
            values.add(Support.writable(serialization.eventTimeTypeAdapter, first));
            values.add(Support.writable(serialization.eventTimeTypeAdapter, second));
            for (ZonedDateTime value : rest) {
                values.add(Support.writable(serialization.eventTimeTypeAdapter, value));
            }
            return new Criteria(serialization, constraint.in(serialization.eventTimeName, false, values));
        }

        public Criteria eventTimeNotIn(Iterable<ZonedDateTime> values) {
            List<Object> wrappedValues = new ArrayList<>();
            for (ZonedDateTime value : values) {
                wrappedValues.add(Support.writable(serialization.eventTimeTypeAdapter, value));
            }
            return new Criteria(serialization, constraint.in(serialization.eventTimeName, true, wrappedValues));
        }

        public Criteria eventTimeNotIn(ZonedDateTime first, ZonedDateTime second, ZonedDateTime... rest) {
            List<Object> values = new ArrayList<>(2 + rest.length);
            values.add(Support.writable(serialization.eventTimeTypeAdapter, first));
            values.add(Support.writable(serialization.eventTimeTypeAdapter, second));
            for (ZonedDateTime value : rest) {
                values.add(Support.writable(serialization.eventTimeTypeAdapter, value));
            }
            return new Criteria(serialization, constraint.in(serialization.eventTimeName, true, values));
        }

        public Criteria eventTimeGreaterThan(ZonedDateTime lower) {
            return eventTimeIn(Range.greaterThan(lower));
        }

        public Criteria eventTimeLessThan(ZonedDateTime upper) {
            return eventTimeIn(Range.lessThan(upper));
        }

        public Criteria eventTimeAtMost(ZonedDateTime upperInclusive) {
            return eventTimeIn(Range.atMost(upperInclusive));
        }

        public Criteria eventTimeAtLeast(ZonedDateTime lowerInclusive) {
            return eventTimeIn(Range.atLeast(lowerInclusive));
        }

        public Criteria eventTimeIn(Range<ZonedDateTime> range) {
            return new Criteria(serialization, constraint.range(serialization.eventTimeName, false, Support.writable(serialization.eventTimeTypeAdapter, range)));
        }

        public Criteria eventTimeNotIn(Range<ZonedDateTime> range) {
            return new Criteria(serialization, constraint.range(serialization.eventTimeName, true, Support.writable(serialization.eventTimeTypeAdapter, range)));
        }

        public Criteria eventType(java.lang.String value) {
            return new Criteria(serialization, constraint.equal(serialization.eventTypeName, false, Support.writable(value)));
        }

        public Criteria eventTypeNot(java.lang.String value) {
            return new Criteria(serialization, constraint.equal(serialization.eventTypeName, true, Support.writable(value)));
        }

        public Criteria eventTypeIn(Iterable<java.lang.String> values) {
            List<Object> wrappedValues = new ArrayList<>();
            for (java.lang.String value : values) {
                wrappedValues.add(Support.writable(value));
            }
            return new Criteria(serialization, constraint.in(serialization.eventTypeName, false, wrappedValues));
        }

        public Criteria eventTypeIn(java.lang.String first, java.lang.String second, java.lang.String... rest) {
            List<Object> values = new ArrayList<>(2 + rest.length);
            values.add(Support.writable(first));
            values.add(Support.writable(second));
            for (java.lang.String value : rest) {
                values.add(Support.writable(value));
            }
            return new Criteria(serialization, constraint.in(serialization.eventTypeName, false, values));
        }

        public Criteria eventTypeNotIn(Iterable<java.lang.String> values) {
            List<Object> wrappedValues = new ArrayList<>();
            for (java.lang.String value : values) {
                wrappedValues.add(Support.writable(value));
            }
            return new Criteria(serialization, constraint.in(serialization.eventTypeName, true, wrappedValues));
        }

        public Criteria eventTypeNotIn(java.lang.String first, java.lang.String second, java.lang.String... rest) {
            List<Object> values = new ArrayList<>(2 + rest.length);
            values.add(Support.writable(first));
            values.add(Support.writable(second));
            for (java.lang.String value : rest) {
                values.add(Support.writable(value));
            }
            return new Criteria(serialization, constraint.in(serialization.eventTypeName, true, values));
        }

        public Criteria eventTypeStartsWith(String prefix) {
            return new Criteria(serialization, constraint.match(serialization.eventTypeName, false, Constraints.prefixPatternOf(prefix)));
        }

        public Criteria eventTypeMatches(Pattern pattern) {
            return new Criteria(serialization, constraint.match(serialization.eventTypeName, false, pattern));
        }

        public Criteria eventTypeNotMatches(Pattern pattern) {
            return new Criteria(serialization, constraint.match(serialization.eventTypeName, true, pattern));
        }

        public Criteria eventTypeGreaterThan(java.lang.String lower) {
            return eventTypeIn(Range.greaterThan(lower));
        }

        public Criteria eventTypeLessThan(java.lang.String upper) {
            return eventTypeIn(Range.lessThan(upper));
        }

        public Criteria eventTypeAtMost(java.lang.String upperInclusive) {
            return eventTypeIn(Range.atMost(upperInclusive));
        }

        public Criteria eventTypeAtLeast(java.lang.String lowerInclusive) {
            return eventTypeIn(Range.atLeast(lowerInclusive));
        }

        public Criteria eventTypeIn(Range<java.lang.String> range) {
            return new Criteria(serialization, constraint.range(serialization.eventTypeName, false, Support.writable(range)));
        }

        public Criteria eventTypeNotIn(Range<java.lang.String> range) {
            return new Criteria(serialization, constraint.range(serialization.eventTypeName, true, Support.writable(range)));
        }

        @Override
        public Criteria or() {
            return new Criteria(serialization, constraint.disjunction());
        }

        public Criteria with(Criteria criteria) {
            return new Criteria(serialization, criteria.constraint.accept(constraint));
        }

        @Override
        public String toString() {
            return "EventRepository.criteria(" + Support.stringify(constraint) + ")";
        }
    }

    private static class Serialization {
        final TypeAdapter<EventId> eventIdTypeAdapter;
        final TypeAdapter<ZonedDateTime> eventTimeTypeAdapter;
        final Gson gson;
        final String eventIdName;
        final String eventTimeName;
        final String eventTypeName;

        Serialization(Gson gson) {
            this.gson = gson;
            this.eventIdTypeAdapter = gson.getAdapter(EventId.class);
            this.eventTimeTypeAdapter = gson.getAdapter(ZonedDateTime.class);
            this.eventIdName = translateName("eventId");
            this.eventTimeName = translateName("eventTime");
            this.eventTypeName = translateName("eventType");
        }

        static final class EventNamingFields {
            public EventId eventId;
            public ZonedDateTime eventTime;
            public java.lang.String eventType;
        }

        private String translateName(String fieldName) {
            try {
                return gson.fieldNamingStrategy().translateName(
                        EventNamingFields.class.getField(fieldName));
            } catch (NoSuchFieldException noSuchField) {
                throw new AssertionError(noSuchField);
            }
        }
    }
}
