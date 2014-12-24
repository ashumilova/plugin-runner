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
package com.codenvy.ide.ext.runner.client.actions;

import com.codenvy.ide.api.action.ActionEvent;
import com.codenvy.ide.api.action.ActionManager;
import com.codenvy.ide.api.action.DefaultActionGroup;
import com.codenvy.ide.api.app.AppContext;
import com.codenvy.ide.api.app.CurrentProject;
import com.codenvy.ide.ext.runner.client.RunnerLocalizationConstant;
import com.codenvy.ide.ext.runner.client.RunnerResources;
import com.google.inject.Inject;

/**
 * Action group which contains list of custom environments.
 *
 * @author Dmitry Shnurenko
 */
public class RunWithGroup extends DefaultActionGroup {
    private final AppContext appContext;

    @Inject
    public RunWithGroup(RunnerLocalizationConstant locale,
                        RunnerResources resources,
                        ActionManager actionManager,
                        AppContext appContext) {

        super(locale.actionGroupRunWith(), true, actionManager);
        this.appContext = appContext;

        getTemplatePresentation().setDescription(locale.actionGroupRunWithDescription());
        getTemplatePresentation().setSVGIcon(resources.runWithImage());
    }

    /** {@inheritDoc} */
    @Override
    public void update(ActionEvent e) {
        CurrentProject currentProject = appContext.getCurrentProject();

        e.getPresentation().setEnabled(currentProject != null && !currentProject.isReadOnly());
    }
}
