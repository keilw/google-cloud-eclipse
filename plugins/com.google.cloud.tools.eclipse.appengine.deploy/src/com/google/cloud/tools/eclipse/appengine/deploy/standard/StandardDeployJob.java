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

  private IPath optionalConfigurationFilesDirectory;

  public StandardDeployJob(IProject project, Credential credential, IPath workDirectory,
      ProcessOutputLineListener stagingStdoutLineListener,
      ProcessOutputLineListener stderrLineListener, DefaultDeployConfiguration deployConfiguration,
      boolean includeOptionalConfigurationFiles) {
    super(project, credential, workDirectory, stagingStdoutLineListener, stderrLineListener,
        deployConfiguration, includeOptionalConfigurationFiles);
  }

  @Override
  protected IStatus stage(IPath stagingDirectory, IPath safeWorkDirectory, IProgressMonitor monitor)
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
