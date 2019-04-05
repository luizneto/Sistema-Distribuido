package monitors;

import java.util.Random;

import genclass.GenericIO;
import genclass.TextFile;
import mainProgram.Customer;
import mainProgram.Mechanic;

public class RepairArea {

	/**
	 *  Constantes que caracterizam o estado interno dos threads cliente e barbeiro.
	 */

	private final static int SLEEPING = 0,                  // O mec�nico est� dormindo
							 GONEWFIXIT = 0,				// o mec�nico alerta que vai come�ar um trabalho
 							 WORKING  = 1,                  // O mec�nico est� trabalhando
 							 REQUERIDPART = 1,				// O mec�nico vai buscar uma pe�a no stock
 							 REQUERINEWPART = 2,			// O mec�ncico solicita ao gerente uma nova pe�a
 							 TAKECARPARK = 1,				// O mec�ncico leva o carro reparado ao park
 							 TAKEKEYLOUNGE = 1,				// O mec�ncico leva a chave do carro reparado ao park
							 WAITREPAIRPARK = 0,			// O carro est� no park aguardando um mec�nico ir busca-lo para o reparo
							 WAITPART = 1,					// O carro aguarda uma pe�a de reparo
							 WAITCUSTOMERPARK = 2,			// O carro est� no park aguardando o cliente ir busca-lo apos o reparo
							 WAITTURN = 3,                  // O carro aguarda a sua vez de ser consertado
							 REPAIRED = 4;					// O carro est� reparado
	
	
	
	/**
	 *  N�mero de mec�ncios na �rea de reparo
	 *
	 *    @serialField nMechanic
	 */
	private int nMechanic = 0;

	/**
	 *  N�mero de me�ncicos ativos em um reparo 
	 *
	 *    @serialField nMecOnFixIt
	 */
	private int nMecOnFixIt;

	/**
	 *  N�mero de carros que frequentam a �rea de reparo
	 *
	 *    @serialField nCar
	 */
	private int nCar = 0;

	/**
	 *  Estado do reparo do carro
	 *
	 *    @serialField statRepair
	 */
	private int [] stateRepair;

	/**
	 *  Estado presente dos mec�nicos
	 *
	 *    @serialField stateMechanic
	 */
	private int [] stateMechanic;

	/**
	 *  Estado de ocupa��o do n�mero de carros
	 *
	 *    @serialField stateCustomerCar
	 */
	private int[] stateCustomerCar;

	/**
	 *   Carro do cliente a ser reparado
	 *
	 *    @serialField stateCar
	 */
	private Customer customerCar;

	/**
	 *  Fila de espera (carros) aguardando o reparo
	 *
	 *    @serialField queueCar
	 */
	private MemFIFO queueCar;

	/**
	 *  Fila de carros j� reparados
	 *
	 *    @serialField queueCarRepaired
	 */
	private MemFIFO queueCarRepaired;

	/**
	 *  Nome do ficheiro de logging
	 *
	 *    @serialField fileName
	 */
	private String fileName = "log.txt";

	/**
	 *  N�mero de itera��ess do ciclo de vida dos mec�nicos
	 *
	 *    @serialField nIter
	 */
	private int nIter = 0;

	/**
	 * N�mero de carros em espera de reparo
	 * 
	 * @serialField nWaitCarRepair
	 */
	private int nWaitCarRepair;

	/**
	 * Instancia de identifica��o do customer
	 * 
	 * @serialField customer
	 */
	private Customer customer;

	/**
	 * Instancia de identifica��o do park
	 * 
	 * @serialField park
	 */
	private Park park;

	/**
	 * Instancia de identifica��o do lounge
	 * 
	 * @serialField lounge
	 */
	private Lounge lounge;

	/**
	 * Instancia��o do construtor da �rea de reparo
	 * 
	 * @param nMechanic
	 * @param nMecOnFixIt
	 * @param nCar
	 * @param stateMechanic
	 * @param fileName
	 * @param nIter
	 */
	public RepairArea(int nMechanic, int nCar, int[] stateMechanic, String fileName, int nIter) {
		/* Parametros do problemas*/
		if(nMechanic > 0) this.nMechanic = nMechanic;
		if(nCar > 0) this.nCar = nCar;
		if(nIter >0) this.nIter = nIter;

		/* Inicializa��o dos estados internos*/

		nMechanic = 0;
		stateRepair = new int [this.nMechanic]; 					//Inicia o estado de reparo
		stateCustomerCar = new int [this.nMechanic];				//Inicia o estado do carro
		stateMechanic = new int [this.nMechanic];					//Inicia o men�nico para o reparo 

		for (int i = 0; i < this.nMechanic; i++) {
			stateRepair[i] = (Integer) null;
			stateMechanic[i] = SLEEPING;								//Mec�nico est� dormindo 
		}

		for (int i = 0; i < this.nCar; i++) 
			stateMechanic[i] = SLEEPING;
			queueCar = new MemFIFO (this.nWaitCarRepair);				//Entra na fila de carros a espera de reparo

		/* inicializar o ficheiro de logging */
		if ((fileName != null) && !("".equals (fileName))) this.fileName = fileName;
			reportInitialStatus();
	}

