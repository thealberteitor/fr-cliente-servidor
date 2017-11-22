// Ejercicio 4: Modificar servidor y cliente para que
//							usen datagramas UDP

//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.UnknownHostException;
import java.net.InetAddress;

public class YodafyClienteUDP {

	public static void main(String[] args) {

		int port = 8989;

		byte []buferEnvio = new byte[256];;
		byte []buferRecepcion = new byte[256];

		DatagramSocket socket = null;
		DatagramPacket paquete = null;
		DatagramPacket paquete_modificado = null;
		InetAddress direccion = null;


		String frase_yodificada;


		try {

			socket = new DatagramSocket();

		} catch (IOException e) {
			System.err.println("Error de entrada/salida al abrir el socket.");
		}

		try{

			direccion = InetAddress.getByName("localhost");

		}	catch (UnknownHostException e) {
			System.err.println("Error: Nombre de host no encontrado.");
		}

		buferEnvio = "Al monte del volcan debes ir sin demora".getBytes();

		try{

			paquete = new DatagramPacket ( buferEnvio , buferEnvio.length , direccion , port );
			socket.send( paquete );

			paquete_modificado = new DatagramPacket ( buferRecepcion , buferRecepcion.length);
			socket.receive( paquete_modificado );

		} catch (IOException e) {
			System.err.println("Error de entrada/salida al abrir el socket.");
		}

		frase_yodificada = new String ( paquete_modificado.getData() );

		System.out.println("Recibido :");
		System.out.println( frase_yodificada );

		socket.close();
	}
}
