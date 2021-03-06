/**   
 * License Agreement for OpenSearchServer
 *
 * Copyright (C) 2013 Emmanuel Keller / Jaeksoft
 * 
 * http://www.open-search-server.com
 * 
 * This file is part of OpenSearchServer.
 *
 * OpenSearchServer is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 * OpenSearchServer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with OpenSearchServer. 
 *  If not, see <http://www.gnu.org/licenses/>.
 **/

package com.jaeksoft.searchlib.test.rest;

import java.io.IOException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.jaeksoft.searchlib.test.IntegrationTest;
import com.jaeksoft.searchlib.webservice.CommonResult;

public class RestDeleteIndexTest extends CommonRestAPI {

	@Test
	public void testADeleteIndexXML() throws IllegalStateException,
			IOException, XPathExpressionException, SAXException,
			ParserConfigurationException {
		Response response = client()
				.path("/services/rest/index/{index_name}",
						IntegrationTest.INDEX_NAME)
				.accept(MediaType.APPLICATION_JSON).delete();
		checkCommonResult(response, CommonResult.class, 200);
	}
}
