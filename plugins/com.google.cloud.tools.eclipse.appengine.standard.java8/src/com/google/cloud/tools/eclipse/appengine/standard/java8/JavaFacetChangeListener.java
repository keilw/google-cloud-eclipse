
package com.google.cloud.tools.eclipse.appengine.standard.java8;

import com.google.cloud.tools.eclipse.appengine.facets.AppEngineStandardFacet;
import com.google.cloud.tools.eclipse.appengine.facets.WebProjectUtil;
import java.util.logging.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.common.project.facet.core.JavaFacet;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.events.IFacetedProjectEvent;
import org.eclipse.wst.common.project.facet.core.events.IFacetedProjectEvent.Type;
import org.eclipse.wst.common.project.facet.core.events.IFacetedProjectListener;
import org.eclipse.wst.common.project.facet.core.events.IProjectFacetActionEvent;

public class JavaFacetChangeListener implements IFacetedProjectListener {
  private static final Logger logger = Logger.getLogger(JavaFacetChangeListener.class.getName());

  @Override
  public void handleEvent(IFacetedProjectEvent event) {
    IFacetedProject project = event.getProject();
    if (event.getType() != Type.POST_VERSION_CHANGE || !AppEngineStandardFacet.hasFacet(project)) {
      return;
    }
    IProjectFacetActionEvent action = (IProjectFacetActionEvent) event;
    if (!JavaFacet.FACET.equals(action.getProjectFacet())) {
      return;
    }
    IFile descriptor = findDescriptor(project);
    if (descriptor == null) {
      logger.warning("Could not find appengine-web.xml for " + project);
      return;
    }
    if (project.hasProjectFacet(JavaFacet.VERSION_1_8)) {
      AppEngineDescriptorTransform.addJava8Runtime(descriptor);
    } else {
      AppEngineDescriptorTransform.removeJava8Runtime(descriptor);
    }
  }

  /**
   * Find the <code>appengine-web.xml</code> file.
   * 
   * @return the file or {@code null} if not found
   */
  private IFile findDescriptor(IFacetedProject project) {
    IFile descriptor =
        WebProjectUtil.findInWebInf(project.getProject(), new Path("appengine-web.xml"));
    return descriptor;
  }

}
