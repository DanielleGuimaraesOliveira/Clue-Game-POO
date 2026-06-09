package view;

import javax.swing.JPanel;

import Interfaces.ICasa;
import Interfaces.IJogador;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;

import controller.ControladorPartida;
import model.Observador;
 class PainelTabuleiro extends JPanel implements Observador {

    private Image imagem;
    private JanelaJogo janelaPai;
    private ControladorPartida controlador;

    public PainelTabuleiro(
        Image imagem,
        ControladorPartida controlador,
        JanelaJogo janelaPai
    ) {

        this.imagem = imagem;
        this.controlador = controlador;
        this.janelaPai = janelaPai;

        addMouseListener(new ManipuladorCliqueTabuleiro(controlador,janelaPai,this));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        desenharTabuleiro(g2d);
        desenharMovimentosValidos(g2d);
        desenharPecas(g2d);
    }

    private void desenharTabuleiro(Graphics2D g2d) {

        if (imagem != null) {
            g2d.drawImage(imagem,0,0,getWidth(),getHeight(),this);
        }
    }

    private void desenharMovimentosValidos(Graphics2D g2d) {
        if (janelaPai == null || controlador == null) {
            return;
        }

        int valorDados = janelaPai.getValorSimuladoDados();

        if (valorDados <= 0 ||controlador.getJogadorAtual() == null) {
            return;
        }

        int larguraCasa = getWidth() / 24;
        int alturaCasa = getHeight() / 25;

        List<ICasa> casas =
            controlador.mapearCasas(valorDados);

        for (ICasa c : casas) {
            int px = c.getX() * larguraCasa;
            int py = c.getY() * alturaCasa;

            g2d.setColor(new java.awt.Color(0, 255, 0, 100));

            g2d.fillRect(px,py,larguraCasa,alturaCasa);

            g2d.setColor(new java.awt.Color(0, 200, 0));

            g2d.drawRect(px, py, larguraCasa - 1, alturaCasa - 1);
        }
    }

    private void desenharPecas(Graphics2D g2d) {

        if (controlador == null ||
            controlador.getJogadores() == null) {
            return;
        }

        int larguraCasa = getWidth() / 24;
        int alturaCasa = getHeight() / 25;

        int tamanhoPiao =(int)(Math.min( larguraCasa,alturaCasa) * 0.7);

        int margemX =(larguraCasa - tamanhoPiao) / 2;

        int margemY =(alturaCasa - tamanhoPiao) / 2;

        for (IJogador j : controlador.getJogadores()) {
            ICasa posicao =j.getPosicaoAtual();

            if (posicao == null) {
                continue;
            }

            int x =posicao.getX() * larguraCasa+ margemX + 12;
            int y = posicao.getY() * alturaCasa + margemY + 12;

            g2d.setColor(CorJogador.getCor(j.getPersonagem().getNome()));
            g2d.fillOval(x, y,  tamanhoPiao,  tamanhoPiao);
        }
    }

    @Override
    public void atualizar() {

        repaint();
    }
}