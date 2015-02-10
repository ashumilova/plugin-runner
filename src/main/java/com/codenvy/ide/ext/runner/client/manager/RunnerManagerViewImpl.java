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
package com.codenvy.ide.ext.runner.client.manager;

import com.codenvy.ide.api.parts.PartStackUIResources;
import com.codenvy.ide.api.parts.base.BaseView;
import com.codenvy.ide.ext.runner.client.RunnerLocalizationConstant;
import com.codenvy.ide.ext.runner.client.RunnerResources;
import com.codenvy.ide.ext.runner.client.inject.factories.WidgetFactory;
import com.codenvy.ide.ext.runner.client.models.Runner;
import com.codenvy.ide.ext.runner.client.tab.TabContainer;
import com.codenvy.ide.ext.runner.client.widgets.button.ButtonWidget;
import com.codenvy.ide.ext.runner.client.widgets.console.Console;
import com.codenvy.ide.ext.runner.client.widgets.tab.TabType;
import com.codenvy.ide.ext.runner.client.widgets.tab.TabWidget;
import com.codenvy.ide.ext.runner.client.widgets.terminal.Terminal;
import com.codenvy.ide.ext.runner.client.widgets.tooltip.MoreInfo;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

import static com.codenvy.ide.ext.runner.client.widgets.tab.Background.GREY;
import static com.codenvy.ide.ext.runner.client.widgets.tab.TabType.LEFT_PANEL;

/**
 * Class provides view representation of runner panel.
 *
 * @author Dmitry Shnurenko
 * @author Valeriy Svydenko
 */
public class RunnerManagerViewImpl extends BaseView<RunnerManagerView.ActionDelegate> implements RunnerManagerView {
    interface RunnerManagerViewImplUiBinder extends UiBinder<Widget, RunnerManagerViewImpl> {
    }

    private static final RunnerManagerViewImplUiBinder UI_BINDER = GWT.create(RunnerManagerViewImplUiBinder.class);

    private static final String GWT_POPUP_STANDARD_STYLE = "gwt-PopupPanel";
    private static final String SPLITTER_STYLE_NAME      = "gwt-SplitLayoutPanel-HDragger";

    private static final int SHIFT_LEFT     = 100;
    private static final int SHIFT_TOP      = 130;
    private static final int SPLITTER_WIDTH = 2;

    @UiField(provided = true)
    SplitLayoutPanel mainPanel;

    @UiField
    SimplePanel leftTabsPanel;

    @UiField
    FlowPanel otherButtonsPanel;
    @UiField
    FlowPanel runButtonPanel;
    @UiField
    FlowPanel tabsPanel;
    @UiField
    FlowPanel textArea;

    //info panel
    @UiField
    Label     appReference;
    @UiField
    FlowPanel moreInfoPanel;
    @UiField
    Label     timeout;
    @UiField
    Image     image;

    @UiField(provided = true)
    final RunnerResources            resources;
    @UiField(provided = true)
    final RunnerLocalizationConstant locale;

    private final WidgetFactory         widgetFactory;
    private final Map<Runner, Console>  consoles;
    private final Map<Runner, Terminal> terminals;
    private final PopupPanel            popupPanel;
    private final MoreInfo              moreInfoWidget;

    private TabWidget consoleTab;
    private TabWidget terminalTab;

    private ButtonWidget run;
    private ButtonWidget stop;
    private ButtonWidget clean;
    private ButtonWidget docker;

    private String url;

    @Inject
    public RunnerManagerViewImpl(PartStackUIResources partStackUIResources,
                                 RunnerResources resources,
                                 RunnerLocalizationConstant locale,
                                 WidgetFactory widgetFactory,
                                 PopupPanel popupPanel) {
        super(partStackUIResources);

        this.resources = resources;
        this.locale = locale;
        this.widgetFactory = widgetFactory;
        this.mainPanel = new SplitLayoutPanel(SPLITTER_WIDTH);

        titleLabel.setText(locale.runnersPanelTitle());
        container.add(UI_BINDER.createAndBindUi(this));

        this.mainPanel.setWidgetMinSize(leftTabsPanel, 165);

        this.consoles = new HashMap<>();
        this.terminals = new HashMap<>();
        this.moreInfoWidget = widgetFactory.createMoreInfo();

        this.popupPanel = popupPanel;
        this.popupPanel.removeStyleName(GWT_POPUP_STANDARD_STYLE);
        this.popupPanel.add(moreInfoWidget);

        addMoreInfoPanelHandler();

        changeSplitterStyle();

        initializeTabs();

        initializeButtons();
    }

