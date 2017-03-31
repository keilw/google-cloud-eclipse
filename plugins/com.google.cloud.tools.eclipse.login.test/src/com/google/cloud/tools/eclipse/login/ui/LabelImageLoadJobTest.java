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

package com.google.cloud.tools.eclipse.login.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.cloud.tools.eclipse.test.util.http.TestHttpServer;
import com.google.cloud.tools.eclipse.test.util.ui.ShellTestResource;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Label;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LabelImageLoadJobTest {

  @Rule public ShellTestResource shellResource = new ShellTestResource();
  @Rule public TestHttpServer server = new TestHttpServer(
      "sample.gif", LabelImageLoaderTest.someImageBytes);

  private Job loadJob;
  private Label label;

  @Before
  public void setUp() {
    label = new Label(shellResource.getShell(), SWT.NONE);
  }

  @After
  public void tearDown() {
    assertEquals(Job.NONE, loadJob.getState());

    Image image = label.getImage();
    label.dispose();
    if (image != null) {
      assertTrue(image.isDisposed());
    }

    LabelImageLoader.cache.clear();
  }

  @Test
  public void test() {
    loadJob = new LabelImageLoadJob(server.getAddress() + "sample.gif");
  }
}
