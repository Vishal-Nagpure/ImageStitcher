package neu.nctracer.mr;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import neu.nctracer.conf.cli.ConfigurationParams;
import neu.nctracer.exception.HdfsException;
import neu.nctracer.utils.HdfsFileUtils;

/**
 * Sets up resources needed by mapreduce job to perform image stitching
 * operation.
 * 
 * @author Ankur Shanbhag
 */
public class ImageDataClusteringDriver extends MapReduceStitchingDriver {

    private String hdfsInputPath = null;
    private String threshold = null;

    public ImageDataClusteringDriver() {
        super(ImageDataClusteringDriver.class);
    }

    /**
     * Copies all the local files to HDFS required by the mapreduce job
     */
    public void setup(ConfigurationParams params) throws HdfsException {
        super.setup(params);

        threshold = params.getParam("error.threshold", "5");
        
        String inputPath = params.getParam("local.input.path", null);
        if (null == inputPath || inputPath.isEmpty())
            throw new IllegalArgumentException("Mandatory parameter [local.input.path] is not set. "
                                               + "This parameter specifies input configuration params for clustering.");

        hdfsInputPath = HdfsFileUtils.copyFromLocal(inputPath, hdfsBaseDirPath, conf);
        logger.info("Image stitching mapreduce job setup successful.");
    }

    /**
     * Copies all required libraries to mapreduce job class path. Kicks off the
     * mapreduce job. Every input line specifies configurations defined for
     * clustering data points.
     */
    public boolean run() throws HdfsException {

        try {
            Job job = createJobInstance();
            job.setMapperClass(ImageDataClusteringMapper.class);

            job.getConfiguration().setInt("mapreduce.input.lineinputformat.linespermap", 3);
            job.getConfiguration().set(HdfsConstants.IMAGE_MATCHING_ERROR, threshold);

            addImageFilesToCache(job);
            
            // add all required jars to mapreduce job
            addJarsToDistributedCache(job);

            NLineInputFormat.addInputPath(job, new Path(hdfsInputPath));
            job.setInputFormatClass(NLineInputFormat.class);

            // map only job
            job.setNumReduceTasks(0);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(NullWritable.class);

            FileOutputFormat.setOutputPath(job, new Path(hdfsOutputPath));

            logger.info("Starting mapreduce job to perform image stitching operation.");
            boolean status = job.waitForCompletion(true);
            cleanup(job, status);
            return status;
        } catch (IOException e) {
            throw new HdfsException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new HdfsException(e.getMessage(), e);
        } catch (InterruptedException e) {
            throw new HdfsException(e.getMessage(), e);
        } catch (URISyntaxException e) {
            throw new HdfsException(e.getMessage(), e);
        }
    }
}

