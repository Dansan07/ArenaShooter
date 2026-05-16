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
public class Player {
    
    public int x;
    public int y;
    public int size;
    public int speed;
    public Color color;
    public Rectangle solidArea;
    public String direction;
    public int health;
    
    public Player(int x, int y, Color color){
        this.x = x;
        this.y = y;
        this.color = color;
        size = 40;
        speed = 5;       
        solidArea = new Rectangle(x, y, size, size);
        direction= "down";
        health = 3;
    }
    
    public void draw(Graphics2D g2){
        g2.setColor(color);
        g2.fillRect(x, y, size, size);
    }
    
    public void updateHitbox(){
        solidArea.x = x;
        solidArea.y = y;
    }
    
}
