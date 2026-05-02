package model;
import java.util.*;


class Tabuleiro {
	
	private Casa[][] grid;
	private List<Comodo> comodos;
	
	public Tabuleiro( int largura, int altura) {
		this.grid = new Casa[largura][altura];
		
		// iniciar o grid com os objetos Casa
		for (int i = 0; i < largura; i++) {
			
			for (int j = 0; j < altura; j++) {
				
				grid[i][j] = new Casa(i, j);
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

	    // cima
	    if (x > 0) vizinhos.add(grid[x - 1][y]);

	    // baixo
	    if (x < grid.length - 1) vizinhos.add(grid[x + 1][y]);

	    // esquerda
	    if (y > 0) vizinhos.add(grid[x][y - 1]);

	    // direita
	    if (y < grid[0].length - 1) vizinhos.add(grid[x][y + 1]);

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
	    return grid[x][y];
	}
	
	// mostrar o tabuleiro
	public void imprimirTabuleiro() {
		System.out.println("Mapa do Tabuleiro");
		
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				
				// imprime as coordenadas da casa gerada
				System.out.print("[Casa " + i + "," + j + "]");
			}
			System.out.println();
		}
	}
}
