package com.automonia.core.utils;


import com.automonia.core.base.tuple.ThreeTuple;
import com.automonia.core.base.tuple.Tuple;
import com.automonia.core.utils.datas.MyX509TrustManager;
import com.automonia.core.utils.datas.ParameterType;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wenteng on
 * 2017/9/16.
 */
public enum HttpUtils {
    singleton;

    private String ipAddrStr = "";

    ////////////////////////////////////////////////////////////////////////////////////

    public String sendGet(String urlAddress) {
        return request(urlAddress, null, null, RequestMethod.GET, null, null);
    }


    public String sendGet(String urlAddress, String encode) {
        return request(urlAddress, null, null, RequestMethod.GET, null, encode);
    }

    public String sendGet(String urlAddress, Map<String, String> parameters) {
        return sendGet(urlAddress, getParameterString(parameters));
    }


    public String sendPostJSON(String urlAddress, Map<String, String> parameters) {
        return request(urlAddress, getParameterString(parameters), ParameterType.json, RequestMethod.POST, null, null);
    }

    public String sendPostXML(String urlAddress, String parameters) {
        return request(urlAddress, parameters, ParameterType.xml, RequestMethod.POST, null, null);
    }

    public String sendGetJSONSSL(String urlAddress, Map<String, String> parameters) {
        return request(urlAddress, getParameterString(parameters), ParameterType.json, RequestMethod.GET, true, null);
    }

    public String sendGetXMLSSL(String urlAddress, Map<String, String> parameters) {
        return request(urlAddress, getParameterString(parameters), ParameterType.xml, RequestMethod.GET, true, null);
    }

    public String sendPostJSONSSL(String urlAddress, Map<String, String> parameters) {
        return request(urlAddress, getParameterString(parameters), ParameterType.json, RequestMethod.POST, true, null);
    }

    public String sendPostJSONSSL(String urlAddress, String parameters) {
        return request(urlAddress, parameters, ParameterType.json, RequestMethod.POST, true, null);
    }

    public String sendPostXMLSSL(String urlAddress, Map<String, String> parameters) {
        return request(urlAddress, getParameterString(parameters), ParameterType.xml, RequestMethod.POST, true, null);
    }

