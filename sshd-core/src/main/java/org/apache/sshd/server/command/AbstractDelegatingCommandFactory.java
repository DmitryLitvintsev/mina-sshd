/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.sshd.server.command;

import org.apache.sshd.common.util.ValidateUtils;
import org.apache.sshd.common.util.logging.AbstractLoggingBean;

/**
 * TODO Add javadoc
 *
 * @author <a href="mailto:dev@mina.apache.org">Apache MINA SSHD Project</a>
 */
public abstract class AbstractDelegatingCommandFactory extends AbstractLoggingBean implements DelegatingCommandFactory {
    private final String name;
    /*
     * NOTE: we expose setters since there is no problem to change these settings between
     * successive invocations of the 'createCommand' method
     */
    private CommandFactory delegate;

    protected AbstractDelegatingCommandFactory(String name) {
        this.name = ValidateUtils.checkNotNullAndNotEmpty(name, "No delegating command factory name provided");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public CommandFactory getDelegateCommandFactory() {
        return delegate;
    }

    @Override
    public void setDelegateCommandFactory(CommandFactory factory) {
        delegate = factory;
    }

    @Override
    public Command createCommand(String command) {
        if (isSupportedCommand(command)) {
            return executeSupportedCommand(command);
        }

        CommandFactory factory = getDelegateCommandFactory();
        if (factory != null) {
            return factory.createCommand(command);
        }

        return createUnsupportedCommand(command);
    }

    protected abstract Command executeSupportedCommand(String command);

    protected Command createUnsupportedCommand(String command) {
        throw new IllegalArgumentException("Unknown command to execute: " + command);
    }
}
