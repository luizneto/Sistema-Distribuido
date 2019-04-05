package monitors;

import mainProgram.Customer;

public class Park {

   /**
    *  Identificação do carro do cliente
    *
    *    @serialField customerCarsId
    */
    private int[] customerCarId;
   
   /**
    *  Identificação do carro de substituição
    *
    *    @serialField replaceCarId
    */
    private int[] replaceCarId;
    
    /**
     * Identificação da fila de vagas de carros
     * 
     * @serialField placeCar
     */
	private Integer[] placeCar;	
	
	private boolean existsReplaceCar;
	
	/**
	 * Identificação do cliente
	 * 
	 * @serialField customer
	 */
	private Customer customer;
	
	public boolean isExistsReplaceCar() {
		return existsReplaceCar;
	}

	private Park(int[] customerCarId) {
		super();
		this.customerCarId = customerCarId;
	}

	public int[] getCustomerCarId() {
		return customerCarId;
	}

	public void setCustomerCarId(int[] customerCarId) {
		this.customerCarId = customerCarId;
	}

	/**
	 * Percorre a fila de replaced cars movendo as chaves dos carros pelo vetor,até que não haja mais carros na fila.
	 * @return keyCardReplace as result
	 */
	public int getReplaceCarId() {
		int result = -1;
		
		switch(replaceCarId.length) {
		
		case 3:{
			 result = replaceCarId[0];
				replaceCarId[0] = replaceCarId[1];
				replaceCarId[1] = replaceCarId[2];
				return result;
			}
		case 2:{
			 result = replaceCarId[0];
				replaceCarId[0] = replaceCarId[1];
				return result;
			}
		case 1:{
			 result = replaceCarId[0];
				return result;
			}
		default:
			return result;
			
		}
	}

	public void setReplaceCarId(int[] replaceCarId) {
		this.replaceCarId = replaceCarId;
		existsReplaceCar = true;
	}

	public Integer[] getPlaceCar() {
		return placeCar;
	}

	public void setPlaceCar(Integer[] placeCar) {
		this.placeCar = placeCar;
	}

	/**
	 * Coloca o carro em uma vaga no estacionamento
	 * 
	 * @param carId
	 */
	public void putPlaceCar(int carId) {
		for (int i = 0; i < this.placeCar.length; i++) {
			if(this.placeCar[i] == null) {
				this.placeCar[i] = carId;
			}
		}

	}

	/**
	 * Cliente vai ao park deixar seu carro
	 * @param carId 
	 * 
	 */
	public void goToRepairShop(int carId) {
		putPlaceCar(carId);							//Cliente estáciona o carro em uma vaga livre
	}

	/**
	 * Cliente vai buscar o carro reserva
	 * @param key
	 * @
	 */
	public int[] findCar(int key, int customerId) {
		// TODO gravar esse estado e associar cliente ao carro(?)

		int[] customerAndCar = new int[2];
		
		customerAndCar[0] = customerId;
		if(key == getReplaceCarId())
			customerAndCar[1] = getReplaceCarId();

		customer.getCustomerAndReplaceCar().write(customerAndCar); 					//Adiciona o clinte com o carro de substituição na fila
		
		return customerAndCar;
		
	}

	/**
	 * Cliente coleta seu carro apois o reparo
	 */
	public void collectCar(int customerId) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Retorna o carro para o park e a chave do carro para o lounge
	 * @param carId
	 * @param keyCar
	 * @param mechanicId
	 */
	public void returnVehicle(int carId, int mechanicId) {	
		putPlaceCar(carId);									// O carro reparado é levado para o park
		//stateMechanic[mechanicId] = TAKECARPARK;         			// Estado do mecânico levar o carro reparado para o park
		//reportStatus ();
	}

	public void getVehicle(int carId) {
		// TODO Auto-generated method stub
		
	}

	
}
