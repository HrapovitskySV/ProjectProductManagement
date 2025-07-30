package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.converters.AbstractInfoSystemMapperImpl;
import ru.otus.hw.converters.AbstractTaskMapperImpl;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.*;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({InfosystemPagesController.class, TaskPagesController.class})
@Import({AbstractTaskMapperImpl.class, AbstractInfoSystemMapperImpl.class, SecurityConfiguration.class})// включаю настройки Security  //, AuthorService.class, GenreService.class
class PagesControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private InfoSystemService infoSystemService;

    @MockBean
    private ProductService productService;

    @MockBean
    private TaskTypeService taskTypeService;

    @MockBean
    private UserService userService;

    @MockBean
    private LoadTaskFomExcelService loadTaskFomExcelService;

    @DisplayName("Should return expected status")
    @ParameterizedTest(name = "{0} {1} for user {2} should return {4} status")
    @MethodSource("getTestData")
    void shouldReturnExpectedStatus(String method, String url, String userName, String[] roles, int status, boolean checkLoginRedirection) throws Exception {

        var request = method2RequestBuilder(method, url);

        if (nonNull(userName)) {
            request = request.with((user(userName).roles(roles)));
        }
        ResultActions resultActions = mvc.perform(request)
                .andExpect(status().is(status));

        if (checkLoginRedirection) {
            resultActions.andExpect(redirectedUrlPattern("**/login"));
        }
    }

    private MockHttpServletRequestBuilder method2RequestBuilder(String method, String url) {
        Map<String, Function<String, MockHttpServletRequestBuilder>> methodMap =
                Map.of("get", MockMvcRequestBuilders::get,
                        "post", MockMvcRequestBuilders::post);

        return  methodMap.get(method).apply(url);
    }

    public static Stream<Arguments> getTestData() {
        var roles = new String[] {"USER"};
        var adminRoles = new String[] {"ADMIN"};


        return Stream.of(
                Arguments.of("get", "/tasks", null, null,302, true),
                Arguments.of("get", "/tasks/edit/1", null, null,302, true),
                Arguments.of("get", "/tasks/add", null, null,302, true),
                Arguments.of("get", "/infosystems/", null, null,302, true),
                Arguments.of("get", "/infosystems/edit/1", null, null,302, true),
                Arguments.of("get", "/infosystems/add", null, null,302, true),
                Arguments.of("get", "/tasks", "USER", roles,200, false),
                Arguments.of("get", "/tasks/edit/1", "USER", roles,200, false),
                Arguments.of("get", "/tasks/add", "USER", roles,200, false),
                Arguments.of("get", "/infosystems", "USER", roles,200, false),
                Arguments.of("get", "/infosystems/edit/1", "USER", roles,200, false),
                Arguments.of("get", "/infosystems/add", "USER", roles,200, false)

        );
    }
}