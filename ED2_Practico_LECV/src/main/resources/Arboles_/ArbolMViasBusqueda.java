
package Arboles;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class ArbolMViasBusqueda<K extends Comparable<K>, V> implements IArbolBusqueda<K, V> {

    protected NodoMVias<K, V> raiz;
    protected int orden;
    protected static final int POSICION_NO_VALIDA = -1;
    protected static final int ORDEN_MINIMO = 3;

    public ArbolMViasBusqueda() {
        this.orden = ORDEN_MINIMO;
    }

    public ArbolMViasBusqueda(int ordenDelArbol) throws ExcepcionOrdenNoValido {
        if (ordenDelArbol < ORDEN_MINIMO) {
            throw new ExcepcionOrdenNoValido();
        }
        this.orden = ordenDelArbol;
    }

    @Override
    public void insertar(K claveAInsertar, V valorAInsertar) throws NullPointerException {
        if (claveAInsertar == NodoMVias.datoVacio()) {
            throw new NullPointerException("No es posible insertar claves nulas");
        }
        if (valorAInsertar == NodoMVias.datoVacio()) {
            throw new NullPointerException("No es posible insertar valores nulos");
        }
        if (this.esArbolVacio()) {
            this.raiz = new NodoMVias<>(this.orden, claveAInsertar, valorAInsertar);
            return;
        }

        NodoMVias<K, V> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            int posicionDeClaveAInsertar = this.getPosicionDeClave(nodoActual, claveAInsertar);
            if (posicionDeClaveAInsertar != ArbolMViasBusqueda.POSICION_NO_VALIDA) {
                nodoActual.setValor(posicionDeClaveAInsertar, valorAInsertar);
                return;
            }

            if (nodoActual.esHoja()) {
                if (nodoActual.estanClavesLLenas()) {
                    NodoMVias<K, V> nuevoHijo = new NodoMVias<>(this.orden, claveAInsertar, valorAInsertar);
                    int posicionDelEnlace = this.getPosicionPorDondeBajar(nodoActual, claveAInsertar);
                    nodoActual.setHijo(posicionDelEnlace, nuevoHijo);
                } else {
                    this.insertarClaveYValorEnNodo(nodoActual, claveAInsertar, valorAInsertar);
                }
                return;
            }

            int posicionPorDondeBajar = this.getPosicionPorDondeBajar(nodoActual, claveAInsertar);
            if (nodoActual.esHijoVacio(posicionPorDondeBajar)) {
                NodoMVias<K, V> nuevoHijo = new NodoMVias<>(this.orden, claveAInsertar, valorAInsertar);
                nodoActual.setHijo(posicionPorDondeBajar, nuevoHijo);
            }

            nodoActual = nodoActual.getHijo(posicionPorDondeBajar);
        }
    }

    private void insertarClaveYValorEnNodo(NodoMVias<K, V> nodoActual, K claveAInsertar, V valorAInsertar) {
        for (int i = nodoActual.cantidadDeClavesNoVacias(); i > 0; i--) {
            K claveActual = nodoActual.getClave(i - 1);
            if (claveActual.compareTo(claveAInsertar) > 0) {
                nodoActual.setClave(i, claveActual);
                nodoActual.setValor(i, nodoActual.getValor(i - 1));
            } else {
                nodoActual.setClave(i, claveAInsertar);
                nodoActual.setValor(i, valorAInsertar);
                return;
            }
        }
        nodoActual.setClave(0, claveAInsertar);
        nodoActual.setValor(0, valorAInsertar);
    }

    protected int getPosicionDeClave(NodoMVias<K, V> nodoActual, K claveABuscar) {
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            K claveActual = nodoActual.getClave(i);
            if (claveABuscar.compareTo(claveActual) == 0) {
                return i;
            }
        }
        return ArbolMViasBusqueda.POSICION_NO_VALIDA;
    }

    protected int getPosicionPorDondeBajar(NodoMVias<K, V> nodoActual, K claveABuscar) {
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            K claveActual = nodoActual.getClave(i);
            if (claveABuscar.compareTo(claveActual) < 0) {
                return i;
            }
        }
        return this.orden - 1;
    }

    @Override
    public V eliminar(K claveAEliminar) throws ExcepcionClaveNoExiste {
        V valorAEliminar = this.buscar(claveAEliminar);
        if (valorAEliminar == null) {
            throw new ExcepcionClaveNoExiste();
        }
        this.raiz = eliminar(this.raiz, claveAEliminar);
        return valorAEliminar;
    }

    private NodoMVias<K, V> eliminar(NodoMVias<K, V> nodoActual, K claveAEliminar) {
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            K claveEnTurno = nodoActual.getClave(i);
            if (claveAEliminar.compareTo(claveEnTurno) == 0) {
                if (nodoActual.esHoja()) {
                    this.eliminarClaveYValor(nodoActual, i);
                    if (nodoActual.cantidadDeClavesNoVacias() == 0) {
                        return NodoMVias.nodoVacio();
                    }
                    return nodoActual;
                }
                // caso de que no sea hoja
                K claveDeReemplazo;
                if (this.tieneHijosMasAdelante(nodoActual, i)) {
                    claveDeReemplazo = this.buscarClaveSucesoraInOrden(nodoActual, i);
                } else {
                    claveDeReemplazo = this.buscarClavePredecesoraInOrden(nodoActual, i);
                }

                V valorAsociadoAClaveDeReemplazo = this.buscar(claveDeReemplazo);

                nodoActual = eliminar(nodoActual, claveDeReemplazo);
                nodoActual.setClave(i, claveDeReemplazo);
                nodoActual.setValor(i, valorAsociadoAClaveDeReemplazo);
                return nodoActual;
            }
            // no encuentro la clave
            if (claveAEliminar.compareTo(claveEnTurno) < 0) {
                NodoMVias<K, V> supuestoHijoNuevo = this.eliminar(nodoActual.getHijo(i), claveAEliminar);
                nodoActual.setHijo(i, supuestoHijoNuevo);
                return nodoActual;
            }
        }
        //fin del for no encontro clave
        NodoMVias<K, V> supuestoHijoNuevo = this.eliminar(nodoActual.getHijo(orden - 1), claveAEliminar);
        nodoActual.setHijo(orden - 1, supuestoHijoNuevo);
        return nodoActual;
    }

    private boolean tieneHijosMasAdelante(NodoMVias<K, V> nodoActual, int posicion) {
        for (int i = posicion + 1; i < orden; i++) {
            if (!NodoMVias.esNodoVacio(nodoActual.getHijo(i))) {
                return true;
            }
        }
        return false;
    }

    private K buscarClaveSucesoraInOrden(NodoMVias<K, V> nodoActual, int posicion) {
        if (!NodoMVias.esNodoVacio(nodoActual.getHijo(posicion + 1))) {
            return nodoActual.getHijo(posicion + 1).getClave(0);
        }
        return nodoActual.getClave(posicion + 1);
    }

    private K buscarClavePredecesoraInOrden(NodoMVias<K, V> nodoActual, int posicion) {
        if (!NodoMVias.esNodoVacio(nodoActual.getHijo(posicion))) {
            return nodoActual.getHijo(posicion).getClave(nodoActual.cantidadDeClavesNoVacias() - 1);
        }
        return nodoActual.getClave(posicion - 1);
    }

    private void eliminarClaveYValor(NodoMVias<K, V> nodoActual, int posicion) {
        for (int i = posicion; i < nodoActual.cantidadDeClavesNoVacias() - 1; i++) {
            nodoActual.setClave(posicion, nodoActual.getClave(posicion + 1));
            nodoActual.setValor(posicion, nodoActual.getValor(posicion + 1));
        }
        nodoActual.setValor(nodoActual.cantidadDeClavesNoVacias() - 1, (V) NodoMVias.datoVacio());
        nodoActual.setClave(nodoActual.cantidadDeClavesNoVacias() - 1, (K) NodoMVias.datoVacio());
    }

    @Override
    public V buscar(K claveABuscar) {
        if (claveABuscar == NodoMVias.datoVacio()) {
            return (V) NodoMVias.datoVacio();
        }
        NodoMVias<K, V> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            boolean huboCambioDeNodo = false;
            for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias() && !huboCambioDeNodo; i++) {
                K claveActual = nodoActual.getClave(i);
                if (claveABuscar.compareTo(claveActual) == 0) {
                    return nodoActual.getValor(i);
                }
                if (claveABuscar.compareTo(claveActual) < 0) {
                    nodoActual = nodoActual.getHijo(i);
                    huboCambioDeNodo = true;
                }
            }
            if (!huboCambioDeNodo) {
                nodoActual = nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias());
            }
        }

        return (V) NodoMVias.datoVacio();
    }

    @Override
    public boolean contiene(K claveABuscar) {
        return this.buscar(claveABuscar) != NodoMVias.datoVacio();
    }

    @Override
    public int size() {
        return size(this.raiz);
    }

    private int size(NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }

        int sizeAcumulado = 0;
