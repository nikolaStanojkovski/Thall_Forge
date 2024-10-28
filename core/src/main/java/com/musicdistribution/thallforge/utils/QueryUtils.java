package com.musicdistribution.thallforge.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.ResourceResolver;

import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

@Slf4j
public final class QueryUtils {

    public static NodeIterator executeQuery(ResourceResolver resourceResolver,
                                            String queryString,
                                            int limit) {
        try {
            Session session = resourceResolver.adaptTo(Session.class);
            if (session == null) {
                return null;
            }
            Workspace workspace = session.getWorkspace();
            QueryManager queryManager = workspace.getQueryManager();
            Query searchQuery
                    = queryManager.createQuery(queryString, "JCR-SQL2");
            if (limit > 0) {
                searchQuery.setLimit(limit);
            }
            return searchQuery.execute().getNodes();
        } catch (NullPointerException | RepositoryException e) {
            log.error("Could not execute the query '{}'", queryString, e);
            throw new RuntimeException(e);
        }
    }
}
