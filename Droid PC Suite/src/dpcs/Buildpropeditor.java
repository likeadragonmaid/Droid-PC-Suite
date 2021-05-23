/*****************************************************************************
 * dpcs/Buildpropeditor.java: build.prop Editor for Droid PC Suite
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *****************************************************************************/

package dpcs;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;

@SuppressWarnings("serial")
public class Buildpropeditor extends JFrame {
	JTextArea BuildPropEditorWindow;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Buildpropeditor frame = new Buildpropeditor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Buildpropeditor() {
		setResizable(false);
		setTitle("build.prop Editor");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Buildpropeditor.class.getResource("/graphics/Icon.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 520);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 698, 415);
		contentPane.add(scrollPane);

		BuildPropEditorWindow = new JTextArea();
		scrollPane.setViewportView(BuildPropEditorWindow);

		JButton btnSaveonpc = new JButton("Save to PC");
		btnSaveonpc.setToolTipText("Save the printed text on sceen as a file");
		btnSaveonpc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (BuildPropEditorWindow.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Cannot save empty file!");
				} else {
					JFrame parentFrame = new JFrame();
					JFileChooser fileChooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("prop Files", "prop");
					fileChooser.setFileFilter(filter);
					fileChooser.setDialogTitle("Save to PC");
					int userSelection = fileChooser.showSaveDialog(parentFrame);
					if (userSelection == JFileChooser.APPROVE_OPTION) {
						File fileToSave = fileChooser.getSelectedFile();
						FileWriter write = null;
						try {
							write = new FileWriter(fileToSave.getAbsolutePath() + ".prop");
							BuildPropEditorWindow.write(write);
						} catch (Exception e1) {
							e1.printStackTrace();
						} finally {
							if (write != null)
								try {
									write.close();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
						}
					}
				}
			}
		});
		btnSaveonpc.setBounds(478, 427, 200, 47);
		contentPane.add(btnSaveonpc);

		JButton btnPushtosdcard = new JButton("Push to sdcard");
		btnPushtosdcard.setToolTipText("Push the text printed on screen to your device's sdcard as a build.prop file");
		btnPushtosdcard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (BuildPropEditorWindow.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Cannot push empty file!");
				} else {
					FileWriter write = null;
						try {
							write = new FileWriter("build.prop");
							BuildPropEditorWindow.write(write);
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					finally {
						if (write != null)
							try {
								write.close();
							} catch (IOException e1) {
								e1.printStackTrace();
								}
					}
					try {
						Process p1 = Runtime.getRuntime().exec("adb push build.prop /sdcard/");
						p1.waitFor();
						File file = new File("build.prop");
						if (file.exists() && !file.isDirectory()) {
							file.delete();
						}
					} catch (IOException | InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnPushtosdcard.setBounds(21, 427, 200, 47);
		contentPane.add(btnPushtosdcard);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setToolTipText("Refetch build.prop from android device");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb pull /system/build.prop");
					p1.waitFor();
					Reader reader = new FileReader(new File("build.prop"));
					BuildPropEditorWindow.read(reader, "");
					File file = new File("build.prop");
					if (file.exists() && !file.isDirectory()) {
						file.delete();
					}
				} catch (IOException | InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnRefresh.setBounds(250, 427, 200, 47);
		contentPane.add(btnRefresh);

		try {
			Process p1 = Runtime.getRuntime().exec("adb pull /system/build.prop");
			p1.waitFor();
			Reader reader = new FileReader(new File("build.prop"));
			BuildPropEditorWindow.read(reader, "");
			File file = new File("build.prop");
			if (file.exists() && !file.isDirectory()) {
				file.delete();
			}
		} catch (IOException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
