package com.nhnent.forward.mybatistojpa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnent.forward.mybatistojpa.config.RootConfig;
import com.nhnent.forward.mybatistojpa.config.WebConfig;
import com.nhnent.forward.mybatistojpa.model.Item;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(classes = RootConfig.class),
        @ContextConfiguration(classes = WebConfig.class)
})
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class ItemControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;

    private Long itemId;


    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        itemId = createAvocado();
    }

    private Long createAvocado() throws Exception {
        String payload = "{ \"itemName\": \"avocado\", \"price\": 690 }";

        MvcResult result = mockMvc.perform(
                post("/items")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(payload)
                                               )
                                  .andExpect(status().isOk())
                                  .andReturn();

        Item item = objectMapper.readValue(result.getResponse().getContentAsString(), Item.class);

        Assert.assertNotNull(item);

        Assert.assertNotNull(item.getItemId());
        Assert.assertTrue(item.getItemId() > 0);

        return item.getItemId();
    }

    @Test
    public void getAvocado() throws Exception {
        Assert.assertNotNull(getAvocado(itemId));
    }

    private Item getAvocado(Long itemId) throws Exception {
        MvcResult result = mockMvc.perform(get("/items/{itemId}", itemId))
                                  .andExpect(status().isOk())
                                  .andReturn();

        Item item = objectMapper.readValue(result.getResponse().getContentAsString(), Item.class);

        Assert.assertNotNull(item);

        return item;
    }

    @Test
    public void updateAvocado() throws Exception {
        Item item = getAvocado(itemId);
        updateAvocado(item);
    }

    private void updateAvocado(Item item) throws Exception {
        String payload = objectMapper.writeValueAsString(item);

        MvcResult result = mockMvc.perform(
                put("/items/{itemId}", item.getItemId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(payload)
                                          )
                                  .andExpect(status().isOk())
                                  .andReturn();

        Item updatedItem = objectMapper.readValue(result.getResponse().getContentAsString(), Item.class);

        Assert.assertNotNull(updatedItem);
        Assert.assertEquals(item.getPrice(), updatedItem.getPrice());
    }

    @Test
    public void deleteAvocado() throws Exception {
        mockMvc.perform(delete("/items/{itemId}", itemId))
               .andExpect(status().isOk());
    }

}
