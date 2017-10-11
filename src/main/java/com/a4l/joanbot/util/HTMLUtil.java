package com.a4l.joanbot.util;

public class HTMLUtil {
    public static String defaultHtml = "<html><head><style id=\"mceDefaultStyles\" type=\"text/css\">.mce-content-body div.mce-resizehandle {position: absolute;border: 1px solid black;background: #FFF;width: 5px;height: 5px;z-index: 10000}.mce-content-body .mce-resizehandle:hover {background: #000}.mce-content-body img[data-mce-selected], hr[data-mce-selected] {outline: 1px solid black;resize: none}.mce-content-body .mce-clonedresizable {position: absolute;outline: 1px dashed black;opacity: .5;filter: alpha(opacity=50);z-index: 10000}.mce-content-body .mce-resize-helper {background: #555;background: rgba(0,0,0,0.75);border-radius: 3px;border: 1px;color: white;display: none;font-family: sans-serif;font-size: 12px;white-space: nowrap;line-height: 14px;margin: 5px 10px;padding: 5px;position: absolute;z-index: 10001}\n" +
"</style><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><link type=\"text/css\" rel=\"stylesheet\" href=\"http://blast.blastingnews.com/js/bo/thirdpart/tinymce/skins/lightgray/content.min.css\"><link type=\"text/css\" rel=\"stylesheet\" href=\"http://blast.blastingnews.com/css/bo/blasting-edit-news.css?v=12.121.340.182c38f3c459a3744aa72a5e12892650\"></head><body id=\"tinymce\" class=\"mce-content-body \" data-id=\"news-body\" contenteditable=\"true\" style=\"background-color: #fcfcfc;\"><p><br data-mce-bogus=\"1\"></p></body></html>";
    
    public static String curateHTML(String html){
        String curHtml;
        
        curHtml = html;
        curHtml = curHtml.replaceAll("style=\"background-color: #fcfcfc;\"", "");
        curHtml = curHtml.replaceAll("(<\\/?font)[\\s\\S]*?(>)", "");
        curHtml = curHtml.replaceAll("<p[\\s\\S]*?>", "<p>");
        curHtml = curHtml.replaceAll("<span[\\s\\S]*?>", "<span id=\"_mce_caret\" data-mce-bogus=\"true\">");
        curHtml = curHtml.replaceAll("<div>", "<p>");
        curHtml = curHtml.replaceAll("</div>", "</p>");
        curHtml = curHtml.replaceAll("<html dir=\"ltr\">", "<html>");
        curHtml = curHtml.replaceAll("<i>", "<em>");
        curHtml = curHtml.replaceAll("</i>", "</em>");
        curHtml = curHtml.replaceAll("<b>", "<strong>");
        curHtml = curHtml.replaceAll("</b>", "</strong>");

        return curHtml;
    }
    
    public static String deleteHTML(String html){
        String noHtml = html;
        
        noHtml = noHtml.replaceAll("<style id=\"mceDefaultStyles\" type=\"text\\/css\">[\\s\\S]*?<\\/style>", "");
        noHtml = noHtml.replaceAll("<[\\s\\S]*?>", "");
        noHtml = noHtml.replaceAll("&nbsp;", " ");
        noHtml = noHtml.replaceAll("&lt;", "<");
        noHtml = noHtml.replaceAll("&gt;", ">");
        noHtml = noHtml.replaceAll("&iexcl;", "¡");
        noHtml = noHtml.replaceAll("&iquest;", "¿");
        
        return noHtml;
    }
}
