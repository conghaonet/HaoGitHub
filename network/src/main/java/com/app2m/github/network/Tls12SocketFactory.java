package com.app2m.github.network;

import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;

/**
 * Enables TLS v1.2 when creating SSLSockets.
 * <p/>
 * For some reason, android supports TLS v1.2 from API 16, but enables it by
 * default only from API 20.
 * @author conghao
 * @link https://developer.android.com/reference/javax/net/ssl/SSLSocket.html
 * @see SSLSocketFactory
 */
public class Tls12SocketFactory extends SSLSocketFactory {

    private static final String[] TLS_V12_ONLY = {TlsVersion.TLS_1_2.javaName()};

    private final SSLSocketFactory delegate;

     private Tls12SocketFactory(SSLSocketFactory base) {
        this.delegate = base;
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return delegate.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return delegate.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        return patch(delegate.createSocket(s, host, port, autoClose));
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        return patch(delegate.createSocket(host, port));
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
        return patch(delegate.createSocket(host, port, localHost, localPort));
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        return patch(delegate.createSocket(host, port));
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return patch(delegate.createSocket(address, port, localAddress, localPort));
    }

    private Socket patch(Socket s) {
        if (s instanceof SSLSocket) {
            ((SSLSocket) s).setEnabledProtocols(TLS_V12_ONLY);
        }
        return s;
    }

    public static OkHttpClient.Builder enableTls12OnKitkat(OkHttpClient.Builder client) {
         if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
             return client;
         }
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance(TlsVersion.TLS_1_2.javaName());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (sslContext != null) {
            try {
                sslContext.init(null, null, null);
                client.sslSocketFactory(new Tls12SocketFactory(sslContext.getSocketFactory()), new HttpsTrustManager());
                client.hostnameVerifier(new HttpsTrustManager.TrustAllHostnameVerifier());
                ConnectionSpec connectionSpec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TLS_V12_ONLY)
                        .build();
                List<ConnectionSpec> specList = new ArrayList<>();
                specList.add(connectionSpec);
                specList.add(ConnectionSpec.COMPATIBLE_TLS);
                specList.add(ConnectionSpec.CLEARTEXT);

                client.connectionSpecs(specList);
            } catch (KeyManagementException e) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", e);
            }
        }
        return client;
    }
}
