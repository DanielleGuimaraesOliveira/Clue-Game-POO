import java.util.List;
import model.GerenciadorDePartida;

class Main {

	public static void main(String[] args) {
		
		GerenciadorDePartida jogo = new GerenciadorDePartida();

		// Jogador j1 = new Jogador("Ana", new PecaSuspeito("Srta. Scarlet"));
		// Jogador j2 = new Jogador("João", new PecaSuspeito("Sr. Verde"));

		jogo.adicionarJogador("Ana", "Srta. Scarlet");
		jogo.adicionarJogador("João","Sr. Verde");

		jogo.iniciarPartida();
		
		System.out.println("Jogo iniciado!");

		int passos = jogo.lancarDados();
		System.out.println("Dados: " + passos);

		List<String> casas = jogo.mapearCasaFormatadas(passos);

		System.out.println("Casas possíveis:");
		for (String c : casas) {
		    System.out.println(c);
		}
	}

}

