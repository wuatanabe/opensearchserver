/**   
 * License Agreement for OpenSearchServer
 *
 * Copyright (C) 2010 Emmanuel Keller / Jaeksoft
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

package com.jaeksoft.searchlib.crawler.file.process;

import java.net.URISyntaxException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.jaeksoft.searchlib.SearchLibException;
import com.jaeksoft.searchlib.crawler.file.database.FileTypeEnum;

public abstract class ItemIterator {

	private final Lock lock = new ReentrantLock(true);

	private ItemIterator parent;

	protected ItemIterator(ItemIterator parent) {
		lock.lock();
		try {
			this.parent = parent;
		} finally {
			lock.unlock();
		}
	}

	protected FileInstanceAbstract getFileInstance() {
		lock.lock();
		try {
			return getFileInstanceImpl();
		} finally {
			lock.unlock();
		}
	}

	protected abstract FileInstanceAbstract getFileInstanceImpl();

	protected ItemIterator next() throws URISyntaxException, SearchLibException {
		lock.lock();
		try {
			ItemIterator next = nextImpl();
			if (next != null)
				return next;
			if (parent == null)
				return null;
			return parent.next();
		} finally {
			lock.unlock();
		}
	}

	protected abstract ItemIterator nextImpl() throws URISyntaxException,
			SearchLibException;

	protected static ItemIterator create(ItemIterator parent,
			FileInstanceAbstract fileInstance, boolean withSubDir)
			throws URISyntaxException, SearchLibException {
		FileTypeEnum type = fileInstance.getFileType();
		if (type == FileTypeEnum.directory)
			return new ItemDirectoryIterator(parent, fileInstance, withSubDir);
		if (type == FileTypeEnum.file)
			return new ItemFileIterator(parent, fileInstance);
		return null;
	}
}