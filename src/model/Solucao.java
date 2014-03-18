package model;

import java.util.ArrayList;

/**
 * @author  André M. Ribeiro dos Santos
 * @author Jorge Aikes Junior
 * 
 * Solucao
 * Classe para representar uma solução a ser analisada pelo Agente de Busca
 * Ela conta com dois inteiros X, Y para representar a posição da solução no labirinto,
 * um inteiro para guardar o custo da solução, um inteiro para guardar o sentido do movimento
 * que gerou a solução atual e um ponteiro para a solução pai, ou seja,
 * a solução de onde andando em algum sentido gerou a solução atual.
 */

public class Solucao {
	/*
	 * Váriaveis de Instância
	 * 
	 * x, y ~> posição da solução no labirinto.
	 * custo ~> custo da solução.
	 * pai ~> ponteiro para solução pai.
	 * acao ~> sentido do movimento que do nó pai gerou a solução atual.
	 */
	private int x, y;
	private int custo;
	private Solucao pai;
	private int acao;
	
	/**
	 * Construtor parametrizado completo.
	 * @param x Posição x da solução no labirinto.
	 * @param y Posição y da solução no labirinto.
	 * @param custo Custo da solução.
	 * @param acao Sentido do movimento que do nó pai gerou a solução atual.
	 * @param pai Ponteiro para solução pai.
	 */
	public Solucao(int x, int y, int custo, int acao, Solucao pai) {
		this.x = x;
		this.y = y;
		this.pai = pai;
		this.acao = acao;
		this.custo = custo;
	}
	
	/**
	 * Construtor padrão.
	 * @param x Posição x da solução no labirinto.
	 * @param y Posição y da solução no labirinto.
	 */
	public Solucao(int x, int y){
		this(x, y, 0, -1, null);
	}
	

	/**
	 * Retorna a posição x da Solução.
	 * @return Posição x da Solução.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Retorna a posição y da Solução.
	 * @return Posição y da Solução.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Retorna o custo da Solução.
	 * @return custo da Solução.
	 */
	public int getCusto() {
		return custo;
	}

	/**
	 * Retorna a Solução pai da Solução atual.
	 * @return Pa da Solução atual.
	 */
	public Solucao getPai() {
		return pai;
	}

	/**
	 * Retorna o sentido que foi percorrido para se chegar a Solução.
	 * @return Ação anterior da atual.
	 */
	public int getAcao() {
		return acao;
	}
	
	/**
	 * Retorna um vetor da sequência de posições X, Y que gerou a solução atual.
	 * @return Matriz de inteiro indicando o caminho percorrido.
	 */
	public int[][] getCaminho() {
		ArrayList<int[]> caminho = new ArrayList<int[]>();
		Solucao atual = this;
		while(atual != null){
			int[] pos = {atual.getX(), atual.getY()};
			caminho.add(pos);
			atual = atual.getPai();
		}
		return caminho.toArray(new int[1][1]);
	}
	
	/**
	 * Retorna um vetor de ações que geraram da origem a solução atual.
	 * @return Vetor de ações.
	 */	
	public Integer[] getMovimentos() {
		ArrayList<Integer> movimentos = new ArrayList<Integer>();
		Solucao atual = this;
		while(atual.getPai() != null){
			movimentos.add(atual.getAcao());
			atual = atual.getPai();
		}
		return movimentos.toArray(new Integer[1]);
	}
	
	/**
	 * Retorna uma nova solução filha da atual ação
	 * @param Ação a ser executada (sentido).
	 */
	
	public Solucao moverPara(int acao) {
		return new Solucao(
				this.getX() + Labirinto.DX[acao], 
				this.getY() + Labirinto.DY[acao], 
				this.getCusto() + 1, acao, this);
	}
}
