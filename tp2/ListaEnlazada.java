package aed;

public class ListaEnlazada {

    private class Nodo {
    public int valor;
    public Nodo siguiente;

        public Nodo(int valor) { // O(1)
            this.valor = valor;
            this.siguiente = null;
        }
    }

    private Nodo cabeza;
    private int largo;

    public ListaEnlazada() { // O(1)
        this.cabeza = null;
        this.largo=0;
    }

    public int longitud(){ // O(1)
        return largo;
    }

    public void insertarAlInicio(int valor) { // O(1)
        Nodo nuevoNodo = new Nodo(valor);
        nuevoNodo.siguiente = cabeza;
        cabeza = nuevoNodo;
        largo++;
    }

    public boolean esta(int valor){ // O(n)
        boolean res=false;
        while(cabeza!=null){
            if(cabeza.valor==valor){
                res=true;
            }
            cabeza=cabeza.siguiente;
        }
        return res;
    }

    public void eliminar(int valor) { // O(n)
        Nodo temp = cabeza;
        Nodo prev = null;

        if (temp != null && temp.valor == valor) {
            cabeza = temp.siguiente;
            return;
        }

        while (temp != null && temp.valor != valor) {
            prev = temp;
            temp = temp.siguiente;
        }

        if (temp == null) {
            return;
        }

        prev.siguiente = temp.siguiente;
        largo--;
    }
}
