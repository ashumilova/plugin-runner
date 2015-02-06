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

import com.codenvy.ide.ext.runner.client.state.PanelState;
import com.codenvy.ide.ext.runner.client.state.State;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Andrey Plotnikov
 */
public class TabContainerPresenterImpl implements TabContainerPresenter, TabContainerView.ActionDelegate, PanelState.StateChangeListener {

    private final TabContainerView     view;
    private final PanelState           panelState;
    private final Map<String, Tab>     tabs;
    private final Map<String, Boolean> tabVisibilities;

    @Inject
    public TabContainerPresenterImpl(TabContainerView view, PanelState panelState) {
        this.view = view;
        this.view.setDelegate(this);
        this.panelState = panelState;

        tabs = new LinkedHashMap<>();
        tabVisibilities = new LinkedHashMap<>();

        panelState.addListener(this);
    }

    /** {@inheritDoc} */
    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    /** {@inheritDoc} */
    @Override
    public void addTab(@Nonnull Tab tab) {
        String title = tab.getTitle();

        if (tabs.containsKey(title)) {
            throw new IllegalStateException("Tab with title " + title + " has already added. Please, check your title name.");
        }

        view.addTab(tab);
        tabs.put(title, tab);
        tabVisibilities.put(title, true);
    }

    /** {@inheritDoc} */
    @Override
    public void onTabClicked(@Nonnull String title) {
        Tab tab = tabs.get(title);
        tab.performHandler();

        view.showTab(tab);
    }

    /** {@inheritDoc} */
    @Override
    public void onStateChanged() {
        State state = panelState.getState();
        Tab firstTab = null;

        for (Tab tab : tabs.values()) {
            boolean visible = tab.isAvailableScope(state);
            if (visible) {
                firstTab = tab;
            }

            tabVisibilities.put(tab.getTitle(), visible);
        }

        view.setVisibleTitle(tabVisibilities);

        if (firstTab != null) {
            view.showTab(firstTab);
        }
    }

}