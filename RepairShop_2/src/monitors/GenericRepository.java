package monitors;

import genclass.GenericIO;
import genclass.TextFile;
import mainProgram.Customer;
import mainProgram.Manager;
import mainProgram.Mechanic;

/**
 *
 * @author luizn
 *
 */
public class GenericRepository {

    /**
     * Identificação do status
     *
     * @serialField status
     */
    private int status;
    private Customer customer;
    private Manager manager;
    private Mechanic mechanic;
    private Park park;
    private Lounge lounge;
    private OutSideWorld oSWorl;
    private Supplier supplier;

    /**
     * Nome do ficheiro de logging
     *
     * @serialField fileName
     */
    public String fileName = "log.txt";

    /**
     * Status do manager. Inicialmente o estado do manager é 0 (dormindo).
     * Valores possíveis: SLEEPING = 0; ATTENDING_CUSTOMER = 1; POSTING_JOB = 2;
     * CHECKING_WHAT_TO_DO = 3
     */
    public static int Stat = 0;

    /**
     * Status do mecânico St# onde # = 0..1 identifica o mecânico (representado pelo índice do array).
     * Valores possíveis: WAITING_FOR_WORK = 0; FIXING_CAR = 1; ALERTING_MANAGER = 2; CHECKING_STOCK = 3.
     * 0 = Estado inicial;
     */
    public static int[] St = new int[4];

    /**
     * Status do cliente. S# onde # = 0..29 identifica o cliente (representado pelo índice do array).
     * Valores possíveis: NORMAL_LIFE_WITH_CAR = 0; NORMAL_LIFE_WITHOUT_CAR = 1; PARK = 2;RECEPTION = 3;WAITING_REPLACE_CAR = 4.
     * 0 = Estado inicial.
     */
    public static int[] S = new int[30];

    /**
     * Veículo dirigido pelo cliente. C# onde # = 0..29 identifica o cliente (representado pelo índice do array).
     * Valores possíveis: Carro Cliente = clientId; Carro Substituto = R0, R1 ou R3; Não se Aplica = '-'
     */
    public static String[] C = new String[30];

    /**
     * Cliente que solicitou, ou não um carro substituto. P# onde # = 0..29
     * identifica o cliente (representado pelo índice do array). Valores
     * possíveis: 1 = TRUE; 2 = FALSE.
     * 0 = estado inicial para cada carro (total de 30).
     */
    public static int[] P = new int[30];

    /**
     * Número de carros já reparados e aguardando cliente. R# onde # = 0..29
     * identifica o cliente (representado pelo índice do array).
     * Valores possíveis: 1 = TRUE; 2 = FALSE.
     * 0 = estado inicial para cada carro (total de 30).
     */
    public static int[] R = new int[30];

    /**
     * Número de clientes presentes na fila para serem atendidos pelo gerente.
     * 0 = estado inicial
     */
    public static int InQ = 0;

    /**
     * Número de cliente presentes na fila aguardando um carro substituto. 
     * 0 = estado inicial
     */
    public static int WtK = 0;

    /**
     * Número de carros já reparados. 0 = estado inicial
     */
    public static int NRV = 0;

    /**
     * Número de carros de clientes atualmente no estacionamento.
     * 0 = estado inicial.
     */
    public static int NCV = 0;

    /**
     * Número de carros substitutos no estacionamento.
     * 3 = estado inicial
     */
    public static int NPV = 3;

    /**
     * Número de serviços (job) requisitados pelo manager para a Repair Area.
     * 0 = estado inicial
     */
    public static int NSRQ = 0;

    /**
     * Número de partes 0 disponíveis no stock Prt# onde # = 0, 1 ou 2
     * (representando as peças 0, 1 ou 2. 0 = estado inicial
     */
    public static int Prt0 = 0;

    /**
     * Número de partes 1 disponíveis no stock Prt# onde # = 0, 1 ou 2
     * (representando as peças 0, 1 ou 2. 0 = estado inicial
     */
    public static int Prt1 = 0;

    /**
     * Número de partes 2 disponíveis no stock Prt# onde # = 0, 1 ou 2
     * (representando as peças 0, 1 ou 2. 0 = estado inicial
     */
    public static int Prt2 = 0;

    /**
     * Número de carros de clientes aguardando a peça 0 N# onde # = 0, 1 ou 2
     * (representando as peças 0, 1 ou 2. 0 = estado inicial
     */
    public static int NV0 = 0;

