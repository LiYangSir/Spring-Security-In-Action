package cn.quguai.config;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;

/**
 * @author LiYangSir
 * @date 2021/5/29
 */
public class JacksonFromTextHtmlHttpMessageConverter extends MappingJackson2HttpMessageConverter {

    public JacksonFromTextHtmlHttpMessageConverter() {
        ArrayList<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.TEXT_HTML);
        setSupportedMediaTypes(mediaTypes);
    }
}
