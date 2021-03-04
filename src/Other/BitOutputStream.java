package Other;

import java.io.*;

/**
 * 一个BitOutputStream类
 * 作用：能将比特流写入到输出流当中
 */
public class BitOutputStream{
    private final FileOutputStream output;
    private int value = 0;
    private int count = 0;  //标记填充比特的个数
    /**构造方法 */
    public BitOutputStream(File file)throws IOException{
        output = new FileOutputStream(file);
    }
    /**存储一个字节变量形式的比特
     * 每次只能写入一个字节的数据
     * 所以每存满一个字节的比特，就写入
     */
    public void writeBit(char bit)throws IOException{
        count ++;
        value = value << 1;
        if(bit == '1'){
            int mask = 1;
            value = value | mask;
        }
        if(count == 8){
            output.write(value);
            count = 0; //在写入一个字节后 填充比特数置为0
        }
    }

    /**
     * 调用writeBit(char bit)字符串划分成单个字符
     */
    public void writeBit(String bit)throws IOException{
        for(int i=0; i<bit.length(); i++){
            writeBit(bit.charAt(i));
        }
    }

    /**
     * 向输出流中输出整数，以4字节存储。
     * @param i 要输出的整数
     * @throws IOException 抛出IO异常
     */
    public void writeInt(int i) throws IOException{
        String value = Integer.toBinaryString(i);
        String zero = "";
        for (int j = 0;j < 32 - value.length();j++){
            zero = zero.concat("0");
        }
        zero = zero.concat(value);
        for(int j = 0; j < 32;j++){
            writeBit(zero.charAt(j));
        }
    }

    public void writeToByte(String str) throws IOException{
        int length = str.length();
        if (length % 8 != 0){
            for(int i = 0;i < 8 - length % 8;i++){
                str = str.concat("0");
            }
        }
        writeBit(str);
    }

    /**
     * 最后的字节既未填满也未空 必须调用close()写入剩余比特流
     */
    public void close()throws IOException{
        if(count > 0){
            value = value << (8 - count);
            output.write(value);//写入低位一个字节
        }
        output.close();
    }
}
