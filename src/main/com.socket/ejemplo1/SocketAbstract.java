import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by ext_gperez on 5/3/16.
 */
public abstract class SocketAbstract {

    protected String host = null;
    protected Integer port = null;
    protected SocketChannel socketChannel;
    protected int bufferSize = 1024;

    public SocketAbstract(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public abstract void start();

    public void read() throws IOException {
        while (socketChannel.isConnected()) {
            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
            Integer numRead = socketChannel.read(buffer);

            if (numRead > 0) {
                byte[] data = new byte[numRead];
                System.arraycopy(buffer.array(), 0, data, 0, numRead);
                String message = new String(data);
                if (message.equals("-----break-----")) {
                    break;
                }
                System.out.println("Got message: " + message);
            }
        }
    }

    public void write(String message) throws IOException {
        byte [] msg = message.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(msg);
        socketChannel.write(buffer);
        buffer.clear();
    }

    public void close() throws IOException {
        socketChannel.close();
    }
}
