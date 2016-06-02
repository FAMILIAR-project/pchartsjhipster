package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Pchart;
import com.mycompany.myapp.repository.PchartRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Pchart.
 */
@RestController
@RequestMapping("/api")
public class PchartResource {

    private final Logger log = LoggerFactory.getLogger(PchartResource.class);
        
    @Inject
    private PchartRepository pchartRepository;
    
    /**
     * POST  /pcharts -> Create a new pchart.
     */
    @RequestMapping(value = "/pcharts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pchart> createPchart(@RequestBody Pchart pchart) throws URISyntaxException {
        log.debug("REST request to save Pchart : {}", pchart);
        if (pchart.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("pchart", "idexists", "A new pchart cannot already have an ID")).body(null);
        }
        Pchart result = pchartRepository.save(pchart);
        return ResponseEntity.created(new URI("/api/pcharts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("pchart", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pcharts -> Updates an existing pchart.
     */
    @RequestMapping(value = "/pcharts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pchart> updatePchart(@RequestBody Pchart pchart) throws URISyntaxException {
        log.debug("REST request to update Pchart : {}", pchart);
        if (pchart.getId() == null) {
            return createPchart(pchart);
        }
        Pchart result = pchartRepository.save(pchart);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("pchart", pchart.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pcharts -> get all the pcharts.
     */
    @RequestMapping(value = "/pcharts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Pchart> getAllPcharts() {
        log.debug("REST request to get all Pcharts");
        return pchartRepository.findAll();
            }

    /**
     * GET  /pcharts/:id -> get the "id" pchart.
     */
    @RequestMapping(value = "/pcharts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pchart> getPchart(@PathVariable Long id) {
        log.debug("REST request to get Pchart : {}", id);
        Pchart pchart = pchartRepository.findOne(id);
        return Optional.ofNullable(pchart)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pcharts/:id -> delete the "id" pchart.
     */
    @RequestMapping(value = "/pcharts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePchart(@PathVariable Long id) {
        log.debug("REST request to delete Pchart : {}", id);
        pchartRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pchart", id.toString())).build();
    }
}
