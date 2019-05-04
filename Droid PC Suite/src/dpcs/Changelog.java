/*****************************************************************************
 * dpcs/Changelog.java: Changelog Viewer JFrame for Droid PC Suite
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
public class Changelog extends JFrame {
	JTextArea ChangelogViewer;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Changelog frame = new Changelog();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Changelog() {
		setResizable(false);
		setTitle("Changelog");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Changelog.class.getResource("/graphics/Icon.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 488, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 480, 468);
		contentPane.add(scrollPane);

		ChangelogViewer = new JTextArea();
		ChangelogViewer.setEditable(false);
		scrollPane.setViewportView(ChangelogViewer);
		try {
			InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/others/changelog.txt"));
			ChangelogViewer.read(reader, "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
