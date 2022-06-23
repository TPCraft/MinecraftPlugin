package cn.tpcraft.minecraft.plugin;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class Function {
    //HTTP GET请求
    public JSONObject HttpGet (String Url) throws Exception {
        IgnoreSsl();
        HttpsURLConnection HttpsURLConnection = (HttpsURLConnection) new URL(Url).openConnection();
        HttpsURLConnection.setRequestMethod("GET");
        HttpsURLConnection.setConnectTimeout(3000);
        InputStream InputStream = HttpsURLConnection.getInputStream();
        BufferedReader BufferedReader = new BufferedReader(new InputStreamReader(InputStream, StandardCharsets.UTF_8));
        JSONObject JSONObject = (JSONObject) JSONValue.parseWithException(BufferedReader.readLine());
        return JSONObject;
    }
    //HTTP POST请求
    public JSONObject HttpPost (String Url, JSONObject Data) throws Exception {
        IgnoreSsl();
        HttpsURLConnection HttpsURLConnection = (HttpsURLConnection) new URL(Url).openConnection();
        HttpsURLConnection.setRequestMethod("POST");
        HttpsURLConnection.setRequestProperty("Content-Type", "application/json");
        HttpsURLConnection.setDoOutput(true);
        DataOutputStream DataOutputStream = new DataOutputStream(HttpsURLConnection.getOutputStream());
        DataOutputStream.writeBytes(Data.toString());
        DataOutputStream.flush();
        DataOutputStream.close();
        InputStream InputStream = HttpsURLConnection.getInputStream();
        BufferedReader BufferedReader = new BufferedReader(new InputStreamReader(InputStream, StandardCharsets.UTF_8));
        JSONObject JSONObject = (JSONObject) JSONValue.parseWithException(BufferedReader.readLine());
        return JSONObject;
    }
    //信任所有证书
    private static void TrustAllHttpsCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[1];
        TrustManager tm = new Mitm();
        trustAllCerts[0] = tm;
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
    static class Mitm implements TrustManager,X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        public boolean isServerTrusted(X509Certificate[] certs) {
            return true;
        }
        public boolean isClientTrusted(X509Certificate[] certs) {
            return true;
        }
        public void checkServerTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }
        public void checkClientTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }
    }
    public static void IgnoreSsl() throws Exception{
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
                return true;
            }
        };
        TrustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }
}