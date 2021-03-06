/*****************************************************************
 *   Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 ****************************************************************/
package org.apache.cayenne.configuration.rop.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.cayenne.DataChannel;
import org.apache.cayenne.configuration.Constants;
import org.apache.cayenne.configuration.ObjectContextFactory;
import org.apache.cayenne.configuration.server.ServerModule;
import org.apache.cayenne.di.Binder;
import org.apache.cayenne.di.DIBootstrap;
import org.apache.cayenne.di.Injector;
import org.apache.cayenne.event.DefaultEventManager;
import org.apache.cayenne.remote.ClientChannel;
import org.apache.cayenne.remote.ClientConnection;
import org.apache.cayenne.remote.MockClientConnection;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("deprecation")
public class ClientModuleTest {

    @Test
    public void testObjectContextFactory() {

        Map<String, String> properties = new HashMap<>();
        ClientModule module = new ClientModule() {

            @Override
            public void configure(Binder binder) {
                super.configure(binder);

                // use a noop connection to prevent startup errors...
                binder.bind(ClientConnection.class).to(MockClientConnection.class);
            }
        };

        Injector injector = DIBootstrap.createInjector(module);

        ObjectContextFactory factory = injector.getInstance(ObjectContextFactory.class);
        assertNotNull(factory);
        assertSame("ObjectContextFactory must be a singleton", factory, injector
                .getInstance(ObjectContextFactory.class));
    }

    @Test
    public void testDataChannel() {

        Map<String, String> properties = new HashMap<>();
        ClientModule module = new ClientModule() {

            @Override
            public void configure(Binder binder) {
                super.configure(binder);

                // use a noop connection to prevent startup errors...
                binder.bind(ClientConnection.class).to(MockClientConnection.class);
                ServerModule.contributeProperties(binder)
                        .put(Constants.SERVER_CONTEXTS_SYNC_PROPERTY, String.valueOf(true));
            }
        };

        Injector injector = DIBootstrap.createInjector(module);

        DataChannel channel = injector.getInstance(DataChannel.class);
        assertNotNull(channel);
        assertTrue(channel instanceof ClientChannel);
        assertSame("DataChannel must be a singleton", channel, injector
                .getInstance(DataChannel.class));

        ClientChannel clientChannel = (ClientChannel) channel;
        assertTrue(clientChannel.getConnection() instanceof MockClientConnection);
        assertTrue(clientChannel.getEventManager() instanceof DefaultEventManager);
        assertFalse(clientChannel.isChannelEventsEnabled());
    }
}
