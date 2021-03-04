package main;

import Other.OperatorModel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * <p>自定义对话框类，继承自JDialog</p>
 * <p>只包含一个构造器，构造器指定了这个对话框将以什么样式生成</p>
 * @author 徐尘化
 */
public class MyDialog extends JDialog {
    /**
     * 类的构造器
     * @param owner
     *              对话框生成时基于的父框架
     */
    public MyDialog(MyFrame owner){
        super(owner,"dialog",false);
    }
}

class HistogramDialog extends MyDialog{
    /**
     * 类的构造器
     *
     * @param owner 对话框生成时基于的父框架
     */
    public HistogramDialog(MyFrame owner, int[] fre,float[] stat) {
        super(owner);
        var label1 = new JLabel("点运算后直方图");
        add(label1, BorderLayout.NORTH);
        HistogramComponent histogramComponent = new HistogramComponent(null,null);
        histogramComponent.frequency = fre;
        histogramComponent.statistics = stat;
        add(histogramComponent,BorderLayout.CENTER);
        setSize(600,610);
    }
}

class ThresholdDialog extends MyDialog{

    /**
     * 类的构造器
     *
     * @param owner 对话框生成时基于的父框架
     */
    public ThresholdDialog(MyFrame owner) {
        super(owner);
        var panel = new Panel();
        var label = new JLabel("threshold:");
        var textFiled = new JTextField(5);
        panel.add(label);
        panel.add(textFiled);
        add(panel,BorderLayout.CENTER);
        var panel2 = new Panel();
        var okButton = new JButton("ok");
        okButton.addActionListener(e -> {
            int threshold = Integer.parseInt(textFiled.getText().trim());
            owner.showImage(1,Algorithm.binarization(owner.getCurrentImage(0),threshold));
            setVisible(false);
        });
        var cancelButton = new JButton("cancel");
        cancelButton.addActionListener(e -> setVisible(false));
        panel2.add(okButton);
        panel2.add(cancelButton);
        add(panel2,BorderLayout.SOUTH);
        setSize(300,200);
    }
}

class SampleDialog extends MyDialog{

    /**
     * 类的构造器
     *
     * @param owner 对话框生成时基于的父框架
     */
    public SampleDialog(MyFrame owner) {
        super(owner);
        var layout = new GridBagLayout();
        setLayout(layout);

        var label1 = new JLabel("采样间距:");
        var consLabel1 = new GridBagConstraints();
        consLabel1.gridx = 0;
        consLabel1.gridy = 0;
        consLabel1.gridwidth = 1;
        consLabel1.gridheight = 1;
        add(label1,consLabel1);

        var sampleCombo = new JComboBox<Integer>();
        sampleCombo.addItem(8);
        sampleCombo.addItem(16);
        sampleCombo.addItem(32);
        sampleCombo.addItem(64);
        sampleCombo.addItem(128);
        sampleCombo.addItem(256);
        var consSample = new GridBagConstraints();
        consSample.gridx = 1;
        consSample.gridy = 0;
        consSample.gridwidth = 2;
        consSample.gridheight = 1;
        add(sampleCombo,consSample);

        var okBtn = new JButton("采样");
        okBtn.addActionListener(e -> {
            int samplingInterval = sampleCombo.getItemAt(sampleCombo.getSelectedIndex());
            owner.showImage(1,Algorithm.sample(owner.getCurrentImage(0),samplingInterval));
            setVisible(false);
        });
        var consOkBtn = new GridBagConstraints();
        consOkBtn.gridx = 0;
        consOkBtn.gridy = 2;
        consOkBtn.gridwidth = 1;
        consOkBtn.gridheight = 1;
        add(okBtn,consOkBtn);

        var cancelBtn = new JButton("取消");
        cancelBtn.addActionListener(e -> setVisible(false));
        var consCancelBtn = new GridBagConstraints();
        consCancelBtn.gridx = 2;
        consCancelBtn.gridy = 2;
        consCancelBtn.gridwidth = 1;
        consCancelBtn.gridheight = 1;
        add(cancelBtn,consCancelBtn);

        setSize(400,200);
    }
}

class QuantizeDialog extends MyDialog{

