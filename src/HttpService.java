import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpService {
    public static void main(String[] args) {
        System.out.println("hi welcome to use this application...");
        try {
            ServerSocket serverSocket = new ServerSocket(8891);
            while(true){
                Socket client = serverSocket.accept();
                System.out.println("当前连接的客户端："+client.getInetAddress()+":"+client.getPort());
                new Thread(new HttpServerService(client)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
