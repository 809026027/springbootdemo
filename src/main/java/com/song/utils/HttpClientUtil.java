package com.song.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.RequestEntity;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 17060342 on 2019/6/3.
 */
public class HttpClientUtil {

    /**
     * 最大线程池
     */
    public static final int THREAD_POOL_SIZE = 5;

    /**
     * log
     */
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    public interface HttpClientDownLoadProgress {
        public void onProgress(int progress);
    }

    private static HttpClientUtil httpClientDownload;

    private ExecutorService downloadExcutorService;

    private HttpClientUtil() {

    }

    public static synchronized HttpClientUtil getInstance() {
        if (httpClientDownload == null) {
            httpClientDownload = new HttpClientUtil();
        }
        return httpClientDownload;
    }

    /**
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    private CloseableHttpClient getHttpsClient(HttpHost proxy) {
        SSLContext sslContext;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                //信任所有
                @Override
                public boolean isTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
                    return true;
                }
            }).build();

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            //把代理设置到请求配置
            HttpClientBuilder httpClientBuilder= HttpClients.custom();
            if(proxy != null){
                RequestConfig defaultRequestConfig = RequestConfig.custom()
                        .setProxy(proxy)
                        .build();
                httpClientBuilder.setDefaultRequestConfig(defaultRequestConfig);
            }
            return httpClientBuilder.setSSLSocketFactory(sslsf).build();
        } catch (KeyStoreException e) {
            logger.info("exception message: ", e);
        } catch (NoSuchAlgorithmException e) {
            logger.info("exception message: ", e);
        } catch (KeyManagementException e) {
            logger.info("exception message: ", e);
        }

        return HttpClients.createDefault();
    }

    /**
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    private CloseableHttpClient getHttpClient(HttpHost proxy) {
        //把代理设置到请求配置
        HttpClientBuilder httpClientBuilder= HttpClients.custom();
        if(proxy != null){
            RequestConfig defaultRequestConfig = RequestConfig.custom()
                    .setProxy(proxy)
                    .build();
            httpClientBuilder.setDefaultRequestConfig(defaultRequestConfig);
        }
        return httpClientBuilder.build();
    }

    /**
     * 下载文件
     *
     * @param url
     * @param filePath
     */
    public void download(final String url, final String filePath, final HttpHost proxy, final boolean isHttps, boolean async) {
        if(async)
        {
            downloadExcutorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
            downloadExcutorService.execute(new Runnable() {

                @Override
                public void run() {
                    httpDownloadFile(url, filePath, null, null, proxy, isHttps);
                }
            });
        }else{
            httpDownloadFile(url, filePath, null, null, proxy, isHttps);
        }
    }

    /**
     * 下载文件
     *
     * @param url
     * @param filePath
     * @param progress
     *            进度回调
     */
    public void download(final String url, final String filePath,
                         final HttpClientDownLoadProgress progress, final HttpHost proxy, final boolean isHttps, boolean async) {
        if(async)
        {
            downloadExcutorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
            downloadExcutorService.execute(new Runnable() {
                @Override
                public void run() {
                    httpDownloadFile(url, filePath, progress, null, proxy, isHttps);
                }
            });
        }else{
            httpDownloadFile(url, filePath, progress, null, proxy, isHttps);
        }
    }

    /**
     * 下载文件
     *
     * @param url
     * @param filePath
     */
    private void httpDownloadFile(String url, String filePath,
                                  HttpClientDownLoadProgress progress, Map<String, String> headMap,
                                  HttpHost proxy, boolean isHttps) {
        CloseableHttpClient httpclient = isHttps ? getHttpsClient(proxy) : getHttpClient(proxy);
        try {
            HttpGet httpGet = new HttpGet(url);
            setGetHead(httpGet, headMap);
            HttpResponse response1 = httpclient.execute(httpGet);
            HttpEntity httpEntity = response1.getEntity();
            long contentLength = httpEntity.getContentLength();
            InputStream is = httpEntity.getContent();
            // 根据InputStream 下载文件
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int r = 0;
            long totalRead = 0;
            while ((r = is.read(buffer)) > 0) {
                output.write(buffer, 0, r);
                totalRead += r;
                if (progress != null) {// 回调进度
                    progress.onProgress((int) (totalRead * 100 / contentLength));
                }
            }
            FileOutputStream fos = new FileOutputStream(filePath);
            output.writeTo(fos);
            output.flush();
            output.close();
            fos.close();
            EntityUtils.consume(httpEntity);
        } catch (Exception e) {
            logger.info("exception message: ", e);
        }
    }

    /**
     * get请求
     *
     * @param url
     * @return
     */
    public String httpGet(String url, boolean isHttps) {
        return httpGet(url, null, isHttps);
    }

    /**
     * http get请求
     *
     * @param url
     * @return
     */
    public String httpGet(String url, Map<String, String> headMap, boolean isHttps) {
        return httpGet(url, headMap, null, isHttps);
    }

