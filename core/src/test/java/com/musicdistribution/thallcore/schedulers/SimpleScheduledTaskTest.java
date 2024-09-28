package com.musicdistribution.thallcore.schedulers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.musicdistribution.thallcore.schedulers.SimpleScheduledTask;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

@ExtendWith(AemContextExtension.class)
class SimpleScheduledTaskTest {

  private final SimpleScheduledTask fixture = new SimpleScheduledTask();

  private final TestLogger logger = TestLoggerFactory.getTestLogger(fixture.getClass());

  @BeforeEach
  void setup() {
    TestLoggerFactory.clear();
  }

  @Test
  void run() {
    SimpleScheduledTask.Config config = mock(SimpleScheduledTask.Config.class);
    when(config.myParameter()).thenReturn("parameter value");
    fixture.activate(config);
    fixture.run();
    List<LoggingEvent> events = logger.getLoggingEvents();
    assertEquals(1, events.size());
    LoggingEvent event = events.get(0);
    assertEquals(Level.DEBUG, event.getLevel());
    assertEquals(1, event.getArguments().size());
    assertEquals("parameter value", event.getArguments().get(0));
  }
}
