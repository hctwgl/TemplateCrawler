package com.seveniu.entity.template.structure.field;

/**
 * Created by seveniu on 7/22/17.
 * *
 */
public enum FieldType {
    HTML_TEXT,          // HTML_TEXT 文本
    NEXT_LEVEL_LINK,        // NEXT_LEVEL_LINK 下一个网页跳转链接
    NEXT_PAGE_LINK,          // NEXT_PAGE_LINK 下一页链接
    TEXT_LINK,          // TEXT_LINK 获取链接的文本 及 链接，改链接不作为跳转链接 以及下一页链接
    PURE_TEXT,          // PURE_TEXT 获取纯文本
    DOWNLOAD_LINK;      // DOWNLOAD_LINK 下载链接
}
