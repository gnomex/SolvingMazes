package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;

import javax.swing.JComponent;
import javax.swing.Timer;

import controller.AgenteBusca;


import model.Labirinto;
/**
 * LabirintoGrid
 * Interface de desenho do labirinto num Componente JSwing. Esta classe além de
 * desenhar o estado atual do labirinto, ela também serve de interface para alteração
 * do Labirinto.
 * 
 */
public class LabirintoGrid extends JComponent implements MouseListener, MouseMotionListener {
	
	private static final long serialVersionUID = -1603879672390660151L;
	
	private class Atualizador extends Timer {
		/**
		 * Classe de Atualizador
		 * Um Timer que enquanto a cada DELAY milisegundos atualiza a tela
		 * dando o aspecto de animação ao desenho 
		 */
		private static final long serialVersionUID = 1L;

		public Atualizador(int delay, final LabirintoGrid grid) {
			super(delay, new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					grid.repaint();
				}
			});
		}
	}
	
	/*
	 * Variáveis de ambiente e acao
	 * 
	 */
	private AgenteBusca agente;
	
	/*
	 * Variáveis de desenho do labirinto
	 * Utilizadas para desenhar o labirinto no componente
	 */
	private int larguraLabirinto;
	private int larguraCelula;
	private int larguraBorda;
	private int correcaoX;
	private int correcaoY;
	
	//Cores de desenho
	private Color fundoCelula = Color.white;
	private Color fundoDestaque = new Color(255,0,0,128);
	private Color fundoVisitada = new Color(0,0,255,128);
	private Color fundoCaminho = new Color(0,255,0,128);
	//Imagens do Agente e Objetivo
	private Image avatarAgente;
	private Image avatarGoal;
	private Image avatarSolution;
	
	/*
	 * Variáveis de interação do Mouse
	 */
	private int mouseX = -1;
	private int mouseY = -1;
	private boolean mouseBordaSul = false;
	private boolean mouseBordaLeste = false;
	
	//Atualizador de Tela
	Timer atualizador;
	
	public LabirintoGrid(AgenteBusca agente){
		this.agente = agente;
		
		this.larguraBorda = 5;
		
		//Define cor de Fundo Padrão
		this.setBackground(Color.darkGray);
		
		//Define as Imagens do Agente e de Goal
		this.avatarAgente = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/resources/agent_2.png"));
		this.avatarGoal = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/resources/goal_0.png"));
		this.avatarSolution = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/resources/solution_0.png"));
		
		//Define os Listener de Mouse para a classe atual
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		//Define o Atualizador de Tela
		this.atualizador = new Atualizador(25, this);
		this.atualizador.start();
		
		this.atualizarDimensoes();
		
	}
	
	public LabirintoGrid(){
		this(new AgenteBusca(new Labirinto(15)));
	}
	
	/*
	 * Atualizar Dimensoes
	 * Atualiza os valores das váriaveis de dimensão usadas para atualizar o desenho
	 */
	private void atualizarDimensoes() {
		this.larguraLabirinto = Math.min(this.getHeight(), this.getWidth());
		this.larguraCelula = (this.larguraLabirinto - this.larguraBorda*(this.getNoCelulas() + 1))/this.getNoCelulas();
		int ocupado = (this.larguraBorda*this.getNoCelulas() + this.larguraCelula * this.getNoCelulas());
		//Coreção de Borda nas Extremidades laterais do labirinto
		this.correcaoX = (this.getWidth() - ocupado)/2;
		this.correcaoY = (this.getHeight() - ocupado)/2;
	}
	
	/*
	 * Paint
	 * Método usado pelos componentes JSwing para desenhar o layout.
	 * Para desenhar o labirinto sigo os seguintes passo:
	 * 1 - Atualizo as variáveis de desenho para garantir que as dimensões estão certas
	 * 2 - Desenho um Retângulo de cor do Fundo cobrindo toda a dimensão do componente
	 * 		para limpar desenhos anteriores
	 * 3 - Para cada célula do labirinto faço
	 * 4 - 		faço X = CORRECAOX + (LARGURA CELULA + LARGURA BORDA) * CELULA X
	 * 5 -		faço Y = CORRECAOY + (LARGURA CELULA + LARGURA BORDA) * CELULA Y
	 * 6 -		Desenho um quadrado de lado LARGURA CELULA em X, Y
	 * 7 -		Se a célula tiver uma passagem ao LESTE desenho um retângulo de altura LARGURA
	 * 			CELULA e largura 2*LARGURA CELULA + LARGURA BORDA na posição X, Y
	 * 8 -		Se a célula tiver uma passagem ao SUL desenho um retângulo de altura 2*LARGURA
	 * 			CELULA + LARGURA BORDA e largura LARGURA CELULA na posição X, Y
	 * 9 - 		Se a CELULA já foi visitada desenho um quadrado de lado LARGURA CELULA na posição X, Y
	 * 10- faço X = CORRECAOX + (LARGURA CELULA + LARGURA BORDA) * CELULA que o mouse está X
	 * 11- faço Y = CORRECAOY + (LARGURA CELULA + LARGURA BORDA) * CELULA que o mouse está Y
	 * 12- Se o mouse está sobre uma borda Leste
	 * 12-		Desenho um retângulo de cor FUNDO DESTAQUE, de largura LARGURA BORDA e altura LARGURA CELULA na posicao X + LARGURA CELULA, Y
	 * 13- Senão se mouse está sobre uma borda sul
	 * 14-		Desenho um retângulo de cor FUNDO DESTAQUE, de largura LARGURA CELULA e altura LARGURA BORDA na posicao X, Y+ LARGURA CELULA
	 * 15- Senão Desenho um quadrado de cor FUNDO DESTAQUE de lado LARGURA CELULA em X, Y
	 * 16- X = CORRECAOX + (LARGURA CELULA + LARGURA BORDA) * CELULA que o agente está X
	 * 17- Y = CORRECAOY + (LARGURA CELULA + LARGURA BORDA) * CELULA que o agente está Y
	 * 18- Se a solução atual do agente está no estado objetivo
	 * 19-		Desenho o AVATAR SOLUCAO com tamanho LARGURA CELULA em X, Y
	 * 20- Senão
	 * 21-		Desenho o AVATAR AGENTE com tamanho LARGURA CELULA em X, Y
	 * 22- 		X = CORRECAOX + (LARGURA CELULA + LARGURA BORDA) * CELULA que o objetivo está X
	 * 23-		Y = CORRECAOY + (LARGURA CELULA + LARGURA BORDA) * CELULA que o objetivo está Y
	 * 24-		Desenho o AVATAR GOAL com tamanho LARGURA CELULA em X, Y
	 * 25- Para cada posição desde o ESTADO INICIAL até o ESTADO ATUAL do agente faça
	 * 26-		X = CORRECAOX + (LARGURA CELULA + LARGURA BORDA) * ESTADO X
	 * 27-		Y = CORRECAOY + (LARGURA CELULA + LARGURA BORDA) * ESTADO Y
	 * 28-		Desenho um quadrado de cor FUNDO CAMINHO de lado LARGURA CELULA em X, Y
	 */
	public void paint(Graphics g1){
		this.atualizarDimensoes();
		Graphics2D graphic = (Graphics2D) g1;
		
		//Limpar Canvas
		graphic.setBackground(this.getBackground());
		graphic.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		//Desenhar celulas e caminhos
		graphic.setColor(this.fundoCelula);
		int x, y;
		for(int i = 0; i < this.getNoCelulas(); i++){
			for(int j = 0; j  < this.getNoCelulas(); j++) {
				x = this.getPosicaoX(i);
				y = this.getPosicaoY(j);
				
				graphic.fillRoundRect(x, y, this.larguraCelula, this.larguraCelula, 5, 5);
				
				//Caminho ao Sul
				if(!this.getLabirinto().getCelula(i , j).isParedeSul()) {
					graphic.fillRoundRect(x, y, this.larguraCelula, 2*this.larguraCelula + this.larguraBorda, 5, 5);
				}
				//Caminho ao Leste
				if(!this.getLabirinto().getCelula(i , j).isParedeLeste()) {
					graphic.fillRoundRect(x, y, 2*this.larguraCelula + this.larguraBorda, this.larguraCelula , 5, 5);
				}
				
				//Pintar Visitadas
				if(this.getLabirinto().getCelula(i , j).isVisitada()){
					graphic.setColor(this.fundoVisitada);
					graphic.fillRoundRect(x, y, this.larguraCelula, this.larguraCelula, 5, 5);
					graphic.setColor(this.fundoCelula);
				}
				
			}
		}
		
		//Desenhar em Destaque
		if(this.mouseX != -1 && this.mouseY != -1){
			x = this.getPosicaoX(this.mouseX);
			y = this.getPosicaoY(this.mouseY);
			
			graphic.setColor(this.fundoDestaque);
			
			if(mouseBordaLeste) {
				x += this.larguraCelula;
				graphic.fillRoundRect(x, y, this.larguraBorda, this.larguraCelula, 5, 5);
			} else if(mouseBordaSul) {
				y += this.larguraCelula;
				graphic.fillRoundRect(x, y, this.larguraCelula, this.larguraBorda, 5, 5);
			} else {
				graphic.fillRoundRect(x, y, this.larguraCelula, this.larguraCelula, 5, 5);
			}
		}
		
		//Desenha os Avatares
		x = this.getPosicaoX(this.getAgente().getAtual().getX());
		y = this.getPosicaoY(this.getAgente().getAtual().getY());
		if(this.getAgente().getAtual().getX() == this.getAgente().getEstadoObjetivo()[0] 
				&& this.getAgente().getAtual().getY() == this.getAgente().getEstadoObjetivo()[1]) {
			this.scaleImageAndDraw(this.avatarSolution, x, y, this.larguraCelula, graphic);
		} else {
			this.scaleImageAndDraw(this.avatarAgente, x, y, this.larguraCelula, graphic);
			this.scaleImageAndDraw(this.avatarGoal, this.getPosicaoX(this.getAgente().getEstadoObjetivo()[0]), this.getPosicaoY(this.getAgente().getEstadoObjetivo()[1]), this.larguraCelula, graphic);
		}
		
		graphic.setColor(this.fundoCaminho);
		int[][] caminho = this.agente.getAtual().getCaminho();
		for(int i = 1; i < caminho.length; i++){
			graphic.fillRect(this.getPosicaoX(caminho[i][0]), this.getPosicaoY(caminho[i][1]), this.larguraCelula, this.larguraCelula);
		}
	}
	
	private int getPosicaoX(int x) {
		return this.correcaoX + (this.larguraCelula + this.larguraBorda)*x;
	}
	
	private int getPosicaoY(int y) {
		return this.correcaoY + (this.larguraCelula + this.larguraBorda)*y;
	}
	
	/*
	 * scaleImageandDraw
	 * recebe uma imagem e muda a escala de forma que o maior lado seja igual a LARGURA
	 * e desenha a imagem no canvas GRAPHIC
	 */
	private void scaleImageAndDraw(Image img, int x, int y, int largura, Graphics2D graphic) {
		int wd = img.getWidth(this);
		int hg = img.getHeight(this);
		double scale = 0;
		
		if(wd > hg){
			scale = (float)largura/(hg+this.larguraBorda);
		} else {
			scale = (float)largura/(wd+this.larguraBorda);
		}
		
		wd = (int)(wd*scale);
		hg = (int)(hg*scale);
		int bordaX = (largura-wd)/2;
		int bordaY = (largura-hg)/2;
		graphic.drawImage(img, x + bordaX, y + bordaY, wd, hg, this);
	}
		
	/*
	 * Getter e Setter das v�riaveis dispon�veis ao acesso
	 */

	public Labirinto getLabirinto() {
		return this.getAgente().getAmbiente();
	}

	public void setLabirinto(Labirinto labirinto) {
		this.agente.setAmbiente(labirinto);
	}

	public AgenteBusca getAgente() {
		return agente;
	}

	public void setAgente(AgenteBusca agente) {
		this.agente = agente;
	}

	public int getLarguraBorda() {
		return larguraBorda;
	}

	public void setLarguraBorda(int larguraBorda) {
		this.larguraBorda = larguraBorda;
	}

	public Color getFundoCelula() {
		return fundoCelula;
	}

	public void setFundoCelula(Color fundoCelula) {
		this.fundoCelula = fundoCelula;
	}

	public Color getFundoDestaque() {
		return fundoDestaque;
	}

	public void setFundoDestaque(Color fundoDestaque) {
		this.fundoDestaque = fundoDestaque;
	}

	public Color getFundoVisitada() {
		return fundoVisitada;
	}

	public void setFundoVisitada(Color fundoVisitada) {
		this.fundoVisitada = fundoVisitada;
	}

	public Color getFundoCaminho() {
		return fundoCaminho;
	}

	public void setFundoCaminho(Color fundoCaminho) {
		this.fundoCaminho = fundoCaminho;
	}

	public void setVisitada(Color visitada) {
		this.fundoVisitada = visitada;
	}

	public void setAvatarAgente(Image avatarAgente) {
		this.avatarAgente = avatarAgente;
	}

	public void setAvatarGoal(Image avatarGoal) {
		this.avatarGoal = avatarGoal;
	}
	
	public int getNoCelulas() {
		return this.getLabirinto().getTamanho();
	}
	
	/*
	 * ActionListeners de interação com o mouse
	 */

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {}
	
	/*
	 * Mouse Moved
	 * Atualiza a posição do ponteiro do mouse em relação a célula do labirinto e
	 * define se ele está sobre a borda leste ou borda sul da celula.
	 * 
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		this.mouseX = (e.getX() - this.correcaoX)/(this.larguraCelula+this.larguraBorda);
		this.mouseY = (e.getY() - this.correcaoY)/(this.larguraCelula+this.larguraBorda);
		if(this.getLabirinto().celulaValida(this.mouseX, this.mouseY) || !this.isEnabled()) {
			this.mouseBordaLeste = (e.getX() - this.correcaoX) % (this.larguraCelula+this.larguraBorda) > this.larguraCelula && this.mouseX < this.getNoCelulas()-1;
			this.mouseBordaSul = 	(e.getY() - this.correcaoY) % (this.larguraCelula+this.larguraBorda) > this.larguraCelula && this.mouseY < this.getNoCelulas()-1;
		} else {
			this.mouseX = -1;
			this.mouseY = -1;
		}
	}
	/*
	 * mouse Clicked
	 * Interação de quando o mouse é pressionado:
	 * Caso o ambiente não esteja habilitado ou a posição seja de uma célula inválida não há resposta
	 * Caso ele esteja sobre alguma borda, ele altera o estado da mesmo
	 * Caso ele esteja sobre uma célula ele define como a posição inicial do Agente a celula,
	 * caso a célula já seja a posição inicial define esta celula como a posição objetivo e 
	 * randomiza a posição inicial do Agente, caso a célula seja a posição objetivo ele define esta
	 * como a posição inicial e randomiza a posição do objetivo. 
	 */
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(!this.isEnabled() || (this.mouseX == -1 && this.mouseY == -1))
			return;
		
		if(this.mouseBordaLeste) {
			this.getLabirinto().alterarCaminho(this.mouseX, this.mouseY, Labirinto.L);
		} else if (this.mouseBordaSul) {
			this.getLabirinto().alterarCaminho(this.mouseX, this.mouseY, Labirinto.S);
		} else {
			int[] pos = {this.mouseX, this.mouseY};
			
			if( Arrays.equals(pos, this.agente.getEstadoInicial()) ){
				this.agente.setEstadoInicial(this.getLabirinto().getCelulaAleatoria());
				this.agente.setEstadoObjetivo(pos);
			} else if( Arrays.equals(pos, this.agente.getEstadoObjetivo()) ) {
				this.agente.setEstadoObjetivo(this.getLabirinto().getCelulaAleatoria());
				this.agente.setEstadoInicial(pos);
			} else {
				this.agente.setEstadoInicial(pos);
			}
		}
	}
	
}
