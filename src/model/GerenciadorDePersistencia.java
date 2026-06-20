package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import Interfaces.ICarta;

class GerenciadorDePersistencia {

    public void salvar(GerenciadorDePartida partida,String caminhoArquivo) throws IOException {
    	 Properties propriedades = new Properties();

         propriedades.setProperty("valorDados", String.valueOf(partida.getGerDados().getValorDados()));
         propriedades.setProperty("ultimoDado1", String.valueOf(partida.getGerDados().getUltimoDado1()));
         propriedades.setProperty("ultimoDado2", String.valueOf(partida.getGerDados().getUltimoDado2()));

         Jogador jogadorAtual = partida.getGerJogadores().getJogadorAtual();
         propriedades.setProperty("jogadorAtual", jogadorAtual != null ? jogadorAtual.getPersonagem().getNome() : "");

         String[] envelope = partida.getGerCartas().exportarEnvelope();
         propriedades.setProperty("envelope.assassino", envelope[0] != null ? envelope[0] : "");
         propriedades.setProperty("envelope.arma", envelope[1] != null ? envelope[1] : "");
         propriedades.setProperty("envelope.local", envelope[2] != null ? envelope[2] : "");

         List<Jogador> jogadores = partida.getGerJogadores().getJogadores();
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

    public void carregar( GerenciadorDePartida partida,String caminhoArquivo) throws IOException {
    	Properties propriedades = new Properties();

        try (Reader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            propriedades.load(reader);
        }

        partida.reiniciarPartida();
        
        int dado1 = Integer.parseInt(propriedades.getProperty("ultimoDado1", "0"));
        int dado2 = Integer.parseInt(propriedades.getProperty("ultimoDado2", "0"));
        
        partida.getGerDados().definirResultadoDados(dado1, dado2);
        partida.getGerTabuleiro().iniciarTabuleiro();
        partida.getGerCartas().definirEnvelope(
            propriedades.getProperty("envelope.assassino", ""),
            propriedades.getProperty("envelope.arma", ""),
            propriedades.getProperty("envelope.local", "")
        );

        int quantidadeJogadores = Integer.parseInt(propriedades.getProperty("jogadores.qtd", "0"));
        for (int i = 0; i < quantidadeJogadores; i++) {
            String nome = propriedades.getProperty(chaveJogador(i, "nome"), "");
            String personagem = propriedades.getProperty(chaveJogador(i, "personagem"), "");
            PecaSuspeito suspeito = partida.getGerTabuleiro().buscarSuspeito(personagem);

            partida.getGerJogadores().adicionarJogador(nome,suspeito);

            Jogador jogador = partida.getGerJogadores().getJogadores().get(i);
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
                partida.getGerTabuleiro().reposicionarJogador(jogador, x, y);
            }
        }

        String nomeJogadorAtual = propriedades.getProperty("jogadorAtual", "");
        Jogador jogadorAtual = partida.getGerJogadores().buscarJogadorPorNome(nomeJogadorAtual);
        if (jogadorAtual != null) {
            partida.getGerJogadores().definirJogadorAtual(jogadorAtual);
        } else {
            partida.getGerJogadores().definirPrimeiroJogador();
        }

        partida.getGerObservador().notificar();
        
    }
    
    

	private String chaveJogador(int indice, String sufixo) {
		return "jogadores." + indice + "." + sufixo;
    }
}