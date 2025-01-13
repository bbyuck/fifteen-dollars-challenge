package com.bb.fifteen.common.util;

import com.bb.fifteen.FifteenDollarChallengeApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@Component
public class ResourceLoader {
    public String loadResource(String resourcePath) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(FifteenDollarChallengeApplication.class.getClassLoader().getResourceAsStream(resourcePath)), StandardCharsets.UTF_8))) {
            StringBuilder script = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                script.append(line).append("\n");
            }
            return script.toString();
        }
        catch(Exception e){
            log.error(e.getMessage(), e);
            throw new IllegalStateException("리소스를 로드하는 과정에서 에러가 발생했습니다.");
        }
    }
}
