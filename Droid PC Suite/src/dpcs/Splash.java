/*****************************************************************************
 * dpcs/Splash.java: Splash screen for Droid PC Suite
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

import java.awt.Color;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Splash extends JFrame {
	public Splash() {
		super("Splash");
		setSize(968, 316);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setUndecorated(true);
		URL img = getClass().getResource("/graphics/Splash.png");
		Toolkit.getDefaultToolkit().getImage(img);
		getContentPane().setLayout(null);
		JLabel imglabel = new JLabel(new ImageIcon(Splash.class.getResource("/graphics/Splash.png")));
		imglabel.setToolTipText("This variation of android robot is created using Androidify");
		imglabel.setBackground(Color.WHITE);
		imglabel.setBounds(0, 0, 968, 316);
		getContentPane().add(imglabel);
	}
}
