
package Arboles;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;


public class ArbolBinarioBusqueda<K extends Comparable<K>, V> implements IArbolBusqueda<K, V> {

    protected NodoBinario<K, V> raiz;

    public void recontruirPostIn(List postOrden, List inOrden) {
    	if(postOrden.size()==0 || inOrden.size()==0) {
    		return;
    	}
    	recontruir(postOrden, inOrden);
    }
    private void recontruir(List postOrden, List inOrden) {
    	List izquierda = new ArrayList<>();
    	List derecha = new ArrayList<>();
    	int raiz = postOrden.indexOf(inOrden.size()-1);
    	int i=0;
    	
    	while(inOrden.indexOf(i)!= raiz) {
    		izquierda.add(inOrden.indexOf(i));
    		i++;
    	}
    	
    	
    }
    
    
    @Override
    public void insertar(K claveAInsertar, V valorAInsertar) throws NullPointerException {
        if (claveAInsertar == null) {
            throw new NullPointerException("No es posible insertar claves nulas");
        }
        if (valorAInsertar == null) {
            throw new NullPointerException("No es posible insertar valores nulos");
        }
        if (this.esArbolVacio()) {
            this.raiz = new NodoBinario<>(claveAInsertar, valorAInsertar);
            return;
        }
        NodoBinario<K, V> nodoAnterior = NodoBinario.nodoVacio();
        NodoBinario<K, V> nodoActual = this.raiz;

        while (!NodoBinario.esNodoVacio(nodoActual)) {
            nodoAnterior = nodoActual;
            K claveActual = nodoActual.getClave();
            if (claveAInsertar.compareTo(claveActual) < 0) {
                nodoActual = nodoActual.getHijoIzquierdo();
            } else if (claveAInsertar.compareTo(claveActual) > 0) {
                nodoActual = nodoActual.getHijoDerecho();
            } else {
                nodoActual.setValor(valorAInsertar);
                return;
            }
        }

        NodoBinario<K, V> nuevoNodo = new NodoBinario<>(claveAInsertar, valorAInsertar);
        K claveAnterior = nodoAnterior.getClave();
        if (claveAInsertar.compareTo(claveAnterior) < 0) {
            nodoAnterior.setHijoIzquierdo(nuevoNodo);
        } else {
            nodoAnterior.setHijoDerecho(nuevoNodo);
        }

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

    private NodoBinario<K, V> eliminar(NodoBinario<K, V> nodoActual, K claveAEliminar) {
        K claveActual = nodoActual.getClave();
        if (claveAEliminar.compareTo(claveActual) < 0) {
            NodoBinario<K, V> aparenteNuevoHijoIzquierdo
                    = eliminar(nodoActual.getHijoIzquierdo(), claveAEliminar);
            nodoActual.setHijoIzquierdo(aparenteNuevoHijoIzquierdo);
            return nodoActual;
        }

        if (claveAEliminar.compareTo(claveActual) > 0) {
            NodoBinario<K, V> aparenteNuevoHijoDerecho
                    = eliminar(nodoActual.getHijoDerecho(), claveAEliminar);
            nodoActual.setHijoDerecho(aparenteNuevoHijoDerecho);
            return nodoActual;
        }

        if (nodoActual.esHoja()) {
            return NodoBinario.nodoVacio();
        }

        if (!nodoActual.esVacioHijoIzquierdo()
                && nodoActual.esVacioHijoDerecho()) {
            return nodoActual.getHijoIzquierdo();
        }
        if (nodoActual.esVacioHijoIzquierdo()
                && !nodoActual.esVacioHijoDerecho()) {
            return nodoActual.getHijoDerecho();
        }

        NodoBinario<K, V> nodoDelSucesor = this.nodoSucesor(nodoActual.getHijoDerecho());

        NodoBinario<K, V> aparenteNuevoHijoDerecho = this.eliminar(
                nodoActual.getHijoDerecho(), nodoDelSucesor.getClave());

        nodoActual.setHijoDerecho(aparenteNuevoHijoDerecho);
        nodoActual.setClave(nodoDelSucesor.getClave());
        nodoActual.setValor(nodoDelSucesor.getValor());

        return nodoActual;
    }

    protected NodoBinario<K, V> nodoSucesor(NodoBinario<K, V> nodoActual) {
        NodoBinario<K, V> nodoAnterior = NodoBinario.nodoVacio();
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            nodoAnterior = nodoActual;
            nodoActual = nodoActual.getHijoIzquierdo();

        }
        return nodoAnterior;
    }

