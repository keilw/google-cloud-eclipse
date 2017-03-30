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

import java.net.MalformedURLException;
import java.net.URL;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

class AsyncImageSetterJob extends Job {

  private final URL imageUrl;
  private final Label placeholder;
  private final Display display;

  private Image image;

  AsyncImageSetterJob(String imageUrl, final Label placeholder, int width, int height)
      throws MalformedURLException {
    super("Google User Profile Picture Fetach Job");
    this.imageUrl = new URL(imageUrl);
    this.placeholder = placeholder;
    display = placeholder.getDisplay();
  }

  private static Image resize(Display display, Image image) {
    ImageData imageData = image.getImageData().scaledTo(50, 50);
    Image scaled = new Image(display, imageData);
    image.dispose();
    return scaled;
  }

  @Override
  protected IStatus run(IProgressMonitor monitor) {
    ImageDescriptor descriptor = ImageDescriptor.createFromURL(imageUrl);
    image = descriptor.createImage();
    if (image == null) {
      return Status.OK_STATUS;
    }
    image = resize(display, image);

    try {
      display.syncExec(new UiRunnable());
    } catch (Throwable throwable) {
      image.dispose();
    }
    return Status.OK_STATUS;
  }

  private class UiRunnable implements Runnable {

    @Override
    public void run() {
      if (placeholder.isDisposed()) {
        image.dispose();
      } else {
        placeholder.setImage(image);
        placeholder.addDisposeListener(new DisposeListener() {
          @Override
          public void widgetDisposed(DisposeEvent event) {
            image.dispose();
          }
        });
      }
    }
  }
}
