import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * Created by ext_gperez on 5/3/16.
 */
public class SocketClient extends SocketAbstractClass {

    protected SocketChannel socketChannel;

    public SocketClient(String host, int port) {
        super(host, port);
    }

    public void start() {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(listenAddress);
            System.out.println("Client... started");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String message) throws IOException {
        write(message, socketChannel);
    }

    public void read() throws IOException {
        read(socketChannel);
    }
}
