/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import entities.Box;
import entities.Bullet;
import entities.Obstacle;
import entities.Player;
import input.AccelerometerInput;
import input.JoystickInput;
import input.KeyboardInput;
import input.SerialManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
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
    SerialManager serial = new SerialManager();
    JoystickInput joystick = new JoystickInput(serial);
    AccelerometerInput accel = new AccelerometerInput(serial);

    Player player1 = new Player(80,  270, "/sprites/player2_idle.png");
    Player player2 = new Player(670, 270, "/sprites/player1_idle.png");

    boolean player1Alive = true;
    boolean player2Alive = true;
    boolean showTitle = true;
    boolean gameOver = false;
    String winner = "";

    int player1ShootCooldown = 0;
    int player2ShootCooldown = 0;

    ArrayList<Bullet> bullets = new ArrayList<>();
    ArrayList<Box> boxes = new ArrayList<>();
    BufferedImage background;

    public GamePanel() {
        serial.connect();
        this.setPreferredSize(new Dimension(screenWidth, ScreenHeigth));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gameOver) reiniciarJuego();
            }
        });

        try {
            background = ImageIO.read(getClass().getResourceAsStream("/sprites/background.png"));
        } catch (IOException | IllegalArgumentException e) {
            background = null;
        }

        boxes.add(new Box(200, 150, 80, 80, "/sprites/box.png"));
        boxes.add(new Box(500, 200, 80, 80, "/sprites/box.png"));
        boxes.add(new Box(350, 400, 80, 80, "/sprites/box.png"));
        boxes.add(new Box(100, 450, 80, 80, "/sprites/box.png"));
        boxes.add(new Box(620, 450, 80, 80, "/sprites/box.png"));
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / 60;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update() {
        if (gameOver) return;

        boolean p1Up    = keyH.upPressed    || joystick.isUp();
        boolean p1Down  = keyH.downPressed  || joystick.isDown();
        boolean p1Left  = keyH.leftPressed  || joystick.isLeft();
        boolean p1Right = keyH.rightPressed || joystick.isRight();

        boolean p2Up    = keyH.up2Pressed    || accel.isUp();
        boolean p2Down  = keyH.down2Pressed  || accel.isDown();
        boolean p2Left  = keyH.left2Pressed  || accel.isLeft();
        boolean p2Right = keyH.right2Pressed || accel.isRight();

        if (p1Up || p1Down || p1Left || p1Right || keyH.shootPressed
                || p2Up || p2Down || p2Left || p2Right || keyH.shoot2Pressed) {
            showTitle = false;
        }

        moverConColision(player1, p1Up, p1Down, p1Left, p1Right);
        moverConColision(player2, p2Up, p2Down, p2Left, p2Right);

        limitesPantalla(player1);
        limitesPantalla(player2);

        dispararJ1();
        dispararJ2();
        gestionBullets();

        if (!player1Alive) { gameOver = true; winner = "¡JUGADOR 2 (AZUL) GANÓ!"; }
        else if (!player2Alive) { gameOver = true; winner = "¡JUGADOR 1 (ROJO) GANÓ!"; }
    }

    private void moverConColision(Player py,
                                   boolean up, boolean down,
                                   boolean left, boolean right) {
        if (left)  { py.x -= py.speed; py.direction = "left";  }
        if (right) { py.x += py.speed; py.direction = "right"; }
        py.updateHitbox();
        for (Box box : boxes) {
            if (py.solidArea.intersects(box.solidArea)) {
                if (left)  py.x += py.speed;
                if (right) py.x -= py.speed;
                py.updateHitbox();
                break;
            }
        }

        if (up)   { py.y -= py.speed; py.direction = "up";   }
        if (down) { py.y += py.speed; py.direction = "down"; }
        py.updateHitbox();
        for (Box box : boxes) {
            if (py.solidArea.intersects(box.solidArea)) {
                if (up)   py.y += py.speed;
                if (down) py.y -= py.speed;
                py.updateHitbox();
                break;
            }
        }
    }

    public void reiniciarJuego() {
        player1 = new Player(80,  270, "/sprites/player2_idle.png");
        player2 = new Player(670, 270, "/sprites/player1_idle.png");

        player1Alive = true;
        player2Alive = true;
        bullets.clear();
        player1ShootCooldown = 0;
        player2ShootCooldown = 0;
        gameOver = false;
        winner = "";
        showTitle = true;
    }

    public void limitesPantalla(Player py) {
        if (py.x < 0) py.x = 0;
        if (py.y < 0) py.y = 0;
        if (py.x > screenWidth - py.size) py.x = screenWidth - py.size;
        if (py.y > ScreenHeigth - py.size) py.y = ScreenHeigth - py.size;
    }

    public void dispararJ1() {
        int speedX = 0, speedY = 0;
        if (player1ShootCooldown > 0) player1ShootCooldown--;
        if (player1.direction.equals("up"))    speedY = -10;
        if (player1.direction.equals("down"))  speedY =  10;
        if (player1.direction.equals("left"))  speedX = -10;
        if (player1.direction.equals("right")) speedX =  10;

        if ((keyH.shootPressed || joystick.isShoot()) && player1ShootCooldown == 0) {
            bullets.add(new Bullet(
                player1.x + player1.size / 2,
                player1.y + player1.size / 2,
                speedX, speedY, Color.RED));
            player1ShootCooldown = 10;
        }
    }

    public void dispararJ2() {
        int speedX = 0, speedY = 0;
        if (player2ShootCooldown > 0) player2ShootCooldown--;
        if (player2.direction.equals("up"))    speedY = -10;
        if (player2.direction.equals("down"))  speedY =  10;
        if (player2.direction.equals("left"))  speedX = -10;
        if (player2.direction.equals("right")) speedX =  10;

        if (keyH.shoot2Pressed && player2ShootCooldown == 0) {
            bullets.add(new Bullet(
                player2.x + player2.size / 2,
                player2.y + player2.size / 2,
                speedX, speedY, Color.BLUE));
            player2ShootCooldown = 10;
        }
    }

    public void gestionBullets() {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.update();

            if (bullet.x < 0 || bullet.x > screenWidth
                    || bullet.y < 0 || bullet.y > ScreenHeigth) {
                bullets.remove(i--);
                continue;
            }
            if (bullet.color == Color.RED && bullet.solidArea.intersects(player2.solidArea)) {
                player2.health--;
                bullets.remove(i--);
                if (player2.health <= 0) player2Alive = false;
                continue;
            }
            if (bullet.color == Color.BLUE && bullet.solidArea.intersects(player1.solidArea)) {
                player1.health--;
                bullets.remove(i--);
                if (player1.health <= 0) player1Alive = false;
                continue;
            }
            boolean hitBox = false;
            for (Box box : boxes) {
                if (bullet.solidArea.intersects(box.solidArea)) {
                    bullets.remove(i--);
                    hitBox = true;
                    break;
                }
            }
            if (hitBox) continue;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (background != null) {
            g2.drawImage(background, 0, 0, screenWidth, ScreenHeigth, null);
        } else {
            g2.setPaint(new java.awt.GradientPaint(
                0, 0, new Color(20, 20, 40),
                screenWidth, ScreenHeigth, new Color(10, 10, 20)));
            g2.fillRect(0, 0, screenWidth, ScreenHeigth);
        }

        for (Box box : boxes) box.draw(g2);

        if (player1Alive) player1.draw(g2);
        if (player2Alive) player2.draw(g2);

        g2.setColor(Color.WHITE);
        g2.drawString("Player 1 HP: " + player1.health, 20, 20);
        g2.drawString("Player 2 HP: " + player2.health, 650, 20);

        for (int i = 0; i < bullets.size(); i++) bullets.get(i).draw(g2);

        if (showTitle) {
            g2.setColor(Color.WHITE);
            g2.drawString("Arena Shooter", 350, 300);
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
