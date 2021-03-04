package main;

import Other.BitOutputStream;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

/**
 * <p>运行窗口类，继承自JFrame。</p>
 * <p>通过构造器定义了运行界面的组成 </p>
 */
public final class MyFrame extends JFrame {
    /**
     * 运行界面的宽度
     */
    private static final int DEFAULT_WIDTH = 1400;
    /**
     * 运行界面的高度
     */
    private static final int DEFAULT_HEIGHT = 800;

    private MyDialog dialog;
    private HistogramDialog histogramDialog;
    private final ImageComponent originalImgComponent;
    private final ImageComponent processedImgComponent;
    ArrayList<BufferedImage> oriImgList = new ArrayList<>(), processedImgList = new ArrayList<>();
    int oriIndex = 0, proIndex = 0;
    private BufferedImage minuendImg = null, subtrahendImg = null;
    //展示图像
    public void showImage(int flag, BufferedImage bufferedImage){
        if (flag == 0){
            oriImgList.add(bufferedImage);
            oriIndex = oriImgList.size() - 1;
            originalImgComponent.A_IMAGE = oriImgList.get(oriIndex);
            originalImgComponent.repaint();
        }
        else if (flag == 1){
            processedImgList.add(bufferedImage);
            proIndex = processedImgList.size() - 1;
            processedImgComponent.A_IMAGE = processedImgList.get(proIndex);
            processedImgComponent.repaint();
        }
    }
    //展示图像
    public void showImage(int flag, int index){
        if (flag == 0){
            originalImgComponent.A_IMAGE = oriImgList.get(index);
            originalImgComponent.repaint();
        }
        else if (flag == 1){
            processedImgComponent.A_IMAGE = processedImgList.get(index);
            processedImgComponent.repaint();
        }
    }
    //获取当前显示的图像
    public BufferedImage getCurrentImage(int flag){
        if (flag == 1){
            return this.processedImgList.get(proIndex);
        }
        return this.oriImgList.get(oriIndex);
    }

    public ImageComponent getOriginalImgComponent() {
        return originalImgComponent;
    }