    private void addMoreInfoPanelHandler() {
        moreInfoPanel.addDomHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                image.addStyleName(resources.runnerCss().opacityButton());

                delegate.onMoreInfoBtnMouseOver();
            }
        }, MouseOverEvent.getType());

        moreInfoPanel.addDomHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                image.removeStyleName(resources.runnerCss().opacityButton());

                popupPanel.hide();
            }
        }, MouseOutEvent.getType());
    }

    private void changeSplitterStyle() {
        int widgetCount = mainPanel.getWidgetCount();

        for (int i = 0; i < widgetCount; i++) {
            Widget widget = mainPanel.getWidget(i);
            String styleName = widget.getStyleName();

            if (SPLITTER_STYLE_NAME.equals(styleName)) {
                widget.removeStyleName(styleName);
                widget.addStyleName(resources.runnerCss().splitter());
            }
        }
    }

    private void initializeTabs() {
        TabWidget.ActionDelegate consoleDelegate = new TabWidget.ActionDelegate() {
            @Override
            public void onMouseClicked() {
                delegate.onConsoleButtonClicked();
            }
        };
        consoleTab = createTab(locale.runnerTabConsole(), consoleDelegate, tabsPanel, LEFT_PANEL);
        consoleTab.select(GREY);

        TabWidget.ActionDelegate terminalDelegate = new TabWidget.ActionDelegate() {
            @Override
            public void onMouseClicked() {
                delegate.onTerminalButtonClicked();
            }
        };
        terminalTab = createTab(locale.runnerTabTerminal(), terminalDelegate, tabsPanel, LEFT_PANEL);
    }

    @Nonnull
    private TabWidget createTab(@Nonnull String tabName,
                                @Nonnull TabWidget.ActionDelegate actionDelegate,
                                @Nonnull FlowPanel tabsPanel,
                                @Nonnull TabType tabType) {
        TabWidget tabWidget = widgetFactory.createTab(tabName, tabType);
        tabWidget.setDelegate(actionDelegate);

        tabsPanel.add(tabWidget);

        return tabWidget;
    }

    private void initializeButtons() {
        ButtonWidget.ActionDelegate runDelegate = new ButtonWidget.ActionDelegate() {
            @Override
            public void onButtonClicked() {
                delegate.onRunButtonClicked();
            }
        };
        run = createButton(resources.runButton(), runDelegate, runButtonPanel);

        ButtonWidget.ActionDelegate stopDelegate = new ButtonWidget.ActionDelegate() {
            @Override
            public void onButtonClicked() {
                delegate.onStopButtonClicked();
            }
        };
        stop = createButton(resources.stopButton(), stopDelegate, otherButtonsPanel);

        ButtonWidget.ActionDelegate cleanDelegate = new ButtonWidget.ActionDelegate() {
            @Override
            public void onButtonClicked() {
                delegate.onCleanConsoleButtonClicked();
            }
        };
        clean = createButton(resources.cleanButton(), cleanDelegate, otherButtonsPanel);

        ButtonWidget.ActionDelegate dockerDelegate = new ButtonWidget.ActionDelegate() {
            @Override
            public void onButtonClicked() {
                delegate.onDockerButtonClicked();
            }
        };
        docker = createButton(resources.dockerButton(), dockerDelegate, otherButtonsPanel);
    }

    @Nonnull
    private ButtonWidget createButton(@Nonnull ImageResource icon,
                                      @Nonnull ButtonWidget.ActionDelegate delegate,
                                      @Nonnull FlowPanel buttonPanel) {
        ButtonWidget button = widgetFactory.createButton(icon);
        button.setDelegate(delegate);
        button.setDisable();

        buttonPanel.add(button);

        return button;
    }

    /** {@inheritDoc} */
    @Override
    public void update(@Nonnull Runner runner) {
        changeButtonsState(runner);

        Terminal terminal = terminals.get(runner);
        if (terminal != null) {
            terminal.update(runner);
        }

        moreInfoWidget.update(runner);
    }

    private void changeButtonsState(@Nonnull Runner runner) {
        run.setEnable();
        stop.setEnable();
        clean.setEnable();
        docker.setEnable();

        switch (runner.getStatus()) {
            case IN_QUEUE:
                run.setDisable();
                docker.setDisable();
                stop.setDisable();
                break;

            case FAILED:
                stop.setDisable();
                docker.setDisable();
                break;

            case STOPPED:
                stop.setDisable();
                break;

            default:
        }
    }

    /** {@inheritDoc} */
    @Override
    public void addRunner(@Nonnull Runner runner) {
        //TODO  method addRunner relocated to history presenter and it is called from manager presenter.
        //TODO It seems method for adding console must be on console presenter which need create at first
        Console console = widgetFactory.createConsole(runner);
        consoles.put(runner, console);
        activateConsole(runner);
    }

    /** {@inheritDoc} */
    @Override
    public void setApplicationURl(@Nullable String applicationUrl) {
        url = null;
        appReference.removeStyleName(resources.runnerCss().cursor());

        if (applicationUrl != null && applicationUrl.startsWith("http")) {
            url = applicationUrl;
            appReference.addStyleName(resources.runnerCss().cursor());
        }

        appReference.setText(applicationUrl);
    }

    /** {@inheritDoc} */
    @Override
    public void setTimeout(@Nonnull String timeoutValue) {
        timeout.setText(timeoutValue);
    }

    /** {@inheritDoc} */
    @Override
    public void printMessage(@Nonnull Runner runner, @Nonnull String line) {
        Console console = consoles.get(runner);
        if (console == null) {
            return;
        }

        console.print(line);
    }

    /** {@inheritDoc} */
    @Override
    public void printInfo(@Nonnull Runner runner, @Nonnull String line) {
        Console console = consoles.get(runner);
        if (console == null) {
            return;
        }

        console.printInfo(line);
    }

    /** {@inheritDoc} */
    @Override
    public void printError(@Nonnull Runner runner, @Nonnull String line) {
        Console console = consoles.get(runner);
        if (console == null) {
            return;
        }

        console.printError(line);
    }

    /** {@inheritDoc} */
    @Override
    public void printWarn(@Nonnull Runner runner, @Nonnull String line) {
        Console console = consoles.get(runner);
        if (console == null) {
            return;
        }

        console.printWarn(line);
    }

    /** {@inheritDoc} */
    @Override
    public void clearConsole(@Nonnull Runner runner) {
        Console console = consoles.get(runner);
        if (console == null) {
            return;
        }

        console.clear();
    }

    /** {@inheritDoc} */
    @Override
    public void activateConsole(@Nonnull Runner runner) {
        consoleTab.select(GREY);
        terminalTab.unSelect();

        for (Terminal terminal : terminals.values()) {
            terminal.setVisible(false);
        }

        for (Console console : consoles.values()) {
            console.setVisible(false);
        }

        Console console = consoles.get(runner);
        if (console == null) {
            textArea.clear();
            return;
        }

        console.setVisible(true);
        textArea.add(console);

        console.scrollBottom();
    }


    /** {@inheritDoc} */
    @Override
    public void activateTerminal(@Nonnull Runner runner) {
        terminalTab.select(GREY);
        consoleTab.unSelect();

        for (Console console : consoles.values()) {
            console.setVisible(false);
        }

        for (Terminal terminal : terminals.values()) {
            terminal.setVisible(false);
            terminal.setUnavailableLabelVisible(false);
        }

        Terminal terminal = terminals.get(runner);
        if (terminal == null) {
            terminal = widgetFactory.createTerminal();
            terminal.update(runner);

            terminals.put(runner, terminal);

            textArea.add(terminal);
        } else {
            boolean isAnyAppRun = runner.isAlive();

            terminal.setVisible(isAnyAppRun);
            terminal.setUnavailableLabelVisible(!isAnyAppRun);
        }

    }

    /** {@inheritDoc} */
    @Override
    public void showMoreInfoPopup(@Nonnull Runner runner) {
        moreInfoWidget.update(runner);

        int x = timeout.getAbsoluteLeft() - SHIFT_LEFT;
        int y = timeout.getAbsoluteTop() - SHIFT_TOP;

        popupPanel.setPopupPosition(x, y);
        popupPanel.show();
    }

    /** {@inheritDoc} */
    @Override
    public void updateMoreInfoPopup(@Nonnull Runner runner) {
        moreInfoWidget.update(runner);
    }

    /** {@inheritDoc} */
    @Override
    public void setLeftPanel(@Nonnull TabContainer containerPresenter) {
        containerPresenter.go(leftTabsPanel);
    }

    /** {@inheritDoc} */
    @Override
    public void hideOtherButtons() {
        otherButtonsPanel.setVisible(false);
    }

    /** {@inheritDoc} */
    @Override
    public void showOtherButtons() {
        otherButtonsPanel.setVisible(true);
    }

    @UiHandler("appReference")
    public void onAppReferenceClicked(@SuppressWarnings("UnusedParameters") ClickEvent clickEvent) {
        if (url != null) {
            Window.open(url, "_blank", "");
        }
    }
}