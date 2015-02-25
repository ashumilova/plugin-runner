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
package com.codenvy.ide.ext.runner.client.tabs.properties.panel;

import com.codenvy.ide.api.mvp.Presenter;
import com.google.inject.ImplementedBy;

/**
 * The common representation of properties panel widget.
 *
 * @author Andrey Plotnikov
 */
@ImplementedBy(PropertiesPanelPresenter.class)
public interface PropertiesPanel extends Presenter {

}