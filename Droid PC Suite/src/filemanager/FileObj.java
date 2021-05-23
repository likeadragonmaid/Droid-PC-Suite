/*****************************************************************************
 * filemanager/FileObj.java: FileObj class for Droid PC Suite
 *****************************************************************************
 * Copyright (C) 2019 Shou
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *****************************************************************************/

package filemanager;

public class FileObj {
	private String path, name, lastEdit;
	private long size;
	private boolean dir, link;

	public FileObj(String name, String path, String lastEdit, long size, boolean dir, boolean link) {
		this.name = name;
		this.path = path;
		this.lastEdit = lastEdit;
		this.size = size;
		this.dir = dir;
		this.link = link;
	}

	public boolean isLink() {
		return link;
	}

	public boolean isDir() {
		return dir;
	}

	public String getPath() {
		return path;
	}

	public String getName() {
		return name;
	}

	public String getLastEdit() {
		return lastEdit;
	}

	public long getSize() {
		return size;
	}

	public Object[] getDataForTable() {
		return new Object[] { name, size, lastEdit };
	}
}
