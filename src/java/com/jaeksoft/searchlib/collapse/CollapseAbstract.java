/**   
 * License Agreement for OpenSearchServer
 *
 * Copyright (C) 2008-2012 Emmanuel Keller / Jaeksoft
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

package com.jaeksoft.searchlib.collapse;

import java.io.IOException;

import org.apache.lucene.search.FieldCache.StringIndex;

import com.jaeksoft.searchlib.function.expression.SyntaxError;
import com.jaeksoft.searchlib.index.DocSetHits;
import com.jaeksoft.searchlib.index.ReaderLocal;
import com.jaeksoft.searchlib.query.ParseException;
import com.jaeksoft.searchlib.request.SearchRequest;
import com.jaeksoft.searchlib.result.ResultScoreDoc;
import com.jaeksoft.searchlib.result.ResultScoreDocCollapse;

public abstract class CollapseAbstract {

	private int collapsedDocCount;
	private transient int collapseMax;
	private transient String collapseField;
	private transient CollapseMode collapseMode;
	protected transient SearchRequest searchRequest;
	private transient ResultScoreDocCollapse[] collapsedDoc;

	protected CollapseAbstract(SearchRequest searchRequest) {
		this.searchRequest = searchRequest;
		this.collapseField = searchRequest.getCollapseField();
		this.collapseMax = searchRequest.getCollapseMax();
		this.collapseMode = searchRequest.getCollapseMode();
		this.collapsedDocCount = 0;
		this.collapsedDoc = ResultScoreDocCollapse.EMPTY_ARRAY;
	}

	protected abstract void collapse(ResultScoreDoc[] fetchedDocs,
			int fetchLength, StringIndex collapseStringIndex);

	public void run(ResultScoreDoc[] fetchedDocs, int fetchLength,
			StringIndex collapseStringIndex) throws IOException {

		collapsedDoc = null;

		if (fetchedDocs == null)
			return;

		if (fetchLength > fetchedDocs.length)
			fetchLength = fetchedDocs.length;

		collapse(fetchedDocs, fetchLength, collapseStringIndex);
	}

	public int getDocCount() {
		return this.collapsedDocCount;
	}

	/**
	 * @param collapsedDocCount
	 *            the collapsedDocCount to set
	 */
	protected void setCollapsedDocCount(int collapsedDocCount) {
		this.collapsedDocCount = collapsedDocCount;
	}

	/**
	 * @param collapsedDoc
	 *            the collapsedDoc to set
	 */
	protected void setCollapsedDoc(ResultScoreDocCollapse[] collapsedDoc) {
		this.collapsedDoc = collapsedDoc;
	}

	protected String getCollapseField() {
		return collapseField;
	}

	public ResultScoreDoc[] getCollapsedDoc() {
		return collapsedDoc;
	}

	public CollapseMode getCollapseMode() {
		return collapseMode;
	}

	protected int getCollapsedDocsLength() {
		if (collapsedDoc == null)
			return 0;
		return collapsedDoc.length;
	}

	/**
	 * @return the collapseMax
	 */
	public int getCollapseMax() {
		return collapseMax;
	}

	public static CollapseAbstract newInstance(SearchRequest searchRequest) {
		CollapseMode mode = searchRequest.getCollapseMode();
		if (mode == CollapseMode.COLLAPSE_FULL)
			return new CollapseFull(searchRequest);
		else if (mode == CollapseMode.COLLAPSE_OPTIMIZED)
			return new CollapseOptimized(searchRequest);
		else if (mode == CollapseMode.COLLAPSE_CLUSTER)
			return new CollapseCluster(searchRequest);
		return null;
	}

	public abstract ResultScoreDoc[] collapse(ReaderLocal reader,
			ResultScoreDoc[] docs, DocSetHits docSetHits) throws IOException,
			ParseException, SyntaxError;

}