    @Override
    public V buscar(K claveABuscar) {
        if (claveABuscar == null) {
            return null;
        }
        NodoBinario<K, V> nodoActual = this.raiz;
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            K claveActual = nodoActual.getClave();
            if (claveABuscar.compareTo(claveActual) < 0) {
                nodoActual = nodoActual.getHijoIzquierdo();
            } else if (claveABuscar.compareTo(claveActual) > 0) {
                nodoActual = nodoActual.getHijoDerecho();
            } else {
                return nodoActual.getValor();
            }
        }
        return null;
    }

    @Override
    public boolean contiene(K claveABuscar) {
        return this.buscar(claveABuscar) != null;
    }

    @Override
    public int size() {
        int cantidad = 0;
        if (this.esArbolVacio()) {
            return cantidad;
        }
        Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
        pilaDeNodos.push(this.raiz);
        while (!pilaDeNodos.isEmpty()) {
            NodoBinario<K, V> nodoActual = pilaDeNodos.pop();
            cantidad++;
            if (!nodoActual.esVacioHijoDerecho()) {
                pilaDeNodos.push(nodoActual.getHijoDerecho());
            }
            if (!nodoActual.esVacioHijoIzquierdo()) {
                pilaDeNodos.push(nodoActual.getHijoIzquierdo());
            }
        }
        return cantidad;
    }

    public int sizeRec() {
        return sizeRec(this.raiz);
    }

    private int sizeRec(NodoBinario<K, V> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }
        int sizePorDerecha = sizeRec(nodoActual.getHijoDerecho());
        int sizePorIzquierda = sizeRec(nodoActual.getHijoIzquierdo());
        return sizePorDerecha + sizePorIzquierda + 1;
    }

    @Override
    public int altura() {
        return altura(this.raiz);
    }

    protected int altura(NodoBinario<K, V> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }
        int alturaPorIzquierda = sizeRec(nodoActual.getHijoIzquierdo());
        int alturaPorDerecha = sizeRec(nodoActual.getHijoDerecho());
        return alturaPorIzquierda > alturaPorDerecha ? alturaPorIzquierda + 1
                : alturaPorDerecha + 1;
    }

    public int alturaIt() {
        int alturaArbol = 0;
        if (this.esArbolVacio()) {
            return alturaArbol;
        }
        Queue<NodoBinario<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()) {
            int nroNodosDelNivel = colaDeNodos.size();
            int contador = 0;
            while (contador < nroNodosDelNivel) {
                NodoBinario<K, V> nodoActual = colaDeNodos.poll();
                contador++;
                if (!nodoActual.esVacioHijoIzquierdo()) {
                    colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                }
                if (!nodoActual.esVacioHijoDerecho()) {
                    colaDeNodos.offer(nodoActual.getHijoDerecho());
                }
            }
            alturaArbol++;

        }
        return alturaArbol;
    }

    @Override
    public void vaciar() {
        this.raiz = NodoBinario.nodoVacio();
    }

    @Override
    public boolean esArbolVacio() {
        return NodoBinario.esNodoVacio(this.raiz);
    }

    @Override
    public List<K> recorridoPorNiveles() {
        List<K> recorrido = new ArrayList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        Queue<NodoBinario<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()) {
            NodoBinario<K, V> nodoActual = colaDeNodos.poll();
            recorrido.add(nodoActual.getClave());
            if (!nodoActual.esVacioHijoIzquierdo()) {
                colaDeNodos.offer(nodoActual.getHijoIzquierdo());
            }
            if (!nodoActual.esVacioHijoDerecho()) {
                colaDeNodos.offer(nodoActual.getHijoDerecho());
            }

        }
        return recorrido;
    }

    @Override
    public List<K> recorridoEnPreOrden() {
        List<K> recorrido = new ArrayList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
        pilaDeNodos.push(this.raiz);
        while (!pilaDeNodos.isEmpty()) {
            NodoBinario<K, V> nodoActual = pilaDeNodos.pop();
            recorrido.add(nodoActual.getClave());
            if (!nodoActual.esVacioHijoDerecho()) {
                pilaDeNodos.push(nodoActual.getHijoDerecho());
            }
            if (!nodoActual.esVacioHijoIzquierdo()) {
                pilaDeNodos.push(nodoActual.getHijoIzquierdo());
            }
        }
        return recorrido;
    }

    @Override
    public List<K> recorridoEnInOrden() {
        List<K> recorrido = new ArrayList<>();
        this.recorridoEnInOrden(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnInOrden(NodoBinario<K, V> nodoActual, List<K> recorrido) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return;
        }

        recorridoEnInOrden(nodoActual.getHijoIzquierdo(), recorrido);
        recorrido.add(nodoActual.getClave());
        recorridoEnInOrden(nodoActual.getHijoDerecho(), recorrido);
    }

    @Override
    public List<K> recorridoEnPostOrden() {
        List<K> recorrido = new ArrayList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
        NodoBinario<K, V> nodoActual = this.raiz;
        meterEnPilaParaPostOrden(nodoActual, pilaDeNodos);

        while (!pilaDeNodos.isEmpty()) {
            nodoActual = pilaDeNodos.pop();
            recorrido.add(nodoActual.getClave());

            if (!pilaDeNodos.isEmpty()) {
                NodoBinario<K, V> nodoDelTope = pilaDeNodos.peek();
                if (!nodoDelTope.esVacioHijoDerecho()
                        && nodoDelTope.getHijoDerecho() != nodoActual) {
                    meterEnPilaParaPostOrden(nodoDelTope.getHijoDerecho(), pilaDeNodos);
                }
            }
        }
        return recorrido;
    }

    private void meterEnPilaParaPostOrden(NodoBinario<K, V> nodoActual, Stack<NodoBinario<K, V>> pilaDeNodos) {
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            pilaDeNodos.push(nodoActual);
            if (!nodoActual.esVacioHijoIzquierdo()) {
                nodoActual = nodoActual.getHijoIzquierdo();
            } else {
                nodoActual = nodoActual.getHijoDerecho();
            }
        }
    }

    public List<K> recorridoEnInOrdenIterativo() {
        List<K> recorrido = new ArrayList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
        NodoBinario<K, V> nodoActual = this.raiz;
        pilaDeNodos.add(nodoActual);
        meterEnPilaInOrden(nodoActual.getHijoIzquierdo(), pilaDeNodos);
        while (!pilaDeNodos.isEmpty()) {
            nodoActual = pilaDeNodos.pop();
            recorrido.add(nodoActual.getClave());
            if (!NodoBinario.esNodoVacio(nodoActual.getHijoDerecho())) {
                nodoActual = nodoActual.getHijoDerecho();
                pilaDeNodos.add(nodoActual);
                meterEnPilaInOrden(nodoActual.getHijoIzquierdo(), pilaDeNodos);
            }
        }
        return recorrido;
    }

    private void meterEnPilaInOrden(NodoBinario<K, V> nodoActual, Stack<NodoBinario<K, V>> pilaDeNodos) {
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            pilaDeNodos.add(nodoActual);
            nodoActual = nodoActual.getHijoIzquierdo();
        }
    }

    //Implemente un método iterativo con el recorrido en inorden que retorne la cantidad de
    //nodos que tienen ambos hijos distintos de vacío en un árbol binario
    public int ejercicio7() {
        int cantidad = 0;
        if (this.esArbolVacio()) {
            return cantidad;
        }
        Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
        NodoBinario<K, V> nodoActual = this.raiz;
        pilaDeNodos.add(nodoActual);
        meterEnPilaInOrden(nodoActual.getHijoIzquierdo(), pilaDeNodos);
        while (!pilaDeNodos.isEmpty()) {
            nodoActual = pilaDeNodos.pop();
            if (!NodoBinario.esNodoVacio(nodoActual.getHijoDerecho())
                    && !NodoBinario.esNodoVacio(nodoActual.getHijoIzquierdo())) {
                cantidad++;
            }
            if (!NodoBinario.esNodoVacio(nodoActual.getHijoDerecho())) {
                nodoActual = nodoActual.getHijoDerecho();
                pilaDeNodos.add(nodoActual);
                meterEnPilaInOrden(nodoActual.getHijoIzquierdo(), pilaDeNodos);
            }
        }
        return cantidad;
    }

