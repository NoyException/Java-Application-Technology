package cn.noy.javahw;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.net.URI;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CommonNovelSpider implements NovelSpider {

    private final HttpClient HttpClient = HttpClients.createDefault();
    private boolean parallel;
    private StringExtractor titleExtractor;
    private StringExtractor descriptionExtractor;
    private StringExtractor authorExtractor;
    private BiFunction<Document, URI, Collection<URI>> chapterUriExtractor;
    private StringExtractor chapterTitleExtractor;
    private StringExtractor chapterContentExtractor;
    private Function<URI, Integer> chapterIdExtractor;

    public static Builder builder() {
        return new Builder();
    }

    public static CommonNovelSpider createDefault(){
        return builder().build();
    }

    public boolean isParallel() {
        return parallel;
    }

    /**
     * 根据uri获取小说
     *
     * @param uri 小说的uri
     * @return 小说对象
     */
    @Override
    public Novel getNovel(URI uri) {
        //创建一个HttpGet对象，用于发送请求
        HttpGet httpGet = new HttpGet(uri);
        //设置请求头，模拟浏览器访问
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36");
        try {
            //发送请求，并获取响应对象
            HttpEntity entity = HttpClient.execute(httpGet).getEntity();
            //响应失败则直接返回null
            if (entity == null) return null;
            //将响应内容转换为字符串
            String html = EntityUtils.toString(entity, "utf-8");
            //使用Jsoup解析html文档，得到一个Document对象
            Document doc = Jsoup.parse(html);
            //提取小说信息
            Novel.Builder builder = Novel.builder()
                    .title(titleExtractor.apply(doc))
                    .description(descriptionExtractor.apply(doc))
                    .author(authorExtractor.apply(doc));
            //提取章节（并发）
            Stream<URI> stream;
            if(parallel)
                stream = chapterUriExtractor.apply(doc, uri).parallelStream();
            else
                stream = chapterUriExtractor.apply(doc, uri).stream();
            stream.forEach(chapterUri -> {
                int id = chapterIdExtractor.apply(chapterUri);
                Novel.Chapter chapter = getChapter(chapterUri);
                builder.chapter(id, chapter);
            });
            return builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据uri获取章节
     *
     * @param uri 章节的uri
     * @return 章节对象
     */
    public Novel.Chapter getChapter(URI uri) {
        //创建一个HttpGet对象，用于发送请求
        HttpGet httpGet = new HttpGet(uri);
        //设置请求头，模拟浏览器访问
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36");
        try {
            //发送请求，并获取响应对象
            HttpEntity entity = HttpClient.execute(httpGet).getEntity();
            //响应失败则直接返回null
            if (entity == null) return null;
            //将响应内容转换为字符串
            String html = EntityUtils.toString(entity, "utf-8");
            //使用Jsoup解析html文档，得到一个Document对象
            Document doc = Jsoup.parse(html);
            //先提取章节标题
            String title = chapterTitleExtractor.apply(doc);
            System.out.println("提取章节：" + title);
            //再提取章节内容
            String content = chapterContentExtractor.apply(doc);

            return new Novel.Chapter(title, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface StringExtractor extends Function<Document, String> {

        static StringExtractor of(String cssQuery, Map<String, String> attributeRestrictions, Function<Element, String> fromElement) {
            return doc -> {
                Elements elements = doc.select(cssQuery);
                AtomicReference<String> s = new AtomicReference<>("");
                elements.stream().filter(element -> {
                            for (Map.Entry<String, String> entry : attributeRestrictions.entrySet()) {
                                if (!entry.getValue().equals(element.attr(entry.getKey())))
                                    return false;
                            }
                            return true;
                        })
                        .forEach(element -> {
                            String value = fromElement.apply(element);
                            if (!value.isEmpty()) s.set(value);
                        });
                return s.get();
            };
        }
    }

    public static class Builder {
        private boolean parallel = false;
        private StringExtractor titleExtractor = StringExtractor.of("meta",
                Map.of("property", "og:novel:book_name"), element -> element.attr("content"));
        private StringExtractor descriptionExtractor = StringExtractor.of("meta",
                Map.of("property", "og:description"), element -> element.attr("content"));
        private StringExtractor authorExtractor = StringExtractor.of("meta",
                Map.of("property", "og:novel:author"), element -> element.attr("content"));
        private BiFunction<Document, URI, Collection<URI>> chapterUriExtractor = (doc, uri) -> {
            Elements elements = doc.select("dd");
            List<URI> uris = new LinkedList<>();
            elements.forEach(element -> {
                Elements a = element.select("a");
                if (a.hasText()) {
                    a.forEach(element1 -> {
                        String href = element1.attr("href");
                        uris.add(uri.resolve(href));
                    });
                }
            });
            return uris;
        };
        private StringExtractor chapterTitleExtractor = StringExtractor.of("div",
                Map.of("class", "bookname"), element -> element.select("h1").text());
        private StringExtractor chapterContentExtractor = StringExtractor.of("div",
                Map.of("id", "content"), element -> {
                    String s = element.outerHtml()
                            .replaceAll("</?\\s*(br|p)\\s*/?\\s*>", "\n");
                    return Jsoup.parse(s).body().wholeText()
                            .replaceAll("\\u00A0", " ")
                            .replaceAll("\\r", "")
                            .replaceAll("\\n[\\n ]*\\n", "\n")
                            .replaceAll("\\n", System.lineSeparator())
                            .trim();
                });

        private Function<URI, Integer> chapterIdExtractor = uri -> {
            int id = uri.hashCode();
            Pattern pattern = Pattern.compile("\\d+\\.html$");
            Matcher matcher = pattern.matcher(uri.toString());
            if (matcher.find()) {
                String match = matcher.group();
                id = Integer.parseInt(match.substring(0, match.length() - 5));
            }
            return id;
        };

        private Builder() {
        }

        public Builder parallel(boolean parallel){
            this.parallel = parallel;
            return this;
        }

        public Builder title(StringExtractor titleExtractor) {
            this.titleExtractor = titleExtractor;
            return this;
        }

        public Builder description(StringExtractor descriptionExtractor) {
            this.descriptionExtractor = descriptionExtractor;
            return this;
        }

        public Builder author(StringExtractor authorExtractor) {
            this.authorExtractor = authorExtractor;
            return this;
        }

        public Builder chapterUri(BiFunction<Document, URI, Collection<URI>> chapterUriExtractor) {
            this.chapterUriExtractor = chapterUriExtractor;
            return this;
        }

        public Builder chapterTitle(StringExtractor chapterTitleExtractor) {
            this.chapterTitleExtractor = chapterTitleExtractor;
            return this;
        }

        public Builder chapterContent(StringExtractor chapterContentExtractor) {
            this.chapterContentExtractor = chapterContentExtractor;
            return this;
        }

        public Builder chapterId(Function<URI, Integer> chapterIdExtractor) {
            this.chapterIdExtractor = chapterIdExtractor;
            return this;
        }

        public CommonNovelSpider build() {
            CommonNovelSpider spider = new CommonNovelSpider();
            spider.parallel = parallel;
            spider.titleExtractor = titleExtractor;
            spider.descriptionExtractor = descriptionExtractor;
            spider.authorExtractor = authorExtractor;
            spider.chapterUriExtractor = chapterUriExtractor;
            spider.chapterTitleExtractor = chapterTitleExtractor;
            spider.chapterContentExtractor = chapterContentExtractor;
            spider.chapterIdExtractor = chapterIdExtractor;
            return spider;
        }
    }

}
