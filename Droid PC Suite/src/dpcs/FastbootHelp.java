/*****************************************************************************
 * dpcs/FastbootHelp.java: Fastboot Help Viewer JFrame for Droid PC Suite
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

package dpcs;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.InputStreamReader;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class FastbootHelp extends JFrame {
	JTextArea HelpViewer;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FastbootHelp frame = new FastbootHelp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public FastbootHelp() {
		setResizable(false);
		setTitle("Fastboot Help");
		setIconImage(Toolkit.getDefaultToolkit().getImage(FastbootHelp.class.getResource("/graphics/Icon.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 530, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 522, 468);
		contentPane.add(scrollPane);

		HelpViewer = new JTextArea();
		HelpViewer.setEditable(false);
		scrollPane.setViewportView(HelpViewer);
		try {
			InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/others/fastboothelp.txt"));
			HelpViewer.read(reader, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
