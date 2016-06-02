package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Pcharts;
import com.mycompany.myapp.repository.PchartsRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PchartsResource REST controller.
 *
 * @see PchartsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PchartsResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_LOCATION = "AAAAA";
    private static final String UPDATED_LOCATION = "BBBBB";

    @Inject
    private PchartsRepository pchartsRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPchartsMockMvc;

    private Pcharts pcharts;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PchartsResource pchartsResource = new PchartsResource();
        ReflectionTestUtils.setField(pchartsResource, "pchartsRepository", pchartsRepository);
        this.restPchartsMockMvc = MockMvcBuilders.standaloneSetup(pchartsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        pcharts = new Pcharts();
        pcharts.setName(DEFAULT_NAME);
        pcharts.setLocation(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    public void createPcharts() throws Exception {
        int databaseSizeBeforeCreate = pchartsRepository.findAll().size();

        // Create the Pcharts

        restPchartsMockMvc.perform(post("/api/pchartss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pcharts)))
                .andExpect(status().isCreated());

        // Validate the Pcharts in the database
        List<Pcharts> pchartss = pchartsRepository.findAll();
        assertThat(pchartss).hasSize(databaseSizeBeforeCreate + 1);
        Pcharts testPcharts = pchartss.get(pchartss.size() - 1);
        assertThat(testPcharts.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPcharts.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    public void getAllPchartss() throws Exception {
        // Initialize the database
        pchartsRepository.saveAndFlush(pcharts);

        // Get all the pchartss
        restPchartsMockMvc.perform(get("/api/pchartss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pcharts.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())));
    }

    @Test
    @Transactional
    public void getPcharts() throws Exception {
        // Initialize the database
        pchartsRepository.saveAndFlush(pcharts);

        // Get the pcharts
        restPchartsMockMvc.perform(get("/api/pchartss/{id}", pcharts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(pcharts.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPcharts() throws Exception {
        // Get the pcharts
        restPchartsMockMvc.perform(get("/api/pchartss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePcharts() throws Exception {
        // Initialize the database
        pchartsRepository.saveAndFlush(pcharts);

		int databaseSizeBeforeUpdate = pchartsRepository.findAll().size();

        // Update the pcharts
        pcharts.setName(UPDATED_NAME);
        pcharts.setLocation(UPDATED_LOCATION);

        restPchartsMockMvc.perform(put("/api/pchartss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pcharts)))
                .andExpect(status().isOk());

        // Validate the Pcharts in the database
        List<Pcharts> pchartss = pchartsRepository.findAll();
        assertThat(pchartss).hasSize(databaseSizeBeforeUpdate);
        Pcharts testPcharts = pchartss.get(pchartss.size() - 1);
        assertThat(testPcharts.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPcharts.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void deletePcharts() throws Exception {
        // Initialize the database
        pchartsRepository.saveAndFlush(pcharts);

		int databaseSizeBeforeDelete = pchartsRepository.findAll().size();

        // Get the pcharts
        restPchartsMockMvc.perform(delete("/api/pchartss/{id}", pcharts.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Pcharts> pchartss = pchartsRepository.findAll();
        assertThat(pchartss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
