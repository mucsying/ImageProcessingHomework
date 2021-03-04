package main;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 * <p>用于绘制直方图的组件，继承自<code>JComponent</code></p>
 * <p>组件的样式在构造器中设置，重写了<code>public void paintComponent(Graphics g)</code>方法以在该组件上绘制直方图和文本</p>
 */

public final class HistogramComponent extends JComponent {

    int[] frequency;
    float[] statistics;
    HistogramComponent(int[] aFrequency,float[] aStatistics) {
        frequency = aFrequency;
        statistics = aStatistics;
        Border border = BorderFactory.createEtchedBorder();
        setBorder(border);
    }
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        var xAxis = new Line2D.Double(50,510,580,510);
        var yAxis = new Line2D.Double(50,510,50,10);
        graphics2D.setColor(Color.BLACK);
        graphics2D.draw(xAxis);
        graphics2D.draw(yAxis);
        graphics2D.draw(new Line2D.Double(45,15,50,10));
        graphics2D.draw(new Line2D.Double(55,15,50,10));
        graphics2D.draw(new Line2D.Double(575,505,580,510));
        graphics2D.draw(new Line2D.Double(575,515,580,510));
        g.drawString("均值:",50,535);
        g.drawString("中位数:",160,535);
        g.drawString("标准差:",250,535);
        g.drawString("总像素数:",410,535);
        if (frequency != null){
            graphics2D.setColor(Color.DARK_GRAY);
            for(int i = 0;i < frequency.length;i++){
                var rect = new Rectangle2D.Double(50+2*i,510-frequency[i],2,frequency[i]);
                graphics2D.fill(rect);
            }
        }
        if (statistics != null){
            g.drawString(Float.toString(statistics[0]),75,535);
            g.drawString(Float.toString(statistics[1]),200,535);
            g.drawString(Float.toString(statistics[2]),290,535);
            g.drawString(Float.toString(statistics[3]),460,535);
        }
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(600,600);
    }
}
