import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * Created by ext_gperez on 5/3/16.
 */
public class SocketServer extends SocketAbstractClass {

    private Selector selector;
    private MessagesHandler messagesHandler;

    public SocketServer(String host, int port) {
        super(host, port);
        openSelector();
        messagesHandler = new MessagesHandler();
    }

    private void openSelector() {
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
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
                    processRequest(key);
                } else if (key.isWritable()) {
                    write(key);
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

    public void processRequest(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        String message = read(socketChannel);

        String result = messagesHandler.processMessage(message);

        socketChannel.register(selector, SelectionKey.OP_WRITE, result);
    }

    public void write(SelectionKey key) throws IOException {
        String attach = "";
        SocketChannel socketChannel = (SocketChannel) key.channel();
        Object result = key.attachment();
        if (result != null) {
            attach = (String) key.attachment();
        }
        write(attach, socketChannel);
        key.cancel();
    }
}
