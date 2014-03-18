package controller;

import java.util.LinkedList;
import java.util.List;

import model.Labirinto;
import model.Solucao;


public abstract class AgenteBuscaAbstrato {
	/**
	 * Agente de Busca Abstrato
	 * Esta classe visa encapsular métodos e operações que operam sobre um ambiente totalmente observável, o LABIRINTO,
	 * para descobrir o melhor caminho entre o ESTADO_INICIAL e ESTADO_FINAL.
	 * Para facilitar o uso da mesma como interface do presente estado da busca, a instância conta com algumas váriaveis
	 * de execução, como:
	 * atual ~> ponteiro para solução atual;
	 * fronteira ~> lista de fronteira de soluções a Serem analisadas;
	 * visitados ~> lista de CELULAS X, Y do labirinto já visitadas;
	 * sleepTime ~> tempo em milisegundos que o algoritmo dorme antes de continuar a análise.
	 * 				Esta váriavel é util para animar o processo de busca, possibilitando a 
	 * 				visualização do processo de busca.
	 * @author André M. Ribeiro dos Santos
	 * @author Jorge Aikes Junior
	 */



	/*
	 * BUSCA
	 * Define quatro tipos de Busca que podem ser executadas para Buscar a solu��o:
	 * A Estrela,
	 * Gulosa,
	 * Profundidade e
	 * Amplitude
	 */
	public static enum Busca{ AESTRELA, GULOSA, PROFUNDIDADE, AMPLITUDE };

	/*
	 * Váriaveis de instância
	 * 
	 */
	protected Labirinto 	ambiente;
	protected int[] 		estadoInicial;
	protected int[] 		estadoObjetivo;

	protected Solucao 		atual;
	protected List<Solucao> 	fronteira;
	protected List<int[]> 	visitados;
	protected long 			sleepTime;
	protected Busca			tipoBusca;		

	/**
	 * Construtor parametrizado.
	 * @param estadoInicial Posição inicial no labirinto.
	 * @param estadoObjetivo Posição meta no labirinto.
	 * @param labirinto Objeto do tipo labirinto a ser percorrido.
	 */
	public AgenteBuscaAbstrato(int[] estadoInicial, int[] estadoObjetivo, Labirinto labirinto) {
		this.estadoInicial = estadoInicial;
		this.estadoObjetivo = estadoObjetivo;
		this.ambiente = labirinto;
		this.tipoBusca = Busca.AESTRELA;

		this.sleepTime = 200;

		this.resetarBusca();
	}

	/**
	 * Construtor padrão, calculando posição inicial e objetivo aleatóriamente.
	 * @param labirinto Objeto do tipo labirinto a ser percorrido.
	 */
	public AgenteBuscaAbstrato(Labirinto labirinto) {
		this(labirinto.getCelulaAleatoria(), labirinto.getCelulaAleatoria(), labirinto);
	} 

	/**
	 * Reinstância as variáveis de execução para iniciar um processo de busca
	 */
	public void resetarBusca() {
		this.atual = new Solucao(estadoInicial[0], estadoInicial[1]);

		this.fronteira = new LinkedList<Solucao>();
		this.fronteira.add(atual);

		this.visitados = new LinkedList<int[]>();

		this.ambiente.limparVisitas();
	}

	/*
	 * Processos de Busca
	 * O algoritmo de Busca generico foi implementado de maneira que dependendo do tipo de Busca
	 * definido o algoritmo busca de maneira diferente a proxima solução a ser visitada.
	 * Para montar uma interface entre a busca, ele oferece 2 métodos:
	 * 
	 * iniciarBusca
	 * Que reseta as váriaveis de execução para iniciar um processo de busca do 0.
	 * 
	 * continuarBusca
	 * Este método continua a execução de uma busca a partir do estado das váriaveis de instância atual.
	 * 
	 * Ambos os métodos retorna uma Solução aceitável (que atinge o ESTADO OBJETIVO) possível ou NULO
	 * caso esgote a busca e não encontre outra solução.
	 */

	/**
	 * Reseta as váriaveis de execução para iniciar um processo de busca do 0.
	 * @return Uma Solução aceitável (que atinge o ESTADO OBJETIVO) possível ou NULO
	 * caso esgote a busca e não encontre outra solução.
	 */
	public Solucao iniciarBusca(){
		this.resetarBusca();
		return this.Busca();
	}
	/**
	 * continua a execução de uma busca a partir do estado das váriaveis de instância atual.
	 * @return Uma Solução aceitável (que atinge o ESTADO OBJETIVO) possível ou NULO
	 * caso esgote a busca e não encontre outra solução.
	 */
	public Solucao continuarBusca(){
		return this.Busca();
	}

