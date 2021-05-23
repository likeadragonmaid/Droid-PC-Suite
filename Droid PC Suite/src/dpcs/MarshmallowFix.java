/**************************************************************************************************
 * dpcs/MarshmallowFix.java: Marshmallow device connectivity Help Viewer JFrame for Droid PC Suite
 **************************************************************************************************
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
 **************************************************************************************************/

package dpcs;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class MarshmallowFix extends JFrame {
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MarshmallowFix frame = new MarshmallowFix();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MarshmallowFix() {
		setResizable(false);
		setTitle("Help for connecting Marshmellow device");
		setIconImage(Toolkit.getDefaultToolkit().getImage(MarshmallowFix.class.getResource("/graphics/Icon.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 479, 451);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnRefresh = new JButton("Download");
		btnRefresh.setToolTipText("Download the file to flash");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(
							new URL("http://forum.xda-developers.com/attachment.php?attachmentid=3761901&d=1464187904")
									.toURI());
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnRefresh.setBounds(125, 357, 220, 47);
		contentPane.add(btnRefresh);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 471, 340);
		contentPane.add(scrollPane);

		JTextArea MarshmallowFixviewer = new JTextArea();
		MarshmallowFixviewer.setEditable(false);
		scrollPane.setViewportView(MarshmallowFixviewer);
		try {
			InputStreamReader reader = new InputStreamReader(
					getClass().getResourceAsStream("/others/marshmallow-fix.txt"));
			MarshmallowFixviewer.read(reader, "");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}
}
