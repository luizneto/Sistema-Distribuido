package monitors;

import java.util.Random;

import mainProgram.Customer;

public class Lounge {
	
    /**
     * Número de carros reparado - contador 
     */
    private int nCarsRepaired;
    
    /**
     * Número de iterações do cliente
     */
    private int nCustomer;

    /**
     * Número de iterações do mecanico
     */
    private int nIterMechanic;

    /**
     * Número de iterações Geral
     */
    private int nIterGeral;
    
   /**
   *  Fila de clientes aguardando atendimento
   *
   *    @serialField queueCustomerWaitingAttending
   */
   private MemFIFO queueCustomerWaitingAttending;

   /**
   *  Fila de mecânicos aguardando comunicar com gerente
   *
   *    @serialField queueMechanicWaitingManager
   */
   private MemFIFO queueMechanicWaitingManager;
   
   /**
   *  Número de vezes que o cliente foi à loja
   *
   *    @serialField nTimesCustomerVisitRepair
   */
   private int nTimesCustomerVisitRepair;
   
  /**
   *  Número de carros para substituição
   *
   *    @serialField nReplaceCars
   */
   private int nReplaceCars;

   /**
   *  Fila de clientes aguardando carro de substituição
   *
   *    @serialField queueCustomerWaitingReplaceCar
   */
   private MemFIFO queueCustomerWaitingReplaceCar;

   /**
    * Identificação do cliente
    * 
    * @serialField customer
    */
   private int customerId;
   
   /**
    * Identificação do customer
    * 
    * @serialField customer
    */
   private Customer customer;
   
   /**
    * Identificação do park
    * 
    * @serialField park
    */
   private Park park;
      
   /**
    *    
    * @param nCarsRepaired
    * @param nIterCustomer
    * @param nIterMechanic
    * @param nIterGeral
    * @param queueCustomerWaitingAttending
    * @param queueMechanicWaitingManager
    * @param nTimesCustomerVisitRepair
    * @param nReplaceCars
    * @param queueKey
    */
   private Lounge(int[] queueCustomerWaitingAttending, int[] queueMechanicWaitingManager) {
	super();
	if(nCarsRepaired > 0) this.nCarsRepaired = nCarsRepaired;
	if(nCustomer > 0)this.nCustomer = nCustomer;
	if(nIterMechanic > 0)this.nIterMechanic = nIterMechanic;
	if(nIterGeral > 0) this.nIterGeral = nIterGeral;

}

   public int getCustomerId() {
	   return customerId;
   }

   public void setCustomerId(int customerId) {
	   this.customerId = customerId;
   }

   
   public MemFIFO getQueueCustomerWaitingAttending() {
	   return queueCustomerWaitingAttending;
   }

   public void setQueueCustomerWaitingAttending(MemFIFO queueCustomerWaitingAttending) {
	   this.queueCustomerWaitingAttending = queueCustomerWaitingAttending;
   }

	public MemFIFO getQueueMechanicWaitingManager() {
		return queueMechanicWaitingManager;
	}
	
	public void setQueueMechanicWaitingManager(MemFIFO queueMechanicWaitingManager) {
		this.queueMechanicWaitingManager = queueMechanicWaitingManager;
	}

   public int getnCustomer() {
		return nCustomer;
	}

	public void setnCustomer(int nCustomer) {
		this.nCustomer = nCustomer;
	}

	public int getNextTask() {
	     /* inicializar os estado interno */
	   if(queueCustomerWaitingAttending.full()) {
		   
	   }

	   //recebe a lista de atendimentos, se será do cliente, mecânico ou fornecedor
	   return 0;
}

   public int getCarId() {
	   int car = 0;
	   car = customer.getCustomerCarId();
	   return car;
   }
   
   /**
    * Método que verifica quem será atendido pelo gerente
    * O gerente atende todos os clientes da fila e só depois atende os mecânicos
    * 
    * @return
    */
   public synchronized char appraiseSit() {
	   char tarefa = 0;
	   
	   if(!queueCustomerWaitingAttending.empty()) {
		   tarefa = 'c';
	   }else {
		   if(!queueMechanicWaitingManager.empty()) {
			   tarefa = 'm';
		   }
	   }
	   
	   return tarefa;
   }

   /**
    * Método que realiza a conversa com do gerente com o cliente
    * 
    * @param customerId
    * @return
    */
   public synchronized char talkToCustomer(int customerId) {
	   char estado = '0';
	   int carReserve;
	   Random random = new Random();
	   carReserve = random.nextInt(2); 							// Cliente decide se quer ou não um carro reserva, 
	   															// e se for 1 quer carro reserva, se for 0 não quer carro reserva
	   if(nTimesCustomerVisitRepair < 1) {
		   if(carReserve == 1) {								// Se cliente quer carro reserva
			   customer.setWishReplaceCar(true);				// Passa que o cliente quiz um carro reserva
			   estado = 'c';
		   }else {												// Se cliente não quer carro reserva
			   estado = 's';
		   }
	   }else {													// Se não for a primeira vez que o cliente vai ao lounge entra nesta condição
		   estado = 'a';
	   }
	   return estado;											// Retorna o estado da conversa com o cliente
   }

   /**
    * O gerente vai buscar a chave para ser entregue ao cliente
    * 
    */
   public void handCarKey() {
	
   }

   /**
    * O gerente recebe do cliente o pagamento pelo serviço realizado no carro do cliente
    * 
    * @param customerId
    */
   public void receivePayment(int customerId) {
	   
   }

   /**
    * O gerente liga para o cliente vim buscar o carro
    * 
    * @param customerId
    */
   public void phoneCustomer(int customerId) {
	
   }

   /**
    * Cliente entra na fila de espera para ser atendido pelo gerente
    * 
    * @param customerId 
    */
   
   public synchronized void queueIn(int customerId) {
	   if(!queueCustomerWaitingAttending.full()) {
		   queueCustomerWaitingAttending.write(customerId);
	   }else {
		   queueCustomerWaitingAttending.write (Thread.currentThread ());    // Continua a esperar
	   }
	   
   }

   /**
    * Cliente conversa com o gerente
    */
   public synchronized void talkWithManager(int customerId) {
	   talkToCustomer(customerId);
   }

   /**
    * Cliente coleta a chave do carro reserva
    * @return
    */
   public synchronized int collectKey(int customerId) {
	   int keyCarReplace = -1;
	   if(queueCustomerWaitingReplaceCar.empty()) {
		   if( (keyCarReplace = park.getReplaceCarId()) >= 0) {
			   return keyCarReplace;
		   }
		   else {
			   queueCustomerWaitingReplaceCar.write(customerId);
		   }
	   }
	   else {
		   if( (keyCarReplace = park.getReplaceCarId()) >= 0) {
			   queueCustomerWaitingReplaceCar.write(customerId);
			   customerId = (int) queueCustomerWaitingReplaceCar.read();
			   return keyCarReplace;
		   }
		   else {
			   queueCustomerWaitingReplaceCar.write(customerId);
		   }
	   }
	   return keyCarReplace;
   }


   /**
    * Cliente realiza o pagamento pelo serviço de reparo do seu carro
    */
   public void payForTheService() {
	// TODO Auto-generated method stub
	
   }
   
   public void replaceCarArrive() {
	   
	   
   }
   
	/**
	 * O mecânico vai até o gerente e faz o pedido da peça que está faltando para reparar o carro
	 * 
	 * @param mechanicId
	 * @param partId
	 */
	public void letManagerKnow(int mechanicId, int partId) {
		queueMechanicWaitingManager.write(partId);			//Mecânico entra na fila de espera para atendimento com o gerente
	}

   
}
