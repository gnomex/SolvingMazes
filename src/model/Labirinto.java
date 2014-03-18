package model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

/**
 * @author  André M. Ribeiro dos Santos
 * @author  Jorge Aikes Junior
 * @version 0.3
 * 
 * Labirinto
 * 
 * Uma representação da estrutura de um labirinto, como uma matriz de
 * células, onde cada uma tenha quatro portas nos sentidos: 
 * Norte, Sul, Leste e Oeste.
 * 
 * Além disso, a classe encapsula as seguintes técnicas de geração de labirinto:
 * Recursivo*
 * Hunt and Kill
 * Algoritmo de Primm
 * 
 */
public class Labirinto {

    /*
     * Váriaveis de Classe
     * N, S, L, O
     * Valores representativos dos movimentos de uma célula.
     * 
     * DX ~> O deslocamento em X de acordo com o sentido.
     * DY ~> O deslocamento em Y de acordo com o sentido.
     * OPOSTO ~> Sentido oposto.
     * 
     */
    public final static int N = 0;
    public final static int S = 1;
    public final static int L = 2;
    public final static int O = 3;
    
    public final static int[] DX = {0, 0, 1, -1};
    public final static int[] DY = {-1, 1, 0, 0};
    public final static int[] OPOSTO = {S, N, O, L};
    
        
    /*
     * Váriaveis de instância
     * 
     * labirinto    ~> matriz de células(NxN)
     * tamanho      ~> tamanho N da matriz
     */
    private Celula[][] labirinto;
    private int tamanho;

    /**
     * Construtor padrão
     * @param tamanho Tamanho na matriz quadrada representando o labirinto.
     */
    public Labirinto(int tamanho) {
        this.setTamanho(tamanho);
    }

    /*
     * Getter e Setter de Tamanho
     * Sendo que toda vez que o tamanho é atualizado, a instância reinicia sua
     * matriz de células
     */
    /**
     * Retorna o tamanho do labirinto.
     * @return tamanho labirinto.
     */
    public final int getTamanho() {
        return tamanho;
    }

    /**
     * Configura o tamanho do labirinto, sendo que toda vez que o tamanho 
     * é atualizado, a instância reinicia sua matriz de células.
     *
     * @param tamanho A dimensão do labirinto
     */
    public final void setTamanho(int tamanho) {
        this.tamanho = tamanho;
        this.reiniciarLabirinto();
    }
    
    /**
     * Retorna a célula na linha Y, coluna X da matriz labirinto
     * Encapsulamento do acesso as células
     * @param x Posição X da célula no labirinto
     * @param y Posição y da célula no labirinto
     * @return Retorna a célula na linha Y, coluna X da matriz labirinto
     */
    public final Celula getCelula(int x, int y) {
        return labirinto[y][x];
    }
    
    
    @Override
    public final String toString(){
        StringBuilder builder = new StringBuilder(" ");
        //Borda superior
        for (int i = 0; i < this.getTamanho()*2 - 1; i++) {
            builder.append("_");
        }
        builder.append("\n");

        for (int y = 0; y < this.getTamanho(); y++) {
            builder.append("|");
            for (int x = 0; x < this.getTamanho(); x++) {
                builder.append(this.getCelula(x, y).isParedeSul()? "_" : " ");
                builder.append(this.getCelula(x, y).isParedeLeste()? "|" : " ");
            }
            builder.append("\n");
        }

        return builder.toString();
    }
      
    /**
     * Reconstrói o labirinto, com as células do labirinto de acordo com Fechado.
     * @param fechado Forma de representação da célula.
     * @see Celula(boolean).
     */
    public void reiniciarLabirinto(boolean fechado) {
        labirinto = new Celula[this.getTamanho()][this.getTamanho()];
        for(int y = 0; y < this.getTamanho(); y++)
            for(int x = 0; x < this.getTamanho(); x++)
                this.labirinto[y][x] = new Celula(fechado);
    }
    
    /**
     * Reconstrói o labirinto com células fechadas.
     */
    public void reiniciarLabirinto(){
        this.reiniciarLabirinto(true);
    }
    
