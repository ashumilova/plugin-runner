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
package com.codenvy.ide.ext.runner.client.inject.factories;

import com.codenvy.ide.ext.runner.client.manager.button.ButtonWidget;
import com.codenvy.ide.ext.runner.client.manager.info.MoreInfo;
import com.codenvy.ide.ext.runner.client.models.Runner;
import com.codenvy.ide.ext.runner.client.tabs.console.button.ConsoleButton;
import com.codenvy.ide.ext.runner.client.tabs.console.panel.Console;
import com.codenvy.ide.ext.runner.client.tabs.console.panel.FullLogMessageWidget;
import com.codenvy.ide.ext.runner.client.tabs.container.tab.TabType;
import com.codenvy.ide.ext.runner.client.tabs.container.tab.TabWidget;
import com.codenvy.ide.ext.runner.client.tabs.history.runner.RunnerWidget;
import com.codenvy.ide.ext.runner.client.tabs.properties.button.PropertyButtonWidget;
import com.codenvy.ide.ext.runner.client.tabs.properties.panel.PropertiesPanel;
import com.codenvy.ide.ext.runner.client.tabs.properties.panel.common.Scope;
import com.codenvy.ide.ext.runner.client.tabs.templates.environment.EnvironmentWidget;
import com.codenvy.ide.ext.runner.client.tabs.templates.scopebutton.ScopeButton;
import com.codenvy.ide.ext.runner.client.tabs.templates.typebutton.TypeButton;
import com.codenvy.ide.ext.runner.client.tabs.terminal.panel.Terminal;
import com.google.gwt.resources.client.ImageResource;

import org.vectomatic.dom.svg.ui.SVGImage;
import org.vectomatic.dom.svg.ui.SVGResource;

import javax.annotation.Nonnull;

/**
 * The factory for creating an instances of the widget.
 *
 * @author Dmitry Shnurenko
 */
public interface WidgetFactory {

    /**
     * Creates button widget with special icon.
     *
     * @param prompt
     *         prompt for current button which is displayed on special popup widget
     * @param resource
     *         icon which need set to button
     * @return an instance of {@link ButtonWidget}
     */
    @Nonnull
    ButtonWidget createButton(@Nonnull String prompt, @Nonnull ImageResource resource);

    /**
     * Creates console button widget with special icon.
     *
     * @param prompt
     *         prompt for current button which is displayed on special popup widget
     * @param resource
     *         icon which need set to button
     * @return an instance of {@link ConsoleButton}
     */
    @Nonnull
    ConsoleButton createConsoleButton(@Nonnull String prompt, @Nonnull SVGResource resource);

    /**
     * Creates tab widget with special title.
     *
     * @param title
     *         title which need set to widget's special place
     * @param tabType
     *         enum which contains string value of height
     * @return an instance of {@link TabWidget}
     */
    @Nonnull
    TabWidget createTab(@Nonnull String title, @Nonnull TabType tabType);

    /**
     * Creates runner widget.
     *
     * @return an instance of {@link RunnerWidget}
     */
    @Nonnull
    RunnerWidget createRunner();

    /**
     * Creates environment widget.
     *
     * @return an instance of {@link EnvironmentWidget}
     */
    @Nonnull
    EnvironmentWidget createEnvironment();

    /**
     * Creates a console widget for a given runner.
     *
     * @param runner
     *         runner that needs to be bound with a widget
     * @return an instance of {@link Console}
     */
    @Nonnull
    Console createConsole(@Nonnull Runner runner);

    /**
     * Creates terminal widget.
     *
     * @return an instance of {@link Terminal}
     */
    @Nonnull
    Terminal createTerminal();

    /**
     * Creates a properties panel widget for a given runner.
     *
     * @param runner
     *         runner that needs to be bound with a widget
     * @return an instance of {@link PropertiesPanel}
     */
    @Nonnull
    PropertiesPanel createPropertiesPanel(@Nonnull Runner runner);

    /**
     * Creates more info popup widget.
     *
     * @return an instance of {@link MoreInfo}
     */
    @Nonnull
    MoreInfo createMoreInfo();

    /**
     * Creates message widget that need to be displayed in the console.
     *
     * @param logUrl
     *         url where full log is located
     * @return an instance of {@link FullLogMessageWidget}
     */
    @Nonnull
    FullLogMessageWidget createFullLogMessage(@Nonnull String logUrl);

    /**
     * Creates type button widget.
     *
     * @return an instance of {@link TypeButton}
     */
    @Nonnull
    TypeButton createTypeButton();

    /**
     * Creates scope button widget with special image.
     *
     * @param buttonScope
     *         scope of current button
     * @param image
     *         image which need set to button
     * @param isChecked
     *         flag which defines is checked button or un checked
     * @return an instance of {@link ScopeButton}
     */
    @Nonnull
    ScopeButton createScopeButton(@Nonnull Scope buttonScope, @Nonnull SVGImage image, boolean isChecked);

    /**
     * Creates property button widget.
     *
     * @param title
     *         title of button
     * @return an instance of {@link PropertyButtonWidget}
     */
    @Nonnull
    PropertyButtonWidget createPropertyButton(@Nonnull String title);

}