//        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
//            int sizeDeHijo = size(nodoActual.getHijo(i));
//            sizeAcumulado += sizeDeHijo;
//        }
//        sizeAcumulado += size(nodoActual.getHijo(orden-1));

        for (int i = 0; i < orden; i++) {
            int sizeDeHijo = size(nodoActual.getHijo(i));
            sizeAcumulado += sizeDeHijo;
        }

        return sizeAcumulado + 1;
    }

    @Override
    public int altura() {
        return altura(this.raiz);
    }

    private int altura(NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }

        int alturaMayorDeHijos = 0;
        for (int i = 0; i < orden; i++) {
            int alturaDeHijo = altura(nodoActual.getHijo(i));
            if (alturaDeHijo > alturaMayorDeHijos) {
                alturaMayorDeHijos = alturaDeHijo;
            }
        }

        return alturaMayorDeHijos + 1;
    }

    @Override
    public void vaciar() {
        this.raiz = NodoMVias.nodoVacio();
    }

    @Override
    public boolean esArbolVacio() {
        return NodoMVias.esNodoVacio(this.raiz);
    }

    @Override
    public List<K> recorridoPorNiveles() {
        List<K> recorrido = new ArrayList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        Queue<NodoMVias<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()) {
            NodoMVias<K, V> nodoActual = colaDeNodos.poll();
            for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
                recorrido.add(nodoActual.getClave(i));
                if (!nodoActual.esHijoVacio(i)) {
                    colaDeNodos.offer(nodoActual.getHijo(i));
                }
            }

            if (!nodoActual.esHijoVacio(nodoActual.cantidadDeClavesNoVacias())) {
                colaDeNodos.offer(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias()));
            }
        }
        return recorrido;
    }

    @Override
    public List<K> recorridoEnPreOrden() {
    	List<K> recorrido = new ArrayList<>();
        return recorrido;
    }

    @Override
    public List<K> recorridoEnInOrden() {
        List<K> recorrido = new ArrayList<>();
        this.recorridoEnInOrden(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnInOrden(NodoMVias<K, V> nodoActual, List<K> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }

        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            recorridoEnInOrden(nodoActual.getHijo(i), recorrido);
            recorrido.add(nodoActual.getClave(i));
        }
        recorridoEnInOrden(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias()), recorrido);
    }

    @Override
    public List<K> recorridoEnPostOrden() {
        List<K> recorrido = new ArrayList<>();
        this.recorridoEnPostOrden(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnPostOrden(NodoMVias<K, V> nodoActual, List<K> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }

        recorridoEnPostOrden(nodoActual.getHijo(0), recorrido);

        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            recorridoEnPostOrden(nodoActual.getHijo(i + 1), recorrido);
            recorrido.add(nodoActual.getClave(i));
        }
    }

    public int cantidadDeClavesNoVacias() {
        return cantidadDeClavesNoVacias(this.raiz);
    }

    private int cantidadDeClavesNoVacias(NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }

        int clavesNoVaciasAcumuladas = nodoActual.cantidadDeClavesNoVacias();
        for (int i = 0; i < orden; i++) {
            int claveNoVaciasDelHijo = cantidadDeClavesNoVacias(nodoActual.getHijo(i));
            clavesNoVaciasAcumuladas += claveNoVaciasDelHijo;
        }
        return clavesNoVaciasAcumuladas;
    }

    public int cantidadDeClavesVacias() {
        int cantidadDeClavesVacias = 0;
        if (this.esArbolVacio()) {
            return 0;
        }

        Queue<NodoMVias<K, V>> colaDeNodosMVias = new LinkedList<>();
        colaDeNodosMVias.offer(this.raiz);
        while (!colaDeNodosMVias.isEmpty()) {
            NodoMVias<K, V> nodoActual = colaDeNodosMVias.poll();
            if (!nodoActual.esHoja()) {
                for (int i = 0; i < orden; i++) {
                    if (!NodoMVias.esNodoVacio(nodoActual.getHijo(i))) {
                        colaDeNodosMVias.offer(nodoActual.getHijo(i));
                    }
                }
            } else {
                cantidadDeClavesVacias += nodoActual.cantidadDeClavesVacias();
            }
        }
        return cantidadDeClavesVacias;
    }

    public int cantidadDeHijosVacios() {
        int cantidadDeHijosVacios = 0;
        Queue<NodoMVias<K, V>> colaDeNodosMVias = new LinkedList<>();
        colaDeNodosMVias.offer(this.raiz);
        while (!colaDeNodosMVias.isEmpty()) {
            NodoMVias<K, V> nodoActual = colaDeNodosMVias.poll();
            cantidadDeHijosVacios += nodoActual.cantidadDeHijosVacios();
            for (int i = 0; i < orden; i++) {
                if (!NodoMVias.esNodoVacio(nodoActual.getHijo(i))) {
                    colaDeNodosMVias.offer(nodoActual.getHijo(i));
                }
            }
        }
        return cantidadDeHijosVacios;
    }

    public int cantidadDeHijosVaciosEnUnNivel(int nivel) {
        if (nivel == 0) {
            return this.raiz.cantidadDeHijosVacios();
        }
        int cantidadDeHijosEnUnNivel = this.raiz.cantidadDeHijosNoVacios();
        int cantidadDeHijosVacios = 0;
        int cantidadDeHijosEnElSiguienteNivel = 0;
        int nivelActual = 0;
        Queue<NodoMVias<K, V>> colaDeNodosMVias = new LinkedList<>();
        colaDeNodosMVias.offer(this.raiz);
        while (!colaDeNodosMVias.isEmpty() && nivelActual != nivel) {
            NodoMVias<K, V> nodoActual = colaDeNodosMVias.poll();
            for (int i = 0; i < orden; i++) {
                if (!NodoMVias.esNodoVacio(nodoActual.getHijo(i))) {
                    colaDeNodosMVias.offer(nodoActual.getHijo(i));
                    cantidadDeHijosEnElSiguienteNivel += nodoActual.getHijo(i).cantidadDeHijosNoVacios();
                    cantidadDeHijosVacios += nodoActual.getHijo(i).cantidadDeHijosVacios();
                    cantidadDeHijosEnUnNivel--;
                }
            }
            if (cantidadDeHijosEnUnNivel == 0) {
                cantidadDeHijosEnUnNivel = cantidadDeHijosEnElSiguienteNivel;
                nivelActual++;
                if (nivelActual != nivel) {
                    cantidadDeHijosVacios = 0;
                }
            }
        }
        return cantidadDeHijosVacios;
    }

    public int cantidadDeHijosVaciosHastaUnNivel(int nivel) {
        if (nivel == 0) {
            return this.raiz.cantidadDeHijosVacios();
        }
        int cantidadDeHijosEnUnNivel = this.raiz.cantidadDeHijosNoVacios();
        int cantidadDeHijosVacios = 0;
        int cantidadDeHijosEnElSiguienteNivel = 0;
        int nivelActual = 0;
        Queue<NodoMVias<K, V>> colaDeNodosMVias = new LinkedList<>();
        colaDeNodosMVias.offer(this.raiz);
        while (!colaDeNodosMVias.isEmpty() && nivelActual != nivel) {
            NodoMVias<K, V> nodoActual = colaDeNodosMVias.poll();
            for (int i = 0; i < orden; i++) {
                if (!NodoMVias.esNodoVacio(nodoActual.getHijo(i))) {
                    colaDeNodosMVias.offer(nodoActual.getHijo(i));
                    cantidadDeHijosEnElSiguienteNivel += nodoActual.getHijo(i).cantidadDeHijosNoVacios();
                    cantidadDeHijosVacios += nodoActual.getHijo(i).cantidadDeHijosVacios();
                    cantidadDeHijosEnUnNivel--;
                }
            }
            if (cantidadDeHijosEnUnNivel == 0) {
                cantidadDeHijosEnUnNivel = cantidadDeHijosEnElSiguienteNivel;
                nivelActual++;
            }
        }
        return cantidadDeHijosVacios;
    }

    public int cantidadDeHijosVaciosDesdeUnNivel(int nivel) {

        int cantidadDeHijosEnUnNivel = this.raiz.cantidadDeHijosNoVacios();
        int cantidadDeHijosVacios = 0;
        int cantidadDeHijosEnElSiguienteNivel = 0;
        int nivelActual = 0;
        Queue<NodoMVias<K, V>> colaDeNodosMVias = new LinkedList<>();
        colaDeNodosMVias.offer(this.raiz);
        while (!colaDeNodosMVias.isEmpty()) {
            NodoMVias<K, V> nodoActual = colaDeNodosMVias.poll();
            for (int i = 0; i < orden; i++) {
                if (!NodoMVias.esNodoVacio(nodoActual.getHijo(i))) {
                    colaDeNodosMVias.offer(nodoActual.getHijo(i));
                    cantidadDeHijosEnElSiguienteNivel += nodoActual.getHijo(i).cantidadDeHijosNoVacios();
                    cantidadDeHijosEnUnNivel--;
                }
            }
            if (nivelActual >= nivel) {
                cantidadDeHijosVacios += nodoActual.cantidadDeHijosVacios();
            }
            if (cantidadDeHijosEnUnNivel == 0) {
                cantidadDeHijosEnUnNivel = cantidadDeHijosEnElSiguienteNivel;
                nivelActual++;
            }
        }
        return cantidadDeHijosVacios;
    }

    public boolean ejercicio12(int nivel) {
        if (this.esArbolVacio()) {
            return false;
        }
        Queue<NodoMVias<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        int nivelActual = 0;
        int cantidadDeNodosEnUnNivel = colaDeNodos.size();
        while (!colaDeNodos.isEmpty() && nivelActual != nivel) {
            NodoMVias<K, V> nodoActual = colaDeNodos.poll();
            cantidadDeNodosEnUnNivel--;
            for (int i = 0; i < orden; i++) {
                if (!nodoActual.esHijoVacio(i)) {
                    colaDeNodos.offer(nodoActual.getHijo(i));
                }
            }
            if (cantidadDeNodosEnUnNivel == 0) {
                nivelActual++;
                cantidadDeNodosEnUnNivel = colaDeNodos.size();
            }
        }
        while (!colaDeNodos.isEmpty()) {
            NodoMVias<K, V> nodoActual = colaDeNodos.poll();
            if (!nodoActual.estanClavesLLenas()) {
                return false;
            }
        }
        return true;
    }

    public boolean ejercicio17(ArbolMViasBusqueda<K, V> arbolAComparar) {
        if (!this.esArbolVacio() && !arbolAComparar.esArbolVacio()) {
            if (this.size() == arbolAComparar.size()) {
                Queue<NodoMVias<K, V>> colaDeNodos = new LinkedList<>();
                Queue<NodoMVias<K, V>> colaDeNodosDeArbolAComparar = new LinkedList<>();
                colaDeNodos.offer(this.raiz);
                colaDeNodosDeArbolAComparar.offer(arbolAComparar.raiz);
                while (!colaDeNodos.isEmpty() && !colaDeNodosDeArbolAComparar.isEmpty()) {
                    NodoMVias<K, V> nodoActual = colaDeNodos.poll();
                    NodoMVias<K, V> nodoActualDeArbolAComparar = colaDeNodosDeArbolAComparar.poll();
                    if (nodoActual.cantidadDeClavesNoVacias() != nodoActualDeArbolAComparar.cantidadDeClavesNoVacias()) {
                        return false;
                    }
                    for (int i = 0; i < orden; i++) {
                        if (!nodoActual.esHijoVacio(i) && !nodoActualDeArbolAComparar.esHijoVacio(i)) {
                            colaDeNodos.offer(nodoActual.getHijo(i));
                            colaDeNodosDeArbolAComparar.offer(nodoActualDeArbolAComparar.getHijo(i));
                        } else if (!nodoActual.esHijoVacio(i) && nodoActualDeArbolAComparar.esHijoVacio(i)
                                || nodoActual.esHijoVacio(i) && !nodoActualDeArbolAComparar.esHijoVacio(i)) {
                            return false;
                        }
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }
}
