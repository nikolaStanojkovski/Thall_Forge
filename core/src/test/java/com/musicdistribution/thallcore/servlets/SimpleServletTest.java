package com.musicdistribution.thallcore.servlets;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.musicdistribution.thallcore.servlets.SimpleServlet;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class SimpleServletTest {

  private final SimpleServlet fixture = new SimpleServlet();

  @Test
  void doGet(AemContext context) throws ServletException, IOException {
    context.build().resource("/content/test", "jcr:title", "resource title").commit();
    context.currentResource("/content/test");
    MockSlingHttpServletRequest request = context.request();
    MockSlingHttpServletResponse response = context.response();
    fixture.doGet(request, response);
    assertEquals("Title = resource title", response.getOutputAsString());
  }
}
