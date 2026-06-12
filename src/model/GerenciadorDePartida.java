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
    
    private List<Dado> dados;
    private List<Observador> observadores;
    private int ultimoDado1;
    private int ultimoDado2;
    private int valorDados;
    
    private GerenciadorDePartida() {
        this.gerJogadores = new GerenciadorDeJogadores();
        this.gerCartas = new GerenciadorDeCartas();
        this.gerTabuleiro = new GerenciadorDeTabuleiro();
        
    	this.dados = new ArrayList<>();
	    this.observadores = new ArrayList<>();
    }

	private void inicializarObservadoresSeNecessario() {
		if (observadores == null) {
			observadores = new ArrayList<>();
		}
	}

    public void reiniciarPartida() {
        this.gerJogadores = new GerenciadorDeJogadores();
        this.gerCartas = new GerenciadorDeCartas();
        this.gerTabuleiro = new GerenciadorDeTabuleiro();
        this.dados = new ArrayList<>();
        this.ultimoDado1 = 0;
        this.ultimoDado2 = 0;
        this.valorDados = 0;
		this.observadores = new ArrayList<>();
    }
    
    public static synchronized GerenciadorDePartida getInstance() {
         if (instancia == null) instancia = new GerenciadorDePartida();
        return instancia; 
    }

    public void iniciarPartida() {
        // 1. Criar Baralho
        // 2. Sortear cartas do Envelope (Crime)
    	gerCartas.iniciarCartas();
    	
        // 3. Distribuir restante para jogadores
    	gerCartas.distribuiCartas(gerJogadores.getJogadores());
    	
        // 4. Posicionar peças no tabuleiro
    	gerTabuleiro.iniciarTabuleiro();
    	
        // evita duplicar dados se iniciarPartida() for chamado várias vezes
        if (dados.isEmpty()) {
            dados.add(new Dado());
            dados.add(new Dado());
        }
        
        gerTabuleiro.posicionarPecas(gerJogadores.getJogadores());
        
        gerJogadores.distribuirBlocoDeNotas();
        gerJogadores.definirPrimeiroJogador();

        notificarObservadores();
    }
    
    public void adicionarJogador(String nome, String nomePersonagem) {
    	gerJogadores.adicionarJogador(nome, nomePersonagem);
    }
    
    public int[] lancarDados() {
        ultimoDado1 = dados.get(0).rolar();
        ultimoDado2 = dados.get(1).rolar();

	    valorDados = ultimoDado1 + ultimoDado2;

	    notificarObservadores();
    	
        // devolve os dois valores separados para a View poder desenhar as imagens dos dados
        return new int[]{ultimoDado1, ultimoDado2}; 
    }

    public void definirResultadoDados(int dado1, int dado2) {
        this.ultimoDado1 = dado1;
        this.ultimoDado2 = dado2;
        this.valorDados = dado1 + dado2;
        notificarObservadores();
    }

    public void zerarDadosRolados() {
        this.ultimoDado1 = 0;
        this.ultimoDado2 = 0;
        this.valorDados = 0;
        notificarObservadores();
    }
     
    public void proximoTurno() {
    	gerJogadores.proximoTurno();
	    notificarObservadores();
    }

    public ICarta realizarPalpite(String suspeito, String arma, String comodo) {
        // puxar o suspeito para o comodo
    	gerTabuleiro.puxarSuspeitoParaComodo(suspeito, gerJogadores.getJogadorAtual(), gerJogadores.getJogadores());
    	
    	// notifica view - desenhar peao no outro comodo
    	notificarObservadores();
    	
    	// roda a mesa para mostrar a carta palpitada
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

    public boolean realizarAcusacao(ICarta suspeito, ICarta arma, ICarta comodo) {
        return gerCartas.realizarAcusacao(suspeito, arma, comodo);
    }
    
    /*______________________________________________________*/
    
    // método chamado pela interface gráfica
 // método chamado pela interface gráfica
    public int processaClickTela(
        int xLogico,
        int yLogico,
        int valorDados
    ) {

        int status =
            gerTabuleiro.processaClickTelaInterno(
                gerJogadores.getJogadorAtual(),
                xLogico,
                yLogico,
                valorDados
            );

        if (status == 1) {

            notificarObservadores();

            return 1; // entrou no cômodo
        }

        else if (status == 2) {

            proximoTurno();

            System.out.println(
                "turno muda para: " +
                gerJogadores.getJogadorAtual()
                    .getPersonagem()
                    .getNome()
            );

            return 2; // moveu normal
        }

        return 0; // inválido
    }
    
    // Passagem secreta
    public boolean usaPassagemSecreta() {
	    boolean sucesso = gerTabuleiro.usaPassagemSecreta(gerJogadores.getJogadorAtual());
	    if (sucesso) {
	        notificarObservadores();
	    }
	    return sucesso;
    }

    public void registrarObservador(Observador observador) {
        inicializarObservadoresSeNecessario();
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void removerObservador(Observador observador) {
        observadores.remove(observador);
    }

    private void notificarObservadores() {
        inicializarObservadoresSeNecessario();
        for (Observador observador : new ArrayList<>(observadores)) {
            observador.atualizar();
        }
    }

    public void salvarPartida(String caminhoArquivo) throws IOException {
        Properties propriedades = new Properties();

        propriedades.setProperty("valorDados", String.valueOf(valorDados));
        propriedades.setProperty("ultimoDado1", String.valueOf(ultimoDado1));
        propriedades.setProperty("ultimoDado2", String.valueOf(ultimoDado2));

        Jogador jogadorAtual = gerJogadores.getJogadorAtual();
        propriedades.setProperty("jogadorAtual", jogadorAtual != null ? jogadorAtual.getPersonagem().getNome() : "");

        String[] envelope = gerCartas.exportarEnvelope();
        propriedades.setProperty("envelope.assassino", envelope[0] != null ? envelope[0] : "");
        propriedades.setProperty("envelope.arma", envelope[1] != null ? envelope[1] : "");
        propriedades.setProperty("envelope.local", envelope[2] != null ? envelope[2] : "");

        List<Jogador> jogadores = gerJogadores.getJogadores();
        propriedades.setProperty("jogadores.qtd", String.valueOf(jogadores.size()));

        for (int i = 0; i < jogadores.size(); i++) {
            Jogador jogador = jogadores.get(i);
            propriedades.setProperty(chaveJogador(i, "nome"), jogador.getNome());
            propriedades.setProperty(chaveJogador(i, "personagem"), jogador.getPersonagem().getNome());
            propriedades.setProperty(chaveJogador(i, "eliminado"), String.valueOf(jogador.isEliminado()));
            propriedades.setProperty(chaveJogador(i, "blocoNotas"), String.valueOf(jogador.isPossuiBlocoDeNotas()));

            Casa posicao = jogador.getPersonagem().getPosicaoAtual();
            propriedades.setProperty(chaveJogador(i, "posicao"), posicao != null ? posicao.getX() + "," + posicao.getY() : "");

            java.util.List<ICarta> mao = jogador.getMao();
            propriedades.setProperty(chaveJogador(i, "mao.qtd"), String.valueOf(mao.size()));
            for (int j = 0; j < mao.size(); j++) {
                ICarta carta = mao.get(j);
                propriedades.setProperty(chaveJogador(i, "mao." + j + ".nome"), carta.getNome());
                propriedades.setProperty(chaveJogador(i, "mao." + j + ".tipo"), carta.getTipo().name());
            }
            
            Map<String, Boolean> marcas = jogador.getBlocoDeNotasInterno().getTodasMarcas();

            propriedades.setProperty(chaveJogador(i, "bloco.qtd"), String.valueOf(marcas.size()));
            
            int indiceMarca = 0;
            	
            	for (Map.Entry<String, Boolean> entry : marcas.entrySet()) {
            	    propriedades.setProperty(chaveJogador(i, "bloco." + indiceMarca + ".carta"), entry.getKey());
            	    propriedades.setProperty(chaveJogador(i, "bloco." + indiceMarca + ".valor"), String.valueOf(entry.getValue()));

            	    indiceMarca++;
            	}
        }
        
        

        try (Writer writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            propriedades.store(writer, "Partida salva");
        }
    }

    public void carregarPartida(String caminhoArquivo) throws IOException {
        Properties propriedades = new Properties();

        try (Reader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            propriedades.load(reader);
        }

        this.gerJogadores = new GerenciadorDeJogadores();
        this.gerCartas = new GerenciadorDeCartas();
        this.gerTabuleiro = new GerenciadorDeTabuleiro();
        this.dados = new ArrayList<>();

        if (dados.isEmpty()) {
            dados.add(new Dado());
            dados.add(new Dado());
        }

        this.valorDados = Integer.parseInt(propriedades.getProperty("valorDados", "0"));
        this.ultimoDado1 = Integer.parseInt(propriedades.getProperty("ultimoDado1", "0"));
        this.ultimoDado2 = Integer.parseInt(propriedades.getProperty("ultimoDado2", "0"));

        gerTabuleiro.iniciarTabuleiro();
        gerCartas.definirEnvelope(
            propriedades.getProperty("envelope.assassino", ""),
            propriedades.getProperty("envelope.arma", ""),
            propriedades.getProperty("envelope.local", "")
        );

        int quantidadeJogadores = Integer.parseInt(propriedades.getProperty("jogadores.qtd", "0"));
        for (int i = 0; i < quantidadeJogadores; i++) {
            String nome = propriedades.getProperty(chaveJogador(i, "nome"), "");
            String personagem = propriedades.getProperty(chaveJogador(i, "personagem"), "");
            gerJogadores.adicionarJogador(nome, personagem);

            Jogador jogador = gerJogadores.getJogadores().get(i);
            jogador.setEliminado(Boolean.parseBoolean(propriedades.getProperty(chaveJogador(i, "eliminado"), "false")));
            jogador.setPossuiBlocoDeNotas(Boolean.parseBoolean(propriedades.getProperty(chaveJogador(i, "blocoNotas"), "false")));

            int quantidadeCartas = Integer.parseInt(propriedades.getProperty(chaveJogador(i, "mao.qtd"), "0"));
            int quantidadeMarcas = Integer.parseInt( propriedades.getProperty( chaveJogador(i, "bloco.qtd"),"0"));

        	for (int j = 0; j < quantidadeMarcas; j++) {
        	    String nomeCarta = propriedades.getProperty(chaveJogador(i, "bloco." + j + ".carta"),"");
        	    boolean valor = Boolean.parseBoolean(propriedades.getProperty( chaveJogador(i, "bloco." + j + ".valor"), "false" ));
        	    jogador.getBlocoDeNotasInterno().marcar(nomeCarta, valor);
        	}
            	
            for (int j = 0; j < quantidadeCartas; j++) {
                String nomeCarta = propriedades.getProperty(chaveJogador(i, "mao." + j + ".nome"), "");
                String tipoCarta = propriedades.getProperty(chaveJogador(i, "mao." + j + ".tipo"), TipoCarta.SUSPEITO.name());
                jogador.recebeCartas(new Carta(nomeCarta, TipoCarta.valueOf(tipoCarta)));
            }

            String posicao = propriedades.getProperty(chaveJogador(i, "posicao"), "");
            if (!posicao.isEmpty()) {
                String[] partes = posicao.split(",");
                int x = Integer.parseInt(partes[0]);
                int y = Integer.parseInt(partes[1]);
                gerTabuleiro.reposicionarJogador(jogador, x, y);
            }
        }

        String nomeJogadorAtual = propriedades.getProperty("jogadorAtual", "");
        Jogador jogadorAtual = gerJogadores.buscarJogadorPorNome(nomeJogadorAtual);
        if (jogadorAtual != null) {
            gerJogadores.definirJogadorAtual(jogadorAtual);
        } else {
            gerJogadores.definirPrimeiroJogador();
        }

        notificarObservadores();
    }

    
    public java.util.List<String> getTodosNomesCartas() {
        return gerCartas.getTodosNomesCartas();
    }

    public java.util.Map<String, Boolean> obterBlocoJogadorAtual() {
        Jogador j = gerJogadores.getJogadorAtual();
        if (j == null) return new java.util.HashMap<>();
        return j.getBlocoDeNotasInterno().getTodasMarcas();
    }

    public void marcarCartaNoBlocoJogadorAtual(String nomeCarta, boolean valor) {
        Jogador j = gerJogadores.getJogadorAtual();
        if (j == null) return;
        j.getBlocoDeNotasInterno().marcar(nomeCarta, valor);
    }

    public void garantirBlocoParaJogadorAtual() {
        Jogador j = gerJogadores.getJogadorAtual();
        if (j == null) return;
        j.receberBlocoDeNotas();
    }

	private String chaveJogador(int indice, String sufixo) {
		return "jogadores." + indice + "." + sufixo;
    }
    
    // get e set
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

    public int getValorDados() {
        return valorDados;
    }
    
    public List<ICarta> getCartasJogadorAtual() {
        return gerJogadores.getCartasJogadorAtual();
    }

    public String getIdentificadorVisual(ICarta carta) {
        return gerCartas.getIdentificadorVisual(carta);
    }

    public int getUltimoDado1() {
        return ultimoDado1;
    }

    public int getUltimoDado2() {
        return ultimoDado2;
    }
}

