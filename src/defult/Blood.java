package defult;

import java.awt.*;

public class Blood {
    private int x, y, w, h;
    TankClient tc;


    private int[][] pos = {{350,300}, {360,300}, {375,275}, {400,200}, {360,270}, {365,290}, {340,280}};


    public Blood() {
        x = pos[0][0];
        y = pos[0][1];
    }

    public void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.MAGENTA);
        g.fillRect(x, y, w, h);
        g.setColor(c);
    }

}
