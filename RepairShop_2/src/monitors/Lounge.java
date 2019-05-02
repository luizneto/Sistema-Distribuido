package monitors;

import java.util.Random;

import mainProgram.Customer;
import monitors.GenericRepository;

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
    * Mundo
    */
   private OutSideWorld outWorld;
      
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
   /*private Lounge(int[] queueCustomerWaitingAttending, int[] queueMechanicWaitingManager) {
	super();
	if(nCarsRepaired > 0) this.nCarsRepaired = nCarsRepaired;
	if(nCustomer > 0)this.nCustomer = nCustomer;
	if(nIterMechanic > 0)this.nIterMechanic = nIterMechanic;
	if(nIterGeral > 0) this.nIterGeral = nIterGeral;

}*/
   GenericRepository gRepo = new GenericRepository();
   
   public void Lounge(){
       
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

        //TODO refatorar esse método
	public int getNextTask() {
            int task = -1;
            //queueCustomerWaitingAttending.write(customer.getCustomerId());//TODO refatorar esse código (linha adicionada para forçar um cliente na fila de atendimento)
            if(!queueCustomerWaitingAttending.empty()){
                int clientId = (int)queueCustomerWaitingAttending.read();
                setCustomerId(clientId);
                return clientId;
            }
            if(!queueCustomerWaitingReplaceCar.empty()){
                task = (int)queueCustomerWaitingReplaceCar.read();
                GenericRepository.WtK = gRepo.getWtK() - 1;
                return task;
            }
            if(!queueMechanicWaitingManager.empty()){
                task = (int)queueMechanicWaitingManager.read();
                gRepo.setSt(task, 0);
                return task;
            }
            else return task;
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
	   char tarefa = '0';
	   
           /*Gerente atende fila de clientes enquanto existirem clientes na fila */
	   if(!queueCustomerWaitingAttending.empty()) {
		   tarefa = 'c';
	   }else {
		/*Gerente atende fila de mecanicos enquanto existirem mecanicos na fila */   
               if(!queueMechanicWaitingManager.empty()) {
			   tarefa = 'm';
		   }
	   }
	   return tarefa;
   }

   /**
    * Método que realiza a conversa do gerente com o cliente.
    * Recebe como parâmetro o cliente e retorna o estado que representa o resultado da conversa.
    * O estado será o que o gerente precisará fazer (fornecer ou não um carro substituto, receber o pagamento do reparo).
    * @param customerId
    * @return estado
    */
   public synchronized char talkToCustomer(int customerId) {
	   char estado = '0';
	   int carReserve;
           customer.setWishReplaceCar(false);
	   Random random = new Random();
           // Cliente decide se quer ou não um carro reserva, e se for 1 quer carro reserva, se for 0 não quer carro reserva
	   carReserve = random.nextInt(2);
	   if(nTimesCustomerVisitRepair < 1) {
		   if(carReserve == 1) {
                            // Cliente deseja um carro reserva
			   customer.setWishReplaceCar(true);
			   estado = 'c';
		   }else {
                            // Cliente não quer carro reserva
			   estado = 's';
		   }
	   }else {
		/* Indica que o cliente está na recepção para pagar pelo reparo.*/
               estado = 'r';
	   }
	   return estado;
   }

   /**
    * O gerente vai buscar a chave para ser entregue ao cliente
    * TODO verificar se precisamos desse método?
    */
   public void handCarKey() {
	
   }

   /**
    * O gerente recebe do cliente o pagamento pelo serviço realizado
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
   public void phoneCustomer(int customerId, int carId) {
       park.setPlaceCar(carId);
       outWorld.decideOnRepair(customerId);
   }

   /**
    * Cliente entra na fila de espera para ser atendido pelo gerente
    * 
    * @param customerId 
    */
   
   public synchronized void queueIn(int customerId) {
	   if(!queueCustomerWaitingAttending.full()) {
		   queueCustomerWaitingAttending.write(customerId);
                   GenericRepository.InQ = gRepo.getInQ() + 1;
                   gRepo.reportStatus();
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
     * @param customerId
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
                           GenericRepository.WtK = gRepo.getWtK()+1;
                           gRepo.reportStatus();
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
                           GenericRepository.WtK = gRepo.getWtK()+1;
                           gRepo.reportStatus();
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
   
   /**
    * TODO rever esse método (ele é necessário realmente?)
    */
   public void replaceCarArrive() {
	   
	   
   }
   
	/**
	 * O mecânico vai até o gerente e faz o pedido da peça que está faltando para reparar o carro
	 * 
	 * @param mechanicId
	 * @param partId
	 */
	public void letManagerKnow(int mechanicId, int partId) {
                //Mecânico entra na fila de espera para atendimento com o gerente
		queueMechanicWaitingManager.write(partId);
                gRepo.setSt(mechanicId, 1);
                gRepo.reportStatus();
	}
        
        /**
         * O mecânico vai até o gerente e informa o carro que foi reparado.
         * @param mechanicId
         * @param carId 
         */
        public void alertingManager(int mechanicId, int carId){
            queueMechanicWaitingManager.write(carId);
            
        }
}
