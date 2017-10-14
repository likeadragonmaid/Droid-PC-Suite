/*****************************************************************************
 * dpcs/UninstallSystemApps.java: System apps uninstaller for Droid PC Suite
 *****************************************************************************
 * Copyright (C) 2017 Karanvir Singh
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *****************************************************************************/

package dpcs;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.apache.commons.io.IOUtils;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.AbstractListModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.Color;

@SuppressWarnings("serial")
public class UninstallSystemApps extends JFrame {

	JLabel SystemAppUninstallDone;
	private JPanel contentPane;
	@SuppressWarnings("rawtypes")
	JList list;
	List<String> lines;
	String[] values;
	String[] moddedvalues;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UninstallSystemApps frame = new UninstallSystemApps();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	public UninstallSystemApps() {
		setResizable(false);
		setTitle("Uninstall System Apps");
		setIconImage(Toolkit.getDefaultToolkit().getImage(UninstallSystemApps.class.getResource("/graphics/Icon.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 482, 430);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel AppStatus = new JLabel("");
		AppStatus.setBounds(12, 366, 456, 17);
		contentPane.add(AppStatus);

		SystemAppUninstallDone = new JLabel("");
		SystemAppUninstallDone.setText("");
		SystemAppUninstallDone.setBounds(151, 312, 186, 56);
		contentPane.add(SystemAppUninstallDone);

		JLabel lblSelect = new JLabel("Select an app to remove");
		lblSelect.setBounds(26, 12, 405, 17);
		contentPane.add(lblSelect);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 41, 428, 259);
		contentPane.add(scrollPane);

		final JButton btnUninstall = new JButton("Uninstall");
		btnUninstall.setToolTipText("Uninstall the selected app");
		btnUninstall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SystemAppUninstallDone.setText("");
				if (list.getSelectedValue() == null) {
					JOptionPane.showMessageDialog(null, "Please select an app first");
				} else {
					try {
						AppStatus.setText("Uninstalling...");
						Process p1 = Runtime.getRuntime()
								.exec("adb shell pm uninstall -k --user 0 " + list.getSelectedValue());
						p1.waitFor();
						Process p2 = Runtime.getRuntime()
								.exec("adb shell pm list packages -s > /sdcard/.systemapps.txt");
						p2.waitFor();
						Process p3 = Runtime.getRuntime().exec("adb pull /sdcard/.systemapps.txt");
						p3.waitFor();
						Process p4 = Runtime.getRuntime().exec("adb shell rm /sdcard/.systemapps.txt");
						p4.waitFor();
						lines = IOUtils.readLines(new FileInputStream(".systemapps.txt"));
						values = new String[lines.size()];
						values = lines.toArray(values);
						moddedvalues = new String[values.length];
						for (int i = 0; i < values.length; i++) {
							moddedvalues[i] = values[i].substring(8);
						}
						list = new JList();
						list.setModel(new AbstractListModel() {
							public int getSize() {
								return moddedvalues.length;
							}

							public Object getElementAt(int index) {
								return moddedvalues[index];
							}
						});
						scrollPane.setViewportView(list);
						File file = new File(".systemapps.txt");
						if (file.exists() && !file.isDirectory()) {
							file.delete();
						}
						AppStatus.setText("App has been uninstalled successfully");
						SystemAppUninstallDone
								.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Smalldone.png")));
						btnUninstall.setSelected(false);
					} catch (IOException | InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnUninstall.setBounds(26, 327, 107, 27);
		contentPane.add(btnUninstall);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setToolTipText("Refresh the apps list");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb shell pm list packages -s > /sdcard/.systemapps.txt");
					p1.waitFor();
					Process p2 = Runtime.getRuntime().exec("adb pull /sdcard/.systemapps.txt");
					p2.waitFor();
					Process p3 = Runtime.getRuntime().exec("adb shell rm /sdcard/.systemapps.txt");
					p3.waitFor();
					lines = IOUtils.readLines(new FileInputStream(".systemapps.txt"));
					values = new String[lines.size()];
					values = lines.toArray(values);
					moddedvalues = new String[values.length];
					for (int i = 0; i < values.length; i++) {
						moddedvalues[i] = values[i].substring(8);
					}
					list = new JList();
					list.setModel(new AbstractListModel() {
						public int getSize() {
							return moddedvalues.length;
						}

						public Object getElementAt(int index) {
							return moddedvalues[index];
						}
					});
					scrollPane.setViewportView(list);
					File file = new File(".systemapps.txt");
					if (file.exists() && !file.isDirectory()) {
						file.delete();
					}
				} catch (IOException | InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnRefresh.setBounds(344, 327, 107, 27);
		contentPane.add(btnRefresh);

		try {
			Process p1 = Runtime.getRuntime().exec("adb shell pm list packages -s > /sdcard/.systemapps.txt");
			p1.waitFor();
			Process p2 = Runtime.getRuntime().exec("adb pull /sdcard/.systemapps.txt");
			p2.waitFor();
			Process p3 = Runtime.getRuntime().exec("adb shell rm /sdcard/.systemapps.txt");
			p3.waitFor();
			lines = IOUtils.readLines(new FileInputStream(".systemapps.txt"));
			values = new String[lines.size()];
			values = lines.toArray(values);
			moddedvalues = new String[values.length];
			for (int i = 0; i < values.length; i++) {
				moddedvalues[i] = values[i].substring(8);
			}
			list = new JList();
			list.setModel(new AbstractListModel() {
				public int getSize() {
					return moddedvalues.length;
				}

				public Object getElementAt(int index) {
					return moddedvalues[index];
				}
			});
			scrollPane.setViewportView(list);
			File file = new File(".systemapps.txt");
			if (file.exists() && !file.isDirectory()) {
				file.delete();
			}
		} catch (IOException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
