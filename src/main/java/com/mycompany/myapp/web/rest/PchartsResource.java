package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.gson.*;
import com.mycompany.myapp.domain.Pcharts;
import com.mycompany.myapp.repository.PchartsRepository;
import com.mycompany.myapp.service.PchartService;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.opencompare.PCMHelper;
import org.opencompare.PCMUtils;
import org.opencompare.ProductChartBuilder;
import org.opencompare.api.java.PCM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.trimou.Mustache;

import javax.inject.Inject;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing Pcharts.
 */
@RestController
@RequestMapping("/api")
public class PchartsResource {

    private final Logger log = LoggerFactory.getLogger(PchartsResource.class);

    @Inject
    private PchartsRepository pchartsRepository;


    @Inject
    private PchartService pChart;

    /**
     * POST  /pchartss -> Create a new pcharts.
     */
    @RequestMapping(value = "/pchartss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pcharts> createPcharts(@RequestBody Pcharts pcharts) throws URISyntaxException {
        log.debug("REST request to save Pcharts : {}", pcharts);
        if (pcharts.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("pcharts", "idexists", "A new pcharts cannot already have an ID")).body(null);
        }
        Pcharts result = pchartsRepository.save(pcharts);
        return ResponseEntity.created(new URI("/api/pchartss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("pcharts", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pchartss -> Updates an existing pcharts.
     */
    @RequestMapping(value = "/pchartss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pcharts> updatePcharts(@RequestBody Pcharts pcharts) throws URISyntaxException {
        log.debug("REST request to update Pcharts : {}", pcharts);
        if (pcharts.getId() == null) {
            return createPcharts(pcharts);
        }
        Pcharts result = pchartsRepository.save(pcharts);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("pcharts", pcharts.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pchartss -> get all the pchartss.
     */
    @RequestMapping(value = "/pchartss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Pcharts> getAllPchartss() {
        log.debug("REST request to get all Pchartss");
        return pchartsRepository.findAll();
            }


    /**
     * GET  /pcms -> get all the PCM files
     */
    @RequestMapping(value = "/pcms",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<String> getAllPCMs() {
        log.debug("REST request to get all PCMs");
        return pChart.listFiles();
    }


    /**
     * GET  /pcms/:id -> get the product chart of the PCM "id" (id: filename)
     */


    @RequestMapping(value = "/pcms/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public String getProductChart(@PathVariable String id) {
        log.debug("REST request to produce product chart : {}", id);
// Comparison_of_Nikon_DSLR_cameras_0.pcm
        try {
            PCM pcm = PCMUtils.loadPCM("/Users/macher1/Downloads/model/" + id + ".pcm");
            PCMHelper pcmmHelper = new PCMHelper();

            Collection<String> fts = pcmmHelper.collectUniformAndNumericalFeatures(pcm);
            for (String ft : fts) {
                Optional<Double> max = pcmmHelper.max(pcm, ft);
                Optional<Double> min = pcmmHelper.min(pcm, ft);
                log.debug("Max (min) of feature " + ft + " = " + max.get() + " (" + min.get() + ")");

            }



            Collection<String> candidateFts = new PCMHelper().collectUniformAndNumericalFeatures(pcm);

            if (candidateFts.size() < 3) {
                return _mkError("Impossible to produce a product chart for PCM, size of candidates fts < 3: " + id);
            }

            List<String> lCandidateFts = new ArrayList<String>(candidateFts);
            Collections.shuffle(lCandidateFts);
            List<String> lfts = lCandidateFts.subList(0, 3);
            String xFeature = lfts.get(0);
            String yFeature = lfts.get(1);
            String zFeature = lfts.get(2);

            ProductChartBuilder pchart = new ProductChartBuilder(pcm, xFeature, yFeature, zFeature);

            String data = pchart.buildData();

            JsonObject jso = new JsonObject();
            jso.add("pcmData", new JsonParser().parse(data).getAsJsonObject());

           // valueTemplates.put("pcmTitle", pcmName);
            jso.add("xFeature", new JsonPrimitive(xFeature));
            jso.add("yFeature", new JsonPrimitive(yFeature));
            jso.add("zFeature", new JsonPrimitive(zFeature));

            JsonArray jsA = new JsonArray();
            candidateFts.stream().forEach(cFt -> jsA.add(cFt));
            jso.add("candidateFts", jsA);


            return new Gson().toJson(jso);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return _mkError("Impossible to produce a product chart, unknown PCM: " + id);

    }

    private String _mkError(String errorMsg) {
        JsonObject jso = new JsonObject();
        jso.add("error", new JsonPrimitive(errorMsg));
        return new Gson().toJson(jso);
    }

    /**
     * GET  /pchartss/:id -> get the "id" pcharts.
     */
    @RequestMapping(value = "/pchartss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pcharts> getPcharts(@PathVariable Long id) {
        log.debug("REST request to get Pcharts : {}", id);
        Pcharts pcharts = pchartsRepository.findOne(id);
        return Optional.ofNullable(pcharts)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pchartss/:id -> delete the "id" pcharts.
     */
    @RequestMapping(value = "/pchartss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePcharts(@PathVariable Long id) {
        log.debug("REST request to delete Pcharts : {}", id);
        pchartsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pcharts", id.toString())).build();
    }
}
