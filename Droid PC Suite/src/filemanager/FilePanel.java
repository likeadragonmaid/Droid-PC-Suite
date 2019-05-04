/*****************************************************************************
 * filemanager/FilePanel.java: FilePanel class for Droid PC Suite
 *****************************************************************************
 * Copyright (C) 2019 Shouko Komi
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

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import filemanager.DataReceiver;
import filemanager.FileObj;
import filemanager.Logger;
import java.awt.Color;

public class FilePanel extends Panel {
	private static final long serialVersionUID = -4692855035526278691L;
	private Label dirLabel;
	private JTable adbFileTable;
	private DataReceiver receiver;
	private MyTableModel model;

	public FilePanel(final DataReceiver receiver) {
		setBackground(Color.WHITE);
		this.receiver = receiver;
		Button backButton = new Button("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dir = dirLabel.getText();
				String[] split = dir.split("/");
				if (split.length > 1) {
					String value = "";
					for (int i = 0; i < split.length - 1; i++) {
						value += split[i] + "/";
					}
					updateADB(receiver.getDirContent(value), value);
				} else {
					updateADB(receiver.getDirContent("/"), "/");
				}
			}
		});

		Panel backButtonPanel = new Panel(new GridLayout(1, 2));
		backButtonPanel.add(backButton);
		backButtonPanel.add(new Panel());

		dirLabel = new Label();
		Panel dirPanel = new Panel(new GridLayout(1, 2));
		dirPanel.add(backButtonPanel);
		dirPanel.add(dirLabel);

		model = new MyTableModel();
		model.addColumn("Filename");
		model.addColumn("Size");
		model.addColumn("Last Modified");

		adbFileTable = new JTable(model);
		adbFileTable.setColumnSelectionAllowed(false);
		adbFileTable.setAutoCreateRowSorter(true);

		adbFileTable.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
				boolean left = (e.getButton() == MouseEvent.BUTTON1);
				boolean doubleClick = e.getClickCount() > 1;

				if (left && doubleClick) {
					adbFileTable.repaint();
					String value = (String) model
							.getValueAt(adbFileTable.convertRowIndexToModel(adbFileTable.getSelectedRow()), 0);

					if (value.contains("->")) {
						String[] split = value.split(" ");
						value = split[2];
					} else {
						value = dirLabel.getText() + value + "/";
					}
					updateADB(receiver.getDirContent(value), value);
				}
			}
		});

		JScrollPane fileTableScrollPane = new JScrollPane(adbFileTable);
		this.setLayout(new BorderLayout());
		this.add(dirPanel, BorderLayout.NORTH);
		this.add(fileTableScrollPane, BorderLayout.CENTER);
	}

	public void updateADB(ArrayList<FileObj> in, String dir) {
		if (in != null) {
			dirLabel.setText(dir);

			while (model.getRowCount() > 0) {
				model.removeRow(0);
			}
			for (int i = 0; i < in.size(); i++) {
				model.addRow(in.get(i).getDataForTable());
			}
		} else {
			openFile(dir);
		}
	}

	private void openFile(String path) {
		Logger.writeToLog("pulling file...");
		File file = null;
		try {
			file = receiver.pullFile(path);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			new ProcessBuilder("explorer", file.getAbsolutePath()).start();
		} catch (IOException e) {
			Logger.writeToLog("failed to open file");
			e.printStackTrace();
		}
	}

	public MyTableModel getFileTableModel() {
		return model;
	}

	public JTable getTable() {
		return adbFileTable;
	}

	public Label getDirLabel() {
		return dirLabel;
	}

	public class MyTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1332744571780301856L;

		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
	}
}
