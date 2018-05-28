import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

public class ChatServer {
	static ArrayList<Socket> ClientSockets;
	static ArrayList<String> LoginNames;

	ChatServer() throws IOException {
		ServerSocket server = new ServerSocket(5217);
		ClientSockets = new ArrayList<Socket>();
		LoginNames = new ArrayList<String>();

		while (true) {
			Socket client = server.accept();
			AcceptClient acceptClient = new AcceptClient(client);
		}
	}

	public static void main(String[] args) throws IOException {
		ChatServer server = new ChatServer();
	}

	class AcceptClient extends Thread {
		Socket ClientSocket;
		DataInputStream din;
		DataOutputStream dout;
		int lo = -1;
		int iterator = 0; 

		AcceptClient(Socket client) throws IOException {
			ClientSocket = client;
			din = new DataInputStream(ClientSocket.getInputStream());
			dout = new DataOutputStream(ClientSocket.getOutputStream());

			String LoginName = din.readUTF();

			LoginNames.add(LoginName);
			ClientSockets.add(ClientSocket);

			start();
		}

		public void run() {
			while (true) {
				try {
					String msgFromClient = din.readUTF();
					System.out.println(msgFromClient);
					StringTokenizer st = new StringTokenizer(msgFromClient);
					String loginName = st.nextToken();
					String MsgType = st.nextToken();
					StringBuffer msgBuffer = new StringBuffer();

					while (st.hasMoreTokens()) {
						msgBuffer.append(" " + st.nextToken());
					}
					final String msg = msgBuffer.toString();

					switch (MsgType) {

					case "LOGIN":
						ClientSockets.forEach(pSocket -> {
							performLogin(pSocket, loginName);
						});
						break;
					case "LOGOUT":
						ClientSockets.forEach(pSocket -> {
							if (loginName.equals(LoginNames.get(iterator)))
								lo = iterator;
							performLogout(pSocket, loginName);
						});

						if (lo >= 0) {
							LoginNames.remove(lo);
							ClientSockets.remove(lo);
						}

						break;
					default:
						ClientSockets.forEach(pSocket -> {
							sendMessage(pSocket, loginName, msg);
						});

					}
					if (MsgType.equals("LOGOUT"))
						break;

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void performLogin(Socket pSocket, String loginName) {
			DataOutputStream pOut;
			try {
				pOut = new DataOutputStream(pSocket.getOutputStream());
				pOut.writeUTF(loginName + " has logged in.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void performLogout(Socket pSocket, String name) {

			DataOutputStream pOut;
			try {
				pOut = new DataOutputStream(pSocket.getOutputStream());
				pOut.writeUTF(name + " has logged out.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void sendMessage(Socket pSocket, String name, String message) {
			DataOutputStream pOut;
			try {
				pOut = new DataOutputStream(pSocket.getOutputStream());
				pOut.writeUTF(name + ": " + message);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
