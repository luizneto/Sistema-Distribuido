/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainProgram;


import genclass.FileOp;
import genclass.GenericIO;
import monitors.RepairArea;
import monitors.GenericRepository;

/**
 *
 * @author clonyjr
 */
public class RepairCar {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RepairArea rArea;                                       // Área de Reparo
        int nMechanics = 2;                                     // número de mecânicos
        int nManager = 1;                                       // número de gerentes
        int nCustomer = 30;                                     // número de clientes
        Customer [] customer = new Customer [nCustomer];        // array de threads clientes
        Mechanic [] mechanic = new Mechanic [nMechanics];       // array de threads mecânicos
        Manager manager = new Manager();                        // instancia do gerente
        int nIter;                                              // número de iterações do ciclo de vida dos clientes
        int nPart = 0;                                          // número de peças
        String fName;                                           // nome do ficheiro de logging
        int nCustomerVisits = 0;
        boolean success;                                        // validação de dados de entrada
        char opt;                                               // opção
     
     /* inicialização */
     
     GenericRepository gRepo = new GenericRepository();

      GenericIO.writelnString ("\n" + "      Trabalho dos Clientes na Loja de Reparos de Veículos\n");
      GenericIO.writeString ("Numero de iterações dos clientes? ");
      nIter = GenericIO.readlnInt ();
      /*
      do
      { //GenericIO.writeString ("Nome do ficheiro de armazenamento da simulação? ");
        //fName = GenericIO.readlnString ();
          
        if (FileOp.exists ("/Users/clonyjr/Library/Mobile Documents/com~apple~CloudDocs/Aveiro/UA/CLONY/MEI/2018-2019 SEM 2/40814-SD/Netbeans/Trabalho_1_pend/Sistema-Distribuido/RepairShop_2", "log.txt"))
           { do
             { GenericIO.writeString ("Já existe um directório/ficheiro com esse nome ( " + gRepo.fileName + "). Quer apagá-lo (s - sim; n - não)? ");
               opt = GenericIO.readlnChar ();
             } while ((opt != 's') && (opt != 'n'));
             if (opt == 's')
                success = true;
                else success = false;
           }
           else success = true;
      } while (!success);*/
      
    rArea = new RepairArea();
    
    for (int i = 0; i < nMechanics; i++)
        mechanic[i] = new Mechanic (i, rArea);
    for (int i = 0; i < nCustomer; i++)
        customer[i] = new Customer (i, nIter, i, rArea);

    /* arranque da simulação */
    manager.start ();
    for (int i = 0; i < nCustomer; i++)
        customer[i].start ();

     /* aguardar o fim da simulação */

     GenericIO.writelnString ();
     for (int i = 0; i < nCustomer; i++)
     { try
     { customer[i].join ();
     }
     catch (InterruptedException e) {}
     GenericIO.writelnString ("O cliente " + i + " terminou.");
     }
     GenericIO.writelnString ();
     for (int i = 0; i < nMechanics; i++)
     { while (mechanic[i].isAlive ())
     { mechanic[i].interrupt ();
     Thread.yield ();
     }
     try
     { mechanic[i].join ();
     }
        catch (InterruptedException e) {}
        GenericIO.writelnString ("O mecanico " + i + " terminou.");
      }
      GenericIO.writelnString ();
      
    GenericIO.writelnString ();
    { while (manager.isAlive ())
    { manager.interrupt ();
    Thread.yield ();
    }
    try
    { manager.join ();
    }
       catch (InterruptedException e) {}
       GenericIO.writelnString ("O gerente terminou.");
    }
    GenericIO.writelnString ();

    }

    }
