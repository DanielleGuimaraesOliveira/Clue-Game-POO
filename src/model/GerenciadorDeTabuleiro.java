package model;
import java.util.*;

public class GerenciadorDeTabuleiro {
    private Tabuleiro tabuleiro;

    public void iniciarTabuleiro() {
        tabuleiro = new Tabuleiro();
    }
    public void posicionarPecas(List<Jogador> jogadores) {

        for (Jogador j : jogadores) {

            String nome = j.getPersonagem().getNome();

            Casa destino = null;

            switch (nome) {

                case "Srta. Scarlet":
                    destino = tabuleiro.getCasa(7, 24);
                    break;

                case "Coronel Mustard":
                    destino = tabuleiro.getCasa(0, 17);
                    break;

                case "Sra. White":
                    destino = tabuleiro.getCasa(9, 0);
                    break;

                case "Rev. Green":
                    destino = tabuleiro.getCasa(14, 0);
                    break;

                case "Sra. Peacock":
                    destino = tabuleiro.getCasa(23, 6);
                    break;

                case "Prof. Plum":
                    destino = tabuleiro.getCasa(23, 19);
                    break;
            }

            if (destino != null) {

                tabuleiro.moverPeca(
                    j.getPersonagem(),
                    destino
                );
            }
        }
    }

    public String getComodoAtual(Jogador jogadorAtual) {
    	String letraComodo =  jogadorAtual.getPosicaoAtual().getTipo();
    	// ADICIONE ESTA LINHA PARA DESCOBRIR A LETRA:
        System.out.println("DEBUG - Letra lida do mapa: [" + letraComodo + "]");
    	switch (letraComodo) {
    	/*
		String[] comodos = {
			"Cozinha",
			"Salão de Baile", -> de jogos
			"Sala de Jantar",
			"Escritório",
			"Biblioteca",
			"Sala de Estar",
			"Jardim de Inverno",
			"Hall",
			"Sala de Música"
		};*/
    	
    		case "c":
    			return "Cozinha";
    		case "m":
    			return "Sala de Musica";
    		case "w":
    			return "Jardim de Inverno";
    		case "j":
    			return "Sala de Jantar";
    		case "g":
    			return "Sala de Jogos";
    		case "b":
    			return "Biblioteca";
    		case "l":
    			return "Sala de Estar";
    		case "e":
    			return "Entrada";
    		case "o":
    			return "Escritorio";
    			
    		default:
    			return "Corredor";
    	}
    }
    
    public List<Casa> mapearCasas(Jogador jogadorAtual, int passos) {
    	if (jogadorAtual == null) {
    		return new ArrayList<>();
    	}
  
    	Casa origem = jogadorAtual.getPersonagem().getPosicaoAtual();
    	
    	// se a origem NÃO é corredor ("1") e NÃO é porta ("p"), então ele está DENTRO de algum cômodo!
    	if ( !origem.getTipo().equals("1") && !origem.getTipo().equals("p") && !origem.getTipo().equals("0")) {
    		
    		// acha todas as portas do comodo
    		List<Casa> portas = encontraTodasPortasComodo(origem.getTipo());
    		
    		Set<Casa> caminhosTotais = new HashSet<>();
    		
    		for (Casa porta : portas) {
    			// REGRA OFICIAL: Só pode sair por essa porta se ela NÃO estiver bloqueada por alguém!
                if (!porta.estaOcupada()) {
                    // O DFS calcula a partir desta porta (gastando 1 passo)
                    List<model.Casa> caminhosDestaPorta = tabuleiro.calculaCaminhosValidos(porta, passos - 1);
                    caminhosTotais.addAll(caminhosDestaPorta);
                }
    		}
    		
    		// Remove da lista todas as casas que pertençam ao mesmo cômodo que o jogador iniciou o turno
    		caminhosTotais.removeIf(casa -> casa.getTipo().equalsIgnoreCase(origem.getTipo()));
    		
    		if (caminhosTotais.isEmpty()) {
    			// jogador preço, propria casa atual como opção
    			caminhosTotais.add(origem);
    		}
    		return new ArrayList<> (caminhosTotais);
    	}
    	
    	// se já estava no corredor ou na porta, calcula normal
        return tabuleiro.calculaCaminhosValidos(origem, passos);
    }

