/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainProgram;

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
       while(rArea.readThePaper(mechanicId)) {							//Mecânico vai ler o papel e verificar se tem um novo serviço
           carId = rArea.startRepairProcedure();						//*Mecânico recebe o id do carro que ira reparar	
           partId = rArea.startRepairProcedure();						//*Mecânico recebe a peça a ser substituida no carro
               park.getVehicle(carId);									//*Mecânico vai ao park buscar o carro para realizar o reparo
               if(rArea.partAvailable(partId)){							//Verifica se tem a peça no stock
                   rArea.getRequiredPart(partId);						//Mecânico vai pega a peça
                    rArea.resumeRepairProcedure(partId);				//*Mecânico volta com a peça e continua o reparo
                    fixIt();											//Mecânico repara o carro
                    park.returnVehicle(carId, mechanicId);				//Mecânico retorna o carro já reparado ao park
                    rArea.repairConcluded(carId);						//**Mecânico alerta o gerente que o reparo está concluído
                }
                else lounge.letManagerKnow(mechanicId, partId);			//Mecânico entra na fila de espera para atendimento com o gerente            
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
    

}