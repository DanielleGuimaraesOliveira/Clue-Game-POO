package model;
import java.io.*;
import java.util.*;

import Interfaces.ICarta;
import Interfaces.ICasa;
import Interfaces.IJogador;

public class GerenciadorDePartida {
    private static GerenciadorDePartida instancia;
    
    // Sub-gerenciadores completamente desacoplados entre si
    private GerenciadorDeJogadores gerJogadores;
    private GerenciadorDeCartas gerCartas;
    private GerenciadorDeTabuleiro gerTabuleiro;
    private GerenciadorDeObservadores gerObservadores;
    private GerenciadorDeDados gerDados;
    private GerenciadorDePersistencia gerPersistencia;
    
    private GerenciadorDePartida() {
        this.gerJogadores = new GerenciadorDeJogadores();
        this.gerCartas = new GerenciadorDeCartas();
        this.gerTabuleiro = new GerenciadorDeTabuleiro();
        this.gerDados = new GerenciadorDeDados();
    	this.gerObservadores = new GerenciadorDeObservadores();
    	this.gerPersistencia = new GerenciadorDePersistencia();
    }
    
    public static synchronized GerenciadorDePartida getInstance() {
    	if (instancia == null) instancia = new GerenciadorDePartida();
        return instancia; 
    }
    
    public void iniciarPartida() {
        gerCartas.iniciarCartas();
        gerCartas.distribuiCartas(gerJogadores.getJogadores());
        gerJogadores.adicionarNPCsAusentes(gerTabuleiro);
        gerTabuleiro.iniciarTabuleiro();
        gerTabuleiro.posicionarSuspeitos();
        gerJogadores.distribuirBlocoDeNotas();
        gerJogadores.definirPrimeiroJogador();
        gerObservadores.notificar();
    }

    public void reiniciarPartida() {
        this.gerJogadores = new GerenciadorDeJogadores();
        this.gerCartas = new GerenciadorDeCartas();
        this.gerTabuleiro = new GerenciadorDeTabuleiro();
        this.gerDados = new GerenciadorDeDados();
    }
    
    public void adicionarJogador(String nome, String nomePersonagem) {
        PecaSuspeito suspeito = gerTabuleiro.buscarSuspeito(nomePersonagem);
        gerJogadores.adicionarJogador(nome, suspeito);
    }
    
    public int[] lancarDados() {
    	if (gerJogadores.getJogadorAtual().isEliminado()) {
            return new int[]{0, 0}; 
        }
    	
    	int[] resultado = gerDados.lancarDados();

	    gerObservadores.notificar();    	
	    return resultado;
    }

    public void definirResultadoDados(int dado1, int dado2) {
    	gerDados.definirResultadoDados(dado1, dado2);
        gerObservadores.notificar();
    }

    public void zerarDadosRolados() {
    	gerDados.zerarDados();
        gerObservadores.notificar();
    }
     
    public void proximoTurno() {
    	gerJogadores.proximoTurno();
    	gerObservadores.notificar();
    }

    public ICarta realizarPalpite(String suspeito, String arma, String comodo) {
    	gerTabuleiro.puxarSuspeitoParaComodo(suspeito, gerJogadores.getJogadorAtual(), gerJogadores.getJogadores());
    	
    	gerObservadores.notificar();
    	
    	return gerCartas.responderPalpite(gerJogadores.getJogadorAtual(), gerJogadores.getJogadores(), suspeito, arma, comodo);
    }
    
    public List<ICasa> mapearCasas(int passos) {
        List<ICasa> resultados = new ArrayList<>();
        
        for (Casa c : gerTabuleiro.mapearCasas(gerJogadores.getJogadorAtual(), passos)) {
            resultados.add(c);
        }
        
        return resultados;
    }
    
    public List<String> mapearCasaFormatadas(int passos){
    	return gerTabuleiro.mapearCasaFormatadas(gerJogadores.getJogadorAtual(), passos);
    }
    
    public void deslocarPiao(String destino) {
        gerTabuleiro.deslocarPiao(gerJogadores.getJogadorAtual(), destino);
    }
    
    public String getPosicaoAtualFormatada() {
        return gerTabuleiro.getPosicaoAtualFormatada(gerJogadores.getJogadorAtual());
    }

