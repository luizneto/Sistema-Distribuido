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
	 *  Nome do ficheiro de logging
	 *
	 *    @serialField fileName
	 */
	private String fileName = "log.txt";
	
	/**
	 * Status do gerente
	 */
	private int stat;

	/**
	 * Status do mecânico
	 */
	private String[] st = {"St0", "St1"};
	
	/**
	 * Status do cliente
	 */
	private int s;
		
	/**
	 * Status do carro
	 */
	private int c;
	
	/**
	 * Status de cada cliente se requeriu ou não um carro substituo
	 */
	private int p;
	
	/**
	 * Carro já foi reparado e aguarda cliente
	 */
	private int r;
	
	/**
	 * Número de cliente presentes na fila para serem atendidos pelo gerente
	 */
	private int inq;
		
	/**
	 * Número de cliente presentes na fila aguardando um carro seubstituto
	 */
	private int wtk;
	
	/**
	 * Número de carros já reparados
	 */
	private int nrv;
	
	/**
	 * Número de carros de cliente no estacionamento
	 */
	private int ncv;
	
	/**
	 * Número de carros substitutos no estacionamento
	 */
	private int npv;
	
	/**
	 * Número de job postado pelo gerente
	 */
	private int nsrq;
	
	/**
	 * Número de partes disponíveis no stock
	 */
	private int prt;
	
	/**
	 * Número de carros aguardando peça 
	 */
	private int nv;
	
	/**
	 * Sinalização da peça "0" está faltando no stock
	 */
	private int s0;

	/**
	 * Sinalização da peça "1" está faltando no stock
	 */
	private int s1;

	/**
	 * Sinalização da peça "2" está faltando no stock
	 */
	private int s2;

	/**
	 * Número de partes "0" que foram compradas pelo gerente
	 */
	private int pp0;

	/**
	 * Número de partes "1" que foram compradas pelo gerente
	 */
	private int pp1;

	/**
	 * Número de partes "2" que foram compradas pelo gerente
	 */
	private int pp2;

	
	
	private GenericRepository(String fileName) {
		super();
		this.fileName = fileName;
		this.stat = stat;
		this.st = st;
		this.s = s;
		this.c = c;
		this.p = p;
		this.r = r;
		this.inq = inq;
		this.wtk = wtk;
		this.nrv = nrv;
		this.ncv = ncv;
		this.npv = npv;
		this.nsrq = nsrq;
		this.prt = prt;
		this.nv = nv;
		this.s0 = s0;
		this.s1 = s1;
		this.s2 = s2;
		this.pp0 = pp0;
		this.pp1 = pp1;
		this.pp2 = pp2;
		
		reportInitialStatus();
		reportStatus();
	}

	/**
	 *  Escrever o estado inicial (operaçãoo interna).
	 *  
	 *  <p>Os cliente estão a realizar atividades normais.
	 *  <p>O gerente está dormindo.
	 *  <p>Os mecânicos estão dormindo.
	 *  <p>O estacionamento está sem carros de clientes, somente com os carros de substituição
	 *  <p>A recepição está vazia.
	 *  
	 */
	private void reportInitialStatus () {
		TextFile log = new TextFile ();                      // instanciação de uma variável de tipo ficheiro de texto

		if (!log.openForWriting (".", fileName)) {
			GenericIO.writelnString ("A operaçãoo de criaçãoo do ficheiro " + fileName + " falhou!");
			System.exit (1);
		}
		log.writelnString ("Problema da Atividade da Loja de Reparo");
		if (!log.close ()) {
			GenericIO.writelnString ("A operação de fechar o ficheiro " + fileName + " falhou!");
			System.exit (1);
		}
		reportStatus ();
	}

	private void reportStatus() {
		
		TextFile log = new TextFile ();                      // instanciação de uma variável de tipo ficheiro de texto

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

		if (!log.openForAppending (".", fileName))
		{ GenericIO.writelnString ("A operação de criação do ficheiro " + fileName + " falhou!");
		System.exit (1);
		}

		lineStatus1 += "MAN		MECHANIC																			CUSTOMER ";
		lineStatus2 += "Stat	St0 St1		S00 C00 P00 R00 S01 C01 P01 R01 S02 C02 P02 R02 S03 C03 P03 R03 S04 C04 P04 R04 S05 C05 P05 R05 S06 C06 P06 R06 S07 C07 P07 R07 S08 C08 P08 R08 S09 C09 P09 R09";
		lineStatus3 += "					S10 C10 P10 R10 S11 C11 P11 R11 S12 C12 P12 R12 S13 C13 P13 R13 S14 C14 P14 R14 S15 C15 P15 R15 S16 C16 P16 R16 S17 C17 P17 R17 S18 C18 P18 R18 S19 C19 P19 R19";
		lineStatus4 += "					S20 C20 P20 R20 S21 C21 P21 R21 S22 C22 P22 R22 S23 C23 P23 R23 S24 C24 P24 R24 S25 C25 P25 R25 S26 C26 P26 R26 S27 C27 P27 R27 S28 C28 P28 R28 S29 C29 P29 R29";
		lineStatus5 += "						LOUNGE		  PARK							REPAIR AREA												SUPPLIERS SITE";
		lineStatus6 += "					INQ	 WTK  NRV	 NCV  NPV	NSRQ  PRT0  NV0  S0  PRT1  NV1  S1  PRT2  NV2  S2  							PP0		PP1		PP2";
		//lineStatus7 += stat +"  "+ st +"  "+ inq +" "+ wtk +" "+ nrv +"  "+ ncv +" "+ npv +" "+ nsrq +""+ prt0 +" "+ nv0 +" "+ s0 +" " ;
		lineStatus8 += "";
		lineStatus9 += "";
		lineStatus10 += "";
		
		
		log.writelnString (lineStatus1);
		log.writelnString (lineStatus2);
		log.writelnString (lineStatus3);
		log.writelnString (lineStatus4);
		log.writelnString (lineStatus5);
		log.writelnString (lineStatus6);
		log.writelnString (lineStatus7);
		log.writelnString (lineStatus8);
		log.writelnString (lineStatus9);
		log.writelnString (lineStatus10);
		if (!log.close ())
		{ GenericIO.writelnString ("A operação de fechar o ficheiro " + fileName + " falhou!");
		System.exit (1);
		}
	}

}
