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

package com.google.cloud.tools.eclipse.appengine.deploy.standard;

import com.google.api.client.auth.oauth2.Credential;
import com.google.cloud.tools.appengine.api.deploy.DefaultDeployConfiguration;
import com.google.cloud.tools.appengine.cloudsdk.CloudSdk;
import com.google.cloud.tools.appengine.cloudsdk.process.ProcessOutputLineListener;
import com.google.cloud.tools.eclipse.appengine.deploy.DeployJob;
import com.google.cloud.tools.eclipse.appengine.deploy.DeployStaging;
import com.google.cloud.tools.eclipse.appengine.deploy.WarPublisher;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubMonitor;

public class StandardDeployJob extends DeployJob {

  public StandardDeployJob(IProject project, Credential credential, IPath workDirectory,
      ProcessOutputLineListener stagingStdoutLineListener,
      ProcessOutputLineListener stderrLineListener, DefaultDeployConfiguration deployConfiguration,
      boolean includeOptionalConfigurationFiles) {
    super(project, credential, workDirectory, stagingStdoutLineListener, stderrLineListener,
        deployConfiguration, includeOptionalConfigurationFiles);
  }

  private IPath optionalConfigurationFilesDirectory;

  @Override
  protected IStatus stage(IProject project, IPath stagingDirectory, IPath safeWorkDirectory,
      IProgressMonitor monitor)
      throws CoreException {
    SubMonitor progress = SubMonitor.convert(monitor, 100);

    RecordProcessError stagingExitListener = new RecordProcessError();
    CloudSdk cloudSdk = getCloudSdk(null /* credential */, stagingStdoutLineListener,
        stagingExitListener);

    WarPublisher.publishExploded(project, safeWorkDirectory, progress.newChild(40));
    DeployStaging.stageStandard(safeWorkDirectory, stagingDirectory, cloudSdk,
        progress.newChild(60));

    optionalConfigurationFilesDirectory =
        stagingDirectory.append(DeployStaging.STANDARD_STAGING_GENERATED_FILES_DIRECTORY);
    return stagingExitListener.getExitStatus();
  }

  @Override
  protected IPath getOptionalConfigurationFilesDirectory() {
    return optionalConfigurationFilesDirectory;
  }
}
