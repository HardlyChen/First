package defult;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

public class TankClient extends Frame{

    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;

    Tank mytank = new Tank(700 ,500, true, Tank.Direction.STOP,this);

    List<Explode> explodes = new ArrayList<>();
    List<Missile> missiles = new ArrayList<>();
    List<Tank> tanks = new ArrayList<>();


    Barrier b1 = new Barrier(100, 200, 20, 150, this);
    Barrier b2 = new Barrier(300, 100, 3000, 20, this);


    Explode e = new Explode(70, 70, this);


    Image offScreenImage = null;

    public void paint(Graphics g) {
        g.drawString("missiles count: " + missiles.size(), 10 ,50);
        g.drawString("tank life:" + mytank.getLife(), 10, 60);

        for(int i = 0; i < missiles.size(); i++){

            Missile m = missiles.get(i);
            m.hitBarrie(b1);
            m.hitBarrie(b2);
            m.hitTanks(tanks);
            m.hitTank(mytank);
            m.draw(g);
        }

        for( int i = 0; i < explodes.size(); i++){
            Explode e = explodes.get(i);
            e.draw(g);
        }


        for( int i = 0  ; i < tanks.size() ; i++){
            Tank t = tanks.get(i);
            t.collidesWithBarrier(b1);
            t.collidesWithBarrier(b2);
            t.collidesWithTanks(tanks);
            t.draw(g);
        }

        mytank.draw(g);
        b1.draw(g);
        b2.draw(g);


    }



    public void update(Graphics g) {
        if( offScreenImage == null){
            offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);
        }
        Graphics goffScreen = offScreenImage.getGraphics();
        Color c = goffScreen.getColor();
        goffScreen.setColor(Color.white);
        goffScreen.fillRect(0 , 0 , GAME_WIDTH ,GAME_HEIGHT);
        goffScreen.setColor(c);
        paint(goffScreen);
        g.drawImage(offScreenImage , 0 ,0 , null);
    }


    public void lauchFrame(){

        for( int i = 0 ; i < 10 ; i++){
            tanks.add(new Tank(50 + 40 * (i+1), 50, false, Tank.Direction.D, this));
        }

        this.setLocation(400,300);
        this.setSize(GAME_WIDTH,GAME_HEIGHT);
        this.setTitle("TankWar");
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setResizable(false);
        this.setBackground(Color.GREEN);
        this.addKeyListener(new KeyMonitor());
        setVisible(true);

        new Thread(new PaintThread()).start();
    }


    public static void main(String[] args) {
        TankClient tc = new TankClient();
        tc.lauchFrame();
    }

    private class PaintThread implements Runnable{

        public void run() {
            while(true){
                repaint();
                try {
                    Thread.sleep(20 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private  class  KeyMonitor extends KeyAdapter{

        public void keyPressed(KeyEvent e) {
            mytank.keyPressed(e);

        }


        public void keyReleased(KeyEvent e) {
            mytank.keyReleased(e);

        }
    }



}
