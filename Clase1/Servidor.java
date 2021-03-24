package Clase1;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;

class Servidor {
  // lee del DataInputStream todos los bytes requeridos

  static void read(DataInputStream f, byte[] b, int posicion, int longitud) throws Exception {
    while (longitud > 0) {
      int n = f.read(b, posicion, longitud);
      posicion += n;
      longitud -= n;
    }
  }

  public static void main(String[] args) throws Exception {
    ServerSocket servidor = new ServerSocket(50000);

    Socket conexion = servidor.accept();

    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    DataInputStream entrada = new DataInputStream(conexion.getInputStream());
    double x = 0;
    // recibe un entero de 32 bits
    for (int i = 0; i < 10000; i++) {
      x = entrada.readFloat();
      System.out.println(x);
    }
    salida.close();
    entrada.close();
    conexion.close();
    servidor.close();
  }
}