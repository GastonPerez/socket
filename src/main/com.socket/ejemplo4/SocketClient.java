import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by ext_gperez on 5/3/16.
 */
public class SocketClient {

    private InetSocketAddress listenAddress;
    protected SocketChannel socketChannel;

    public SocketClient(String host, int port) {
        listenAddress = new InetSocketAddress(host, port);
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
        byte [] msg = message.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(msg);
        socketChannel.write(buffer);
        buffer.clear();
    }

    public void read() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Integer numRead = socketChannel.read(buffer);

        if (numRead > 0) {
            byte[] data = new byte[numRead];
            System.arraycopy(buffer.array(), 0, data, 0, numRead);
            System.out.println("Got: " + new String(data));
        }
    }

    public static void main(String[] args) throws Exception {
        String host = Inet4Address.getLocalHost().getHostAddress();
        int port = 8989;

        SocketClient socketClient = new SocketClient(host, port);
        socketClient.start();

        socketClient.write("3");

        Thread.sleep(1000);

        socketClient.read();
    }
}
