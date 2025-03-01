package ufps.arqui.python.poo.gui.views.impl;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.Observable;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import ufps.arqui.python.poo.gui.controllers.IProyectoController;
import ufps.arqui.python.poo.gui.models.Proyecto;
import ufps.arqui.python.poo.gui.utils.ViewTool;
import ufps.arqui.python.poo.gui.utils.ConfGrid;
import ufps.arqui.python.poo.gui.views.IPanelFichero;

/**
 * Implementación de Interfaz lateral del proyecto, donde el usuario puede gestionar sus archivos.
 *
 * @author Sachikia
 */
public class PanelFichero implements IPanelFichero{
    private IProyectoController controller;
    private final JPanel panel;
    
    // elementos de GUI
    private JButton btnNuevoArchivo;
    private JButton btnVerificar;
    private ArbolDinamico arbol;
    
    public PanelFichero(IProyectoController controller) throws Exception {
        this.controller = controller;
        this.panel = new JPanel(new GridBagLayout());
        
        this.btnNuevoArchivo = new JButton("Nuevo archivo");
        this.btnVerificar = new JButton("Verificar");
        this.arbol = new ArbolDinamico(controller);
        
        this.inicializarContenido();
    }
    
    @Override
    public void inicializarContenido() {
        this.btnVerificar.addActionListener(e -> {
            try{
                this.controller.escanearProyecto();
            }catch(IOException err){
                JOptionPane.showMessageDialog(null, err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        ConfGrid config = new ConfGrid(panel, btnNuevoArchivo);
        config.setWeighty(0);
        config.setAnchor(GridBagConstraints.PAGE_START);
        config.setInsets(10, 0, 0, 0);

        ViewTool.insert(config);

        config = new ConfGrid(panel, btnVerificar);
        config.setGridy(1);
        config.setAnchor(GridBagConstraints.PAGE_START);
        config.setInsets(10, 0, 0, 0);

        ViewTool.insert(config);

        config = new ConfGrid(panel, this.arbol.getPanel());
        config.setGridy(2);
        config.setWeightx(1);
        config.setWeighty(1);
        config.setFill(GridBagConstraints.BOTH);
        config.setAnchor(GridBagConstraints.PAGE_END);
        config.setInsets(10, 0, 0, 0);

        ViewTool.insert(config);

//        ViewTool.insert(this.panel, this.btnVerificar, 0, 1, 0, 0, 1, 1, GridBagConstraints.NONE, GridBagConstraints.PAGE_START, new Insets(10, 0, 0, 0), 0, 0);
//        ViewTool.insert(this.panel, this.arbol.getPanel(), 0, 2, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.PAGE_END, new Insets(10, 0, 0, 0), 0, 0);
    }
    
    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof String){
            String update = arg.toString();
            if(update.equals("directoriosTrabajo")){
                Proyecto proyecto = (Proyecto)o;
                this.arbol.populate(proyecto.getDirectorioTrabajo());
            }
        }
    }
    
    @Override
    public JPanel getPanel() {
        return this.panel;
    }
}
