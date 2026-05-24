/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import entities.Bullet;
import entities.Obstacle;
import entities.Player;
import input.KeyboardInput;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Usuario
 */
public class GamePanel extends JPanel implements Runnable{
    
    final int screenWidth = 800;
    final int ScreenHeigth = 600;
    Thread gameThread;

    KeyboardInput keyH = new KeyboardInput();
    
    Player player1 = new Player(600, 400, "/sprites/player2_idle .png");
    Player player2 = new Player(100, 100, "/sprites/player1_idle.png");
    
    
    boolean player1Alive  = true;
    boolean player2Alive  = true;
    boolean showTitle = true;
    boolean gameOver = false;
    String winner = "";
    
    int player1ShootCooldown = 0;
    int player2ShootCooldown = 0;
    
    ArrayList<Bullet> bullets = new ArrayList<>();
    ArrayList<Obstacle> obstacles = new ArrayList<>();
    
    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,ScreenHeigth));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true); 
        this.addKeyListener(keyH);
        this.setFocusable(true);
        
        //obstaculos
        obstacles.add(new Obstacle(300, 200, 200, 40));
        obstacles.add(new Obstacle(150, 400, 40, 120));
        obstacles.add(new Obstacle(600, 100, 40, 150));
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gameOver) {
                    reiniciarJuego();
                }
            }
        });
    }
    
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / 60;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        while (gameThread != null){
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            
            if (delta >= 1){
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update() {
     if (gameOver) {
        return; 
    }

    if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.shootPressed ||
        keyH.up2Pressed || keyH.down2Pressed || keyH.left2Pressed || keyH.right2Pressed || keyH.shoot2Pressed) {
        showTitle = false;
    }

    player1.updateHitbox();
    player2.updateHitbox();
    
    player1.oldX = player1.x;
    player1.oldY = player1.y;

    player2.oldX = player2.x;
    player2.oldY = player2.y;    
    
    moverJugador1(player1);
    moverJugador2(player2);
    limitesPantalla(player1);
    limitesPantalla(player2);
    
    colision_obstaculo();
 
    dispararJ1();
    dispararJ2();
    gestionBullets();
    
    if (!player1Alive) {
        gameOver = true;
        winner = "¡JUGADOR 2 (AZUL) GANÓ!";
    } else if (!player2Alive) {
        gameOver = true;
        winner = "¡JUGADOR 1 (ROJO) GANÓ!";
    }
    }
    
    private void colision_obstaculo(){
        
        for (int i = 0; i < obstacles.size(); i++) {

            Obstacle obstacle = obstacles.get(i);

            player1.updateHitbox();

            if (player1.solidArea.intersects(obstacle.solidArea)) {

                player1.x = player1.oldX;

                player1.y = player1.oldY;
            }

            player2.updateHitbox();

            if (player2.solidArea.intersects(obstacle.solidArea)) {

                player2.x = player2.oldX;

                player2.y = player2.oldY;
            }
        }
        
    }
    
    public void reiniciarJuego() {
         player1 = new Player(600, 400, "/sprites/player2_idle .png");
         player2 = new Player(100, 100, "/sprites/player1_idle.png");
        
        player1Alive = true;
        player2Alive = true;
        bullets.clear();
        player1ShootCooldown = 0;
        player2ShootCooldown = 0;
        gameOver = false;
        winner = "";
        showTitle = true;
    }
    
    public void limitesPantalla(Player py){
        if (py.x < 0){
            py.x = 0;
        }
        if(py.y < 0){
            py.y = 0;
        }        
        if (py.x > screenWidth - py.size){
            py.x = screenWidth - py.size;
        }
        if (py.y > ScreenHeigth - py.size){
            py.y = ScreenHeigth - py.size;
        }
    }
    public void moverJugador1(Player py){
        if (keyH.upPressed){
            py.y -= py.speed;
            py.direction = "up";
        }
        if (keyH.downPressed){
            py.y += py.speed;
            py.direction = "down";
        }
        if (keyH.leftPressed){
            py.x -= py.speed;
            py.direction = "left";
        }
        if (keyH.rightPressed){
            py.x += py.speed;
            py.direction = "right";
        }
    }
    public void moverJugador2(Player py){
        if (keyH.up2Pressed){
            py.y -= py.speed;
            py.direction = "up";
        }
        if (keyH.down2Pressed){
            py.y += py.speed;
            py.direction = "down";
        }
        if (keyH.left2Pressed){
            py.x -= py.speed;
            py.direction = "left";
        }
        if (keyH.right2Pressed){
            py.x += py.speed;
            py.direction = "right";
        }
    }
    public void dispararJ1(){
        
        int speedX = 0;
        int speedY = 0;

        if(player1ShootCooldown > 0) {
            player1ShootCooldown--;
        }
        if(player1.direction.equals("up")) {
            speedY = -10;
        }

        if(player1.direction.equals("down")) {
            speedY = 10;
        }

        if(player1.direction.equals("left")) {
            speedX = -10;
        }
        if(player1.direction.equals("right")) {
            speedX = 10;
        }
        
        if(keyH.shootPressed && player1ShootCooldown == 0) {
            bullets.add(new Bullet(
                player1.x + player1.size / 2,
                player1.y + player1.size / 2,
                speedX,
                speedY,
                Color.RED
                )
            );
            // contrala velocidad de disparo
            player1ShootCooldown = 10;
        }      
    }
    public void dispararJ2(){
        
        int speedX = 0;
        int speedY = 0;
        
        if (player2ShootCooldown > 0) {
            player2ShootCooldown--;
        }
        
        if(player2.direction.equals("up")) {
            speedY = -10;
        }

        if(player2.direction.equals("down")) {
            speedY = 10;
        }

        if(player2.direction.equals("left")) {
            speedX = -10;
        }
        if(player2.direction.equals("right")) {
            speedX = 10;
        }
        
        if(keyH.shoot2Pressed && player2ShootCooldown == 0) {
            bullets.add(new Bullet(
                player2.x + player2.size / 2,
                player2.y + player2.size / 2,
                speedX,
                speedY,
                Color.BLUE
                )
            );
            // contrala velocidad de disparo
            player2ShootCooldown = 10;
        }        
    }
    public void gestionBullets(){
        for(int i = 0; i < bullets.size(); i++) {
            
            Bullet bullet = bullets.get(i);
            bullet.update();
            
            if(bullet.x < 0 ||
                bullet.x > screenWidth ||
                bullet.y < 0 ||
                bullet.y > ScreenHeigth) {
                bullets.remove(i);
                i--;
             }
            for (int j = 0; j < obstacles.size(); j++) {

                if (bullet.solidArea.intersects(
                        obstacles.get(j).solidArea)) {

                    bullets.remove(i);

                    i--;

                    break;
                }
            }

            if(bullet.color == Color.RED &&
               bullet.solidArea.intersects(player2.solidArea)) {

                player2.health--;
                bullets.remove(i);
                i--;
                if (player2.health <= 0){
                    player2Alive = false;
                }
            }

            if(bullet.color == Color.BLUE &&
               bullet.solidArea.intersects(player1.solidArea)) {

                player1.health--;
                bullets.remove(i);
                i--;
                if (player1.health <= 0){
                    player1Alive = false;
                }
            }
        }
    }
   
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
             if (showTitle) {
                 g.setColor(Color.WHITE);
                 g.drawString("Arena Shooter", 350, 300);
             }

             Graphics2D g2 = (Graphics2D) g;
             
             for(int i = 0; i < obstacles.size(); i++) {
                obstacles.get(i).draw(g2);
            }

             if(player1Alive) {
                 player1.draw(g2);
             }

             if(player2Alive) {
                 player2.draw(g2);
             }

             g2.setColor(Color.WHITE);
             g2.drawString("Player 1 HP: " + player1.health, 20, 20);
             g2.drawString("Player 2 HP: " + player2.health, 650, 20);

             for (int i = 0; i < bullets.size(); i++){
                 bullets.get(i).draw(g2);
             }


             if (gameOver) {
         g2.setColor(new Color(0, 0, 0, 180)); 
         g2.fillRect(0, 0, screenWidth, ScreenHeigth);

         g2.setColor(Color.YELLOW);
         g2.drawString(winner, 320, 260);

             g2.setColor(Color.WHITE);

             g2.drawString("Haz CLIC en la pantalla para volver a jugar", 260, 320);
         }
        
        g2.dispose();
    }
}
