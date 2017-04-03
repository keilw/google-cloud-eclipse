/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.tools.eclipse.appengine.newproject.flex;

import com.google.cloud.tools.eclipse.appengine.newproject.AppEngineProjectConfig.BuildTool;
import com.google.cloud.tools.eclipse.appengine.newproject.AppEngineWizardPage;
import com.google.cloud.tools.eclipse.appengine.newproject.Messages;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class AppEngineFlexWizardPage extends AppEngineWizardPage {

  // TODO(chanseok): remove archetype-based creation for Standard too, and push the field,
  // "createControl()", and "getBuildTool()" down to super class.
  // https://github.com/GoogleCloudPlatform/google-cloud-eclipse/issues/1326
  private Button asMavenProject;

  public AppEngineFlexWizardPage() {
    super(false);
    setTitle(Messages.getString("app.engine.flex.project")); //$NON-NLS-1$
    setDescription(Messages.getString("create.app.engine.flex.project")); //$NON-NLS-1$
  }

  @Override
  public void sendAnalyticsPing(Shell shell) {
    // TODO: send analytics ping
  }

  @Override
  public void setHelp(Composite container) {
    PlatformUI.getWorkbench().getHelpSystem().setHelp(container,
        "com.google.cloud.tools.eclipse.appengine.newproject.NewFlexProjectContext"); //$NON-NLS-1$
  }

  @Override
  public void createControl(Composite parent) {
    super.createControl(parent);

    Composite container = (Composite) getControl();
    Composite composite = new Composite(container, SWT.NONE);
    GridLayoutFactory.swtDefaults().numColumns(2).generateLayout(composite);

    asMavenProject = new Button(composite, SWT.LEAD | SWT.CHECK);
    asMavenProject.setText(Messages.getString("create.maven.project")); //$NON-NLS-1$
  }

  @Override
  public BuildTool getBuildTool() {
    if (asMavenProject.getSelection()) {
      return BuildTool.MAVEN;
    }
    return BuildTool.NONE;
  }
}