    /**
     * 类的构造器
     *
     * @param owner 对话框生成时基于的父框架
     */
    public QuantizeDialog(MyFrame owner) {
        super(owner);
        var layout = new GridBagLayout();
        setLayout(layout);

        var label2 = new JLabel("量化等级:");
        var consLabel2 = new GridBagConstraints();
        consLabel2.gridx = 0;
        consLabel2.gridy = 1;
        consLabel2.gridwidth = 1;
        consLabel2.gridheight = 1;
        add(label2,consLabel2);

        var quantizeCombo = new JComboBox<Integer>();
        quantizeCombo.addItem(2);
        quantizeCombo.addItem(4);
        quantizeCombo.addItem(8);
        quantizeCombo.addItem(16);
        quantizeCombo.addItem(32);
        quantizeCombo.addItem(64);
        quantizeCombo.addItem(128);
        quantizeCombo.addItem(256);
        var consQuantize = new GridBagConstraints();
        consQuantize.gridx = 1;
        consQuantize.gridy = 1;
        consQuantize.gridwidth = 2;
        consQuantize.gridheight = 1;
        add(quantizeCombo,consQuantize);

        var okBtn = new JButton("量化");
        okBtn.addActionListener(e -> {
            int quantizeLevel = quantizeCombo.getItemAt(quantizeCombo.getSelectedIndex());
            owner.showImage(1,Algorithm.quantization(owner.getCurrentImage(0),quantizeLevel));
            setVisible(false);
        });
        var consOkBtn = new GridBagConstraints();
        consOkBtn.gridx = 0;
        consOkBtn.gridy = 2;
        consOkBtn.gridwidth = 1;
        consOkBtn.gridheight = 1;
        add(okBtn,consOkBtn);

        var cancelBtn = new JButton("取消");
        cancelBtn.addActionListener(e -> setVisible(false));
        var consCancelBtn = new GridBagConstraints();
        consCancelBtn.gridx = 2;
        consCancelBtn.gridy = 2;
        consCancelBtn.gridwidth = 1;
        consCancelBtn.gridheight = 1;
        add(cancelBtn,consCancelBtn);

        setSize(400,200);
    }
}

class LinearDialog extends MyDialog{

    /**
     * 类的构造器
     *
     * @param owner 对话框生成时基于的父框架
     */
    public LinearDialog(MyFrame owner) {
        super(owner);
        var panel = new Panel();
        var label = new JLabel("ax+b,a=");
        var textFiled = new JTextField(5);
        panel.add(label);
        panel.add(textFiled);
        var label1 = new JLabel("ax+b,b=");
        var textFiled1 = new JTextField(5);
        panel.add(label1);
        panel.add(textFiled1);
        add(panel,BorderLayout.CENTER);

        var panel2 = new Panel();
        var okButton = new JButton("ok");
        okButton.addActionListener(e -> {
            float a = 0,b = 0;
            if (!textFiled.getText().trim().equals(""))
                a = Float.parseFloat(textFiled.getText().trim());
            if (!textFiled1.getText().trim().equals(""))
                b = Float.parseFloat(textFiled1.getText().trim());
            owner.showImage(1,Algorithm.linearPointOperation(owner.getCurrentImage(0),a,b));
            setVisible(false);
        });
        var cancelButton = new JButton("cancel");
        cancelButton.addActionListener(e -> setVisible(false));
        panel2.add(okButton);
        panel2.add(cancelButton);
        add(panel2,BorderLayout.SOUTH);
        setSize(300,200);
    }
}

class ExponentialDialog extends MyDialog{

    /**
     * 类的构造器
     *
     * @param owner 对话框生成时基于的父框架
     */
    public ExponentialDialog(MyFrame owner) {
        super(owner);

        Panel panel1 = new Panel();
        var label2 = new JLabel("x^γ,γ=");
        var textFiled2 = new JTextField(5);
        panel1.add(label2);
        panel1.add(textFiled2);
        add(panel1,BorderLayout.NORTH);
        var panel2 = new Panel();
        var okButton = new JButton("ok");
        okButton.addActionListener(e -> {
            float v = 0;
            if (!textFiled2.getText().trim().equals(""))
                v = Float.parseFloat(textFiled2.getText().trim());
            owner.showImage(1,Algorithm.powerPointOperation(owner.getCurrentImage(0), v));
            setVisible(false);
        });
        var cancelButton = new JButton("cancel");
        cancelButton.addActionListener(e -> setVisible(false));
        panel2.add(okButton);
        panel2.add(cancelButton);
        add(panel2,BorderLayout.SOUTH);
        setSize(300,200);
    }
}

class PanDialog extends MyDialog{

