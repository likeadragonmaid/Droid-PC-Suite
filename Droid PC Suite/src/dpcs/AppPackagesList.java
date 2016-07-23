package dpcs;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.IOUtils;

@SuppressWarnings("serial")
public class AppPackagesList extends JFrame {
	private JPanel contentPane;
	@SuppressWarnings("rawtypes")
	JList applist;
	List<String> lines;
	String[] values;
	String[] moddedvalues;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppPackagesList frame = new AppPackagesList();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	public AppPackagesList() {
		setResizable(false);
		setTitle("App Packages List");
		setIconImage(Toolkit.getDefaultToolkit().getImage(AppPackagesList.class.getResource("/graphics/Icon.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 479, 451);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 12, 428, 333);
		contentPane.add(scrollPane);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setToolTipText("Refresh the apps list");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb shell pm list packages > /sdcard/.allapps.txt");
					p1.waitFor();
					Process p2 = Runtime.getRuntime().exec("adb pull /sdcard/.allapps.txt");
					p2.waitFor();
					Process p3 = Runtime.getRuntime().exec("adb shell rm /sdcard/.allapps.txt");
					p3.waitFor();
					lines = IOUtils.readLines(new FileInputStream(".allapps.txt"));
					values = new String[lines.size()];
					values = lines.toArray(values);
					moddedvalues = new String[values.length];
					for (int i = 0; i < values.length; i++) {
						moddedvalues[i] = values[i].substring(8);
					}
					applist = new JList();
					applist.setModel(new AbstractListModel() {
						public int getSize() {
							return moddedvalues.length;
						}

						public Object getElementAt(int index) {
							return moddedvalues[index];
						}
					});
					scrollPane.setViewportView(applist);
					File file = new File(".allapps.txt");
					if (file.exists() && !file.isDirectory()) {
						file.delete();
					}
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});
		btnRefresh.setBounds(125, 357, 220, 47);
		contentPane.add(btnRefresh);

		try {
			Process p1 = Runtime.getRuntime().exec("adb shell pm list packages > /sdcard/.allapps.txt");
			p1.waitFor();
			Process p2 = Runtime.getRuntime().exec("adb pull /sdcard/.allapps.txt");
			p2.waitFor();
			Process p3 = Runtime.getRuntime().exec("adb shell rm /sdcard/.allapps.txt");
			p3.waitFor();
			lines = IOUtils.readLines(new FileInputStream(".allapps.txt"));
			values = new String[lines.size()];
			values = lines.toArray(values);
			moddedvalues = new String[values.length];
			for (int i = 0; i < values.length; i++) {
				moddedvalues[i] = values[i].substring(8);
			}
			applist = new JList();
			applist.setModel(new AbstractListModel() {
				public int getSize() {
					return moddedvalues.length;
				}

				public Object getElementAt(int index) {
					return moddedvalues[index];
				}
			});
			scrollPane.setViewportView(applist);
			File file = new File(".allapps.txt");
			if (file.exists() && !file.isDirectory()) {
				file.delete();
			}
		} catch (Exception e1) {
			System.err.println(e1);
		}
	}
}
