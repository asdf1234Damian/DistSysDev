package Clase1;

import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;

class Cliente {
  // lee del DataInputStream todos los bytes requeridos

  static void read(DataInputStream f, byte[] b, int posicion, int longitud) throws Exception {
    while (longitud > 0) {
      int n = f.read(b, posicion, longitud);
      posicion += n;
      longitud -= n;
    }
  }

  public static void main(String[] args) throws Exception {
    Socket conexion = new Socket("localhost", 50000);

    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    DataInputStream entrada = new DataInputStream(conexion.getInputStream());

    // enva un entero de 32 bits
    float tic = System.currentTimeMillis();
    System.out.println(tic);
    for (float i = 0; i < 10000; i++) {
      salida.writeFloat(i);
      System.out.println(i);
    }
    float toc = System.currentTimeMillis();
    System.out.println(toc);
    System.out.println(tic - toc);
    salida.close();
    entrada.close();
    conexion.close();
  }
}