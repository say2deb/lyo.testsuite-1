/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompanies this distribution. 
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *
 *    Steve Speicher - initial API and implementation
 *******************************************************************************/
package org.eclipse.lyo.testsuite.server.oslcv2tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathException;


import org.eclipse.lyo.testsuite.server.util.OSLCConstants;
import org.eclipse.lyo.testsuite.server.util.SetupProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.xml.sax.SAXException;


/**
 * This class provides JUnit tests for the validation of the OSLCv2 creation and
 * updating of change requests. It uses the template files specified in
 * setup.properties as the entity to be POST or PUT, for creation and updating
 * respectively.
 * 
 * After each test, it attempts to perform a DELETE call on the resource that
 * was presumably created, but this DELETE call is not technically required in
 * the OSLC spec, so the created change request may still exist for some service
 * providers.
 */
@RunWith(Parameterized.class)
public class CreationAndUpdateRdfXmlTests extends CreationAndUpdateBaseTests {

	public CreationAndUpdateRdfXmlTests(String url) {
		super(url);
	}

	@Before
	public void setup() throws IOException, ParserConfigurationException,
			SAXException, XPathException {
		super.setup();
	}
	
	@Parameters
	public static Collection<Object[]> getAllDescriptionUrls()
			throws IOException {
		Properties setupProps = SetupProperties.setup(null);
		ArrayList<String> serviceUrls = getServiceProviderURLsUsingRdfXml(setupProps.getProperty("baseUri"),
				onlyOnce);
		String [] types = getCreateTemplateTypes();
		ArrayList<String> capabilityURLsUsingRdfXml = TestsBase
				.getCapabilityURLsUsingRdfXml(OSLCConstants.CREATION_PROP,
						serviceUrls, useDefaultUsageForCreation, types);
		return toCollection(capabilityURLsUsingRdfXml);
	}

	@Test
	public void createValidResourceUsingRdfXmlTemplate() throws IOException {
		createValidResourceUsingTemplate(OSLCConstants.CT_RDF,
				OSLCConstants.CT_RDF, rdfXmlCreateTemplate);
	}

	@Test
	public void createResourceWithInvalidContent() throws IOException {
		createResourceWithInvalidContent(OSLCConstants.CT_RDF,
				OSLCConstants.CT_RDF, "notvalidrdfxmlcontent");
	}

	@Test
	public void createResourceAndUpdateIt() throws IOException {
		createResourceAndUpdateIt(OSLCConstants.CT_RDF, OSLCConstants.CT_RDF,
				rdfXmlCreateTemplate, rdfXmlUpdateTemplate);
	}

	@Test
	public void updateCreatedResourceWithInvalidContent() throws IOException {
		updateCreatedResourceWithInvalidContent(OSLCConstants.CT_RDF,
				OSLCConstants.CT_RDF, rdfXmlCreateTemplate,
				"notvalidrdfxmlcontent");
	}

	@Test
	public void updateCreatedResourceWithBadType() throws IOException {
		updateCreatedResourceWithBadType(OSLCConstants.CT_RDF,
				OSLCConstants.CT_RDF, rdfXmlCreateTemplate,
				rdfXmlUpdateTemplate, "invalid/type");
	}

	@Test
	public void updateCreatedResourceWithFailedPrecondition()
			throws IOException {
		updateCreatedResourceWithFailedPrecondition(OSLCConstants.CT_RDF,
				OSLCConstants.CT_RDF, rdfXmlCreateTemplate,
				rdfXmlUpdateTemplate);
	}
}
