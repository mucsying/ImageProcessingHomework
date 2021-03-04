package main;

import Other.DiffClass;
import Other.HoughResult;
import Other.MyHeap;
import Other.MyTree;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Algorithm {

    /**
     * 对图像进行采样
     *
     * @param originalImage  待采样图像
     * @param levels 采样点数
     */
    public static BufferedImage sample(BufferedImage originalImage, int levels) {
        int iw = originalImage.getWidth();
        int ih = originalImage.getHeight();
        int[] pix = new int[iw * ih];
        originalImage.getRGB(0, 0, iw, ih, pix, 0, iw);
        ColorModel cm = ColorModel.getRGBdefault();

        int d = 512 / levels; // 采样间隔
        int dd = d * d; // 采样区域包含的原像素数
        for (int i = 0; i < ih; i = i + d) {
            for (int j = 0; j < iw; j = j + d) {
                int r = 0, g = 0, b = 0;
                for (int k = 0; k < d; k++)
                    for (int l = 0; l < d; l++) {
                        int index = (i + k) * iw + (j + l);
                        r = r + cm.getRed(pix[index]);
                        g = g + cm.getGreen(pix[index]);
                        b = b + cm.getBlue(pix[index]);
                    }
                //求r、g、b的均值
                r = r / dd;
                g = g / dd;
                b = b / dd;
                // 新的像素数组
                for (int k = 0; k < d; k++) {
                    for (int l = 0; l < d; l++) {
                        pix[(i + k) * iw + (j + l)] = new Color(r, g, b).getRGB();
                    }
                }

            }
        }
        BufferedImage newImage = new BufferedImage(iw,ih,BufferedImage.TYPE_BYTE_GRAY);
        newImage.setRGB(0, 0, iw, ih, pix, 0, iw);
        return newImage;
    }

    /**
     * 对图像进行量化
     *
     * @param originalImage    待量化图像
     * @param series 量化等级
     * @return 量化后的图像
     */
    public static BufferedImage quantization(BufferedImage originalImage, int series) {
        int w = originalImage.getWidth();
        int h = originalImage.getHeight();
        int[] pix = new int[w * h];
        originalImage.getRGB(0, 0, w, h, pix, 0, w);
        int r, g, b, temp;
        ColorModel cm = ColorModel.getRGBdefault();
        for (int i = 0; i < w * h; i++) {
            r = cm.getRed(pix[i]);
            temp = r / series;
            r = temp * series;
            g = cm.getGreen(pix[i]);
            temp = g / series;
            g = temp * series;
            b = cm.getBlue(pix[i]);
            temp = b / series;
            b = temp * series;
            pix[i] = new Color(r, g, b).getRGB();
        }
        BufferedImage newImage = new BufferedImage(w,h,BufferedImage.TYPE_BYTE_GRAY);
        newImage.setRGB(0, 0, w, h, pix, 0, w);
        return newImage;
    }

    /**
     * 将图像转成灰度图像，转化的方法是使用经验公式：0.299 * r + 0.587 * g + 0.114 * b
     *
     * @param originalImage 待转化的原始图像
     * @return 转化后的灰度图像
     */
    public static BufferedImage toGrayscaleImage(BufferedImage originalImage) {
        int originalWeight = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        //新建灰度图
        BufferedImage imageGray = new BufferedImage(originalWeight, originalHeight, BufferedImage.TYPE_BYTE_GRAY);
        int r, g, b, rgbValue, grayValue;
        for (int height = 0; height < originalHeight; height++) {
            for (int width = 0; width < originalWeight; width++) {
                rgbValue = originalImage.getRGB(width, height);
                r = (rgbValue & 0xff0000) >> 16;
                g = (rgbValue & 0xff00) >> 8;
                b = (rgbValue & 0xff);
                int transform = (int) (0.299 * r + 0.587 * g + 0.114 * b);//计算灰度值
                grayValue = (transform << 16) | (transform << 8) | (transform);//灰度图像像素值
                imageGray.setRGB(width, height, grayValue);//将灰度图像像素值赋予新的图像

            }
        }
        return imageGray;
    }

    /**
     * 获取该图像的位平面图组，如果该图像不是灰度图像，
     * 得到的是blue下的位平面图。
     * 所以使用前请务必保证该图像已经是灰度图像，
     * 本方法未进行图像类型的检查。
     *
     * @param originalImage 待转化的原始图像
     * @return 转化后的灰度图像组，存储在ArrayList当中，头部权重最小，尾部权重最大
     */
    public static ArrayList<BufferedImage> getBitPlain(BufferedImage originalImage) {
        ArrayList<BufferedImage> bfList = new ArrayList<>();
        int originalWeight = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        int[][][] extractMatrix = new int[originalWeight][originalHeight][8];// 提取矩阵
        int[][] bitValueMatrix = new int[originalWeight][originalHeight];// 像素值矩阵
        int[][][] resultMatrix = new int[originalWeight][originalHeight][8];// 结果矩阵
        // 初始化提取矩阵
        for (int i = 0; i < 8; i++) {
            for (int height = 0; height < originalHeight; height++) {
                for (int width = 0; width < originalWeight; width++) {
                    extractMatrix[width][height][i] = (int) Math.pow(2, i);
                }
            }
        }
        // 初始化像素值矩阵
        for (int height = 0; height < originalHeight; height++) {
            for (int width = 0; width < originalWeight; width++) {
                bitValueMatrix[width][height] = originalImage.getRGB(width, height) & 0xFF;// 得到灰度值
            }
        }
        // 结果计算
        for (int i = 0; i < 8; i++) {
            BufferedImage bitImage = new BufferedImage(originalWeight,originalHeight,BufferedImage.TYPE_BYTE_BINARY);
            for (int height = 0; height < originalHeight; height++) {
                for (int width = 0; width < originalWeight; width++) {
                    // 按位与运算得到结果
                    resultMatrix[width][height][i] = extractMatrix[width][height][i]&bitValueMatrix[width][height];
                    // 为1则转换为255的灰度
                    if (resultMatrix[width][height][i] > 0){
                        resultMatrix[width][height][i] = 255<<16|255<<8|255;
                    }
                    bitImage.setRGB(width,height,resultMatrix[width][height][i]);
                }
            }
            bfList.add(bitImage);
        }
        return bfList;
    }

    /**
     * 获取图像的统计数据。
     * @param targetImage 需要计算的目标图像
     * @return 该图像的统计数据（均值，中位数，标准差，像素总数）
     */
    public static float[] getStatistics(BufferedImage targetImage){
        float[] statistics = new float[4];
        int[] frequency = new int[256];
        int originalWeight = targetImage.getWidth();
        int originalHeight = targetImage.getHeight();
        //获取频数数组，数组下标表示灰度值，即从0到255，frequency[1] = a表示灰度值为1的频数为a
        for (int height = 0; height < originalHeight; height++) {
            for (int width = 0; width < originalWeight; width++) {
                frequency[targetImage.getRGB(width, height) & 0xFF]++;
            }
        }
        float numbs = 0, sumGray = 0, median = 0, average;
        for (int i = 0;i < frequency.length;i++) {
            numbs += frequency[i];
            sumGray += frequency[i] * i;
        }
        average = sumGray/numbs;
        float tempSum = 0, squareSum = 0;
        for (int i = 0;i < frequency.length;i++) {
            squareSum += Math.pow((frequency[i] - average),2);
            tempSum += frequency[i];
            if (tempSum >= numbs/2){
                median = i;
                break;
            }
        }
        statistics[0] = average;
        statistics[1] = median;
        statistics[2] = (float) Math.pow(squareSum,0.5);
        statistics[3] = numbs;
        return statistics;
    }

    /**
     * 获取指定图像的灰度频数数组。该方法会检测输入的图像类型，如果该图像不是灰度图像，那么本方法会调用
     * <code>toGrayscaleImage(originalImage)</code>方法将其转换为灰度图像。
     *
     * @param originalImage 需要获取灰度频数的图像
     * @return 频数数组，数组下标表示灰度值，即从0到255，frequency[1] = a表示灰度值为1的频数为a
     */
    public static int[] getFrequency(BufferedImage originalImage){
        int[] frequency = new int[256];
        if (originalImage.getType() != BufferedImage.TYPE_BYTE_GRAY){
            originalImage = toGrayscaleImage(originalImage);
        }
        if (originalImage.getType() == BufferedImage.TYPE_BYTE_GRAY){
            int originalWeight = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();
            //获取频数数组，数组下标表示灰度值，即从0到255，frequency[1] = a表示灰度值为1的频数为a
            for (int height = 0; height < originalHeight; height++) {
                for (int width = 0; width < originalWeight; width++) {
                    frequency[originalImage.getRGB(width, height) & 0xFF]++;
                }
            }
        }
        return frequency;
    }

    public static int[] normalizeForHistogram(int[] frequency){
        float maxFre = 0.0f;
        for (int aFrequency: frequency) {
            if (aFrequency > maxFre){
                maxFre = aFrequency;
            }
        }
        //归一化
        for (int i = 0;i < frequency.length;i++) {
            frequency[i] = (int) (frequency[i]/maxFre * 500);
        }
        return frequency;
    }

    /**
     * 获取指定图像的频率数组，用该数组得到的直方图是归一化直方图。
     * @param targetImage 目标图像
     * @return 频率数组
     */
    private static float[] getNormalizedFrequency(BufferedImage targetImage){
        float[] frequency = new float[256];
        int originalWeight = targetImage.getWidth();
        int originalHeight = targetImage.getHeight();
        //获取频数数组，数组下标表示灰度值，即从0到255，frequency[1] = a表示灰度值为1的频数为a
        for (int height = 0; height < originalHeight; height++) {
            for (int width = 0; width < originalWeight; width++) {
                frequency[targetImage.getRGB(width, height) & 0xFF]++;
            }
        }
        for (int i = 0;i < frequency.length;i++) {
            frequency[i] = frequency[i]/(originalHeight * originalWeight);
        }
        return frequency;
    }

    /**
     * 将指定的图像二值化。该方法会检测输入的图像类型，如果该图像不是灰度图像，那么本方法会调用
     * <code>toGrayscaleImage(originalImage)</code>方法将其转换为灰度图像。
     * @param originalImage 需要二值化的图像
     * @param threshold 阈值
     * @return 二值化后的图像
     */
    public static BufferedImage binarization(BufferedImage originalImage, int threshold){
        if (originalImage.getType() != BufferedImage.TYPE_BYTE_GRAY){
            originalImage = toGrayscaleImage(originalImage);
        }
        int originalWeight = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        BufferedImage binaryImage = new BufferedImage(originalWeight,originalHeight,BufferedImage.TYPE_BYTE_BINARY);
        // 二值化
        for (int height = 0; height < originalHeight; height++) {
            for (int width = 0; width < originalWeight; width++) {
                // 大于阈值则转为255
                if ((originalImage.getRGB(width, height) & 0xFF) > threshold){
                    int newValue = 255<<16|255<<8|255;
                    binaryImage.setRGB(width,height,newValue);
                }
                // 否则为0
                else {
                    binaryImage.setRGB(width,height, 0);
                }
            }
        }
        return binaryImage;
    }

    /**
     * 进行线性点运算。该方法会检测输入的图像类型，如果该图像不是灰度图像，那么本方法会调用
     * <code>toGrayscaleImage(originalImage)</code>方法将其转换为灰度图像。
     * @param originalImage 原始图像
     * @return 线性点运算后的图像
     */
    public static BufferedImage linearPointOperation(BufferedImage originalImage, float a,float b){
        if (originalImage.getType() != BufferedImage.TYPE_BYTE_GRAY){
            originalImage = toGrayscaleImage(originalImage);
        }
        int originalWeight = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        BufferedImage newImage = new BufferedImage(originalWeight,originalHeight,BufferedImage.TYPE_BYTE_GRAY);
        for (int height = 0; height < originalHeight; height++) {
            for (int width = 0; width < originalWeight; width++) {
                int value = originalImage.getRGB(width, height) & 0xFF;// 得到灰度值
                int newValue = (int) (a * value + b);
                if (newValue > 255){
                    newValue = 255;
                }
                else if (newValue < 0){
                    newValue = 0;
                }
                newValue = newValue<<16|newValue<<8|newValue;
                newImage.setRGB(width,height,newValue);
            }
        }
        return newImage;
    }

    /**
     * 对图像采用公式 t = log(1 + s) 进行对数变换，其中t为变换后的灰度值，s为变换前的灰度值
     * 对数变换扩展的是图像的较暗部分，如果图像本身灰度值都很大，那么直接进行对数变换对图像的影响很小。
     * 该方法会检测输入的图像类型，如果该图像不是灰度图像，那么本方法会调用
     * <code>toGrayscaleImage(originalImage)</code>方法将其转换为灰度图像。
     * @param originalImage 原始图像
     * @return 对数变换后的图像
     */
    public static BufferedImage logPointOperation(BufferedImage originalImage){
        if (originalImage.getType() != BufferedImage.TYPE_BYTE_GRAY){
            originalImage = toGrayscaleImage(originalImage);
        }
        int originalWeight = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        double[][] tempValue = new double[originalWeight][originalHeight];
        double min = 1000000,max = -1000000;
        BufferedImage newImage = new BufferedImage(originalWeight,originalHeight,BufferedImage.TYPE_BYTE_GRAY);
        for (int height = 0; height < originalHeight; height++) {
            for (int width = 0; width < originalWeight; width++) {
                int value = originalImage.getRGB(width, height) & 0xFF;// 得到灰度值
                tempValue[width][height] = Math.log(value + 1)/Math.log(2);//对数变换
                if (tempValue[width][height] > max){
                    max = tempValue[width][height];
                }
                else if (tempValue[width][height] < min){
                    min = tempValue[width][height];
                }
            }
        }
        for (int height = 0; height < originalHeight; height++) {
            for (int width = 0; width < originalWeight; width++) {
                int newValue = (int) ((tempValue[width][height] - min)/(max - min) * 256);
                newValue = newValue<<16|newValue<<8|newValue;
                newImage.setRGB(width,height,newValue);
            }
        }
        return newImage;
    }

    /**
     * 使用公式 t = s^γ 进行幂次变换，其中t为变换后的灰度值，s为变换前的灰度值
     * 该方法会检测输入的图像类型，如果该图像不是灰度图像，那么本方法会调用
     * <code>toGrayscaleImage(originalImage)</code>方法将其转换为灰度图像。
     * @param originalImage 原始图像
     * @return 对数变换后的图像
     */
    public static BufferedImage powerPointOperation(BufferedImage originalImage, float v){
        if (originalImage.getType() != BufferedImage.TYPE_BYTE_GRAY){
            originalImage = toGrayscaleImage(originalImage);
        }
        int originalWeight = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        double[][] tempValue = new double[originalWeight][originalHeight];
        double min = 1000000,max = -1000000;
        BufferedImage newImage = new BufferedImage(originalWeight,originalHeight,BufferedImage.TYPE_BYTE_GRAY);
        for (int height = 0; height < originalHeight; height++) {
            for (int width = 0; width < originalWeight; width++) {
                int value = originalImage.getRGB(width, height) & 0xFF;// 得到灰度值
                tempValue[width][height] = Math.pow(value,v);//对数变换
                if (tempValue[width][height] > max){
                    max = tempValue[width][height];
                }
                else if (tempValue[width][height] < min){
                    min = tempValue[width][height];
                }
            }
        }
        //归一化处理
        for (int height = 0; height < originalHeight; height++) {
            for (int width = 0; width < originalWeight; width++) {
                int newValue = (int) ((tempValue[width][height] - min)/(max - min) * 256);
                newValue = newValue<<16|newValue<<8|newValue;
                newImage.setRGB(width,height,newValue);
            }
        }
        return newImage;
    }

    /**
     * 进行传统直方图均衡化
     * @param originalImage 原始图像
     * @return 均衡化后的图像
     */
    public static BufferedImage traditionalHistogramEqualization(BufferedImage originalImage){
        float[] frequency = getNormalizedFrequency(originalImage);
        float[] s = frequency.clone();
        for (int i = 1; i <= 255; i++) {
            s[i] += s[i-1];
        }
        int[] newValue = new int[256];
        for (int i = 0; i < 256; i++) {
            newValue[i] = Math.round(s[i] * 255);
        }

        int originalWeight = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        BufferedImage newImage = new BufferedImage(originalWeight,originalHeight,BufferedImage.TYPE_BYTE_GRAY);
        for (int height = 0; height < originalHeight; height++) {
            for (int width = 0; width < originalWeight; width++) {
                int value = originalImage.getRGB(width, height) & 0xFF;// 得到灰度值
                value = newValue[value];
                newImage.setRGB(width,height,value<<16|value<<8|value);
            }
        }

        return newImage;
    }

    /**
     * 图像相减，会对相减的结果进行对数运算得到本人认为更清晰的结果。
     * @param minuendImg 被减数图像
     * @param subtrahendImg 减数图像
     * @return 结果灰度图像
     */
    public static BufferedImage subtraction(BufferedImage minuendImg, BufferedImage subtrahendImg){
        int width = Math.min(minuendImg.getWidth(),subtrahendImg.getWidth());
        int height = Math.min(minuendImg.getHeight(), subtrahendImg.getHeight());
        BufferedImage resultImg = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int value = (minuendImg.getRGB(j, i) & 0xFF) - (subtrahendImg.getRGB(j, i) & 0xFF);// 得到灰度值
                if (value < 0){
                    value = 0;
                }
                resultImg.setRGB(j,i,value<<16|value<<8|value);
            }
        }
        return logPointOperation(resultImg);
    }

    /**
     * 进行图像的旋转，使用最近邻插值。
     * @param originalImage 原始图像
     * @param x 旋转中心点x坐标
     * @param y 旋转中心点y坐标
     * @param theta 旋转角度
     * @return 旋转后的图像
     */
    public static BufferedImage rotate(BufferedImage originalImage,double x,double y,double theta){
        // 获取旋转矩阵
        var transform = AffineTransform.getRotateInstance(Math.toRadians(theta), x, y);
        // 进行旋转
        var op = new AffineTransformOp(transform,AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(originalImage,null);
    }

    /**
     * 进行图像的缩放，使用最近邻插值。
     * @param originalImage 原始图像
     * @param x 在x方向上缩放的倍数
     * @param y 在y方向上缩放的倍数
     * @return 缩放后的图像
     */
    public static BufferedImage scale(BufferedImage originalImage,double x,double y){
        // 获取缩放矩阵
        var transform = AffineTransform.getScaleInstance(x,y);
        // 进行缩放
        var op = new AffineTransformOp(transform,AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(originalImage,null);
    }

    /**
     * 使用邻域平均法进行图像平滑，使用7*7的窗口进行平滑
     * 该方法会检测输入的图像类型，如果该图像不是灰度图像，那么本方法会调用
     * <code>toGrayscaleImage(originalImage)</code>方法将其转换为灰度图像。
     * @param originalImage 原始图像
     * @return 平滑后的图像
     */
    public static BufferedImage neighborhoodAveraging(BufferedImage originalImage){
        if (originalImage.getType() != BufferedImage.TYPE_BYTE_GRAY){
            originalImage = toGrayscaleImage(originalImage);
        }
        int originalWeight = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        BufferedImage newImage = new BufferedImage(originalWeight,originalHeight,BufferedImage.TYPE_BYTE_GRAY);
        for (int height = 0; height < originalHeight; height++) {
            for (int width = 0; width < originalWeight; width++) {
                int value;
                if (height - 3 < 0 || height + 4 > originalHeight || width - 3 < 0 || width + 4 > originalWeight){
                    value = originalImage.getRGB(width, height);
                    newImage.setRGB(width,height,value);
                }
                else {
                    int sum = 0;
                    for (int i = height - 3;i <= height + 3;i++){
                        for (int j = width - 3;j <= width + 3;j++){
                            sum+= originalImage.getRGB(j, i) & 0xFF;// 得到灰度值
                        }
                    }
                    value = (int) (sum/49.0);
                    newImage.setRGB(width,height,value<<16|value<<8|value);
                }
            }
        }

        return newImage;
    }

    /**
     * k近邻均值滤波平滑。使用7*7的正方形窗口，取K值为25
     * @param originalImage 原始图像
     * @return 平滑后的图像
     */
    public static BufferedImage kNearestNeighborMeanFilter(BufferedImage originalImage){
        if (originalImage.getType() != BufferedImage.TYPE_BYTE_GRAY){
            originalImage = toGrayscaleImage(originalImage);
        }
        int originalWeight = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        BufferedImage newImage = new BufferedImage(originalWeight,originalHeight,BufferedImage.TYPE_BYTE_GRAY);
        for (int height = 0; height < originalHeight; height++) {
            for (int width = 0; width < originalWeight; width++) {
                int value = originalImage.getRGB(width, height) & 0xFF;
                if (height - 3 < 0 || height + 4 > originalHeight || width - 3 < 0 || width + 4 > originalWeight){
                    value = originalImage.getRGB(width, height);
                    newImage.setRGB(width,height,value);
                }
                else {
                    ArrayList<DiffClass> diffClasses = new ArrayList<>();
                    for (int i = height - 3;i <= height + 3;i++){
                        for (int j = width - 3;j <= width + 3;j++){
                            if (i != height || j != width){
                                int temp = originalImage.getRGB(j, i) & 0xFF;
                                int diff = Math.abs(value - temp);
                                DiffClass diffClass = new DiffClass(temp,diff);
                                diffClasses.add(diffClass);
                            }
                        }
                    }
                    diffClasses.sort((o1, o2) -> {
                        if (o1.b < o2.b){
                            return -1;
                        }
                        else if (o1.b > o2.b){
                            return 1;
                        }
                        return 0;
                    });
                    int sum = 0;
                    for (int i = 0;i < 25;i++){
                        sum += diffClasses.get(i).a;
                    }
                    value = (int) (sum/25.0);
                    newImage.setRGB(width,height,value<<16|value<<8|value);
                }
            }
        }

        return newImage;
    }

    /**
     * 中值滤波。使用7*7的正方形窗口。
     * @param originalImage 原始图像
     * @return 结果灰度图像
     */
    public static BufferedImage medianFiltering(BufferedImage originalImage){
        if (originalImage.getType() != BufferedImage.TYPE_BYTE_GRAY){
            originalImage = toGrayscaleImage(originalImage);
        }
        int originalWeight = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        BufferedImage newImage = new BufferedImage(originalWeight,originalHeight,BufferedImage.TYPE_BYTE_GRAY);
        for (int height = 0; height < originalHeight; height++) {
            for (int width = 0; width < originalWeight; width++) {
                int value;
                if (height - 3 < 0 || height + 3 > originalHeight - 1
                        || width - 3 < 0 || width + 3 > originalWeight - 1){
                    value = originalImage.getRGB(width, height);
                    newImage.setRGB(width,height,value);
                }
                else {
                    int[] arr =new int[49];
                    int k = 0;
                    for (int i = height - 3;i <= height + 3;i++){
                        for (int j = width - 3;j <= width + 3;j++){
                            if (i != height || j != width){
                                arr[k] = originalImage.getRGB(j, i) & 0xFF;
                                k++;
                            }
                        }
                    }
                    Arrays.sort(arr);
                    value = arr[24];
                    newImage.setRGB(width,height,value<<16|value<<8|value);
                }
            }
        }

        return newImage;
    }

    /**
     * 图像相加。如果图像不是灰度图像，该方法会把RGB模型中的B相加。
     * @param imageA 待相加的图像A
     * @param imageB 待相加的图像B
     * @return 相加结果
     */
    public static BufferedImage addition(BufferedImage imageA, BufferedImage imageB){
        int width = Math.min(imageA.getWidth(),imageB.getWidth());
        int height = Math.min(imageA.getHeight(), imageB.getHeight());
        BufferedImage resultImg = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int value = (imageA.getRGB(j, i) & 0xFF) + (imageB.getRGB(j, i) & 0xFF);// 得到灰度值
                if (value > 255){
                    value = 255;
                }
                resultImg.setRGB(j,i,value<<16|value<<8|value);
            }
        }
        return resultImg;
    }

    /**
     * 对图像进行卷积运算。超过255的灰度值会被截断为255。模型的行数和列数应该为奇数。
     * @param model 以二维数组表示的模型
     * @param originalImage 需要进行卷积运算的图像
     * @return 卷积后生成的图像的一维数组
     */
    public static int[] convolution(int[][] model, BufferedImage originalImage){
        int rows = model.length;
        int columns = model[0].length;
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int[] originalPixel = new int[width*height];
        originalImage.getRGB(0, 0, width, height, originalPixel, 0, width);
        int[] convolutionPixel = new int[width*height];
        for (int h = 0; h < height; h++){
            for (int w = 0; w < width; w++){
                if (h < rows/2 || w < columns/2 || width-1 < w+columns/2 || height-1 < h+rows/2){
                    convolutionPixel[h*width+w] = originalPixel[h*width+w];
                }
                else {
                    int pixel = originalPixel[h*width+w];
                    int alpha = pixel & 0xff000000;
                    int gray = 0;
                    for (int i = 0;i < rows;i++){
                        for (int j = 0;j < columns;j++){
                            int realH,realW;
                            realH = h - (rows/2 - i);
                            realW = w - (columns/2 - j);
                            gray += model[i][j]*(originalPixel[realH*width+realW] & 0xff);
                        }
                    }
                    gray = Math.abs(gray);
                    if (gray > 255)
                        gray = 255;
                    convolutionPixel[h*width+w] = alpha | (gray<<16) | (gray<<8) | gray;
                }
            }
        }
        return convolutionPixel;

    }

    /**
     * 使用输入的图像像素数组得到一个图像。
     * @param pixel 图像像素数组
     * @param bufferedImage 得到的图像使用该传入图像的大小参数
     * @return 一个与传入的像素数组对应的新的灰度图像
     */
    public static BufferedImage pixelToImage(int[] pixel,BufferedImage bufferedImage){
        BufferedImage resultImage = new BufferedImage(bufferedImage.getWidth(),bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        resultImage.setRGB(0,0,bufferedImage.getWidth(),bufferedImage.getHeight(),
                pixel,0,bufferedImage.getWidth());
        return resultImage;
    }

    /**
     * sobel算子进行卷积。使用平方和再开方计算最终结果。
     * @param originalImage 待卷积的图像
     * @return 卷积后的图像像素数组
     */
    public static int[] sobel(BufferedImage originalImage){
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int[] pixelArray = new int[width*height];
        originalImage.getRGB(0, 0, width, height, pixelArray, 0, width);
        int[] grayArray = new int[width*height];
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                if(0==i || 0==j || width-1==j || height-1==i){
                    grayArray[i*width+j] = pixelArray[i*width+j];
                }else{
                    int pixel = pixelArray[i*width+j];
                    int alpha = pixel & 0xff000000;
                    int[] sourceMatrix = new int[9];
                    sourceMatrix[0] = pixelArray[(i-1)*width+j-1] & 0XFF;
                    sourceMatrix[1] = pixelArray[(i-1)*width+j] & 0XFF;
                    sourceMatrix[2] = pixelArray[(i-1)*width+j+1] & 0XFF;
                    sourceMatrix[3] = pixelArray[i*width+j-1] & 0XFF;
                    sourceMatrix[4] = pixelArray[i*width+j] & 0XFF;
                    sourceMatrix[5] = pixelArray[i*width+j+1] & 0XFF;
                    sourceMatrix[6] = pixelArray[(i+1)*width+j-1] & 0XFF;
                    sourceMatrix[7] = pixelArray[(i+1)*width+j] & 0XFF;
                    sourceMatrix[8] = pixelArray[(i+1)*width+j+1] & 0XFF;
                    int gy = sourceMatrix[6]+2*sourceMatrix[7]+sourceMatrix[8]
                            -(sourceMatrix[0]+2*sourceMatrix[1]+sourceMatrix[2]);
                    int gx = sourceMatrix[0]+2*sourceMatrix[3]+sourceMatrix[6]
                            -(sourceMatrix[2]+2*sourceMatrix[5]+sourceMatrix[8]);
                    int temp = (int) Math.pow(Math.pow(gx,2)+Math.pow(gy,2),0.5);
                    int gray = Math.abs(temp);
                    if (gray > 255) gray = 255;
                    grayArray[i*width+j] = alpha | (gray<<16) | (gray<<8) | gray;
                }
            }
        }
        pixelArray = grayArray;
        return pixelArray;
    }

    /**
     * laplacian算子进行卷积。使用算子模板为（0，-1，0）（-1，4，-1）（0，-1，0）
     * @param originalImage 待卷积的图像
     * @return 卷积后的图像像素数组
     */
    public static int[] laplacian(BufferedImage originalImage){
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int[] pixelArray = new int[width*height];
        originalImage.getRGB(0, 0, width, height, pixelArray, 0, width);
        int[] grayArray = new int[width*height];
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                if(0==i || 0==j || width-1==j || height-1==i){
                    grayArray[i*width+j] = pixelArray[i*width+j];
                }else{
                    int pixel = pixelArray[i*width+j];
                    int alpha = pixel & 0xff000000;
                    int[] sourceMatrix = new int[9];
                    sourceMatrix[0] = pixelArray[(i-1)*width+j-1] & 0XFF;
                    sourceMatrix[1] = pixelArray[(i-1)*width+j] & 0XFF;
                    sourceMatrix[2] = pixelArray[(i-1)*width+j+1] & 0XFF;
                    sourceMatrix[3] = pixelArray[i*width+j-1] & 0XFF;
                    sourceMatrix[4] = pixelArray[i*width+j] & 0XFF;
                    sourceMatrix[5] = pixelArray[i*width+j+1] & 0XFF;
                    sourceMatrix[6] = pixelArray[(i+1)*width+j-1] & 0XFF;
                    sourceMatrix[7] = pixelArray[(i+1)*width+j] & 0XFF;
                    sourceMatrix[8] = pixelArray[(i+1)*width+j+1] & 0XFF;
                    int temp = 4*sourceMatrix[4]-sourceMatrix[7]-sourceMatrix[1]-sourceMatrix[3]-sourceMatrix[5];
                    int gray = (pixel&0xFF) + Math.abs(temp);
                    if (gray > 255) gray = 255;
                    grayArray[i*width+j] = alpha | (gray<<16) | (gray<<8) | gray;
                }
            }
        }
        pixelArray = grayArray;
        return pixelArray;
    }

    /**
     * roberts算子进行卷积。使用平方和再开方计算最终结果。
     * @param originalImage 待卷积的图像
     * @return 卷积后的图像像素数组
     */
    public static int[] roberts(BufferedImage originalImage){
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int[] pixelArray = new int[width*height];
        originalImage.getRGB(0, 0, width, height, pixelArray, 0, width);
        int[] grayArray = new int[width*height];
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                if(0==i || 0==j || width-1==j || height-1==i){
                    grayArray[i*width+j] = pixelArray[i*width+j];
                }else{
                    int pixel = pixelArray[i*width+j];
                    int alpha = pixel & 0xff000000;
                    int[] sourceMatrix = new int[9];
                    sourceMatrix[0] = pixelArray[(i-1)*width+j-1] & 0XFF;
                    sourceMatrix[1] = pixelArray[(i-1)*width+j] & 0XFF;
                    sourceMatrix[2] = pixelArray[(i-1)*width+j+1] & 0XFF;
                    sourceMatrix[3] = pixelArray[i*width+j-1] & 0XFF;
                    sourceMatrix[4] = pixelArray[i*width+j] & 0XFF;
                    sourceMatrix[5] = pixelArray[i*width+j+1] & 0XFF;
                    sourceMatrix[6] = pixelArray[(i+1)*width+j-1] & 0XFF;
                    sourceMatrix[7] = pixelArray[(i+1)*width+j] & 0XFF;
                    sourceMatrix[8] = pixelArray[(i+1)*width+j+1] & 0XFF;
                    int gy = sourceMatrix[7] - sourceMatrix[5];
                    int gx = sourceMatrix[8] - sourceMatrix[4];
                    int gray = (pixel&0xFF) + (int) Math.pow(Math.pow(gx,2)+Math.pow(gy,2),0.5);
                    if (gray > 255) gray = 255;
                    grayArray[i*width+j] = alpha | (gray<<16) | (gray<<8) | gray;
                }
            }
        }
        pixelArray = grayArray;
        return pixelArray;
    }

    /**
     * 霍夫变换检测直线（极坐标系方法），阈值为0.5。
     * @param binaryImage 原始二值图像
     * @return 变换后图像
     */
    public static BufferedImage houghTransform(BufferedImage binaryImage){
        int height = binaryImage.getHeight();
        int width = binaryImage.getWidth();
        int[] pixelArray = new int[width*height];
        int[] grayArray = new int[width*height];
        binaryImage.getRGB(0, 0, width, height, pixelArray, 0, width);
        for (int i = 0;i < pixelArray.length;i++) {
            grayArray[i] = pixelArray[i] & 0xFF;
        }
        int roMax = (int)Math.sqrt(height*height+width*width) + 1;
        int theta = 180;
        int[][] hist = new int[roMax][theta];

        double roValue;
        int transValue;
        for(int k=0;k<theta;k++){
            for(int i=0;i<height;i++){
                for(int j=0;j<width;j++){
                    if(grayArray[j+i*width] != 0){
                        roValue = j*Math.cos(k*Math.PI/theta)+i*Math.sin(k*Math.PI/theta);
                        transValue = (int)(roValue / 2 + roMax / 2);
                        hist[transValue][k]++;
                    }
                }
            }
        }
        //找到大于最大值*0.5的二维直方图的点
        ArrayList<HoughResult> index = new ArrayList<>();
        int max = 0;

        for (int[] ints : hist) {
            for (int anInt : ints) {
                if (max < anInt) {
                    max = anInt;
                }
            }
        }

        for(int i1=0;i1<hist.length;i1++){
            for(int j1=0;j1<hist[i1].length;j1++){
                if(hist[i1][j1] > max*0.5)
                    index.add(new HoughResult(i1,j1));
            }
        }

        for (HoughResult houghResult : index) {
            double resTheta = houghResult.angle * Math.PI / theta;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    roValue = j * Math.cos(resTheta) + i * Math.sin(resTheta);
                    transValue = (int)(roValue / 2 + roMax / 2);
                    if (grayArray[j + i * width] != 0 && transValue == houghResult.ro) {
                        grayArray[j + i * width] = 0xffff0000;   //在直线上的点设为红色
                    } else {
                        grayArray[j + i * width] = (255 << 24)
                                | (grayArray[j + i * width] << 16)
                                | (grayArray[j + i * width] << 8)
                                | (grayArray[j + i * width]);
                    }
                }
            }

        }
        return pixelToImage(grayArray,binaryImage);
    }

    /**
     * 哈夫曼编码压缩
     * @param originalImage 待压缩的图像
     * @param codes 编码
     * @return 压缩后的数据
     */
    public static String[] huffmanCodingCompression(BufferedImage originalImage,String[] codes){
        if (originalImage.getType() != BufferedImage.TYPE_BYTE_GRAY){
            originalImage = toGrayscaleImage(originalImage);
        }
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();
        int[] pixelArray = new int[width*height];
        int[] grayArray = new int[width*height];
        originalImage.getRGB(0, 0, width, height, pixelArray, 0, width);
        for (int i = 0;i < pixelArray.length;i++) {
            grayArray[i] = pixelArray[i] & 0xFF;
        }
        //获取频率数组
        int[] frequency = getFrequency(originalImage);
        MyHeap<MyTree> heap = new MyHeap<>();

        //构造huffman树
        for(int i=0; i<256; i++){
            if(frequency[i]!=0){
                heap.add(new MyTree(frequency[i],(char)i));
            }
        }
        while(heap.getSize() > 1){
            MyTree t1 = heap.remove();
            MyTree t2 = heap.remove();
            heap.add(new MyTree(t1,t2));
        }
        MyTree huffmanTree = heap.remove();
        if(huffmanTree.root == null)
            return null;

        //获取编码表
        getCodingTable(huffmanTree.root,codes);
        String[] data = new String[grayArray.length];
        int i = 0;
        for (int gray: grayArray) {
            data[i] = codes[gray];
            i++;
        }

        return data;
    }

    /**
     * 获取编码表
     * @param root 树的根节点
     * @param codes 编码表数组
     */
    private static void getCodingTable(MyTree.Node root, String[] codes){
        if(root.left != null){
            root.left.code = root.code + "0";
            getCodingTable(root.left,codes);

            root.right.code = root.code + "1";
            getCodingTable(root.right,codes);
        }
        else{
            codes[root.element] = root.code;
        }
    }

    /**
     * 哈夫曼解码
     * @param src 文件路径
     * @return 解码后的图像
     * @throws IOException IO异常
     */
    public static BufferedImage decompression(String src) throws IOException {
        DataInputStream input = new DataInputStream(new FileInputStream(src));
        int w = input.readInt();
        int h = input.readInt();
        String[] codes = new String[256];
        Arrays.fill(codes,null);
        int counts = input.readInt();
        for (int i = 0; i < counts; i++) {
            int gray = input.readInt();
            codes[gray] = "";
            //编码字符串长度
            int length = input.readInt();
            //编码字符串所占字节数
            int byteNumbs = (int) Math.ceil(length/8.0);

            byte[] codeByte = new byte[byteNumbs];
            // 得到编码表对应位置的编码
            for (int j = 0; j < byteNumbs; j++) {
                codeByte[j] = input.readByte();
                String codeStr = byteToBit(codeByte[j]);
                if (j + 1 == byteNumbs && length % 8 != 0){
                    codeStr = codeStr.substring(0,length % 8);
                }
                codes[gray] = codes[gray].concat(codeStr);

            }
        }
        int[] pixel = new int[w*h];
        byte[] remainingBytes = input.readAllBytes();
        String strA = byteToBit(remainingBytes[0]);
        int i = 0, j = 0;
        while (true) {
            boolean hasFind = false;
            for (int gray = 0; gray < codes.length; gray++) {
                if (codes[gray] != null && strA.indexOf(codes[gray]) == 0) {
                    hasFind = true;
                    pixel[j] = (255<<24) | (gray<<16) | (gray<<8) | gray;
                    //System.out.println(pixel[j] + ",gray = " + gray + "," + codes[gray]);
                    j++;
                    int index = strA.indexOf(codes[gray]) + codes[gray].length();
                    strA = strA.substring(index);
                    break;
                }
            }
            if (i == remainingBytes.length - 1 && !hasFind){
                break;
            }
            if (!hasFind || strA.equals("")){
                i++;
                if (i < remainingBytes.length){
                    strA = strA.concat(byteToBit(remainingBytes[i]));
                }
            }

        }
        BufferedImage resImage = new BufferedImage(w,h,BufferedImage.TYPE_BYTE_GRAY);
        resImage.setRGB(0,0,w,h,pixel,0,w);
        return resImage;
    }

    /**
     * 字节转位字符串
     * @param b 待转换字节
     * @return 位字符串
     */
    public static String byteToBit(byte b) {
        return "" +(byte)((b >> 7) & 0x1) +
                (byte)((b >> 6) & 0x1) +
                (byte)((b >> 5) & 0x1) +
                (byte)((b >> 4) & 0x1) +
                (byte)((b >> 3) & 0x1) +
                (byte)((b >> 2) & 0x1) +
                (byte)((b >> 1) & 0x1) +
                (byte)((b) & 0x1);
    }
}
