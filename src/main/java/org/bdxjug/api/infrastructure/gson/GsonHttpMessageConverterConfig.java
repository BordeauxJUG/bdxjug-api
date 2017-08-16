package org.bdxjug.api.infrastructure.gson;

import com.google.gson.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import springfox.documentation.spring.web.json.Json;

@Configuration
public class GsonHttpMessageConverterConfig {

    @Bean
    public GsonHttpMessageConverter gsonHttpMessageConverter() {
        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
        converter.setGson(gson());
        return converter;
    }

    private Gson gson() {
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Json.class, springfoxJsonToGsonAdapter());
        return builder.create();
    }

    private JsonSerializer<Json> springfoxJsonToGsonAdapter() {
        return (json, type, jsonSerializationContext) -> {
            final JsonParser parser = new JsonParser();
            return parser.parse(json.value());
        };
    }
}