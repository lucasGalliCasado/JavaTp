public class Tupla<A, B> {
    private A primerElemento;
    private B segundoElemento;

    public Tupla(A primerElemento, B segundoElemento) {
        this.primerElemento = primerElemento;
        this.segundoElemento = segundoElemento;
    }

    public A getPrimerElemento() {
        return primerElemento;
    }

    public B getSegundoElemento() {
        return segundoElemento;
    }

    public void setPrimerElemento(A primerElemento) {
        this.primerElemento = primerElemento;
    }

    public void setSegundoElemento(B segundoElemento) {
        this.segundoElemento = segundoElemento;
    }

    @Override
    public String toString() {
        return "(" + primerElemento + ", " + segundoElemento + ")";
    }

    public static void main(String[] args) {
        Tupla<String, Integer> tupla = new Tupla<>("Hola", 42);
        System.out.println("Primer Elemento: " + tupla.getPrimerElemento());
        System.out.println("Segundo Elemento: " + tupla.getSegundoElemento());
        System.out.println("Tupla completa: " + tupla);
    }
}