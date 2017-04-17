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

public class StandardDeployJobTest {
/*
  private static final IProjectFacetVersion APPENGINE_STANDARD_FACET_VERSION_1 =
      ProjectFacetsManager.getProjectFacet(AppEngineStandardFacet.ID).getVersion("1");

  @Rule public TestProjectCreator projectCreator = new TestProjectCreator().withFacetVersions(
      JavaFacet.VERSION_1_7, WebFacetUtils.WEB_25, APPENGINE_STANDARD_FACET_VERSION_1);

  private IProject project;
  private IPath safeWorkDirectory;
  private IPath stagingDirectory;

  @Before
  public void setUp() throws IOException {
    IProject project = projectCreator.getProject();
    safeWorkDirectory = project.getFolder("safe-work-directory").getLocation();
    stagingDirectory = project.getFolder("staging-result").getLocation();
  }

  @Test
  public void testStage() throws CoreException {
    StandardDeployJob job = newStandardDeployJob();
    job.stage(project, stagingDirectory, safeWorkDirectory, new NullProgressMonitor());
    for (String f : stagingDirectory.toFile().list()) {
      System.out.println(f);
    }
    System.out.println("--");
    for (String f : safeWorkDirectory.toFile().list()) {
      System.out.println(f);
    }
  }

  private StandardDeployJob newStandardDeployJob() {
    return new StandardDeployJob(mock(IProject.class), mock(Credential.class), mock(IPath.class),
        mock(ProcessOutputLineListener.class), mock(ProcessOutputLineListener.class),
        mock(DefaultDeployConfiguration.class), false);
  }
*/
}
