
import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class WordCount {
    public static String inFileName;//待统计文件名
    public static String outFileName;//输出文件名
    public static int words = 0;
    public static int lines = 1;
    public static int chars = 0;
    public static int count_c = 0;//参数是否要求统计字符
    public static int count_w = 0;//参数是否要求统计单词
    public static int count_l = 0;//参数是否要求统计行数
    public static int count_o = 0;//参数是否要求输出到文件
    public static int state = 0;//记录是否出现-o
    //统计单词，字符，行数函数
    public static void wc(InputStream f) throws IOException {
        int c = 0;
        int last = 0;
        boolean lastNotWhite = false;
        while ((c = f.read()) != -1) {

            chars++;
            if (c == '\n')//换行符，行数加一
            {
                lines++;
            }

            if (last != ' ' && last != ',' && last != 0 && last != '\t' && last != '\r') {
                lastNotWhite = true;
            } else
                lastNotWhite = false;
            //如果为单词分隔符，且前一个为非空格，‘，’，换行，tab字符，则为一个单词
            if ((c == ' ' || c == ',' || c == '\t' || c == '\r') && lastNotWhite == true)
                words++;
            last = c;
        }
        if ((c == -1 && last != '\n')) {
            words++;
            if (c == -1 && last == 0)
                words--;
        }
    }
    //记录参数状态函数
    public static void keepParam(String args[]) throws FileNotFoundException {

        try {
            if (args.length != 0) {
                int param = args.length;
                for (int i = 0; i < param; i++) {
                    if (args[i].equals("-c"))
                        count_c = 1;
                    else if (args[i].equals("-w"))
                        count_w = 1;
                    else if (args[i].equals("-l"))
                        count_l = 1;
                    else if (args[i].equals("-o")) {
                        count_o = 1;
                        state = 1;
                    } else {
                        if (state == 0)
                            inFileName = args[i];
                        if (state == 1)
                            outFileName = args[i];
                    }
                }
                FileInputStream file = new FileInputStream(inFileName);
                wc(file);
            }
        } catch (IOException e1) {
            return;
        }

    }
    //打印结果函数
    public static void printResult() throws FileNotFoundException {

        if (count_c == 1)
            System.out.println(inFileName+","+"字符数："+ chars + '\n');
        if (count_w == 1)
            System.out.println(inFileName+","+"单词数："+ words + '\n');
        if (count_l == 1)
            System.out.println(inFileName+","+"行数："+ lines + '\n');
        if (state == 1) {
            FileWriter fw = null;
            try {
                //如果文件存在，则追加内容；如果文件不存在，则创建文件
                File f = new File(outFileName);
                fw = new FileWriter(f, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            PrintWriter pw = new PrintWriter(fw);
            if (count_c == 1)
                pw.println(inFileName+","+"字符数："+ chars + '\n');
            if (count_w == 1)
                pw.println(inFileName+","+"单词数："+ words + '\n');
            if (count_l == 1)
                pw.println(inFileName+","+"行数："+ lines + '\n');
            pw.flush();
            try {
                fw.flush();
                pw.close();
                fw.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }

        }
    }

    public static void main(String args[]) throws FileNotFoundException {
        keepParam(args);
        printResult();
    }
}
