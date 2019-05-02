package monitors;

import java.util.ArrayList;
import java.util.List;
import mainProgram.Customer;

public class Park {

    /**
     * Identificação do carro do cliente
     *
     * @serialField customerCarsId
     */
    private int customerCarId;

    /**
     * Identificação do carro de substituição
     *
     * @serialField replaceCarId
     */
    private int[] replaceCarId;

    /**
     * Identificação da fila de vagas de carros
     *
     * @serialField parkAddressCar
     */
    private int[] parkAddressCar = new int[30];
    
    private static int parkSpot = 0;

    private boolean existsReplaceCar;

    /**
     * Identificação do cliente
     *
     * @serialField customer
     */
    private Customer customer;
    
    GenericRepository gRepo = new GenericRepository();

    public boolean isExistsReplaceCar() {
        return existsReplaceCar;
    }

    public Park() {
        //this.customerCarId = customerCarId;
    }

    public int getCustomerCarId() {
        return customerCarId;
    }

    public void setCustomerCarId(int customerCarId) {
        this.customerCarId = customerCarId;
    }

    /**
     * Percorre a fila de replaced cars movendo as chaves dos carros pelo
     * vetor,até que não haja mais carros na fila.
     *
     * @return keyCardReplace as result
     */
    public int getReplaceCarId() {
        int result = -1;

        switch (replaceCarId.length) {

            case 3: {
                result = replaceCarId[0];
                replaceCarId[0] = replaceCarId[1];
                replaceCarId[1] = replaceCarId[2];
                return result;
            }
            case 2: {
                result = replaceCarId[0];
                replaceCarId[0] = replaceCarId[1];
                return result;
            }
            case 1: {
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

    public int getParkSpot() {
        return parkSpot;
    }

    /**
     * Estaciona um carro em uma vaga do estacionamento (Park)
     * @param carId 
     */
    public void setPlaceCar(int carId) {
        if (parkSpot < 30) {
            parkAddressCar[parkSpot] = carId;
            parkSpot -= 1;
        }
    }

    /**
     * Cliente vai ao park deixar seu carro
     *
     * @param carId
     *
     */
    public void goToRepairShop(int carId) {
        //Cliente estáciona o carro em uma vaga livre
        System.out.println("Estou em goToRepairShop o valor de carId: " + carId);
        GenericRepository.NCV = gRepo.getNCV() + 1;
        gRepo.reportStatus();
        setPlaceCar(carId);
    }

    /**
     * Cliente vai buscar o carro reserva
     *
     * @param key
     * @
     */
    public int[] findCar(int key, int customerId) {
        // TODO gravar esse estado e associar cliente ao carro(?)

        int[] customerAndCar = new int[2];

        customerAndCar[0] = customerId;
        if (key == getReplaceCarId()) {
            customerAndCar[1] = getReplaceCarId();
        }

        customer.getCustomerWithReplaceCar().write(customerAndCar); 	//Adiciona o clinte com o carro de substituição na fila
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
     *
     * @param carId
     * @param keyCar
     * @param mechanicId
     */
    public void returnVehicle(int carId, int mechanicId) {
        // O carro reparado é levado para o park
        setPlaceCar(carId);
        
        
        /* Trecho de código removido pois não há previsão de um estado nomeado TAKECARPARK ou de monitorar esse momento do mecânico*/
        // Estado do mecânico levar o carro reparado para o park //stateMechanic[mechanicId] = TAKECARPARK;         			
        //reportStatus ();
    }

    public void getVehicle(int carId) {
        // TODO Auto-generated method stub

    }

}
