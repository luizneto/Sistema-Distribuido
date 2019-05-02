/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainProgram;

import monitors.GenericRepository;
import monitors.Lounge;
import monitors.Park;
import monitors.RepairArea;

/**
 *
 * @author clonyjr
 */
public class Mechanic extends Thread{
    
     /**
     * Identificação do mecanico
     *
     * @serialField mechanicId
     */
    private int mechanicId;
       
    /**
     * Área de Reparo
     * rArea
     */
    private RepairArea rArea;
    
    /**
     * Lounge
     */
    private Lounge lounge;

    /**
     * Park
     */
    private Park park;
    
    GenericRepository gRepo = new GenericRepository();
            
            
    /**
     * 
     * @param mechanicId
     * @param carId 
     */

    public Mechanic(int mechanicId, RepairArea repairArea) {
        this.mechanicId = mechanicId;
        this.rArea = repairArea;
    }
    
    
   /**
   *  Concertando um carro (operação interna).
   */

   private void fixIt ()
   {
      try
      { sleep ((long) (1 + 100 * Math.random ()));
      }
      catch (InterruptedException e) {}
   }
   
   public void run(){
	   int carId;
	   int partId;
       //Mecânico vai ler o papel e verificar se tem um novo serviço
       while(rArea.readThePaper(mechanicId)) {
           //*Mecânico recebe o id do carro que ira reparar
           carId = rArea.startRepairProcedure();
           //*Obtém recebe a peça a ser substituida no carro
           partId = rArea.startRepairProcedure();
                //*Retira o carrodo estacionamento para realizar o reparo
               park.getVehicle(carId);		
               //Verifica se tem a peça no estoque
               if(rArea.getRequiredPart(partId)>0){
                   //* existe a peça e o reparo continua
                    rArea.resumeRepairProcedure(partId);
                    fixIt();
                    //* Retorna o veículo para o estacionamento
                    park.returnVehicle(carId, mechanicId);
                    //**Alerta o gerente que o reparo está concluído
                    rArea.repairConcluded(carId);
                    GenericRepository.NRV = gRepo.getNRV() + 1;
                    gRepo.reportStatus();
                }
               //* Não há peça disponível para reparo.
                else {
                    //Adiciona o carro na fila de espera de peça
                    rArea.getQueueWaitCarPart().write(carId);
                    //Entra na fila de espera para atendimento com o gerente            
                    lounge.letManagerKnow(mechanicId, partId);
                    switch(partId){
                        case 0:
                            GenericRepository.NV0 = gRepo.getNV0() + 1;
                            break;
                        case 1:
                            GenericRepository.NV1 = gRepo.getNV1() + 1;
                            break;
                        case 2:
                            GenericRepository.NV2 = gRepo.getNV2() + 1;
                            break;
                        default: System.out.println("OPÇÃO INVÁLIDA!");
                    }
                    gRepo.reportStatus();
                }
       }
   }

    /**
     * @return the mechanicId
     */
    public int getMechanicId() {
        return mechanicId;
    }

    /**
     * @param mechanicId the mechanicId to set
     */
    public void setMechanicId(int mechanicId) {
        this.mechanicId = mechanicId;
    }

    /**
     * @return the rArea
     */
    public RepairArea getrArea() {
        return rArea;
    }

    /**
     * @param rArea the rArea to set
     */
    public void setrArea(RepairArea rArea) {
        this.rArea = rArea;
    }
    
    public void startRepairProcedure(){
        
    }
    
    public void repairConcluded(){
    }
    
    public void fixingTheCar(){
    }
    
    public void alertingManager(int carId){
        
    }
    

}