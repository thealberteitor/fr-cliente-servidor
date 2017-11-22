// Ejercicio 2: Modificar servidor y cliente para usar las clases
//							PrintWriter y BufferedReader

//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class YodafyClienteTCP {

	public static void main(String[] args) {

		String frase;

		// Nombre del host donde se ejecuta el servidor:
		String host="localhost";
		// Puerto en el que espera el servidor:
		int port=8989;

		// Socket para la conexión TCP
		Socket socketServicio=null;

		try {

			socketServicio = new Socket (host,port);

			InputStream inputStream = socketServicio.getInputStream();
			OutputStream outputStream = socketServicio.getOutputStream();

			PrintWriter outPrinter = new PrintWriter (outputStream, true);
			BufferedReader inReader = new BufferedReader ( new InputStreamReader (inputStream) );


			outPrinter.println("Al monte del volcán debes ir sin demora");
			outPrinter.flush();


			frase = inReader.readLine();

			// Mostremos la cadena de caracteres recibidos:
			System.out.println("Recibido: ");
			System.out.println(frase);

			socketServicio.close();

		} catch (UnknownHostException e) {
			System.err.println("Error: Nombre de host no encontrado.");
		} catch (IOException e) {
			System.err.println("Error de entrada/salida al abrir el socket.");
		}
	}
}