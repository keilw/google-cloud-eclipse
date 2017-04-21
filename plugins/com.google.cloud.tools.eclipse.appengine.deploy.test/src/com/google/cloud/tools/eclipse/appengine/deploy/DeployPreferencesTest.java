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

package com.google.cloud.tools.eclipse.appengine.deploy;

import static org.hamcrest.text.IsEmptyString.isEmptyString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.google.cloud.tools.eclipse.test.util.project.TestProjectCreator;
import org.eclipse.core.resources.IProject;
import org.junit.Rule;
import org.junit.Test;

public class DeployPreferencesTest {

  @Rule public final TestProjectCreator projectCreator = new TestProjectCreator();

  @Test
  public void testDefaultProjectId() {
    assertThat(DeployPreferences.DEFAULT_PROJECT_ID, isEmptyString());
  }

  @Test
  public void testSetProjectId() {
    IProject project = projectCreator.getProject();
    DeployPreferences preferences = new DeployPreferences(project);
    assertThat(preferences.getProjectId(), isEmptyString());
    preferences.setProjectId("someproject32");
    assertEquals("someproject32", preferences.getProjectId());
    preferences.setProjectId(null);
    assertThat(preferences.getProjectId(), isEmptyString());
  }

  @Test
  public void testDefaultAccountEmail() {
    assertThat(DeployPreferences.DEFAULT_ACCOUNT_EMAIL, isEmptyString());
  }

  @Test
  public void testDefaultVersion() {
    assertThat(DeployPreferences.DEFAULT_CUSTOM_VERSION, isEmptyString());
  }

  @Test
  public void testDefaultAutoPromote() {
    assertTrue(DeployPreferences.DEFAULT_ENABLE_AUTO_PROMOTE);
  }

  @Test
  public void testDefaultBucket() {
    assertThat(DeployPreferences.DEFAULT_CUSTOM_BUCKET, isEmptyString());
  }

  @Test
  public void testDefaultStopPreviousVersion() {
    assertTrue(DeployPreferences.DEFAULT_STOP_PREVIOUS_VERSION);
  }

  @Test
  public void testIncludeOptionalConfigurationFiles() {
    assertTrue(DeployPreferences.DEFAULT_INCLUDE_OPTIONAL_CONFIGURATION_FILES);
  }

}
