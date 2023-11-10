//Aca vamos a poner las clases que vamos haciendo

//Debemos agregar una funcion a ColaDePrioridad que devuelve los primeros dos 
//elementos del heap SIN SACARLOS

//TAMBEIN HAY QUE IMPLEMENTAR ARRAY. Lo hacemos de tal forma que se inicialize en 0 todas las coordenadas

import java.util.*;


import java.util.Arrays;

//Creamos una class para un heap que ordena de mayor a menor
public class maxHeap{
    private int[] heap;
    private int size;
    private int cota;

    //Creamos un heap nuevo sin elementos
    // Esto tiene complejidad constante, O(1)
    public maxHeap(int cota) {
        this.cota = cota;
        this.size = 0;
        this.heap = new int[cota];
    }

    //Esto tiene complejidad constante, O(1)
    // Esto es la inversa de la funcion p(v) (ver slide 13) que nos dice la posicion de 
    // un nodo en base a su padre
    private int padre(int index) {
        // Al hacer division entera (equiv a dividir y tomar parte entera) no nos importa si es
        // el hijo izquierdo o derecho del padre al cual queremos volver
        return (index - 1) / 2;
    }

    // Esto tiene complejidad constante, O(1)
    private int hijoIzquierdo(int index) {
        return 2 * index + 1;
    }

    // Esto tiene complejidad constante, O(1)
    private int hijoDerecho(int index) {
        return 2 * index + 2;
    }
    
    // Esto tiene complejidad constante, O(1)
    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    // voceto de algoritmo en slide 18
    public void encolar(int value) {
        // O(1)
        if (size == cota) {
            // Segun la idea detras de nuestra implementacion, no deberia suceder este caso(agregar por encima de la cota)
            // asi que simplemente lo dejamos en return
            return;
        }

        // Agregamos el nuevo elemento al final del heap
        // O(1)
        heap[size] = value;
        //Nos paramos en el elemento que recien ingresamos
        // O(1)
        int posActual = size;
        //aumentamos el tamano del heap
        // O(1)
        size++;
        
        
        // Restaurar la propiedad de heap (heapify hacia arriba)
        // Es decir, si Padre < Hijo, los swapeamos

        // n = old(heap).size

        // tiene complejidad O(log(n))
        while (posActual > 0 && heap[posActual] < heap[padre(posActual)]) {
            // Lo de adentro del while es O(1)
            swap(posActual, padre(posActual));
            posActual = padre(posActual);
        }
        // 

    }

    public int mirarMax(){
        return heap[0];
    }


    public int mirarSegundo(){
        return heap[1];
    }

    public int extraerMax() {
        // Este caso no deberia pasar, lo ponemos por si acaso
        if (size == 0) {
            return -1;
        }

        // Por como esta definido nuestro heap el maximo es el primer elemento de la lsita
        int max = heap[0];
        //Ponemos el ultimo elemento del heap(visto de izq a derecha) como el primero
        heap[0] = heap[size - 1];
        //Ponemos la ultima posicion en 0
        heap[size-1] = 0;
        //Modifcamos el tamano de forma tal que la ultima coordenada no nos importe
        size--;

        // Restaurar la propiedad de heap (heapify hacia arriba)
        maxHeapify(0);

        return max;
    }

    //Aplicamos el algoritmo de Floyd sobre el array ingresado
    private maxHeap array2heap(int[] array){
        // Constante, complejidad O(1)
        maxHeap heapA = new maxHeap(array.length);

        // Constante, complejidad O(1)
        int n = array.length;
       
        // Comenzamos por el ultimo nodo que NO es una hoja
        // Constante, complejidad O(1)
        int nodoActual = n/2 - 1;
        
        //Esto ciclo se repite n/2 - 1 y lo de adentro es O(1), es decir, es O(n)
        while( 0 <= nodoActual ){
            //Vemos si se cumple el orden en el hijo derecho
            if(array[nodoActual] < array[hijoDerecho(nodoActual)]){
                //Si no, intercambiamos los valores
                swap(nodoActual, hijoDerecho(nodoActual));
            }
            //Vemos si se cumple el orden en el hijo derecho
            if(array[nodoActual] < array[hijoIzquierdo(nodoActual)]){
                //Si no, intercambiamos los valores
                swap(nodoActual, hijoIzquierdo(nodoActual));
            }

            //De esta forma, si no se cumple el orden, cambiamos el valor del nodo
            // por el valor del hijo mas grande
            
            nodoActual --;
        }
        
        return heapA;

    }

    // Cuando la llamamos de forma no recursiva debe ser con cero!!
    private void maxHeapify(int nodoActual) {
        int hIzq = hijoIzquierdo(nodoActual);
        int hDer = hijoDerecho(nodoActual);
        int max = nodoActual;

        if (hIzq < size && heap[hIzq] > heap[max]) {
            max = hIzq;
        }
        if (hDer < size && heap[hDer] > heap[max]) {
            max = hDer;
        }

        if (max != nodoActual) {
            swap(nodoActual, max);
            maxHeapify(max);
        }
    }

    
}


//Para transformar un array a un heap usar heapify up, algortimo de Floyd


    