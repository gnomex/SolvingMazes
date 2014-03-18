import java.awt.BorderLayout;

import javax.swing.JFrame;

import controller.AgenteBusca;

import model.Labirinto;

import view.LabirintoGrid;


public class Teste extends JFrame {

	/**
	 * @param args
	 */
	
	public LabirintoGrid lab;
	public Teste(){
		super("Teste");
		this.setSize(600,600);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		lab = new LabirintoGrid();
		lab.getLabirinto().gerarLabirintoHuntandKill();
		this.add(lab, BorderLayout.CENTER);
	}
	
	public static void main(String[] args) {
		final Teste t = new Teste();
		t.setVisible(true);
		t.lab.setEnabled(false);
		t.lab.getAgente().setTipoBusca(AgenteBusca.Busca.PROFUNDIDADE);
		t.lab.getAgente().iniciarBusca();
		t.lab.setEnabled(true);
	}

}
