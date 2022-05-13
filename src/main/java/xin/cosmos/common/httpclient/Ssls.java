package xin.cosmos.common.httpclient;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * 设置SSL
 */
@Slf4j
public class Ssls {

    private static final SslHandler SimpleVerifier = new SslHandler();
    private static SSLConnectionSocketFactory sslConnFactory;
    private SSLContext sc;

    public static Ssls getInstance() {
        return new Ssls();
    }

    /**
     * 重写X509TrustManager类的三个方法,信任服务器证书
     **/
    private static class SslHandler implements X509TrustManager, HostnameVerifier {

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        @Override
        public boolean verify(String paramString, SSLSession paramSslSession) {
            return true;
        }
    }

    ;

    /**
     * 信任主机
     */
    public static HostnameVerifier getVerifier() {
        return SimpleVerifier;
    }

    public synchronized SSLConnectionSocketFactory getSslConnsf() {
        if (sslConnFactory != null) {
            return sslConnFactory;
        }
        try {
            SSLContext sc = getSslContext();
            sc.init(null, new TrustManager[]{SimpleVerifier}, null);
            sslConnFactory = new SSLConnectionSocketFactory(sc, SimpleVerifier);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
        return sslConnFactory;
    }

    public Ssls customSsl(String keyStorePath, String keyStorepass) {
        FileInputStream instream = null;
        KeyStore trustStore = null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            instream = new FileInputStream(new File(keyStorePath));
            trustStore.load(instream, keyStorepass.toCharArray());
            // 相信自己的CA和所有自签名的证书
            sc = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
        } catch (KeyStoreException | NoSuchAlgorithmException | IOException | KeyManagementException | CertificateException e) {
            log.error("证书加载异常", e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                log.error("证书读取文件流关闭异常", e);
                e.printStackTrace();
            }
        }
        return this;
    }

    public SSLContext getSslContext() {
        try {
            if (sc == null) {
                //sc = SSLContext.getInstance("TLSv1.2");
                sc = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
            }
            return sc;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}