import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by ext_gperez on 5/3/16.
 */
public class SocketServer {

    private InetSocketAddress listenAddress;
    private Selector selector;

    public SocketServer(String host, int port) {
        listenAddress = new InetSocketAddress(host, port);
    }

    public void start() throws IOException {
        selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        // retrieve server socket and bind to port
        serverSocketChannel.socket().bind(listenAddress);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        listen();
    }

    public void listen() throws IOException {
        while (true) {
            // wait for events
            this.selector.select();

            //work on selected keys
            Iterator keys = this.selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = (SelectionKey) keys.next();

                // this is necessary to prevent the same key from coming up
                // again the next time around.
                keys.remove();

                if (!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable()) {
                    accept(key);
                } else if (key.isReadable()) {
                    read(key);
                } else if (key.isWritable()) { // Personalizar en subclase
                    processMessage(key);
                    key.cancel();
                }
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        Socket socket = socketChannel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        System.out.println("Connected to: " + remoteAddr);

        // register channel with selector for further IO
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    //read from the socket channel
    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int numRead = socketChannel.read(buffer);

        if (numRead > 0) {
            byte[] data = new byte[numRead];
            System.arraycopy(buffer.array(), 0, data, 0, numRead);
            String message = new String(data);
            System.out.println("Got: " + message);

            socketChannel.register(selector, SelectionKey.OP_WRITE, message);
        }
    }

    public void processMessage(SelectionKey key) throws IOException {
        String message = (String) key.attachment();
        String result;
        if (message.equals("1")) {
            result = "Me enviaste un 1 (uno)";
        } else if (message.equals("2")) {
            result = "Me enviaste un 2 (dos)";
        } else {
            result = "No enviaste ni un 1 (uno) ni un 2 (dos)";
        }
        SocketChannel socketChannel = (SocketChannel) key.channel();
        write(result, socketChannel);
    }

    public void write(String message, SocketChannel socketChannel) throws IOException {
        byte [] msg = message.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(msg);
        socketChannel.write(buffer);
        buffer.clear();
    }

    public static void main(String[] args) throws Exception {
        String host = Inet4Address.getLocalHost().getHostAddress();
        int port = 8989;

        SocketServer socketServer = new SocketServer(host, port);
        socketServer.start();
    }
}