    /**
     * Número de carros de clientes aguardando a peça 1 N# onde # = 0, 1 ou 2
     * (representando as peças 0, 1 ou 2. 0 = estado inicial
     */
    public static int NV1 = 0;

    /**
     * Número de carros de clientes aguardando a peça 2 N# onde # = 0, 1 ou 2
     * (representando as peças 0, 1 ou 2. 0 = estado inicial
     */
    public static int NV2 = 0;

    /**
     * Sinalizador de que o manager foi avisado que a peça "0" está faltando no
     * stock. S# onde # = 0, 1 ou 2. Valores possíveis: 0 = estado inicial; 1 =
     * true; 2 = false.
     */
    public static int S0 = 0;

    /**
     * Sinalizador de que o manager foi avisado que a peça "1" está faltando no
     * stock. S# onde # = 0, 1 ou 2. Valores possíveis: 0 = estado inicial; 1 =
     * true; 2 = false.
     */
    public static int S1 = 0;

    /**
     * Sinalizador de que o manager foi avisado que a peça "2" está faltando no
     * stock. S# onde # = 0, 1 ou 2. Valores possíveis: 0 = estado inicial; 1 =
     * true; 2 = false.
     */
    public static int S2 = 0;

    /**
     * Número de partes (peças) do tipo "0" compradas, até o momento, pelo
     * gerente. 0 = estado inicial.
     */
    public static int PP0 = 0;

    /**
     * Número de partes (peças) do tipo "1" compradas, até o momento, pelo
     * gerente. 0 = estado inicial.
     */
    public static int PP1 = 0;

    /**
     * Número de partes (peças) do tipo "2" compradas, até o momento, pelo
     * gerente. 0 = estado inicial.
     */
    public static int PP2 = 0;

    /**
     *
     */
    public GenericRepository() {
        super();
        reportInitialStatus();
    }

    
    /**
     * Escrever o estado inicial (operação interna).
     *
     * <p>
     * Os cliente estão a realizar atividades normais.
     * <p>
     * O gerente está dormindo.
     * <p>
     * Os mecânicos estão dormindo.
     * <p>
     * O estacionamento está sem carros de clientes, somente com os carros de
     * substituição
     * <p>
     * A recepção está vazia.
     *
     */
    private void reportInitialStatus() {
        TextFile log = new TextFile();                      // instanciação de uma variável de tipo ficheiro de texto
                //estado inicial mecânicos
        for (int i = 0; i < this.St.length; i++) {
            this.St[i] = 0;
        }
        //estado inicial cliente
        for (int i = 0; i < this.S.length; i++) {
            this.S[i] = 0;
        }
        //carro dirigido pelo cliente (id cliente = id carro)
        for (int i = 0; i < this.C.length; i++) {
            this.C[i] = String.valueOf(i);
        }
        //estado inicial número de carros reparados e aguardando o cliente
        for (int i = 0; i < this.R.length; i++) {
            this.R[i] = 0;
        }


        if (!log.openForWriting("/Users/clonyjr/Library/Mobile Documents/com~apple~CloudDocs/Aveiro/UA/CLONY/MEI/2018-2019 SEM 2/40814-SD/Netbeans/Trabalho_1_pend/Sistema-Distribuido/RepairShop_2", fileName)) {
            GenericIO.writelnString("A operação de criação do ficheiro " + fileName + " falhou!");
            System.exit(1);
        }
        log.writelnString("Problema da Atividade da Loja de Reparo");
        if (!log.close()) {
            GenericIO.writelnString("A operação de fechar o ficheiro " + fileName + " falhou!");
            System.exit(1);
        }
        reportStatus();
    }

