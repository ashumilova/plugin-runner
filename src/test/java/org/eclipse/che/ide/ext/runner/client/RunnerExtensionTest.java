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
package org.eclipse.che.ide.ext.runner.client;

import org.eclipse.che.ide.api.action.ActionManager;
import org.eclipse.che.ide.api.action.DefaultActionGroup;
import org.eclipse.che.ide.api.constraints.Anchor;
import org.eclipse.che.ide.api.constraints.Constraints;
import org.eclipse.che.ide.api.parts.PartStackType;
import org.eclipse.che.ide.api.parts.WorkspaceAgent;
import org.eclipse.che.ide.ext.runner.client.actions.ChooseRunnerAction;
import org.eclipse.che.ide.ext.runner.client.actions.RunAction;
import org.eclipse.che.ide.ext.runner.client.manager.RunnerManagerPresenter;
import com.google.gwtmockito.GwtMockitoTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.eclipse.che.ide.api.action.IdeActions.GROUP_BUILD_TOOLBAR;
import static org.eclipse.che.ide.api.action.IdeActions.GROUP_MAIN_CONTEXT_MENU;
import static org.eclipse.che.ide.api.action.IdeActions.GROUP_MAIN_TOOLBAR;
import static org.eclipse.che.ide.api.action.IdeActions.GROUP_RUN_CONTEXT_MENU;
import static org.eclipse.che.ide.api.action.IdeActions.GROUP_RUN_TOOLBAR;
import static org.eclipse.che.ide.ext.runner.client.RunnerExtension.BUILDER_PART_ID;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Andrey Plotnikov
 * @author Valeriy Svydenko
 */
@RunWith(GwtMockitoTestRunner.class)
public class RunnerExtensionTest {

    @Captor
    private ArgumentCaptor<Constraints>        constraintsCaptor;
    @Captor
    private ArgumentCaptor<DefaultActionGroup> actionGroupCaptor;
    @Mock
    private RunnerResources.RunnerCss          runnerCss;
    @Mock
    private RunnerResources                    resources;
    private RunnerExtension                    extension;

    @Before
    public void setUp() throws Exception {
        when(resources.runnerCss()).thenReturn(runnerCss);

        extension = new RunnerExtension(resources);
    }

    @Test
    public void cssResourcesShouldBeInjected() throws Exception {
        verify(runnerCss).ensureInjected();
    }

    @Test
    public void runnerPanelShouldBeOpened() throws Exception {
        WorkspaceAgent workspaceAgent = mock(WorkspaceAgent.class);
        RunnerManagerPresenter runnerManagerPresenter = mock(RunnerManagerPresenter.class);

        extension.setUpRunnerConsole(workspaceAgent, runnerManagerPresenter);

        verify(workspaceAgent).openPart(eq(runnerManagerPresenter), eq(PartStackType.INFORMATION), constraintsCaptor.capture());
        verifyConstants(Anchor.AFTER, BUILDER_PART_ID);
    }


    @Test
    public void runnerMenuActionsShouldBeAdded() throws Exception {
        // prepare step
        ActionManager actionManager = mock(ActionManager.class);

        RunAction runAction = mock(RunAction.class);
        ChooseRunnerAction chooseRunnerAction = mock(ChooseRunnerAction.class);

        DefaultActionGroup mainToolbarGroup = mock(DefaultActionGroup.class);
        DefaultActionGroup runContextGroup = mock(DefaultActionGroup.class);
        DefaultActionGroup contextMenuGroup = mock(DefaultActionGroup.class);

        when(actionManager.getAction(GROUP_MAIN_TOOLBAR)).thenReturn(mainToolbarGroup);
        when(actionManager.getAction(GROUP_RUN_CONTEXT_MENU)).thenReturn(runContextGroup);
        when(actionManager.getAction(GROUP_MAIN_CONTEXT_MENU)).thenReturn(contextMenuGroup);

        // test step
        extension.setUpRunActions(actionManager, runAction, chooseRunnerAction);

        // check step
        verify(mainToolbarGroup).add(actionGroupCaptor.capture(), constraintsCaptor.capture());
        verifyConstants(Anchor.AFTER, GROUP_BUILD_TOOLBAR);

        verify(runContextGroup).addSeparator();
        verify(runContextGroup).add(runAction);
        verify(contextMenuGroup).add(runContextGroup);

        DefaultActionGroup runToolbarGroup = actionGroupCaptor.getValue();
        assertThat(runToolbarGroup.getChildrenCount(), is(2));

        verify(actionManager).registerAction(GROUP_RUN_TOOLBAR, runToolbarGroup);

        verifyConstants(Anchor.AFTER, GROUP_BUILD_TOOLBAR);
    }

    private void verifyConstants(Anchor anchor, String actionId) {
        Constraints constraints = constraintsCaptor.getValue();
        assertThat(constraints.myAnchor, is(anchor));
        assertThat(constraints.myRelativeToActionId, is(actionId));
    }

}