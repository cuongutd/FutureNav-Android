package com.cuong.futurenav.service;

import android.content.Context;

import com.cuong.futurenav.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
/**
 * Created by Cuong on 11/17/2015.
 */
public class RestClient {

    private static RestClient client;

    public static RestClient getInstance(Context context){

        if (client == null)
            client = new RestClient(context);

        return client;

    }

    public RestClient(Context contect){
        mContext = contect;

    }

    private static final String BASE_URL = "https://futurenav.elasticbeanstalk.com";

    private static RestAdapter.Builder builder = new RestAdapter.Builder()
            .setEndpoint(BASE_URL)
            .setLogLevel(RestAdapter.LogLevel.BASIC);

    static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
    static SSLContext mSSLContext;
    private static ApiService apiService;
    private static final boolean isSelfSigned = true;
    private static Context mContext;

    public ApiService getApiService() {
        return getApiService(null);
    }

    private RestClient(){}

    public ApiService getApiService(final AccessToken token) {

        if (apiService == null) {
            builder.setConverter(new GsonConverter(gson));

            if (isSelfSigned){
                initSelfSignedTrustManager();
                OkHttpClient client = new OkHttpClient();
                client.setSslSocketFactory(mSSLContext.getSocketFactory());
                builder.setClient(new OkClient(client));

            }

            if (token != null) {
                builder.setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestInterceptor.RequestFacade request) {
                        request.addHeader("Accept", "application/json");
                        request.addHeader("Authorization",
                                token.getTokenType() + " " + token.getAccessToken());
                    }
                });
            }

            RestAdapter restAdapter = builder.build();
            apiService = restAdapter.create(ApiService.class);

        }

        return apiService;
    }

    private static void initSelfSignedTrustManager(){
        if (mSSLContext != null)
            return;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
// From https://www.washington.edu/itconnect/security/ca/load-der.crt
            InputStream caInput = mContext.getResources().openRawResource(R.raw.futurenav);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate)ca).getSubjectDN());
            } finally {
                caInput.close();
            }

// Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

// Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

// Create an SSLContext that uses our TrustManager
            mSSLContext = SSLContext.getInstance("TLS");
            mSSLContext.init(null, tmf.getTrustManagers(), null);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}