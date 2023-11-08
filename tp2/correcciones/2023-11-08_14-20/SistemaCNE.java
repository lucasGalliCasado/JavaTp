package aed;

//luego quitarlo al crear la clase set
import java.util.*;

public class SistemaCNE {
    // Completar atributos privados

    String[] nombresPartidos;
    String[] nombresDistritos;
    int[] diputadosPorDistritos;
    int[] rangoMesasDistritosInf;
    int[] rangoMesasDistritosSup;
    
    int[] votosPresidenciales;
    // heap con los votos presidenciales de cada partido 
    //HAY QUE AGREGAR

    // matriz M donde Mij = votos del partido j en el districto i para los diputados 
    int[][] votosDiputados;
    // Lista de heaps para cada districto, donde cada heap tendra los votos de diputados de cada partido en
    // ese districto
    //HAY QUE AGREGAR
    
    Set<Integer> mesasRegistradas;
    boolean ballotage;
    int totalVotos;

    

    //pensar en agregar como observador un heap para diputados y otro de presidente
    //estos se actualizan cada vez que agregamos una mesa
    //si hacemos esto podemos sacar balotage y utilizar el heap de presidente

    public class VotosPartido{
        private int presidente;
        private int diputados;
        VotosPartido(int presidente, int diputados){this.presidente = presidente; this.diputados = diputados;}
        public int votosPresidente(){return presidente;}
        public int votosDiputados(){return diputados;}
    }   

    public SistemaCNE(String[] nombresDistritos, int[] diputadosPorDistrito, String[] nombresPartidos, int[] ultimasMesasDistritos) {
        this.nombresDistritos=nombresDistritos;
        this.nombresPartidos=nombresPartidos;
        this.diputadosPorDistritos=diputadosPorDistrito;
        this.rangoMesasDistritosSup=ultimasMesasDistritos;
        this.rangoMesasDistritosInf= new int[ultimasMesasDistritos.length];
        rangoMesasDistritosInf[0]=0;
        int i=1;
        while(i<ultimasMesasDistritos.length){
            rangoMesasDistritosInf[i]=ultimasMesasDistritos[i-1];
            i++;
        }
        // Se inicializa en cero por predeterminado en java tenemos que hacer q nuestro tipo array haga lo mismo
        this.votosPresidenciales= new int[nombresPartidos.length];
        this.mesasRegistradas= new HashSet<>();
        this.votosDiputados= new int[nombresPartidos.length][nombresDistritos.length];
        this.ballotage=true;
        this.totalVotos=0;
    }

    public String nombrePartido(int idPartido) {
        return nombresPartidos[idPartido];
    }

    public String nombreDistrito(int idDistrito) {
        return nombresDistritos[idDistrito];
    }

    public int diputadosEnDisputa(int idDistrito) {
        return diputadosPorDistritos[idDistrito];
    }

    //Dado un idMesa, busca a que disrito pertenece, y el lugar del mismo en nombresDistritos
    private int indiceDeMesa(int idMesa) {
        int izquierda = 0;
        int derecha = rangoMesasDistritosSup.length - 1;

        while (izquierda <= derecha) {

            //medio nos dice el numero del distrito en el que estamos mirando
            int medio = izquierda + (derecha - izquierda) / 2;

            // Si idMesa corresponde a [rangoInf[medio],rangoSup[medio]) retorna medio
            if (rangoMesasDistritosSup[medio] > idMesa && rangoMesasDistritosInf[medio]<=idMesa) {
                return medio;
            }

            // Si idMesa es mas grande que el limite Sup a medio entonces descartamos la mitad de abajo
            if (rangoMesasDistritosSup[medio] <= idMesa) {
                izquierda = medio + 1;
            }

            // Si idMesa es mas chico que el limite Inf a medio entonces descartamos la mitad de arriba
            else {
                derecha = medio - 1;
            }
        }
        return -1;
    }

    public String distritoDeMesa(int idMesa) {
        return nombreDistrito(indiceDeMesa(idMesa));
    }

    public void registrarMesa(int idMesa, VotosPartido[] actaMesa) {
        
        //buscamos el distrito de la mesa, complejidad log(D)
        int distrito=indiceDeMesa(idMesa);

        //sobre la matriz votosDiputados sumamos los votos de cada partido
        // del districto dado, i.e. sumamos la fila de votos de diputados.  complejidad(P)

        // Tomamos la fila de la matriz votosDiputados que acabamos de actualizar, i.e. la fila indiceDeMesa(idMesa)
        // y la transformamos en un heap. Complejidad(P)
        //!!!! Se debe transformar toda la lista junta, no de a uno, sino no da la complejidad!!!!!

        // Si sale bien esto, ya esta garantizada la complejidad de resultados diputados 


        

    }

    public int votosPresidenciales(int idPartido) {
        return votosPresidenciales[idPartido];
    }

    public int votosDiputados(int idPartido, int idDistrito) {
        throw new UnsupportedOperationException("No implementada aun");
    }

    public int[] resultadosDiputados(int idDistrito){

        



        throw new UnsupportedOperationException("No implementada aun");
    }

    public boolean hayBallotage(){
        return ballotage;
    }
}

