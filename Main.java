package Pdix;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
//import javax.swing.UIManager;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.skin.RavenGraphiteSkin;

public class Main {
static ServerSocket SERVER_SOCKET;
static Socket socket;
static DataInputStream dataInput;
static DataOutputStream dataOutput;
    
    public static void main(String[] args) {        
        System.out.println("Pdix. diccionarios offline\nalpha 0.02");
      try {
        SERVER_SOCKET = new ServerSocket(1334);//si ya se esta ejecutando el puerto estara ocupado
    } catch (IOException x) {
        System.out.println("puerto 1334 ocupado\n¿Otra instancia de la aplicación se está ejecutando?");
        try{
            System.out.println("enviando señal");//como ya se esta ejecutando solo hay k ponerlo al frente
            socket = new Socket("localhost", 1334);//conectarse al puerto
            dataOutput = new DataOutputStream(socket.getOutputStream());//esta tonteria es necesaria en java¬¬
            dataOutput.writeUTF("Pdix.Show!!!");//enviando orden para k le ventana se haga visible
            socket.close();
        }
        catch(Exception e){e.printStackTrace();}
            System.exit(0);//ya enviada la orden se detiene
    }


        SubstanceLookAndFeel.setSkin(new RavenGraphiteSkin());
        //try {UIManager.setLookAndFeel(new SubstanceMagmaLookAndFeel());}catch(Error e){System.out.println("waaaa");}
	JFrame.setDefaultLookAndFeelDecorated(true);
        ventana obj = new ventana();//llamar a la ventana principal
        //obj.setVisible(true);
        try{
            if(args[0].equals("hide")){obj.setVisible(false);}else{obj.setVisible(true);}}// si revice un argumento "hide" la ventano no sera visible. cualquier otro argummento sera ignorado
        catch(Exception e){obj.setVisible(true);}//sin arguemntos, visible

        while (true){//la vetana principal ya esta funcionando y ahora escuchara cuadno se le ordene ir al frente
            try{
        Socket cliente = SERVER_SOCKET.accept();//espera k algo se coneccte
        dataInput = new DataInputStream(cliente.getInputStream());
        String text = dataInput.readUTF();//espera a recivir una orden
        //System.out.println(text);
        //dasdasdasddda
        if (text.equals("Pdix.Show!!!")){obj.setVisible(true);}//se asegura k la orden sea de la misma aplicacion y pone la ventana al frente
              }
            catch(Exception e){e.printStackTrace();}
        }

    }

}