    /**
     * Este método visita todas as células da matriz e diz que a celula X,Y
     * não foi visitada.
     */
    public void limparVisitas() {
        for (int y = 0; y < labirinto.length; y++)
            for (int x = 0; x < labirinto[y].length; x++)
                this.getCelula(x, y).setVisitada(false);
    }
    
    /*
     * getCelulaAleatoria
     * Retorna uma CELULA em alguma posição do labirinto. Este m�todo tem como objetivo simplificar o acesso a uma 
     * célula qualquer do labirinto para os métodos internos e externos
     */
    /**
     * Retorna uma posição aleatória do labirinto. Este método tem como 
     * objetivo simplificar o acesso a uma  célula qualquer do labirinto para 
     * os métodos internos e externos.
     * @return Uma posição de célula aleatória
     */
    public int[] getCelulaAleatoria() {
    	Random rnd = new Random();
    	int[] pos = {rnd.nextInt(this.getTamanho()), rnd.nextInt(this.getTamanho())};
    	return pos;
    }
    
    /**
     * Verifica se a célula X, Y está fechada, ou seja, todas as 
     * paredes são verdadeiras.
     * @param x Posição X da célula no labirinto
     * @param y Posição y da célula no labirinto
     * @return True se a célula está com todas as paredes contruídas.
     */
    public boolean celulaFechada(int x, int y) {
        return this.getCelula(x, y).isFechada();
    }
    
    /**
     * Verifica se a celula X, Y está dentro do escopo da matriz, ou seja,
     * se o X e Y pertencem ao {0..TAMANHO}
     * @param x Posição X da célula no labirinto
     * @param y Posição y da célula no labirinto
     * @return True se a célula está dentro dos valores válidos.
     */
    public boolean celulaValida(int x, int y) {
        return x >= 0 && x < this.getTamanho() && y >= 0 && y < this.getTamanho();
    }
    
    /**
     * Testes de Movimentos
     * 
     * Este movimento verifica se é possível mover de uma CELULA X, Y no sentido indicado.
     * @param x Posição X da célula no labirinto
     * @param y Posição y da célula no labirinto
     * @param sentido Orientação de movimentação N, S, L, O
     * @return True se a movimentação é possível.
     */
    public boolean movimentoValido(int x, int y, int sentido) {
    	boolean possivel = false;
    	
    	switch(sentido) {
    	case N:
    		possivel = !this.getCelula(x, y).isParedeNorte();
    		break;
    	case S:
    		possivel = !this.getCelula(x, y).isParedeSul();
    		break;
    	case L:
    		possivel = !this.getCelula(x, y).isParedeLeste();
    		break;
    	case O:
    		possivel = !this.getCelula(x, y).isParedeOeste();
    		break;
    	}
    	
    	return possivel;
    }
    
    /*
     * Operação do Labirinto
     * Conjunto de métodos para encapsular alteração na estrutura do labirinto.
     * 
     */
     
    /**
     * Abre uma passagem da célula X, Y no sentido S,
     * settando a parede neste sentido e a parede no sentido oposto a S
     * da célula adjacente(no sentido S) como falsa.
     * @param x Posição X da célula no labirinto
     * @param y Posição y da célula no labirinto
     * @param sentido Orientação de movimentação N, S, L, O
     */
    public void abrirCaminho(int x, int y, int sentido) {
        int nx = x + DX[sentido];
        int ny = y + DY[sentido];
        
        if(celulaValida(nx, ny)) {
            this.getCelula(x, y).destruirParede(sentido);
            this.getCelula(nx, ny).destruirParede(OPOSTO[sentido]);
        }
    }
    /**
     * Fecha uma passagem da célula X, Y no sentido S,
     * settando a parede neste sentido e a parede no sentido oposoto a S
     * da célula adjacente(no sentido S) como verdadeira.
     * @param x Posição X da célula no labirinto
     * @param y Posição y da célula no labirinto
     * @param sentido Orientação de movimentação N, S, L, O
     */
    public void fecharCaminho(int x, int y, int sentido) {
        int nx = x + DX[sentido];
        int ny = y + DY[sentido];
        
        if(celulaValida(nx, ny)) {
            this.getCelula(x, y).construirParede(sentido);
            this.getCelula(nx, ny).construirParede(OPOSTO[sentido]);
        }
    }
    
