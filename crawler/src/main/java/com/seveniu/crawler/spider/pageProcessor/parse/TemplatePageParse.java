package com.seveniu.crawler.spider.pageProcessor.parse;

import com.seveniu.common.html.HtmlUtil;
import com.seveniu.entity.data.content.FieldContent;
import com.seveniu.entity.data.content.Link;
import com.seveniu.entity.template.structure.PageTemplate;
import com.seveniu.entity.template.structure.field.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.HtmlNode;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by seveniu on 7/24/17.
 * //TODO: 模板有误 如何处理
 * *
 */
public class TemplatePageParse {
    private static Logger logger = LoggerFactory.getLogger(TemplatePageParse.class);

    public static PageParseResult parse(PageTemplate pageTemplate, String html, String url) {
        return parse(pageTemplate, new Html(html, url), url);
    }


    public static PageParseResult parse(PageTemplate pageTemplate, Page page) {
        return parse(pageTemplate, page.getHtml(), page.getUrl().get());
    }

    public static PageParseResult parse(PageTemplate pageTemplate, Html html, String url) {
        PageParseResult pageParseResult = new PageParseResult(url);
        List<FieldContent> fieldContents = new LinkedList<>();
        pageParseResult.getPageContent().setFieldContents(fieldContents);
        for (Field field : pageTemplate.getFields()) {
            switch (field.getType()) {
                case NEXT_LEVEL_LINK:
                    List<String> urls = parseLinkLabel(html, field);
                    pageParseResult.setNextLevelUrls(urls);
                    break;
                case HTML_TEXT:
                    fieldContents.add(parseHtmlContent(html, field));
                    break;
                case NEXT_PAGE_LINK:
                    String nextPageUrl = parseNextPageLinkLabel(html, field);
                    if (nextPageUrl != null) {
                        pageParseResult.setPageUrls(Collections.singletonList(nextPageUrl));
                    }
                    break;
                case TEXT_LINK:
                    fieldContents.add(parseTextLinkField(html, field));
                    break;
                case PURE_TEXT:
                    fieldContents.add(parsePureContent(html, field));
                    break;
                case DOWNLOAD_LINK:
                    fieldContents.add(parseTextLinkField(html, field));
                    break;
                default:
                    logger.error("field html type is not found : {}", field.getType());
                    return null;
            }
        }
//        if (!parseResult.hasError() && !parseResult.isMatchField()) {
//            parseResult.setParseError(new ParseError(null, ParseErrorType.NO_FIELD_MATCH));
//        }
        return pageParseResult;
    }


    /////////////////////////////    parse label    ////////////////////////////
    private static Selectable parseBase(Html html, Field field) {
        String xpath = field.getXpath();
        if (xpath != null && xpath.length() > 0) {
            Selectable selectable = html.xpath(xpath);
            if (selectable.match()) {

                List<String> regexes = field.getRegex();
                if (regexes != null && regexes.size() > 0) {
                    for (String regex : regexes) {
                        selectable = selectable.regex(regex);
                    }
                }
            } else {
                if (field.isMust()) {
//                    parseResult.setParseError(new ParseError(field, ParseErrorType.NOT_FOUND_XPATH));
                }
            }
            return selectable;
        }

        return null;
    }

    private static FieldContent parseHtmlContent(Html html, Field field) {
        Selectable selectable = parseBase(html, field);
        if (selectable == null || !selectable.match()) {
            return null;
        }
        List<String> contents;
        if (field.isMultiple()) {
            contents = selectable.all();
        } else {
            String content = trim(selectable.get());
            if (content == null || content.length() == 0) {
                content = field.getDefaultValue();
            }
            contents = Collections.singletonList(content);
        }
        return new FieldContent(field, contents);
    }

    private static FieldContent parsePureContent(Html html, Field field) {
        Selectable selectable = parseBase(html, field);
        if (selectable == null || !selectable.match()) {
            return null;
        }
        String content = selectable.get();
        content = HtmlUtil.getPlainText(content);
        if (content == null || content.length() == 0) {
            content = field.getDefaultValue();
        }
        return new FieldContent(field, Collections.singletonList(content));
    }

