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
        out.print("������������Ե����ֻ�����:");
        String names = sanner.next();
        if (names == null || names == "") {
            logger.info("����������");
            System.exit(-1);
            return;
        }
        if (names.length() > 3) {
            logger.info("��������ֲ��ܳ�����������!����������.");
            System.exit(-1);
            return;
        }
        int countSum = 0;
        for (int i = 0; i < names.length(); i++) {
            String name = names.substring(i, i + 1);
            NameEntitiy nameEntitiy = nameMap.get(name);
            if (nameEntitiy == null){
                System.out.print("��"+i+"������û���ҵ�,�޷�����.");
                System.exit(-1);
                return;
            }
            int sum = nameEntitiy.getSum();

            if (names.length() == 3) {
                if (i == 0) {
                    countSum += getCountSum(sum,100);
                    logger.info(name+":һ�� "+getCountSum(sum,100)+" ��");
                }else if(i== 1){
                    countSum += getCountSum(sum,10);
                    logger.info(name+":һ�� "+getCountSum(sum,10)+" ��");
                }else if (i ==2){
                    countSum += sum;
                    logger.info(name+":һ�� "+sum+" ��");
                }
            }else if(names.length() == 2){
                if (i == 0) {
                    countSum += getCountSum(sum,100);
                    logger.info(name+":һ�� "+getCountSum(sum,100)+" ��");
                }else if(i== 1){
                    countSum += getCountSum(sum,10);
                    logger.info(name+":һ�� "+getCountSum(sum,10)+" ��");
                }
            }else if (names.length() ==1){
                countSum += getCountSum(sum,100);
                logger.info(name+":һ�� "+countSum+" ��");
            }
        }
        logger.info("��:"+countSum+"��");

        String godNumber = godBookMap.get(String.valueOf(countSum % 215));
        if (godNumber !=null){
            logger.info("¼������:"+names);
            logger.info("��������������:"+countSum % 215 +" "+ godNumber);
        }else{
            logger.info("����:"+countSum % 215+",���޴˽��,�����²�ѯ.");
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
     * ����ת���� unicode
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
                // Ӣ�ļ����ֵ�
                sb.append(myBuffer[i]);
            } else if (ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                // ȫ�ǰ���ַ�
                int j = myBuffer[i] - 65248;
                sb.append((char) j);
            } else {
                // ����
                int s = myBuffer[i];
                String hexS = Integer.toHexString(Math.abs(s));
                String unicode = "\\u" + hexS;
                sb.append(unicode.toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * unicode ת���� ����
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
