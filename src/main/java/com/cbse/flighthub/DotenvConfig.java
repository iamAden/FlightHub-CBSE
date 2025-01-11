package com.cbse.flighthub;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvConfig {
    public DotenvConfig() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        System.setProperty("MONGO_URI", dotenv.get("MONGO_URI"));
    }
}
