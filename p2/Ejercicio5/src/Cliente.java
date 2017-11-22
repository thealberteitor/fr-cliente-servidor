import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Cliente{
	public Player yo=new Player(0,0,' ');
	public Player adversario=new Player(19,19,' ');
	public static final int FIL=20;
	public static final int COL=20;
	
	private Socket client;
	private ThreadCliente thread;
	private Semaphore sem1, sem2, mutex;
	
	public Cliente(String nama){ //INICIALIZA LOS SEMAFOROS Y LAS HEBRAS
		sem1 = new Semaphore(1);
        	sem2 = new Semaphore(1);
        	mutex= new Semaphore(1);
        	thread = new ThreadCliente(nama);
        	thread.start();
	
	}
	
	public void changePos(String nueva, int who){ //MODIFICA LA POSICION DE CADA JUGADOR AL RECIBIR LA INFORMACION DEL SERCIDOR
		if(who==1){
			String[] ejem=nueva.split(";");
			try{ 
				sem1.acquire();
					yo.pos_x=Integer.parseInt(ejem[0]);
					yo.pos_y=Integer.parseInt(ejem[1]);
				sem1.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else if(who==2){
			String[] ejem=nueva.split(";");
			try{
				sem2.acquire();
					adversario.pos_x=Integer.parseInt(ejem[0]);
					adversario.pos_y=Integer.parseInt(ejem[1]);
				sem2.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else System.out.println("wtf r u doin");	
	}
	
	public void giveLetraAdv(){ //SI ERES EL JUGADOR X, LE DA AL ADVERSARIO LA LETRA O Y VICEVERSA
		if(yo.letra=='X') adversario.letra='O';
		else adversario.letra='X';
	}
	
	public void print(String nama){ //DIBUJA EL MAPA DE LOS JUGADORES
        	System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); //Un buen clear
        	for(int i=-1; i<FIL+1; i++){
        		System.out.print("\n");
			for(int j=-1; j<COL+1; j++){                        
				if(yo.pos_x==i && yo.pos_y==j){ System.out.print(yo.letra + " "); }
				else if(adversario.pos_x==i && adversario.pos_y==j){ System.out.print(adversario.letra + " "); }
				else {
					if(i==-1 || i==FIL){ System.out.print("| "); }
					else if(j==-1 || j==COL){ System.out.print("- "); }
 					else{ System.out.print("  "); }
				}
			}
		}
		System.out.print("\n" + nama + ": Posicion: " + yo.pos_x + "-" +yo.pos_y+ ".");
	}
	
	public void me_parece_a_mi_que_te_vas_a_mover(String pa_donde){ //MUEVE AL PLAYER QUE CONTROLA EL CLIENTE
		if("w".equals(pa_donde))
			yo.Arriba();
		if("a".equals(pa_donde))
		    	yo.Izquierda();
		if("s".equals(pa_donde))
		    	yo.Abajo();
		if("d".equals(pa_donde))
		    	yo.Derecha();
	}
	
/////////////////////////////////////////////////////////////

	//CLASE THREAD
	
	public class ThreadCliente extends Thread{
		public String neim;
		
		public ThreadCliente(String nama){
			neim=nama;
		}
		
		public void run(){
			String buferEnvio=" ";
			String buferRecepcion=" ";
			int bytesLeidos=0;
		
			String host="192.168.151.107"; //localhost para mi portatil
			int port=2052; //Un cliente envia al 2052, otro al 2053
		
			Socket socketServicio=null;
		
			//Comprobamos entrada al servidor
			try{
				socketServicio=new Socket(host, port);
				InputStream inputStream = socketServicio.getInputStream();
				OutputStream outputStream = socketServicio.getOutputStream();
			
				//Enviamos nombre para conectarnos al servior
				PrintWriter outPrinter= new PrintWriter(outputStream, true);
				BufferedReader inReader= new BufferedReader(new InputStreamReader(inputStream));
			
				buferEnvio=neim;
				outPrinter.println(buferEnvio);
				outPrinter.flush();
			
				buferRecepcion=inReader.readLine();
			
				if(buferRecepcion.equals("FAILED")){
					System.out.println("Nombre para conexión inválido.");
					System.exit(0);
				}else{
					yo.letra=buferRecepcion.charAt(0);
				}
				
				buferRecepcion=inReader.readLine(); 
				changePos(buferRecepcion, 1);
				print(neim);
				System.out.print("        Eres el jugador " + yo.letra);
				giveLetraAdv();
				
				//Ahora empieza el juego
				Scanner in= new Scanner(System.in);
				
				while(!buferRecepcion.equals("FIN")){
					
					
					String mov=in.nextLine(); //AQUÍ ESTÁ EL PROBLEMA POR EL QUE NO PODEMOS SINCRONIZAR BIEN LA POSICIÓN DE LOS DOS CLIENTE. AL REALIZAR LA FUNCIÓN NEXTLINE(), EL PROGRAMA QUEDA PAUSADO HASTA QUE NO INTRODUCIMOS OTRA LETRA, Y CON LOS CONOCIMIENTO ACTUALES SOBRE JAVA, Y LO INVESTIGADO EN DISTINTOS MANUALES, NO HAY OTRA MANERA DE LEER POR TECLADO EN TERMINAL
					
					me_parece_a_mi_que_te_vas_a_mover(mov);
					
					try{
						mutex.acquire();
						buferEnvio = yo.pos_x+";"+yo.pos_y;
						mutex.release();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					outPrinter.println(buferEnvio);
									
					buferRecepcion=inReader.readLine();
					
					if(buferRecepcion.equals("FIN")){
						break;
					}
					
					changePos(buferRecepcion, 2);
					
					print(neim);
					outPrinter.flush();
								
				}
					
				System.out.println("FIN DEL JUEGO");
				
				socketServicio.close();
			}catch (UnknownHostException e){
				System.err.println("Error: Nombre de host no encontrado.");
			}catch (IOException e){
				System.err.println("Error de entrada/salida al abrir el socket.");
			}			
		}
	}
	
	//HASTA LUEGO LUCAS
	
/////////////////////////////////////////////////////////////
	
	public static void main(String[] args) {
		Cliente c=new Cliente(args[0]);	
	}
}
