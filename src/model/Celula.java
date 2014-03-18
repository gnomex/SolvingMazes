package model;

/**
 * @author André M. Ribeiro dos Santos
 * @author Jorge Aikes Junior
 * @version 0.3
 * 
 * 
 * Celula
 * Representação de unidade do Labirinto
 * com 4 váriaveis booleanas, 4 representando a presença das paredes Norte,
 * Sul, Leste e Oeste e 1 representando se a célula já voi visitada.
 * 
 */

public class Celula {

    private boolean paredeNorte, paredeSul, paredeLeste, paredeOeste;
    private boolean visitada;

    /** 
     * Construtor opcional.
     */
    public Celula(boolean fechada) {
        this.paredeLeste = fechada;
        this.paredeOeste = fechada;
        this.paredeNorte = fechada;
        this.paredeSul = fechada;
        this.visitada = false;
    }

    /** 
     * Construtor padrão modificado.
     */
    public Celula(){
        this(true);
    }
    
    /**
     * Verifica se a célula foi visitada.
     * @return True se a célula já foi visitada, False de outra forma.
     */
    public boolean isVisitada() {
        return visitada;
    }
    
    /**
     * Confirma a célula como visitada
     * @param visitada True se a célula está sendo visitada
     */
    public void setVisitada(boolean visitada) {
        this.visitada = visitada;
    }
    
    
    /**
     * Encapsulamento da destruição das paredes.
     * Muda o estado da parede para falso (não existente).
     *  
     * @param sentido Orientação da parede sentido N, S, L e O.
     */
    public void destruirParede(int sentido) {
        switch (sentido) {
            case Labirinto.N:
                this.paredeNorte    = false;
                break;
            case Labirinto.S:
                this.paredeSul      = false;
                break;
            case Labirinto.O:
                this.paredeOeste    = false;
                break;
            case Labirinto.L:
                this.paredeLeste    = false;
                break;
        }
    }
    
    /**
     * Encapsulamento da construção das paredes.
     * Muda o estado da parede para verdadeiro (parede existente).
     *  
     * @param sentido Orientação da parede sentido N, S, L e O.
     */
    public void construirParede(int sentido) {
        switch (sentido) {
            case Labirinto.N:
                this.paredeNorte    = true;
                break;
            case Labirinto.S:
                this.paredeSul      = true;
                break;
            case Labirinto.O:
                this.paredeOeste    = true;
                break;
            case Labirinto.L:
                this.paredeLeste    = true;
                break;
        }
    }
    
    /**
     * Alteração da parede, muda seu estado para não-atual (¬estado)
     *  
     * @param sentido Orientação da parede sentido N, S, L e O.
     */
    public void alterarParede(int sentido) {
        switch (sentido) {
            case Labirinto.N:
                this.paredeNorte    = !this.paredeNorte;
                break;
            case Labirinto.S:
                this.paredeSul      = !this.paredeSul;
                break;
            case Labirinto.O:
                this.paredeOeste    = !this.paredeOeste;
                break;
            case Labirinto.L:
                this.paredeLeste    = !this.paredeLeste;
                break;
        }
    }
  

    /** 
     * Verifica se existe a parede Leste.
     * @return True se a parede existir.
     */
    public boolean isParedeLeste() {
        return paredeLeste;
    }

    /** 
     * Verifica se existe a parede Norte.
     * @return True se a parede existir.
     */
    public boolean isParedeNorte() {
        return paredeNorte;
    }

    /** 
     * Verifica se existe a parede Oeste.
     * @return True se a parede existir.
     */
    public boolean isParedeOeste() {
        return paredeOeste;
    }

    /** 
     * Verifica se existe a parede Sul.
     * @return True se a parede existir.
     */
    public boolean isParedeSul() {
        return paredeSul;
    }
    
    /**
     * Verifica se a célula X, Y está fechada, ou seja, todas as 
     * paredes são verdadeiras.
     * @return True se todas as paredes estão construídas.
     */
    public boolean isFechada(){
    	return this.isParedeNorte() && this.isParedeSul() &&
    		   this.isParedeOeste() && this.isParedeLeste(); 
    }
}

