import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Token {
    static DataInputStream entrada;
    static DataOutputStream salida;
    static ServerSocket servidor;
    static boolean primera_vez = true;
    static String ip;
    static int nodo, token, contador = 0;

    static class Worker extends Thread {
        public void run() {
            try {
                servidor = new ServerSocket(50000);
                Socket conexion = servidor.accept();
                entrada = new DataInputStream(conexion.getInputStream());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }

    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Se debe pasar como parametros el numero del nodo y la IP del siguiente nodo");
            System.exit(1);
        }

        nodo = Integer.valueOf(args[0]);
        ip = args[1];

        Worker w = new Worker();
        w.start();
        Socket conexion = null;
        while (true) {
            try {
                conexion = new Socket(ip, 50000);
                break;
            } catch (Exception e) {
                Thread.sleep(500);
            }
        }
        salida = new DataOutputStream(conexion.getOutputStream());
        w.join();

        while (true) {
            if (nodo == 0) {
                if (primera_vez) {
                    primera_vez = false;
                    token = 1;
                } else {
                    token = entrada.readInt();
                }
            } else {
                token = entrada.readInt();
            }
            System.out.println("Nodo: " + nodo + ", Contador: " + contador + ", Token: " + token);
            if (++contador == 1000 && nodo == 0) {
                break;
            }
            salida.writeInt(token);
        }
        servidor.close();
    }
}
