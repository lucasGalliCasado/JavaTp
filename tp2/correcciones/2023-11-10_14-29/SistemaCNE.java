//package aed;

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
    maxHeap votosPresidencialesHeap;
    // matriz M donde Mij = votos del partido j en el districto i para los diputados 
    int[][] votosDiputados;
    // Lista de heaps para cada districto, donde cada heap tendra los votos de diputados de cada partido en
    // ese districto
    maxHeap[] votosDiputadosXDistHeap;
    
    int[] mesasRegistradas;
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
        this.votosDiputados= new int[nombresDistritos.length][nombresPartidos.length];
        this.mesasRegistradas= new int[0];
        this.totalVotos=0;
        //inicializar los heaps
        this.votosDiputadosXDistHeap = new maxHeap[nombresDistritos.length];
        this.votosPresidencialesHeap = new maxHeap(votosPresidenciales.length);
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
        // del districto dado, i.e. sumamos la fila de votos de diputados.  complejidad O(P)
        int i=0;
        while(i<nombresPartidos.length){
            votosDiputados[distrito][i]=votosDiputados[distrito][i]+actaMesa[i].votosDiputados();
            votosPresidenciales[i]=votosPresidenciales[i]+actaMesa[i].votosPresidente();
            totalVotos=totalVotos+actaMesa[i].votosPresidente();
            i++;
        }
        // Tomamos la fila de la matriz votosDiputados que acabamos de actualizar, i.e. la fila indiceDeMesa(idMesa)
        // y la transformamos en un heap. Complejidad O(P)
        // Ojo! NO tienen que estar los votos en blanco en los heaps de votos de partido x districto, porque les vamos 
        // a tomar max
        
        // Copiamos los votos no blancos de nuestra mesa
        int[] filaId = new int[votosDiputados.length-1];
        for(int j = 0; j < votosDiputados.length-1;j++){
            filaId = votosDiputados[j];
        }
        //heapifiamos
        maxHeap filaIdHeap = new maxHeap(filaId.length);
        filaIdHeap.array2heap(filaId);

        // Le agregamos el heap a la variable correspondiente
        votosDiputadosXDistHeap[i] = filaIdHeap;
        
        //En el caso de votosPresidencialesHeap, es un unico heap, que ahora debemos actualizar
        int[] votosPr = new int[votosPresidenciales.length-1];
        for(int j = 0; j < votosPresidenciales.length-1;j++){
            votosPr[j] = votosPresidenciales[j];
        }
        
        maxHeap votosPrHeap = new maxHeap(votosPresidenciales.length -1); //nuevamente, excluimos los votos en blanco
        votosPrHeap.array2heap(votosPr);
        
        votosPresidencialesHeap = votosPrHeap;


        //agrego la mesa a mesasRegistradas 
        //mesasRegistradas.agregar(idMesa);
    }

    public int votosPresidenciales(int idPartido) {
        return votosPresidenciales[idPartido];
    }

    public int votosDiputados(int idPartido, int idDistrito) {
        return votosDiputados[idDistrito][idPartido];
    }

    public int[] resultadosDiputados(int idDistrito){
        // agarro el heap correspondiente al distrito
        // inicializo un array "res" con longitud P-1, el que voy a devolver
        // saco el primer valor del heap, y sumo 1 al partido que corresponde a ese valor
        // meto el valor dividido res[partido correspondiente]+1, complejidad log(P-1)
        // esto lo repito Dd veces
        throw new UnsupportedOperationException("No implementada aun");
    }

    public boolean hayBallotage(){
        //aca necesitamos metodos q nos devuelvan el primero y segundo de heapPresidentes <- estan implementados en maxHeap
        //float porcetajeA= (heapPresidentes.primero()*100)/totalVotos;
        //float porcetajeB= (heapPresidentes.segundo()*100)/totalVotos;
        //return !(porcetajeA>=45 || (porcetajeA>=40&& (porcetajeA-porcetajeB)>10));
        throw new UnsupportedOperationException("No implementada aun");
    }
}
