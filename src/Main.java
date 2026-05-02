import java.util.List;

public class Main {

	public static void main(String[] args) {
		
		GerenciadorDePartida jogo = new GerenciadorDePartida();

		Jogador j1 = new Jogador("Ana", new PecaSuspeito("Srta. Scarlet"));
		Jogador j2 = new Jogador("João", new PecaSuspeito("Sr. Verde"));

		jogo.adicionarJogador(j1);
		jogo.adicionarJogador(j2);

		jogo.iniciarPartida();
		
		System.out.println("Jogo iniciado!");

		int passos = jogo.lancarDados();
		System.out.println("Dados: " + passos);

		List<Casa> casas = jogo.mapearCasas(passos);

		System.out.println("Casas possíveis:");
		for (Casa c : casas) {
		    System.out.println("(" + c.getX() + "," + c.getY() + ")");
		}
	}

}
