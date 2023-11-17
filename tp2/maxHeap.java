package aed;


//Aca vamos a poner las clases que vamos haciendo

//Debemos agregar una funcion a ColaDePrioridad que devuelve los primeros dos 
//elementos del heap SIN SACARLOS

//TAMBIEN HAY QUE IMPLEMENTAR ARRAY. Lo hacemos de tal forma que se inicialize en 0 todas las coordenadas

//Creamos una class para un heap que ordena de mayor a menor
public class maxHeap{
    private float[][] heap;
    private int size;
    private int cota;

    //Creamos un heap nuevo sin elementos
    // Esto tiene complejidad constante, O(1)
    public maxHeap(int cota) {
        this.cota = cota;
        this.size = 0;
        this.heap = new float[cota][2];

    }

    public void defHeap(float[][] array){
        this.heap = array;
        this.size = array.length;
    } 

    public void copyHeap(maxHeap h){
        cota = h.cota;
        size = h.size;
        heap = h.heap;
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
        float[] temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    // boceto de algoritmo en slide 18
    public void encolar(float[] value) {
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
        while (posActual > 0 && heap[posActual][0] > heap[padre(posActual)][0]) {
            // Lo de adentro del while es O(1)
            swap(posActual, padre(posActual));
            posActual = padre(posActual);
        }
        // 

    }
    // queda 4*O(1) + O(log(n)) -> Tiene complejidad O(log(n))

    //O(1)
    public float[] mirarMax(){
        return heap[0];
    }


    public float[] mirarSegundo(){
        float[] res;
        if(heap[hijoIzquierdo(0)][0] > heap[hijoDerecho(0)][0]){
            res = heap[hijoIzquierdo(0)];
        }
        else{
            res = heap[hijoDerecho(0)];
        }

        return res;
    }

    
    public float[] extraerMax() {
        // Este caso no deberia pasar, lo ponemos por si acaso
        //if (size == 0) {
        //}
        
        float[] tuplaCero = {0,0};

        // Por como esta definido nuestro heap el maximo es el primer elemento de la lsita
        float[] max = heap[0];
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
    public void heapfiear(){

        // Constante, complejidad O(1)
        int n = size;
       
        // Comenzamos por el ultimo nodo que NO es una hoja
        // Constante, complejidad O(1)
        int nodoActual = n/2 - 1;
        
        //Esto ciclo se repite n/2 - 1 y lo de adentro es O(1), es decir, es O(n)
        while( 0 <= nodoActual ){
            //Nos fijamos si se cumple el orden del heap
            int h = hijoMasGrande(heap,nodoActual);
            //Si es necesario, swapeamos
            if(h == 1){
                swapArray(heap,nodoActual,hijoIzquierdo(nodoActual));
            }
            else if(h == 0){
                swapArray(heap,nodoActual,hijoDerecho(nodoActual));
            }

            nodoActual --;
        }
        // Una vez que heapifiamos el array, terminamos
        

    }

    public void swapArray(float[][] array,int i,int j){
        float[] temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // Si ninguno de los hijos es mas grande, da -1 , si el derecho es el mas grande, da 0, si el izquerdo 
    // es el mas grande, da 1
    public int hijoMasGrande(float[][] array,int n){
        if(heap[hijoDerecho(n)][0] > heap[hijoIzquierdo(n)][0] && heap[hijoDerecho(n)][0] > heap[n][0]){
            return 0;
        }
        else if(heap[hijoIzquierdo(n)][0] > heap[hijoDerecho(n)][0] && heap[hijoIzquierdo(n)][0] > heap[n][0]){
            return 1;
        }
        else{
            return -1;
        }

    }

    // Cuando la llamamos de forma no recursiva debe ser con cero!!
    // Se asegura de que se cumpla la relacion de orden a partir del nodo que ingresamos
    private void maxHeapify(int nodoActual) {
        int hIzq = hijoIzquierdo(nodoActual);
        int hDer = hijoDerecho(nodoActual);
        int max = nodoActual;

        if (hIzq < size && heap[hIzq][0] > heap[max][0]) {
            max = hIzq;
        }
        if (hDer < size && heap[hDer][0] > heap[max][0]) {
            max = hDer;
        }

        if (max != nodoActual) {
            swap(nodoActual, max);
            maxHeapify(max);
        }
    }

    
}


//Para transformar un array a un heap usar heapify up, algortimo de Floyd
