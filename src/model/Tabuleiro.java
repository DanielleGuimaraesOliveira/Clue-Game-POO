package model;
import java.util.*;
import java.io.InputStream;


class Tabuleiro {
	// private Map<String, Casa> grid;
	
	private Casa[][] grid; // voltando para a matriz
	private int largura = 24;
	private int altura = 25;
	
	private List<Comodo> comodos;

	public Tabuleiro() {
		this.grid = new Casa[altura][largura]; // inicializando o mapa	
		carregarMapaTxt();
	}
	
	private void carregarMapaTxt() {
		
		try {
			// lendo a orquivo do pacote assets
			InputStream leitorMapa = getClass().getResourceAsStream("/assets/mapa.txt");
			Scanner leitor = new Scanner(leitorMapa);
			
			int y = 0; //linha
			while (leitor.hasNextLine() && y < altura) {
				String linhaTxt = leitor.nextLine();
				String[] celulas = linhaTxt.trim().split("\\s+");
				
				for (int x = 0; x < celulas.length; x++) {
					String tipoStr = celulas[x];
					
					// criando a Casa passando x, y e o TIPO lido no arquivo
					grid[y][x] = new Casa(x, y, tipoStr);
				}
				y++;
			}
			
			leitor.close();
			System.out.println("Mapa carregado com sucesso");
		}
		
		catch( Exception e) {
			System.out.println("Err ao carregar o mapa");
			e.printStackTrace();
		}
	}
	
	public List<Casa> calculaCaminhosValidos(Casa origem, int passos){
		
		Set<Casa> resultado = new HashSet<>();
		Set<Casa> visitadas = new HashSet<>();
		
		dfs(origem, passos, visitadas, resultado);
		
		return new ArrayList<>(resultado);	
	}
	
	public List<Casa> getVizinhos(Casa casa) {
	    List<Casa> vizinhos = new ArrayList<>();

	    int x = casa.getX();
	    int y = casa.getY();

	    // Usando a checagem de limites da matriz
	    
	    if (y > 0) vizinhos.add(grid[y - 1][x]); // cima
	    if (y < altura - 1) vizinhos.add(grid[y + 1][x]); // baixo
	    if (x > 0) vizinhos.add(grid[y][x - 1]); // esquerda
	    if (x < largura - 1) vizinhos.add(grid[y][x + 1]); // direita
	    
	    return vizinhos;
	}
	
	
	private void dfs(Casa atual, int passos, Set<Casa> visitadas, Set<Casa> resultado) {
			
		// 1) se os passos acabaram
	    if (passos == 0) {
	        resultado.add(atual);
	        return;
	    }
	    
	    // 2) se parou na porta (entra no cômodo)
	    if (atual.getTipo().equals("p")) {
	    	resultado.add(atual);
	    	return;
	    }

	    visitadas.add(atual);

	    for (Casa vizinho : getVizinhos(atual)) {

	    	// além de não está ocupada, precisa ser caminhvel
	        if (!visitadas.contains(vizinho) && !vizinho.estaOcupada() && vizinho.isCaminhavel()) {
	        	
	        	dfs(vizinho, passos - 1, visitadas, resultado);
	        }
	    }

	    visitadas.remove(atual);
	}
	
	
	public void moverPeca(Peca peca, Casa destino) {
		
		if ( peca.getPosicaoAtual() != null) {
			peca.getPosicaoAtual().setOcupante(null);
		}
		
		destino.setOcupante(peca); // ocupa a nova casa
		peca.setPosicaoAtual(destino); // atualiza a peca
	}
	
	public Casa getCasa(int x, int y) {
	    return grid[y][x];
	}
	
	// mostrar o tabuleiro
	public void imprimirTabuleiro() {
		System.out.println("Mapa do Tabuleiro");
		
		for (int i = 0; i < largura; i++) {
			for (int j = 0; j < altura; j++) {
				
				// imprime as coordenadas da casa gerada
				System.out.print("[Casa " + i + "," + j + "]");
			}
			System.out.println();
		}
	}
}
