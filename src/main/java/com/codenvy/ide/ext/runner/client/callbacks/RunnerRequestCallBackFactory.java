/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.ide.ext.runner.client.callbacks;

import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.rest.DtoUnmarshallerFactory;
import com.codenvy.ide.websocket.rest.Unmarshallable;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The factory that is used for creating callback for GAE extension.
 *
 * @author Dmitry Shnurenko
 */
@Singleton
public class RunnerRequestCallBackFactory {

    private final DtoUnmarshallerFactory dtoUnmarshallerFactory;
    private final NotificationManager    notificationManager;

    @Inject
    public RunnerRequestCallBackFactory(DtoUnmarshallerFactory dtoUnmarshallerFactory,
                                        NotificationManager notificationManager) {

        this.notificationManager = notificationManager;
        this.dtoUnmarshallerFactory = dtoUnmarshallerFactory;
    }

    /**
     * Returns instance of GAERequestCallBack.
     *
     * @param unmarshallable
     *         unmarshaller which need to convert JSON object to JAVA object
     * @return an instance {@link RunnerRequestCallback}
     */
    public <T> RunnerRequestCallback<T> build(@Nonnull Unmarshallable<T> unmarshallable, @Nonnull SuccessCallback<T> successCallback) {
        return new RunnerRequestCallback<>(notificationManager, unmarshallable, successCallback);
    }

    /**
     * Returns instance of GAERequestCallBack using unmarshaller. Method contains creating of unmarshaller using object's class.
     *
     * @param clazz
     *         java class for which need to create unmarshaller
     * @param successCallback
     *         callback which contains method which is called when operation is success
     * @return an instance {@link RunnerRequestCallback}
     */
    public <T> RunnerRequestCallback<T> build(@Nonnull Class<T> clazz, @Nonnull SuccessCallback<T> successCallback) {
        Unmarshallable<T> unmarshallable = dtoUnmarshallerFactory.newWSUnmarshaller(clazz);

        return new RunnerRequestCallback<>(notificationManager, unmarshallable, successCallback);
    }

    /**
     * Returns instance of GAERequestCallBack using unmarshaller.
     *
     * @param notificationManager
     *         shows different messages to user
     * @param unmarshallable
     *         unmarshaller which need to convert JSON object to JAVA object
     * @param successCallback
     *         callback which contains method which is called when operation is success
     * @param failureCallback
     *         callback which contains method which is called when operation is fail
     * @return an instance {@link RunnerRequestCallback}
     */
    public <T> RunnerRequestCallback build(@Nonnull NotificationManager notificationManager,
                                           @Nullable Unmarshallable<T> unmarshallable,
                                           @Nonnull SuccessCallback<T> successCallback,
                                           @Nullable FailureCallback failureCallback) {

        return new RunnerRequestCallback<>(notificationManager, unmarshallable, successCallback, failureCallback);
    }

    /**
     * Returns instance of GAERequestCallBack using unmarshaller. Method contains creating of unmarshaller using object's class.
     *
     * @param clazz
     *         java class for which need to create unmarshaller
     * @param successCallback
     *         callback which contains method which is called when operation is success
     * @param failureCallback
     *         callback which contains method which is called when operation is failed
     * @return an instance {@link RunnerRequestCallback}
     */
    public <T> RunnerRequestCallback<T> build(@Nonnull Class<T> clazz,
                                              @Nonnull SuccessCallback<T> successCallback,
                                              @Nonnull FailureCallback failureCallback) {
        Unmarshallable<T> unmarshallable = dtoUnmarshallerFactory.newWSUnmarshaller(clazz);

        return new RunnerRequestCallback<>(notificationManager, unmarshallable, successCallback, failureCallback);
    }

}
