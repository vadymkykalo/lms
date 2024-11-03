package com.vadymkykalo.lms.service.markdown;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class MarkdownServiceImpl implements MarkdownService {

    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();

    public String renderMarkdown(String filePath) throws Exception {
        String markdown = Files.readString(Path.of(filePath), StandardCharsets.UTF_8);
        return renderer.render(parser.parse(markdown));
    }
}
