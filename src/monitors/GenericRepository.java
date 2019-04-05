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
	 *  Escrever o estado inicial (opera��oo interna).
	 *  <p>Os mec�nicos est�o a dormir e os clientes a realizar as tarefas do dia a dia.
	 */
	private void reportInitialStatus (int nIter) {
		TextFile log = new TextFile ();                      // instancia��o de uma vari�vel de tipo ficheiro de texto

		if (!log.openForWriting (".", fileName)) {
			GenericIO.writelnString ("A opera��oo de cria��oo do ficheiro " + fileName + " falhou!");
			System.exit (1);
		}
		log.writelnString ("Problema da Atividade Oficina de Repara��o");
		log.writelnString ("N�mero de itera��es = " + nIter + "\n");
		if (!log.close ()) {
			GenericIO.writelnString ("A opera��o de fechar o ficheiro " + fileName + " falhou!");
			System.exit (1);
		}
		reportStatus ();
	}

	private void reportStatus() {
		TextFile log = new TextFile ();                      // instancia��o de uma vari�vel de tipo ficheiro de texto

		String lineStatus = "";                              // linha a imprimir

		if (!log.openForAppending (".", fileName))
		{ GenericIO.writelnString ("A opera��o de cria��o do ficheiro " + fileName + " falhou!");
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
			case WAITPART: lineStatus += " Esperando pe�a ";
			break;
			}
		log.writelnString (lineStatus);
		if (!log.close ())
		{ GenericIO.writelnString ("A opera��o de fechar o ficheiro " + fileName + " falhou!");
		System.exit (1);
		}
	}

}
