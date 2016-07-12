package dpcs;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.apache.commons.io.IOUtils;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.AbstractListModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.Color;

@SuppressWarnings("serial")
public class UninstallSystemApps extends JFrame {

	JLabel SystemAppUninstallDone;
	private JPanel contentPane;
	@SuppressWarnings("rawtypes")
	JList list;
	List<String> lines;
	String[] values;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UninstallSystemApps frame = new UninstallSystemApps();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	public UninstallSystemApps() {
		setResizable(false);
		setTitle("Uninstall System Apps");
		setIconImage(Toolkit.getDefaultToolkit().getImage(UninstallSystemApps.class.getResource("/graphics/Icon.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 482, 460);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel AppStatus = new JLabel("");
		AppStatus.setBounds(12, 393, 456, 17);
		contentPane.add(AppStatus);

		SystemAppUninstallDone = new JLabel("");
		SystemAppUninstallDone.setBounds(151, 312, 186, 56);
		contentPane.add(SystemAppUninstallDone);

		JLabel lblSelect = new JLabel("Select an app to remove");
		lblSelect.setBounds(26, 12, 405, 17);
		contentPane.add(lblSelect);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 41, 428, 259);
		contentPane.add(scrollPane);

		final JButton btnUninstall = new JButton("Uninstall");
		btnUninstall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SystemAppUninstallDone.setText("");
				if (list.getSelectedValue() == null) {
					JOptionPane.showMessageDialog(null, "Please select an app first");
				} else {
					try {
						AppStatus.setText("Uninstalling...");
						Process p1 = Runtime.getRuntime().exec("adb remount");
						p1.waitFor();
						String[] commands = new String[3];
						commands[0] = "adb shell su -c rm -r ";
						commands[1] = "/system/app/";
						commands[2] = " " + list.getSelectedValue();
						Process p2 = Runtime.getRuntime().exec(commands, null);
						p2.waitFor();
						Process p3 = Runtime.getRuntime().exec("adb shell ls /system/app/ > /sdcard/.systemapps.txt");
						p3.waitFor();
						Process p4 = Runtime.getRuntime().exec("adb pull /sdcard/.systemapps.txt");
						p4.waitFor();
						Process p5 = Runtime.getRuntime().exec("adb shell rm /sdcard/.systemapps.txt");
						p5.waitFor();
						lines = IOUtils.readLines(new FileInputStream(".systemapps.txt"));
						values = new String[lines.size()];
						values = lines.toArray(values);
						list = new JList();
						list.setModel(new AbstractListModel() {
							public int getSize() {
								return values.length;
							}

							public Object getElementAt(int index) {
								return values[index];
							}
						});
						scrollPane.setViewportView(list);
						File file = new File(".systemapps.txt");
						if (file.exists() && !file.isDirectory()) {
							file.delete();
						}
						AppStatus.setText("App has been uninstalled successfully");
						SystemAppUninstallDone
								.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Smalldone.png")));
						btnUninstall.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});
		btnUninstall.setBounds(26, 327, 107, 27);
		contentPane.add(btnUninstall);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb shell ls /system/app/ > /sdcard/.systemapps.txt");
					p1.waitFor();
					Process p2 = Runtime.getRuntime().exec("adb pull /sdcard/.systemapps.txt");
					p2.waitFor();
					Process p3 = Runtime.getRuntime().exec("adb shell rm /sdcard/.systemapps.txt");
					p3.waitFor();
					lines = IOUtils.readLines(new FileInputStream(".systemapps.txt"));
					values = new String[lines.size()];
					values = lines.toArray(values);
					list = new JList();
					list.setModel(new AbstractListModel() {
						public int getSize() {
							return values.length;
						}

						public Object getElementAt(int index) {
							return values[index];
						}
					});
					scrollPane.setViewportView(list);
					File file = new File(".systemapps.txt");
					if (file.exists() && !file.isDirectory()) {
						file.delete();
					}
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});
		btnRefresh.setBounds(344, 327, 107, 27);
		contentPane.add(btnRefresh);

		try {
			Process p1 = Runtime.getRuntime().exec("adb shell ls /system/app/ > /sdcard/.systemapps.txt");
			p1.waitFor();
			Process p2 = Runtime.getRuntime().exec("adb pull /sdcard/.systemapps.txt");
			p2.waitFor();
			Process p3 = Runtime.getRuntime().exec("adb shell rm /sdcard/.systemapps.txt");
			p3.waitFor();
			lines = IOUtils.readLines(new FileInputStream(".systemapps.txt"));
			values = new String[lines.size()];
			values = lines.toArray(values);
			list = new JList();
			list.setModel(new AbstractListModel() {
				public int getSize() {
					return values.length;
				}

				public Object getElementAt(int index) {
					return values[index];
				}
			});
			scrollPane.setViewportView(list);
			JLabel lblNewLabel = new JLabel("Note: You should also remove app's odex file if it exists");
			lblNewLabel.setBounds(26, 374, 438, 17);
			contentPane.add(lblNewLabel);
			File file = new File(".systemapps.txt");
			if (file.exists() && !file.isDirectory()) {
				file.delete();
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
