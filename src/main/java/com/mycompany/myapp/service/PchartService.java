package com.mycompany.myapp.service;

import org.opencompare.PCMHelper;
import org.opencompare.PCMUtils;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class PchartService {

    private final Logger log = LoggerFactory.getLogger(PchartService.class);

    public List<String> listFiles() {

        List<String> lr = new ArrayList<>();

        File dir = new File("/Users/macher1/Downloads/model/");
        File[] pcms = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".pcm");
            }
        });

        int chartDimension = 3;

        for (File pcmFile : pcms) {


            try {
                PCM pcm = PCMUtils.loadPCM(pcmFile.getAbsolutePath());
                Collection<String> candidateFts = new PCMHelper().collectUniformAndNumericalFeatures(pcm);
                if (candidateFts.size() < chartDimension)
                    continue;

                lr.add(pcmFile.getName());
            }

            catch (IOException e) {

            }

        }
        return lr;
    }
}
