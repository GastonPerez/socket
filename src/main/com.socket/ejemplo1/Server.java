import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * Created by ext_gperez on 5/3/16.
 */
public class Server extends SocketAbstract {

    public Server(String host, int port) {
        super(host, port);
    }

    public void start() {
        try {
            InetSocketAddress listenAddress = new InetSocketAddress(host, port);
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(listenAddress);
            socketChannel = serverSocketChannel.accept();
            System.out.println("Server... started");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        String host = Inet4Address.getLocalHost().getHostAddress();
        int port = 9999;

        Server server = new Server(host, port);
        server.start();

        server.read();

        String message = "Hola que tal?";
        server.write(message);
        System.out.println("Send message: " + message);
        Thread.sleep(1000);
        server.write("-----break-----");
        Thread.sleep(1000);

        server.read();

        message = "todo bien, trabajando";
        server.write(message);
        System.out.println("Send message: " + message);
        Thread.sleep(1000);
        server.write("-----break-----");
        Thread.sleep(1000);

        server.read();
    }
}
