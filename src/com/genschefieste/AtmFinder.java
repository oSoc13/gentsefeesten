package com.genschefieste;

import android.content.Context;
import android.util.Log;

import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.auth.oauth.OAuthRsaSigner;
import com.google.api.client.http.GenericUrl;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Retrieves a list of nearby ATMs
 * @author Jeppe Knockaert, Leen De Baets, Nicolas Dierck, Benjamin Mestdagh
 * (c) 2013, OKFN. All rights reserved.
 */
public class AtmFinder {

    /**
     * Retrieves ATMs around the location of the user
     * @param longitude longitude of the user location
     * @param latitude latitude of the user location
     * @return a list of ATMs around the user
     */
    public List<Atm> findATM(Context context, float longitude, float latitude){
        ArrayList<Atm> atmlist = new ArrayList<Atm>();
        try {
            retrieve(context, 51.051539, 3.731575);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return atmlist;
    }

    public void retrieve(final Context context, double longitude, double latitude) throws IOException, ParserConfigurationException, SAXException {

        final String endPoint = "https://sandbox.api.mastercard.com/atms/v3/atm?Format=XML&PageOffset=0&PageLength=10&Longitude="+longitude+"&Latitude="+latitude;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpsURLConnection conn = null;
                try {
                    conn = createOpenAPIConnection(endPoint, context);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }


                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = null;
                try {
                    docBuilder = docBuilderFactory.newDocumentBuilder();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
                Document doc = null;
                String response = "";
                if (docBuilder != null) {
                    try {
                        doc = docBuilder.parse(conn.getInputStream());
                        response = doc.toString();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // Test output
                Log.d("ATM","-------- TEST -------" + response);
            }
        });
        thread.start();
    }

    /*
     * Establish an OAuth connection to a MasterCard API over HTTPS.
     * @param httpsURL The full URL to call, including any querystring parameters.
     */
    private HttpsURLConnection createOpenAPIConnection(String httpsURL, Context context) throws GeneralSecurityException, IOException {
        HttpsURLConnection con = null;
        PrivateKey privKey = getPrivateKey(context);
        if (privKey != null) {
            OAuthRsaSigner rsaSigner = new OAuthRsaSigner();
            OAuthParameters params = new OAuthParameters();
            params.computeNonce();
            params.computeTimestamp();
            params.signer = rsaSigner;
            params.version = "1.0";
            rsaSigner.privateKey = privKey;

            String method = "GET";

            GenericUrl genericUrl = new GenericUrl(httpsURL);
            params.computeSignature(method,genericUrl);
            Log.d("TEST",params.signature);
            URL url = new URL(httpsURL);
            con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
            con.setRequestProperty("Accept","*/*");
            con.setSSLSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault());
            con.setDoOutput(true);
            con.addRequestProperty("Authorization",	params.getAuthorizationHeader());

            con.connect();
        }
        return con;
    }

    protected PrivateKey getPrivateKey(Context context)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, KeyStoreException, CertificateException, UnrecoverableEntryException {

        //here goes the password of the file with the private key
        String kspw = "***";
        String privKeyFile = "MCOpenAPI.p12";
        String keyAlias = "mckp";

        KeyStore ks = KeyStore.getInstance("PKCS12");


        // get user password and file input stream
        ClassLoader cl = this.getClass().getClassLoader();
        InputStream stream = context.getAssets().open(privKeyFile);
        ks.load(stream, kspw.toCharArray());
        Key key = ks.getKey(keyAlias, kspw.toCharArray());

        return (PrivateKey) key;

    }
}
