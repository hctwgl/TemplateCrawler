package com.seveniu.crawler.spider.pipeline;

import com.seveniu.common.json.Json;
import com.seveniu.crawler.spider.Def;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by seveniu on 8/9/17.
 * *
 */
@Service
@ConditionalOnExpression("${crawler.pipeline.file.enable:false}")
public class FilePipeline implements Pipeline {

    private Path FILE_PATH;

    public FilePipeline(@Value("${crawler.pipeline.file.path:@null}") String filePath) throws IOException {
        this.FILE_PATH = Paths.get(filePath);
        if (!Files.exists(FILE_PATH)) {
            Files.createFile(FILE_PATH);
        }
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        try (BufferedWriter writer = Files.newBufferedWriter(FILE_PATH, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            Object obj = resultItems.get(Def.DATA_CONTENT_RESULT_KEY);
            writer.write(Json.toJson(obj));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void write(Path path, String text) {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            writer.newLine();
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
