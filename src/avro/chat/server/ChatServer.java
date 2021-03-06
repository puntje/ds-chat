package avro.chat.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.avro.AvroRemoteException;
import org.apache.avro.ipc.SaslSocketServer;
import org.apache.avro.ipc.SaslSocketTransceiver;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.Transceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.avro.ipc.specific.SpecificResponder;

import avro.chat.proto.Chat;
import avro.chat.proto.ChatClientServer;

public class ChatServer implements Chat, Runnable {
	private ChatRoom publicRoom = new ChatRoom();
	private Hashtable<String, Transceiver> clients = new Hashtable<String, Transceiver>();
	private Hashtable<String, ChatClientServer> clientsServer = new Hashtable<String, ChatClientServer>();
	private Hashtable<String, String> pendingRequests = new Hashtable<String, String>();

	/** Proxy methods **/
	/***
	 * Simple method to test if the server is still alive.
	 *
	 * @return null Returns null if it can answer and is thus still alive.
	 *
	 * @throws AvroRemoteException
	 */
	@Override
	public Void isAlive() throws AvroRemoteException {
		return null;
	}

	/***
	 * Registers client's username to its local server proxy so we can
	 * communicate both ways.
	 *
	 * @param username
	 *            The nickname of the client.
	 * @param clientIP
	 *            The IP address of the client.
	 * @param clientServerPort
	 *            The port to which client's local server is bound to.
	 *
	 * @return boolean Whether the client was successfully registered on the
	 *         server.
	 *
	 * @throws AvroRemoteException
	 */
	@Override
	public boolean register(String username, String clientIP, int clientServerPort) throws AvroRemoteException {
		try {
			Transceiver transceiver = new SaslSocketTransceiver(
					new InetSocketAddress(InetAddress.getByName(clientIP), clientServerPort));
			ChatClientServer proxy = (ChatClientServer) SpecificRequestor.getClient(ChatClientServer.class,
					transceiver);

			if (!clients.containsKey(username)) {
				clients.put(username, transceiver);
				clientsServer.put(username, proxy);
				System.out.println("server> Registered client with username: " + username);
				return true;
			} else {
				System.err.println("server> " + username + " is already registered with the server.");
				return false;
			}
		} catch (IOException e1) {
			System.err.println("server> Couldn't connect back to the client on: " + clientIP + ":" + clientServerPort);
			return false;
		}
	}

	/***
	 * Gets all clients which are currently connected to the server.
	 *
	 * @return List The list of user names.
	 * 
	 * @throws AvroRemoteException
	 */
	@Override
	public ArrayList<String> getClientList() throws AvroRemoteException {
		ArrayList<String> clientList = new ArrayList<String>();
		clientList.addAll(clients.keySet());
		return clientList;
	}

	/***
	 * Allows a client to join a specific room.
	 *
	 * @param username
	 *            The nickname of the client.
	 * @param roomName
	 *            The name of the room, either a public chat room or a private
	 *            room.
	 *
	 * @return boolean Whether or not the client has successfully joined the
	 *         room.
	 *
	 * @throws AvroRemoteException
	 */
	@Override
	public String join(String username, String roomName) throws AvroRemoteException {
		String output;
		if (username.equals(roomName)) {
			output = "server> You can just talk to yourself, " + "you don't need our chat for that ;)";
			return output;
		}
		if (roomName.equals("Public")) { // Public chat
			if (publicRoom.join(username)) {
				output = "server> " + username + " has successfully joined the Public chat room.";
				System.out.println(output);
				return output;
			} else {
				output = "server> " + username + " is already in the public room.";
				System.err.println(output);
				return output;
			}
		} else { // Private Chat
			if (clients.containsKey(roomName)) {
				pendingRequests.put(username, roomName);
				clientsServer.get(roomName).incomingMessage(
						"server> " + username + " would like to start a private conversation with you.\n"
								+ "server> You will be disconnected from all your current chats if you accept.\n"
								+ "server> Type \"accept '" + username + "'\" when you want to start.");
				output = "server> A request was sent to " + roomName
						+ ".\nserver> When he accepts your existing chats will be closed.";
				return output;
			} else {
				output = "server> " + roomName + " is not connected to the server right now.\n"
						+ "server> Type 'gcl' to see currently connected clients.";
				System.err.println(output);
				return output;
			}
		}
	}

	/***
	 * Allows a client to leave the public chat room.
	 *
	 * @param userName
	 *            The nickname of the client.
	 * 
	 * @throws AvroRemoteException
	 */
	@Override
	public boolean leave(String userName) throws AvroRemoteException {
		// if the user is in a private room, the disconnection happens outside
		// the server
		if (publicRoom.contains(userName)) {
			publicRoom.leave(userName);
			if (!publicRoom.contains(userName)) {
				System.out.println("server> " + userName + " has left the Public chat room.");
				return true;
			} else {
				System.err.println("server> " + userName + " couldn't leave the Public chat room.");
				return false;
			}
		} else {
			return false;
		}
	}

