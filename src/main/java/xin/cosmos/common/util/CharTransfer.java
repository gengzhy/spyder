package xin.cosmos.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * 字符串编码转换器
 */
@Slf4j
public class CharTransfer {
    private InputStreamReader inputStreamReader;
    private OutputStreamWriter outputStreamWriter;
    private ByteArrayOutputStream byteArrayOutputStream;

    public static CharTransfer getCharsetTransfer() {
        return new CharTransfer();
    }

    /**
     * 字符串编码转换
     *
     * @param src           需转换编码字符串
     * @param srcCharset    原始编码
     * @param targetCharset 目标编码
     * @return
     */
    public String transfer(String src, String srcCharset, String targetCharset) {
        try {
            inputStreamReader = new InputStreamReader(new ByteArrayInputStream(src.getBytes(srcCharset)), srcCharset);
            byteArrayOutputStream = new ByteArrayOutputStream();
            outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream, targetCharset);
            char[] c = new char[1024];
            int len;
            while ((len = inputStreamReader.read(c)) != -1) {
                outputStreamWriter.write(c, 0, len);
            }
            outputStreamWriter.flush();
            return byteArrayOutputStream.toString(targetCharset);
        } catch (Exception e) {
            log.error("charset translate error:{}", e.getMessage());
        } finally {
            this.release();
        }
        return null;
    }

    private void release() {
        try {
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
        } catch (Exception e) {
            log.error("close stream error:{}", e.getMessage());
        }
    }
}
