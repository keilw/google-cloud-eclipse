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

package com.google.cloud.tools.eclipse.appengine.compat;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.TransformerException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;

import com.google.cloud.tools.eclipse.util.NatureUtils;
import com.google.cloud.tools.eclipse.util.Xslt;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;

public class GpeMigrator {

  private static final Logger logger = Logger.getLogger(GpeMigrator.class.getName());

  private static final ImmutableList<String> GPE_CLASSPATH_ENTRIES_PATH = ImmutableList.of(
      "org.eclipse.jst.server.core.container/com.google.appengine.server.runtimeTarget/Google App Engine",
      "com.google.appengine.eclipse.core.GAE_CONTAINER",
      "com.google.appengine.eclipse.wtp.GAE_WTP_CONTAINER"
  );

  private static final String GPE_GAE_NATURE_ID = "com.google.appengine.eclipse.core.gaeNature";

  private static final String WTP_METADATA_XSLT = "/xslt/wtpMetadata.xsl";

  // FacetedProject.METADATA_FILE = ".settings/" + FacetCorePlugin.PLUGIN_ID + ".xml";
  private static final String FACETS_METADATA_FILE =
      ".settings/org.eclipse.wst.common.project.facet.core.xml";

  /**
   * Removes various GPE-related remnants: classpath entries, nature, runtime, and facets. Any
   * error during operation is logged but ignored.
   */
  public static void removeObsoleteGpeRemnants(
      final IFacetedProject facetedProject, IProgressMonitor monitor) {
    SubMonitor subMonitor = SubMonitor.convert(monitor, 40);
    IProject project = facetedProject.getProject();

    removeGpeClasspathEntries(project);
    subMonitor.worked(10);

    removeGpeNature(project);
    subMonitor.worked(10);

    removeGpeRuntimeAndFacets(facetedProject);
    subMonitor.worked(20);
  }

  @VisibleForTesting
  static void removeGpeClasspathEntries(IProject project) {
    try {
      IJavaProject javaProject = JavaCore.create(project);
      List<IClasspathEntry> newEntries = new ArrayList<>();
      for (IClasspathEntry entry : javaProject.getRawClasspath()) {
        String path = entry.getPath().toString();  // note: '/' is a path separator.
        if (!GPE_CLASSPATH_ENTRIES_PATH.contains(path)) {
          newEntries.add(entry);
        }
      }

      IClasspathEntry[] rawEntries = newEntries.toArray(new IClasspathEntry[0]);
      javaProject.setRawClasspath(rawEntries, new NullProgressMonitor());
      javaProject.save(new NullProgressMonitor(), true);

    } catch (JavaModelException ex) {
      logger.log(Level.WARNING, "Failed to remove GPE classpath entries.", ex);
    }
  }

  @VisibleForTesting
  static void removeGpeNature(IProject project) {
    try {
      NatureUtils.removeNature(project, GPE_GAE_NATURE_ID);
    } catch (CoreException ex) {
      logger.log(Level.WARNING, "Failed to remove GPE nature.", ex);
    }
  }

  @VisibleForTesting
  static void removeGpeRuntimeAndFacets(IFacetedProject facetedProject) {
    // To remove the facets, we will directly modify the WTP facet metadata file (using XSLT):
    // .settings/org.eclipse.wst.common.project.facet.core.xml
    IFile metadataFile = facetedProject.getProject().getFile(FACETS_METADATA_FILE);
    if (!metadataFile.exists()) {
      return;
    }
    
    URL xslt = GpeMigrator.class.getResource(WTP_METADATA_XSLT);
    try {
      Xslt.transformInPlace(metadataFile, xslt);
    } catch (IOException | TransformerException | CoreException ex) {
      logger.log(Level.WARNING, "Failed to modify WTP facet metadata.", ex);
    }
  }

}
