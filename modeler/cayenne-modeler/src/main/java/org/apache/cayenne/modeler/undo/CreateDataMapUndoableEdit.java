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
package org.apache.cayenne.modeler.undo;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.apache.cayenne.configuration.DataChannelDescriptor;
import org.apache.cayenne.map.DataMap;
import org.apache.cayenne.modeler.action.CreateDataMapAction;
import org.apache.cayenne.modeler.action.RemoveAction;
import org.apache.cayenne.modeler.event.DomainDisplayEvent;

public class CreateDataMapUndoableEdit extends CayenneUndoableEdit {

    @Override
    public String getPresentationName() {
        return "Create DataMap";
    }

    private DataChannelDescriptor domain;
    private DataMap map;

    public CreateDataMapUndoableEdit(DataChannelDescriptor domain, DataMap map) {
        this.domain = domain;
        this.map = map;
    }

    @Override
    public void redo() throws CannotRedoException {
        CreateDataMapAction action = actionManager.getAction(CreateDataMapAction.class);
        action.createDataMap(map);
    }

    @Override
    public void undo() throws CannotUndoException {
        RemoveAction action = actionManager.getAction(RemoveAction.class);

        controller.fireDomainDisplayEvent(new DomainDisplayEvent(this, domain));

        action.removeDataMap(map);
    }
}
