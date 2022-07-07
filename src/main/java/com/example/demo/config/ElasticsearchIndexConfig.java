package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("codemao.es")
public class ElasticsearchIndexConfig {

    /**
     * 用户的index
     */
    private String unifiedOrderIndex;


    public String getUnifiedOrderIndex() {
        return unifiedOrderIndex;
    }

    public void setUnifiedOrderIndex(String unifiedOrderIndex) {
        this.unifiedOrderIndex = unifiedOrderIndex;
    }


    @Override
    public String toString() {
        return "ElasticsearchIndexConfig{" + "unifiedOrderIndex='" + unifiedOrderIndex + '\''  + '}';
    }
}
