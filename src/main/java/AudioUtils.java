import com.baidu.aip.speech.AipSpeech;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import org.json.JSONObject;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * ��������:2018��1��14��
 * ����ʱ��:����10:09:39
 * ������    :yellowcong
 * ���ܸ�Ҫ:MP3תPCM Java��ʽʵ��
 * http://ai.baidu.com/forum/topic/show/496972
 */
public class AudioUtils {
    private static AudioUtils audioUtils = null;
    private AudioUtils(){}

    //˫�жϣ������������
    public static AudioUtils getInstance(){
        if(audioUtils == null){
            synchronized (AudioUtils.class) {
                if(audioUtils == null){
                    audioUtils = new AudioUtils();
                }
            }
        }
        return audioUtils;
    }

    /**
     * MP3ת��PCM�ļ�����
     *
     * @param mp3filepath ԭʼ�ļ�·��
     * @param pcmfilepath ת���ļ��ı���·��
     * @return
     * @throws Exception
     */
    public boolean convertMP32Pcm(String mp3filepath, String pcmfilepath){
        try {
            //��ȡ�ļ�����Ƶ����pcm�ĸ�ʽ
            AudioInputStream audioInputStream = getPcmAudioInputStream(mp3filepath);
            //����Ƶת��Ϊ  pcm�ĸ�ʽ��������
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File(pcmfilepath));
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ����MP3����
     *
     * @param mp3filepath
     * @throws Exception
     */
    public void playMP3(String mp3filepath) throws Exception {
        //��ȡ��ƵΪpcm�ĸ�ʽ
        AudioInputStream audioInputStream = getPcmAudioInputStream(mp3filepath);

        // ����
        if (audioInputStream == null){
            System.out.println("null audiostream");
            return;
        }
        //��ȡ��Ƶ�ĸ�ʽ
        AudioFormat targetFormat = audioInputStream.getFormat();
        DataLine.Info dinfo = new DataLine.Info(SourceDataLine.class, targetFormat, AudioSystem.NOT_SPECIFIED);
        //����豸
        SourceDataLine line = null;
        try {
            line = (SourceDataLine) AudioSystem.getLine(dinfo);
            line.open(targetFormat);
            line.start();

            int len = -1;
//            byte[] buffer = new byte[8192];
            byte[] buffer = new byte[1024];
            //��ȡ��Ƶ�ļ�
            while ((len = audioInputStream.read(buffer)) > 0) {
                //�����Ƶ�ļ�
                line.write(buffer, 0, len);
            }

            // Block�ȴ���ʱ���ݱ����Ϊ��
            line.drain();

            //�رն�ȡ��
            audioInputStream.close();

            //ֹͣ����
            line.stop();
            line.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("audio problem " + ex);

        }
    }

    /**
     * ��������:2018��1��14��<br/>
     * ����ʱ��:����9:53:14<br/>
     * �����û�:yellowcong<br/>
     * ���ܸ�Ҫ:��ȡ�ļ�����Ƶ��
     * @param mp3filepath
     * @return
     */
    private AudioInputStream getPcmAudioInputStream(String mp3filepath) {
        File mp3 = new File(mp3filepath);
        AudioInputStream audioInputStream = null;
        AudioFormat targetFormat = null;
        try {
            AudioInputStream in = null;

            //��ȡ��Ƶ�ļ�����
            MpegAudioFileReader mp = new MpegAudioFileReader();
            in = mp.getAudioInputStream(mp3);
            AudioFormat baseFormat = in.getFormat();

            //�趨�����ʽΪpcm��ʽ����Ƶ�ļ�
            targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
                    baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);

            //�������Ƶ
            audioInputStream = AudioSystem.getAudioInputStream(targetFormat, in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return audioInputStream;
    }

//    public static void main(String[] args) {
//        try {
//
//            String path = "E:\\1";
//            String mp3 = path +".mp3";
//            String pcm = path +".pcm";
//            AudioUtils audioUtils = AudioUtils.getInstance();
//            audioUtils.convertMP32Pcm(mp3,pcm);
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
