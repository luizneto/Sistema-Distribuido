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

	private final static int SLEEPING = 0,                  // O mecânico está dormindo
							 GONEWFIXIT = 0,				// o mecânico alerta que vai começar um trabalho
 							 WORKING  = 1,                  // O mecânico está trabalhando
 							 REQUERIDPART = 1,				// O mecânico vai buscar uma peça no stock
 							 REQUERINEWPART = 2,			// O mecâncico solicita ao gerente uma nova peça
 							 TAKECARPARK = 1,				// O mecâncico leva o carro reparado ao park
 							 TAKEKEYLOUNGE = 1,				// O mecâncico leva a chave do carro reparado ao park
							 WAITREPAIRPARK = 0,			// O carro está no park aguardando um mecânico ir busca-lo para o reparo
							 WAITPART = 1,					// O carro aguarda uma peça de reparo
							 WAITCUSTOMERPARK = 2,			// O carro está no park aguardando o cliente ir busca-lo apos o reparo
							 WAITTURN = 3,                  // O carro aguarda a sua vez de ser consertado
							 REPAIRED = 4;					// O carro está reparado
	
	
	
	/**
	 *  Número de mecâncios na área de reparo
	 *
	 *    @serialField nMechanic
	 */
	private int nMechanic = 0;

	/**
	 *  Número de meâncicos ativos em um reparo 
	 *
	 *    @serialField nMecOnFixIt
	 */
	private int nMecOnFixIt;

	/**
	 *  Número de carros que frequentam a área de reparo
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
	 *  Estado presente dos mecânicos
	 *
	 *    @serialField stateMechanic
	 */
	private int [] stateMechanic;

	/**
	 *  Estado de ocupação do número de carros
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
	 *  Fila de carros já reparados
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
	 *  Número de iteraçõess do ciclo de vida dos mecânicos
	 *
	 *    @serialField nIter
	 */
	private int nIter = 0;

	/**
	 * Número de carros em espera de reparo
	 * 
	 * @serialField nWaitCarRepair
	 */
	private int nWaitCarRepair;

	/**
	 * Instancia de identificação do customer
	 * 
	 * @serialField customer
	 */
	private Customer customer;

	/**
	 * Instancia de identificação do park
	 * 
	 * @serialField park
	 */
	private Park park;

	/**
	 * Instancia de identificação do lounge
	 * 
	 * @serialField lounge
	 */
	private Lounge lounge;

	/**
	 * Instanciação do construtor da Área de reparo
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

		/* Inicialização dos estados internos*/

		nMechanic = 0;
		stateRepair = new int [this.nMechanic]; 					//Inicia o estado de reparo
		stateCustomerCar = new int [this.nMechanic];				//Inicia o estado do carro
		stateMechanic = new int [this.nMechanic];					//Inicia o menânico para o reparo 

		for (int i = 0; i < this.nMechanic; i++) {
			stateRepair[i] = (Integer) null;
			stateMechanic[i] = SLEEPING;								//Mecânico está dormindo 
		}

		for (int i = 0; i < this.nCar; i++) 
			stateMechanic[i] = SLEEPING;
			queueCar = new MemFIFO (this.nWaitCarRepair);				//Entra na fila de carros a espera de reparo

		/* inicializar o ficheiro de logging */
		if ((fileName != null) && !("".equals (fileName))) this.fileName = fileName;
			reportInitialStatus();
	}

	/**
	 *  Escrever o estado inicial (operaçãoo interna).
	 *  <p>Os mecânicos estão a dormir e os clientes a realizar as tarefas do dia a dia.
	 */
	private void reportInitialStatus () {
		TextFile log = new TextFile ();                      // instanciação de uma variável de tipo ficheiro de texto

		if (!log.openForWriting (".", fileName)) {
			GenericIO.writelnString ("A operaçãoo de criaçãoo do ficheiro " + fileName + " falhou!");
			System.exit (1);
		}
		log.writelnString ("Problema da Atividade Oficina de Reparação");
		log.writelnString ("Número de iterações = " + nIter + "\n");
		if (!log.close ()) {
			GenericIO.writelnString ("A operação de fechar o ficheiro " + fileName + " falhou!");
			System.exit (1);
		}
		reportStatus ();
	}

	private void reportStatus() {
		TextFile log = new TextFile ();                      // instanciação de uma variável de tipo ficheiro de texto

		String lineStatus = "";                              // linha a imprimir

		if (!log.openForAppending (".", fileName))
		{ GenericIO.writelnString ("A operação de criação do ficheiro " + fileName + " falhou!");
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
			case WAITPART: lineStatus += " Esperando peça ";
			break;
			}
		log.writelnString (lineStatus);
		if (!log.close ())
		{ GenericIO.writelnString ("A operação de fechar o ficheiro " + fileName + " falhou!");
		System.exit (1);
		}
	}

	/**
	 *  Operação de reparo do carro (originada pelo mecânico).
	 *
	 *    @return <li> true, se reparou o carro
	 *            <li> false, se não reparou o carro
	 */
