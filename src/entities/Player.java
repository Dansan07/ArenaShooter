
package entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Usuario
 */
public class Player {
 public int x;
    public int y;
    
    public int oldX;
    public int oldY;
    
    public int size;
    public int speed;
    
    public BufferedImage sprite; 
    public Rectangle solidArea;
    public String direction;
    public int health;
    
   
    public Player(int x, int y, String imagePath){
        this.x = x;
        this.y = y;
        size = 70;
        speed = 5;       
        solidArea = new Rectangle(x, y, size, size);
        direction = "down";
        health = 3;
        
       
        getPlayerImage(imagePath);
    }
    
 
    public void getPlayerImage(String path) {
        try {
           
            sprite = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            System.out.println("Error al cargar el sprite en la ruta: " + path);
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics2D g2){
   
        if (sprite != null) {
            g2.drawImage(sprite, x, y, size, size, null);
        } else {
            g2.fillRect(x, y, size, size); 
        }
    }
    
    public void updateHitbox(){
        solidArea.x = x;
        solidArea.y = y;
    }

}

