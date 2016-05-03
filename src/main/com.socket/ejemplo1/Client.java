import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * Created by ext_gperez on 5/3/16.
 */
public class Client extends SocketAbstract {

    public Client(String host, int port) {
        super(host, port);
    }

    public void start() {
        try {
            InetSocketAddress hostAddress = new InetSocketAddress(host, port);
            socketChannel = SocketChannel.open();
            socketChannel.connect(hostAddress);
            System.out.println("Client... started");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        String host = Inet4Address.getLocalHost().getHostAddress();
        int port = 8989;

        Client client = new Client(host, port);
        client.start();

        client.write("Hola");
    }
}
