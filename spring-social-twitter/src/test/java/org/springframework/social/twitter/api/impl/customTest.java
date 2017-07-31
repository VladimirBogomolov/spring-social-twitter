package org.springframework.social.twitter.api.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.social.twitter.api.UserList;

import java.io.IOException;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Created by olmer on 19.06.17.
 */
public class customTest extends AbstractTwitterApiTest{
    @Test
    public void serializationListTest() throws IOException {
        mockServer.expect(requestTo("https://api.twitter.com/1.1/lists/show.json?list_id=40841803"))
                .andExpect(method(GET))
                .andRespond(withSuccess(jsonResource("mylist"), APPLICATION_JSON));

        ObjectMapper twitterObjectMapper = ((MappingJackson2HttpMessageConverter)
                (twitter
                        .getRestTemplate()
                        .getMessageConverters()
                        .stream()
                        .filter(httpMessageConverter -> httpMessageConverter instanceof MappingJackson2HttpMessageConverter)
                        .findAny()
                        .get()
                )).getObjectMapper();
        UserList list = twitter.listOperations().getList(40841803);
        String listJson = twitterObjectMapper.writeValueAsString(list);
        UserList userList = twitterObjectMapper.readValue(listJson, UserList.class);

    }
}
