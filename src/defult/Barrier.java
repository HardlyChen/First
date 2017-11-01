package defult;

import java.awt.*;

public class Barrier {
    private int x, y, w, h;
    TankClient tc;

    public Barrier ( int x, int y, int w, int h, TankClient tc) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.tc = tc;
    }

    public void draw(Graphics g) {
        g.fillRect( x, y, w, h);
    }

    public Rectangle getRect() {
        return new Rectangle( x, y, w, h);
    }
}
