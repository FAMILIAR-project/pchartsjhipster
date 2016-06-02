import org.opencompare.PCMHelper;
import org.opencompare.PCMUtils;
import org.opencompare.ProductChartBuilder;
import org.opencompare.api.java.PCM;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by macher1 on 31/05/2016.
 */
public class HW {


    //public static void main (String[] args) throws IOException {
    public void foo1() throws IOException {
        System.out.println("Hello world!");
        ProductChartBuilder pb = new ProductChartBuilder(null, "", "");

        PCM pcm = PCMUtils.loadPCM("/Users/macher1/Downloads/model/Comparison_of_Nikon_DSLR_cameras_0.pcm");
        PCMHelper pcmmHelper = new PCMHelper();

        Collection<String> fts = pcmmHelper.collectUniformAndNumericalFeatures(pcm);
        for (String ft : fts) {
            Optional<Double> max = pcmmHelper.max(pcm, ft);
            Optional<Double> min = pcmmHelper.min(pcm, ft);
            System.err.println("Max (min) of feature " + ft + " = " + max.get() + " (" + min.get() + ")");

        }

    }



}
