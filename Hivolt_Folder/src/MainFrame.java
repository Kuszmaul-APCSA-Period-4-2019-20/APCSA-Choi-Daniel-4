import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;


public class MainFrame extends JFrame implements KeyListener{
    private static MainDraw draw;

    public void keyPressed(KeyEvent e) {
        //System.out.println("keyPressed");
    }

    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode()== KeyEvent.VK_D)
            draw.moveRight();
        else if(e.getKeyCode()== KeyEvent.VK_A)
            draw.moveLeft();
        else if(e.getKeyCode()== KeyEvent.VK_X)
            draw.moveDown();
        else if(e.getKeyCode()== KeyEvent.VK_W)
            draw.moveUp();
        else if(e.getKeyCode()== KeyEvent.VK_E)
            draw.moveRightUp();
        else if(e.getKeyCode()== KeyEvent.VK_Q)
            draw.moveLeftUp();
        else if(e.getKeyCode()== KeyEvent.VK_Z)
            draw.moveLeftDown();
        else if(e.getKeyCode()== KeyEvent.VK_C)
            draw.moveRightDown();
        else if(e.getKeyCode()== KeyEvent.VK_S)
            draw.stay();
        else if (e.getKeyCode() == KeyEvent.VK_J)
        	draw.moveJump();
        else if (e.getKeyCode() == KeyEvent.VK_ENTER)
			draw.spawn();

    }
    public void keyTyped(KeyEvent e) {
        //System.out.println("keyTyped");
    }

    public MainFrame(){
        this.draw=new MainDraw();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {         	
                MainFrame frame = new MainFrame();
                frame.setTitle("Hivolts");
                frame.setResizable(false);
                frame.setSize(600, 600);
                frame.setMinimumSize(new Dimension(600, 600));
                frame.setBackground(Color.white);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(frame.draw);
                frame.pack();
                frame.setVisible(true);  
                draw.spawn();
            }
        });
    }
}

