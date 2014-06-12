package com.cfranc.tui;
import static org.junit.Assert.*;

import java.net.Socket;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cfranc.irc.server.BroadcastThread;
import com.cfranc.irc.server.ServerToClientThread;
import com.cfranc.irc.server.User;


public class SimpleChatTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testSendMessage() {
		String msg = "test message";
	
		BroadcastThread.sendMessage(new User("Lauren", "pwd"), msg);
	}
	
	@Test
	public void testaddClient() {
		User user = new User("Laurent", "pwd");
	
		boolean condition = BroadcastThread.addClient(user, new ServerToClientThread(user, new Socket()));
		
		assertTrue("Test addClient", condition);
	}

}
