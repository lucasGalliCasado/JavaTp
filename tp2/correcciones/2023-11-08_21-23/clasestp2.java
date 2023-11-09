//Aca vamos a poner las clases que vamos haciendo

//Debemos agregar una funcion a ColaDePrioridad que devuelve los primeros dos 
//elementos del heap SIN SACARLOS

//TAMBEIN HAY QUE IMPLEMENTAR ARRAY. Lo hacemos de tal forma que se inicialize en 0 todas las coordenadas


import java.util.Arrays;

public class ColaDePrioridadSinPriorityQueue {
    private int[] heap;
    private int size;
    private int capacity;

    public ColaDePrioridadSinPriorityQueue(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.heap = new int[capacity];
    }

    public void insert(int value) {
        if (size == capacity) {
            // La cola de prioridad está llena, manejar este caso según tus necesidades.
            return;
        }

        // Agregar el nuevo elemento al final del montículo
        heap[size] = value;
        int current = size;
        size++;

        // Restaurar la propiedad de montículo (heapify hacia arriba)
        while (current > 0 && heap[current] < heap[parent(current)]) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    public int extractMin() {
        if (size == 0) {
            // La cola de prioridad está vacía, manejar este caso según tus necesidades.
            return -1;
        }

        // Extraer el elemento mínimo que se encuentra en la raíz del montículo
        int min = heap[0];
        heap[0] = heap[size - 1];
        size--;

        // Restaurar la propiedad de montículo (heapify hacia abajo)
        minHeapify(0);

        return min;
    }

    private void minHeapify(int index) {
        int left = leftChild(index);
        int right = rightChild(index);
        int smallest = index;

        if (left < size && heap[left] < heap[index]) {
            smallest = left;
        }
        if (right < size && heap[right] < heap[smallest]) {
            smallest = right;
        }

        if (smallest != index) {
            swap(index, smallest);
            minHeapify(smallest);
        }
    }

    private int parent(int index) {
        return (index - 1) / 2;
    }

    private int leftChild(int index) {
        return 2 * index + 1;
    }

    private int rightChild(int index) {
        return 2 * index + 2;
    }

    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    public static void main(String[] args) {
        ColaDePrioridadSinPriorityQueue colaDePrioridad = new ColaDePrioridadSinPriorityQueue(10);

        colaDePrioridad.insert(5);
        colaDePrioridad.insert(1);
        colaDePrioridad.insert(3);
        colaDePrioridad.insert(2);

        while (colaDePrioridad.size > 0) {
            int min = colaDePrioridad.extractMin();
            System.out.println("Elemento retirado: " + min);
        }
    }
}