    public String inputStream2String(InputStream inStream, String encoding) {
        String result = null;
        int _buffer_size = 1024;
        try {
            if (inStream != null) {
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] tempBytes = new byte[_buffer_size];
                int count;
                while ((count = inStream.read(tempBytes, 0, _buffer_size)) != -1) {
                    outStream.write(tempBytes, 0, count);
                }
                outStream.flush();
                result = new String(outStream.toByteArray(), encoding);
            }
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    /**
     * 获取本地IP地址信息
     *
     * @return
     */
    public String getLocalIP() {
        if (!StringUtils.isEmpty(ipAddrStr)) {
            return ipAddrStr;
        }
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (addr == null) {
            return null;
        }
        byte[] ipAddr = addr.getAddress();
        for (int i = 0; i < ipAddr.length; i++) {
            if (i > 0) {
                ipAddrStr += ".";
            }
            ipAddrStr += ipAddr[i] & 0xFF;
        }
        return ipAddrStr;
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        }
        return ip;
    }

//    public String uploadFile(String urlAddress, MultipartFile file) {
//        String towHyphens = "--";   // 定义连接字符串
//        String boundary = "******"; // 定义分界线字符串
//        String end = "\r\n";    //定义结束换行字符串
//        try {
//            // 创建URL对象
//            URL url = new URL(urlAddress);
//            // 获取连接对象
//            URLConnection urlConnection = url.openConnection();
//            // 设置允许输入流输入数据到本机
//            urlConnection.setDoOutput(true);
//            // 设置允许输出流输出数据到服务器
//            urlConnection.setDoInput(true);
//            // 设置不使用缓存
//            urlConnection.setUseCaches(false);
//            // 设置请求参数中的内容类型为multipart/form-data,设置请求内容的分割线为******
//            urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//
//            // 从连接对象中获取输出流
//            OutputStream outputStream = urlConnection.getOutputStream();
//            // 实例化数据输出流对象，将输出流传入
//            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
//
//            // 向数据输出流中写出分割符
//            dataOutputStream.writeBytes(towHyphens + boundary + end);
//            // 向数据输出流中写出文件参数名与文件名
//            FileItem fileItem = ((CommonsMultipartFile) file).getFileItem();
//            StringBuilder fileInfo = new StringBuilder();
//            fileInfo.append("Content-Disposition:form-data;name=" + file.getName() + ";fileName=" + fileItem.getName() + end);
//            fileInfo.append("Content-Type:" + fileItem.getContentType() + end);
//
//            dataOutputStream.writeBytes(fileInfo.toString());
//            // 向数据输出流中写出结束标志
//            dataOutputStream.writeBytes(end);
//
//            // 实例化文件输入流对象，将文件路径传入，用于将磁盘上的文件读入到内存中
//            InputStream fileInputStream = file.getInputStream();
//            //定义缓冲区大小
//            int bufferSize = 1024;
//            // 定义字节数组对象，用来读取缓冲区数据
//            byte[] buffer = new byte[bufferSize];
//            // 定义一个整形变量，用来存放当前读取到的文件长度
//            int length;
//            // 循环从文件输出流中读取1024字节的数据，将每次读取的长度赋值给length变量，直到文件读取完毕，值为-1结束循环
//            while ((length = fileInputStream.read(buffer)) != -1) {
//                // 向数据输出流中写出数据
//                dataOutputStream.write(buffer, 0, length);
//            }
//            // 每写出完成一个完整的文件流后，需要向数据输出流中写出结束标志符
//            dataOutputStream.writeBytes(end);
//            // 关闭文件输入流
//            fileInputStream.close();
//
//            // 向数据输出流中写出分隔符
//            dataOutputStream.writeBytes(towHyphens + boundary + towHyphens + end);
//            // 刷新数据输出流
//            dataOutputStream.flush();
//
//            // 从连接对象中获取字节输入流
//            InputStream inputStream = urlConnection.getInputStream();
//            // 实例化字符输入流对象，将字节流包装成字符流
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//            // 创建一个输入缓冲区对象，将要输入的字符流对象传入
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//            // 创建一个字符串对象，用来接收每次从输入缓冲区中读入的字符串
//            String line;
//            // 创建一个可变字符串对象，用来装载缓冲区对象的最终数据，使用字符串追加的方式，将响应的所有数据都保存在该对象中
//            StringBuilder stringBuilder = new StringBuilder();
//            // 使用循环逐行读取缓冲区的数据，每次循环读入一行字符串数据赋值给line字符串变量，直到读取的行为空时标识内容读取结束循环
//            while ((line = bufferedReader.readLine()) != null) {
//                // 将缓冲区读取到的数据追加到可变字符对象中
//                stringBuilder.append(line);
//            }
//
//            // 依次关闭打开的输入流
//            bufferedReader.close();
//            inputStreamReader.close();
//            inputStream.close();
//
//            // 依次关闭打开的输出流
//            dataOutputStream.close();
//            outputStream.close();
//
//            // 返回服务器响应的数据
//            return stringBuilder.toString();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 具体使用待确定
     *
     * @param urlAddress  文件url地址
     * @param downloadDir 文件保存的目录
     * @return 文件对象， 文件名称信息, 本地文件路径
     */
    public ThreeTuple<File, String, String> downloadFile(String urlAddress, String downloadDir, String fileType) {
        if (StringUtils.isEmptyOr(urlAddress, downloadDir)) {
            return null;
        }
        try {
            // 创建URL对象
            URL url = new URL(urlAddress);
            // 获取连接对象
            URLConnection urlConnection = url.openConnection();
            // 设置允许输入流输入数据到本地
            urlConnection.setDoInput(true);
            // 设置允许输出流输出到服务器
            urlConnection.setDoOutput(true);
            // 获取内容长度
            int fileLength = urlConnection.getContentLength();
            String headerField = urlConnection.getHeaderField("Content-Type");

            // 获取文件url径名称
            String fileName = DateUtils.singleton.getStringWithPattern(new Date(), "yyyyMMddHHmmss") + StringUtils.generateNumber(6);

            // 实例化文件对象
            String localFilePath = downloadDir + File.separatorChar + fileName + "." + fileType;
            File file = new File(localFilePath);

            // 判断文件路径是否存在
            if (!file.getParentFile().exists()) {
                file.mkdirs();
            }
            file.createNewFile();

            // 从连接对象中获取输入字节流
            InputStream inputStream = urlConnection.getInputStream();

            // 实例化输入流缓冲区，将输入字节流传入
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

            // 实例化输出流对象，将文件对象传入
            OutputStream outputStream = new FileOutputStream(file);

            // 定义整形变量用来接收读取到的文件大小
            int size;
            // 定义整形变量用来累计当前读取到的文件长度
            int len = 0;
            // 定义字节数组对象，用来从输入缓冲区中装载数据块
            byte[] buf = new byte[1024];
            // 从输入缓冲区中一次读取1024个字节的文件内容到buf对象中，并将读取大小赋值给size变量，当读取完毕后size=-1，结束循环读取
            while ((size = bufferedInputStream.read(buf)) != -1) {
                // 累加每次读取到的文件大小
                len += size;
                // 向输出流中写出数据
                outputStream.write(buf, 0, size);
                // 打印当前文件下载的百分比
                System.out.println("下载进度：" + len * 100 / fileLength + "%\n");
            }
            // 关闭输出流
            outputStream.close();
            // 关闭输入缓冲区
            bufferedInputStream.close();
            // 关闭输入流
            inputStream.close();

            // 返回文件对象
            return Tuple.tuple(file, fileName + "." + fileType, localFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getBrowse(HttpServletRequest request) {
        String userAgent = request.getHeader("USER-AGENT");
        if (regex("Opera", userAgent))
            return "Opera";
        if (regex("Chrome", userAgent))
            return "Chrome";
        if (regex("Firefox", userAgent))
            return "Firefox";
        if (regex("Safari", userAgent))
            return "Safari";
        if (regex("360SE", userAgent))
            return "360SE";
        if (regex("GreenBrowser", userAgent))
            return "GreenBrowser";
        if (regex("QQBrowser", userAgent))
            return "QQBrowser";
        if (regex("Maxthon", userAgent))
            return "Maxthon";
        if (regex("rv:11.0", userAgent))
            return "rv:11.0";
        if (regex("MSIE 10.0", userAgent))
            return "MSIE 10.0";
        if (regex("MSIE 9.0", userAgent))
            return "MSIE 9.0";
        if (regex("MSIE 8.0", userAgent))
            return "MSIE 8.0";
        if (regex("MSIE 7.0", userAgent))
            return "MSIE 7.0";
        if (regex("MSIE 6.0", userAgent))
            return "MSIE 6.0";
        return "其它";
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////


//    private String get(String urlAddress, String encode) throws Exception {
//        if (StringUtils.isEmpty(urlAddress)) {
//            return null;
//        }
//        BufferedReader bufferedReader = null;
//        InputStreamReader inputStreamReader = null;
//        InputStream inputStream = null;
//        try {
//            // 创建URL对象
//            URL url = new URL(urlAddress);
//            // 打开连接 获取连接对象
//            URLConnection connection = url.openConnection();
//
//            // 从连接对象中获取网络连接中的输入字节流对象
//            inputStream = connection.getInputStream();
//            // 将输入字节流包装成输入字符流对象,并进行字符编码
//            inputStreamReader = new InputStreamReader(inputStream, encode);
//            // 创建一个输入缓冲区对象，将字符流对象传入
//            bufferedReader = new BufferedReader(inputStreamReader);
//
//            // 定义一个字符串变量，用来接收输入缓冲区中的每一行字符串数据
//            String line;
//            // 创建一个可变字符串对象，用来装载缓冲区对象的数据，使用字符串追加的方式，将响应的所有数据都保存在该对象中
//            StringBuilder stringBuilder = new StringBuilder();
//            // 使用循环逐行读取输入缓冲区的数据，每次循环读入一行字符串数据赋值给line字符串变量，直到读取的行为空时标识内容读取结束循环
//            while ((line = bufferedReader.readLine()) != null) {
//                // 将从输入缓冲区读取到的数据追加到可变字符对象中
//                stringBuilder.append(line);
//            }
//            // 依次关闭打开的输入流
//            bufferedReader.close();
//            inputStreamReader.close();
//            inputStream.close();
//            // 将可变字符串转换成String对象返回
//            return stringBuilder.toString();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (bufferedReader != null) {
//                bufferedReader.close();
//            }
//            if (inputStreamReader != null) {
//                inputStreamReader.close();
//            }
//            if (inputStream != null) {
//                inputStream.close();
//            }
//        }
//        return null;
//    }

//    private String postJson(String url, String parameter) {
//        PrintWriter printWriter = null;
//        BufferedReader bufferedReader = null;
//        StringBuilder response = new StringBuilder();
//        try {
//            //创建请求对象URL
//            URL httpUrl = new URL(url);
//            //创建连接对象
//            URLConnection connection = httpUrl.openConnection();
//            connection.setDoOutput(true);
//            connection.setDoInput(true);
//
//            connection.setRequestProperty("Accept", "application/json");
//            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//
//            if (!StringUtils.isEmpty(parameter)) {
//                byte[] bytes = parameter.getBytes("utf-8");
//                connection.setRequestProperty("Content-Length", String.valueOf(bytes.length));
//
//                printWriter = new PrintWriter(connection.getOutputStream());
//                printWriter.print(Arrays.toString(bytes));
//                printWriter.flush();
//            }
//
//            //读取返回的输入流 并设置字符编码
//            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
//            //读行
//            String line = null;
//            while ((line = bufferedReader.readLine()) != null) {
//                response.append(line);
//            }
//            return response.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (printWriter != null) {
//                printWriter.close();
//            }
//            if (bufferedReader != null) {
//                try {
//                    bufferedReader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//        return null;
//    }


    /**
     * 统一的网络请求
     *
     * @param requestUrl    请求的网址
     * @param parameters    请求的参数
     * @param parameterType 参数的类型
     * @param requestMethod 请求类型
     * @param userSsf       是否使用ssf验证
     * @return 请求结果
     */
    private String request(String requestUrl, String parameters, ParameterType parameterType, RequestMethod requestMethod, Boolean userSsf, String encode) {
        // 空值的默认处理
        if (StringUtils.isEmpty(encode)) {
            encode = "UTF-8";
        }
        if (userSsf == null) {
            userSsf = false;
        }

        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        InputStream inputStream = null;

        HttpURLConnection conn = null;

        try {

            URL url = new URL(requestUrl);

            if (userSsf) {
                // 创建SSLContext对象，并使用我们指定的信任管理器初始化
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

            switch (requestMethod) {
                case POST: {
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");

                    if (parameterType == ParameterType.json) {
                        conn.setRequestProperty("Accept", "application/json");
                        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    } else if (parameterType == ParameterType.xml) {
                        conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    }
                }
                break;
            }

            if (!StringUtils.isEmpty(parameters)) {
                LogUtils.singletion.debug("请求参数--》" + parameters);
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(parameters.getBytes("UTF-8"));
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
            LogUtils.singletion.error("连接超时: {}" + e);
        } catch (Exception e) {
            LogUtils.singletion.error("https请求异常: {}" + e);
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

    private boolean regex(String regex, String str) {
        Pattern p = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher m = p.matcher(str);
        return m.find();
    }

    private String getParameterString(Map<String, String> parameterMap) {
        if (parameterMap == null) {
            return null;
        }
        StringBuilder parameterString = new StringBuilder();
        for (String key : parameterMap.keySet()) {
            parameterString.append("&").append(key).append("=").append(parameterMap.get(key));
        }

        if (parameterString.length() <= 0) {
            return null;
        }
        return parameterString.substring(1);
    }

    public void main(String[] args) throws Exception {
//        String url = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm";
//        String smsUrl = url + "?tel=15989280204";
//
//        String result = HttpUtils.sendGet(smsUrl, "GBK");
//
//        System.out.println(result);
//
//        JSONObject jsonObject = JSONObject.fromObject("{" + result + "}");
//        JSONObject resultObject = (JSONObject) jsonObject.get("__GetZoneResult_");
//
//        System.out.println(resultObject.get("carrier"));

        String questionnaireUrl = "https://www.lediaocha.com/pc/s/rjrjbs";
        String fetchStreamData = sendGet(questionnaireUrl);
        System.out.println(fetchStreamData);
    }
    /**
     * 根据内容类型判断文件扩展名
     *
     * @param contentType
     *            内容类型
     * @return
     */

}
