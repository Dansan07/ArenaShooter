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
public class Obstacle {
    
    public int x;
    public int y;
    public int width;
    public int height;
    public Rectangle solidArea;

    public Obstacle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.solidArea = new Rectangle(x,y,width,height);
    }
    
    public void draw (Graphics2D g2){
        g2.setColor(Color.GRAY);
        g2.fillRect(x, y, width, height);
    }
    
    
}