	/**
	 * Busca
	 * Implementação genérica de um algoritmo de Busca, que segue os seguintes passos:
	 * 
	 * 1 - faça ATUAL = seleciona e retira proxima visita da Fronteira
	 * 2 - Se foi visitado então volte para 1
	 * 3 - Se ATUAL é uma solução que está no ESTADO OBJETIVO então retorna ATUAL
	 * 4 - Adiciona estados filhos de ATUAL a FRONTEIRA
	 * 5 - Repete 1 a 5 enquanto a FRONTEIRA não está VAZIA
	 * 6 - Retorna NULO
	 */
	public abstract Solucao Busca();

	/**
	 * proximaVisita
	 * Este método seleciona e retira a proxima solução a ser visitada da FRONTEIRA de acordo com o
	 * algoritmo de Busca. Ele percore a fronteira procurando o nodo que melhor atende a seleção do
	 * tipo de Busca escolhida.
	 * 
	 * Seleções:
	 * A ESTRELA ~> seleciona solução com menor F(), ou seja, menor soma entre Heuristica e Custo
	 * PROFUNDIDADE ~> seleciona a solução de maior custo, pois neste caso o custo representa o número de visitas
	 * AMPLITUDE ~> seleciona a solução com menor custo
	 * GULOSA ~> seleciona a solução de menor Heurística
	 */
	protected abstract Solucao proximaVisita();

	/**
	 * Encapsula a verificação se uma CELULA X, Y já foi visitada pelo algoritmo de busca, ou seja,
	 * se ela está contida na lista de VISITADAS.
	 * @param solucao Solução a ser verificada
	 * @return True se a solução já foi visitada, False contrário
	 */

	protected boolean foiVisitada(Solucao solucao) {
		for(int i = 0; i < this.visitados.size(); i++) {
			if(this.visitados.get(i)[0] == solucao.getX() && this.visitados.get(i)[1] == solucao.getY())
				return true;
		}
		return false;
	}

	/**
	 * Encapsula a verificação se a Solucao S está no ESTADO OBJETIVO.
	 * @param solucao Solução a ser validade como objetivo ou não.
	 * @return True se for a solução objetivo, False contrário.
	 */
	protected boolean isObjetivo(Solucao solucao){
		return solucao.getX() == this.estadoObjetivo[0] && solucao.getY() == this.estadoObjetivo[1];
	}

	/*
	 * Funções de Custo
	 * Estas funções encapsulam o calculo para as funções de custo de uma dada solução.
	 * 
	 * Função F
	 * Resulta o custo total da solução mais a heurística da solução. Representa, portanto, uma estimativa
	 * do custo total da solução.
	 * 
	 * Função Custo
	 * Resulta no custo acumulado dos passos tomados desde da origem até o estado atual da solução
	 * 
	 * Função Heuristíca
	 * Estimativa do custo da posição de uma solução até o Objetivo. No caso do labirinto, uma boa heurística pode
	 * ser a distância linear entre a posição da solução S até o OBJETIVO
	 */
	protected abstract double funcaoF(Solucao solucao);

	protected abstract  double funcaoCusto(Solucao solucao);

	protected abstract  double funcaoHeuristica(Solucao solucao);

	/**
	 * Encapsula a visita de uma posição do labirinto, adicionando a posição X, Y
	 * a lista de visitados e avisa ao ambiente que a CELULA X, Y foi visitada.
	 */

	protected void visitarEstado(){
		int[] posicao = {this.getAtual().getX(), this.getAtual().getY()};
		this.visitados.add(posicao);
		this.ambiente.getCelula(this.getAtual().getX(), this.getAtual().getY()).setVisitada(true);
	}

	/*
	 * Getter e Setters da váriaveis de instância
	 */
	public Labirinto getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(Labirinto ambiente) {
		this.ambiente = ambiente;
		this.estadoInicial = ambiente.getCelulaAleatoria();
		this.estadoObjetivo = ambiente.getCelulaAleatoria();

		this.resetarBusca();
	}

	public int[] getEstadoInicial() {
		return estadoInicial;
	}

	public void setEstadoInicial(int[] estadoInicial) {
		this.estadoInicial = estadoInicial;
		this.resetarBusca();
	}

	public int[] getEstadoObjetivo() {
		return estadoObjetivo;
	}

	public void setEstadoObjetivo(int[] estadoObjetivo) {
		this.estadoObjetivo = estadoObjetivo;
		this.resetarBusca();
	}

	public Solucao getAtual() {
		return atual;
	}

	public List<Solucao> getFronteira() {
		return fronteira;
	}

	public List<int[]> getVisitados() {
		return visitados;
	}

	public Busca getTipoBusca() {
		return tipoBusca;
	}

	public void setTipoBusca(Busca tipoBusca) {
		this.tipoBusca = tipoBusca;
	}

	public long getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

}

