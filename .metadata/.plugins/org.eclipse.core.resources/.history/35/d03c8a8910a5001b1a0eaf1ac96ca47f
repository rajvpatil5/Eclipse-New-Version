package demo.websocketIO;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

public class SocketsSmokeTestSE {

	// ws://echo.websocket.org/

	public static void main(String[] args) throws DeploymentException, IOException, URISyntaxException {
		System.out.println("Hello world!!!!!!!!");
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		HelloEndpoint helloEndpoint = new HelloEndpoint();

		Session connectToServer = container.connectToServer(helloEndpoint, new URI("wss://fujitsu-staging.loookit.com/socket.io/?EIO=3&transport=websocket&sid=zkR8vtFAZ5hOGCldAABF"));
		helloEndpoint.sendMessage("client");
	}
}