    public boolean realizarAcusacao(String suspeito, String arma, String comodo) {
        boolean acertou = gerCartas.realizarAcusacao(suspeito, arma, comodo);
        
        //  se errou, elimina
        if (!acertou) {
            Jogador jogadorDaVez = gerJogadores.getJogadorAtual(); 
            jogadorDaVez.setEliminado(true);
            System.out.println(jogadorDaVez.getPersonagem().getNome() + " errou a acusação e foi eliminado!");
        
            // cemitério do jogador
            gerTabuleiro.moverParaCentroDoTabuleiro(jogadorDaVez.getPersonagem());
            
            gerObservadores.notificar();
            
        }
        
        return acertou;
    }
    
    public boolean todosEliminados() {
    	for (Jogador  j: gerJogadores.getJogadores()) {
    		if (!j.isEliminado()) {
    			return false; // se pelo menos um estiver vivo, continua
    		}
    	}
    	return true;
    }
    
    public List<Object> getTodasAsPecas(){
    	List<Object> pecas = new ArrayList<>();
    	
    	for (Object peca : gerTabuleiro.getTodasAsPecas()) {
    		pecas.add(peca);
    	}
    	
    	return pecas;
    }
    
    public int processaClickTela(int xLogico, int yLogico, int valorDados) {
    	if (gerJogadores.getJogadorAtual().isEliminado()) {
            return 0;
        }
    	
        int status = gerTabuleiro.processaClickTelaInterno(gerJogadores.getJogadorAtual(), xLogico, yLogico, valorDados );

        if (status == 1) {
        	gerObservadores.notificar();

            return 1; // entrou no cômodo
        }

        else if (status == 2) {
            proximoTurno();

            System.out.println("turno muda para: " + gerJogadores.getJogadorAtual().getPersonagem().getNome());
            return 2; // moveu normal
        }

        return 0; // inválido
    }

    public boolean usaPassagemSecreta() {
	    boolean sucesso = gerTabuleiro.usaPassagemSecreta(gerJogadores.getJogadorAtual());
	    if (sucesso) {
	    	gerObservadores.notificar();
	    }
	    return sucesso;
    }

    public void registrarObservador(Observador observador) {
    	gerObservadores.registrar(observador);
    }

    public void removerObservador(Observador observador) {
    	gerObservadores.remover(observador);
    }

    public void salvarPartida(String caminhoArquivo) throws IOException {
        gerPersistencia.salvar(this, caminhoArquivo);
    }

    public void carregarPartida(String caminhoArquivo) throws IOException {
        gerPersistencia.carregar(this, caminhoArquivo);

        gerObservadores.notificar();
    }

    
    public java.util.List<String> getTodosNomesCartas() {
        return gerCartas.getTodosNomesCartas();
    }

    public Map<String, Boolean> obterBlocoJogadorAtual() {
        return gerJogadores.obterBlocoJogadorAtual();
    }

    public void marcarCartaNoBlocoJogadorAtual(String nomeCarta, boolean valor) {
        gerJogadores.marcarCartaNoBlocoJogadorAtual(nomeCarta, valor);
    }

    public void garantirBlocoParaJogadorAtual() {
        gerJogadores.garantirBlocoParaJogadorAtual();
    }

    public IJogador getJogadorAtual() {
    	return gerJogadores.getJogadorAtual();
    }
    
    public String getComodoJogadorAtual() {
    	Jogador jogador = gerJogadores.getJogadorAtual();
    	return gerTabuleiro.getComodoAtual(jogador);
    }
    
    public List<IJogador> getJogadores(){
    	List<IJogador> resultados = new ArrayList<>();
    	for (Jogador j : gerJogadores.getJogadores()) resultados.add(j);
    	return resultados;
    }
    public List<ICarta> getCartasJogadorAtual() {
        return gerJogadores.getCartasJogadorAtual();
    }

    public String getIdentificadorVisual(ICarta carta) {
        return gerCartas.getIdentificadorVisual(carta);
    }

    public int getValorDados() {
        return gerDados.getValorDados();
    }

    public int getUltimoDado1() {
        return gerDados.getUltimoDado1();
    }

    public int getUltimoDado2() {
        return gerDados.getUltimoDado2();
    }

    public String[] revelarEnvelope() {
        return gerCartas.exportarEnvelope();
    }
    
    GerenciadorDeJogadores getGerJogadores() {
        return gerJogadores;
    }

    GerenciadorDeCartas getGerCartas() {
        return gerCartas;
    }

    GerenciadorDeTabuleiro getGerTabuleiro() {
        return gerTabuleiro;
    }
    
    GerenciadorDeObservadores getGerObservador() {
        return gerObservadores;
    }

    GerenciadorDeDados getGerDados() {
        return gerDados;
    }
    
}

