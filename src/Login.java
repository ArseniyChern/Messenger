import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.*;

public class Login {

	static void startChat(JFrame login, String loginName) {
		try {
			ChatClient client = new ChatClient(loginName);
			login.setVisible(false);
			login.dispose();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) {
		var login = new JFrame("Login");
		var panel = new JPanel();
		var loginName = new JTextField(20);
		var enter = new JButton("Login");

		panel.add(loginName);
		panel.add(enter);

		login.setSize(300, 100);
		login.add(panel);
		login.setVisible(true);
		login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		enter.addActionListener(event -> {

				startChat(login, loginName.getText());
			

		});

		loginName.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					startChat(login, loginName.getText());
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

		});

	}
}
