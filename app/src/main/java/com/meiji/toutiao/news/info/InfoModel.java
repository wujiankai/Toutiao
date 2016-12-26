package com.meiji.toutiao.news.info;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.meiji.toutiao.InitApp;
import com.meiji.toutiao.bean.news.NewsInfoBean;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Meiji on 2016/12/17.
 */

public class InfoModel implements IInfo.Model {

    private Gson gson = new Gson();
    private String url;
    private NewsInfoBean newsInfoBean;

    public InfoModel() {
    }

    @Override
    public boolean getRequestData(String url) {

        boolean flag = false;
        System.out.println("newsInfoApi " + url);

        try {
            Request request = new Request.Builder()
                    .get()
                    .url(url)
                    .build();
            Response response = InitApp.getOkHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                flag = true;
                String responseJson = response.body().string();
//                System.out.println("新闻内容抓取" + html);
                newsInfoBean = gson.fromJson(responseJson, NewsInfoBean.class);
            }
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;
    }

    @Override
    public String getHtml() {

        NewsInfoBean.DataBean dataBean = newsInfoBean.getData();
        String title = dataBean.getTitle();
        String content = dataBean.getContent();
        if (content != null) {

            String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/wap.css\" type=\"text/css\">";

            String html = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">" +
                    css +
                    "<body>\n" +
                    "<article class=\"article-container\">\n" +
                    "    <div class=\"article__content article-content\">" +
                    "<h1 class=\"article-title\">" +
                    title +
                    "</h1>" +
                    content +
                    "    </div>\n" +
                    "</article>\n" +
                    "</body>\n" +
                    "</html>";

            return html;
        } else {
            return null;
        }
    }
}