/*	public boolean goFixIt (int carId) { 
		carId = customer.getCustomerCarId();                 // Passando a identificação do carro pelo cliente
		int partId;											 // Identificação da peça
		int mechanicId = -1;                                 // identificação do mecânico
		boolean newMechanic = false;                         // sinalização de lançamento de um thread mecânico

		synchronized (this) {                                // entrada no monitor área de reparo
			stateCustomerCar[carId] = WAITREPAIRPARK;		 // Sinaliza que tem um carro em espera para reparo
			reportStatus ();

			if (queueCar.full ())                            // verifica se a fila de carros está cheia
				return (false);                              // em caso afirmativo, não entra
	
			if (nMecOnFixIt < nMechanic) {                   // verifica se tem algum mecânico livre
				newMechanic = true;                          // sinaliza a necessidade de acorda um mecânico
			}
			else {
				stateCustomerCar[carId] = WAITTURN;          // Carro fica na espera de um mecânico
				reportStatus ();
				queueCar.write (Thread.currentThread ());    // Continua a esperar
			}
		}

		synchronized (Thread.currentThread ()) {               					// Entrada no monitor mecânico
			if (newMechanic)                                             		// Verifica se há necessidade de acordar um mecânico
				new Mechanic (mechanicId, this).start ();     					// Acorda um mecânico
				stateMechanic[mechanicId] = WORKING;         					// Mecânico está trabalhando
				reportStatus ();
 			    
				Random random = new Random();
				partId = random.nextInt(3); 									// Gera a peça a ser reparado, 

				getRequeridPart(partId);										// Pede a peça para reparar o carro (recebe a peça pelo stock)
				stateMechanic[mechanicId] = REQUERIDPART;         				// Mecânico vai buscar uma peça no stock
				reportStatus ();
				if(partAvailable(partId) == true) {								// Se tiver a peça no stock entra na condição
					resumeRepairProcedure(carId, partId);						// Retorna ao reparo do carro
					stateMechanic[mechanicId] = WORKING;         				// Mecânico está trabalhando
					reportStatus ();					
				}else {					
					lounge.letManagerKnow(mechanicId, carId, partId);								// Fala com o gerente para solicitar a peça
					stateMechanic[mechanicId] = REQUERINEWPART;         		// Mecânico está alertando o gerente que precisa de uma peça
					reportStatus ();

				}
			try {
				Thread.currentThread ().wait ();                 				// Aguarda a continuação das operações
			}catch (InterruptedException e) {}
		}
		
		return (true);                                       					// Retorna que o reparo está pronto
	}
*/
	
	public void resumeRepairProcedure(int partId) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Realiza a conversa com o stock para pedir uma peça
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
	 * Método do stock, aqui conta as peças disponíveis no stock.
	 * 
	 * @return
	 */
	public static int[] stock() {
		/**
		 * Peças do stock 
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
	 * Método que valida se existe peça em stock
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
	 * Atualiza o stock de peças recebidas pelo fornecedor
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
	 *    @return <li> true, se tem um novo serviço 
	 *            <li> false, se não tiver um novo serviço	 
	 */
	public synchronized boolean readThePaper(int mechanicId) {

		  int carId = -1;                                 
	      Customer carCustomer;                                   		// cliente
	      
	      if (queueCar.empty ()) {                            			// Verifica se existe algum carro na fila para reparo
	           stateMechanic[mechanicId] = SLEEPING;               		// Sinaliza que vai colocar o mecânico para dormir
				reportStatus ();
	         }
	         else { 
	        	 carCustomer = (Customer) queueCar.read ();            // 	        	 
	        	 carId = customer.getCustomerCarId();
	             stateMechanic[mechanicId] = GONEWFIXIT;        	   // Sinaliza que o mecânico vai começa um novo reparo
 				 reportStatus ();
	         }
	      reportStatus ();

	      return true;
	}

	/**
	 * Método que verifica se o carro está reparado e se caso esteja,
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
