package cn.noy.javahw;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 代表了一本小说
 */
public class Novel {
    private URI uri;
    private String title;
    private String description;
    private String author;
    private Map<Integer, Chapter> chapters;

    @Contract(" -> new")
    public static @NotNull Builder builder() {
        return new Builder();
    }

    public URI getURI() {
        return uri;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public Chapter getChapter(int id) {
        return chapters.get(id);
    }

    public Map<Integer, Chapter> getChapters() {
        return chapters;
    }

    /**
     * 将小说转换为人肉眼可读的字符串
     *
     * @return 可读的字符串
     */
    public String toReadableString() {
        StringBuilder sb = new StringBuilder();
        sb.append('《').append(title).append('》').append(System.lineSeparator());
        if(author!=null && !author.isEmpty()) sb.append("作者：").append(author).append(System.lineSeparator());
        if(description!=null && !description.isEmpty()) sb.append("描述：").append(description).append(System.lineSeparator());
        chapters.forEach((id, chapter) ->
                sb.append(System.lineSeparator())
                        .append(chapter.title())
                        .append(System.lineSeparator())
                        .append(chapter.content())
                        .append(System.lineSeparator()));
        return sb.toString();
    }

    /**
     * 将小说保存到文件
     *
     * @param path 文件路径
     */
    public void saveToFile(String path) {
        File file = new File(path + "/" + title + ".txt");
        if (file.exists()) {
            System.out.println("文件已存在");
            return;
        }

        try {
            boolean res = file.createNewFile();
            if (!res) {
                System.out.println("文件创建失败");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("文件创建失败");
            return;
        }

        try (FileOutputStream out = new FileOutputStream(file)) {
            out.write(toReadableString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Novel{" +
                "uri=" + uri +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                ", chapters=" + chapters +
                '}';
    }

    /**
     * Novel的构造器
     */
    public static class Builder {
        private URI uri;
        private String title;
        private String description;
        private String author;
        private final Map<Integer, Chapter> chapters = new TreeMap<>();

        private Builder() {
        }

        public Builder uri(URI uri) {
            this.uri = uri;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder chapter(int id, String title, String content) {
            this.chapters.put(id, new Chapter(title, content));
            return this;
        }

        public Builder chapter(int id, Chapter chapter) {
            this.chapters.put(id, chapter);
            return this;
        }

        public Novel build() {
            Novel novel = new Novel();
            novel.uri = this.uri;
            novel.title = this.title;
            novel.description = this.description;
            novel.author = this.author;
            novel.chapters = this.chapters;
            return novel;
        }
    }

    /**
     * 章节
     *
     * @param title   章节标题
     * @param content 章节内容
     */
    public record Chapter(String title, String content) {
        @Override
        public String toString() {
            return "Chapter{" +
                    "title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }
}
