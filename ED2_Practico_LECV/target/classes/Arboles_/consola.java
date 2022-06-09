/**
 * 
 */
package Arboles;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * @author Usuario
 *
 */
public class consola {

	/**
	 * @param <Lista>
	 * @param args
	 */
	public static <postOrden, Lista> void main(String[] args) throws ExcepcionClaveNoExiste{
		

		ArbolBinarioBusqueda<Integer,String> arbolPruebaABB = new ArbolBinarioBusqueda<>();
        AVL<Integer,String> arbolPruebaAVL = new AVL<>();
        ArbolBinarioBusqueda<Integer,String> arbolPruebaABB2 = new ArbolBinarioBusqueda<>();
        ArbolMViasBusqueda<Integer,String> arbolPruebaMVias = new ArbolMViasBusqueda<>();
        ArbolMViasBusqueda<Integer,String> arbolPruebaMVias2 = new ArbolMViasBusqueda<>();
        System.out.println("insertando en el AVL");
        arbolPruebaAVL.insertar(50, "a");
        arbolPruebaAVL.insertar(100, "b");
        arbolPruebaAVL.insertar(20, "c");
        arbolPruebaAVL.insertar(30, "d");
        arbolPruebaAVL.insertar(40, "e");
        System.out.println("recorrido por niveles :" + arbolPruebaAVL.recorridoPorNiveles());
        System.out.println("eliminando el 100");
        arbolPruebaAVL.eliminar(100);
        System.out.println("recorrido por niveles :" + arbolPruebaAVL.recorridoPorNiveles());
        System.out.println("insertando en el MVias");
        arbolPruebaMVias.insertar(50, "a");
        arbolPruebaMVias.insertar(100, "b");
        arbolPruebaMVias.insertar(30, "d");
        arbolPruebaMVias.insertar(60, "e");
        arbolPruebaMVias.insertar(70, "f");
        arbolPruebaMVias.insertar(110, "g");
        arbolPruebaMVias.insertar(120, "h");
        arbolPruebaMVias.insertar(300, "i");
        System.out.println("recorrido por niveles :" +arbolPruebaMVias.recorridoPorNiveles());
        System.out.println("eliminando 60, 70, 100");
        arbolPruebaMVias.eliminar(60);
        arbolPruebaMVias.eliminar(70);
        arbolPruebaMVias.eliminar(100);
        System.out.println("recorrido por niveles :" +arbolPruebaMVias.recorridoPorNiveles());
        
        arbolPruebaABB.insertar(75, "a");
        arbolPruebaABB.insertar(60, "b");
        arbolPruebaABB.insertar(43, "d");
        arbolPruebaABB.insertar(68, "e");
        arbolPruebaABB.insertar(56, "f");
        arbolPruebaABB.insertar(70, "f");

        arbolPruebaABB2.insertar(75, "a");
        arbolPruebaABB2.insertar(60, "b");
        arbolPruebaABB2.insertar(43, "d");
        arbolPruebaABB2.insertar(68, "e");
        arbolPruebaABB2.insertar(56, "f");
        arbolPruebaABB2.insertar(70, "f");
        
        
        
        arbolPruebaMVias2.insertar(100, "a");
        arbolPruebaMVias2.insertar(110, "g");
        arbolPruebaMVias2.insertar(50, "b");
        arbolPruebaMVias2.insertar(200, "h");
        arbolPruebaMVias2.insertar(300, "i");
        
        
        System.out.println("2. para un arbol binario de buqueda implementar el recorrido en postOrden iteractivo. :" + arbolPruebaABB.recorridoEnPostOrden());
        System.out.println("3. Para un arbolMvias implementar recorrido en postOrden  :" + arbolPruebaMVias.recorridoEnPostOrden());
        System.out.println("4. para un arbolMvias Implementar recorrido en preOrden  :" + arbolPruebaMVias.recorridoEnPreOrden());
        System.out.println("5. para un arbolMvias Implementar recorrido en InOrden  :" + arbolPruebaMVias.recorridoEnInOrden());
        System.out.println("6. para un arbolAVL implemente el metodo Insertar  : Fue Implemetado" );
        System.out.println("7. para un arbolAVL implemente el metodo Eliminar  : Fue Implemetado" );
        System.out.println("10. para un arbolMvias de busqueda implemente el metodo Insertar  : Fue Implemetado" );
        System.out.println("11. para un arbolMvias de busqueda implemente el metodo Eliminar  : Fue Implemetado" );
        System.out.println("12. Implemente un metodo recursivo que retorne la cantidad de nodos que tienen un solo hijo no vacio. la solucion debe usar un recorrido postOrden: " );
        System.out.println("13. Implemente un metodo iterativo con la logica de un recorrido en inOrden que retorne el numero de hijos vacios: Fue Implemetado" );
        System.out.println("14. Implemente un metodo privado que reciba un nodo binario de un Arbol binario y que retorne cual seria su predecesor inorden de la clave de dicho nodo: Fue Implemetado" );
        System.out.println("15. Implemente un metodo que retorne verdadero si solo hay nodos completos en el nivel n de un arbol Mvias.Falso en caso contrario"  );
        System.out.println("16. Implemente una clase ArbolBinarioBusquedaEnteroCadena que usando como base el arbolBinario de Busqueda con Claves enteras y valores cadena");
        System.out.println("17 para un arbol MVias implementar un metodo que reciba otro arbol de parametro y que retorne verdadero si los arboles son similares.falso en caso contrario    :"+ arbolPruebaMVias.ejercicio17(arbolPruebaMVias2)  );
        System.out.println("18 para un arbol binario de busqueda implementar un metodo que reciba como parametro otro arbol y retorne verdadero si los arboles son similares, falso en caso contrario   : "+ arbolPruebaABB.ejercicio18(arbolPruebaABB2)  );
        
        
        
        
        
    }

}
