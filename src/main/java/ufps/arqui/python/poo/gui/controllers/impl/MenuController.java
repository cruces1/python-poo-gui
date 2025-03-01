package ufps.arqui.python.poo.gui.controllers.impl;

import java.io.File;
import java.io.IOException;
import ufps.arqui.python.poo.gui.controllers.IMenuController;
import ufps.arqui.python.poo.gui.models.Proyecto;

/**
 * Controlador del menú superior del proyecto.
 *
 * Implementación del controlador menú
 * @author Omar Ramón Montes
 */
public class MenuController implements IMenuController{
    
    private final Proyecto proyecto;

    public MenuController(Proyecto proyecto) {
        this.proyecto = proyecto;
    }   

    @Override
    public void abrirProyecto(String nombre, String directorio) throws Exception {
        File dir = new File(directorio);
        if (dir.exists() && dir.isDirectory()) {
            this.proyecto.setNombre(nombre);
            this.proyecto.setDirectorioRaiz(dir);
        } else {
            throw new Exception("El directorio no existe");
        }
    }

    @Override
    public void crearProyecto(String nombre, String directorio) throws IOException{
        this.proyecto.setNombre(nombre);
        this.proyecto.setDirectorioRaiz(new File(directorio));
    }
    
    @Override
    public void visualizarManualUsuario() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return proyecto.toString();
    }
}
