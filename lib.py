#import gtk
import os#, sys
#from ventana import ventana#este estaba encendido
import ventana
#from ventana import textfield

#cb = gtk.clipboard_get()
#ultimoCB=" "
ruta = os.getcwd()
ficheros=""
diccionarios=[]
lista=["no encontrado", "not found","sin archivo .pdix","no .pdix file"]
errores=False
RutaDix=""
#defin=""


"""def recortar(recorte):
    #global ultimoCB
    ignorar="()[]{}' .,*:\n!?@#/+^"
    iniciopalabra=-1
    i=0
    while i<len(recorte):        
            if iniciopalabra<0 and ignorar.find(recorte[i])<0:
                iniciopalabra=i
            if iniciopalabra>=0 and ignorar.find(recorte[i])>=0:
                break
            i=1+i
    if iniciopalabra>=0 and i-iniciopalabra>=0:
        recorte=recorte[iniciopalabra:i]
    return recorte
def portapapeles():
    global ultimoCB
    clipboard=""
    try:
        clipboard=cb.wait_for_text()        
        #print clipboard
        if len(clipboard)>0 and len(clipboard)<150 and clipboard.find("http")<0 and ultimoCB!=clipboard:
            ultimoCB=clipboard
            cbpalabra=recortar(clipboard)
            if len(cbpalabra)>0:
                ventana.ventana.textfield.set_text(cbpalabra)
                #return cbpalabra
    except:
        print "no se puede leer el portapapeles"
        """ 
def buscardiccionarios():
    global ruta , errores , ficheros , diccionarios
    dictem=[]
    ficheros = os.listdir(ruta)
    #print ficheros    
    for leer in ficheros:
        if leer[leer.rfind(".")+1:len(leer)+1]=="pdix":
            dictem.append(leer[0:leer.rfind(".")])
            print leer
    if len(dictem)<1:
        errores=True
        diccionarios=["error","vacio","empty"]
    else:
        diccionarios = dictem
def ListarPalabras(diccionario):
    global diccionarios, ruta, lista
    print "listar" , ruta + "/" + diccionarios[diccionario] + ".pdix"
    linea=""
    lista=[]
    try:
        leer = open(ruta + "/" + diccionarios[diccionario] + ".pdix","r")
        while True:
            linea=leer.readline()
            if not linea:
                break
            if linea.find("{")>=0:
                linea=linea[linea.find("{")+1:linea.find("}")]
                lista.append(linea)
                #print linea
    except:
        print "error al leer archivo " , diccionarios[diccionario]
    try:
        leer.close()
    except:
        print "no se pudo cerrar el archivo"
    print len(lista), "  palabras encontradas"
def establecerRutaDix(i):
    global RutaDix,ruta
    RutaDix=ruta+"/"+diccionarios[i]+".pdix"
    #print RutaDix
def definicion(defi):
    global lista, RutaDix
    linea=""
    palabra="{" + lista[defi] + "}"
    Definicion=""
    #try:
    leer=open(RutaDix,"r")
    while True:
        linea=leer.readline()
        #print linea
        if not linea:
            break
        if linea.find(palabra)>=0:
            #print "encontro"
            #print linea
            if linea.find("}[")>=0 and linea.find("]~@")>=0:
                Definicion=Definicion + linea[linea.find("}[")+2:linea.find("]~@")]
                break
            if linea.find("}[")>=0 and linea.find("]~@")<0:
                Definicion=Definicion + linea[linea.find("}[")+2:len(linea)] + "\n"
            while True:
                linea=leer.readline()
        
                if not linea:
                    break
                if linea.find("}[")<0 and linea.find("]~@")<0:
                    Definicion =Definicion + linea + "\n"
                if linea.find("}[")<0 and linea.find("]~@")>=0:
                    Definicion=Definicion + linea[0:linea.find("]~@")]
                    break
            break

    if len(Definicion)==0:
        Definicion="ERROR\n\ndefinicion no encontrada\n\nnot found\n\npor favor abra el archivo .pidx con un editor de texto y revise el formato\n\nplease open the .pdix file whiy an text editor and cheack the format"
    #print Definicion
    #Definicion = unicode(Definicion,'utf-8')#
    ventana.ventana.textBuffer.set_text(Definicion)
    #except:
        #print "error al abrir"
    try:
        leer.close()
    except:
        print "error cerrando el archivo"
    #defin=Definicion
def BuscarPalabra(buscar=""):
    #if len(buscar)==0 no seleccionar nada en el list
    #else
    for i in lista:
        if i.lower().startswith(buscar.lower()):
            #hacer visible en la ventana
            definicion(lista.index(i))
            break
def ejemplo(self,event):
    #ventana.textfield.set_text("waaaa")
    ventana.ventana.textfield.set_text("waaaa")
    print "waaa"
