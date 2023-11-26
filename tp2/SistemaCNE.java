package aed;
 

public class SistemaCNE {   

    private String[] nombresPartidos; 
    private String[] nombresDistritos;
    private int[] diputadosPorDistritos;
    private int[] rangoMesasDistritosInf;
    private int[] rangoMesasDistritosSup;
    
    private int[] votosPresidenciales;
    // heap con los votos presidenciales de cada partido 
    private maxHeap votosPresidencialesHeap;
    // matriz M donde Mij = votos del partido j en el districto i para los diputados 
    private int[][] votosDiputados;
    // Lista de heaps para cada districto, donde cada heap tendra los votos de diputados de cada partido en
    // ese districto
    private maxHeap[] votosDiputadosXDistHeap;
    
    private boolean[] mesasRegistradas;
    // total de votos
    private int totalVotos;
    // total de votos por distrito
    private int[] totalVotosDist;
    //lista de booleans para saber si ya calcule los resultados de los diputados
    private boolean[] calcDip;
    // resultados de diputados
    private int[][] resDip;
    
    /*
     * InvRep (VotosPartido):
     * 
     * presidente >= 0
     * diputados >= 0
     */
    public class VotosPartido{
        private int presidente;
        private int diputados;
        VotosPartido(int presidente, int diputados){this.presidente = presidente; this.diputados = diputados;}//O(1)
        public int votosPresidente(){return presidente;}//O(1)
        public int votosDiputados(){return diputados;}//O(1)
    }   
// Complejidad: O(1)+ O(3*D)  + O(2*P) + O(DxP) + corregir = O(DxP)
    public SistemaCNE(String[] nombresDistritos, int[] diputadosPorDistrito, String[] nombresPartidos, int[] ultimasMesasDistritos) {
        this.nombresDistritos=nombresDistritos; // O(1)
        this.nombresPartidos=nombresPartidos;// O(1)
        this.diputadosPorDistritos=diputadosPorDistrito; //O(1)
        this.rangoMesasDistritosSup=ultimasMesasDistritos;// O(1)
        this.rangoMesasDistritosInf= new int[ultimasMesasDistritos.length];//O(D) 
        rangoMesasDistritosInf[0]=0;//O(1)
        int i=1;// O(1)
        while(i<ultimasMesasDistritos.length){ //O(D)
            rangoMesasDistritosInf[i]=ultimasMesasDistritos[i-1];// O(1) 
            i++;
        }
        // Se inicializa en cero por predeterminado en java tenemos que hacer q nuestro tipo array haga lo mismo
        this.votosPresidenciales= new int[nombresPartidos.length]; // O(P)
        this.votosDiputados= new int[nombresDistritos.length][nombresPartidos.length]; //O(DxP)
        this.mesasRegistradas= new boolean[ultimasMesasDistritos[ultimasMesasDistritos.length-1]-1]; //Corregir
        this.totalVotos=0;// O(1)
        this.totalVotosDist= new int[nombresDistritos.length];//O(D)
        //inicializar los heaps
        this.votosDiputadosXDistHeap = new maxHeap[nombresDistritos.length];//O(D)
        this.votosPresidencialesHeap = new maxHeap(votosPresidenciales.length-1);// O(P-1) --> O(P)
        this.calcDip = new boolean[nombresDistritos.length]; //O(D)
        this.resDip = new int[nombresDistritos.length][nombresPartidos.length]; //O(DxP)
    }
// Complejidad : O(1)
    public String nombrePartido(int idPartido) {
        return nombresPartidos[idPartido];//O(1)
    }
// Complejidad: O(1)
    public String nombreDistrito(int idDistrito) {
        return nombresDistritos[idDistrito]; //O(1)
    }
// Complejidad: O(1)
    public int diputadosEnDisputa(int idDistrito) {
        return diputadosPorDistritos[idDistrito]; //O(1)
    }

    //Dado un idMesa, busca a que distrito pertenece, y el lugar del mismo en nombresDistritos
    // Complejidad: // Complejidad: O(log D)
    private int indiceDeMesa(int idMesa) {
        int izquierda = 0; //O(1)
        int derecha = rangoMesasDistritosSup.length - 1;//O(1)

        while (izquierda <= derecha) { //O(log(D)) busqueda binaria

            //medio nos dice el numero del distrito en el que estamos mirando
            int medio = izquierda + (derecha - izquierda) / 2; //O(1)

            // Si idMesa corresponde a [rangoInf[medio],rangoSup[medio]) retorna medio
            if (rangoMesasDistritosSup[medio] > idMesa && rangoMesasDistritosInf[medio]<=idMesa) { //O(1) 
                return medio;//O(1)
            }

            // Si idMesa es mas grande que el limite Sup a medio entonces descartamos la mitad de abajo
            if (rangoMesasDistritosSup[medio] <= idMesa) {//O(1)
                izquierda = medio + 1;//O(1)
            }

            // Si idMesa es mas chico que el limite Inf a medio entonces descartamos la mitad de arriba
            else {
                derecha = medio - 1;//O(1)
            }
        }
        return -1;//O(1)
    }

    // Complejidad : O(log D) 
    public String distritoDeMesa(int idMesa) {
        return nombreDistrito(indiceDeMesa(idMesa));//O(log D)
    }

