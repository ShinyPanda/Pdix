import pygtk
pygtk.require('2.0')
#import gtk
#import os#, sys
import socket
#import ventana
import thread
import time
import sys

print "PyDix. diccionarios offline\nalpha 0.02"

try:
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.bind(("",1333))
    server.listen(1)    
except:
    print "puerto 1333 ocupado\n?Otra instancia de la aplicacion se esta ejecutando?"
    #try:
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect(("localhost", 1333))        
    #time.sleep(3)
    s.send("PyDix.show!!!")
    s.shutdown(True)
    s.close()
    print "sennal enviada"
    #except:
    #    pass
    
    exit()
    
import ventana

def escuchapuerto(e,i):
    global server
    time.sleep(5)
    while True:        
        #try:
        socket_cliente, datos_cliente = server.accept()
        mensaje = socket_cliente.recv(32)            #print mensaje , "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
        if mensaje == "PyDix.show!!!":
            if ventana.ventana.window.get_visible():
                ventana.ventana.window.present()
                print "ventana visible. mostrando"
            else:
                print "ventana no visible"
                ventana.ventana.window.set_visible(True)
                print "poniendo al frente"
                #if ventana.ventana.window.get_i
                #time.sleep(3)
                #ventana.ventana.window.present()
                
            
                #time.sleep(3)
        #except:
        #    print "error escuchando puerto"
    
thread.start_new_thread(escuchapuerto,(0,0))
try:
    mostrar = (sys.argv[1] != "hide")
except:
    mostrar = True
    
ventana.start(mostrar,None)
