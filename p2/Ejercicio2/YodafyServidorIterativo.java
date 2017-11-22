// Ejercicio 2: Modificar servidor y cliente para usar las clases
//							PrintWriter y BufferedReader

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//
public class YodafyServidorIterativo {

	public static void main(String[] args) {

		// Puerto de escucha
		int port=8989;

		ServerSocket serverSocket;
		Socket socketServicio = null;

		try {
			
			serverSocket = new ServerSocket(port);

			// Mientras ... siempre!
			do {

				socketServicio = serverSocket.accept();

				ProcesadorYodafy procesador=new ProcesadorYodafy(socketServicio);
				procesador.procesa();

			} while (true);

		} catch (IOException e) {
			System.err.println("Error al escuchar en el puerto "+port);
		}

	}

}
