package cn.noy.javahw;

import java.net.URI;

/**
 * 小说爬虫接口。根据不同的网址有不同的实现。
 */
public interface NovelSpider {
    Novel getNovel(URI uri);
    default Novel getNovel(String uri){
        return getNovel(URI.create(uri));
    }
    Novel.Chapter getChapter(URI uri);
    default  Novel.Chapter getChapter(String uri){
        return getChapter(URI.create(uri));
    }
}
