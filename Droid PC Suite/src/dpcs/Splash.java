package dpcs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings({ "unused", "serial" })
public class Splash extends JFrame {
	private JLabel imglabel;
	private ImageIcon img;

	public Splash() {
		super("Splash");
		setSize(968, 316);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setUndecorated(true);
		URL img = getClass().getResource("/graphics/Splash.png");
		Image myPicture = Toolkit.getDefaultToolkit().getImage(img);
		getContentPane().setLayout(null);
		JLabel imglabel = new JLabel(new ImageIcon(Splash.class.getResource("/graphics/Splash.png")));
		imglabel.setBackground(Color.WHITE);
		imglabel.setBounds(0, 0, 968, 316);

		getContentPane().add(imglabel);
	}
}
