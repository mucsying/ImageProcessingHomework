package main;

import javax.swing.*;
import java.awt.*;

/**
 * 项目的入口，产生一个图形化的界面供用户操作
 *
 * @author 徐尘化
 *
 */
public class ImageProcessing {
    public static void main(String[] args){
        EventQueue.invokeLater(()->
        {
            MyFrame frame = new MyFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
