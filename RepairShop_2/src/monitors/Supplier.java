package monitors;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author luizn
 *
 */
public class Supplier {

    /**
     * Array das partes (peças). São 3 tipos de partes (peças) representadas
     * pelo índice do array. (parte 0; parte 1; parte 2) Valores possíveis para
     * a quantidade de partes (peças) de cada tipo: O construtor atribui
     * randomicamente um valor entre 0 (inclusive) e 11 (exclusive) pra cada
     * tipo de parte.
     */
    private int[] part = new int[3];

    /**
     * Método verifica se a peça (parte) está disponível. Recebe uma peça como
     * parâmetro e verifica a sua quantidade. Caso exista a peça, atualiza a
     * quantidade e retorna a peça. Caso não exista quantidade para a peça,
     * retorna 0. O tratamento para a falta de peça, é feito por quem chamou o
     * método.
     *
     * @param peca
     * @return
     */
    public int partAvailable(int peca) {
        if (this.part[peca] == 0) {
            return peca;
        } else {
            int ret = this.part[peca];
            this.part[peca] = --this.part[peca];
            return ret;
        }
    }

    /**
     * Método para iniciar randomicamente a quantidade de peças pra cada tipo.
     */
    public void intiateSupplier() {
        //Todo Verificar se haverá necessidade de utilizar esse método substituindo o método stock em RepairArea
        for (int i = 0; i < part.length; i++) {
            part[i] = ThreadLocalRandom.current().nextInt(11);
        }
    }

    public void replenishStock(int part) {
        this.part[part] = 1 + ThreadLocalRandom.current().nextInt(11);
    }

    public void gettingNewParts() {
        
        for (int i = 0; i < part.length; i++) {
            part[i] = ThreadLocalRandom.current().nextInt(11);
        }
    }

    public int[] getPart() {
        return part;
    }

}
