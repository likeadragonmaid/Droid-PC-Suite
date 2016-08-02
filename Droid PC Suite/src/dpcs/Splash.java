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
