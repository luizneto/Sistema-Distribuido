/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainProgram;

/*Porta servidor grupo 7: 20170 à 20169*/


import monitors.RepairArea;
import monitors.OutSideWorld;
import monitors.Park;
import monitors.Lounge;
import monitors.MemFIFO;

/**
 *
 * @author clonyjr
 * @author luizn
 */
public class Customer extends Thread {

    /**
     * Identificação do cliente
     *
     * @serialField customerId
     */
    private int customerId;

    /**
     * Identificação do estado do cliente
     *
     * @serialField customerId
     */
    private int[] status;

    /**
     * Número de iterações do ciclo de vida do cliente
     *
     * @serialField nIter
     */
    private int nIter = 0;

    /**
     * Identificação do carro
     *
     * @serialField carId
     */
    private int customerCarId = 0;
    
    /**
     * Identificação se o cliente quer um carro de substituição
     */
    private boolean wishReplaceCar;
    
	/**
	 * Idendificação da fila de clientes com um carro de substituição
	 * 
	 * @serialField sustomerAndReplaceCar
	 */
	private MemFIFO customerAndReplaceCar;

	/**
	 * Idendificação da fila de clientes sem um carro de substituição
	 * 
	 * @serialField sustomerBotReplaceCar
	 */
	private MemFIFO customerNotReplaceCar;
    
    /**
     * Lounge
     */
    private Lounge lounge;
    
    /**
     * Mundo
     */
    private OutSideWorld oSWorld;

    /**
     * Área de reparo
     */
    private RepairArea rArea;
    
    /**
     * Identificação do park
     * 
     */
    private Park park;

    /**
     * Instanciação do thread cliente.
     *
     * @param customerId identificação do cliente
     * @param lounge Lounge
     * @param nIter número de iterações do ciclo de vida do cliente
     * @param customerCarId identificação do carro
     * @param oSWorld mundo
     * @param rArea Área de reparo
     */
    public Customer(int customerId, int nIter, int customerCarId, RepairArea rArea) {
        this.customerId = customerId;
        this.customerCarId = customerCarId;
        this.rArea = rArea;
        if (nIter > 0) {
            this.nIter = nIter;
        }
    }
    
    public int getCustomerId() {
		return customerId;
	}

	public int getCustomerCarId() {
		return customerCarId;
	}
		
	public boolean getWishReplaceCar() {
		return wishReplaceCar;
	}

	public void setWishReplaceCar(boolean wishReplaceCar) {
		this.wishReplaceCar = wishReplaceCar;
	}

	public MemFIFO getCustomerAndReplaceCar() {
		return customerAndReplaceCar;
	}

	public void setCustomerAndReplaceCar(MemFIFO customerAndReplaceCar) {
		this.customerAndReplaceCar = customerAndReplaceCar;
	}

	public MemFIFO getCustomerNotReplaceCar() {
		return customerNotReplaceCar;
	}

	public void setCustomerNotReplaceCar(MemFIFO customerNotReplaceCar) {
		this.customerNotReplaceCar = customerNotReplaceCar;
	}

	public int[] getStatus() {
		return status;
	}

	public void setStatus(int[] status) {
		this.status = status;
	}

	/**
     * Ciclo de vida do thread cliente.
     */
    @Override
    public void run() {
    	int key;
    	
    	decideOnRepair(customerId);										//Cliente decide ir reparar o seu carro
    	park.goToRepairShop(customerCarId);								//Cliente estaciona o carro no park					
    	lounge.queueIn(customerId);										//Cliente entra na fila de espera para atendimento com o gerente
    	lounge.talkWithManager(customerId);								//Cliente conversa com o gerente
    	if(wishReplaceCar) {											//Cliente decide se quer um carro reserva
    		key = lounge.collectKey(customerId);						//Se não tiver carro substituto disponível, fica bloqueado até chegar novo carro substituto
    		park.findCar(customerId, key);								//Cliente vai ao park já com a chave e busca o carro substituto
    		oSWorld.setnCustomerVisits(2);								//Número de visitas do cliente ao mundo
    		oSWorld.backToWorkByCar(customerId, customerCarId);			//Cliente retorna para o mundo com o carro substituto
    		oSWorld.setnCustomerVisits(2);								//Número de visitas do cliente ao mundo
    		park.goToRepairShop(customerCarId);							//Cliente volta a loja para buscar seu carro reparado
    		lounge.queueIn(customerCarId);								//Cliente entra na fila de espera para atendimento com o gerente
    		lounge.payForTheService();									//
    	}else {
    		oSWorld.backToWorkByBus(customerId);						//Cliente retorna ao mundo de ônibus
    		lounge.queueIn(customerCarId);								//Cliente entra na fila de espera para atendimento com o gerente
    		lounge.payForTheService();									//*Cliente realiza o pagamento pelo serviço
    	}		
   		park.collectCar(customerId);									//*Cliente coleta seu carro reparado
		oSWorld.backToWorkByCar(customerId, customerCarId);				//*Cliente retorna para o mundo com o carro substituto ou seu carro reparado
    }
    
	/**
     * Vivendo a vida normal (operação interna)
	 * @param customerId 
     */
    private void decideOnRepair(int customerId) {
        try {
            sleep((long) (1 + 40 * Math.random()));
        } catch (InterruptedException e) {
        }
    }
}
