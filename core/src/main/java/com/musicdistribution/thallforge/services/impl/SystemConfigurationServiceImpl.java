package com.musicdistribution.thallforge.services.impl;

import com.musicdistribution.thallforge.services.SystemConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Slf4j
@Component(service = SystemConfigurationService.class)
@Designate(ocd = SystemConfigurationServiceImpl.Config.class)
public class SystemConfigurationServiceImpl implements SystemConfigurationService {

    private final boolean isAuthor;
    private final boolean isPublish;

    @Activate
    public SystemConfigurationServiceImpl(Config config) {
        this.isAuthor = config.authorInstance();
        this.isPublish = config.publishInstance();
    }

    @Override
    public boolean isAuthor() {
        return isAuthor;
    }

    @Override
    public boolean isPublish() {
        return isPublish;
    }

    @ObjectClassDefinition(name = "Thallforge: System Configuration Service")
    public @interface Config {

        @AttributeDefinition(name = "Author instance")
        boolean authorInstance() default false;

        @AttributeDefinition(name = "Publish instance")
        boolean publishInstance() default false;
    }
}
