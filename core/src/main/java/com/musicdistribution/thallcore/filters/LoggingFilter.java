package com.musicdistribution.thallcore.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.engine.EngineConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple servlet filter component that logs incoming requests.
 */
@Component(service = Filter.class,
    property = {
        EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.FILTER_SCOPE_REQUEST,
    })
@ServiceDescription("Demo to filter incoming requests")
@ServiceRanking(-700)
@ServiceVendor("Adobe")
public class LoggingFilter implements Filter {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void doFilter(final ServletRequest request, final ServletResponse response,
      final FilterChain filterChain) throws IOException, ServletException {
    final SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
    logger.debug("request for {}, with selector {}", slingRequest
        .getRequestPathInfo().getResourcePath(), slingRequest
        .getRequestPathInfo().getSelectorString());
    filterChain.doFilter(request, response);
  }

  @Override
  public void destroy() {
  }

  @Override
  public void init(final FilterConfig filterConfig) {
  }
}