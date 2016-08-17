/*
 * Copyright 2016 Open Geospatial Consortium.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opengis.cite.sta10.receiveUpdatesViaMQTT;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;
import org.testng.Assert;

/**
 *
 * @author jab
 */
public class MqttListener implements Callable<JSONObject> {

    private final static String CLIENT_ID = "STA-test_suite";
    private static final int QOS = 2;

    private final CountDownLatch barrier;
    private final String topic;
    private final String mqttServerUri;

    private MqttAsyncClient mqttClient;
    private JSONObject result;

    public MqttListener(String mqttServer, String topic) {
        this.mqttServerUri = mqttServer;
        this.topic = topic;
        barrier = new CountDownLatch(1);
    }

    public void connect() {
        try {
            final CountDownLatch connectBarrier = new CountDownLatch(1);
            mqttClient = new MqttAsyncClient(mqttServerUri, CLIENT_ID + "-" + topic + "-" + UUID.randomUUID(), new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            mqttClient.connect(connOpts, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    mqttClient.setCallback(new MqttCallback() {
                        @Override
                        public void connectionLost(Throwable thrwbl) {
                            Assert.fail("MQTT connection lost.");
                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage mm) throws Exception {
                            if (barrier.getCount() > 0) {
                                result = new JSONObject(new String(mm.getPayload(), StandardCharsets.UTF_8));
                                barrier.countDown();
                            }
                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken imdt) {
                        }
                    });
                    try {
                        mqttClient.subscribe(topic, QOS, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken imt) {
                                connectBarrier.countDown();
                            }

                            @Override
                            public void onFailure(IMqttToken imt, Throwable thrwbl) {
                                Assert.fail("MQTT subscribe failed.", thrwbl);
                            }
                        });
                    } catch (MqttException ex) {
                        Assert.fail("Error MQTT subscribe.", ex);
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Assert.fail("MQTT connect failed.", exception);
                }
            });
            try {
                connectBarrier.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(Capability8Test.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (MqttException | IllegalArgumentException ex) {
            Assert.fail("Could not connect to MQTT server.", ex);
        }
    }

    @Override
    public JSONObject call() throws Exception {
        try {
            barrier.await();
        } catch (InterruptedException ex) {
            Assert.fail("waiting for MQTT events exceeded time limit.", ex);
            throw ex;
        } finally {
            if (mqttClient != null) {
                if (mqttClient.isConnected()) {
                    mqttClient.unsubscribe(topic, null, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken imt) {
                            try {
                                mqttClient.disconnect(null, new IMqttActionListener() {
                                    @Override
                                    public void onSuccess(IMqttToken imt) {
                                        try {
                                            mqttClient.close();
                                        } catch (MqttException ex) {
                                            Assert.fail("Error closing MQTT connection.", ex);
                                        }
                                    }

                                    @Override
                                    public void onFailure(IMqttToken imt, Throwable thrwbl) {
                                        try {
                                            mqttClient.disconnectForcibly();
                                            mqttClient.close();
                                        } catch (MqttException ex) {
                                            Assert.fail("Error disconnecting MQTT.", ex);
                                        }
                                    }
                                });
                            } catch (MqttException ex) {
                                Assert.fail("Error Error disconnecting from MQTT", ex);
                            }
                        }

                        @Override
                        public void onFailure(IMqttToken imt, Throwable thrwbl) {
                            try {
                                mqttClient.disconnect(null, new IMqttActionListener() {
                                    @Override
                                    public void onSuccess(IMqttToken imt) {
                                        try {
                                            mqttClient.close();
                                        } catch (MqttException ex) {
                                            Assert.fail("Error closing MQTT connection.", ex);
                                        }
                                    }

                                    @Override
                                    public void onFailure(IMqttToken imt, Throwable thrwbl) {
                                        try {
                                            mqttClient.disconnectForcibly();
                                            mqttClient.close();
                                        } catch (MqttException ex) {
                                            Assert.fail("Error disconnecting MQTT.", ex);
                                        }
                                    }
                                });
                            } catch (MqttException ex) {
                                Assert.fail("Error Error disconnecting from MQTT", ex);
                            }
                        }
                    });

                }
            }
        }
        return result;
    }
}
