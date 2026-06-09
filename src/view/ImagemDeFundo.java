package view;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;

public class ImagemDeFundo extends JPanel {
    private Image imagem;

    public ImagemDeFundo(Image imagem) {
        this.imagem = imagem;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (imagem != null) {

            g.drawImage(imagem, 0, 0,getWidth(), getHeight(), this);
        }
    }
}