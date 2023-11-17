package aed;


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
    // total de votos
    int totalVotos;
    // total de votos por distrito
    int[] totalVotosDist;
    //lista de booleans para saber si ya calcule los resultados de los diputados
    boolean[] calcDip;
    // resultados de diputados
    int[][] resDip;

    

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
        this.totalVotosDist= new int[nombresDistritos.length];
        //inicializar los heaps
        this.votosDiputadosXDistHeap = new maxHeap[nombresDistritos.length];
        this.votosPresidencialesHeap = new maxHeap(votosPresidenciales.length-1);
        this.calcDip = new boolean[nombresDistritos.length];
        this.resDip = new int[nombresDistritos.length][nombresPartidos.length];
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
            totalVotosDist[distrito]=totalVotosDist[distrito]+actaMesa[i].votosDiputados();
            i++;
        }
        calcDip[distrito]=false;
        // Tomamos la fila de la matriz votosDiputados que acabamos de actualizar, i.e. la fila indiceDeMesa(idMesa)
        // y la transformamos en un heap. Complejidad O(P)
        // Ojo! NO tienen que estar los votos en blanco en los heaps de votos de partido x districto, porque les vamos 
        // a tomar max
        
        // Copiamos los votos no blancos de nuestra mesa
        float[][] filaId = new float[nombresPartidos.length-1][2];

        for(int j = 0; j < nombresPartidos.length-1;j++){
            if(votosDiputados[distrito][j]*100/totalVotosDist[distrito] >=3){
                float[] temp={votosDiputados[distrito][j],j};
                filaId[j] = temp;
            } else {
                filaId[j] = new float[]{-1,j};
            }
            
        }



        //Creamos el heap para copiar la  info
        maxHeap filaIdHeap = new maxHeap(filaId.length);
        // Le asignamos el array como heap SIN HEAPFIAR
        filaIdHeap.defHeap(filaId);
        // Heapfiamos el array que esta en el observador heap de filaIdHeap
        filaIdHeap.heapfiear();

        // Le agregamos el heap a la variable correspondiente
        votosDiputadosXDistHeap[distrito] = filaIdHeap;
        /*
        votosDiputadosXDistHeap[distrito].defHeap(filaId);
        votosDiputadosXDistHeap[distrito].heapfiear();*/
        //En el caso de votosPresidencialesHeap, es un unico heap, que ahora debemos actualizar
        float[][] votosPr = new float[votosPresidenciales.length-1][2];
        for(int j = 0; j < votosPresidenciales.length-1;j++){
            float[] temp={votosPresidenciales[j],j};
            votosPr[j] = temp;
        }
        
        maxHeap votosPrHeap = new maxHeap(votosPresidenciales.length -1); //nuevamente, excluimos los votos en blanco
        votosPrHeap.defHeap(votosPr);
        votosPrHeap.heapfiear();
        
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

    // el primer test con hacer que no sea por referencia anda
    // en el tercero hay que ver el umbral

    public int[] resultadosDiputados(int idDistrito) {
        
        if(calcDip[idDistrito] == false){
            // estas tres lineas son O(1)
            // creamos una copia del heap para no modificar los datos que tenemos
            maxHeap heap = new maxHeap(nombresPartidos.length);
            heap.copyHeap(votosDiputadosXDistHeap[idDistrito]);
            int cantidad=diputadosPorDistritos[idDistrito];
            int[] contadorCantidadDeBancas = new int[nombresPartidos.length-1];   

            int k = 0;
            while (k < cantidad) {
                //O(1) por heap. Miramos y sacamos el maximo(1era coord)
                float[] max = heap.extraerMax();
                //Vamos a la posicion que corresponde al partido con mas votos O(1)
                contadorCantidadDeBancas[Math.round(max[1])]=contadorCantidadDeBancas[Math.round(max[1])]+ 1;
                //Dividimos el maximo segun Dhont y encolamos la tupla con la division y su id 

                //complejidad O(log(n))
                float division = votosDiputados[idDistrito][Math.round(max[1])] / contadorCantidadDeBancas[Math.round(max[1])]+1;
                float[] temp={division, max[1]};
                heap.encolar(temp);
                k++;
            }
            resDip[idDistrito]=contadorCantidadDeBancas;
            calcDip[idDistrito]=true;
        }
        return resDip[idDistrito];
    }

    public boolean hayBallotage() {
        float porcentajeA = (Math.round(votosPresidencialesHeap.mirarMax()[0]) * 100) / totalVotos;
        float porcentajeB = (Math.round(votosPresidencialesHeap.mirarSegundo()[0]) * 100) / totalVotos;
        return !(porcentajeA >= 45 || (porcentajeA >= 40 && (porcentajeA - porcentajeB) > 10));
    }
}