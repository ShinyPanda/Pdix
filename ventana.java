/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * pagina.java
 *
 * Created on Mar 30, 2012, 5:51:19 PM
 */

package Pdix;
import java.io.*;
import java.util.ArrayList;
//import java.awt.Toolkit;
//import java.awt.datatransfer.Clipboard;
//import java.awt.datatransfer.StringSelection;
//import javax.swing.JOptionPane;
import java.awt.datatransfer.*;
//import java.awt.datatransfer.DataFlavor;
//import java.awt.datatransfer.Transferable;
//import java.awt.AWTException;
import java.awt.*;
//import java.awt.datatransfer.ClipboardOwner;
import java.awt.event.*;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class ventana extends javax.swing.JFrame{
    String [] Diccionarios={"error"};//esto aparecera si no se encuentras mas diccionarios
    //String [] ficheros;
    String ultimoCB="";//lo ultimo k se guardo en el portapapeles. para no leer cada ves
    File ruta = new File(".");//ruda de ejecucion
    File rutadix;//ruta del diccionario k se esta usando actualmente(español, ingles, lo k sea)
    FileReader fr = null;
    BufferedReader br = null;
    String [] lista={"no encontrado", "not found","sin archivo .pdix","no .pdix file"};//si no hay archivos se mostraran estos item
    Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
    //ArrayList<String> diccionarios = new ArrayList<String>();
    //ArrayList<String> list = new ArrayList<String>();
    String mensaje_error="ERROR archivo de diccionario no encontrado\nERROR no dictionary file found\n" +
            "los archivos de diccionario compatibles usan la extensions .pdix\ndictionary files have extensions .pdix\n"+
            "guarde los archivos en esta misma carpeta y reinicie el programa\n"+
            "save file  in this folder and restart program";
    boolean errores=false;//bandera para indicar cuadno no se encuentran los archivos de diccionario
    boolean eventos = false; // "habilitar eventos"



    public ventana() {        
        systray();//talves de poblemas si lo pongo al principio.....
        buscardiccionarios();
        initComponents();
        if(!errores){//sino hay erroes leer la configuracion. si hay, para k molestarse
            leerconf();
            establecerRutaDix();//establecer la ruta del diccionario k se uso la ultima ves
            ListarPalabras(jComboBox1.getSelectedIndex());//mostrar las palabras disponibles en el diccionario
            //jList1.setModel(new javax.swing.AbstractListModel() {String[] strings = lista;public int getSize() { return strings.length; }public Object getElementAt(int i){ return strings[i]; }});
            //actualizar el jlist
        }
        if (errores){jTextArea1.setText(mensaje_error);}
        setIconImage(Toolkit.getDefaultToolkit().getImage("logo.png"));    
        jTextField1.requestFocus();//poner el puntero en el textfield. x comodidad
        jTextField1.selectAll();//sececionar todo para no tener k borrar el mensajito se buskeda rapida
        jTextArea1.setText("diccionarios offline\nprogramado con amor por P4ND4");
        mapeoTeclas();//k escuche las teclas presionadas
        System.out.println("habilitar eventos aki");
        eventos = true ;

    }


public void buscardiccionarios (){
        String [] ficheros;
        String f="";       
        ArrayList<String> diccionarios = new ArrayList<String>();
        ruta = new File(ruta.getAbsolutePath());
        //System.out.println(ruta.getAbsolutePath());
        ficheros = ruta.list();//leer todos los archivos k estan en la misma carpeta
        for (int i = 0; i < ficheros.length; i++){
            f= (String) ficheros[i];
            if (f.substring(f.lastIndexOf(".")+1,f.length()).equals("pdix")){//buscar solo los k estan en fromato .pdix
                diccionarios.add(f.substring(0,f.lastIndexOf(".")));
                System.out.println(f);
            }
        }
        if (diccionarios.size()<1){errores=true;}//si no se encontro ningun diccionario compatible poner la bandera de errores en true
        else{
        Diccionarios = new String[diccionarios.size()];
        Diccionarios = diccionarios.toArray(Diccionarios);}//actualizar la lista de diccionarios disponibles

        
}
public void ListarPalabras(int dicionario){
    ArrayList<String> list = new ArrayList<String>();
    ruta = new File(".");
    //System.out.println(""+ruta.getAbsolutePath());
    ruta= new File(ruta.getAbsolutePath().substring(0, ruta.getAbsolutePath().length()-1) + Diccionarios[dicionario] + ".pdix");
    System.out.println("listar " + ruta);    
    String linea="";
    list = new ArrayList<String>();    
    try{
    fr = new FileReader (ruta);
    br = new BufferedReader(fr);

    while((linea=br.readLine())!=null){
        if (linea.indexOf("{")>=0){
            linea=linea.substring(linea.indexOf("{")+1, linea.indexOf("}"));
            //System.out.println(linea);
            list.add(linea);
    }
    }
        }
    catch(Exception e){
         e.printStackTrace();
         System.out.println(linea);
      }finally{
          try{
            if( null != fr ){
               fr.close();
            }
         }catch (Exception e2){
            e2.printStackTrace();
         }
      }
    
    lista = new String[list.size()];
    lista = list.toArray(lista);
    jList1.setModel(new javax.swing.AbstractListModel() {String[] strings = lista;public int getSize() { return strings.length; }public Object getElementAt(int i){ return strings[i]; }});
   }
public void establecerRutaDix(){
    rutadix= new File(".");
    rutadix= new File(rutadix.getAbsolutePath().substring(0, rutadix.getAbsolutePath().length()-1) + Diccionarios[jComboBox1.getSelectedIndex()] + ".pdix");
    //System.out.println("nueva ruta dix: ---> "+rutadix);
}
public void definicion(int def){    
    String linea="";
    String palabra="{" + lista[def]+ "}";
    String Definicion="";
    int C=0;
    //System.out.println("definicion >"+ palabra +">"+ ruta);

    try{
    fr = new FileReader (rutadix);
    br = new BufferedReader(fr);

    while((linea=br.readLine())!=null){
        if (linea.indexOf(palabra)>=0){
            if(linea.indexOf("}[")>=0 & linea.indexOf("]~@")>=0){Definicion +=linea.substring(linea.indexOf("}[")+2, linea.indexOf("]~@"));break;}
            if(linea.indexOf("}[")>=0 & linea.indexOf("]~@")<0){Definicion +=linea.substring(linea.indexOf("}[")+2, linea.length()) + "\n";}
            while((linea=br.readLine())!=null){
                C++;
                if(linea.indexOf("}[")<0 & linea.indexOf("]~@")<0){Definicion +=linea + "\n";}
                if(linea.indexOf("}[")<0 & linea.indexOf("]~@")>=0){Definicion +=linea.substring(0, linea.indexOf("]~@"));break;}
                if(C>50){break;}
            }
            break;
    }
    }
    if(Definicion.equalsIgnoreCase("")){Definicion="ERROR\n\ndefinicion no encontrada\n\nnot found\n\npor favor abra el archivo .pidx con un editor de texto y revise el formato\n\nplease open the .pdix file whiy an text editor and cheack the format";}
    jTextArea1.setText(Definicion);
    //jScrollPane2. ¿como hacer k muestre el principio del texto?

    }
    catch(Exception e){
         e.printStackTrace();
      }finally{
          try{
            if( null != fr ){
               fr.close();
            }
         }catch (Exception e2){
            e2.printStackTrace();
         }
      }

}
public void BuscarPalabra(String buscar){
    if (buscar.isEmpty()){jList1.clearSelection();}
    else{
        for (int i =0; i<lista.length; i++){
            if (lista[i].toLowerCase().startsWith(buscar.toLowerCase())){
            //if (lista[i].toLowerCase().replaceAll("à","a").replaceAll("è","e").replaceAll("ì","i").replaceAll("ò","o").replaceAll("ù","u").replaceAll("ü","u").replaceAll("á","a").replaceAll("é","e").replaceAll("í","i").replaceAll("ó","o").replaceAll("ú","u").startsWith(buscar.toLowerCase().replaceAll("à","a").replaceAll("è","e").replaceAll("ì","i").replaceAll("ò","o").replaceAll("ù","u").replaceAll("ü","u").replaceAll("á","a").replaceAll("é","e").replaceAll("í","i").replaceAll("ó","o").replaceAll("ú","u")))

                jList1.ensureIndexIsVisible(i);
                jList1.setSelectedIndex(i);
                definicion(i);
                break;

            }
        }
    }

}
public void llenarTxtField(int i){
    //String set = lista[jList1.getSelectedIndex()];
    //jTextField1.setText(set);
    jTextField1.setText(lista[jList1.getSelectedIndex()]);
}
public void portapapeles(){
    String clipboard="";

    try {
        Transferable t = cb.getContents(cb);
        DataFlavor dataFlavorStringJava = new DataFlavor("application/x-java-serialized-object; class=java.lang.String");
        if (t.isDataFlavorSupported(dataFlavorStringJava)) {
            clipboard = (String) t.getTransferData(dataFlavorStringJava);
        }
    }
    catch (Exception e){System.out.println("no se puede leer el portapapeles");}


    if (clipboard.length()>0 & clipboard.length()<150 & clipboard.indexOf("http")<0 & !ultimoCB.equals(clipboard)){
        ultimoCB=clipboard;
        String cbpalabra=recortar(clipboard);
        if(cbpalabra.length()>0){
            jTextField1.setText(cbpalabra);
            BuscarPalabra(cbpalabra);
        }
    }    
}
public String recortar(String recorte){
    int i=0;
    String ignorar="()[]{}' .,*:\n!¡ç¿?@#/¬+^";
    int iniciodepalabra=-1;

    for (i=0; i<recorte.length();i++){
        //System.out.print(""+ i);
        if (iniciodepalabra<0 & ignorar.indexOf("" + recorte.charAt(i))<0){ iniciodepalabra=i; }//acercarce por la derecha hastra encontrar un caracter significativo. donde inicia una palabra
        if(iniciodepalabra>=0 & ignorar.indexOf("" + recorte.charAt(i))>=0){break;}//buscar donde termina la palabra. buscando un caracter no significativo
    }

    if (iniciodepalabra>=0 & i-iniciodepalabra>=0){recorte=recorte.substring(iniciodepalabra,i);}//si hay una palabra, la resta debe ser positiva
    else{recorte="";}  


    return recorte;
}
public void systray() {
        //se declara el objeto tipo icono
        final TrayIcon iconoSystemTray;
                //se verifica que el SystemTray sea soportado
        if (SystemTray.isSupported()) {
            //se obtiene una instancia estática de la clase SystemTray
            SystemTray tray = SystemTray.getSystemTray();
            //esta es la imagen de icono
            Image imagenIcono = Toolkit.getDefaultToolkit().getImage("logo.png");
            //este listener nos permite capturar cualquier tipo de evento
            //que se haga con el mouse sobre el icono
            MouseListener mouseListener = new MouseListener() {
                public void mouseClicked(MouseEvent e) {                    
                   if (isShowing()){setVisible(false);}
                    else{setVisible(true);}

                }
                public void mouseEntered(MouseEvent e) {}
                public void mouseExited(MouseEvent e) {}
                public void mousePressed(MouseEvent e) {}
                public void mouseReleased(MouseEvent e) {}
            };
            //este listener se asociara con un item del menu contextual
            //que aparece al hacer click derecho sobre el icono
            ActionListener Salir = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Saliendo...");
                    guardarconf();
                    System.exit(0);
                }
            };
            ActionListener  imprimir = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("WAAAAAA");
                    setVisible(true);
                    jTextArea1.setText("por panda; en mis ratos libres ='3\nby panda; in my free time XD");

                }
            };
            //menu que aparece al hacer click derecho
            PopupMenu menu = new PopupMenu();
            MenuItem item = new MenuItem("salir");
            item.addActionListener(Salir);
            MenuItem visible = new MenuItem ("info");
            visible.addActionListener(imprimir);
            menu.add(visible);
            menu.addSeparator();
            menu.add(item);

            //iniciamos el objeto TrayIcon
            iconoSystemTray = new TrayIcon(imagenIcono, "Pdix", menu);
            //este tipo de listener captura el doble click sobre el icono
            ActionListener accionMostrarMensaje = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    iconoSystemTray.displayMessage("Pdix",
                            "diccionarios offline",
                            TrayIcon.MessageType.INFO);
                }
            };

            iconoSystemTray.setImageAutoSize(true);
            iconoSystemTray.addActionListener(accionMostrarMensaje);
            iconoSystemTray.addMouseListener(mouseListener);

            //se debe capturar una excepción en caso que falle la adicion de un icono
            try {
                tray.add(iconoSystemTray);
            } catch (AWTException e) {
                System.err.println("No es posible agregar el icono al System Tray");
            }
        }
        else
            System.err.println("System Tray no soportado/ not suported");
    }
