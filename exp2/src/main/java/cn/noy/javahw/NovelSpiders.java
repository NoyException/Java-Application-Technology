package cn.noy.javahw;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 几个小说爬虫实例
 */
public enum NovelSpiders {
    /**
     * 对应网站http://www.zzzcn.org/
     */
    ZZZCN(CommonNovelSpider.createDefault()),
    /**
     * 对应网站https://www.ishuquge.la/
     */
    ISHUQUGE(
            CommonNovelSpider.builder()
                    .parallel(true)
                    .chapterTitle(CommonNovelSpider.StringExtractor.of("div",
                            Map.of("class", "content"), element -> element.select("h1").text()))
                    .build()
    ),
    BIQUGE66(
            CommonNovelSpider.builder()
                    .parallel(true)
                    .chapterUri((doc, uri)->{
                        Elements elements = doc.select("div");
                        List<URI> uris = new LinkedList<>();
                        elements.stream().filter(element -> "list".equals(element.attr("id"))).forEach(element -> {
                            Elements a = element.select("a");
                            if (a.hasText() && "haitung".equals(a.attr("id"))) {
                                a.forEach(element1 -> {
                                    String href = element1.attr("href");
                                    uris.add(uri.resolve(href));
                                });
                            }
                        });
                        return uris;
                    })
                    .chapterTitle(CommonNovelSpider.StringExtractor.of("span",Map.of("class","divcss5"), Element::text))
                    .chapterContent(CommonNovelSpider.StringExtractor.of("div",
                            Map.of("id", "booktxt"), element -> {
                                element = element.select("div").get(0);
                                String s = element.outerHtml()
                                        .replaceAll("</?\\s*(br|p)\\s*/?\\s*>", "\n");
                                return Jsoup.parse(s).body().wholeText()
                                        .replaceAll("\\u00A0", " ")
                                        .replaceAll("\\r", "")
                                        .replaceAll("\\n[\\n ]*\\n", "\n")
                                        .replaceAll("\\n", System.lineSeparator())
                                        .trim();
                            }))
                    .build()
    ),
    /**
     * www.gudianmingzhu.com/
     */
    GUDIANMINGZHU(
            CommonNovelSpider.builder()
                    .title(document -> document.select("title").get(0).text())
                    .description(CommonNovelSpider.StringExtractor.of("meta",
                            Map.of("property", "description"), element -> element.attr("content")))
                    .author(document -> null)
                    .chapterUri((doc, uri) -> {
                        Elements elements = doc.select("div");
                        Element e = elements.stream().filter(element -> "djnr".equals(element.attr("class"))).findFirst().get();

                        List<URI> uris = new LinkedList<>();
                        Elements a = e.select("a");
                        if (a.hasText()) {
                            a.forEach(element1 -> {
                                String href = element1.attr("href");
                                uris.add(uri.resolve(href));
                            });
                        }
                        return uris;
                    })
                    .chapterTitle(CommonNovelSpider.StringExtractor.of("div",
                            Map.of("class", "ooo"), element -> element.select("h1").text()))
                    .chapterContent(CommonNovelSpider.StringExtractor.of("div",
                            Map.of("class", "ooo"), element -> {
                                element = element.select("div").get(0);
                                String s = element.outerHtml()
                                        .replaceAll("</?\\s*(br|p)\\s*/?\\s*>", "\n");
                                return Jsoup.parse(s).body().wholeText()
                                        .replaceAll("\\u00A0", " ")
                                        .replaceAll("\\r", "")
                                        .replaceAll("\\n[\\n ]*\\n", "\n")
                                        .replaceAll("\\n", System.lineSeparator())
                                        .trim();
                            }))
                    .build()
    )

    ;
    private final NovelSpider spider;

    NovelSpiders(NovelSpider spider) {
        this.spider = spider;
    }

    public NovelSpider getSpider() {
        return spider;
    }
}
