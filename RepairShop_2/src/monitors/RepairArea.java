package monitors;

import java.util.Random;

import genclass.GenericIO;
import genclass.TextFile;
import java.util.concurrent.ThreadLocalRandom;
import mainProgram.Customer;
import mainProgram.Mechanic;
import monitors.GenericRepository;
import monitors.Supplier;

public class RepairArea {

    /**
     * Número de mecâncios na área de reparo
     *
     * @serialField nMechanic
     */
    private int nMechanic = 0;

    /**
     * Número de mecânicos ativos em um reparo
     *
     * @serialField nMecOnFixIt
     */
    private int nMecOnFixIt = 0;

    /**
     * Número de carros na área de reparo
     *
     * @serialField nCar
     */
    private int nCar = 0;

    /**
     * Estado do reparo do carro
     *
     * @serialField statRepair
     */
    private int[] stateRepair;

    /**
     * Estado presente dos mecânicos
     *
     * @serialField stateMechanic
     */
    private int[] stateMechanic;

    /**
     * Estado de ocupação do número de carros
     *
     * @serialField stateCustomerCar
     */
    private int[] stateCustomerCar;

    /**
     * Carro do cliente a ser reparado
     *
     * @serialField stateCar
     */
    private Customer customerCar;

    /**
     * Fila de espera (carros) aguardando o reparo
     *
     * @serialField queueJob
     */
    private MemFIFO queueJob;

    /**
     * Fila de carros aguardando peça de substituição
     *
     * @serialField queueCarRepaired
     */
    private MemFIFO queueWaitCarPart;

    /**
     * Fila de carros já reparados
     *
     * @serialField queueCarRepaired
     */
    private MemFIFO queueCarRepaired;

    /**
     * Número de iteraçõess do ciclo de vida dos mecânicos
     *
     * @serialField nIter
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
     * Repositório genérico
     */
    private GenericRepository gRepo;
    

    private Supplier supplier;

    /**
     * Instanciação do construtor da Área de reparo
     *
     * @param nMechanic
     * @param nMecOnFixIt
     * @param nCar
     * @param stateMechanic
     * @param nIter
     */
    public RepairArea() {

        gRepo = new GenericRepository();
        supplier = new Supplier();
    }

    public MemFIFO getQueueWaitCarPart() {
        return queueWaitCarPart;
    }

    public void setQueueWaitCarPart(MemFIFO queueWaitCarPart) {
        this.queueWaitCarPart = queueWaitCarPart;
    }

    /**
     * Obtém a peça informada no parâmetro, do fornecedor.
     * Se não houver a peça em estoque, retorna 0
     * @param partId
     * @return
     */
    public int getRequiredPart(int partId) {
        return supplier.partAvailable(partId);
    }

    public MemFIFO getQueueCarRepaired() {
        return queueCarRepaired;
    }

    public void setQueueCarRepaired(MemFIFO queueCarRepaired) {
        this.queueCarRepaired = queueCarRepaired;
    }
    
    


    /**
     *
     * @param mechanicId
     *
     * @return <li> true, se tem um novo serviço
     * <li> false, se não tiver um novo serviço
     */
    public synchronized boolean readThePaper(int mechanicId) {

        int carId = -1;
        Customer carCustomer;

        // Verifica se existe algum carro na fila para reparo
        if (queueJob.empty()) {
            // Estado do Mecanico = WAITING_FOR_JOB = 0
            stateMechanic[mechanicId] = 0;
            //* Registra em log
            gRepo.reportStatus();
        } else {
            carCustomer = (Customer) queueJob.read();
            carId = customer.getCustomerCarId();
            // Estado do Mecanico = FIXING_CAR = 1
            stateMechanic[mechanicId] = 1;
            gRepo.reportStatus();
        }
        gRepo.reportStatus();
        return true;
    }

    /**
     * Método que verifica se o carro está reparado e se caso esteja, adiciona o
     * carro reparado em uma fila de carros reparados
     *
     * @param carId
     */
    public void repairConcluded(int carId) {
        if (stateRepair[carId] == 1) {
            queueCarRepaired.write(carId);
        }
    }

    public int startRepairProcedure() {
        return ThreadLocalRandom.current().nextInt(4);
    }

    public void registerService(int carId) {
        queueJob.write(carId);
    }

    public int resumeRepairProcedure(int partId) {
        return partId;
    }

}
