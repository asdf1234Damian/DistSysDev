
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

class Matriz_D {
    static Object lock = new Object();
    static int N = 1000;
    static int[][] A = new int[N][N];
    static int[][] B = new int[N][N];
    static int[][] C = new int[N][N];

    static class Worker extends Thread {
        Socket conexion;

        Worker(Socket con) {
            this.conexion = con;
        }

        public void run() {
            try {
                DataOutputStream os = new DataOutputStream(this.conexion.getOutputStream());
                DataInputStream is = new DataInputStream(this.conexion.getInputStream());
                double x = is.readDouble();
                synchronized (lock) {
                    // pi += x;
                }
                os.close();
                is.close();
            } catch (Exception e) {
            }

        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Uso:");
            System.err.println("java Pi<nodo>");
            System.exit(0);
        }
        int nodo = Integer.valueOf(args[0]);

        if (nodo == 0) {
            ServerSocket servidor = new ServerSocket(50000);
            Socket conexion = null;
            DataOutputStream os = null;
            while (true) {
                try {
                    conexion = new Socket("localhost", 50000);
                    os = new DataOutputStream(conexion.getOutputStream());
                    break;
                } catch (Exception e) {
                    Thread.sleep(100);
                }
            }

            // inicializar matriz las matrices
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++) {
                    A[i][j] = 2 * i + j;
                    B[i][j] = 2 * i - j;
                    C[i][j] = 0;
                }
            // transpone la matriz B, la matriz WSStraspuesta queda en B
            // se traspone para leer la columna como renglon

            for (int i = 0; i < N; i++)
                for (int j = 0; j < i; j++) {
                    int x = B[i][j];
                    B[i][j] = B[j][i];
                    B[j][i] = x;
                }

            Vector<Worker> w = new Vector<Worker>();
            for (int i = 0; i < 4; i++) {
                Worker newWorker = new Worker(servidor.accept());
                w.add(newWorker);
                w.lastElement().start();
                os.writeInt(N / 2);
            }
            for (int i = 0; i < 4; i++) {
                w.elementAt(i).join();
            }
            System.out.println(pi);
            servidor.close();
        } else {
            // Algoritmos 3
            Socket conexion = null;
            DataOutputStream os = null;
            while (true) {
                try {
                    conexion = new Socket("localhost", 50000);
                    os = new DataOutputStream(conexion.getOutputStream());
                    break;
                } catch (Exception e) {
                    Thread.sleep(100);
                }
            }
            double sum = 0;
            for (int i = 0; i < 10000000; i++) {
                sum += 4.0 / (8 * i + 2 * (nodo - 2) + 3);
            }
            sum = nodo % 2 == 0 ? -sum : sum;
            os.writeDouble(sum);
            os.close();
        }

    }
}