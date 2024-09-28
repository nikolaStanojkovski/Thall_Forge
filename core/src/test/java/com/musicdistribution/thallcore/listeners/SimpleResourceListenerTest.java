package com.musicdistribution.thallcore.listeners;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.musicdistribution.thallcore.listeners.SimpleResourceListener;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChange.ChangeType;
import org.junit.jupiter.api.Test;

import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

class SimpleResourceListenerTest {

    private final SimpleResourceListener fixture = new SimpleResourceListener();

    private final TestLogger logger = TestLoggerFactory.getTestLogger(fixture.getClass());

    @Test
    void handleEvent() {
        
        ResourceChange change = new ResourceChange(ChangeType.ADDED,"/content/test", false);
        
        fixture.onChange(List.of(change));

        List<LoggingEvent> events = logger.getLoggingEvents();
        assertEquals(1, events.size());
        LoggingEvent event = events.get(0);

        assertAll(
                () -> assertEquals(Level.DEBUG, event.getLevel()),
                () -> assertEquals(3, event.getArguments().size()),
                () -> assertEquals(ChangeType.ADDED, event.getArguments().get(0)),
                () -> assertEquals("/content/test", event.getArguments().get(1)),
                () -> assertEquals(Boolean.FALSE,event.getArguments().get(2))
        );
    }
}