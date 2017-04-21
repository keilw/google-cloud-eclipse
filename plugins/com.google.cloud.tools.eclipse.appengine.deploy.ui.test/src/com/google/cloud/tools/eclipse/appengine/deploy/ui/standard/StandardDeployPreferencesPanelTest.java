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

package com.google.cloud.tools.eclipse.appengine.deploy.ui.standard;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.google.cloud.tools.eclipse.login.IGoogleLoginService;
import com.google.cloud.tools.eclipse.projectselector.ProjectRepository;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

public class StandardDeployPreferencesPanelTest {

  @Test
  public void testGetHelpContextId() {
    StandardDeployPreferencesPanel panel = new StandardDeployPreferencesPanel(mock(Shell.class),
        mock(IProject.class), mock(IGoogleLoginService.class), mock(Runnable.class), false,
        mock(ProjectRepository.class));
    assertEquals(
        "com.google.cloud.tools.eclipse.appengine.deploy.ui.DeployAppEngineStandardProjectContext",
        panel.getHelpContextId());
  }
}
