import java.io.IOException;
import java.net.Inet4Address;

/**
 * Created by ext_gperez on 5/4/16.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Runnable server = new Runnable() {
            @Override
            public void run() {
                try {
                    String host = Inet4Address.getLocalHost().getHostAddress();
                    int port = 8989;
                    new SocketServer(host, port).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable client = new Runnable() {
            private int num = 1;

            @Override
            public void run() {
                try {
                    String host = Inet4Address.getLocalHost().getHostAddress();
                    int port = 8989;
                    SocketClient socketClient = new SocketClient(host, port);
                    socketClient.start();

                    socketClient.write(String.valueOf(num++));
                    Thread.sleep(1000);
                    socketClient.read();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(server, "server").start();

        Thread.sleep(1000);

        new Thread(client, "client-A").start();
        new Thread(client, "client-B").start();
        new Thread(client, "client-C").start();
    }
}
