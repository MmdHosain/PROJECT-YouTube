package server.models;

import javax.imageio.IIOException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


// server Class
public class Server {
    private final ServerSocket serverSocket;
    private final ExecutorService pool = Executors.newFixedThreadPool(8);



    public Server(ServerSocket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
    }



    public void  startServer(){

        try {
            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("a new client has been accepted into server");
                ClientHandler clientHandler = new ClientHandler(socket);
                pool.execute(clientHandler);
            }
        }
        catch (IIOException e){
            e.printStackTrace();

        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public void closeServerSocket(){
        try {
            if (serverSocket != null){
                serverSocket.close();
            }
        }
        catch (IIOException e){
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        try {

            ServerSocket serverSocket1 = new ServerSocket(4444);
            Server server = new Server(serverSocket1);
            server.startServer();


        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

}