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
package com.codenvy.ide.ext.runner.client.tab;

import com.codenvy.ide.ext.runner.client.state.State;
import com.codenvy.ide.ext.runner.client.tab.TabContainer.TabSelectHandler;
import com.codenvy.ide.ext.runner.client.widgets.tab.TabType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Set;

import static com.codenvy.ide.ext.runner.client.tab.Tab.VisibleState.REMOVABLE;

/**
 * @author Andrey Plotnikov
 */
public class Tab {

    private final String           title;
    private final TabPresenter     tabPresenter;
    private final Set<State>       scopes;
    private final TabSelectHandler handler;
    private final TabType          tabType;
    private final VisibleState     visibleState;

    public Tab(@Nonnull String title,
               @Nonnull TabPresenter tabPresenter,
               @Nonnull Set<State> scopes,
               @Nullable TabSelectHandler handler,
               @Nonnull TabType tabType,
               @Nonnull VisibleState visibleState) {
        this.title = title;
        this.tabPresenter = tabPresenter;
        this.scopes = scopes;
        this.handler = handler;
        this.tabType = tabType;
        this.visibleState = visibleState;
    }

    @Nonnull
    public String getTitle() {
        return title;
    }

    @Nonnull
    public TabPresenter getTab() {
        return tabPresenter;
    }

    public boolean isAvailableScope(@Nonnull State scope) {
        return scopes.contains(scope);
    }

    public void performHandler() {
        if (handler == null) {
            return;
        }

        handler.onTabSelected();
    }

    @Nonnull
    public TabType getTabType() {
        return tabType;
    }

    public boolean isRemovable() {
        return REMOVABLE.equals(visibleState);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Tab)) {
            return false;
        }

        Tab tab = (Tab)other;
        return Objects.equals(title, tab.title);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    public enum VisibleState {
        REMOVABLE, VISIBLE
    }

}