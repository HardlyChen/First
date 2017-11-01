package defult;

import java.awt.*;

public class Explode {
    private int x, y;
    private boolean live = true;

    private TankClient tc;

    int[] diameter = { 4, 7, 12 ,18, 26, 32, 49, 40, 30, 14, 6};
    int step = 0;

    public Explode(int x, int y, TankClient tc) {
        this.x = x;
        this.y = y;
        this.tc = tc;

    }

    public void draw(Graphics g) {
        if( !live) {
            tc.explodes.remove(this);
            return;
        }

        if( step == diameter.length){
            live =true;
            return;
        }

        Color c = g.getColor();
        g.setColor(Color.orange);
        g.fillOval(x-diameter[step]/2, y-diameter[step]/2,diameter[step] , diameter[step]);
        g.setColor(c);
        step++;
    }
}