    private static List<String> parseLinkLabel(Html html, Field field) {
        Selectable selectable = parseBase(html, field);
        if (selectable == null) {
            return Collections.emptyList();
        }
        List<String> hrefs = null;
        List<String> texts = null;
        if (selectable instanceof HtmlNode) {
            hrefs = selectable.links().all();
            texts = selectable.xpath("/text()").all();
        } else if (selectable instanceof PlainText) {
            hrefs = selectable.all();
        }
        List<String> urls = new LinkedList<>();
        if (hrefs == null || hrefs.size() == 0) {
            if (field.isMust()) {
//                parseResult.setParseError(new ParseError(field, ParseErrorType.NOT_FOUND_XPATH));
            }
        } else {
            for (int i = 0; i < hrefs.size(); i++) {
                String href = hrefs.get(i).trim();
                String text = "";
                if (texts != null) {
                    try {
                        text = texts.get(i);
                    } catch (Exception ignored) {
                    }
                }
                if (href.length() > 0) {
                    urls.add(href);
                }
            }
        }
        return urls;
    }


    /**
     * 不接受 regex 处理
     */
    private static String parseNextPageLinkLabel(Html html, Field field) {
        String xpath = field.getXpath();
        List<String> listUrls = html.xpath(xpath + "/@href").all();
        List<String> listTexts = html.xpath(xpath + "/text()").all();
        List<String> nextUrls = new LinkedList<>();
        if (listUrls == null || listUrls.size() == 0) {
            if (field.isMust()) {
//                parseResult.setParseError(new ParseError(field, ParseErrorType.NOT_FOUND_XPATH));
                return null;
            }
        } else {

            if (listUrls.size() == 1) {
                return listUrls.get(0);
            } else {
                if (field.getFilterContainText() != null) {
                    for (int i = 0; i < listTexts.size(); i++) {
                        String s = listTexts.get(i);
                        if (s.trim().contains(field.getFilterContainText().trim())) {
                            return listUrls.get(i);
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 不接受 regex 处理
     */
    private static void parseDownloadLinkLabel(Html html, Field field) {
        parseTextLinkField(html, field);
    }

    /**
     * 不接受 regex 处理
     */
    private static FieldContent parseTextLinkField(Html html, Field field) {
        String xpath = field.getXpath();
        Selectable selectable = html.xpath(xpath);
        if (selectable != null && selectable.match()) {
            List<String> hrefs = selectable.xpath("//a/@href").all();
            if (hrefs == null || hrefs.size() == 0) {
                hrefs = selectable.xpath("//img/@src").all();
            }
            if (hrefs != null && hrefs.size() > 0) {
                List<String> titles = selectable.xpath("//a/allText()").all();
                List<Link> links = new ArrayList<>(titles.size());
                for (int i = 0; i < titles.size(); i++) {
                    String title = titles.get(i).trim();
                    String url = hrefs.get(i).trim();
                    if (url.length() > 0) {
                        links.add(new Link(title, url));
                    }
                }
                return new FieldContent(field, null, links);
//                parseResult.addFieldResult(new FieldResult(field.getId(), field.getType(), field.getName(), links));
            }
        } else {
            if (field.isMust()) {
//                parseResult.setParseError(new ParseError(field, ParseErrorType.NOT_FOUND_XPATH));
            }
        }
        return null;
    }


    private static final String[] nextLinkText = new String[]{"下页", "[下一页]", "下一页", ">", "下页", "next"};
    private static final String[] containerNextLinkText = new String[]{"下一页", "下页", "next"};

    private static int findNextLink(List<String> linkTexts) {
        for (int i = 0; i < linkTexts.size(); i++) {
            String linkText = linkTexts.get(i);
            linkText = linkText.trim();
            for (String s : nextLinkText) {
                if (s.equals(linkText)) {
                    return i;
                }
            }
        }
        for (int i = 0; i < linkTexts.size(); i++) {
            String linkText = linkTexts.get(i);
            linkText = linkText.trim();
            for (String s : containerNextLinkText) {
                if (linkText.contains(s)) {
                    return i;
                }
            }
        }


        return -1;
    }


    private static String trim(String text) {
        String result = null;
        if (text != null) {
            result = text.trim();
        }
        return result;
    }
}