    /**
     * Quando chamado este método registra o status do momento em que foi chamado.
     */
    public void reportStatus() {

        TextFile log = new TextFile();                      // instanciação de uma variável de tipo ficheiro de texto
        System.out.println(log.readChar());

        String lineStatus1 = "";                              // linha a imprimir
        String lineStatus2 = "";                              // linha a imprimir
        String lineStatus3 = "";                              // linha a imprimir
        String lineStatus4 = "";                              // linha a imprimir
        String lineStatus5 = "";                              // linha a imprimir
        String lineStatus6 = "";                              // linha a imprimir
        String lineStatus7 = "";                              // linha a imprimir
        String lineStatus8 = "";                              // linha a imprimir
        String lineStatus9 = "";                              // linha a imprimir
        String lineStatus10 = "";                             // linha a imprimir
        String lineStatus11 = "";                             // linha a imprimir

        if (!log.openForAppending(".", fileName)) {
            GenericIO.writelnString("A operação de criação do ficheiro " + fileName + " falhou!");
            System.exit(1);
        }

        lineStatus1 += "MAN	MECHANIC																			CUSTOMER ";
        lineStatus2 += "Stat	St0 St1		";
        //lineStatus3 += "					S10 C10 P10 R10 S11 C11 P11 R11 S12 C12 P12 R12 S13 C13 P13 R13 S14 C14 P14 R14 S15 C15 P15 R15 S16 C16 P16 R16 S17 C17 P17 R17 S18 C18 P18 R18 S19 C19 P19 R19";
        //lineStatus4 += "					S20 C20 P20 R20 S21 C21 P21 R21 S22 C22 P22 R22 S23 C23 P23 R23 S24 C24 P24 R24 S25 C25 P25 R25 S26 C26 P26 R26 S27 C27 P27 R27 S28 C28 P28 R28 S29 C29 P29 R29";
        for(int i=0; i < 30; i++){
            lineStatus4 += S[i] + "  " + C[i] + "  " + P[i] + "  " + R[i] + "  ";
        }
        lineStatus3 += Stat +"  "+ St[0]+"  "+ St[1] +"		" + lineStatus4;
        lineStatus5 += "						LOUNGE		  PARK							REPAIR AREA												SUPPLIERS SITE";
        lineStatus6 += "					INQ	 WTK  NRV	 NCV  NPV	NSRQ  PRT0  NV0  S0  PRT1  NV1  S1  PRT2  NV2  S2  							PP0		PP1		PP2";
        lineStatus7 += Stat +"  "+ St[0]+"  "+ St[1] +"		" + lineStatus3;
        lineStatus8 += "					"+ InQ +" "+ WtK +" "+ NRV +"  "+ NCV +" "+ NPV +" "+ NSRQ +""+ Prt0 +" " + NV0 +" "+ S0 +" " + Prt1 +" " + NV1 +" "+ S1 +" " + Prt2 +" "+ NV2 +" "+ S2 + "  							" + PP0 + "		" + PP1 + "		" + PP2;
        lineStatus9 += "";
        lineStatus10 += "";
        lineStatus11 += "********************************************************************* NOVA ITERAÇÃO ******************************************************************************";

        
        log.writelnString(lineStatus1);
        log.writelnString(lineStatus2);
        log.writelnString(lineStatus3);
        log.writelnString(lineStatus4);
        log.writelnString(lineStatus5);
        log.writelnString(lineStatus6);
        log.writelnString(lineStatus7);
        log.writelnString(lineStatus8);
        log.writelnString(lineStatus9);
        log.writelnString(lineStatus10);
        log.writelnString(lineStatus11);
        if (!log.close()) {
            GenericIO.writelnString("A operação de fechar o ficheiro " + fileName + " falhou!");
            System.exit(1);
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStat() {
        return Stat;
    }


    public int[] getSt() {
        return St;
    }

    public void setSt(int mecanichId, int State) {
        this.St[mecanichId] = State;
    }

    public int[] getS() {
        return S;
    }



    public String[] getC() {
        return C;
    }

    public int[] getP() {
        return P;
    }


    public int[] getR() {
        return R;
    }


    public int getInQ() {
        return InQ;
    }


    public int getWtK() {
        return WtK;
    }


    public int getNRV() {
        return NRV;
    }


    public int getNCV() {
        return NCV;
    }


    public int getNPV() {
        return NPV;
    }

    public int getNSRQ() {
        return NSRQ;
    }

    public int getPrt0() {
        return Prt0;
    }

    public int getPrt1() {
        return Prt1;
    }

    public int getPrt2() {
        return Prt2;
    }

    public int getNV0() {
        return NV0;
    }

    public int getNV1() {
        return NV1;
    }

    public int getNV2() {
        return NV2;
    }

    public void setNV2(int NV2) {
        this.NV2 = NV2;
    }

    public int getS0() {
        return S0;
    }

    public int getS1() {
        return S1;
    }

    public int getS2() {
        return S2;
    }

    public int getPP0() {
        return PP0;
    }

    public int getPP1() {
        return PP1;
    }

    public int getPP2() {
        return PP2;
    }

    public void setPP2(int PP2) {
        this.PP2 = PP2;
    }
    
}
