package com.musicdistribution.thallforge.schedulers;

import com.musicdistribution.thallforge.services.ArtistQueryService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Slf4j
@Designate(ocd = ArtistDropdownOptionsPopulateScheduler.Config.class)
@Component(service = ArtistDropdownOptionsPopulateScheduler.class, immediate = true)
public class ArtistDropdownOptionsPopulateScheduler {

    @Reference
    private ArtistQueryService artistQueryService;

    @Reference
    private ResourceResolverRetrievalService resourceResolverRetrievalService;

    @Reference(policy = ReferencePolicy.STATIC)
    private Scheduler scheduler;

    private Integer schedulerId;

    @Activate
    protected final void activate(final Config config) {
        removeScheduler();
        addScheduler(config);
    }

    @Deactivate
    protected final void deactivate() {
        removeScheduler();
    }

    @Modified
    protected final void modified(final Config config) {
        removeScheduler();
        addScheduler(config);
    }

    private void addScheduler(final Config config) {
        String schedulerCronExpression = config.scheduler_expression();
        if (StringUtils.isNotBlank(schedulerCronExpression)) {
            this.schedulerId = ArtistDropdownOptionsPopulateScheduler.class.hashCode();
            ScheduleOptions options = scheduler.EXPR(schedulerCronExpression);
            options.name(String.valueOf(schedulerId));
            options.canRunConcurrently(config.scheduler_concurrent());
            scheduler.schedule(new ArtistDropdownOptionsPopulateJob(resourceResolverRetrievalService, artistQueryService), options);
        } else {
            log.error("Could not setup scheduler. Cron expression for setting up the scheduler is empty");
        }
    }

    private void removeScheduler() {
        scheduler.unschedule(String.valueOf(schedulerId));
    }

    @ObjectClassDefinition(name = "Artist dropdown options populate scheduler",
            description = "A scheduler that populates the dropdown-options for the available artists, with the path and name")
    public @interface Config {

        @AttributeDefinition(name = "Cron-job expression")
        String scheduler_expression() default "0 0 8/12 ? * * *";

        @AttributeDefinition(name = "Concurrent task",
                description = "Whether or not to schedule this task concurrently")
        boolean scheduler_concurrent() default false;
    }
}
