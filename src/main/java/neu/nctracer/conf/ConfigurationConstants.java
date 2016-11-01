package neu.nctracer.conf;

/**
 * Constants to be read from <tt>configuration.properties</tt> file
 * 
 * @author Ankur Shanbhag
 */
public final class ConfigurationConstants {

    public static final String PROJECT_BASE_DIR = "base.project.path";

    public static final String LOCAL_SOURCE_IMAGE_FILE = "local.image.source.file";
    public static final String LOCAL_TARGET_IMAGE_FILE = "local.image.target.file";

    public static final String ERROR_THRESHOLD = "error.threshold";

    /* Hadoop configuration parameters */
    public static final String HADOOP_HOME = "hadoop.home";
    public static final String HADOOP_CONF_DIR = "hadoop.conf.path";

    // HDFS configurations
    public static final String HDFS_BASE_DIR = "hadoop.main.dir";
    public static final String DELETE_HDFS_DIRS = "hdfs.dirs.delete";

    public static final String GNUPLOT_PATH = "gnuplot.process.path";
    public static final String GNUPLOT_OUTPUT_PATH = "gnuplot.output.path";

    private ConfigurationConstants() {
        // Deny object creation
    }
}