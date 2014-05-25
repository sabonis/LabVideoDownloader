package com.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class VideoUrlParser {

    /**
     * Converts the url to the downloadable url
     *
     * @param  url
     * @return downloadable url or null
     */
    public String parse(String url) {
        String downloadableUrl = null;
        try {
            downloadableUrl = getDownloadableUrl(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        System.out.print(downloadableUrl);
        return downloadableUrl;
    }

    /**
     *
     * @param url
     * @return
     * @throws UnsupportedEncodingException
     * @throws ScriptException
     * @throws NoSuchMethodException
     */
    private String getDownloadableUrl(String url)
            throws UnsupportedEncodingException, ScriptException, NoSuchMethodException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        engine.put("downloadUrl", "");
        engine.put("rdata", getVideoInfo(url));
        String js = HttpHelper.get(
                "http://kej.tw/flvretriever/script/parse.youtube.fmt_url_map.js?v=20131107",
                new HttpHelper.Filter() {
                    @Override
                    public boolean filter(String inputLine) {
                        return !inputLine.contains("//") && !inputLine.contains("$") && !inputLine.contains("createElement");
                    }
                });

        js = js.replace("if(dllinks.length > 0){", "if(downloadUrl == ''){downloadUrl = unescape(url_classic[i].fmt_url) + '&signature=' + url_classic[i].fmt_sig + '&title=' + title;}if(dllinks.length > 0){");

        engine.eval(js);
        //engine.eval("/*function getYouTubeUrl() {}*/function getYouTubeUrl() {downloadUrl = 'fuck'}");

        ((Invocable) engine).invokeFunction("getYouTubeUrl");

        return (String) engine.get("downloadUrl");
    }

    private String getVideoInfo(String url) throws UnsupportedEncodingException {
        Document doc = Jsoup.parse(
                HttpHelper.get("http://kej.tw/flvretriever/youtube.php?videoUrl=" + URLEncoder.encode(url, "UTF-8"))
        );
        Elements elements = doc.select("a");
        String videoInfoUrl = "";
        for(Element element : elements){
            if(element.attr("href").contains("www.youtube.com")){
                videoInfoUrl = element.attr("href");
                break;
            }
        }
        return HttpHelper.get(videoInfoUrl);
    }
}
