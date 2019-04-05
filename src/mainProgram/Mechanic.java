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
     * Identifica��o do mecanico
     *
     * @serialField mechanicId
     */
    private int mechanicId;
       
    /**
     * �rea de Reparo
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
   *  Concertando um carro (opera��o interna).
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
       while(rArea.readThePaper(mechanicId)) {							//Mec�nico vai ler o papel e verificar se tem um novo servi�o
           carId = rArea.startRepairProcedure();						//*Mec�nico recebe o id do carro que ira reparar	
           partId = rArea.startRepairProcedure();						//*Mec�nico recebe a pe�a a ser substituida no carro
               park.getVehicle(carId);									//*Mec�nico vai ao park buscar o carro para realizar o reparo
               if(rArea.partAvailable(partId)){							//Verifica se tem a pe�a no stock
                   rArea.getRequiredPart(partId);						//Mec�nico vai pega a pe�a
                    rArea.resumeRepairProcedure(partId);				//*Mec�nico volta com a pe�a e continua o reparo
                    fixIt();											//Mec�nico repara o carro
                    park.returnVehicle(carId, mechanicId);				//Mec�nico retorna o carro j� reparado ao park
                    rArea.repairConcluded(carId);						//**Mec�nico alerta o gerente que o reparo est� conclu�do
                }
                else lounge.letManagerKnow(mechanicId, partId);			//Mec�nico entra na fila de espera para atendimento com o gerente            
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