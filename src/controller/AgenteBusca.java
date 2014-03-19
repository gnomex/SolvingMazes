package controller;

import model.Labirinto;
import model.Solucao;

public class AgenteBusca extends AgenteBuscaAbstrato {

	public AgenteBusca(int[] estadoInicial, int[] estadoObjetivo,
			Labirinto labirinto) {
		super(estadoInicial, estadoObjetivo, labirinto);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Solucao Busca() {
		// TODO Auto-generated method stub
		return null;
	}

	public AgenteBusca(Labirinto labirinto) {
		super(labirinto);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Solucao proximaVisita() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected double funcaoF(Solucao solucao) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected double funcaoCusto(Solucao solucao) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected double funcaoHeuristica(Solucao solucao) {
		// TODO Auto-generated method stub
		return 0;
	}

}
