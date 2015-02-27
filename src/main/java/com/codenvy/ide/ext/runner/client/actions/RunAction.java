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
package com.codenvy.ide.ext.runner.client.actions;

import com.codenvy.api.project.shared.dto.RunnerEnvironment;
import com.codenvy.api.runner.dto.RunOptions;
import com.codenvy.ide.api.action.ActionEvent;
import com.codenvy.ide.api.app.AppContext;
import com.codenvy.ide.api.app.CurrentProject;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.runner.client.RunnerLocalizationConstant;
import com.codenvy.ide.ext.runner.client.RunnerResources;
import com.codenvy.ide.ext.runner.client.manager.RunnerManager;
import com.google.inject.Inject;

import javax.annotation.Nonnull;

/**
 * Action which allows run project with default runner parameters.
 *
 * @author Dmitry Shnurenko
 * @author Valeriy Svydenko
 */
public class RunAction extends AbstractRunnerActions {

    private final RunnerManager      runnerManager;
    private final DtoFactory         dtoFactory;
    private final ChooseRunnerAction chooseRunnerAction;
    private final AppContext         appContext;

    @Inject
    public RunAction(RunnerManager runnerManager,
                     RunnerLocalizationConstant locale,
                     AppContext appContext,
                     ChooseRunnerAction chooseRunnerAction,
                     DtoFactory dtoFactory,
                     RunnerResources resources) {
        super(appContext, locale.actionRun(), locale.actionRunDescription(), resources.runAppImage());

        this.runnerManager = runnerManager;
        this.dtoFactory = dtoFactory;
        this.appContext = appContext;
        this.chooseRunnerAction = chooseRunnerAction;
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(@Nonnull ActionEvent event) {
        CurrentProject currentProject = appContext.getCurrentProject();
        if (currentProject == null) {
            return;
        }

        RunnerEnvironment environment = chooseRunnerAction.getSelectedEnvironment();

        if (environment == null || currentProject.getRunner().equals(environment.getId())) {
            runnerManager.launchRunner();
        } else {
            RunOptions runOptions = dtoFactory.createDto(RunOptions.class).withOptions(environment.getOptions());

            runnerManager.launchRunner(runOptions, environment.getId());
        }
    }
}