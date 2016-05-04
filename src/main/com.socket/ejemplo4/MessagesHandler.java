import java.io.IOException;

/**
 * Created by ext_gperez on 5/4/16.
 */
public class MessagesHandler {

    public String processMessage(String message) throws IOException {
        String result;
        if (message.equals("1")) {
            result = "Me enviaste un 1 (uno)";
        } else if (message.equals("2")) {
            result = "Me enviaste un 2 (dos)";
        } else {
            result = "No enviaste ni un 1 (uno) ni un 2 (dos)";
        }
        return result;
    }
}
