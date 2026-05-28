package model;
import java.util.*;

public class GerenciadorDeTabuleiro {
    private Tabuleiro tabuleiro;

    public void iniciarTabuleiro() {
        tabuleiro = new Tabuleiro();
    }

    public void posicionarPecas(List<Jogador> jogadores) {
    	for (Jogador j : jogadores) {
    		boolean posicionado = false;
    		
    		// O algoritmo varre o tabuleiro procurando um lugar seguro para nascer
            for (int y = 0; y < 25 && !posicionado; y++) {
                for (int x = 0; x < 24 && !posicionado; x++) {
                    
                    Casa casaTeste = tabuleiro.getCasa(x, y);
                    
                    // Se a casa existe, é um corredor ("1") e ninguém pisou nela ainda:
                    if (casaTeste != null && casaTeste.getTipo().equals("1") && !casaTeste.estaOcupada()) {
                        
                        // Posiciona a peça!
                        tabuleiro.moverPeca(j.getPersonagem(), casaTeste);
                        posicionado = true; // Para o loop e vai para o próximo jogador
                        
                    }
                }
            }
    	}
    }

    public List<Casa> mapearCasas(Jogador jogadorAtual, int passos) {
    	if (jogadorAtual == null) {
    		return new ArrayList<>();
    	}
  
    	Casa origem = jogadorAtual.getPersonagem().getPosicaoAtual();
    	
    	// se a origem NÃO é corredor ("1") e NÃO é porta ("p"), então ele está DENTRO de algum cômodo!
    	if ( !origem.getTipo().equals("1") && !origem.getTipo().equals("p")) {
    		
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
    	if (casasPossiveis.contains(destino) && destino.isCaminhavel()) {
   
			// move a peça para  corredor ou porta	
			tabuleiro.moverPeca(jogadorAtual.getPersonagem(), destino);
			System.out.println("Movimento para: " + xLogico + ", " + yLogico + " - tipo: " + destino.getTipo());
    		
			if (destino.getTipo().equals("p")){
				// em vez de parar na porta, entra no comodo
				Casa lugarNoComodo = encontraLugarComodoPorPorta(destino);
				
				if (lugarNoComodo != null) {
					tabuleiro.moverPeca(jogadorAtual.getPersonagem(), lugarNoComodo);
				}
				else {
					tabuleiro.moverPeca(jogadorAtual.getPersonagem(), destino); // Fica na porta só se a sala lotar
				}
				
				System.out.println("🚪 " + jogadorAtual.getPersonagem().getNome() + " entrou no cômodo!");
                
				// o turno só passa quando fecha a janela de palpite
				return 1;
			}
			else {
				// Tabuleiro não muda o turno, apenas avisa a fachada que moveu
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
    
    /*
    private String descobreComodo(model.Casa casa) {
        
    	// se está na porta, olhamos os vizinhos dessa porta para ver a letra do comodo
        if (casa.getTipo().equalsIgnoreCase("p")) {
            
        	for (model.Casa vizinho : tabuleiro.getVizinhos(casa)) {
                String c = vizinho.getTipo();
               
                if (c.equals("c") || c.equals("o") || c.equals("w") || c.equals("l")) {
                    return c; //acho o cômodo que a porta entra!
                }
            }
        } 
        else {
             // se já está dentro do cômodo
             String c = casa.getTipo();
             if (c.equals("c") || c.equals("o") || c.equals("w") || c.equals("l")) {
                 return c;
             }
        }
        return "Nenhum";
    }
    */

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
}