	/**
	 *  Escrever o estado inicial (opera��oo interna).
	 *  <p>Os mec�nicos est�o a dormir e os clientes a realizar as tarefas do dia a dia.
	 */
	private void reportInitialStatus () {
		TextFile log = new TextFile ();                      // instancia��o de uma vari�vel de tipo ficheiro de texto

		if (!log.openForWriting (".", fileName)) {
			GenericIO.writelnString ("A opera��oo de cria��oo do ficheiro " + fileName + " falhou!");
			System.exit (1);
		}
		log.writelnString ("Problema da Atividade Oficina de Repara��o");
		log.writelnString ("N�mero de itera��es = " + nIter + "\n");
		if (!log.close ()) {
			GenericIO.writelnString ("A opera��o de fechar o ficheiro " + fileName + " falhou!");
			System.exit (1);
		}
		reportStatus ();
	}

	private void reportStatus() {
		TextFile log = new TextFile ();                      // instancia��o de uma vari�vel de tipo ficheiro de texto

		String lineStatus = "";                              // linha a imprimir

		if (!log.openForAppending (".", fileName))
		{ GenericIO.writelnString ("A opera��o de cria��o do ficheiro " + fileName + " falhou!");
		System.exit (1);
		}

		for (int i = 0; i < nMechanic; i++)
			switch (stateMechanic[i])
			{ case SLEEPING: lineStatus += " DORMINDO ";
			break;
			case WORKING:  lineStatus += " ACTIVIDA ";
			break;
			}

		for (int i = 0; i < nCar; i++)
			switch (stateRepair[i]) {
			case WAITREPAIRPARK: lineStatus += " Carro esperando no park para reparo ";
			break;
			case WAITTURN: lineStatus += " Esperando iniciar reparo ";
			break;
			case WAITPART: lineStatus += " Esperando pe�a ";
			break;
			}
		log.writelnString (lineStatus);
		if (!log.close ())
		{ GenericIO.writelnString ("A opera��o de fechar o ficheiro " + fileName + " falhou!");
		System.exit (1);
		}
	}

	/**
	 *  Opera��o de reparo do carro (originada pelo mec�nico).
	 *
	 *    @return <li> true, se reparou o carro
	 *            <li> false, se n�o reparou o carro
	 */
/*	public boolean goFixIt (int carId) { 
		carId = customer.getCustomerCarId();                 // Passando a identifica��o do carro pelo cliente
		int partId;											 // Identifica��o da pe�a
		int mechanicId = -1;                                 // identifica��o do mec�nico
		boolean newMechanic = false;                         // sinaliza��o de lan�amento de um thread mec�nico

		synchronized (this) {                                // entrada no monitor �rea de reparo
			stateCustomerCar[carId] = WAITREPAIRPARK;		 // Sinaliza que tem um carro em espera para reparo
			reportStatus ();

			if (queueCar.full ())                            // verifica se a fila de carros est� cheia
				return (false);                              // em caso afirmativo, n�o entra
	
			if (nMecOnFixIt < nMechanic) {                   // verifica se tem algum mec�nico livre
				newMechanic = true;                          // sinaliza a necessidade de acorda um mec�nico
			}
			else {
				stateCustomerCar[carId] = WAITTURN;          // Carro fica na espera de um mec�nico
				reportStatus ();
				queueCar.write (Thread.currentThread ());    // Continua a esperar
			}
		}

		synchronized (Thread.currentThread ()) {               					// Entrada no monitor mec�nico
			if (newMechanic)                                             		// Verifica se h� necessidade de acordar um mec�nico
				new Mechanic (mechanicId, this).start ();     					// Acorda um mec�nico
				stateMechanic[mechanicId] = WORKING;         					// Mec�nico est� trabalhando
				reportStatus ();
 			    
				Random random = new Random();
				partId = random.nextInt(3); 									// Gera a pe�a a ser reparado, 

				getRequeridPart(partId);										// Pede a pe�a para reparar o carro (recebe a pe�a pelo stock)
				stateMechanic[mechanicId] = REQUERIDPART;         				// Mec�nico vai buscar uma pe�a no stock
				reportStatus ();
				if(partAvailable(partId) == true) {								// Se tiver a pe�a no stock entra na condi��o
					resumeRepairProcedure(carId, partId);						// Retorna ao reparo do carro
					stateMechanic[mechanicId] = WORKING;         				// Mec�nico est� trabalhando
					reportStatus ();					
				}else {					
					lounge.letManagerKnow(mechanicId, carId, partId);								// Fala com o gerente para solicitar a pe�a
					stateMechanic[mechanicId] = REQUERINEWPART;         		// Mec�nico est� alertando o gerente que precisa de uma pe�a
					reportStatus ();

				}
			try {
				Thread.currentThread ().wait ();                 				// Aguarda a continua��o das opera��es
			}catch (InterruptedException e) {}
		}
		
		return (true);                                       					// Retorna que o reparo est� pronto
	}
*/
	
