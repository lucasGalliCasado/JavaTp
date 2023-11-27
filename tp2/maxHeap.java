package aed;


//Creamos una class para un heap que ordena de mayor a menor
public class maxHeap{
    private float[][] heap;
    private int size;
    private int cota;

    //Creamos un heap nuevo sin elementos
    // Complejidad: O(Cota)
    public maxHeap(int cota) {
        this.cota = cota; // O(1)
        this.size = 0;// O(1)
        this.heap = new float[cota][2]; //O(Cota*2)= O(Cota)

    }

   // Complejidad : O(1)
    public void defHeap(float[][] array){
        this.heap = array;//O(1)
        this.size = array.length;//O(1)
    } 

    // Complejidad: O(1)
    public void copyHeap(maxHeap h){
        cota = h.cota;// O(1)
        size = h.size;// O(1)
        heap = h.heap;// O(1)
    }

    //Complejidad: O(1)
    private int padre(int index) {
        // Al hacer division entera (equiv a dividir y tomar parte entera) no nos importa si es
        // el hijo izquierdo o derecho del padre al cual queremos volver
        return (index - 1) / 2;// O(1)
    }

    //Complejidad: O(1)
    private int hijoIzquierdo(int index) {
        return 2 * index + 1;// O(1)
    }

    // Complejidad: O(1)
    private int hijoDerecho(int index) {
        return 2 * index + 2; //O(1)
    }
    
    // Complejidad : O(1)
    private void swap(int i, int j) {
        float[] temp = heap[i];// O(1)
        heap[i] = heap[j];// O(1)
        heap[j] = temp;// O(1)
    }

    //Complejidad : O(log n) +4*O(1) = O(log n)
    public void encolar(float[] value) {
        
        if (size == cota) {// O(1)            
            return;
        }

        // Agregamos el nuevo elemento al final del heap
        
        heap[size] = value;// O(1)
       
        int posActual = size; // O(1)
        
        
        size++;// O(1)   
        
        while (posActual > 0 && heap[posActual][0] > heap[padre(posActual)][0]) {//O(log n)           
            swap(posActual, padre(posActual));// O(1)
            posActual = padre(posActual);//O(1)
        }
         

    }
   

    //Complejidad :O(1)
    public float[] mirarMax(){
        return heap[0];//O(1)
    }


    // Complejidad :  O(1)
    public float[] mirarSegundo(){
        float[] res;
        if(heap[hijoIzquierdo(0)][0] > heap[hijoDerecho(0)][0]){//O(1)
            res = heap[hijoIzquierdo(0)];//O(1)
        }
        else{
            res = heap[hijoDerecho(0)];//O(1)
        }

        return res;
    }

    // Complejidad: O(log n)
    public float[] extraerMax() {
        
        float[] tuplaCero = {0,0};// O(1)

        float[] max = heap[0]; // O(1)
        //Ponemos el ultimo elemento del heap(visto de izq a derecha) como el primero
        heap[0] = heap[size - 1]; // O(1)
        //Ponemos la ultima posicion en 0
        heap[size-1] = tuplaCero; // O(1)
        //Modifcamos el tamano de forma tal que la ultima coordenada no nos importe
        size--;// O(1)

        // Restaurar la propiedad de heap (heapify hacia arriba)
        maxHeapify(0); // O(log n)

        return max;
    }

    //Aplicamos el algoritmo de Floyd sobre el array ingresado
    // Complejidad:  O(n)
    public void heapfiear(){        
        int n = size;//O(1)
       
        // Comenzamos por el ultimo nodo que NO es una hoja        
        int nodoActual = n/2 - 1;//O(1)
        
        //Esto ciclo se repite n/2 - 1 y lo de adentro es O(1), es decir, es O(n)
        while( 0 <= nodoActual ){
            //Nos fijamos si se cumple el orden del heap
            int h = hijoMasGrande(heap,nodoActual);//O(1)
            //Si es necesario, swapeamos
            if(h == 1){
                swapArray(heap,nodoActual,hijoIzquierdo(nodoActual));//O(1)
            }
            else if(h == 0){
                swapArray(heap,nodoActual,hijoDerecho(nodoActual));//O(1)
            }

            nodoActual --;//O(1)
        }
        // Una vez que heapifiamos el array, terminamos
        

    }
    // Complejidad: O(1)
    public void swapArray(float[][] array,int i,int j){
        float[] temp = array[i];//O(1)
        array[i] = array[j];//O(1)
        array[j] = temp;//O(1)
    }

    // Complejidad: O(1)
    public int hijoMasGrande(float[][] array,int n){
        if(heap[hijoDerecho(n)][0] > heap[hijoIzquierdo(n)][0] && heap[hijoDerecho(n)][0] > heap[n][0]){//O(1)
            return 0;//O(1)
        }
        else if(heap[hijoIzquierdo(n)][0] > heap[hijoDerecho(n)][0] && heap[hijoIzquierdo(n)][0] > heap[n][0]){//O(1)
            return 1;//O(1)
        }
        else{
            return -1;//O(1)
        }

    }


    // Complejidad : O(log n)
    private void maxHeapify(int nodoActual) {
        int hIzq = hijoIzquierdo(nodoActual);
        int hDer = hijoDerecho(nodoActual);
        int max = nodoActual;

        if (hIzq < size && heap[hIzq][0] > heap[max][0]) {// O(1)
            max = hIzq;//O(1)
        }
        if (hDer < size && heap[hDer][0] > heap[max][0]) {//O(1)
            max = hDer;//O(1)
        }

        if (max != nodoActual) {//O(1)
            swap(nodoActual, max);//O(1) por función
            maxHeapify(max);
        }
    }

    
}




