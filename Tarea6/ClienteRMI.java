import java.rmi.Naming;

public class ClienteRMI {
  static int N = 500;
  static int[][] A = new int[N][N];
  static int[][] B = new int[N][N];
  static int[][] C = new int[N][N];

  public static void desplegar(int[][] x) {
    for (int[] i : x) {
      for (int j : i)
        System.out.print("\t" + j + "\t");
      System.out.print("\n");
    }
  }

  static int[][] parte_matriz(int[][] A, int inicio) {
    int[][] M = new int[N / 2][N];
    for (int i = 0; i < N / 2; i++)
      for (int j = 0; j < N; j++)
        M[i][j] = A[i + inicio][j];
    return M;
  }

  static void acomoda_matriz(int[][] C, int[][] A, int renglon, int columna) {
    for (int i = 0; i < N / 2; i++)
      for (int j = 0; j < N / 2; j++)
        C[i + renglon][j + columna] = A[i][j];
  }

  static long checksum(int[][] m) {
    long s = 0;
    for (int i = 0; i < m.length; i++)
      for (int j = 0; j < m[0].length; j++)
        s += m[i][j];
    return s;
  }

  public static void main(String args[]) throws Exception {

    String url_nodo0 = "rmi://52.150.21.160/prueba";
    String url_nodo1 = "rmi://13.90.133.24/prueba";
    InterfaceRMI r0 = (InterfaceRMI) Naming.lookup(url_nodo0);
    InterfaceRMI r1 = (InterfaceRMI) Naming.lookup(url_nodo1);

    for (int i = 0; i < N; i++)
      for (int j = 0; j < N; j++) {
        A[i][j] = 2 * i - j;
        B[i][j] = 2 * i + j;
        C[i][j] = 0;
      }

    // transpose
    for (int i = 0; i < N; i++)
      for (int j = 0; j < i; j++) {
        int x = B[i][j];
        B[i][j] = B[j][i];
        B[j][i] = x;
      }

    if (N == 4) {
      System.out.print("Matriz A\n");
      desplegar(A);
      System.out.print("Matriz B\n");
      desplegar(B);
    }

    // Partir las matrices
    int[][] A1 = parte_matriz(A, 0);
    int[][] A2 = parte_matriz(A, N / 2);
    int[][] B1 = parte_matriz(B, 0);
    int[][] B2 = parte_matriz(B, N / 2);

    // Multiplicación
    int[][] C1 = r0.multiplica_matrices(A1, B1, N);
    int[][] C2 = r0.multiplica_matrices(A1, B2, N);
    int[][] C3 = r1.multiplica_matrices(A2, B1, N);
    int[][] C4 = r1.multiplica_matrices(A2, B2, N);

    // Acomoda matriz C
    acomoda_matriz(C, C1, 0, 0);
    acomoda_matriz(C, C2, 0, N / 2);
    acomoda_matriz(C, C3, N / 2, 0);
    acomoda_matriz(C, C4, N / 2, N / 2);

    if (N == 8) {
      System.out.print("Matriz C\n");
      desplegar(C);
      System.out.println("Checksum =" + checksum(C));
    } else if (N == 500) {
      System.out.println("Checksum =" + checksum(C));
    }
  }
}
