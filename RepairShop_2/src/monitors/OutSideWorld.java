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
     * Número de clientes
     * @serialField nCustomer
     */
    private int nCustomer = 0;
    
    /**
     * Número de visitas do cliente
     * 
     * @serialField nCustomer
     */
    private int nCustomerVisits = 1;
    
    /**
     * Identificação carro de substituição
     * @serialField replaceCarId
     */
    private int replaceCarId = 0;
    
       /**
     * Identificação do carro
     *
     * @serialField carId
     */
    private int customerCarId = 0;
    
    /**
     * Identificação do cliente
     *
     * @serialField customerId
     */
    private int customerId = 0;

	/**
	 * Identificação do cliente
	 * 
	 * @serialField customer
	 */
	private Customer customer;

	/**
	 * Identificação do cliente
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
     * Operação interna
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
		int[] customerWithReplaceCar;
		
		if(nCustomerVisits == 1) {
			customerWithReplaceCar = park.findCar(customerId, carId);
			customer.getCustomerWithReplaceCar().write(customerWithReplaceCar);
			//Vai ficar bloqueado até receber o chamado do gerente para voltar e buscar o seu carro reparado
		}else {
			//Fazer o tratamento para o retorno com o carro reparado
		}
	}
	
	/**
	 * Cliente retorna ao mundo de ônibus sem um carro reserva
	 */
	public synchronized void backToWorkByBus(int customerId) {
            //TODO Analisar esse método
		if(nCustomerVisits == 1) {
                    //Adiciona o cliente sem o carro de substituição na fila
                    //Vai ficar bloqueado até receber o chamado do gerente para voltar e buscar o seu carro reparado
                    customer.getCustomerWithoutReplaceCar().write(customerId);
		}
	}

}
