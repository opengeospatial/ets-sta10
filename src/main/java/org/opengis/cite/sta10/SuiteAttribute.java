package org.opengis.cite.sta10;

import com.sun.jersey.api.client.Client;
import org.w3c.dom.Document;

/**
 * An enumerated type defining ISuite attributes that may be set to constitute a
 * shared test fixture.
 */
@SuppressWarnings("rawtypes")
public enum SuiteAttribute {

    /**
     * A client component for interacting with HTTP endpoints.
     */
    CLIENT("httpClient", Client.class),
    /**
     * A DOM Document representation of the test subject or metadata about it.
     */
    TEST_SUBJECT("testSubject", Document.class),
    /**
     * An integer denoting the conformance level to check. A given conformance
     * level includes all lower levels.
     */
    LEVEL("level", Integer.class),
    /**
     * Address of the MQTT server including port (e.g. tcp://localhost:1883)
     */
    MQTT_SERVER("mqttServer", String.class),
    /**
     * Timeout used to wait for messages on MQTT in milliseconds (e.g. 3000
     * equals 3 seconds)
     */
    MQTT_TIMEOUT("mqttTimeout", Long.class);

    private final Class attrType;
    private final String attrName;

    private SuiteAttribute(String attrName, Class attrType) {
        this.attrName = attrName;
        this.attrType = attrType;
    }

    public Class getType() {
        return attrType;
    }

    public String getName() {
        return attrName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(attrName);
        sb.append('(').append(attrType.getName()).append(')');
        return sb.toString();
    }
}
