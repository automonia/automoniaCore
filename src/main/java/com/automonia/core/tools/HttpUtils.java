package com.automonia.core.tools;


import com.automonia.core.tools.model.MyX509TrustManager;
import com.automonia.core.tools.model.RequestType;
import com.automonia.core.tools.model.ResponseType;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @作者 温腾
 * @创建时间 2019年01月26日 22:39
 */
public enum HttpUtils {

    singleton;

    public String get(String url) {
        return get(url, null, ResponseType.json, false);
    }

    public String get(String url, Map<String, Object> parameters) {
        return get(url, parameters, ResponseType.json, false);
    }

    public String getSsl(String url) {
        return get(url, null, ResponseType.json, true);
    }

    public String getSsl(String url, Map<String, Object> parameters) {
        return get(url, parameters, ResponseType.json, true);
    }

    public String getXml(String url) {
        return get(url, null, ResponseType.xml, false);
    }

    public String getXml(String url, Map<String, Object> parameters) {
        return get(url, parameters, ResponseType.xml, false);
    }

    public String getXmlSsl(String url) {
        return get(url, null, ResponseType.xml, true);
    }

    public String getXmlSsl(String url, Map<String, Object> parameters) {
        return get(url, parameters, ResponseType.xml, true);
    }

    public String getHtml(String url) {
        return get(url, null, ResponseType.html, false);
    }

    public String getHtml(String url, Map<String, Object> parameters) {
        return get(url, parameters, ResponseType.html, false);
    }

    public String getHtmlSsl(String url) {
        return get(url, null, ResponseType.html, true);
    }

    public String getHtmlSsl(String url, Map<String, Object> parameters) {
        return get(url, parameters, ResponseType.html, true);
    }


    public String post(String url, Map<String, Object> parameters) {
        return post(url, parameters, ResponseType.json, false);
    }

    public String postSsl(String url, Map<String, Object> parameters) {
        return post(url, parameters, ResponseType.json, true);
    }

    public String postSsl(String url, String parameters) {
        return request(url, parameters, ResponseType.json, RequestType.POST, true, "UTF-8");
    }

    public String postXml(String url, Map<String, Object> parameters) {
        return post(url, parameters, ResponseType.xml, false);
    }

    public String postXml(String url, String parameters) {
        return request(url, parameters, ResponseType.xml, RequestType.POST, true, "UTF-8");
    }

    public String postXmlSsl(String url, Map<String, Object> parameters) {
        return post(url, parameters, ResponseType.xml, true);
    }

    public String postHtml(String url, Map<String, Object> parameters) {
        return post(url, parameters, ResponseType.html, false);
    }

    public String postHtmlSsl(String url, Map<String, Object> parameters) {
        return post(url, parameters, ResponseType.html, true);
    }

    //////////////////////////////////////////////////////////////////////////////////////////


    private String get(String url, Map<String, Object> parameters, ResponseType responseType, Boolean useSsl) {
        String parameterString = getParameterString(parameters);
        if (!StringUtils.singleton.isEmpty(parameterString)) {
            url += "?" + parameterString;
        }
        return request(url, null, responseType, RequestType.GET, useSsl, "UTF-8");
    }

    private String post(String url, Map<String, Object> parameters, ResponseType responseType, Boolean useSsl) {
        return request(url, getParameterString(parameters), responseType, RequestType.POST, useSsl, "UTF-8");
    }


    /**
     * 将参数map转换为url格式, 最前面并没有用？链接
     *
     * @param parameterMap 参数map对象
     * @return 参数拼接url格式
     */
    private String getParameterString(Map<String, Object> parameterMap) {
        if (parameterMap == null || parameterMap.isEmpty()) {
            return null;
        }
        StringBuilder parameterString = new StringBuilder();
        parameterMap.keySet().stream().filter(key -> !StringUtils.singleton.isEmpty(parameterMap.get(key))).forEach(key -> parameterString.append("&").append(key).append("=").append(parameterMap.get(key)));
        return parameterString.substring(1);
    }

    /**
     * 统一的网络请求
     *
     * @param requestUrl   请求的网址
     * @param parameters   请求的参数
     * @param responseType 响应参数的类型
     * @param requestType  请求类型
     * @param userSsl      是否使用ssl验证
     * @return 请求结果
     */
    private String request(String requestUrl, String parameters, ResponseType responseType, RequestType requestType, Boolean userSsl, String encode) {

        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        InputStream inputStream = null;
        HttpURLConnection conn = null;

        try {
            URL url = new URL(requestUrl);

            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            if (userSsl) {
                TrustManager[] trustManagers = {new MyX509TrustManager()};
                SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
                sslContext.init(null, trustManagers, new java.security.SecureRandom());

                // 针对https设置证书信息
                HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
                httpUrlConn.setSSLSocketFactory(sslContext.getSocketFactory());

                conn = httpUrlConn;
            }
            // 无需证书的使用http请求
            else {
                conn = (HttpURLConnection) url.openConnection();
            }

            if (requestType == RequestType.POST) {
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
            }

            // 设置响应内容格式
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // JSON
            if (responseType == ResponseType.json) {
                conn.setRequestProperty("Accept", "application/json");
            }
            // XML
            else if (responseType == ResponseType.xml) {
                conn.setRequestProperty("Accept", "text/xml");
            }
            // HTML
            else if (responseType == ResponseType.html) {
                conn.setRequestProperty("Accept", "text/html");
            }

            // 参数内容
            if (!StringUtils.singleton.isEmpty(parameters)) {
                LogUtils.singleton.info("请求参数--》" + parameters);
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(parameters.getBytes(encode));
                outputStream.flush();
                outputStream.close();
            }

            // 从输入流读取返回内容
            inputStream = conn.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, encode);
            bufferedReader = new BufferedReader(inputStreamReader);

            String str;
            StringBuilder buffer = new StringBuilder();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            return buffer.toString();

        } catch (ConnectException e) {
            LogUtils.singleton.error("连接超时: {}" + e);
        } catch (Exception e) {
            LogUtils.singleton.error("https请求异常: {}" + e);
        } finally {
            // 释放资源
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }
}
