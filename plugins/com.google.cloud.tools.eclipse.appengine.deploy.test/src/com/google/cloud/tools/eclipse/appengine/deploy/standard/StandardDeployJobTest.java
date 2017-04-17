/*
 * Copyright 2016 Google Inc.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import com.google.api.client.auth.oauth2.Credential;
import com.google.cloud.tools.appengine.api.deploy.DefaultDeployConfiguration;
import com.google.cloud.tools.appengine.cloudsdk.process.ProcessOutputLineListener;
import com.google.cloud.tools.eclipse.appengine.facets.AppEngineStandardFacet;
import com.google.cloud.tools.eclipse.test.util.project.TestProjectCreator;
import java.io.IOException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jst.common.project.facet.core.JavaFacet;
import org.eclipse.jst.j2ee.web.project.facet.WebFacetUtils;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class StandardDeployJobTest {

  private static final IProjectFacetVersion APPENGINE_STANDARD_FACET_VERSION_1 =
      ProjectFacetsManager.getProjectFacet(AppEngineStandardFacet.ID).getVersion("1");

  @Rule public TestProjectCreator projectCreator = new TestProjectCreator().withFacetVersions(
      JavaFacet.VERSION_1_7, WebFacetUtils.WEB_25, APPENGINE_STANDARD_FACET_VERSION_1);

  private IProject project;
  private IPath safeWorkDirectory;
  private IPath stagingDirectory;

  @Before
  public void setUp() throws IOException {
    project = projectCreator.getProject();
    safeWorkDirectory = project.getFolder("safe-work-directory").getLocation();
    stagingDirectory = project.getFolder("staging-result").getLocation();
  }

  @Test
  public void testStage() throws CoreException {
    StandardDeployJob job = newStandardDeployJob();
    job.stage(project, stagingDirectory, safeWorkDirectory, new NullProgressMonitor());

    assertTrue(stagingDirectory.append("WEB-INF").toFile().exists());
    assertTrue(stagingDirectory.append("WEB-INF/appengine-generated").toFile().exists());
    assertTrue(stagingDirectory.append("META-INF").toFile().exists());
    assertTrue(stagingDirectory.append("app.yaml").toFile().exists());
  }

  @Test
  public void testGetOptionalConfigurationFilesDirectory() throws CoreException {
    StandardDeployJob job = newStandardDeployJob();
    job.stage(project, stagingDirectory, safeWorkDirectory, new NullProgressMonitor());

    assertEquals(stagingDirectory.append("WEB-INF/appengine-generated"),
        job.getOptionalConfigurationFilesDirectory());
  }

  private StandardDeployJob newStandardDeployJob() {
    return new StandardDeployJob(mock(IProject.class), mock(Credential.class), mock(IPath.class),
        mock(ProcessOutputLineListener.class), mock(ProcessOutputLineListener.class),
        mock(DefaultDeployConfiguration.class), false);
  }
}
