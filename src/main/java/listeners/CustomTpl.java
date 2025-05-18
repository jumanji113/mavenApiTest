package listeners;

import io.qameta.allure.restassured.AllureRestAssured;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomTpl {
    private static final AllureRestAssured FILTER = new AllureRestAssured();

    public static CustomTpl customLogFilter() {
        return InitLogFilter.logFilter;
    }

    public AllureRestAssured withCustomTemplates() {
        FILTER.setResponseTemplate("response.ftl");
        FILTER.setRequestTemplate("request.ftl");
        return FILTER;
    }

    private static class InitLogFilter {
        private static final CustomTpl logFilter = new CustomTpl();
    }
}
