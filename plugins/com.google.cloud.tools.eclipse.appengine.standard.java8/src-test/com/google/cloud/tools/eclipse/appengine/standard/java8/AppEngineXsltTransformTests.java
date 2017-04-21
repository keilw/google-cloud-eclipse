/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.tools.eclipse.appengine.standard.java8;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.cloud.tools.eclipse.util.Xslt;
import com.google.common.io.CharStreams;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.xml.transform.TransformerException;
import org.junit.Test;

/**
 * Test the appengine-web.xml XSLT transforms
 */
public class AppEngineXsltTransformTests {

  @Test
  public void testAddBare() throws IOException, TransformerException {
    apply("/xslt/addJava8Runtime.xsl",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><appengine-web-app xmlns=\"http://appengine.google.com/ns/1.0\"/>",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><appengine-web-app xmlns=\"http://appengine.google.com/ns/1.0\">"
            + "<runtime>java8</runtime></appengine-web-app>");
  }

  @Test
  public void testAddExistingRuntime() throws IOException, TransformerException {
    apply("/xslt/addJava8Runtime.xsl",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><appengine-web-app xmlns=\"http://appengine.google.com/ns/1.0\"><runtime>XXX</runtime></appengine-web-app>",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><appengine-web-app xmlns=\"http://appengine.google.com/ns/1.0\">"
            + "<runtime>java8</runtime></appengine-web-app>");
  }

  @Test
  public void testAddPreservesOrder() throws IOException, TransformerException {
    apply("/xslt/addJava8Runtime.xsl",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><appengine-web-app xmlns=\"http://appengine.google.com/ns/1.0\">"
            + "<threadsafe>true</threadsafe><runtime>XXX</runtime>"
            + "<sessions-enabled>true</sessions-enabled></appengine-web-app>",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><appengine-web-app xmlns=\"http://appengine.google.com/ns/1.0\">"
            + "<threadsafe>true</threadsafe><runtime>java8</runtime>"
            + "<sessions-enabled>true</sessions-enabled></appengine-web-app>");
  }

  @Test
  public void testAddExistingJava8Runtime() throws IOException, TransformerException {
    apply("/xslt/addJava8Runtime.xsl",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><appengine-web-app xmlns=\"http://appengine.google.com/ns/1.0\">"
            + "<runtime>java8</runtime></appengine-web-app>",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><appengine-web-app xmlns=\"http://appengine.google.com/ns/1.0\">"
            + "<runtime>java8</runtime></appengine-web-app>");
  }

  @Test
  public void testdAdBareNoDefaultNS() throws IOException, TransformerException {
    apply("/xslt/addJava8Runtime.xsl",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ae:appengine-web-app xmlns:ae=\"http://appengine.google.com/ns/1.0\"/>",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ae:appengine-web-app xmlns:ae=\"http://appengine.google.com/ns/1.0\">"
            + "<runtime xmlns=\"http://appengine.google.com/ns/1.0\">java8</runtime>"
            + "</ae:appengine-web-app>");
  }

  @Test
  public void testRemoveBare() throws IOException, TransformerException {
    apply("/xslt/removeJava8Runtime.xsl",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><appengine-web-app xmlns=\"http://appengine.google.com/ns/1.0\"/>",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><appengine-web-app xmlns=\"http://appengine.google.com/ns/1.0\"/>");
  }

  @Test
  public void testRemovePreservesNonJava8Runtime() throws IOException, TransformerException {
    apply("/xslt/removeJava8Runtime.xsl",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><appengine-web-app xmlns=\"http://appengine.google.com/ns/1.0\">"
            + "<runtime>XXX</runtime></appengine-web-app>",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><appengine-web-app xmlns=\"http://appengine.google.com/ns/1.0\">"
            + "<runtime>XXX</runtime></appengine-web-app>");
  }

  @Test
  public void testRemovePreservesOrder() throws IOException, TransformerException {
    apply("/xslt/removeJava8Runtime.xsl",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><appengine-web-app xmlns=\"http://appengine.google.com/ns/1.0\">"
            + "<threadsafe>true</threadsafe>" + "<runtime>java8</runtime>"
            + "<sessions-enabled>true</sessions-enabled>" + "</appengine-web-app>",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><appengine-web-app xmlns=\"http://appengine.google.com/ns/1.0\">"
            + "<threadsafe>true</threadsafe>" + "<sessions-enabled>true</sessions-enabled>"
            + "</appengine-web-app>");
  }

  @Test
  public void testRemoveExistingJava8Runtime() throws IOException, TransformerException {
    apply("/xslt/removeJava8Runtime.xsl",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><appengine-web-app xmlns=\"http://appengine.google.com/ns/1.0\">"
            + "<runtime>java8</runtime></appengine-web-app>",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><appengine-web-app xmlns=\"http://appengine.google.com/ns/1.0\"/>");
  }

  private void apply(String templateFile, String inputString, String outputString)
      throws IOException, TransformerException {
    URL xslPath = AppEngineXsltTransformTests.class.getResource(templateFile);
    if (xslPath == null) {
      File xslFile = new File("." + templateFile);
      assertTrue(xslFile.exists());
      xslPath = xslFile.toURI().toURL();
    }
    InputStream documentStream =
        new ByteArrayInputStream(inputString.getBytes(StandardCharsets.UTF_8));
    InputStream transformed = Xslt.applyXslt(documentStream, xslPath.openStream());
    String result =
        CharStreams.toString(new InputStreamReader(transformed, StandardCharsets.UTF_8));
    result = result.trim();
    assertEquals(outputString, result);
  }

}
