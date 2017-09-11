/*****************************************************************************
 * dpcs/ADBHelp.java: ADB Help Viewer JFrame for Droid PC Suite
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
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class ADBHelp extends JFrame {
	JTextArea HelpViewer;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ADBHelp frame = new ADBHelp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ADBHelp() {
		setResizable(false);
		setTitle("ADB Help");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ADBHelp.class.getResource("/graphics/Icon.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 755, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 747, 468);
		contentPane.add(scrollPane);

		HelpViewer = new JTextArea();
		HelpViewer.setEditable(false);
		scrollPane.setViewportView(HelpViewer);
		try {
			InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/others/adbhelp.txt"));
			HelpViewer.read(reader, "");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
