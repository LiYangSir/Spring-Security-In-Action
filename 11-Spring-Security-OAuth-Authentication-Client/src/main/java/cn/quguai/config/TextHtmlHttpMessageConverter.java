package cn.quguai.config;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author LiYangSir
 * @date 2021/5/29
 */
public class TextHtmlHttpMessageConverter extends AbstractHttpMessageConverter {

    public TextHtmlHttpMessageConverter() {
        super(StandardCharsets.UTF_8, MediaType.TEXT_HTML);
    }

    @Override
    protected boolean supports(Class aClass) {
        return String.class == aClass;
    }

    @Override
    protected Object readInternal(Class aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        Charset charset = this.getContentTypeCharset(httpInputMessage.getHeaders().getContentType());
        return StreamUtils.copyToString(httpInputMessage.getBody(), charset);
    }

    private Charset getContentTypeCharset(MediaType contentType) {
        return contentType != null && contentType.getCharset() != null ? contentType.getCharset() : this.getDefaultCharset();
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {

    }
}