    /**
     * Abre uma passagem da célula X, Y no sentido S,
     * settando a parede neste sentido e a parede no sentido oposoto a S
     * da célula adjacente(no sentido S) como ¬ do estado atual.
     * @param x Posição X da célula no labirinto
     * @param y Posição y da célula no labirinto
     * @param sentido Orientação de movimentação N, S, L, O
     */
    public void alterarCaminho(int x, int y, int sentido) {
        int nx = x + DX[sentido];
        int ny = y + DY[sentido];
        
        if(celulaValida(nx, ny)) {
            this.getCelula(x, y).alterarParede(sentido);
            this.getCelula(nx, ny).alterarParede(OPOSTO[sentido]);
        }
    }
    
    /*
     * Algoritmos de Geração de Labirinto (gerarLabirinto*)
     * a classe de Labirinto, conta com três técnicas de geração automâtica
     * de labirinto:
     * 
     * Técnica Recursiva*
     * Hunt and Kill
     * Primm
     * 
     * *~> O algoritmo recursivo foi alterado para operar de forma não 
     * recursiva para evitar overflow de mémoria
     */ 

     /** 
      * Este método usa uma adaptação da técnica de geração recursiva para
     * funcionar de forma não recursiva. Está técnica funciona semelhante a
     * uma busca em profundidade, onde ele segue um camaninho até não poder
     * mais caminhar.
     * O funcionamento é o seguinte:
     * 
     * MOVIMENTO_INICIAL =  movimento da CELULA 0, 0 no SENTIDO L
     * 
     * 1 - Inicia uma pilha FRONTEIRA e adiciona MOVIMENTO_INICIAL
     * 2 - ATUAL = Retira o topo da FRONTEIRA
     * 3 - Se a ATUAL.CELULA está fechada(todas as paredes verdadeiras) então
     *     destroi parede da ATUAL.CELULA no sentido ATUAL.SENTIDO
     * 4 - Seleciona aleatoriamente um SENTIDO entre N, S, L, O
     * 5 - faça NX = ATUAL.X + DX[SENTIDO] e NY = ATUAL.Y + DY[SENTIDO]
     * 6 - Se CELULA NX, NY for válida e inalterada então
     *     adiciona o movimento da CELULA NX, NY no sentido OPOSTO[SENTIDO]
     * 7 - Selecione outro SENTIDO aleatoriamente entre os restantes
     * 8 - Repita 5 a 8 enquanto restar movimentos
     * 9 - Repita 2 a 9 enquanto a FRONTEIRA não estiver vazia
     * 
     */
    public void gerarLabirintoRecursivo() {
        this.reiniciarLabirinto();
        /*
         * Os movimentos aqui são representados 
         * por um vetor de 3 inteiros:
         * {X, Y, SENTIDO}
         */
        Stack<int[]> fronteira = new Stack<int[]>();
        
        LinkedList<Integer> movimentos = new LinkedList<Integer>();
        movimentos.add(N);
        movimentos.add(S);
        movimentos.add(L);
        movimentos.add(O);
        
        int[] atual = {0, 0, S};
        int nx, ny;

        fronteira.add(atual);

        while (!fronteira.isEmpty()) {
            atual = fronteira.pop();
            
            if (this.celulaFechada(atual[0], atual[1])) {
                this.abrirCaminho(atual[0], atual[1], atual[2]);
                //Print de labirinto para observar os passos de geração
                //System.out.println(this.toString());
            }
            
            Collections.shuffle(movimentos);
            for (int i = 0; i < movimentos.size(); i++) {
                nx = atual[0] + DX[movimentos.get(i)];
                ny = atual[1] + DY[movimentos.get(i)];

                if (this.celulaValida(nx, ny) && this.celulaFechada(nx, ny)) {
                    int proximo[] = {nx, ny, OPOSTO[movimentos.get(i)]};
                    fronteira.add(proximo);
                }
            }
        }
    }