    public MyFrame() {
        //添加组件
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        var layout = new GridBagLayout();
        setLayout(layout);

        var label1 = new JLabel("原始图像");
        var constraintsLabel1 = new GridBagConstraints();
        constraintsLabel1.gridx = 2;
        constraintsLabel1.gridy = 0;
        constraintsLabel1.gridwidth = 1;
        constraintsLabel1.gridheight = 1;
        constraintsLabel1.weightx = 100;
        constraintsLabel1.anchor = GridBagConstraints.CENTER;
        add(label1,constraintsLabel1);

        var label2 = new JLabel("处理后的图像");
        var constraintsLabel2 = new GridBagConstraints();
        constraintsLabel2.gridx = 8;
        constraintsLabel2.gridy = 0;
        constraintsLabel2.gridwidth = 1;
        constraintsLabel2.gridheight = 1;
        constraintsLabel2.weightx = 100;
        constraintsLabel2.anchor = GridBagConstraints.CENTER;
        add(label2,constraintsLabel2);

        originalImgComponent = new ImageComponent(null);
        var constraintsCom1 = new GridBagConstraints();
        constraintsCom1.gridx = 0;
        constraintsCom1.gridy = 1;
        constraintsCom1.gridwidth = 5;
        constraintsCom1.gridheight = 5;
        constraintsCom1.fill = GridBagConstraints.BOTH;
        add(originalImgComponent,constraintsCom1);

        processedImgComponent = new ImageComponent(null);
        var constraintsCom2 = new GridBagConstraints();
        constraintsCom2.gridx = 6;
        constraintsCom2.gridy = 1;
        constraintsCom2.gridwidth = 5;
        constraintsCom2.gridheight = 5;
        constraintsCom2.fill = GridBagConstraints.BOTH;
        add(processedImgComponent,constraintsCom2);

        var toLeftBtn = new JButton("左移");
        toLeftBtn.addActionListener(e -> {
            if (processedImgList.size() > 0){
                showImage(0, getCurrentImage(1));
            }
        });
        var constraintsToLeftBtn = new GridBagConstraints();
        constraintsToLeftBtn.gridx = 5;
        constraintsToLeftBtn.gridy = 5;
        constraintsToLeftBtn.gridwidth = 1;
        constraintsToLeftBtn.gridheight = 1;
        constraintsToLeftBtn.fill = GridBagConstraints.HORIZONTAL;
        add(toLeftBtn,constraintsToLeftBtn);

        var lastBtn1 = new JButton("上一张");
        lastBtn1.addActionListener(e ->{
            if (oriImgList.size() > 0){
                if (oriIndex == 0){
                    oriIndex = oriImgList.size();
                }
                oriIndex--;
                showImage(0, oriIndex);
            }
        });
        var constraintsLastBtn1 = new GridBagConstraints();
        constraintsLastBtn1.gridx = 0;
        constraintsLastBtn1.gridy = 6;
        constraintsLastBtn1.gridwidth = 1;
        constraintsLastBtn1.gridheight = 1;
        add(lastBtn1,constraintsLastBtn1);

        var deleteBtn1 = new JButton("删除");
        deleteBtn1.addActionListener(e -> {
            if (oriImgList.size() > 0){
                oriImgList.remove(getCurrentImage(0));
                oriIndex = oriImgList.size() - 1;
                if (oriIndex >= 0)
                    showImage(0, oriIndex);

                else {
                    originalImgComponent.A_IMAGE = null;
                    originalImgComponent.repaint();
                }
            }
        });
        var constraintsDeleteBtn1 = new GridBagConstraints();
        constraintsDeleteBtn1.gridx = 2;
        constraintsDeleteBtn1.gridy = 6;
        constraintsDeleteBtn1.gridwidth = 1;
        constraintsDeleteBtn1.gridheight = 1;
        add(deleteBtn1,constraintsDeleteBtn1);

        var nextBtn1 = new JButton("下一张");
        nextBtn1.addActionListener(e -> {
            if (oriImgList.size() > 0){
                if (oriIndex == oriImgList.size() - 1){
                    oriIndex = -1;
                }
                oriIndex++;
                showImage(0, oriIndex);
            }
        });
        var constraintsNextBtn1 = new GridBagConstraints();
        constraintsNextBtn1.gridx = 4;
        constraintsNextBtn1.gridy = 6;
        constraintsNextBtn1.gridwidth = 1;
        constraintsNextBtn1.gridheight = 1;
        add(nextBtn1,constraintsNextBtn1);

        var lastBtn2 = new JButton("上一张");
        lastBtn2.addActionListener(e -> {
            if (processedImgList.size() > 0){
                if (proIndex == 0){
                    proIndex = processedImgList.size();
                }
                proIndex--;
                showImage(1, proIndex);
            }
        });
        var constraintsLastBtn2 = new GridBagConstraints();
        constraintsLastBtn2.gridx = 6;
        constraintsLastBtn2.gridy = 6;
        constraintsLastBtn2.gridwidth = 1;
        constraintsLastBtn2.gridheight = 1;
        add(lastBtn2,constraintsLastBtn2);

        var deleteBtn2 = new JButton("删除");
        deleteBtn2.addActionListener(e -> {
            if (processedImgList.size() > 0){
                processedImgList.remove(getCurrentImage(1));
                proIndex = processedImgList.size() - 1;
                if (proIndex >= 0)
                    showImage(1, proIndex);

                else {
                    processedImgComponent.A_IMAGE = null;
                    processedImgComponent.repaint();
                }
            }

        });
        var constraintsDeleteBtn2 = new GridBagConstraints();
        constraintsDeleteBtn2.gridx = 8;
        constraintsDeleteBtn2.gridy = 6;
        constraintsDeleteBtn2.gridwidth = 1;
        constraintsDeleteBtn2.gridheight = 1;
        add(deleteBtn2,constraintsDeleteBtn2);

        var nextBtn2 = new JButton("下一张");
        nextBtn2.addActionListener(e -> {
            if (processedImgList.size() > 0){
                if (proIndex == processedImgList.size() - 1){
                    proIndex = -1;
                }
                proIndex++;
                showImage(1, proIndex);
            }
        });
        var constraintsNextBtn2 = new GridBagConstraints();
        constraintsNextBtn2.gridx = 10;
        constraintsNextBtn2.gridy = 6;
        constraintsNextBtn2.gridwidth = 1;
        constraintsNextBtn2.gridheight = 1;
        add(nextBtn2,constraintsNextBtn2);

        var popup = new JPopupMenu();
        var setBackgroundItem = new JMenuItem("设为减数");
        setBackgroundItem.addActionListener(e -> {
            if (oriImgList.size() > 0)
                subtrahendImg = getCurrentImage(0);
        });
        popup.add(setBackgroundItem);
        var setTargetItem = new JMenuItem("设为被减数");
        setTargetItem.addActionListener(e -> {
            if (oriImgList.size() > 0)
                minuendImg = getCurrentImage(0);
        });
        popup.add(setTargetItem);
        originalImgComponent.setComponentPopupMenu(popup);

        // 设置菜单栏
        var menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        var fileMenu = new JMenu("文件");
        menuBar.add(fileMenu);
        var openItem = new JMenuItem("打开");
        fileMenu.add(openItem);
        fileMenu.addSeparator();
        var saveItem = new JMenuItem("保存");
        saveItem.setEnabled(false);
        fileMenu.add(saveItem);
        var editMenu = new JMenu("图像处理");
        editMenu.setEnabled(false);
        menuBar.add(editMenu);
        var sampleItem = new JMenuItem("采样");
        editMenu.add(sampleItem);
        editMenu.addSeparator();
        var quantizeItem = new JMenuItem("量化");
        editMenu.add(quantizeItem);
        editMenu.addSeparator();
        var toGrayItem = new JMenuItem("转为灰度图像");
        editMenu.add(toGrayItem);
        editMenu.addSeparator();
        var getBitPlainItem = new JMenuItem("获取位平面图");
        getBitPlainItem.setEnabled(false);
        editMenu.add(getBitPlainItem);
        editMenu.addSeparator();
        var binaryItem = new JMenuItem("阈值化");
        binaryItem.setEnabled(false);
        editMenu.add(binaryItem);
        var histogramMenu = new JMenu("直方图");
        histogramMenu.setEnabled(false);
        menuBar.add(histogramMenu);
        var viewHisItem = new JMenuItem("查看直方图");
        histogramMenu.add(viewHisItem);
        histogramMenu.addSeparator();
        var equalizeItem = new JMenuItem("传统直方图均衡");
        histogramMenu.add(equalizeItem);
        var pointOperationMenu = new JMenu("点运算");
        pointOperationMenu.setEnabled(false);
        menuBar.add(pointOperationMenu);
        var linearItem = new JMenuItem("线性点运算");
        pointOperationMenu.add(linearItem);
        pointOperationMenu.addSeparator();
        var logItem = new JMenuItem("对数运算");
        pointOperationMenu.add(logItem);
        pointOperationMenu.addSeparator();
        var exItem = new JMenuItem("幂次变换");
        pointOperationMenu.add(exItem);
        var geometricOperationMenu = new JMenu("几何运算");
        geometricOperationMenu.setEnabled(false);
        menuBar.add(geometricOperationMenu);
        var panItem = new JMenuItem("平移");
        geometricOperationMenu.add(panItem);
        geometricOperationMenu.addSeparator();
        var rotateItem = new JMenuItem("旋转");
        geometricOperationMenu.add(rotateItem);
        geometricOperationMenu.addSeparator();
        var scaleItem = new JMenuItem("缩放");
        geometricOperationMenu.add(scaleItem);
        var algebraicOperationMenu = new JMenu("代数运算");
        menuBar.add(algebraicOperationMenu);
        var subtractItem = new JMenuItem("图像相减");
        algebraicOperationMenu.add(subtractItem);
        var enhancementMenu = new JMenu("图像增强");
        menuBar.add(enhancementMenu);
        var neighborhoodAveragingItem = new JMenuItem("邻域平均法平滑");
        enhancementMenu.add(neighborhoodAveragingItem);
        enhancementMenu.addSeparator();
        var kNearestNeighborMeanFilterItem = new JMenuItem("K近邻均值平滑");
        enhancementMenu.add(kNearestNeighborMeanFilterItem);
        enhancementMenu.addSeparator();
        var medianFilteringItem = new JMenuItem("中值滤波平滑");
        enhancementMenu.add(medianFilteringItem);
        enhancementMenu.addSeparator();
        var sobelGradientSharpItem = new JMenuItem("Sobel算子锐化");
        enhancementMenu.add(sobelGradientSharpItem);
        enhancementMenu.addSeparator();
        var laplacianSharpItem = new JMenuItem("Laplacian算子锐化");
        enhancementMenu.add(laplacianSharpItem);
        enhancementMenu.addSeparator();
        var robertsSharpItem = new JMenuItem("Roberts算子锐化");
        enhancementMenu.add(robertsSharpItem);
        enhancementMenu.addSeparator();
        var convolutionItem = new JMenuItem("任意模板卷积");
        enhancementMenu.add(convolutionItem);
        var segmentationMenu = new JMenu("图像分割");
        menuBar.add(segmentationMenu);
        var edgeDetectionItem = new JMenuItem("边缘检测");
        segmentationMenu.add(edgeDetectionItem);
        segmentationMenu.addSeparator();
        var houghTransformItem = new JMenuItem("霍夫变换");
        segmentationMenu.add(houghTransformItem);
        var compressionMenu = new JMenu("图像压缩");
        menuBar.add(compressionMenu);
        var huffmanCodingItem = new JMenuItem("哈夫曼编码压缩");
        compressionMenu.add(huffmanCodingItem);
        compressionMenu.addSeparator();
        var decompressionItem = new JMenuItem("哈夫曼编码解压");
        compressionMenu.add(decompressionItem);

        // 设置”打开图片“菜单的监听器
        openItem.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("."));
            chooser.showOpenDialog(this);
            String imgSrc = chooser.getSelectedFile().getPath();
            BufferedImage imgRead = null;
            try {
                imgRead = ImageIO.read(new File(imgSrc));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            assert imgRead != null;
            showImage(0,imgRead);
            if (!saveItem.isEnabled()){
                saveItem.setEnabled(true);
            }
            if (!editMenu.isEnabled()){
                editMenu.setEnabled(true);
            }
            if (getBitPlainItem.isEnabled()){
                getBitPlainItem.setEnabled(false);
            }
            if (!viewHisItem.isEnabled()){
                viewHisItem.setEnabled(true);
            }
            if (!binaryItem.isEnabled()){
                binaryItem.setEnabled(true);
            }
            if (!geometricOperationMenu.isEnabled()){
                geometricOperationMenu.setEnabled(true);
            }
        });
        // 设置“保存图片”菜单的监听器
        saveItem.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("."));
            chooser.showSaveDialog(this);
            String imgSrc = chooser.getSelectedFile().getPath();
            try {
                OutputStream outputStream = new FileOutputStream(imgSrc);
                BufferedImage imageSave = getCurrentImage(1);
                ImageIO.write(imageSave, "jpg", outputStream);
                System.out.println("save successfully");
                outputStream.close();
            } catch (IOException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });
        // 设置“转化为灰度图像”菜单的监听器
        toGrayItem.addActionListener(e -> {
            if (getCurrentImage(0).getType() != BufferedImage.TYPE_BYTE_GRAY) {
                showImage(1,Algorithm.toGrayscaleImage(getCurrentImage(0)));
            }
            if (!getBitPlainItem.isEnabled()){
                getBitPlainItem.setEnabled(true);
            }
            if (!histogramMenu.isEnabled()){
                histogramMenu.setEnabled(true);
            }
            if (!pointOperationMenu.isEnabled()){
                pointOperationMenu.setEnabled(true);
            }
        });
        // 设置“获取位平面”菜单的监听器
        getBitPlainItem.addActionListener(e -> {
            if (getCurrentImage(0) != null){
                ArrayList<BufferedImage> bfList = Algorithm.getBitPlain(getCurrentImage(0));
                System.out.println(bfList.size());
                for (BufferedImage bufferedImage:bfList) {
                    showImage(1,bufferedImage);
                }
                System.out.println(proIndex);
                System.out.println(processedImgList.size());
            }
        });
        //设置“查看直方图”菜单的监听器
        viewHisItem.addActionListener(e -> {
            histogramDialog = new HistogramDialog(this,
                    Algorithm.normalizeForHistogram(Algorithm.getFrequency(getCurrentImage(0))),
                    Algorithm.getStatistics(getCurrentImage(0)));
            histogramDialog.setVisible(true);
        });
        //设置“阈值化”菜单的监听器
        binaryItem.addActionListener(e -> {
            dialog = new ThresholdDialog(this);
            dialog.setVisible(true);
        });
        //设置“采样”菜单的监听器
        sampleItem.addActionListener(e -> {
            dialog = new SampleDialog(this);
            dialog.setVisible(true);
        });
        //设置“量化”菜单的监听器
        quantizeItem.addActionListener(e -> {
            dialog = new QuantizeDialog(this);
            dialog.setVisible(true);
        });
        //设置“线性点运算”菜单的监听器
        linearItem.addActionListener(e -> {
            dialog = new LinearDialog(this);
            dialog.setVisible(true);
        });
        //设置“对数运算”菜单的监听器
        logItem.addActionListener(e -> {
            BufferedImage bufferedImage = getCurrentImage(0);
            bufferedImage = Algorithm.logPointOperation(bufferedImage);
            showImage(1,bufferedImage);
        });
        //设置“幂次运算”菜单的监听器
        exItem.addActionListener(e -> {
            dialog = new ExponentialDialog(this);
            dialog.setVisible(true);
        });
        //设置“传统直方图均衡”菜单的监听器
        equalizeItem.addActionListener(e -> {
            BufferedImage bufferedImage = getCurrentImage(0);
            bufferedImage = Algorithm.traditionalHistogramEqualization(bufferedImage);
            showImage(1,bufferedImage);
            histogramDialog = new HistogramDialog(this,
                    Algorithm.normalizeForHistogram(Algorithm.getFrequency(bufferedImage)),
                    Algorithm.getStatistics(bufferedImage));
            histogramDialog.setVisible(true);
        });
        //设置“平移”菜单的监听器
        panItem.addActionListener(e ->{
            dialog = new PanDialog(this);
            dialog.setVisible(true);
        });
        //设置“旋转”菜单的监听器
        rotateItem.addActionListener(e -> {
            dialog = new RotateDialog(this);
            dialog.setVisible(true);
        });
        //设置“缩放”菜单的监听器
        scaleItem.addActionListener(e -> {
            dialog = new ScaleDialog(this);
            dialog.setVisible(true);
        });
        //设置“图像相减”菜单的监听器
        subtractItem.addActionListener(e -> showImage(1,Algorithm.subtraction(minuendImg,subtrahendImg)));
        //设置“邻域平均法平滑”菜单的监听器
        neighborhoodAveragingItem.addActionListener(e -> showImage(1,
                Algorithm.neighborhoodAveraging(getCurrentImage(0))));
        //设置“K近邻均值平滑”菜单的监听器
        kNearestNeighborMeanFilterItem.addActionListener(e -> showImage(1,
                Algorithm.kNearestNeighborMeanFilter(getCurrentImage(0))));
        //设置“中值滤波平滑”菜单的监听器
        medianFilteringItem.addActionListener(e -> showImage(1,
                Algorithm.medianFiltering(getCurrentImage(0))));
        //设置“sobel算子锐化”菜单的监听器
        sobelGradientSharpItem.addActionListener(e -> {
            BufferedImage curImg = getCurrentImage(0);
            int[] pixel = Algorithm.sobel(curImg);
            BufferedImage conImg = Algorithm.pixelToImage(pixel,curImg);
            BufferedImage bufferedImage = Algorithm.addition(conImg, curImg);
            showImage(1, bufferedImage);
        });
        //设置“laplacian算子锐化”菜单的监听器
        laplacianSharpItem.addActionListener(e -> {
            BufferedImage curImg = getCurrentImage(0);
            int[] pixel = Algorithm.laplacian(curImg);
            BufferedImage conImg = Algorithm.pixelToImage(pixel,curImg);
            BufferedImage bufferedImage = Algorithm.addition(conImg, curImg);
            showImage(1, bufferedImage);
        });
        //设置“roberts算子锐化”菜单的监听器
        robertsSharpItem.addActionListener(e -> {
            BufferedImage curImg = getCurrentImage(0);
            int[] pixel = Algorithm.roberts(curImg);
            BufferedImage conImg = Algorithm.pixelToImage(pixel,curImg);
            BufferedImage bufferedImage = Algorithm.addition(conImg, curImg);
            showImage(1, bufferedImage);
        });
        //设置“任意模板卷积”菜单的监听器
        convolutionItem.addActionListener(e ->{
            dialog = new ConvolutionDialog(this);
            dialog.setVisible(true);
        });
        //设置“霍夫变换”菜单的监听器
        houghTransformItem.addActionListener(e -> showImage(1,
                Algorithm.houghTransform(getCurrentImage(0))));
        //设置“边缘检测”菜单的监听器
        edgeDetectionItem.addActionListener(e -> {
            BufferedImage curImg = getCurrentImage(0);
            BufferedImage detImage = Algorithm.pixelToImage(Algorithm.sobel(curImg),curImg);
            showImage(1,detImage);
        });
        //设置“哈夫曼编码压缩”菜单的监听器
        huffmanCodingItem.addActionListener(e -> {
            BufferedImage comImage = getCurrentImage(0);
            int w = comImage.getWidth();
            int h = comImage.getHeight();
            String[] codes = new String[256];
            String[] data = Algorithm.huffmanCodingCompression(comImage,codes);
            int counts  = 0;
            for (String code: codes) {
                if (code != null)
                    counts++;
            }
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("."));
            chooser.showSaveDialog(this);
            String comSrc = chooser.getSelectedFile().getPath();
            try {
                File file = new File(comSrc);
                BitOutputStream output = new BitOutputStream(file);
                output.writeInt(w);
                output.writeInt(h);
                output.writeInt(counts);
                for (int i = 0; i < codes.length; i++) {
                    if (codes[i] != null){
                        output.writeInt(i);
                        output.writeInt(codes[i].length());
                        output.writeToByte(codes[i]);
                    }
                }
                assert data != null;
                for (String str: data) {
                    output.writeBit(str);
                }
                output.close();
            } catch (IOException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });
        //设置“哈夫曼编码解压”菜单的监听器
        decompressionItem.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("."));
            chooser.showOpenDialog(this);
            String src = chooser.getSelectedFile().getPath();
            try {
                showImage(0,Algorithm.decompression(src));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }
}
