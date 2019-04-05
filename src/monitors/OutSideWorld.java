/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monitors;

import mainProgram.Customer;

/**
 *
 * @author clonyjr
 */
public class OutSideWorld {
    
    /**
     * N�mero de clientes
     * @serialField nCustomer
     */
    private int nCustomer = 0;
    
    /**
     * N�mero de visitas do cliente
     * 
     * @serialField nCustomer
     */
    private int nCustomerVisits = 1;
    
    /**
     * Identifica��o carro de substitui��o
     * @serialField replaceCarId
     */
    private int replaceCarId = 0;
    
       /**
     * Identifica��o do carro
     *
     * @serialField carId
     */
    private int customerCarId = 0;
    
    /**
     * Identifica��o do cliente
     *
     * @serialField customerId
     */
    private int customerId = 0;

	/**
	 * Identifica��o do cliente
	 * 
	 * @serialField customer
	 */
	private Customer customer;

	/**
	 * Identifica��o do cliente
	 * 
	 * @serialField customer
	 */
	private Park park;
		
    public int getnCustomerVisits() {
		return nCustomerVisits;
	}

	public void setnCustomerVisits(int nCustomerVisits) {
		this.nCustomerVisits = nCustomerVisits;
	}

	/**
     * Opera��o interna
     * @param customerId
     * @return 
     */
    public boolean decideOnRepair(int customerId){
          return true;
    }
    
    /**
     * Cliente retorna ao mundo com o carro reserva
     */
	public synchronized void backToWorkByCar(int customerId, int carId) {
		int[] customerAndCar;
		
		if(nCustomerVisits == 1) {
			customerAndCar = park.findCar(customerId, carId);
			customer.getCustomerAndReplaceCar().write(customerAndCar); 					//Adiciona o cliente com o carro de substitui��o na fila
			//Vai ficar bloqueado at� receber o chamado do gerente para voltar e buscar o seu carro reparado
		}else {
			//Fazer o tratamento para o retorno com o carro reparado
		}
	}
	
	/**
	 * Cliente retorna ao mundo de �nibus sem um carro reserva
	 */
	public synchronized void backToWorkByBus(int customerId) {
		if(nCustomerVisits == 1) {
			customer.getCustomerNotReplaceCar().write(customerId); 					//Adiciona o cliente sem o carro de substitui��o na fila
			//Vai ficar bloqueado at� receber o chamado do gerente para voltar e buscar o seu carro reparado
		}
		
	}

}
