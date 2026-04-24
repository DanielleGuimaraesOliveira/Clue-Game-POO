import java.util.*;


public class Tabuleiro {
	
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
		
		// lógica da busca aqui
		
		return new ArrayList<>();
	}
	
	public void moverPeca(Peca peca, Casa destino) {
		
		if ( peca.getPosicaoAtual() != null) {
			peca.getPosicaoAtual().setOcupante(null);
		}
		
		destino.setOcupante(peca); // ocupa a nova casa
		peca.setPosicaoAtual(destino); // atualiza a peca
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
