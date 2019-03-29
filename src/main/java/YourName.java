import com.neo.GodBook;
import com.neo.NameEntitiy;
import org.apache.log4j.Logger;

import java.io.*;
import java.lang.Character.UnicodeBlock;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class YourName {
    private static Logger logger = Logger.getLogger(YourName.class);
    private static Map<String, NameEntitiy> nameMap = new HashMap<String, NameEntitiy>();
    private static Map<String, String> godBookMap = new HashMap<String, String>();

    public YourName() {
        this.nameMap = nameMapInit();
        this.godBookMap = godBookMapInit();
    }

    public static void main(String[] args) {
        new YourName();
        PrintStream out = System.out;
        InputStream inputStream = System.in;
        Scanner sanner = new Scanner(inputStream);
        out.print("请输入你想测试的名字或文字:");
        String names = sanner.next();
        if (names == null || names == "") {
            logger.info("请输入文字");
            System.exit(-1);
            return;
        }
        if (names.length() > 3) {
            logger.info("输入的文字不能超过三个文字!请重新输入.");
            System.exit(-1);
            return;
        }
        int countSum = 0;
        for (int i = 0; i < names.length(); i++) {
            String name = names.substring(i, i + 1);
            NameEntitiy nameEntitiy = nameMap.get(name);
            if (nameEntitiy == null){
                System.out.print("第"+i+"个汉字没有找到,无法测试.");
                System.exit(-1);
                return;
            }
            int sum = nameEntitiy.getSum();

            if (names.length() == 3) {
                if (i == 0) {
                    countSum += getCountSum(sum,100);
                    logger.info(name+":一共 "+getCountSum(sum,100)+" 数");
                }else if(i== 1){
                    countSum += getCountSum(sum,10);
                    logger.info(name+":一共 "+getCountSum(sum,10)+" 数");
                }else if (i ==2){
                    countSum += sum;
                    logger.info(name+":一共 "+sum+" 数");
                }
            }else if(names.length() == 2){
                if (i == 0) {
                    countSum += getCountSum(sum,100);
                    logger.info(name+":一共 "+getCountSum(sum,100)+" 数");
                }else if(i== 1){
                    countSum += getCountSum(sum,10);
                    logger.info(name+":一共 "+getCountSum(sum,10)+" 数");
                }
            }else if (names.length() ==1){
                countSum += getCountSum(sum,100);
                logger.info(name+":一共 "+countSum+" 数");
            }
        }
        logger.info("共:"+countSum+"数");

        String godNumber = godBookMap.get(String.valueOf(countSum % 215));
        if (godNumber !=null){
            logger.info("录入文字:"+names);
            logger.info("诸葛武侯巧连神数:"+countSum % 215 +" "+ godNumber);
        }else{
            logger.info("余数:"+countSum % 215+",查无此结果,请重新查询.");
        }
    }

    private static int getCountSum(int sum,int x) {
//        if (sum < 10) {
//            countSum += sum * x;
//        } else if (sum > 10) {
//            countSum += (sum - 10) * x;
//        } else if (sum % 10 == 0) {
//            countSum += (sum / 10) * x;
//        }
        int countSum = sum * x;
        return countSum;
    }

    public Map<String, NameEntitiy> nameMapInit() {
//        String dicPath = YourName.class.getClassLoader().getResource("/nameDic.txt").getPath();
//        String dicPath = this.getClass().getResourceAsStream("/nameDic.txt");
        try {
//            BufferedReader br = new BufferedReader(new FileReader(new File(dicPath)));
            BufferedReader br =
                    new BufferedReader(
                            new InputStreamReader(
                                    this.getClass().getResourceAsStream("/nameDic.txt"), "UTF-8"));
            String tempString = null;
            int count = 0;
            while ((tempString = br.readLine()) != null) {
                count++;
                String[] lineValue = tempString.trim().split(",");
                if (count == 1) continue;
                NameEntitiy nameEntitiy = new NameEntitiy();
                nameEntitiy.setId(lineValue[0]);
                nameEntitiy.setChinese(lineValue[1]);
                nameEntitiy.setSum(Integer.valueOf(lineValue[2]));
                nameEntitiy.setStroke(lineValue[3]);
                nameEntitiy.setUNicode(lineValue[4]);
                nameEntitiy.setGB(lineValue[5]);
                nameMap.put(nameEntitiy.getChinese(), nameEntitiy);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return nameMap;
    }

    public Map<String, String> godBookMapInit() {
//        String dicPath = YourName.class.getClassLoader().getResource("/godbook.csv").getPath();
        try {
//            BufferedReader br = new BufferedReader(new FileReader(new File(dicPath)));
            BufferedReader br =
                    new BufferedReader(
                            new InputStreamReader(
                                    this.getClass().getResourceAsStream("godbook.csv")));
            String tempString = null;
            int count = 0;
            while ((tempString = br.readLine()) != null) {
                count++;
                String[] lineValue = tempString.trim().split(",");
                if (count == 1) continue;
                GodBook godBook = new GodBook();
                godBook.setId(lineValue[0]);
                godBook.setSign(lineValue[1]);
                godBookMap.put(godBook.getId(), godBook.getSign());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return godBookMap;
    }

    /**
     * 中文转换成 unicode
     *
     * @param inStr
     * @return
     * @author leon 2016-3-15
     */
    public static String encodeUnicode(String inStr) {
        char[] myBuffer = inStr.toCharArray();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < inStr.length(); i++) {
            char ch = myBuffer[i];
            if (ch < 10) {
                sb.append("\\u000" + (int) ch);
                continue;
            }
            UnicodeBlock ub = UnicodeBlock.of(ch);
            if (ub == UnicodeBlock.BASIC_LATIN) {
                // 英文及数字等
                sb.append(myBuffer[i]);
            } else if (ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                // 全角半角字符
                int j = myBuffer[i] - 65248;
                sb.append((char) j);
            } else {
                // 汉字
                int s = myBuffer[i];
                String hexS = Integer.toHexString(Math.abs(s));
                String unicode = "\\u" + hexS;
                sb.append(unicode.toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * unicode 转换成 中文
     *
     * @param theString
     * @return
     * @author leon 2016-3-15
     */
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') aChar = '\t';
                    else if (aChar == 'r') aChar = '\r';
                    else if (aChar == 'n') aChar = '\n';
                    else if (aChar == 'f') aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

}
