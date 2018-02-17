package code.exampleaxon.accountdomain.command.repository;

import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;

public class EventSourcingRepositoryImpl<T> extends EventSourcingRepository<T> {
    public EventSourcingRepositoryImpl(Class<T> aggregateType, EventStore eventStore) {
        super(aggregateType, eventStore);
    }
}
