package com.musicdistribution.thallcore.testcontext;

import static com.adobe.cq.wcm.core.components.testing.mock.ContextPlugins.CORE_COMPONENTS;
import static org.apache.sling.testing.mock.caconfig.ContextPlugins.CACONFIG;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextCallback;
import org.apache.sling.testing.mock.sling.ResourceResolverType;

/**
 * Sets up {@link AemContext} for unit tests in this application.
 */
public final class AppAemContext {

  /**
   * Custom set up rules required in all unit tests.
   */
  private static final AemContextCallback SETUP_CALLBACK = context -> {
    // custom project initialization code for every unit test
  };

  private AppAemContext() {
    // static methods only
  }

  /**
   * @return {@link AemContext}
   */
  public static AemContext newAemContext() {
    return newAemContextBuilder().build();
  }

  /**
   * @return {@link AemContextBuilder}
   */
  public static AemContextBuilder newAemContextBuilder() {
    return newAemContextBuilder(ResourceResolverType.RESOURCERESOLVER_MOCK);
  }

  /**
   * @return {@link AemContextBuilder}
   */
  public static AemContextBuilder newAemContextBuilder(ResourceResolverType resourceResolverType) {
    return new AemContextBuilder()
        .plugin(CACONFIG)
        .plugin(CORE_COMPONENTS)
        .afterSetUp(SETUP_CALLBACK);
  }
}
