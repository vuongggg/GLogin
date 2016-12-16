package com.glogin.Helper;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

public class CsHttpsConnection {
    private static final String LINE_FEED = "\r\n";

    private HttpsURLConnection connection;
    private String charset;
    private String boundary;
    private OutputStream outputStream;
    private PrintWriter writer;

    public CsHttpsConnection (Context context, String url) {
        AssetManager assetManager = context.getAssets();
        InputStream caInput = null;
        this.charset = "UTF-8";

        // creates a unique boundary based on time stamp
        boundary = "===" + System.currentTimeMillis() + "===";
        try {
            caInput = assetManager.open("ca.crt");
            CertificateFactory cf = null;
            cf = CertificateFactory.getInstance("X.509");

            Certificate ca;
            ca = cf.generateCertificate(caInput);

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore;

            keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            //Bypass hostname verified
            HttpsURLConnection.setDefaultHostnameVerifier((HostnameVerifier) new NullHostNameVerifier());

            // Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            // Tell the URLConnection to use a SocketFactory from our SSLContext
            URL sslUrl = new URL(url);
            connection = (HttpsURLConnection) sslUrl.openConnection();
            connection.setSSLSocketFactory(sslContext.getSocketFactory());
//			connection.setRequestProperty("Content-Type",
//		                "multipart/form-data; boundary=" + boundary);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public void setMultipart (boolean isMultipart) {
        if (isMultipart) {
            try {
                connection.setRequestProperty("Content-Type",
                        "multipart/form-data; boundary=" + boundary);
                setPostMethod(true);
                writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                        true);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void setPostMethod (boolean isPost) {
        if (!isPost) {
            return;
        }
        try {
            connection.setRequestMethod("POST");
            connection.setDoOutput(isPost); // indicates POST method
            connection.setUseCaches(false);
            connection.setDoInput(true);
            outputStream = connection.getOutputStream();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public HttpsURLConnection getConnection () {
        return connection;
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField (String name, String value) {
        if (writer == null) {
            return;
        }
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(
                LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart (String fieldName, File uploadFile)
            throws IOException {
        String fileName = uploadFile.getName();
        if (writer == null || outputStream == null) {
            return;
        }
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append(
                "Content-Disposition: form-data; name=\"" + fieldName
                        + "\"; filename=\"" + fileName + "\"")
                .append(LINE_FEED);
        writer.append(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();

        writer.append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a header field to the request.
     *
     * @param name  - name of the header field
     * @param value - value of the header field
     */
    public void addHeaderField (String name, String value) {
        if (writer == null) {
            return;
        }
        writer.append(name + ": " + value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Add string parameter
     **/
    public void addParameter (String parameters) {
        DataOutputStream wr = new DataOutputStream(outputStream);
        try {
            wr.writeBytes(parameters);
            wr.flush();
            wr.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     * @throws JSONException
     */
    public JSONObject finish () throws IOException, JSONException {
        if (writer != null) {
            writer.append(LINE_FEED).flush();
            writer.append("--" + boundary + "--").append(LINE_FEED);
            writer.close();
        }

        // checks server's status code first
        int status;

        status = connection.getResponseCode();
        Log.d("ERROR", "server response status = " + status);
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            connection.disconnect();
            Log.d("RESPON", response.toString());
            JSONObject jo = new JSONObject(response.toString());
            return jo;
        }
        else {
            return null;
        }
    }

    public void toFinish () {
        try {
            if (writer != null) {
                writer.append(LINE_FEED).flush();
                writer.append("--" + boundary + "--").append(LINE_FEED);
                writer.close();
            }

            // checks server's status code first
            int status;

            status = connection.getResponseCode();
            Log.d("ERROR", "server response status = " + status);
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                connection.disconnect();
                return;
            }
            else {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

    }

    public class NullHostNameVerifier implements HostnameVerifier {

        @Override
        public boolean verify (String hostname, SSLSession session) {
            Log.i("RestUtilImpl", "Approving certificate for " + hostname);
            return true;
        }
    }


}