	/***
	 * Allows a client to exit the server.
	 *
	 * @param userName
	 *            The nickname of the client.
	 * 
	 * @throws AvroRemoteException
	 */
	private Void exit(String userName) throws AvroRemoteException {
		leave(userName);
		clients.remove(userName);
		clientsServer.remove(userName);
		System.out.println("server> " + userName + " has exited the server.");
		return null;
	}

	/***
	 * Allows a client to send a message to the public room.
	 *
	 * @param userName
	 *            The nickname of the client.
	 * @param message
	 *            The message to be delivered.
	 *
	 * @throws AvroRemoteException
	 */
	@Override
	public String sendMessage(String userName, String message) throws AvroRemoteException {
		if (!publicRoom.contains(userName)) {
			String error = "server> You have not joined a chatroom yet.\n"
					+ "server> To join type: \"join 'Public'\" to join the public chatroom.\n"
					+ "server> Or \"join '(username)'\" to start a private conversation with someone.";
			return error;
		} else {
			publicRoom.sendMessage(userName, message);

			// send the message to all other clients
			String output = userName + "> (Public): " + message;
			for (String client : clients.keySet()) {
				if (publicRoom.contains(client)) {
					if (!client.equals(userName)) {
						(clientsServer.get(client)).incomingMessage(output);
					}
				}
			}
			return output;
		}
	}

	/***
	 * Set up the connection between two clients for a private chat.
	 *
	 * @param client1
	 *            The nickname of the first client.
	 * @param client2
	 *            The nickname of the second client.
	 *
	 * @return boolean Whether or not the connection was successfully made.
	 *
	 * @throws AvroRemoteException
	 */
	@Override
	public boolean setupConnection(String client1, String client2) throws AvroRemoteException {
		if (pendingRequests.containsKey(client1)) {
			if ((pendingRequests.get(client1)).equals(client2)) {
				clientsServer.get(client1).incomingMessage("server> " + client2 + " has accepted your connection."
						+ "\nserver> Your existing chats will now be closed and a private connection will be made.");

				if (publicRoom.contains(client1)) {
					leave(client1);
				}
				if (publicRoom.contains(client2)) {
					leave(client2);
				}

				try {
					System.out.println("server> Setting up connections between " + client1 + " and " + client2);
					String client1Address = (clients.get(client1)).getRemoteName();
					String client2Address = (clients.get(client2)).getRemoteName();
					if (((clientsServer.get(client1)).register(client2, client2Address))
							&& ((clientsServer.get(client2)).register(client1, client1Address))) {
						System.out
								.println("server> Connection succesfully made between " + client1 + " and " + client2);
						pendingRequests.remove(client1);
						return true;
					} else {
						System.err.println("server> Something went wrong with setting up connections between " + client1
								+ " and " + client2);
						return false;
					}
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/***
	 * Checks if all connected users are still alive. If not manually exit them
	 * from the server.
	 * 
	 * @throws AvroRemoteException
	 */
	private void checkUsers() throws AvroRemoteException {
		Hashtable<String, Transceiver> clientsCopy = new Hashtable<String, Transceiver>(clients);

		for (String client : clientsCopy.keySet()) {
			try {
				clientsServer.get(client).isAlive();
			} catch (AvroRemoteException e) {
				System.out.println("server> Failed to reconnect to " + client + ", dropping connection.");
				exit(client);
			}
		}
	}

	/***
	 * This thread runs a polling function which checks for the connected users
	 * every 5 seconds.
	 */
	@Override
	public void run() {
		try {
			while (true) {
				checkUsers();
				Thread.sleep(5000); // milliseconds
			}
		} catch (InterruptedException | AvroRemoteException e) {
			e.printStackTrace();
		}
	}

	/***
	 * Main method for the server.
	 * 
	 * Starts the server (on default port of 10010 if no argument was given).
	 * 
	 * @param args
	 *            Only 1 optional argument is accepted for giving the server a
	 *            port to run on.
	 */
	public static void main(String[] args) {
		Server server = null;
		int serverPort = 10010;

		ChatServer cs = new ChatServer();

		if (args.length == 1) {
			serverPort = Integer.parseInt(args[0]);
		} else if (args.length > 1) {
			System.err.println("ERROR: Max. 1 arguments ([server port]) expected.");
		}

		try {
			server = new SaslSocketServer(new SpecificResponder(Chat.class, cs), new InetSocketAddress(serverPort));
			server.start();

			Thread t = new Thread(cs);
			t.start();

			server.join();
			server.close();
		} catch (IOException e) {
			System.err.println("ERROR: Starting server. Double check server-ip and server-port.");
			System.exit(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
