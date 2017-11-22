// Ejercicio 4: Modificar servidor y cliente para que
//							usen datagramas UDP

import java.io.IOException;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Random;


//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//
public class YodafyServidorIterativoUDP {

	public static void main(String[] args) {

		// Puerto de escucha
		int port=8989;

		DatagramSocket serverSocket = null;;
		byte[] buferEnvio = new byte[256];
		byte[] buferRecepcion = new byte[256];
		DatagramPacket paquete = null;
		DatagramPacket paquete_modificado = null;
		InetAddress direccion;
		int puerto;

		String mensaje;

		try {

			serverSocket = new DatagramSocket(port);

		} catch (IOException e) {
			System.err.println("Error al escuchar en el puerto "+port);
		}

		do {

			paquete = new DatagramPacket ( buferRecepcion , buferRecepcion.length );

			try {

				serverSocket.receive( paquete );

			} catch (IOException e) {
				System.err.println("Error de entrada/salida al abrir el socket.");
			}

			mensaje = new String ( paquete.getData() );
			direccion = paquete.getAddress();
			puerto = paquete.getPort();

			// Yodificar
			String[] s = mensaje.split(" ");
			String resultado = "";

			Random random = new Random();

			for( int i = 0 ; i < s.length ; i++){
				int j = random.nextInt( s.length );
				int k = random.nextInt( s.length );
				String tmp = s[j];

				s[j] = s[k];
				s[k] = tmp;
			}

			resultado = s[0];

			for( int i = 1 ; i < s.length ; i++){
				resultado += " " + s[i];
			}

			buferEnvio = resultado.getBytes();

			paquete_modificado = new DatagramPacket ( buferEnvio , buferEnvio.length ,
																								direccion , puerto );

			try {

				serverSocket.send( paquete_modificado );

			} catch (IOException e) {
				System.err.println("Error de entrada/salida al abrir el socket.");
			}

		} while ( true );

	}

}