public void guardarconf(){
    ruta = new File(".");
    //System.out.println(""+ruta.getAbsolutePath());
    //System.out.println("parece k esto se ejecuta cuadno no debe");
    ruta= new File(ruta.getAbsolutePath().substring(0, ruta.getAbsolutePath().length()-1)+"pdix.conf");
    //System.out.println(""+ruta.getAbsolutePath());
    FileWriter fichero = null;
    PrintWriter pw = null;
    try{
        fichero = new FileWriter(ruta);
        pw = new PrintWriter(fichero);
        pw.println(""+jComboBox1.getSelectedIndex());
        pw.println(""+jCheckBox1.isSelected());
    }
    catch (Exception e) {e.printStackTrace();}
    finally {try {if (null != fichero) fichero.close();} catch (Exception e2) {e2.printStackTrace(); }}
}
public void leerconf(){
    ruta = new File(".");    
    ruta= new File(ruta.getAbsolutePath().substring(0, ruta.getAbsolutePath().length()-1)+"pdix.conf");
    FileReader freader = null;
    BufferedReader breader = null;
    try {
        freader = new FileReader (ruta);
        breader = new BufferedReader(freader);
        jComboBox1.setSelectedIndex(Integer.parseInt(breader.readLine()));
        jCheckBox1.setSelected(Boolean.parseBoolean(breader.readLine()));
         //while((linea=breader.readLine())!=null)System.out.println(linea);
    }
    catch(Exception e){e.printStackTrace();}
    finally{try{if( null != freader ){freader.close();}}catch (Exception e2){e2.printStackTrace();}}
}


