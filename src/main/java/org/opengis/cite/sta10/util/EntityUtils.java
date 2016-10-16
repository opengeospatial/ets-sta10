package org.opengis.cite.sta10.util;

import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.dao.BaseDao;
import de.fraunhofer.iosb.ilt.sta.model.Entity;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility methods for comparing results and cleaning the service.
 *
 * @author Hylke van der Schaaf
 */
public class EntityUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityUtils.class.getName());

    /**
     * Class returned by checks on results. Encapsulates the result of the
     * check, and the message.
     */
    public static class resultTestResult {

        public final boolean testOk;
        public final String message;

        public resultTestResult(boolean testOk, String message) {
            this.testOk = testOk;
            this.message = message;
        }

    }

    public static resultTestResult resultContains(EntityList<? extends Entity> result, Entity... entities) {
        return resultContains(result, new ArrayList(Arrays.asList(entities)));
    }

    /**
     * Checks if the list contains all the given entities exactly once.
     *
     * @param result
     * @param entityList
     * @return
     */
    public static resultTestResult resultContains(EntityList<? extends Entity> result, List<? extends Entity> entityList) {
        long count = result.getCount();
        if (count != -1 && count != entityList.size()) {
            LOGGER.info("Result count ({}) not equal to expected count ({})", count, entityList.size());
            return new resultTestResult(false, "Result count " + count + " not equal to expected count (" + entityList.size() + ")");
        }
        Iterator<? extends Entity> it;
        for (it = result.fullIterator(); it.hasNext();) {
            Entity next = it.next();
            Entity inList = findEntityIn(next, entityList);
            if (!entityList.remove(inList)) {
                LOGGER.info("Entity with id {} found in result that is not expected.", next.getId());
                return new resultTestResult(false, "Entity with id " + next.getId() + " found in result that is not expected.");
            }
        }
        if (!entityList.isEmpty()) {
            LOGGER.info("Expected entity not found in result.");
            return new resultTestResult(false, entityList.size() + " expected entities not in result.");
        }
        return new resultTestResult(true, "Check ok.");
    }

    public static Entity findEntityIn(Entity entity, List<? extends Entity> entities) {
        Long id = entity.getId();
        for (Entity inList : entities) {
            if (Objects.equals(inList.getId(), id)) {
                return inList;
            }
        }
        return null;
    }

    public static void deleteAll(SensorThingsService sts) throws ServiceFailureException {
        deleteAll(sts.things());
        deleteAll(sts.locations());
        deleteAll(sts.sensors());
        deleteAll(sts.featuresOfInterest());
        deleteAll(sts.observedProperties());
        deleteAll(sts.observations());
    }

    public static <T extends Entity> void deleteAll(BaseDao<T> doa) throws ServiceFailureException {
        boolean more = true;
        int count = 0;
        while (more) {
            EntityList<T> entities = doa.query().list();
            if (entities.getCount() > 0) {
                LOGGER.info("{} to go.", entities.getCount());
            } else {
                more = false;
            }
            for (T entity : entities) {
                doa.delete(entity);
                count++;
            }
        }
        LOGGER.info("Deleted {} using {}.", count, doa.getClass().getName());
    }

}