//    Implemente un método recursivo que retorne la cantidad de nodos que tienen un solo hijo
//    no vació
    public int ejercicio8() {
        return ejercicio8(this.raiz);
    }

    private int ejercicio8(NodoBinario<K, V> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }
        int nodosPorIzquierda = ejercicio8(nodoActual.getHijoIzquierdo());
        int nodosPorDerecha = ejercicio8(nodoActual.getHijoDerecho());
        if ((NodoBinario.esNodoVacio(nodoActual.getHijoDerecho()) && !NodoBinario.esNodoVacio(nodoActual.getHijoIzquierdo()))
                || (!NodoBinario.esNodoVacio(nodoActual.getHijoDerecho()) && NodoBinario.esNodoVacio(nodoActual.getHijoIzquierdo()))) {
            return nodosPorIzquierda + nodosPorDerecha + 1;
        }
        return nodosPorIzquierda + nodosPorDerecha;
    }

    // Implemente un método iterativo con la lógica de un recorrido en inOrden que retorne el 
    //número de hijos vacios que tiene un árbol binario.
    public int ejercicio13HIjoVacioIt() {
        int cantidadDeHijosVacios = 0;
        if (this.esArbolVacio()) {
            return cantidadDeHijosVacios;
        }
        Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
        NodoBinario<K, V> nodoActual = this.raiz;
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            pilaDeNodos.add(nodoActual);
            nodoActual = nodoActual.getHijoIzquierdo();
        }
        cantidadDeHijosVacios++;
        while (!pilaDeNodos.isEmpty()) {
            nodoActual = pilaDeNodos.pop();
            nodoActual = nodoActual.getHijoDerecho();
            if (!NodoBinario.esNodoVacio(nodoActual)) {
                pilaDeNodos.add(nodoActual);
                nodoActual = nodoActual.getHijoIzquierdo();
                while (!NodoBinario.esNodoVacio(nodoActual)) {
                    pilaDeNodos.add(nodoActual);
                    nodoActual = nodoActual.getHijoIzquierdo();
                }
                cantidadDeHijosVacios++;
            } else {
                cantidadDeHijosVacios++;
            }
        }
        return cantidadDeHijosVacios;
    }

    
    public K ejercicio11(K claveDelNodoAEncontrar){
        V valor = this.buscar(claveDelNodoAEncontrar);
        NodoBinario<K, V> nodoABuscar = new NodoBinario<>(claveDelNodoAEncontrar, valor);
        return ejercicio11(nodoABuscar).getClave();
    }
    //Implemente un método privado que reciba un nodo binario de un árbol binario y que 
    //retorne cuál sería su predecesor inorden de la clave de dicho nodo.
    private NodoBinario<K, V> ejercicio11(NodoBinario<K, V> nodoAEncontrarPredecesor) {
        if (this.esArbolVacio()) {
            return NodoBinario.nodoVacio();
        }
        K claveDelNodoAEncontrar = nodoAEncontrarPredecesor.getClave();
        Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
        boolean encontrado = false;
        NodoBinario<K, V> nodoActual = this.raiz;
        meterEnPilaInOrden(nodoActual, pilaDeNodos);
        while (!pilaDeNodos.isEmpty()) {
            nodoActual = pilaDeNodos.pop();
            K claveDelNodoActual = nodoActual.getClave();
            if (claveDelNodoAEncontrar.compareTo(claveDelNodoActual) == 0) {
                encontrado = true;
            }
            nodoActual = nodoActual.getHijoDerecho();
            if (!NodoBinario.esNodoVacio(nodoActual)) {
                pilaDeNodos.add(nodoActual);
                nodoActual = nodoActual.getHijoIzquierdo();
                meterEnPilaInOrden(nodoActual, pilaDeNodos);
                if (encontrado == true) {
                    return pilaDeNodos.pop();
                }
            } else if (encontrado == true) {
                return pilaDeNodos.pop();
            }
        }
        return NodoBinario.nodoVacio();
    }

    public boolean ejercicio18(ArbolBinarioBusqueda<K, V> arbolAComparar) {
        if (!this.esArbolVacio() && !arbolAComparar.esArbolVacio()) {
            if (this.size() == arbolAComparar.size()) {
                Queue<NodoBinario<K, V>> colaDeNodos = new LinkedList<>();
                Queue<NodoBinario<K, V>> colaDeNodosDelArbolAComparar = new LinkedList<>();
                colaDeNodos.offer(this.raiz);
                colaDeNodosDelArbolAComparar.offer(arbolAComparar.raiz);
                while (!colaDeNodos.isEmpty() && !colaDeNodosDelArbolAComparar.isEmpty()) {
                    NodoBinario<K, V> nodoActual = colaDeNodos.poll();
                    NodoBinario<K, V> nodoActualDelArbolAComparar = colaDeNodosDelArbolAComparar.poll();
                    if (!nodoActual.esVacioHijoIzquierdo() && !nodoActualDelArbolAComparar.esVacioHijoIzquierdo()) {
                        colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                        colaDeNodosDelArbolAComparar.offer(nodoActualDelArbolAComparar.getHijoIzquierdo());
                    }else if (!nodoActual.esVacioHijoIzquierdo() && nodoActualDelArbolAComparar.esVacioHijoIzquierdo()
                            || nodoActual.esVacioHijoIzquierdo() && !nodoActualDelArbolAComparar.esVacioHijoIzquierdo()) {
                        return false;
                    }
                    if (!nodoActual.esVacioHijoDerecho() && !nodoActualDelArbolAComparar.esVacioHijoDerecho()) {
                        colaDeNodos.offer(nodoActual.getHijoDerecho());
                        colaDeNodosDelArbolAComparar.offer(nodoActualDelArbolAComparar.getHijoDerecho());
                    }else if (!nodoActual.esVacioHijoIzquierdo() && nodoActualDelArbolAComparar.esVacioHijoIzquierdo()
                            || nodoActual.esVacioHijoIzquierdo() && !nodoActualDelArbolAComparar.esVacioHijoIzquierdo()) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
