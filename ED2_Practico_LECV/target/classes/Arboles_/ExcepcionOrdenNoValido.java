
package Arboles;


public class ExcepcionOrdenNoValido extends Exception {

    /**
     * Creates a new instance of <code>ExcepcionClaveNoExiste</code> without
     * detail message.
     */
    public ExcepcionOrdenNoValido() {
        super("Orden del arbol no puede ser menor a 3");
    }

    /**
     * Constructs an instance of <code>ExcepcionClaveNoExiste</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ExcepcionOrdenNoValido(String msg) {
        super(msg);
    }
}