    public List<String> mapearCasaFormatadas(Jogador jogadorAtual, int passos){
    	List<Casa> casasValidas = mapearCasas(jogadorAtual, passos);
    	
    	// converte para string no formato (x, y)
    	List<String> casasFormatadas = new ArrayList<>();
    	for (Casa c : casasValidas) {
    		casasFormatadas.add("(" + c.getX() + "," + c.getY() + ")");
    	}
    	
    	return casasFormatadas;
    }

    public void deslocarPiao(Jogador jogadorAtual, String destino) {
        Casa casaDestino = transformaStringParaCasa(destino);
        tabuleiro.moverPeca(jogadorAtual.getPersonagem(), casaDestino);
    }
    

	public void reposicionarJogador(Jogador jogadorAtual, int x, int y) {
		Casa destino = tabuleiro.getCasa(x, y);
		if (jogadorAtual != null && destino != null) {
			tabuleiro.moverPeca(jogadorAtual.getPersonagem(), destino);
		}
	}
	


    public String getPosicaoAtualFormatada(Jogador jogadorAtual) {
        // acessa o jogador e a peça
        Casa atual = jogadorAtual.getPersonagem().getPosicaoAtual();
        
        // retorna apenas a String formatada para main
        return "(" + atual.getX() + "," + atual.getY() + ")";
    }

    private Casa transformaStringParaCasa(String casa){
        casa = casa.replace("(", "").replace(")", "");

        String[] partes = casa.split(",");

        int x = Integer.parseInt(partes[0]);
        int y = Integer.parseInt(partes[1]);

        Casa casaFormatada = tabuleiro.getCasa(x, y);
        
        return casaFormatada;
    }

    // Retorna: 0 = inválido, 1 = entrou no cômodo (mantém turno), 2 = moveu normal (passa turno)
    public int processaClickTelaInterno(Jogador jogadorAtual, int xLogico, int yLogico, int valorDados) {
    	if (tabuleiro == null || jogadorAtual == null) {
    		return 0;
    	}
    	
    	// pega a casa do click 
    	Casa destino = tabuleiro.getCasa(xLogico, yLogico);
    	if (destino == null) {
    		return 0; // clicou fora do tabuleiro
    	}   	
    	    	
    	// chama o DFS para saber as casas que pode ir
    	List<Casa> casasPossiveis = mapearCasas(jogadorAtual, valorDados);
    	
    	// valida se o destino está na lista das casas permitidas 
    	if (casasPossiveis.contains(destino)) {
   
    		// Verifica se o destino é um cômodo (não é "1", nem "p", nem "0")
            boolean isComodo = !destino.getTipo().equals("1") && 
                               !destino.getTipo().equalsIgnoreCase("p") && 
                               !destino.getTipo().equals("0");
            
			// move a peça para o destino
			tabuleiro.moverPeca(jogadorAtual.getPersonagem(), destino);
			System.out.println("Movimento para: " + xLogico + ", " + yLogico + " - tipo: " + destino.getTipo());
			    		
			if (isComodo) {
				
				Casa lugarLivre = encontraCasaLivre(destino.getTipo());
                
                if (lugarLivre != null) {
                    tabuleiro.moverPeca(jogadorAtual.getPersonagem(), lugarLivre);
                    System.out.println("Movimento automático para o fundo do cômodo: " + destino.getTipo());
                } else {
                    // Fallback: Se o cômodo estiver incrivelmente lotado (raro), fica onde clicou
                    tabuleiro.moverPeca(jogadorAtual.getPersonagem(), destino);
                }
                
                System.out.println("🚪 " + jogadorAtual.getPersonagem().getNome() + " entrou no cômodo!");
                return 1; // Mantém o turno para a Janela de Palpite
			                
            } else {
                // Se for "1" ou "p", apenas andou ou parou na porta. Passa o turno.
                return 2; 
            }
			
    	}
    	
    	return 0; // click inválido
    }