    /**
     * http的post请求
     *
     * @param url
     * @return
     */
    public String httpGet(String url, Map<String, String> headMap, HttpHost proxy, boolean isHttps) {
        String responseContent = "";
        CloseableHttpClient httpclient = isHttps ? getHttpsClient(proxy) : getHttpClient(proxy);
        try {
            HttpGet httpGet = new HttpGet(url);
            setGetHead(httpGet, headMap);
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            responseContent = getRespString(entity);
            EntityUtils.consume(entity);
        } catch (Exception e) {
            logger.info("exception message: ", e);
        }
        return responseContent;
    }

    public String httpPost(String url, Map<String, String> paramsMap, boolean isHttps) {
        return httpPost(url, paramsMap, null, null, isHttps);
    }

    /**
     * http的post请求
     *
     * @param url
     * @param paramsMap
     * @return
     */
    public String httpPost(String url, Map<String, String> paramsMap,
                           Map<String, String> headMap, HttpHost proxy, boolean isHttps) {
        String responseContent = "";
        CloseableHttpClient httpclient = isHttps ? getHttpsClient(proxy) : getHttpClient(proxy);
        try {
            HttpPost httpPost = new HttpPost(url);
            setPostHead(httpPost, headMap);
            setPostParams(httpPost, paramsMap);
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            responseContent = getRespString(entity);
            EntityUtils.consume(entity);
        } catch (Exception e) {
            logger.info("exception message: ", e);
        }
        return responseContent;
    }

    /**
     * http的post请求
     *
     * @param url
     * @param json
     * @return
     */
    public String httpPost(String url, String json,HttpHost proxy,boolean isHttps) {
        String responseContent = "";
        CloseableHttpClient httpclient = isHttps ? getHttpsClient(proxy) : getHttpClient(proxy);
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity stringEntity = new StringEntity(json, "UTF-8");
            stringEntity.setContentType("application/json");
            stringEntity.setContentEncoding("UTF-8");
            httpPost.setEntity(stringEntity);
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            responseContent = getRespString(entity);
            EntityUtils.consume(entity);
        } catch (Exception e) {
            logger.info("exception message: ", e);
        }
        return responseContent;
    }

    /**
     * http的post请求
     *
     * @param url
     * @param paramsMap
     * @return
     */
    public HttpResponse httpPostResponse(String url, Map<String, String> paramsMap,
                           Map<String, String> headMap, HttpHost proxy, boolean isHttps) {
        CloseableHttpClient httpclient = isHttps ? getHttpsClient(proxy) : getHttpClient(proxy);
        try {
            HttpPost httpPost = new HttpPost(url);
            setPostHead(httpPost, headMap);
            setPostParams(httpPost, paramsMap);
            HttpResponse response = httpclient.execute(httpPost);
            return response;
        } catch (Exception e) {
            logger.info("exception message: ", e);
        }
        return null;
    }

    /**
     * 设置POST的参数
     *
     * @param httpPost
     * @param paramsMap
     * @throws UnsupportedEncodingException
     */
    private void setPostParams(HttpPost httpPost, Map<String, String> paramsMap) throws UnsupportedEncodingException {
        if (paramsMap != null && paramsMap.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        }
    }

    /**
     * 设置http的HEAD
     *
     * @param httpPost
     * @param headMap
     */
    private void setPostHead(HttpPost httpPost, Map<String, String> headMap) {
        if (headMap != null && headMap.size() > 0) {
            for (Map.Entry<String, String> entry : headMap.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 设置http的HEAD
     *
     * @param httpGet
     * @param headMap
     */
    private void setGetHead(HttpGet httpGet, Map<String, String> headMap) {
        if (headMap != null && headMap.size() > 0) {
            for (Map.Entry<String, String> entry : headMap.entrySet()) {
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 上传文件
     *
     * @param serverUrl
     *            服务器地址
     * @param localFilePath
     *            本地文件路径
     * @param serverFieldName
     * @param params
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public String uploadFileImpl(String serverUrl, String localFilePath,
                                 String serverFieldName, Map<String, String> params,HttpHost proxy,boolean isHttps) throws ClientProtocolException, IOException {
        String respStr = null;
        CloseableHttpClient httpclient = isHttps ? getHttpsClient(proxy) : getHttpClient(proxy);
        try {
            HttpPost httppost = new HttpPost(serverUrl);
            FileBody binFileBody = new FileBody(new File(localFilePath));

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
            // add the file params
            multipartEntityBuilder.addBinaryBody("file", new File(localFilePath));
            HttpEntity requestEntity = multipartEntityBuilder.build();
            httppost.setEntity(requestEntity);

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            respStr = getRespString(resEntity);
            EntityUtils.consume(resEntity);
        }  catch (Exception e) {
            logger.info("exception message: ", e);
        }
        return respStr;
    }

    /**
     * 将返回结果转化为String
     *
     * @param entity
     * @return
     * @throws IOException
     * @throws UnsupportedOperationException
     */
    private String getRespString(HttpEntity entity) throws UnsupportedOperationException, IOException{
        if (entity == null) {
            return null;
        }
        InputStream is = entity.getContent();
        StringBuffer strBuf = new StringBuffer();
        byte[] buffer = new byte[4096];
        int r = 0;
        while ((r = is.read(buffer)) > 0) {
            strBuf.append(new String(buffer, 0, r, "UTF-8"));
        }
        return strBuf.toString();
    }
}
