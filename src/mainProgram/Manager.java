package mainProgram;

import monitors.Lounge;
import monitors.Park;
import monitors.RepairArea;
import monitors.Supplier;

public class Manager extends Thread {

	/**
	 * Identificação do manager
	 * 
	 * @serialField
	 */

	/**
	 * Identificação do park
	 * 
	 * @serialField
	 */
	private Park park;
	
	/**
	 * Identificação do lounge
	 * 
	 * @serialField
	 */
	private Lounge lounge;
	
	/**
	 * Identificação da área de reparo
	 * 
	 * @serialField
	 */
	private RepairArea repairArea;
	
	/**
	 * Identificação do fornecedor
	 * 
	 * @serialField
	 */
	private Supplier supplier;

	/**
	 * Ciclo de vida da thread do manager
	 * 
	 */
	@Override
	public void run() {
		int part;
		while(lounge.getNextTask() != -1) { 									//Verifica se tem alguma tarefa a ser realizada
			switch(lounge.appraiseSit()) {										//Verifica qual o tipo de tarefa a ser realizada
				case 'c': 														//Tarefa de atendimento do cliente
					switch(lounge.talkToCustomer(lounge.getCustomerId())) {		//Gerente conversa com o cliente
						case 's':												//Atendimento do cliente que não requereu carro substituto
							repairArea.registerService(lounge.getCarId());		//*Registra um novo serviço
							break;
						case 'c': 												//Atendimento do cliente que requereu carro substituto
							lounge.handCarKey(); 								//*Pega a chave do carro substituto para o cliente
							repairArea.registerService(lounge.getCarId()); 		//*Registra um novo serviço
							break;
						case 'r': 												//Atendimento do cliente que retornou para pagar pelo serviço do reparo
							lounge.receivePayment(lounge.getCustomerId());		//Gerente rebece o pagamento pelo reparo do carro do cliente
							break;	
					}
				case 'm': 														//Tarefa de atendimento do mecânico
					part = supplier.goToSupplier();								//*Vai ao fornecedor comprar uma peça
					repairArea.storePart(part);									//Atualiza o stock de peças da area de reparo
				case 'a': 														//Gerente alerta o cliente que seu carro está pronto
					lounge.phoneCustomer(lounge.getCustomerId()); 				//*Gerente chama o cliente para vim buscar seu carro reparado
					
			}
		
		}	
	}

    
}
