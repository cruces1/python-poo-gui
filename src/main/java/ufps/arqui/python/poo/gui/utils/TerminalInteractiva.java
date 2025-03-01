package ufps.arqui.python.poo.gui.utils;

import ufps.arqui.python.poo.gui.models.Mensaje;
import java.io.*;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Terminal interactiva que interactura con python.
 */
public class TerminalInteractiva extends Observable{

    private final Logger logger = Logger.getLogger(TerminalInteractiva.class.getName());
    
    private String directorio;
    private Process process;
    private String parameters[];
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private BufferedReader bufferedReaderError;

    public TerminalInteractiva() {
    }

    public void inicializarTerminal(File directorio, String parameters[]) throws IOException {
        this.parameters = parameters;
        // Validar que no se quiera reiniciar la terminal si el directorio es el mismo
        if (this.directorio == null || !directorio.getAbsolutePath().equals(this.directorio)) {
            this.directorio = directorio.getAbsolutePath();
            this.reiniciarTerminal();
        }
    }

    public boolean terminalActiva() {
        return this.process != null;
    }

    /**
     * Reinicia el proceso siempre y cuando el proceso este activo.
     */
    public void reiniciarTerminal() throws IOException {
        if (terminalActiva()) {
            this.process.destroyForcibly();
            this.bufferedReader.close();
            this.bufferedWriter.close();
            this.bufferedWriter.close();
        }

        this.process = new ProcessBuilder(this.parameters).directory(new File(this.directorio)).start();
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.process.getOutputStream()));
        this.bufferedReader = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
        this.bufferedReaderError = new BufferedReader(new InputStreamReader(this.process.getErrorStream()));

        this.leerSalida(this.bufferedReader, false);
        this.leerSalida(this.bufferedReaderError, true);
    }

    /**
     * Ingresar comando para ejecutar.
     *
     * @param command comando de python, debe ser una sola linea, sin salto de linea.
     * @throws IOException en caso de que los buffer no están abiertos.
     */
    public void ingresarComando(String command) throws IOException {
        if (terminalActiva()) {
            bufferedWriter.write(command);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } else {
            throw new IOException("Terminal inactiva");
        }
    }

    /**
     * Lee la salida linea por linea de la terminal de python.
     *
     * @param buffered bufer del archivo de lectura.
     */
    private void leerSalida(BufferedReader buffered, boolean error) {
        new Thread(() -> {
            try {
                String linea = "";
                while ((linea = buffered.readLine()) != null) {
                    this.setChanged();
                    this.notifyObservers(new Mensaje(linea, error));
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error al leer el archivo: " + e.getMessage() + ": " + e.getLocalizedMessage());
            }
        }).start();
    }
    
}
