
package Arboles;


public class AVL<K extends Comparable<K>, V> extends ArbolBinarioBusqueda<K, V> {
    private static final byte TOPE_DIFERENCIA = 1;
    
    @Override
    public void insertar(K claveAInsertar, V valorAInsertar) throws NullPointerException{
        if (valorAInsertar == null) {
            throw new NullPointerException("No se permiten claves nulas");
        }
        this.raiz = this.insertar(this.raiz, claveAInsertar, valorAInsertar);
    }
    
    private NodoBinario<K,V> insertar(NodoBinario<K,V> nodoActual, K claveAInsertar, V valorAInsertar){
        if (NodoBinario.esNodoVacio(nodoActual)) {
            NodoBinario<K,V> nuevoNodo = new NodoBinario<K,V>(claveAInsertar, valorAInsertar);
            return nuevoNodo;
        }
        K claveNodoAcutal = nodoActual.getClave();
        if (claveAInsertar.compareTo(claveNodoAcutal) < 0) {
            NodoBinario<K,V> aparenteNuevoHijoIzquierdo = 
                    insertar(nodoActual.getHijoIzquierdo(), claveAInsertar, valorAInsertar);
            nodoActual.setHijoIzquierdo(aparenteNuevoHijoIzquierdo);
            return balancear(nodoActual);
        }
        
        if (claveAInsertar.compareTo(claveNodoAcutal) > 0) {
            NodoBinario<K,V> aparenteNuevoHijoDerecho = 
                    insertar(nodoActual.getHijoDerecho(), claveAInsertar, valorAInsertar);
            nodoActual.setHijoDerecho(aparenteNuevoHijoDerecho);
            return balancear(nodoActual);
        }
        
        nodoActual.setValor(valorAInsertar);
        return nodoActual;
    }
    
    private NodoBinario<K,V> balancear(NodoBinario<K,V> nodoActual){
        int alturaPorIzquierda = altura(nodoActual.getHijoIzquierdo());
        int alturaPorDerecha = altura(nodoActual.getHijoDerecho());
        
        int diferenciaDeAltura = alturaPorIzquierda - alturaPorDerecha;
        if (diferenciaDeAltura > TOPE_DIFERENCIA) {
            NodoBinario<K,V> hijoIzquierdoDelActual = nodoActual.getHijoIzquierdo();
            alturaPorIzquierda = altura(hijoIzquierdoDelActual.getHijoIzquierdo());
            alturaPorDerecha = altura(hijoIzquierdoDelActual.getHijoDerecho());
            if (alturaPorDerecha > alturaPorIzquierda) {
                return rotarDobleALaDerecha(nodoActual);
            }
            return rotarSimpleALaDerecha(nodoActual);
        } else if (diferenciaDeAltura < -TOPE_DIFERENCIA) {
            NodoBinario<K,V> hijoDerechoDelActual = nodoActual.getHijoDerecho();
            alturaPorIzquierda = altura(hijoDerechoDelActual.getHijoIzquierdo());
            alturaPorDerecha = altura(hijoDerechoDelActual.getHijoDerecho());
            if (alturaPorIzquierda > alturaPorDerecha) {
                return rotarDobleALaIzquierda(nodoActual);
            }
            return rotarSimpleALaIzquierda(nodoActual);
        }
        return nodoActual;
    } 
    
    private NodoBinario<K,V> rotarDobleALaDerecha(NodoBinario<K,V> nodoActual){
        NodoBinario<K,V> primerNodoARotar = rotarSimpleALaIzquierda(nodoActual.getHijoIzquierdo());
        nodoActual.setHijoIzquierdo(primerNodoARotar);
        return rotarSimpleALaDerecha(nodoActual);
    }
    
    private NodoBinario<K,V> rotarSimpleALaDerecha(NodoBinario<K,V> nodoActual){
        NodoBinario<K,V> nodoARotar = nodoActual.getHijoIzquierdo();
        nodoActual.setHijoIzquierdo(nodoARotar.getHijoDerecho());
        nodoARotar.setHijoDerecho(nodoActual);
        return nodoARotar;
    }
    
    private NodoBinario<K,V> rotarDobleALaIzquierda(NodoBinario<K,V> nodoActual){
        NodoBinario<K,V> primerNodoARotar = rotarSimpleALaDerecha(nodoActual.getHijoDerecho());
        nodoActual.setHijoDerecho(primerNodoARotar);
        return rotarSimpleALaIzquierda(nodoActual);
    }
    
    private NodoBinario<K,V> rotarSimpleALaIzquierda(NodoBinario<K,V> nodoActual){
        NodoBinario<K,V> nodoARotar = nodoActual.getHijoDerecho();
        nodoActual.setHijoDerecho(nodoARotar.getHijoIzquierdo());
        nodoARotar.setHijoIzquierdo(nodoActual);
        return nodoARotar;
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
            return balancear(nodoActual);
        }

        if (claveAEliminar.compareTo(claveActual) > 0) {
            NodoBinario<K, V> aparenteNuevoHijoDerecho
                    = eliminar(nodoActual.getHijoDerecho(), claveAEliminar);
            nodoActual.setHijoDerecho(aparenteNuevoHijoDerecho);
            return balancear(nodoActual);
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

        return balancear(nodoActual);
    }
}
