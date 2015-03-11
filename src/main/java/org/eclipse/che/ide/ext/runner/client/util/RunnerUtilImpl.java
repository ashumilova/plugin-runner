/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.ext.runner.client.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.ide.api.notification.Notification;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.ext.runner.client.RunnerLocalizationConstant;
import org.eclipse.che.ide.ext.runner.client.manager.RunnerManagerPresenter;
import org.eclipse.che.ide.ext.runner.client.models.Runner;
import org.eclipse.che.ide.ext.runner.client.tabs.console.container.ConsoleContainer;
import org.eclipse.che.ide.ui.dialogs.DialogFactory;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.eclipse.che.ide.api.notification.Notification.Status.FINISHED;
import static org.eclipse.che.ide.api.notification.Notification.Type.ERROR;
import static org.eclipse.che.ide.ext.runner.client.tabs.properties.panel.common.RAM.MB_128;

/**
 * Contains implementations of methods which are general for runner plugin classes.
 *
 * @author Dmitry Shnurenko
 */
@Singleton
public class RunnerUtilImpl implements RunnerUtil {

    private final DialogFactory              dialogFactory;
    private final RunnerLocalizationConstant locale;
    private final RunnerManagerPresenter     presenter;
    private final NotificationManager        notificationManager;
    private final ConsoleContainer           consoleContainer;

    @Inject
    public RunnerUtilImpl(DialogFactory dialogFactory,
                          RunnerLocalizationConstant locale,
                          RunnerManagerPresenter presenter,
                          ConsoleContainer consoleContainer,
                          NotificationManager notificationManager) {
        this.dialogFactory = dialogFactory;
        this.locale = locale;
        this.presenter = presenter;
        this.notificationManager = notificationManager;
        this.consoleContainer = consoleContainer;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRunnerMemoryCorrect(@Nonnegative int totalMemory, @Nonnegative int usedMemory, @Nonnegative int availableMemory) {
        if (usedMemory < 0 || totalMemory < 0 || availableMemory < 0) {
            showWarning(locale.messagesIncorrectValue());
            return false;
        }

        if (usedMemory % MB_128.getValue() != 0) {
            showWarning(locale.ramSizeMustBeMultipleOf(MB_128.getValue()));
            return false;
        }

        if (usedMemory > totalMemory) {
            showWarning(locale.messagesTotalRamLessCustom(usedMemory, totalMemory));
            return false;
        }

        if (usedMemory > availableMemory) {
            showWarning(locale.messagesAvailableRamLessCustom(usedMemory, totalMemory, totalMemory - availableMemory));
            return false;
        }

        return true;
    }

    /** {@inheritDoc} */
    @Override
    public void showWarning(@Nonnull String message) {
        dialogFactory.createMessageDialog(locale.titlesWarning(), message, null).show();
    }

    /** {@inheritDoc} */
    @Override
    public void showError(@Nonnull Runner runner, @Nonnull String message, @Nullable Throwable exception) {
        Notification notification = new Notification(message, ERROR, true);

        showError(runner, message, exception, notification);

        notificationManager.showNotification(notification);
    }

    /** {@inheritDoc} */
    @Override
    public void showError(@Nonnull Runner runner,
                          @Nonnull String message,
                          @Nullable Throwable exception,
                          @Nonnull Notification notification) {
        runner.setStatus(Runner.Status.FAILED);

        presenter.update(runner);

        notification.update(message, ERROR, FINISHED, null, true);

        if (exception != null && exception.getMessage() != null) {
            consoleContainer.printError(runner, message + ": " + exception.getMessage());
        } else {
            consoleContainer.printError(runner, message);
        }
    }

}