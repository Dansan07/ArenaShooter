/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 *
 * @author Usuario
 */
public class KeyboardInput implements KeyListener{
    
    //movimiento jugador 1
    public boolean upPressed;
    public boolean downPressed;
    public boolean leftPressed;
    public boolean rightPressed;
    
    //movimiento jugador 2
    public boolean up2Pressed;
    public boolean down2Pressed;
    public boolean left2Pressed;
    public boolean right2Pressed;
    
    //disparos
    public boolean shootPressed;
    public boolean shoot2Pressed;
    
    // Variable para el reinicio
    public boolean enterPressed;
    
    public void moverJ1(int code, boolean bool){
        if (code == KeyEvent.VK_W){
            upPressed = bool;             
        }
        
        if (code == KeyEvent.VK_S){
            downPressed = bool;             
        }
        
        if (code == KeyEvent.VK_A){
            leftPressed = bool;             
        }
        
        if (code == KeyEvent.VK_D){
            rightPressed = bool;             
        }
    }
    
    public void moverJ2(int code, boolean bool){
        if (code == KeyEvent.VK_UP){
            up2Pressed = bool;             
        }
        
        if (code == KeyEvent.VK_DOWN){
            down2Pressed = bool;             
        }
        
        if (code == KeyEvent.VK_LEFT){
            left2Pressed = bool;             
        }
        
        if (code == KeyEvent.VK_RIGHT){
            right2Pressed = bool;             
        }
    }
    
    public void disparar(int code, boolean bool){
        if(code == KeyEvent.VK_SPACE) {
            shootPressed = bool;
        }

        if(code == KeyEvent.VK_ENTER) {
            shoot2Pressed = bool;
        }        
    }
    public void revisarReinicio(int code, boolean bool) {
        if (code == KeyEvent.VK_R) {
            enterPressed = bool; // Mapeamos la tecla R a tu variable del GamePanel
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
       
        int code = e.getKeyCode();
        moverJ1(code, true);
        moverJ2(code, true);
        disparar(code, true);
        revisarReinicio(code, true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        
        moverJ1(code, false);
        moverJ2(code, false);
        disparar(code, false);
        revisarReinicio(code, false);
    }
    
}
