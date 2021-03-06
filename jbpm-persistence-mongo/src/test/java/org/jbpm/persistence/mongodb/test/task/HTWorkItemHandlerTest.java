package org.jbpm.persistence.mongodb.test.task;
/**
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import org.jbpm.persistence.mongodb.task.service.MongoTaskServiceFactory;
import org.jbpm.services.task.wih.NonManagedLocalHTWorkItemHandler;
import org.junit.After;
import org.junit.Before;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.internal.task.api.InternalTaskService;

public class HTWorkItemHandlerTest extends HTWorkItemHandlerBaseTest {

    private WorkItemHandler htWorkItemHandler;
    
    @Before
    public void setUp() throws Exception {
        this.taskService = (InternalTaskService) MongoTaskServiceFactory.newTaskServiceConfigurator()
				.getTaskService();
        htWorkItemHandler = new NonManagedLocalHTWorkItemHandler(getKSession(), taskService);
 
        setHandler(htWorkItemHandler);
    }

    @After
    public void tearDown() throws Exception {
        ((InternalTaskService)taskService).removeAllTasks();
    }
}