    // Passagem secreta
    public boolean usaPassagemSecreta(Jogador jogadorAtual) {
    	if (jogadorAtual == null) {
    		return false;
    	}
    	
    	Casa posicaoAtual = jogadorAtual.getPersonagem().getPosicaoAtual();
    	
    	// verifica de qual lugar o jogador está
    	String comodoAtual = posicaoAtual.getTipo();
    	
    	if (comodoAtual.equals("1") || comodoAtual.equalsIgnoreCase("p")) {
    		return false; // não está dentro de um comodo
    	}
    	
    	model.Casa destino = null;
    	
    	// faz a ligação entre os cômodos
    	
    	// cozinha (c) <-> escritorio (o)
    	if (comodoAtual.equals("c")) {
    		destino = encontraCasaLivre("o");
    	}
    	else if( comodoAtual.equals("o")) {
    		destino = encontraCasaLivre("c");
    	}
    	
    	// Jardim Inverno (w) <-> sala de estar (l)
    	else if( comodoAtual.equals("w")) {
    		destino = encontraCasaLivre("l");
    	}
    	else if( comodoAtual.equals("l")) {
    		destino = encontraCasaLivre("w");
    	}
    	
    	// executa a ligação
    	if (destino != null) {
    		tabuleiro.moverPeca(jogadorAtual.getPersonagem(), destino);
    		System.out.println("Passagem secreta para o comodo: " + destino.getTipo());
    	
    		return true;
    	}
    	
    	return false;
    }

    // métodos auxiliares - passagem secreta
    private Casa encontraLugarComodoPorPorta(Casa porta) {
    	// olha para os vizinhos da porta para descobrir qual é a letra do comodo
    	for (Casa vizinho : tabuleiro.getVizinhos(porta)) {
            
    		String tipo = vizinho.getTipo();
            
    		if (!tipo.equals("1") && !tipo.equals("0") && !tipo.equalsIgnoreCase("P")) {
                return encontraCasaLivre(tipo);
            }
        }
        return null;
    }
    
    

    private Casa encontraCasaLivre(String tipoSala) {
        for (int y = 0; y < 25; y++) {
            for (int x = 0; x < 24; x++) {
                Casa c = tabuleiro.getCasa(x, y);
               
                // retorna o primeiro quadradinho daquela letra que não tenha ninguém em cima
                if (c != null && c.getTipo().equals(tipoSala) && !c.estaOcupada()) {
                    return c;
                }
            }
        }
        return null;
    }

    private List<Casa> encontraTodasPortasComodo(String letraComodo) {
    	
    	List<Casa> portas = new ArrayList<>();
    	
    	for (int y = 0; y < 25; y++) {
    		for (int x = 0; x < 24; x++) {
    			
    			Casa c = tabuleiro.getCasa(x, y);
    			if ( c != null && c.getTipo().equalsIgnoreCase("p")) {
    				
    				// verifica se essa porta está grudada no comodo que queremos
    				for ( Casa vizinho : tabuleiro.getVizinhos(c)) {
    					if (vizinho.getTipo().equals(letraComodo)) {
    						portas.add(c);
    						break; // se achou a porta, vai para a proxima casa
    					}
    					
    				}
    			}
    		}
    	}
    	
    	return portas;
    }
    
    public void puxarSuspeitoParaComodo(String nomeSuspeito, Jogador jogadorAtual, List<Jogador> todosJogadores) {
        // 1. Procurar quem é o jogador/peão correspondente ao suspeito do palpite
        Jogador jogadorAlvo = null;
        for (Jogador j : todosJogadores) {
            if (j.getPersonagem().getNome().equals(nomeSuspeito)) {
                jogadorAlvo = j;
                break;
            }
        }

        // Se encontrou o suspeito e ele não é o próprio jogador que está palpitando
        if (jogadorAlvo != null && !jogadorAlvo.equals(jogadorAtual)) {
            
            // 2. Descobre a letra do cômodo onde o palpite está acontecendo
            String letraComodo = jogadorAtual.getPersonagem().getPosicaoAtual().getTipo();
            
            // 3. Procura uma cadeira vazia naquele cômodo
            Casa destino = encontraCasaLivre(letraComodo);
            
            // 4. Se tiver espaço, puxa o suspeito para lá!
            if (destino != null) {
                tabuleiro.moverPeca(jogadorAlvo.getPersonagem(), destino);
                System.out.println("🚨 Suspeito " + nomeSuspeito + " foi puxado para o cômodo " + letraComodo);
            }
        }
    }
}