    /**
     * Este método usa a estratégia de Hunt and Kill para geração automâtica
     * do labirinto. Segundo esta estratégia e partindo de uma posição X, Y
     * aletória ele caminha em sentido aleatórios até não poder mais andar (as
     * células adjacentes já foram alteradas), a seguir ele caça por uma nova
     * célula inalterada com alguma célula adjacente alterada e cria uma caminho
     * entre elas e volta a caminhar. Ele repete esses ciclos até não haver
     * mais células inalteradas.
     * O processo pode ser representado da seguinte maneira:
     * ATUAL ~> X, Y (aleatórios)
     * 1 - ATUAL = Escolhe um sentido entre N, S, L, O e caminha neste sentido
     * 2 - Repete 1 a 2 Enquanto houver para onde caminhar( as células adjacentes
     * são válidas e fechada).
     * 3 - ATUAL = Procura no labirinto uma célula fechada com alguma 
     * adjacente não fechada e cria uma passagem
     * 4 - Repete 1 a 4 enquanto houver células inalteradas
     * 
     * Este processo pode ser simplificado em dois momentos:
     * 1 - Caminhar(passo 2)
     * 2 - Caçar(passo 3)
     * 
     * O algoritmo principal simplificado fica:
     * 1 - Faça ATUAL = uma CELULA X, Y aleatória
     * 2 - Faça ATUAL = CAMINHAR()
     * 3 - Se ATUAL for NULO então faça ATUAL = CAÇAR()
     * 4 - Repita 2 a 4 enquanto ATUAL nao for NULO
     * 
     */
    
    public void gerarLabirintoHuntandKill() {
        this.reiniciarLabirinto();

        int[] atual = this.getCelulaAleatoria();

        while (atual != null) {
            atual = this.walk(atual[0], atual[1]);
            if (atual == null)
                atual = this.hunt();
            //Printa o labirinto atual para observar os passos de desenvolvimento
            //System.out.println(this.toString());
        }
    }

    /**
     * Método para o algoritmo "caminhar" pelo labirinto.
     * @param x Posição X da célula no labirinto
     * @param y Posição y da célula no labirinto
     * @return Movimentação
     */
    private int[] walk(int x, int y) {
        int nx, ny;

        LinkedList<Integer> movimentos = new LinkedList<Integer>();
        movimentos.add(N);
        movimentos.add(S);
        movimentos.add(L);
        movimentos.add(O);

        Collections.shuffle(movimentos);
        for (int i = 0; i < movimentos.size(); i++) {
            nx = x + DX[movimentos.get(i)];
            ny = y + DY[movimentos.get(i)];

            if (this.celulaValida(nx, ny) && this.celulaFechada(nx, ny)) {
                this.abrirCaminho(x, y, movimentos.get(i));
                int[] to = {nx, ny};
                return to;
            }
        }
        return null;
    }
    
    /**
     * Método para o algoritmo "caçar" pelo labirinto.
     * @return Movimentação
     */

    private int[] hunt() {
        for (int y = 0; y < this.getTamanho(); y++) {
            for (int x = 0; x < this.getTamanho(); x++) {
                
                if (this.celulaFechada(x, y)) {
                    LinkedList<Integer> vizinhos = new LinkedList<Integer>();
                    //Verifica cada movimento N, S, L, O ~> 0,1,2,3
                    for (int i = 0; i < 4; i++) {
                        if (this.celulaValida(x + DX[i], y + DY[i]) && !this.celulaFechada(x + DX[i], y + DY[i])) {
                            vizinhos.add(i);
                        }
                    }
                    if (!vizinhos.isEmpty()) {
                        Random random = new Random();
                        this.abrirCaminho(x, y, vizinhos.get(random.nextInt(vizinhos.size())));
                        int[] to = {x, y};
                        return to;
                    }
                }
            }
        }
        return null;
    }

}
