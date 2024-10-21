package com.musicdistribution.thallforge.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component(immediate = true, service = ServiceUserManager.class)
public class ServiceUserManager {

    private static final String SERVICE_USER_NAME = "admin-service-user";
    private static final String SERVICE_USER_PATH = "/home/users/system";

    @Reference
    private ResourceResolverFactory resourceResolverFactory;
    @Reference
    private SlingRepository repository;
    private ResourceResolver resourceResolver;

    /**
     * This method is called when the component is activated.
     * It creates a service user if it doesn't already exist.
     */
    @Activate
    public void activate() {
        try {
            createServiceUser();
        } catch (Exception e) {
            log.error("Could not create service user", e);
        }
    }

    /**
     * This method is called when the component is deactivated.
     * It ensures that resources like the ResourceResolver are closed.
     */
    @Deactivate
    public void deactivate() {
        if (resourceResolver != null && resourceResolver.isLive()) {
            resourceResolver.close();
        }
    }

    /**
     * Method to create the service user if it doesn't exist.
     *
     * @throws RepositoryException if something goes wrong.
     */
    private void createServiceUser() throws RepositoryException, LoginException {
        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, "admin-subservice");

        resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);
        Session session = resourceResolver.adaptTo(Session.class);
        JackrabbitSession jackrabbitSession = (JackrabbitSession) session;

        if (jackrabbitSession != null) {
            UserManager userManager = jackrabbitSession.getUserManager();
            Authorizable user = userManager.getAuthorizable(SERVICE_USER_NAME);

            if (user == null) {
                userManager.createSystemUser(SERVICE_USER_NAME, SERVICE_USER_PATH);
                session.save();
                log.info("Service user created: {}", SERVICE_USER_NAME);
            } else {
                log.info("Service already exists: {}", SERVICE_USER_NAME);
            }
        }
    }
}
