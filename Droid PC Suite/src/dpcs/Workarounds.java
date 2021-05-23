/*****************************************************************************
 * dpcs/Workarounds.java: Workarounds and additional assistance for Droid PC Suite
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

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class Workarounds extends JFrame {

	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Workarounds frame = new Workarounds();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Workarounds() {
		setResizable(false);
		setType(Type.UTILITY);
		setTitle("Common Workarounds");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Workarounds.class.getResource("/graphics/Icon.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 565, 275);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblcommonworkaround3_2 = new JLabel("and fastboot files with their newer or older versions.");
		lblcommonworkaround3_2.setForeground(Color.BLACK);
		lblcommonworkaround3_2.setBounds(35, 82, 478, 24);
		contentPane.add(lblcommonworkaround3_2);

		JLabel lblcommonworkaround3 = new JLabel("* If some operations do not work on windows, please replace adb");
		lblcommonworkaround3.setForeground(Color.BLACK);
		lblcommonworkaround3.setBounds(25, 65, 506, 24);
		contentPane.add(lblcommonworkaround3);

		JLabel lblcommonworkaround1 = new JLabel(
				"* Use USB 2.0 to connect android device to system instead of USB 3.0.");
		lblcommonworkaround1.setForeground(Color.BLACK);
		lblcommonworkaround1.setBounds(25, 12, 506, 25);
		contentPane.add(lblcommonworkaround1);

		JLabel lblcommonworkaround2 = new JLabel("* You may also need to install your android device usb drivers.");
		lblcommonworkaround2.setForeground(Color.BLACK);
		lblcommonworkaround2.setBounds(25, 32, 506, 38);
		contentPane.add(lblcommonworkaround2);

		JLabel lblcommonworkaround6 = new JLabel("* Click here for help with connectivity of android device on Linux.");
		lblcommonworkaround6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent arg0) {
				lblcommonworkaround6.setForeground(Color.BLACK);
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				lblcommonworkaround6.setForeground(Color.BLUE);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					Desktop.getDesktop()
							.browse(new URL("https://androidonlinux.wordpress.com/2013/05/12/setting-up-adb-on-linux/")
									.toURI());
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		lblcommonworkaround6.setBounds(25, 148, 504, 24);
		contentPane.add(lblcommonworkaround6);

		JLabel lblcommonworkaround5 = new JLabel("* Don't worry if the app says to connect your device while android");
		lblcommonworkaround5.setBounds(25, 106, 486, 19);
		contentPane.add(lblcommonworkaround5);

		JLabel lblcommonworkaround5_2 = new JLabel("is not booted ex. fastboot, bootloader, booting etc.");
		lblcommonworkaround5_2.setBounds(35, 125, 476, 19);
		contentPane.add(lblcommonworkaround5_2);

		JLabel lblcommonworkaround7 = new JLabel("* Unable to connect Marshmallow device?");
		lblcommonworkaround7.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent arg0) {
				lblcommonworkaround7.setForeground(Color.BLACK);
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				lblcommonworkaround7.setForeground(Color.BLUE);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				MarshmallowFix obj = new MarshmallowFix();
				obj.setVisible(true);
				dispose();
			}
		});
		lblcommonworkaround7.setBounds(25, 173, 504, 24);
		contentPane.add(lblcommonworkaround7);

		JLabel lblIfYour = new JLabel("* If your rom have built in root support without using SuperSU or");
		lblIfYour.setBounds(25, 198, 504, 24);
		contentPane.add(lblIfYour);

		JLabel lblIfYour_1 = new JLabel("any similar app then DPCS will depict wrong root status!");
		lblIfYour_1.setBounds(35, 211, 494, 24);
		contentPane.add(lblIfYour_1);
	}
}