    /**
     * 类的构造器
     *
     * @param owner 对话框生成时基于的父框架
     */
    public PanDialog(MyFrame owner) {
        super(owner);
        var panel = new Panel();
        var label = new JLabel("Δx=");
        var textFiled = new JTextField(5);
        panel.add(label);
        panel.add(textFiled);
        var label1 = new JLabel("Δy=");
        var textFiled1 = new JTextField(5);
        panel.add(label1);
        panel.add(textFiled1);
        add(panel,BorderLayout.CENTER);

        var panel2 = new Panel();
        var okButton = new JButton("ok");
        // 平移按钮的监听器，同时平移功能在这里实现
        okButton.addActionListener(e -> {
            int x = 0,y = 0;
            if (!textFiled.getText().trim().equals(""))
                x = Integer.parseInt(textFiled.getText().trim());
            if (!textFiled1.getText().trim().equals(""))
                y = Integer.parseInt(textFiled1.getText().trim());
            owner.getOriginalImgComponent().setXY(x, y);
            owner.getOriginalImgComponent().repaint();
            setVisible(false);
        });
        var cancelButton = new JButton("cancel");
        cancelButton.addActionListener(e -> setVisible(false));
        panel2.add(okButton);
        panel2.add(cancelButton);
        add(panel2,BorderLayout.SOUTH);
        setSize(300,200);
    }
}

class RotateDialog extends MyDialog{

    /**
     * 类的构造器
     *
     * @param owner 对话框生成时基于的父框架
     */
    public RotateDialog(MyFrame owner) {
        super(owner);
        var panel = new Panel();
        var label = new JLabel("x=");
        var textFiled = new JTextField(5);
        panel.add(label);
        panel.add(textFiled);
        var label1 = new JLabel("y=");
        var textFiled1 = new JTextField(5);
        panel.add(label1);
        panel.add(textFiled1);
        add(panel,BorderLayout.CENTER);

        var panel1 = new Panel();
        var label2 = new JLabel("旋转角度=");
        var textFiled2 = new JTextField(5);
        panel1.add(label2);
        panel1.add(textFiled2);
        add(panel1,BorderLayout.NORTH);

        var buttonPanel1 = new JPanel();
        var group1 = new ButtonGroup();
        var radioButton1 = new JRadioButton("256",true);
        group1.add(radioButton1);
        buttonPanel1.add(radioButton1);
        var radioButton2 = new JRadioButton("128",false);
        group1.add(radioButton2);
        buttonPanel1.add(radioButton2);
        add(buttonPanel1,BorderLayout.SOUTH);

        var panel2 = new Panel();
        var okButton = new JButton("ok");
        okButton.addActionListener(e -> {
            double x = 0.0, y = 0.0, theta = 0.0;
            if (!textFiled.getText().trim().equals(""))
                x = Double.parseDouble(textFiled.getText().trim());
            if (!textFiled1.getText().trim().equals(""))
                y = Double.parseDouble(textFiled1.getText().trim());
            if (!textFiled2.getText().trim().equals(""))
                theta = Double.parseDouble(textFiled2.getText().trim());
            owner.showImage(1,Algorithm.rotate(owner.getCurrentImage(0),x,y,theta));
            setVisible(false);
        });
        var cancelButton = new JButton("cancel");
        cancelButton.addActionListener(e -> setVisible(false));
        panel2.add(okButton);
        panel2.add(cancelButton);
        add(panel2,BorderLayout.SOUTH);
        setSize(300,200);
    }
}

class ScaleDialog extends MyDialog{

    /**
     * 类的构造器
     *
     * @param owner 对话框生成时基于的父框架
     */
    public ScaleDialog(MyFrame owner) {
        super(owner);
        var panel = new Panel();
        var label = new JLabel("scale on x=");
        var textFiled = new JTextField(5);
        panel.add(label);
        panel.add(textFiled);
        var label1 = new JLabel("scale on y=");
        var textFiled1 = new JTextField(5);
        panel.add(label1);
        panel.add(textFiled1);
        add(panel,BorderLayout.CENTER);

        var panel2 = new Panel();
        var okButton = new JButton("ok");
        okButton.addActionListener(e -> {
            double x = 0.0, y = 0.0;
            if (!textFiled.getText().trim().equals(""))
                x = Double.parseDouble(textFiled.getText().trim());
            if (!textFiled1.getText().trim().equals(""))
                y = Double.parseDouble(textFiled1.getText().trim());
            owner.showImage(1,Algorithm.scale(owner.getCurrentImage(0),x,y));
            setVisible(false);
        });
        var cancelButton = new JButton("cancel");
        cancelButton.addActionListener(e -> setVisible(false));
        panel2.add(okButton);
        panel2.add(cancelButton);
        add(panel2,BorderLayout.SOUTH);
        setSize(300,200);
    }
}

