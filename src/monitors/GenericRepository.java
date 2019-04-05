package monitors;

import genclass.GenericIO;
import genclass.TextFile;

/**
 * 
 * @author luizn
 *
 */
public class GenericRepository {

	/**
	 *  Nome do ficheiro de logging
	 *
	 *    @serialField fileName
	 */
	private String fileName = "log.txt";

	/**
	 *  Escrever o estado inicial (operaçãoo interna).
	 *  <p>Os mecânicos estão a dormir e os clientes a realizar as tarefas do dia a dia.
	 */
	private void reportInitialStatus (int nIter) {
		TextFile log = new TextFile ();                      // instanciação de uma variável de tipo ficheiro de texto

		if (!log.openForWriting (".", fileName)) {
			GenericIO.writelnString ("A operaçãoo de criaçãoo do ficheiro " + fileName + " falhou!");
			System.exit (1);
		}
		log.writelnString ("Problema da Atividade Oficina de Reparação");
		log.writelnString ("Número de iterações = " + nIter + "\n");
		if (!log.close ()) {
			GenericIO.writelnString ("A operação de fechar o ficheiro " + fileName + " falhou!");
			System.exit (1);
		}
		reportStatus ();
	}

	private void reportStatus() {
		TextFile log = new TextFile ();                      // instanciação de uma variável de tipo ficheiro de texto

		String lineStatus = "";                              // linha a imprimir

		if (!log.openForAppending (".", fileName))
		{ GenericIO.writelnString ("A operação de criação do ficheiro " + fileName + " falhou!");
		System.exit (1);
		}

		for (int i = 0; i < nMechanic; i++)
			switch (stateMechanic[i])
			{ case SLEEPING: lineStatus += " DORMINDO ";
			break;
			case WORKING:  lineStatus += " ACTIVIDA ";
			break;
			}

		for (int i = 0; i < nCar; i++)
			switch (stateRepair[i]) {
			case WAITREPAIRPARK: lineStatus += " Carro esperando no park para reparo ";
			break;
			case WAITTURN: lineStatus += " Esperando iniciar reparo ";
			break;
			case WAITPART: lineStatus += " Esperando peça ";
			break;
			}
		log.writelnString (lineStatus);
		if (!log.close ())
		{ GenericIO.writelnString ("A operação de fechar o ficheiro " + fileName + " falhou!");
		System.exit (1);
		}
	}

}
