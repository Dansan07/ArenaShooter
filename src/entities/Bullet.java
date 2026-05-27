/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author Usuario
 */
public class Bullet {
    
    public int x;
    public int y;
    public int size = 10;
    public int speedX;
    public int speedY;
    public Color color;
    public Rectangle solidArea;

    public Bullet(int x, int y, int speedX, int speedY, Color color) {
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.color = color;
        solidArea = new Rectangle(x, y, size, size);
    }
    
    public void update(){
        x += speedX;
        y += speedY;
        
        solidArea.x = x;
        solidArea.y = y;        
    }
    
    public void draw(Graphics2D g2){
        g2.setColor(color);
        g2.fillOval(x, y, size, size);
    }
    
}
