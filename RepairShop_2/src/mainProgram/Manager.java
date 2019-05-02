package mainProgram;

import monitors.Lounge;
import monitors.Park;
import monitors.RepairArea;
import monitors.Supplier;
import monitors.GenericRepository;

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
    
    GenericRepository gRepo = new GenericRepository();

    /**
     * Ciclo de vida da thread do manager
     *
     */
    @Override
    public void run() {
        int part;
        //Verifica se tem alguma tarefa a ser realizada
        lounge = new Lounge();
        while (lounge.getNextTask() != -1) {
            //Verifica qual o tipo de tarefa a ser realizada
            switch (lounge.appraiseSit()) {
                //Tarefa de atendimento do cliente
                case 'c':
                    //Gerente conversa com o cliente
                    int customerId = lounge.getCustomerId();
                    int carId = lounge.getCarId();
                    switch (lounge.talkToCustomer(customerId)) {

                        //*Cliente não requisitou carro substituto
                        case 's':
                            //*Registra um novo serviço
                            repairArea.registerService(carId);
                            GenericRepository.NSRQ = gRepo.getNSRQ() + 1;
                            break;
                        //*Cliente requisitou carro substituto
                        case 'c':
                            //*Pega a chave do carro substituto para o cliente
                            if(lounge.collectKey(customerId) > 0) GenericRepository.NPV = gRepo.getNPV() - 1;
                            //*Registra um novo serviço
                            repairArea.registerService(carId);
                            GenericRepository.NSRQ = gRepo.getNSRQ() + 1;
                            gRepo.reportStatus();
                            break;
                        //Atendimento do cliente que retornou para pagar pelo serviço do reparo
                        case 'r':
                            //Gerente rebece o pagamento pelo reparo do carro do cliente
                            lounge.receivePayment(customerId);
                            break;
                        default:
                            System.out.println("OPÇÃO INVÁLIDA!");
                    }
                case 'm'://Tarefa de atendimento do mecânico
                    //*Vai ao fornecedor comprar uma peça
                    supplier.gettingNewParts();
                    //* Repõe peças em estoque
                    supplier.replenishStock((int) lounge.getQueueMechanicWaitingManager().read());
                    int[] replacePart = supplier.getPart();
                    GenericRepository.Prt0 = replacePart[0];
                    GenericRepository.Prt1 = replacePart[1];
                    GenericRepository.Prt2 = replacePart[2];
                    int repairedCarId = (int) repairArea.getQueueCarRepaired().read();
                    int clientId = repairedCarId;
                    GenericRepository.NRV = gRepo.getNRV()+ 1;
                    gRepo.reportStatus();
                    /* Gerente alerta o cliente que seu carro está pronto */
                    lounge.phoneCustomer(clientId, repairedCarId);
                    break;
                default:
                    System.out.println("OPÇÃO INVÁLIDA!");
            }

        }
    }

}
