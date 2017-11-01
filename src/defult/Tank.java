package defult;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.PortUnreachableException;
import java.util.*;
import java.util.List;

public class Tank {
    private static final int XSPEED= 5;
    private static final int YSPEED = 5;
    private static final int WIDTH = 30;
    private static final int HEIGHT = 30;



    private boolean live = true;
    private BloodBar bb = new BloodBar();

    TankClient tc;

    private int x , y;
    private int oldX, oldY;

    public boolean isGood() {
        return good;
    }



    private boolean good = true ;

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    private int life = 100;

    private  boolean bL = false , bR = false , bU = false , bD = false;

    private static Random r = new Random();


    enum Direction {L, LU, U, RU, R, RD, D, LD,STOP};
    private Direction dir = Direction.STOP;
    private Direction ptdir = Direction.D;

    private int step = r.nextInt(20) + 3;


    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }


    public Tank(int x, int y, boolean good, Direction dir, TankClient tc) {
        this(x, y, good);
        this.dir = dir;
        this.tc = tc ;
    }



    public Tank(int x, int y, boolean good) {
        this.x = x;
        this.y = y;
        this.oldX = x;
        this.oldY = y;
        this.good = good;
    }

    public void draw(Graphics g) {
        if( !live ) return;

        Color c = g.getColor();
        if(good) g.setColor(Color.red);
        else g.setColor(Color.blue);
        g.fillOval(x,y,WIDTH,HEIGHT);
        g.setColor(c);

        if( isGood() && isLive()) {
            bb.draw(g);
        }

        switch (ptdir){
            case L:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT/2);
                break;
            case LU:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y );
                break;
            case U:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y);
                break;
            case RU:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y);
                break;
            case R:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT/2);
                break;
            case RD:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT);
                break;
            case D:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y + Tank.HEIGHT);
                break;
            case LD:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT);
                break;
            case STOP:
                break;
        }

        move();
    }

    void move() {
        this.oldX = x;
        this.oldY = y;
        switch (dir){
            case L:
                x -= XSPEED;
                break;
            case LU:
                x -= XSPEED/1.414;
                y -= YSPEED/1.414;
                break;
            case U:
                y -= YSPEED;
                break;
            case RU:
                x += XSPEED/1.414;
                y -= YSPEED/1.414;
                break;
            case R:
                x += XSPEED;
                break;
            case RD:
                x += XSPEED/1.414;
                y += YSPEED/1.414;
                break;
            case D:
                y += YSPEED;
                break;
            case LD:
                x -= XSPEED/1.414;
                y += YSPEED/1.414;
                break;
            case STOP:
                break;
        }

        if( this.dir != Direction.STOP){
            this.ptdir = this.dir;
        }

        if( x < 0 ) x = 0;
        if( y < 30 ) y = 30;
        if( x + Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
        if( y + Tank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.HEIGHT;

        if( !good ){
            Direction[] dirs = Direction.values();
            if(step == 0){
                step = r.nextInt(20)+3;
                int rn = r.nextInt(dirs.length);
                dir = dirs[rn];
            }
            step--;

            if(r.nextInt(40) > 38)this.fire();
        }
    }

    private void stay(){
        x = oldX;
        y = oldY;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key){
            case KeyEvent.VK_LEFT:
                bL = true;
                break;
            case KeyEvent.VK_UP:
                bU = true;
                break;
            case KeyEvent.VK_RIGHT:
                bR = true;
                break;
            case KeyEvent.VK_DOWN:
                bD = true;
                break;
        }
        locateDirection();
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key){
            case KeyEvent.VK_SPACE:
                fire();
                break;
            case KeyEvent.VK_LEFT:
                bL = false;
                break;
            case KeyEvent.VK_UP:
                bU = false;
                break;
            case KeyEvent.VK_RIGHT:
                bR = false;
                break;
            case KeyEvent.VK_DOWN:
                bD = false;
                break;
        }
        locateDirection();
    }


    void locateDirection() {
        if(bL && !bU && !bR && !bD) dir = Direction.L;
        else if(bL && bU && !bR && !bD) dir = Direction.LU;
        else if(!bL && bU && !bR && !bD) dir = Direction.U;
        else if(!bL && bU && bR && !bD) dir = Direction.RU;
        else if(!bL && !bU && bR && !bD) dir = Direction.R;
        else if(!bL && !bU && bR && bD) dir = Direction.RD;
        else if(!bL && !bU && !bR && bD) dir = Direction.D;
        else if(bL && !bU && !bR && bD) dir = Direction.LD;
        else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;

    }


    public Missile fire(){
        if(!live) return null;
        int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
        int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;

        Missile m = new Missile(x, y, good, ptdir, this.tc);
        tc.missiles.add(m);
        return m;
    }

    public Rectangle getReact() {
        return new Rectangle( x, y, WIDTH, HEIGHT);
    }

    public boolean collidesWithBarrier(Barrier b) {
        if(this.live && this.getReact().intersects(b.getRect()) ) {
            this.stay();
            return true;
        }
        return false;
    }

    public boolean collidesWithTanks(List<Tank> tanks) {
        for( int i = 0; i < tanks.size(); i++) {
            Tank t = tanks.get(i);
            if( this != t){
                if(this.live && this.getReact().intersects(t.getReact()) ) {
                    this.stay();
                    t.stay();
                    return true;
            }
        }
        }
        return false;
    }

    private class BloodBar {
        public void draw(Graphics g) {
            Color c = g.getColor();
            g.setColor(Color.green);
            g.drawRect(x,y-10, WIDTH, 10);
            int w = WIDTH * life / 100;
            g.fillRect(x,y-10, w, 10);
            g.setColor(c);
        }
    }

}