public void mapeoTeclas(){

ActionMap mapaAccion = getRootPane().getActionMap();
InputMap map = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
//ActionMap traymapaAccion = iconoSystemTray.getActionMap();
//F1
//KeyStroke key_F1 = KeyStroke.getKeyStroke(KeyEvent.VK_F1,0);
// CTRL + O
//KeyStroke ctrl_O = KeyStroke.getKeyStroke(KeyEvent.VK_O,Event.CTRL_MASK);
// CTRL + C, CTRL + V
KeyStroke ctrl_C = KeyStroke.getKeyStroke(KeyEvent.VK_C,Event.CTRL_MASK, true);
map.put(ctrl_C , "accion_ctrl_C");
mapaAccion.put("accion_ctrl_C",CTRLC());

KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0);
map.put(esc,"escape");
mapaAccion.put("escape",escape());
KeyStroke mod = KeyStroke.getKeyStroke(KeyEvent.VK_WINDOWS,0);
map.put(mod,"cambiardiccionario");
mapaAccion.put("cambiardiccionario",cambiadic());

KeyStroke ctrl_X = KeyStroke.getKeyStroke(KeyEvent.VK_X,Event.CTRL_MASK, true);
map.put(ctrl_X , "accion_ctrl_X");
mapaAccion.put("accion_ctrl_X",forzarPortapapeles());
//Key Actions
//map.put(key_F1, "accion_F1");mapaAccion.put("accion_F1",Accion_F1());

