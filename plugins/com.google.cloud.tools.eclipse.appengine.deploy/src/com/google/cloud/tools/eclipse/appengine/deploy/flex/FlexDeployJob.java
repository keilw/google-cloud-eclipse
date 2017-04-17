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

package com.google.cloud.tools.eclipse.appengine.deploy.flex;

import com.google.api.client.auth.oauth2.Credential;
import com.google.cloud.tools.appengine.api.deploy.DefaultDeployConfiguration;
import com.google.cloud.tools.appengine.cloudsdk.process.ProcessOutputLineListener;
import com.google.cloud.tools.eclipse.appengine.deploy.DeployJob;
import com.google.cloud.tools.eclipse.appengine.deploy.DeployStaging;
import com.google.cloud.tools.eclipse.appengine.deploy.WarPublisher;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;

public class FlexDeployJob extends DeployJob {

  private final IPath appEngineDirectory;

  public FlexDeployJob(IProject project, Credential credential, IPath workDirectory,
      ProcessOutputLineListener stagingStdoutLineListener,
      ProcessOutputLineListener stderrLineListener, DefaultDeployConfiguration deployConfiguration,
      boolean includeOptionalConfigurationFiles, IPath appEngineDirectory) {
    super(project, credential, workDirectory, stagingStdoutLineListener, stderrLineListener,
        deployConfiguration, includeOptionalConfigurationFiles);
    this.appEngineDirectory = appEngineDirectory;
  }

  @Override
  protected IStatus stage(IPath stagingDirectory, IPath safeWorkDirectory, IProgressMonitor monitor)
      throws CoreException {
    SubMonitor subMonitor = SubMonitor.convert(monitor, 100);

    IPath war = safeWorkDirectory.append("app-to-deploy.war");
    WarPublisher.publishWar(project, war, subMonitor.newChild(40));
    DeployStaging.stageFlexible(appEngineDirectory, war, stagingDirectory,
        subMonitor.newChild(60));

    return Status.OK_STATUS;
  }

  @Override
  protected IPath getOptionalConfigurationFilesDirectory() {
    return appEngineDirectory;
  }
}
