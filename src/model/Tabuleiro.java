package model;
import java.util.*;


class Tabuleiro {
	
	// substituir pelo HashMap
	// private Casa[][] grid;
	
	private Map<String, Casa> grid;
	
	private List<Comodo> comodos;
	private int largura;
	private int altura;
	
	public Tabuleiro( int largura, int altura) {
		this.largura = largura;
		this.altura = altura;
		this.grid = new HashMap<>(); // inicializando o mapa
		
		// iniciar o grid populando o HashMap
		for (int i = 0; i < largura; i++) {		
			for (int j = 0; j < altura; j++) {
				String chave = i + "," + j;
				grid.put(chave, new Casa(i, j));
			}
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

	    // Usando o containsKey do HashMap para verificar se o vizinho existe
	    
	    // cima
	    if (grid.containsKey((x - 1) + "," + y)) vizinhos.add(grid.get((x - 1) + "," + y));
	    
	    // baixo
	    if (grid.containsKey((x + 1) + "," + y)) vizinhos.add(grid.get((x + 1) + "," + y));
	    
	    // esquerda
	    if (grid.containsKey(x + "," + (y - 1))) vizinhos.add(grid.get(x + "," + (y - 1)));
	    
	    // direita
	    if (grid.containsKey(x + "," + (y + 1))) vizinhos.add(grid.get(x + "," + (y + 1)));

	    return vizinhos;
	}
	
	
	private void dfs(Casa atual, int passos, Set<Casa> visitadas, Set<Casa> resultado) {
		
		
	    if (passos == 0) {
	        resultado.add(atual);
	        return;
	    }

	    visitadas.add(atual);

	    for (Casa vizinho : getVizinhos(atual)) {

	    
	        if (!visitadas.contains(vizinho) && !vizinho.estaOcupada()) {
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
	    return grid.get(x + "," + y);
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
