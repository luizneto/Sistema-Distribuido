/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainProgram;


import genclass.FileOp;
import genclass.GenericIO;
import monitors.RepairArea;

/**
 *
 * @author clonyjr
 */
public class RepairCar {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RepairArea rArea;                                       // �rea de Reparo
        int nMechanics = 2;                                     // n�mero de mec�nicos
        int nManager = 1;                                       // n�mero de gerentes
        int nCustomer = 30;                                     // n�mero de clientes
        Customer [] customer = new Customer [nCustomer];        // array de threads clientes
        Mechanic [] mechanic = new Mechanic [nMechanics];       // array de threads mec�nicos
        Manager manager = new Manager();                        // instancia do gerente
        int nIter;                                              // n�mero de itera��es do ciclo de vida dos clientes
        int nPart = 0;                                          // n�mero de pe�as
        String fName;                                           // nome do ficheiro de logging
        int nCustomerVisits = 0;
        boolean success;                                        // valida��o de dados de entrada
        char opt;                                               // op��o
     
     /* inicializa��o */

      GenericIO.writelnString ("\n" + "      Trabalho dos Clientes na Loja de Reparos de Ve�culos\n");
      GenericIO.writeString ("Numero de itera��es dos clientes? ");
      nIter = GenericIO.readlnInt ();
      do
      { GenericIO.writeString ("Nome do ficheiro de armazenamento da simula��o? ");
        fName = GenericIO.readlnString ();
        if (FileOp.exists (".", fName))
           { do
             { GenericIO.writeString ("J� existe um direct�rio/ficheiro com esse nome. Quer apag�-lo (s - sim; n - n�o)? ");
               opt = GenericIO.readlnChar ();
             } while ((opt != 's') && (opt != 'n'));
             if (opt == 's')
                success = true;
                else success = false;
           }
           else success = true;
      } while (!success);
    rArea = new RepairArea(manager, nMechanics, nCustomer, nPart, fName, nIter, nCustomerVisits);
    
    for (int i = 0; i < nMechanics; i++)
        mechanic[i] = new Mechanic (i, rArea);
    for (int i = 0; i < nCustomer; i++)
        customer[i] = new Customer (i, nIter, i, rArea);

    /* arranque da simula��o */
    manager.start ();
    for (int i = 0; i < nCustomer; i++)
        customer[i].start ();

     /* aguardar o fim da simula��o */

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
