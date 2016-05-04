import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by ext_gperez on 5/4/16.
 */
public abstract class SocketAbstractClass {

    protected InetSocketAddress listenAddress;

    public SocketAbstractClass(String host, int port) {
        listenAddress = new InetSocketAddress(host, port);
    }

    protected abstract void start() throws IOException;

    public void write(String message, SocketChannel socketChannel) throws IOException {
        byte [] msg = message.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(msg);
        socketChannel.write(buffer);
        buffer.clear();
    }

    public String read(SocketChannel socketChannel) throws IOException {
        String message = null;
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Integer numRead = socketChannel.read(buffer);

        if (numRead > 0) {
            byte[] data = new byte[numRead];
            System.arraycopy(buffer.array(), 0, data, 0, numRead);
            message = new String(data);
            System.out.println("Got: " + message);
        }
        return message;
    }
}
