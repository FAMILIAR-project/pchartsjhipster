package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Pchart;
import com.mycompany.myapp.repository.PchartRepository;

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
 * Test class for the PchartResource REST controller.
 *
 * @see PchartResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PchartResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private PchartRepository pchartRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPchartMockMvc;

    private Pchart pchart;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PchartResource pchartResource = new PchartResource();
        ReflectionTestUtils.setField(pchartResource, "pchartRepository", pchartRepository);
        this.restPchartMockMvc = MockMvcBuilders.standaloneSetup(pchartResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        pchart = new Pchart();
        pchart.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createPchart() throws Exception {
        int databaseSizeBeforeCreate = pchartRepository.findAll().size();

        // Create the Pchart

        restPchartMockMvc.perform(post("/api/pcharts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pchart)))
                .andExpect(status().isCreated());

        // Validate the Pchart in the database
        List<Pchart> pcharts = pchartRepository.findAll();
        assertThat(pcharts).hasSize(databaseSizeBeforeCreate + 1);
        Pchart testPchart = pcharts.get(pcharts.size() - 1);
        assertThat(testPchart.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllPcharts() throws Exception {
        // Initialize the database
        pchartRepository.saveAndFlush(pchart);

        // Get all the pcharts
        restPchartMockMvc.perform(get("/api/pcharts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pchart.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getPchart() throws Exception {
        // Initialize the database
        pchartRepository.saveAndFlush(pchart);

        // Get the pchart
        restPchartMockMvc.perform(get("/api/pcharts/{id}", pchart.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(pchart.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPchart() throws Exception {
        // Get the pchart
        restPchartMockMvc.perform(get("/api/pcharts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePchart() throws Exception {
        // Initialize the database
        pchartRepository.saveAndFlush(pchart);

		int databaseSizeBeforeUpdate = pchartRepository.findAll().size();

        // Update the pchart
        pchart.setName(UPDATED_NAME);

        restPchartMockMvc.perform(put("/api/pcharts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pchart)))
                .andExpect(status().isOk());

        // Validate the Pchart in the database
        List<Pchart> pcharts = pchartRepository.findAll();
        assertThat(pcharts).hasSize(databaseSizeBeforeUpdate);
        Pchart testPchart = pcharts.get(pcharts.size() - 1);
        assertThat(testPchart.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deletePchart() throws Exception {
        // Initialize the database
        pchartRepository.saveAndFlush(pchart);

		int databaseSizeBeforeDelete = pchartRepository.findAll().size();

        // Get the pchart
        restPchartMockMvc.perform(delete("/api/pcharts/{id}", pchart.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Pchart> pcharts = pchartRepository.findAll();
        assertThat(pcharts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
