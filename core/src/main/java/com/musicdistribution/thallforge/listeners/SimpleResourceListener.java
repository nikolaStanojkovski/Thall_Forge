package com.musicdistribution.thallforge.listeners;

import java.util.List;

import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A service to demonstrate how changes in the resource tree
 * can be listened for. 
 * Please note, that apart from EventHandler services,
 * the immediate flag should not be set on a service.
 */
@Component(service = ResourceChangeListener.class,
           immediate = true
)
@ServiceDescription("Demo to listen on changes in the resource tree")
public class SimpleResourceListener implements ResourceChangeListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onChange(List<ResourceChange> changes) {
        changes.forEach(change ->
            logger.debug("Resource event: {} at: {} isExternal {}",
                change.getType(), change.getPath(), change.isExternal()));
        
    }
}

