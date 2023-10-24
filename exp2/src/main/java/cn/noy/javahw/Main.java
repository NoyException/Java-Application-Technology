package cn.noy.javahw;

import java.util.Scanner;

public class Main {

    /**
     * 程序入口<br>
     * 测试所用小说网址：
     * <li><a href="http://www.zzzcn.org/197_197480/">科普物理，教出一票法神？</a></li> 选取原因：体积小，章节少，方便测试
     * <li><a href="https://www.ishuquge.la/txt/158004/index.html">道诡异仙</a></li> 选取原因：体积大，且我喜欢看:)
     * <li><a href="http://www.biquge66.net/book/697/">庆余年</a></li> 选取原因：体积大，适合做并发测试
     */
    public static void main(String[] args) {
        //获取小说uri
        String uri;
        if(args==null || args.length==0){
            System.out.println("请输入小说（目录）的网址：");
            Scanner scanner = new Scanner(System.in);
            uri = scanner.next();
        }
        else uri = args[0];
        //获取对应爬虫
        NovelSpider spider;
        if(uri.contains("www.zzzcn.org")) spider = NovelSpiders.ZZZCN.getSpider();
        else if(uri.contains("www.ishuquge.la")) spider = NovelSpiders.ISHUQUGE.getSpider();
        else if(uri.contains("www.biquge66.net")) spider = NovelSpiders.BIQUGE66.getSpider();
        else spider = CommonNovelSpider.createDefault();
        //爬取小说
        System.out.println("开始爬取小说...");
        Novel novel = spider.getNovel(uri);
        novel.saveToFile(".");
        System.out.println("爬取完成");
    }
}