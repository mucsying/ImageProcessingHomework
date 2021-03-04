package main;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * <p>用于展示图片的组件，继承自JComponent</p>
 * <p>组件的样式在构造器中设置</p>
 */
public final class ImageComponent extends JComponent {

    Image A_IMAGE;
    static int x = 5,y = 5;
    ImageComponent(Image a){
        A_IMAGE = a;
        Border border = BorderFactory.createEtchedBorder();
        setBorder(border);
    }

    public void setXY(int ax,int ay){
        x = ax;
        y = ay;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (A_IMAGE != null){
            g.drawImage(A_IMAGE,x,y,this);
        }
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(600,600);
    }
}
