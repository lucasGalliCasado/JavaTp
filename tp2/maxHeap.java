package aed;


//Aca vamos a poner las clases que vamos haciendo

//Debemos agregar una funcion a ColaDePrioridad que devuelve los primeros dos 
//elementos del heap SIN SACARLOS

//TAMBIEN HAY QUE IMPLEMENTAR ARRAY. Lo hacemos de tal forma que se inicialize en 0 todas las coordenadas

//Creamos una class para un heap que ordena de mayor a menor
public class maxHeap{
    private Tupla<Integer,Integer>[] heap;
    private int size;
    private int cota;

    //Creamos un heap nuevo sin elementos
    // Esto tiene complejidad constante, O(1)
    public maxHeap(int cota) {
        this.cota = cota;
        this.size = 0;
        this.heap = new Tupla<Integer,Integer>[cota];

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
        Tupla<Integer,Integer> temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    // boceto de algoritmo en slide 18
    public void encolar(Tupla<Integer, Integer> value) {
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
        while (posActual > 0 && heap[posActual].getPrimerElemento() > heap[padre(posActual)].getPrimerElemento()) {
            // Lo de adentro del while es O(1)
            swap(posActual, padre(posActual));
            posActual = padre(posActual);
        }
        // 

    }
    // queda 4*O(1) + O(log(n)) -> Tiene complejidad O(log(n))

    //O(1)
    public Tupla<Integer,Integer> mirarMax(){
        return heap[0];
    }


    public Tupla<Integer,Integer> mirarSegundo(){
        Tupla<Integer,Integer> res;
        if(heap[hijoIzquierdo(0)].getPrimerElemento() > heap[hijoDerecho(0)].getPrimerElemento()){
            res = heap[hijoIzquierdo(0)];
        }
        else{
            res = heap[hijoDerecho(0)];
        }

        return res;
    }

    
    public Tupla<Integer, Integer> extraerMax() {
        // Este caso no deberia pasar, lo ponemos por si acaso
        //if (size == 0) {
        //}
        
        Tupla<Integer,Integer> tuplaCero = new Tupla(0,0);

        // Por como esta definido nuestro heap el maximo es el primer elemento de la lsita
        Tupla<Integer,Integer> max = heap[0];
        //Ponemos el ultimo elemento del heap(visto de izq a derecha) como el primero
        heap[0] = heap[size - 1];
        //Ponemos la ultima posicion en 0
        heap[size-1] = tuplaCero;
        //Modifcamos el tamano de forma tal que la ultima coordenada no nos importe
        size--;

        // Restaurar la propiedad de heap (heapify hacia arriba)
        maxHeapify(0);

        return max;
    }

    //Aplicamos el algoritmo de Floyd sobre el array ingresado
    public void array2heap(Tupla<Integer,Integer>[] array){

        // Creo que no hace falta pero lo dejo comentado por las dudas
        //Tupla<Integer,String>[] arrayConId = new Tupla[array.length];

        //for(int i = 0; i < array.length;i++) {
        //    arrayConId[i] = array[i];
        //}

        //Ponemos el tamano y la cota del heap en el largo del array
        size = array.length;
        cota = array.length;

        // Constante, complejidad O(1)
        int n = array.length;
       
        // Comenzamos por el ultimo nodo que NO es una hoja
        // Constante, complejidad O(1)
        int nodoActual = n/2 - 1;
        
        //Esto ciclo se repite n/2 - 1 y lo de adentro es O(1), es decir, es O(n)
        while( 0 <= nodoActual ){
            //Vemos si se cumple el orden en el hijo derecho
            if(array[nodoActual].getPrimerElemento() < array[hijoDerecho(nodoActual)].getPrimerElemento()){
                //Si no, intercambiamos los valores
                swap(nodoActual, hijoDerecho(nodoActual));
            }
            //Vemos si se cumple el orden en el hijo derecho
            if(array[nodoActual].getPrimerElemento() < array[hijoIzquierdo(nodoActual)].getPrimerElemento()){
                //Si no, intercambiamos los valores
                swap(nodoActual, hijoIzquierdo(nodoActual));
            }

        // Una vez que heapifiamos el array, lo asignamos
        heap = array;

            //De esta forma, si no se cumple el orden, cambiamos el valor del nodo
            // por el valor del hijo mas grande
            
            nodoActual --;
        }
        

    }

    // Cuando la llamamos de forma no recursiva debe ser con cero!!
    // Se asegura de que se cumpla la relacion de orden a partir del nodo que ingresamos
    private void maxHeapify(int nodoActual) {
        int hIzq = hijoIzquierdo(nodoActual);
        int hDer = hijoDerecho(nodoActual);
        int max = nodoActual;

        if (hIzq < size && heap[hIzq].getPrimerElemento() > heap[max].getPrimerElemento()) {
            max = hIzq;
        }
        if (hDer < size && heap[hDer].getPrimerElemento() > heap[max].getPrimerElemento()) {
            max = hDer;
        }

        if (max != nodoActual) {
            swap(nodoActual, max);
            maxHeapify(max);
        }
    }

    
}


//Para transformar un array a un heap usar heapify up, algortimo de Floyd
