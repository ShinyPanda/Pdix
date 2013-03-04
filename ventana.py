import pygtk
#from lib import ejemplo
#from pydix import diccionarios
pygtk.require('2.0')
import gtk
#from lib import *
import lib
#import pydix

Dic=lib
ComboLastConf=0
CheckBoxLastConf=False

def leerconf():
    global ComboLastConf , CheckBoxLastConf
    print "leer configuracion"
    try:
        r=open(Dic.ruta + "/pydix.conf","r")
        ComboLastConf = int(r.readline())
        #print ComboLastConf
        CheckBoxLastConf = (r.readline()[0:4] == "True")
        #print CheckBoxLastConf
    except:
        print "error al leer configuracion"
        
    try:
        r.close()
    except:
        print "error al cerrar archivo de configuracion"
        
class ventana:
    global CheckBoxLastConf, ComboLastConf  
    window = gtk.Window(gtk.WINDOW_TOPLEVEL)
    textfield = gtk.Entry(0)
    textArea = gtk.TextView(buffer=None)
    textBuffer = textArea.get_buffer()
    model = gtk.ListStore(str)
    enter=65293
    down=65364    
    
    def guardarconf(self):        
        try:
            w=open(Dic.ruta + "/pydix.conf","w")
            w.write(str(self.combo.get_active()) + "\n")
            w.write(str(self.checkbox.get_active()))
        except:
            print "error al guardar configuracion"
            
        try:
            w.close()
            print "configuracion guardada"
        except:
            print "error al cerrar archivo"
        
   
    def hello(self, widget, data=None):
        print "Hello World"        

    def delete_event(self, widget, event, data=None):
        print "saliendo..."
        #pydix.workin = False     
        #return False

    def destroy(self, widget, data=None):
        #pydix.workin = False
        gtk.main_quit()

    def visible(self,widget,event,data=None):
        print "la ventana se hiso visible"
    
    def mapeoteclas(self,widget,event,data=None):
        #print "presionada tecla...."
        #print event.keyval
        if event.keyval == gtk.keysyms.Escape:
            self.window.set_visible(False)
        elif event.keyval in (gtk.keysyms.Control_L, gtk.keysyms.c):            
            self.btncopiarevento(event, data)
        elif event.keyval == gtk.keysyms.Alt_L:
            print "cambiar diccionario"
            #self.combo.
            
            
            
    def textfieldEvent(self,widget,event,data=None):
        if event.keyval == self.enter:
            self.window.set_focus(self.textArea)
            #no selecciona
        elif event.keyval == self.down or event.keyval == gtk.keysyms.Up:# down y kp_down la misma mierda
            print "presionada tecla Down\nfocus debe ir a treeview"
            self.window.set_focus(self.treeView)
            # no salta a donde debe
        else:
            Dic.BuscarPalabra(self.textfield.get_text())
        
        
    def btncopiarevento(self,event,data=None):    
        Dic.ultimoCB = self.textBuffer.get_text(self.textBuffer.get_start_iter(),self.textBuffer.get_end_iter(),False)        
        Dic.cb.set_text((Dic.ultimoCB))        
        Dic.cb.store()
        self.window.set_focus(self.textfield)
        
        
    def focusEvento(self,widget,event,data=None):
        self.window.set_focus(self.textfield)
        self.textfield.select_region(0,-1)
        if self.checkbox.get_active():
            Dic.portapapeles()                    
                           
         
    def checkEvento(self,event,data=None):
        if self.checkbox.get_active():
            Dic.portapapeles()
        self.guardarconf()
            
    def combochange(self,event):
        #print(self.combo.get_active())
        Dic.ListarPalabras(self.combo.get_active())
        Dic.establecerRutaDix(self.combo.get_active())
        Dic.BuscarPalabra(self.textfield.get_text())
        self.window.set_focus(self.textfield)
        self.guardarconf()
        self.actualizarLista()
        
    def DibujarLista(self):
        #model = gtk.ListStore(str)
        global model
        for item in Dic.lista:
            self.model.append([item])            
        return self.model
    
    def ListMouseEvento(self,event,data="nada"):        
        #print "cursor cambiado mouse"
        #print self.seleccion.get_selected_rows()[1][0][0]
        lib.definicion(self.seleccion.get_selected_rows()[1][0][0])
        self.llenarTxtField()        
        
    def ListKeyEvento(self,widget,event):        
        if event.keyval == gtk.keysyms.Right:
            self.window.set_focus(self.textArea)
        elif event.keyval == self.enter:
            self.llenarTxtField()
            self.window.set_focus(self.textfield)
            self.textfield.select_region(0,-1)
        lib.definicion(self.seleccion.get_selected_rows()[1][0][0])
        #print "cursor cambiado teclado"
            
    def textareaEvento(self, widget, event):
        if event.keyval == self.enter:
            self.window.set_focus(self.textfield)
        if event.keyval == gtk.keysyms.Left:
            self.window.set_focus(self.treeView)
            
    def llenarTxtField(self):
        self.textfield.set_text(Dic.lista[self.seleccion.get_selected_rows()[1][0][0]])
    
    def actualizarLista(self):
        global model
        self.model.clear()
        #self.DibujarLista()
        for item in Dic.lista:
            self.model.append([item])
        #self.model
    
    def __init__(self):        
        #self.window = gtk.Window(gtk.WINDOW_TOPLEVEL)
        self.icono = gtk.gdk.pixbuf_new_from_file("logo.png")
        self.window.set_icon(self.icono)
        self.window.connect("delete_event", self.delete_event)
        self.window.connect("destroy", self.destroy)
        self.window.set_title("PyDix")
        self.window.set_position(gtk.WIN_POS_CENTER)
        

        #self.window.set_opacity(0.5)   
        
        #self.window.connect("focus_in_event",self.focusEvento)#cambiar este por otro evento. solo cuando la ventana es activada 
        self.window.connect("key_release_event",self.mapeoteclas)
        #self.window.connect("visibility_notify_event",Dic.ejemplo)
        self.window.set_border_width(10) 
        self.window.set_default_size(920,460) 
        self.textfield.set_text("busqueda rapida")
        self.textfield.connect("key_release_event",self.textfieldEvent)
        self.checkbox = gtk.CheckButton("leer portapapeles")        
        self.checkbox.set_active(CheckBoxLastConf)
        self.checkbox.connect("toggled",self.checkEvento)        
        self.btncopiar = gtk.Button("copiar")
        self.btncopiar.connect("clicked",self.btncopiarevento)
        self.combo = gtk.combo_box_new_text()
        
        for d in lib.diccionarios:
            self.combo.append_text(d)            
                       
        self.combo.set_active(ComboLastConf)        
        self.combo.connect("changed",self.combochange)
        
        
        self.scroll2 = gtk.ScrolledWindow()
        self.scroll2.set_policy(gtk.POLICY_AUTOMATIC,gtk.POLICY_AUTOMATIC)
        self.model = self.DibujarLista()
        self.treeView = gtk.TreeView(self.model)
        self.treeView.connect("key_release_event",self.ListKeyEvento)
        self.treeView.connect("button_release_event",self.ListMouseEvento)  #<---------------------------------              
        self.cellRenderer = gtk.CellRendererText()
        self.column = gtk.TreeViewColumn("PyDix", self.cellRenderer, text=0)
        self.treeView.append_column(self.column)
        self.seleccion = self.treeView.get_selection()
        self.scroll2.add(self.treeView)        
        
        
                
        self.scroll = gtk.ScrolledWindow()
        self.scroll.set_policy(gtk.POLICY_AUTOMATIC,gtk.POLICY_AUTOMATIC)
        self.textArea.set_editable(False)
        self.textArea.set_cursor_visible(True)
        self.textArea.set_wrap_mode(gtk.WRAP_WORD)
        self.textArea.connect("key_release_event", self.textareaEvento)
        self.textBuffer.set_text("diccionarios offline\nprogramado con amor por P4ND4")       
        self.scroll.add(self.textArea)        
        

        
        
        
        #ordenar los objetos en la ventana
        hbox1=gtk.HBox(False,0)
        hbox1.pack_start(self.textfield,False,False,0)
        self.textfield.show()
        hbox1.pack_start(self.checkbox,False,False,0)
        self.checkbox.show()
        hbox1.pack_start(self.btncopiar,False,False,0)        
        self.btncopiar.show()
        vbox0=gtk.VBox(False,0)
        vbox0.pack_start(hbox1,False,False,10)
        hbox1.show()
        hbox2=gtk.HBox(False,0)
        vbox3=gtk.VBox(False,0)
        vbox3.pack_start(self.combo,False,False,0)
        self.combo.show()
        vbox3.pack_start(self.scroll2,True,True,10)
        self.scroll2.show()
        hbox2.pack_start(vbox3,False,False,10)
        vbox3.show()
        hbox2.pack_start(self.scroll,True,True,10)
        self.scroll.show()
        self.textArea.show()
        vbox0.pack_start(hbox2,True,True,0)
        hbox2.show()
        self.window.add(vbox0)
        vbox0.show()
        #hacer visible la ventana
        self.window.show_all()
        self.window.show()

    def main(self):
        gtk.gdk.threads_init()     
        gtk.main()        
        
    #comiensa el systray
    def on_right_click(self, icon, event_button, event_time):
        self.make_menu(event_button, event_time)
    def on_left_click(self, widget):
        #print "ocultar o mostrar ventana"
        self.window.set_visible(not self.window.get_visible())
    def make_menu(self, event_button, event_time):
        menu = gtk.Menu()
        #add info
        info = gtk.MenuItem("info")
        info.show()
        menu.append(info)
        info.connect("activate",self.imprimir)
        #add separador
        #menu.append(Separator)
        #menu.append(gtk.Separator)
        #menu.add(gtk.VSeparator())
        salir = gtk.MenuItem("salir")
        salir.show()
        menu.append(salir)
        salir.connect("activate",self.salir)
        menu.popup(None, None, gtk.status_icon_position_menu,event_button, event_time, self.tray)
    def salir(self,widget):
        print "bye"
        #pydix.workin = False
        exit()
    def imprimir(self,widget):
        self.textBuffer.set_text("por panda; en mis ratos libres ='3\nby panda; in my free time XD")
        #self.window.set_visible(False)
        self.window.set_visible(True)
        self.window.get_focus()
        
    def systray(self):
        #self.tray = gtk.StatusIcon()
        self.tray = gtk.status_icon_new_from_file("logo.png")                
        #self.tray.set_from_stock(gtk.STOCK_ABOUT)    
        self.tray.connect('popup-menu', self.on_right_click)
        self.tray.connect("activate",self.on_left_click)
        self.tray.set_tooltip(('PyDix'))
    #termina el systray   
    

#if __name__ == "__main__":y
    #gui = ventana()
    #gui.systray()    
    #gui.main()

def start(mostrar,nada):    
    Dic.buscardiccionarios()
    leerconf()
    Dic.ListarPalabras(ComboLastConf)
    Dic.establecerRutaDix(ComboLastConf)
    gui = ventana()
    gui.systray()
    #gui.window.set_focus()
    gui.window.set_visible(mostrar)    
    gui.main()
#start()