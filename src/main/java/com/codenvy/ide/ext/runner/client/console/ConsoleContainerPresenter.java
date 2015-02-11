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
package com.codenvy.ide.ext.runner.client.console;

import com.codenvy.ide.ext.runner.client.inject.factories.WidgetFactory;
import com.codenvy.ide.ext.runner.client.models.Runner;
import com.codenvy.ide.ext.runner.client.selection.Selection;
import com.codenvy.ide.ext.runner.client.selection.SelectionManager;
import com.codenvy.ide.ext.runner.client.widgets.console.Console;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

import static com.codenvy.ide.ext.runner.client.selection.Selection.ENVIRONMENT;

/**
 * @author Andrey Plotnikov
 * @author Valeriy Svydenko
 */
@Singleton
public class ConsoleContainerPresenter implements ConsoleContainer,
                                                  ConsoleContainerView.ActionDelegate,
                                                  SelectionManager.SelectionChangeListener {

    private final ConsoleContainerView view;
    private final SelectionManager     selectionManager;
    private final WidgetFactory        widgetFactory;
    private final Map<Runner, Console> consoles;

    @Inject
    public ConsoleContainerPresenter(ConsoleContainerView view, WidgetFactory widgetFactory, SelectionManager selectionManager) {
        this.view = view;
        this.view.setDelegate(this);
        this.widgetFactory = widgetFactory;
        this.selectionManager = selectionManager;

        this.selectionManager.addListener(this);

        consoles = new HashMap<>();
    }

    /** {@inheritDoc} */
    @Override
    public void print(@Nonnull Runner runner, @Nonnull String message) {
        Console console = getConsoleOrCreate(runner);
        console.print(message);
    }

    /** {@inheritDoc} */
    @Override
    public void printInfo(@Nonnull Runner runner, @Nonnull String message) {
        Console console = getConsoleOrCreate(runner);
        console.printInfo(message);
    }

    /** {@inheritDoc} */
    @Override
    public void printError(@Nonnull Runner runner, @Nonnull String message) {
        Console console = getConsoleOrCreate(runner);
        console.printError(message);
    }

    /** {@inheritDoc} */
    @Override
    public void printWarn(@Nonnull Runner runner, @Nonnull String message) {
        Console console = getConsoleOrCreate(runner);
        console.printWarn(message);
    }

    /** {@inheritDoc} */
    @Override
    public void clear(@Nonnull Runner runner) {
        Console console = getConsoleOrCreate(runner);

        console.clear();
    }

    /** {@inheritDoc} */
    @Override
    public void onSelectionChanged(@Nonnull Selection selection) {
        if (ENVIRONMENT.equals(selection)) {
            return;
        }

        Runner runner = selectionManager.getRunner();
        if (runner == null) {
            return;
        }

        view.showWidget(getConsoleOrCreate(runner));
    }

    @Nonnull
    private Console getConsoleOrCreate(@Nonnull Runner runner) {
        Console result = consoles.get(runner);
        if (result == null) {
            result = widgetFactory.createConsole(runner);
            consoles.put(runner, result);
        }

        return result;
    }

    /** {@inheritDoc} */
    @Nonnull
    @Override
    public IsWidget getView() {
        return view;
    }

    /** {@inheritDoc} */
    @Override
    public void setVisible(boolean visible) {
        view.setVisible(visible);
    }

    /** {@inheritDoc} */
    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);
    }

}