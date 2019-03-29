import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.json.JSONObject;

import com.baidu.aip.speech.AipSpeech;

public class MP3ToWav {
    /**
     * mp3���ֽ���������wav�ļ�
     * @param sourceBytes
     * @param targetPath
     */
    public static boolean byteToWav(byte[] sourceBytes, String targetPath) {
        if (sourceBytes == null || sourceBytes.length == 0) {
            System.out.println("Illegal Argument passed to this method");
            return false;
        }

        try{

                final ByteArrayInputStream bais = new ByteArrayInputStream(sourceBytes);
                final AudioInputStream sourceAIS = AudioSystem.getAudioInputStream(bais);
            AudioFormat sourceFormat = sourceAIS.getFormat();
            // ����MP3��������ʽ,������16bit
            AudioFormat mp3tFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), sourceFormat.getChannels() * 2, sourceFormat.getSampleRate(), false);
            // ���ðٶ�����ʶ�����Ƶ��ʽ
            AudioFormat pcmFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 16000, 16, 1, 2, 16000, false);

                    // ��ͨ��MP3תһ�Σ�ʹ��Ƶ���ܵĸ�ʽ����
                    final AudioInputStream mp3AIS = AudioSystem.getAudioInputStream(mp3tFormat, sourceAIS);
                    // ת�ɰٶ���Ҫ����
                    final AudioInputStream pcmAIS = AudioSystem.getAudioInputStream(pcmFormat, mp3AIS);
                // ����·������wav�ļ�
                AudioSystem.write(pcmAIS, AudioFileFormat.Type.WAVE, new File(targetPath));
            return true;
        } catch (IOException e) {
            System.out.println("�ļ�ת���쳣��" + e.getMessage());
            return false;
        } catch (UnsupportedAudioFileException e) {
            System.out.println("�ļ�ת���쳣��" + e.getMessage());
            return false;
        }
    }

    /**
     * ���ļ�ת���ֽ���
     * @param filePath
     * @return
     */
    private static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

//    public static void main(String args[]) {
//        String filePath = "E:\\WorkSpace\\Voltaren\\out\\artifacts\\Voltaren_Web_exploded\\WEB-INF\\file\\miniProgramVoice\\6192c8f8-33e6-4b6f-b6f8-dce35df0fa3b.mp3";
//        String targetPath = "E:\\WorkSpace\\Voltaren\\out\\artifacts\\Voltaren_Web_exploded\\WEB-INF\\file\\miniProgramVoice\\6192c8f8-33e6-4b6f-b6f8-dce35df0fa3b.wav";
//        byteToWav(getBytes(filePath), targetPath);
//        AipSpeech client = new AipSpeech("XXXXXX", "XXXXXXXX", "XXXXXXXX");
//        JSONObject asrRes = client.asr(targetPath, "wav", 16000, null);
//        System.out.println(asrRes);
//        System.out.println(asrRes.get("result"));
//    }
}

