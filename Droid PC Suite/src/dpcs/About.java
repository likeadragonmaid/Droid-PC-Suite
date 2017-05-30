/*****************************************************************************
 * dpcs/About.java: About JFrame for Droid PC Suite
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

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.IOUtils;

import javax.swing.SwingConstants;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class About extends JFrame {

	private JPanel contentPane;
	double AppVersion;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					About frame = new About();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public About() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(About.class.getResource("/graphics/Icon.png")));
		setResizable(false);
		setType(Type.UTILITY);
		setTitle("About");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 540, 400);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		try {
			InputStreamReader reader2 = new InputStreamReader(
					getClass().getResourceAsStream("/others/app-version.txt"));
			String tmp = IOUtils.toString(reader2);
			AppVersion = Double.parseDouble(tmp);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		JButton btnGitHub = new JButton("GitHub");
		btnGitHub.setToolTipText("Access Droid PC Suite github repository");
		btnGitHub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().browse(new URL("https://github.com/kvsjxd/Droid-PC-Suite/").toURI());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnGitHub.setBounds(369, 295, 111, 25);
		contentPane.add(btnGitHub);

		JLabel lblMyFriend4 = new JLabel("Gulati-kun");
		lblMyFriend4.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblMyFriend4.setBounds(25, 266, 242, 24);
		contentPane.add(lblMyFriend4);

		JLabel lblMyFriend3 = new JLabel("Anil-kun");
		lblMyFriend3.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblMyFriend3.setBounds(25, 242, 242, 24);
		contentPane.add(lblMyFriend3);

		JLabel lblMyFriend2 = new JLabel("Suri-kun");
		lblMyFriend2.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblMyFriend2.setBounds(25, 217, 242, 24);
		contentPane.add(lblMyFriend2);

		JLabel lblApplicationVersion = new JLabel("Version: " + AppVersion);
		lblApplicationVersion.setFont(new Font("Dialog", Font.BOLD, 14));
		lblApplicationVersion.setBounds(382, 16, 132, 18);
		contentPane.add(lblApplicationVersion);

		JLabel lblForMyOther = new JLabel("For my other Android stuff visit me on XDA - Developers (@kvsjxd)");
		lblForMyOther.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					Desktop.getDesktop().browse(
							new URL("http://forum.xda-developers.com/member.php?s=82fb1dacfee601c8f79084b30d57d5a2&u=5640594")
									.toURI());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				lblForMyOther.setForeground(Color.BLUE);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblForMyOther.setForeground(Color.BLACK);
			}
		});
		lblForMyOther.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblForMyOther.setBounds(25, 321, 502, 24);
		contentPane.add(lblForMyOther);

		JLabel lblMySensei2 = new JLabel("Karun Sensei");
		lblMySensei2.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblMySensei2.setBounds(25, 120, 242, 24);
		contentPane.add(lblMySensei2);

		JLabel lblMySensei1 = new JLabel("Prashotam Sensei");
		lblMySensei1.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblMySensei1.setBounds(25, 98, 242, 24);
		contentPane.add(lblMySensei1);

		JLabel lblDaretobe = new JLabel("D4r3T0B3");
		lblDaretobe.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblDaretobe.setBounds(25, 194, 242, 24);
		contentPane.add(lblDaretobe);

		JLabel label_9 = new JLabel("");
		label_9.setToolTipText("This variation of android robot is created using Androidify");
		label_9.setIcon(new ImageIcon(About.class.getResource("/graphics/Droidrobot.png")));
		label_9.setBounds(334, 50, 180, 270);
		contentPane.add(label_9);

		JLabel lblDeveloper = new JLabel("Developer");
		lblDeveloper.setFont(new Font("Dialog", Font.BOLD, 16));
		lblDeveloper.setBounds(25, 12, 233, 24);
		contentPane.add(lblDeveloper);

		JLabel lblMrAleksandarDespotovski_shi = new JLabel("Aleksandar Despotovski-shi");
		lblMrAleksandarDespotovski_shi.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblMrAleksandarDespotovski_shi.setBounds(25, 169, 242, 24);
		contentPane.add(lblMrAleksandarDespotovski_shi);

		JLabel lblMyname = new JLabel("Karanvir Singh");
		lblMyname.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(
							new URL("http://forum.xda-developers.com/member.php?s=82fb1dacfee601c8f79084b30d57d5a2&u=5640594")
									.toURI());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				lblMyname.setForeground(Color.BLUE);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblMyname.setForeground(Color.RED);
			}
		});
		lblMyname.setForeground(Color.RED);
		lblMyname.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 16));
		lblMyname.setBounds(25, 36, 242, 24);
		contentPane.add(lblMyname);

		JLabel lblMyFriend1 = new JLabel("My friend - Chetan-kun");
		lblMyFriend1.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblMyFriend1.setBounds(25, 145, 242, 24);
		contentPane.add(lblMyFriend1);

		JLabel label_6 = new JLabel("Special thanks to");
		label_6.setForeground(UIManager.getColor("OptionPane.questionDialog.titlePane.shadow"));
		label_6.setFont(new Font("Dialog", Font.BOLD, 16));
		label_6.setBounds(25, 71, 240, 25);
		contentPane.add(label_6);

		JLabel lblGoogle = new JLabel(
				"Android, android green colored robot are trademarks of Google, Inc. We are not affliated with Google, Inc. in any way.");
		lblGoogle.setHorizontalAlignment(SwingConstants.LEFT);
		lblGoogle.setFont(new Font("Dialog", Font.PLAIN, 8));
		lblGoogle.setBounds(25, 341, 514, 24);
		contentPane.add(lblGoogle);
	}
}
