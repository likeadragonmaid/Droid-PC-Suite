/*****************************************************************************
 * filemanager/Explorer.java: Explorer class for Droid PC Suite
 *****************************************************************************
 * Copyright (C) 2016 Karanvir Singh
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
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import filemanager.DataReceiver;
import filemanager.Logger;
import java.awt.Color;
import java.awt.Toolkit;

public class Explorer extends Frame {

	private static final long serialVersionUID = 5908837654492782371L;
	private List deviceList;
	private static DataReceiver receiver;
	private final FilePanel filePanel;
	private Logger logger;
	private TextField connectField, destination, source;

	@SuppressWarnings("static-access")
	public Explorer() {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Explorer.class.getResource("/graphics/Icon.png")));
		setBackground(Color.WHITE);
		setAlwaysOnTop(true);
		List logList = new List();
		logger = new Logger(logList);
		receiver = new DataReceiver();
		filePanel = new FilePanel(receiver);
		deviceList = new List();
		deviceList.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				String selection = receiver.getDevices(false).get(Integer.parseInt(arg0.getItem().toString()));
				if (receiver.setSelectedDevice(selection)) {
					filePanel.updateADB(receiver.getDirContent("/"), "/");
				}
			}
		});

		Button refreshButton = new Button("Refresh");
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateDevices(receiver.getDevices(true));
			}
		});

		connectField = new TextField();
		Button connectButton = new Button("Connect");
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				receiver.connectDevice(connectField.getText());
				updateDevices(receiver.getDevices(false));
			}
		});

		destination = new TextField();
		destination.setText(receiver.getSaveLocation());

		Button chooseDestination = new Button("Choose Location");
		chooseDestination.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (fc.showOpenDialog(Explorer.this) == JFileChooser.APPROVE_OPTION) {
					File loc = fc.getSelectedFile();
					receiver.setSaveLocation(loc);
					destination.setText(receiver.getSaveLocation());
					Logger.writeToLog(" - set as save destination");
				}

			}
		});

		Button pull = new Button("Pull");
		pull.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selection = (String) filePanel.getFileTableModel().getValueAt(
						filePanel.getTable().convertRowIndexToModel(filePanel.getTable().getSelectedRow()), 0);
				try {
					receiver.pullFile(filePanel.getDirLabel().getText() + selection);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				try {
					new ProcessBuilder("explorer", receiver.getSaveLocation() + selection).start();
				} catch (Exception ex) {
					ex.printStackTrace();
					Logger.writeToLog("failed to open file or directory");
				}
			}
		});

		source = new TextField();
		source.setSize(500, source.getHeight());

		Button chooseSource = new Button("Choose File");
		chooseSource.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(fc.FILES_AND_DIRECTORIES);

				if (fc.showOpenDialog(Explorer.this) == JFileChooser.APPROVE_OPTION) {
					File selection = fc.getSelectedFile();
					source.setText(selection.getAbsolutePath());
				}
			}
		});

		Button pushButton = new Button("Push Files");
		pushButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (source.getText() != null && source.getText().length() > 0) {
					try {
						receiver.pushFile(source.getText(), filePanel.getDirLabel().getText());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					filePanel.updateADB(receiver.getDirContent(filePanel.getDirLabel().getText()),
							filePanel.getDirLabel().getText());
				} else {
					Logger.writeToLog("Please select a valid pull source");
				}
			}
		});

		Button deleteButton = new Button("Delete Selected Files");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					receiver.deleteFile(filePanel.getDirLabel().getText() + filePanel.getFileTableModel().getValueAt(
							filePanel.getTable().convertRowIndexToModel(filePanel.getTable().getSelectedRow()), 0));
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				filePanel.updateADB(receiver.getDirContent(filePanel.getDirLabel().getText()),
						filePanel.getDirLabel().getText());
			}
		});

		Panel refreshLine = new Panel();
		refreshLine.add(refreshButton);

		Panel subConnectPanel = new Panel(new GridLayout(1, 3));
		subConnectPanel.add(connectField);
		subConnectPanel.add(connectButton);

		Panel connectPanel = new Panel(new BorderLayout());
		connectPanel.add(subConnectPanel, BorderLayout.NORTH);
		connectPanel.add(refreshLine, BorderLayout.EAST);
		connectPanel.add(new Label("Pull Destination"), BorderLayout.SOUTH);

		Panel subSourcePanel = new Panel(new GridLayout(1, 2));
		subSourcePanel.add(source);
		subSourcePanel.add(chooseSource);

		Panel pushPanel = new Panel();
		pushPanel.add(pushButton);

		Panel anotherPanel = new Panel(new GridLayout(1, 3));
		anotherPanel.add(new Label());
		anotherPanel.add(new Label());
		anotherPanel.add(deleteButton);

		Panel sourcePanel = new Panel(new BorderLayout());
		sourcePanel.add(pushPanel, BorderLayout.EAST);
		sourcePanel.add(anotherPanel, BorderLayout.SOUTH);
		sourcePanel.add(subSourcePanel, BorderLayout.NORTH);

		Panel subDestinationPanel = new Panel(new GridLayout(1, 2));
		subDestinationPanel.add(destination);
		subDestinationPanel.add(chooseDestination);

		Panel pullPanel = new Panel();
		pullPanel.add(pull);

		Panel destinationPanel = new Panel(new BorderLayout());
		destinationPanel.add(subDestinationPanel, BorderLayout.NORTH);
		destinationPanel.add(pullPanel, BorderLayout.EAST);
		destinationPanel.add(new Label("Push Source:"), BorderLayout.SOUTH);

		Panel devicePanel = new Panel(new GridLayout(5, 1));
		devicePanel.add(deviceList);
		devicePanel.add(connectPanel);
		devicePanel.add(destinationPanel);
		devicePanel.add(sourcePanel);
		devicePanel.add(logList);

		this.add(devicePanel);
		this.add(filePanel);
		this.setLayout(new GridLayout(1, 3));
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				close();
			}
		});
		this.setSize(800, 500);
		this.setTitle("File Manager");
		this.setVisible(true);
		updateDevices(receiver.getDevices(true));
	}

	public void updateDevices(ArrayList<String> in) {
		deviceList.removeAll();
		if (in.size() == 0) {
			deviceList.setEnabled(false);
		} else {
			deviceList.setEnabled(true);
			for (int i = 0; i < in.size(); i++) {
				deviceList.add(in.get(i));
			}
		}
	}

	public static DataReceiver getReceiver() {
		return receiver;
	}

	private void close() {
		logger.close();
		File file = new File(".explorer.properties");
		if (file.exists() && !file.isDirectory()) {
			file.delete();
		}
		File file2 = new File(".log.txt");
		if (file2.exists() && !file2.isDirectory()) {
			file2.delete();
		}
		File file3 = new File(".lastLog.txt");
		if (file3.exists() && !file3.isDirectory()) {
			file3.delete();
		}
		dispose();
	}
}