//KeyStroke popup = KeyStroke.getKeyStroke(KeyEvent.VK_P,Event.CTRL_MASK,true);
//map.put(popup,"popup");
//mapaAccion.put("popup",PopUp());
}
public AbstractAction CTRLC(){
return new AbstractAction() { public void actionPerformed(ActionEvent e) {
StringSelection ss = new StringSelection(jTextArea1.getText());
        cb.setContents(ss, ss);
        ultimoCB=jTextArea1.getText();
        jTextField1.requestFocus();
} };
}
public AbstractAction escape(){
    return new AbstractAction() { public void actionPerformed(ActionEvent e) { setVisible(false); } };
}

public AbstractAction cambiadic(){
    return new AbstractAction() { public void actionPerformed(ActionEvent e) {jComboBox1.setPopupVisible(enabled);jComboBox1.requestFocus(); } };
}

public AbstractAction forzarPortapapeles(){
    return new AbstractAction() { public void actionPerformed(ActionEvent e) {
        ultimoCB = "";//esta variable recuerda el untimo contenido del clipboard. si al ejecutarse el evento GainFocus el CB no ha cambiado se ingnora. para forzar la lectura del portapapeles vacia la varible
        portapapeles();} };
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jComboBox1 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pdix");
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowIconified(java.awt.event.WindowEvent evt) {
                formWindowIconified(evt);
            }
        });

        jTextField1.setText("busqueda rapida");
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jScrollPane2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jScrollPane2KeyReleased(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setFont(new java.awt.Font("DejaVu Sans", 0, 18));
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextArea1KeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTextArea1);

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = lista;
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jList1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jList1KeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(Diccionarios));
        jComboBox1.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                jComboBox1PopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton1.setText("copiar");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("leer portapapeles");
        jCheckBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBox1MouseClicked(evt);
            }
        });
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox1, 0, 219, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 191, Short.MAX_VALUE)
                        .addComponent(jCheckBox1)
                        .addGap(211, 211, 211)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jCheckBox1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

                                             
  

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
               //BuscarPalabra(jTextField1.getText());
               /*if (evt.getKeyCode() == KeyEvent.VK_ENTER){jTextField1.select(0,0);jTextArea1.requestFocus();jTextArea1.selectAll();jScrollPane2.requestFocus();}
               else */if (evt.getKeyCode() == KeyEvent.VK_UP | evt.getKeyCode() == KeyEvent.VK_DOWN){jList1.requestFocus();jTextField1.select(0,0);}
               else{BuscarPalabra(jTextField1.getText());}
               //System.out.println(evt.getKeyCode());
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jList1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jList1KeyReleased
       //definicion(jList1.getSelectedIndex());
       if (evt.getKeyCode() == KeyEvent.VK_ENTER){llenarTxtField(jList1.getSelectedIndex());jTextField1.requestFocus();jTextField1.selectAll();}
       //else if (evt.getKeyCode() == KeyEvent.VK_RIGHT){jTextArea1.requestFocus();jTextArea1.selectAll();jScrollPane2.requestFocus();}
       else{definicion(jList1.getSelectedIndex());}

               //System.out.println("waaaaa");
    }//GEN-LAST:event_jList1KeyReleased

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        definicion(jList1.getSelectedIndex());
        llenarTxtField(jList1.getSelectedIndex());
        //jTextField1.requestFocus();
    }//GEN-LAST:event_jList1MouseClicked

    private void jComboBox1PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBox1PopupMenuWillBecomeInvisible
       /*
       ListarPalabras(jComboBox1.getSelectedIndex());
       establecerRutaDix();
       //jList1.setModel(new javax.swing.AbstractListModel() {String[] strings = lista;public int getSize() { return strings.length; }public Object getElementAt(int i) { return strings[i]; }});
       BuscarPalabra(jTextField1.getText());
       jTextField1.requestFocus();
       jTextField1.selectAll();
       guardarconf();//aver si pone mas lento
        */
    }//GEN-LAST:event_jComboBox1PopupMenuWillBecomeInvisible

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        StringSelection ss = new StringSelection(jTextArea1.getText());
        cb.setContents(ss, ss);
        ultimoCB=jTextArea1.getText();
        jTextField1.requestFocus();     
    }//GEN-LAST:event_jButton1MouseClicked

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        //System.out.println("foco");
        jTextField1.requestFocus();
        jTextField1.selectAll();
        if (jCheckBox1.isSelected()){portapapeles();}
    }//GEN-LAST:event_formWindowGainedFocus

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        System.out.println("saliendo...");
        //guardarconf();
        System.exit(0);
    }//GEN-LAST:event_formWindowClosed

    private void formWindowIconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowIconified
        // TODO add your handling code here:
        setVisible(false);
    }//GEN-LAST:event_formWindowIconified

    private void jTextArea1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){jTextArea1.select(0,0);jTextField1.requestFocus();jTextField1.selectAll();}
        if (evt.getKeyCode() == KeyEvent.VK_LEFT){jTextArea1.select(0,0);jList1.requestFocus();}
    }//GEN-LAST:event_jTextArea1KeyReleased

    private void jScrollPane2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jScrollPane2KeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){jTextArea1.select(0,0);jTextField1.requestFocus();jTextField1.selectAll();}
        if (evt.getKeyCode() == KeyEvent.VK_LEFT){jTextArea1.select(0,0);jList1.requestFocus();}
    }//GEN-LAST:event_jScrollPane2KeyReleased

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        if (eventos){
        ListarPalabras(jComboBox1.getSelectedIndex());
        establecerRutaDix();
        //jList1.setModel(new javax.swing.AbstractListModel() {String[] strings = lista;public int getSize() { return strings.length; }public Object getElementAt(int i) { return strings[i]; }});
        BuscarPalabra(jTextField1.getText());
        jTextField1.requestFocus();
        jTextField1.selectAll();
        guardarconf();}//aver si pone mas lento}
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jCheckBox1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBox1MouseClicked
        if (jCheckBox1.isSelected()){portapapeles();}
        if (eventos){guardarconf();}//aver si todo se pone mas lento.}
    }//GEN-LAST:event_jCheckBox1MouseClicked



    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                new ventana().setVisible(true);                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