class ConvolutionDialog extends MyDialog{

    /**
     * 类的构造器
     *
     * @param owner 对话框生成时基于的父框架
     */
    public ConvolutionDialog(MyFrame owner) {
        super(owner);
        var layout = new GridBagLayout();
        setLayout(layout);

        var label1 = new JLabel("行数:");
        var consLabel1 = new GridBagConstraints();
        consLabel1.gridx = 0;
        consLabel1.gridy = 1;
        consLabel1.gridwidth = 1;
        consLabel1.gridheight = 1;
        add(label1,consLabel1);

        var textFiled1 = new JTextField(5);
        var consTextFiled1 = new GridBagConstraints();
        consTextFiled1.gridx = 1;
        consTextFiled1.gridy = 1;
        consTextFiled1.gridwidth = 2;
        consTextFiled1.gridheight = 1;
        add(textFiled1,consTextFiled1);

        var label2 = new JLabel("列数:");
        var consLabel2 = new GridBagConstraints();
        consLabel2.gridx = 0;
        consLabel2.gridy = 2;
        consLabel2.gridwidth = 1;
        consLabel2.gridheight = 1;
        add(label2,consLabel2);

        var textFiled2 = new JTextField(5);
        var consTextFiled2 = new GridBagConstraints();
        consTextFiled2.gridx = 1;
        consTextFiled2.gridy = 2;
        consTextFiled2.gridwidth = 2;
        consTextFiled2.gridheight = 1;
        add(textFiled2,consTextFiled2);

        var label3 = new JLabel("模板值（用，分隔）:");
        var consLabel3 = new GridBagConstraints();
        consLabel3.gridx = 0;
        consLabel3.gridy = 3;
        consLabel3.gridwidth = 1;
        consLabel3.gridheight = 1;
        add(label3,consLabel3);

        var textFiled3 = new JTextField(10);
        var consTextFiled3 = new GridBagConstraints();
        consTextFiled3.gridx = 1;
        consTextFiled3.gridy = 3;
        consTextFiled3.gridwidth = 2;
        consTextFiled3.gridheight = 1;
        add(textFiled3,consTextFiled3);

        var okBtn = new JButton("卷积");
        okBtn.addActionListener(e -> {
            int rows = 0, columns = 0, i = 0;
            String str = "";
            String[] arr;
            if (!textFiled1.getText().trim().equals(""))
                rows = Integer.parseInt(textFiled1.getText().trim());
            if (!textFiled2.getText().trim().equals(""))
                columns = Integer.parseInt(textFiled2.getText().trim());
            if (!textFiled3.getText().trim().equals(""))
                str = textFiled3.getText().trim();
            arr = str.split(",");
            System.out.println(str);
            int[] value = new int[rows*columns];
            for (String s: arr) {
                value[i] = Integer.parseInt(s);
                i++;
            }
            System.out.println(Arrays.toString(value));
            OperatorModel operatorModel = new OperatorModel(rows,columns,value);
            int[] pixel = Algorithm.convolution(operatorModel.modelValue,
                    owner.getCurrentImage(0));
            System.out.println(Arrays.deepToString(operatorModel.modelValue));
            owner.showImage(1,Algorithm.pixelToImage(pixel,owner.getCurrentImage(0)));
            setVisible(false);
        });
        var consOkBtn = new GridBagConstraints();
        consOkBtn.gridx = 2;
        consOkBtn.gridy = 4;
        consOkBtn.gridwidth = 1;
        consOkBtn.gridheight = 1;
        add(okBtn,consOkBtn);

        var cancelBtn = new JButton("取消");
        cancelBtn.addActionListener(e -> setVisible(false));
        var consCancelBtn = new GridBagConstraints();
        consCancelBtn.gridx = 0;
        consCancelBtn.gridy = 4;
        consCancelBtn.gridwidth = 1;
        consCancelBtn.gridheight = 1;
        add(cancelBtn,consCancelBtn);

        setSize(300,200);
    }
}