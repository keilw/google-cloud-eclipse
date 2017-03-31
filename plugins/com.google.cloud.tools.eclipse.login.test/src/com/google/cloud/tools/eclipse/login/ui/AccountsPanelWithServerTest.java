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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.google.cloud.tools.eclipse.login.IGoogleLoginService;
import com.google.cloud.tools.eclipse.test.util.http.TestHttpServer;
import com.google.cloud.tools.eclipse.test.util.ui.ShellTestResource;
import com.google.cloud.tools.login.Account;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccountsPanelWithServerTest {

  @Rule public ShellTestResource shellTestResource = new ShellTestResource();
  @Rule public TestHttpServer server1 = new TestHttpServer(
      "sample1.gif", LabelImageLoaderTest.someImageBytes);
  @Rule public TestHttpServer server2 = new TestHttpServer(
      "sample2.gif", LabelImageLoaderTest.someImageBytes);

  private Shell shell;

  @Mock private IGoogleLoginService loginService;
  @Mock private Account account1;
  @Mock private Account account2;
  @Mock private Account account3;

  @Before
  public void setUp() {
    shell = shellTestResource.getShell();
    when(account1.getEmail()).thenReturn("alice@example.com");
    when(account2.getEmail()).thenReturn("bob@example.com");
    when(account1.getName()).thenReturn("Alice");
    when(account2.getName()).thenReturn("Bob");
    when(account1.getAvatarUrl()).thenReturn(server1.getAddress() + "sample1.gif");
    when(account2.getAvatarUrl()).thenReturn(server2.getAddress() + "sample2.gif");

    when(loginService.hasAccounts()).thenReturn(false);
    when(loginService.getAccounts()).thenReturn(Sets.newHashSet(account1, account2));
  }

  @Test
  public void testAvatarsLoaded() throws InterruptedException {
    AccountsPanel panel = new AccountsPanel(null, loginService);

    Control control = panel.createDialogArea(shell);
    joinAllImageLoadJobs();

    List<Label> avatarLabels = collectAvatarLabels(control);
    assertEquals(2, avatarLabels.size());
    assertNotNull(avatarLabels.get(0).getImage());
    assertNotNull(avatarLabels.get(0).getImage());
  }

  private void joinAllImageLoadJobs() throws InterruptedException {
    for (Job job : Job.getJobManager().find(null)) {
      if (job instanceof LabelImageLoadJob) {
        job.join();
      }
    }
  }

  private static List<Label> collectAvatarLabels(Control dialogArea) {
    List<Label> imageLabels = new ArrayList<>();

    Control[] controls = ((Composite) dialogArea).getChildren();
    for (int i = 0; i + 2 < controls.length; i += 2) {
      Composite accountRow = (Composite) controls[i];
      Label imageLabel = (Label) accountRow.getChildren()[0];
      imageLabels.add(imageLabel);

      assertTrue(imageLabel.getText().isEmpty());
      assertEquals(SWT.SEPARATOR, ((Label) controls[i+1]).getStyle() & SWT.SEPARATOR);
    }
    return imageLabels;
  }
}
