package controller;

import model.Labirinto;
import model.Solucao;

public class AgenteBusca extends AgenteBuscaAbstrato {

	public AgenteBusca(int[] estadoInicial, int[] estadoObjetivo,
			Labirinto labirinto) {
		super(estadoInicial, estadoObjetivo, labirinto);
		// TODO Auto-generated constructor stub
	}

	public AgenteBusca(Labirinto labirinto) {
		super(labirinto);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Solucao Busca() {
		while(!fronteira.isEmpty()) {
			this.atual = proximaVisita();

			if(!foiVisitada(this.atual)) {
				if(this.isObjetivo(atual)){
					return atual;
				} else {
					this.visitarEstado();
					for(int i = 0; i < 4; i++) {
						if(this.getAmbiente().movimentoValido(atual.getX(), atual.getY(), i))
							this.fronteira.add(this.getAtual().moverPara(i));
					}
				}
				try { Thread.sleep(this.sleepTime);} catch (InterruptedException e) {}
			}
		}
		System.out.print("Ops, nenhuma solução encontrada");
		return null;
	}

	@Override
	protected Solucao proximaVisita() {
		int melhor = 0;
		for(int i = 1; i < this.fronteira.size(); i++) {
			switch(this.getTipoBusca()) {
			case AESTRELA:
				if(this.funcaoF(fronteira.get(melhor)) > this.funcaoF(fronteira.get(i)))
					melhor = i;
				break;
			case PROFUNDIDADE:
				if(this.funcaoCusto(fronteira.get(melhor)) < this.funcaoCusto(fronteira.get(i)))
					melhor = i;
				break;
			}
		}
		return fronteira.remove(melhor);
	}

	@Override
	protected double funcaoF(Solucao solucao) {
		return funcaoCusto(solucao) + funcaoHeuristica(solucao);
	}

	@Override
	protected double funcaoCusto(Solucao solucao) {
		return solucao.getCusto();
	}

	@Override
	protected double funcaoHeuristica(Solucao solucao) {
		double distancia = Math.pow(solucao.getX() - this.estadoObjetivo[0], 2);
		distancia += Math.pow(solucao.getY() - this.estadoObjetivo[1], 2);
		return Math.sqrt(distancia);
	}

}
