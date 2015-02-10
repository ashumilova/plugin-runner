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
package com.codenvy.ide.ext.runner.client.widgets.tab;

import com.codenvy.ide.api.mvp.View;

import javax.annotation.Nonnull;

/**
 * Provides methods which allow change visual representation of tab.
 *
 * @author Dmitry Shnurenko
 */
public interface TabWidget extends View<TabWidget.ActionDelegate> {

    /**
     * Performs some actions when tab is selected.
     *
     * @param background
     *         parameter which need to set correct background color
     */
    void select(@Nonnull Background background);

    /** Performs some actions when tab is unselected. */
    void unSelect();

    interface ActionDelegate {
        /** Performs some actions in response to user's clicking on the tab. */
        void onMouseClicked();
    }

}