package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Interfaces.ICarta;
import Interfaces.ICasa;
import Interfaces.IJogador;
import model.GerenciadorDePartida;
import model.Observador;

public class ControladorPartida {
    private GerenciadorDePartida gerenciador;
    private java.util.List<model.Observador> observadores = new java.util.ArrayList<>();

    public ControladorPartida(GerenciadorDePartida gerenciador) {
        this.gerenciador = gerenciador;
        
        this.gerenciador.registrarObservador(new model.Observador() {
            @Override
            public void atualizar() {
        
                for (model.Observador o : new java.util.ArrayList<>(observadores)) {
                    o.atualizar();
                }
            }
        });
    }

    public void iniciarPartida() {
        gerenciador.iniciarPartida();
    }

    public void adicionarJogador(String nome, String personagem) {
        gerenciador.adicionarJogador(nome, personagem);
    }

    public int[] lancarDados() {
        return gerenciador.lancarDados();
    }

    public void definirResultadoDados(int d1, int d2) {
        gerenciador.definirResultadoDados(d1, d2);
    }

    public void zerarDadosRolados() {
        gerenciador.zerarDadosRolados();
    }

    public void proximoTurno() {
        gerenciador.proximoTurno();
    }

    public boolean processaClickTela(int xLogico, int yLogico, int valorDados) {
        return gerenciador.processaClickTela(xLogico, yLogico, valorDados);
    }

    public boolean usaPassagemSecreta() {
        return gerenciador.usaPassagemSecreta();
    }

    public void registrarObservador(Observador observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void removerObservador(Observador observador) {
        observadores.remove(observador);
    }

    public int getValorDados() {
        return gerenciador.getValorDados();
    }

    public IJogador getJogadorAtual() {
        return gerenciador.getJogadorAtual();
    }

    public List<IJogador> getJogadores() {
        List<IJogador> resultados = new ArrayList<>();
        for (Object o : gerenciador.getJogadores()) {
            resultados.add((IJogador) o);
        }
        return resultados;
    }

    public List<ICasa> mapearCasas(int passos) {
        List<ICasa> resultados = new ArrayList<>();
        for (Object o : gerenciador.mapearCasas(passos)) {
            resultados.add((ICasa) o);
        }
        return resultados;
    }

    public List<String> mapearCasaFormatadas(int passos){
        return gerenciador.mapearCasaFormatadas(passos);
    }

    public void deslocarPiao(String destino) {
        gerenciador.deslocarPiao(destino);
    }

    public String realizarPalpite(String suspeito, String arma, String comodo) {
        return gerenciador.realizarPalpite(suspeito, arma, comodo);
    }

    public void salvarPartida(String caminhoArquivo) throws IOException {
        gerenciador.salvarPartida(caminhoArquivo);
    }

    public void carregarPartida(String caminhoArquivo) throws IOException {
        gerenciador.carregarPartida(caminhoArquivo);
    }

    public java.util.List<String> getTodosNomesCartas() {
        return gerenciador.getTodosNomesCartas();
    }

    public java.util.Map<String, Boolean> obterBlocoJogadorAtual() {
        return gerenciador.obterBlocoJogadorAtual();
    }

    public void marcarCartaNoBlocoAtual(String nomeCarta, boolean valor) {
        gerenciador.marcarCartaNoBlocoJogadorAtual(nomeCarta, valor);
    }

    public void garantirBlocoParaJogadorAtual() {
        gerenciador.garantirBlocoParaJogadorAtual();
    }
    
    public List<ICarta> getCartasJogadorAtual() {
        return gerenciador.getCartasJogadorAtual();
    }
    
    public String getIdentificadorVisual(ICarta carta) {
        return gerenciador.getIdentificadorVisual(carta);
    }
    
    
}

