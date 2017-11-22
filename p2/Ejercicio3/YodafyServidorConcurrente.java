// Ejercicio 3: Modificar servidor para que funcione
//							como servidor TCP concurrente

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//
public class YodafyServidorConcurrente {

	public static void main(String[] args) {

		// Puerto de escucha
		int port=8989;


		try {

			ServerSocket serverSocket = new ServerSocket(port);

			// Mientras ... siempre!
			do {

				Socket socketServicio = serverSocket.accept();

				ProcesadorYodafy procesador=new ProcesadorYodafy(socketServicio);
				procesador.start();

			} while (true);

		} catch (IOException e) {
			System.err.println("Error al escuchar en el puerto "+port);
		}

	}

}
