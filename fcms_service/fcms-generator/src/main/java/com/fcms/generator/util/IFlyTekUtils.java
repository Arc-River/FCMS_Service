package com.fcms.generator.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.HttpUrl;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author NYS
 * @date 2022/5/14
 */
public class IFlyTekUtils {

    private static final Logger log = LoggerFactory.getLogger(IFlyTekUtils.class);

    // 地址与鉴权信息
    public static final String hostUrl = "https://api.xf-yun.com/v1/private/s9a87e3ec";
    public static final String appid = "8d66c2eb";
    public static final String apiSecret = "MzkyYjMzYmMyY2U0ODMxNmIxNmJmNDBk";
    public static final String apiKey = "9456508e0e42c12f25659ad9b85d9aca";

    public static CheckTekResult checkTek(String content) throws Exception {
        String url = getAuthUrl(hostUrl, apiKey, apiSecret);
        String json = getRequestJson(content);
        String backResult = doPostJson(url, json);
        log.info(backResult);
        CheckTekResult checkTekResult = JSON.parseObject(backResult, CheckTekResult.class);
        return checkTekResult;
    }

    public static String getString(String str, String key) {
        String[] split = key.split("\\.");
        if (split.length > 1) {
            for (String s : split) {
                String targetKey = key.substring(key.indexOf(".") + 1);
                JSONObject jsonObject = JSONObject.parseObject(str);
                String string = jsonObject.getString(s);
                return getString(string, targetKey);
            }
        }
        // 没有"." 直接取
        JSONObject jsonObject = JSONObject.parseObject(str);
        return jsonObject.getString(key);
    }

    // 请求参数json拼接
    public static String getRequestJson(String content) {
        return "{\n" +
                "  \"header\": {\n" +
                "    \"app_id\": \"" + appid + "\",\n" +
                //"    \"uid\": \"XXXXX\",\n" +
                "    \"status\": 3\n" +
                "  },\n" +
                "  \"parameter\": {\n" +
                "    \"s9a87e3ec\": {\n" +
                //"    \"res_id\": \"XXXXX\",\n" +
                "      \"result\": {\n" +
                "        \"encoding\": \"utf8\",\n" +
                "        \"compress\": \"raw\",\n" +
                "        \"format\": \"json\"\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"payload\": {\n" +
                "    \"input\": {\n" +
                "      \"encoding\": \"utf8\",\n" +
                "      \"compress\": \"raw\",\n" +
                "      \"format\": \"plain\",\n" +
                "      \"status\": 3,\n" +
                "      \"text\": \"" + getBase64TextData(content) + "\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }

    // 读取文件
    public static String getBase64TextData(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }

    // 根据json和url发起post请求
    public static String doPostJson(String url, String json) {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        CloseableHttpResponse closeableHttpResponse = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            closeableHttpResponse = closeableHttpClient.execute(httpPost);
            resultString = EntityUtils.toString(closeableHttpResponse.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (closeableHttpResponse != null) {
                    closeableHttpResponse.close();
                }
                if (closeableHttpClient != null) {
                    closeableHttpClient.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    // 鉴权方法
    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        // 时间
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        // 拼接
        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "POST " + url.getPath() + " HTTP/1.1";
        //System.out.println(preStr);
        // SHA256加密
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        // 拼接
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        // 拼接地址
        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder().//
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))).//
                addQueryParameter("date", date).//
                addQueryParameter("host", url.getHost()).//
                build();

        return httpUrl.toString();
    }

    //返回的json结果拆解
    public static class CheckTekResult {

        private Header header;
        private Payload payload;
        public void setHeader(Header header) {
            this.header = header;
        }
        public Header getHeader() {
            return header;
        }

        public void setPayload(Payload payload) {
            this.payload = payload;
        }
        public Payload getPayload() {
            return payload;
        }
    }

    public static class Header {

        private int code;
        private String message;
        private String sid;
        public void setCode(int code) {
            this.code = code;
        }
        public int getCode() {
            return code;
        }

        public void setMessage(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }
        public String getSid() {
            return sid;
        }

    }

    public static class Payload {

        private Result result;
        public void setResult(Result result) {
            this.result = result;
        }
        public Result getResult() {
            return result;
        }

    }

    public static class Result {

        private String compress;
        private String encoding;
        private String format;
        private String seq;
        private String status;
        private String text;
        public void setCompress(String compress) {
            this.compress = compress;
        }
        public String getCompress() {
            return compress;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }
        public String getEncoding() {
            return encoding;
        }

        public void setFormat(String format) {
            this.format = format;
        }
        public String getFormat() {
            return format;
        }

        public void setSeq(String seq) {
            this.seq = seq;
        }
        public String getSeq() {
            return seq;
        }

        public void setStatus(String status) {
            this.status = status;
        }
        public String getStatus() {
            return status;
        }

        public void setText(String text) {
            this.text = text;
        }
        public String getText() {
            return text;
        }

    }
}

