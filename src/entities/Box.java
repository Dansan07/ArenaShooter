/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author jeanf
 */
public class Box {
   public int x, y, width, height;
    public Rectangle solidArea;
    private BufferedImage texture;

    public Box(int x, int y, int width, int height, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.solidArea = new Rectangle(x, y, width, height);

        try {
            texture = ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch (IOException | IllegalArgumentException e) {
            texture = null; // fallback al color sólido
        }
    }

    public void draw(Graphics2D g2) {
        if (texture != null) {
            g2.drawImage(texture, x, y, width, height, null);
        } else {
            g2.setColor(new Color(139, 90, 43));   
            g2.fillRect(x, y, width, height);
            g2.setColor(new Color(80, 50, 20));
            g2.drawRect(x, y, width, height);
        }
    } 
}