    // Complejidad : O(log(D) + 3*P)= O(log D + P)
    public void registrarMesa(int idMesa, VotosPartido[] actaMesa) {
        
        //buscamos el distrito de la mesa, 
        int distrito=indiceDeMesa(idMesa);//log(D)
        //sobre la matriz votosDiputados sumamos los votos de cada partido
        // del distrito dado, i.e. sumamos la fila de votos de diputados.  
        int i=0; //O(1)
        while(i<nombresPartidos.length){//O(P)
            votosDiputados[distrito][i]=votosDiputados[distrito][i]+actaMesa[i].votosDiputados(); //O(1)
            votosPresidenciales[i]=votosPresidenciales[i]+actaMesa[i].votosPresidente();//O(1)
            totalVotos=totalVotos+actaMesa[i].votosPresidente();//O(1)
            totalVotosDist[distrito]=totalVotosDist[distrito]+actaMesa[i].votosDiputados();//O(1)
            i++;//O(1)
        }
        calcDip[distrito]=false;// O(1)
        resDip[distrito]= new int[nombresPartidos.length-1];//O(P-1)--> O(P)
        // Tomamos la fila de la matriz votosDiputados que acabamos de actualizar, i.e. la fila indiceDeMesa(idMesa)
        // y la transformamos en un heap
        
        // Copiamos los votos no blancos de nuestra mesa
        float[][] filaId = new float[nombresPartidos.length-1][2]; //O((P-1)x2) --> //O(P)

        for(int j = 0; j < nombresPartidos.length-1;j++){ //O(P)
            if(votosDiputados[distrito][j]*100/totalVotosDist[distrito] >=3){ // O(1)
                float[] temp={votosDiputados[distrito][j],j}; //O(1)
                filaId[j] = temp;//O(1)
            } else {
                filaId[j] = new float[]{-1,j};//O(1)
            }
            
        }



        //Creamos el heap para copiar la  info
        maxHeap filaIdHeap = new maxHeap(filaId.length);//O(P)
        // Le asignamos el array como heap SIN HEAPFIAR
        filaIdHeap.defHeap(filaId);//O(P)
        // Heapfiamos el array que esta en el observador heap de filaIdHeap
        filaIdHeap.heapfiear();//O(P)

        // Le agregamos el heap a la variable correspondiente
        votosDiputadosXDistHeap[distrito] = filaIdHeap; //O(P)
        
        //En el caso de votosPresidencialesHeap, es un unico heap, que ahora debemos actualizar
        float[][] votosPr = new float[votosPresidenciales.length-1][2]; //O(P)
        for(int j = 0; j < votosPresidenciales.length-1;j++){ //O(P)
            float[] temp={votosPresidenciales[j],j};//O(1)
            votosPr[j] = temp;//O(1)
        }
        
        maxHeap votosPrHeap = new maxHeap(votosPresidenciales.length -1); //nuevamente, excluimos los votos en blanco O(P)
        votosPrHeap.defHeap(votosPr);//O(P)
        votosPrHeap.heapfiear();//O(P)
        
        votosPresidencialesHeap = votosPrHeap;//O(1)


        mesasRegistradas[idMesa]=true;//O(1)
    }
    // Complejidad: O(1)
    public int votosPresidenciales(int idPartido) {
        return votosPresidenciales[idPartido];//O(1)
    }
    // Complejidad: O(1)
    public int votosDiputados(int idPartido, int idDistrito) {
        return votosDiputados[idDistrito][idPartido];//O(1)
    }
    // Complejidad: O(Dd*log(P))
    public int[] resultadosDiputados(int idDistrito) {
        
        if(calcDip[idDistrito] == false){//O(1)
            
            int cantidad=diputadosPorDistritos[idDistrito];  //O(1)

            int k = 0;//O(1)
            while (k < cantidad) {//O(Dd)
                //O(1) por heap. Miramos y sacamos el maximo(1era coord)
                float[] max = votosDiputadosXDistHeap[idDistrito].extraerMax();
                //Vamos a la posicion que corresponde al partido con mas votos O(1)
                resDip[idDistrito][Math.round(max[1])]=resDip[idDistrito][Math.round(max[1])]+ 1;
                //Dividimos el maximo segun Dhont y encolamos la tupla con la division y su id                 
                float division = votosDiputados[idDistrito][Math.round(max[1])] / (resDip[idDistrito][Math.round(max[1])]+1);//O(1)
                float[] temp={division, max[1]};//O(1)
                votosDiputadosXDistHeap[idDistrito].encolar(temp); //O(log(P)) 
                k++;
            }
            calcDip[idDistrito]=true;//O(1)
        }
        return resDip[idDistrito];//O(1)
    }
    //Complejidad: O(1)
    public boolean hayBallotage() {
        float porcentajeA = (Math.round(votosPresidencialesHeap.mirarMax()[0]) * 100) / totalVotos; // O(1)
        float porcentajeB = (Math.round(votosPresidencialesHeap.mirarSegundo()[0]) * 100) / totalVotos;// O(1)
        return !(porcentajeA >= 45 || (porcentajeA >= 40 && (porcentajeA - porcentajeB) > 10));// O(1)
    }
}