	public void resumeRepairProcedure(int partId) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Realiza a conversa com o stock para pedir uma pe�a
	 * 
	 * @param partId
	 * @return 
	 */
	public int getRequiredPart (int partId) {
		int[] part = stock();
		int partReturn = -1;
			switch(partId) {
			
			case 3:{
					for(int i = 0; i < part.length; i++) {
						if(part[i] == partId) {
							partReturn = part[i];
							part[i] = (Integer) null;
							return partReturn;
						}	
					}
				}
			case 2:{
					for(int i = 0; i < part.length; i++) {
						if(part[i] == partId) {
							partReturn = part[i];
							part[i] = (Integer) null;
							return partReturn;
						}	
					}
				}
			case 1:{
					for(int i = 0; i < part.length; i++) {
						if(part[i] == partId) {
							partReturn = part[i];
							part[i] = (Integer) null;
							return partReturn;
						}	
					}
				}
			default:
				return partReturn;
				
			}
	}

	/**
	 * M�todo do stock, aqui conta as pe�as dispon�veis no stock.
	 * 
	 * @return
	 */
	public static int[] stock() {
		/**
		 * Pe�as do stock 
		 * 
		 * @serialField part
		 */
		int[] part = new int[3];
		
		part[0] = 1;
		part[1] = 2;
		part[2] = 3;

		return part;
	}
	
	/**
	 * M�todo que valida se existe pe�a em stock
	 * 
	 * @param partId
	 * @return
	 */
	public static boolean partAvailable(int partId) {
		int[] part = stock();

		for(int i = 0; i < part.length; i++) {
			if(part[i] == partId) {
				return true;
			}else {
				return false;
			}

		}
		return false;
	}
	
	/**
	 * Atualiza o stock de pe�as recebidas pelo fornecedor
	 * 
	 * @param newPart
	 */
	public void storePart(int newPart) {
		int[] part = stock();
		 for (int i = 0; i < part.length; i++) {
			 if(part[i] == newPart) {
				 part[i] = newPart;
			 }
		 }

	}
	
	/**
	 * 
	 * @param mechanicId
	 *
	 *    @return <li> true, se tem um novo servi�o 
	 *            <li> false, se n�o tiver um novo servi�o	 
	 */
	public synchronized boolean readThePaper(int mechanicId) {

		  int carId = -1;                                 
	      Customer carCustomer;                                   		// cliente
	      
	      if (queueCar.empty ()) {                            			// Verifica se existe algum carro na fila para reparo
	           stateMechanic[mechanicId] = SLEEPING;               		// Sinaliza que vai colocar o mec�nico para dormir
				reportStatus ();
	         }
	         else { 
	        	 carCustomer = (Customer) queueCar.read ();            // 	        	 
	        	 carId = customer.getCustomerCarId();
	             stateMechanic[mechanicId] = GONEWFIXIT;        	   // Sinaliza que o mec�nico vai come�a um novo reparo
 				 reportStatus ();
	         }
	      reportStatus ();

	      return true;
	}

	/**
	 * M�todo que verifica se o carro est� reparado e se caso esteja,
	 * adiciona o carro reparado em uma fila de carros reparados
	 * 
	 * @param carId
	 */
	public void repairConcluded(int carId) {
		if(stateRepair[carId] == REPAIRED)
			queueCarRepaired.write(carId);
	}

	
	public int startRepairProcedure() {
		int servico = 0;
		
		
		return servico;
	}

	public void registerService() {
		// TODO Auto-generated method stub
		
	}
		
	
}
