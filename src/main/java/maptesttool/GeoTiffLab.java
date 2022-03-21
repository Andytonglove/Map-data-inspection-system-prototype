package maptesttool;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.data.Parameter;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.StyleLayer;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.action.SafeAction;
import org.geotools.swing.data.JParameterListWizard;
import org.geotools.swing.wizard.JWizard;
import org.geotools.util.KVP;
import org.geotools.util.factory.Hints;
import org.opengis.filter.FilterFactory2;
import org.opengis.style.ContrastMethod;

public class GeoTiffLab {

    private StyleFactory sf = CommonFactoryFinder.getStyleFactory();
    private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    private JMapFrame frame;
    private GridCoverage2DReader reader;

    public String getTiffLayersAndDisplay() throws Exception {
        List<Parameter<?>> list = new ArrayList<>();
        list.add(
                new Parameter<>(
                        "image",
                        File.class,
                        "Image",
                        "GeoTiff or World+Image to display as basemap",
                        new KVP(Parameter.EXT, "tif", Parameter.EXT, "jpg")));

        JParameterListWizard wizard = new JParameterListWizard("Display GeoTiff", "Fill in with the layers of tif",
                list);
        int finish = wizard.showModalDialog();

        if (finish != JWizard.FINISH) {
            System.exit(0);
        }
        File imageFile = (File) wizard.getConnectionParameters().get("image");
        String filePath = displayTiffLayers(imageFile);

        return filePath;
    }

    public String displayTiffLayers(File rasterFile) throws Exception {
        AbstractGridFormat format = GridFormatFinder.findFormat(rasterFile);
        // this is a bit hacky but does make more geotiffs work
        Hints hints = new Hints();
        if (format instanceof GeoTiffFormat) {
            hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
            hints.add(new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, DefaultGeographicCRS.WGS84)); // 加上坐标系不然读不出来！！！
        }
        reader = format.getReader(rasterFile, hints);

        // Initially display the raster in greyscale using the
        // data from the first image band
        Style rasterStyle = createGreyscaleStyle(1);

        // Set up a MapContent with the two layers
        final MapContent map = new MapContent();
        map.setTitle("Disaplay GeoTiff");

        Layer rasterLayer = new GridReaderLayer(reader, rasterStyle);
        map.addLayer(rasterLayer);

        // Create a JMapFrame with a menu to choose the display style for the
        frame = new JMapFrame(map);
        frame.setSize(800, 600);
        frame.enableStatusBar(true);
        // frame.enableTool(JMapFrame.Tool.ZOOM, JMapFrame.Tool.PAN,
        // JMapFrame.Tool.RESET);
        frame.enableToolBar(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu menu = new JMenu("Raster");
        menuBar.add(menu);

        menu.add(
                new SafeAction("Grayscale display") {
                    public void action(ActionEvent e) throws Throwable {
                        Style style = createGreyscaleStyle();
                        if (style != null) {
                            ((StyleLayer) map.layers().get(0)).setStyle(style);
                            frame.repaint();
                        }
                    }
                });

        menu.add(
                new SafeAction("RGB display") {
                    public void action(ActionEvent e) throws Throwable {
                        Style style = createRGBStyle();
                        if (style != null) {
                            ((StyleLayer) map.layers().get(0)).setStyle(style);
                            frame.repaint();
                        }
                    }
                });
        // Finally display the map frame.
        // When it is closed the app will exit.
        frame.setVisible(true);

        return rasterFile.getName(); // 返回文件名模块，和上一起添加
    }

    private Style createGreyscaleStyle() {
        GridCoverage2D cov = null;
        try {
            cov = reader.read(null);
        } catch (IOException giveUp) {
            throw new RuntimeException(giveUp);
        }
        int numBands = cov.getNumSampleDimensions();
        Integer[] bandNumbers = new Integer[numBands];
        for (int i = 0; i < numBands; i++) {
            bandNumbers[i] = i + 1;
        }
        Object selection = JOptionPane.showInputDialog(
                frame,
                "Band to use for greyscale display",
                "Select an image band",
                JOptionPane.QUESTION_MESSAGE,
                null,
                bandNumbers,
                1);
        if (selection != null) {
            int band = ((Number) selection).intValue();
            return createGreyscaleStyle(band);
        }
        return null;
    }

    private Style createGreyscaleStyle(int band) {
        ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NORMALIZE);
        SelectedChannelType sct = sf.createSelectedChannelType(String.valueOf(band), ce);

        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
        ChannelSelection sel = sf.channelSelection(sct);
        sym.setChannelSelection(sel);

        return SLD.wrapSymbolizers(sym);
    }

    private Style createRGBStyle() {
        GridCoverage2D cov = null;
        try {
            cov = reader.read(null);
        } catch (IOException giveUp) {
            throw new RuntimeException(giveUp);
        }
        // We need at least three bands to create an RGB style
        int numBands = cov.getNumSampleDimensions();
        if (numBands < 3) {
            return null;
        }
        // Get the names of the bands
        String[] sampleDimensionNames = new String[numBands];
        for (int i = 0; i < numBands; i++) {
            GridSampleDimension dim = cov.getSampleDimension(i);
            sampleDimensionNames[i] = dim.getDescription().toString();
        }
        final int RED = 0, GREEN = 1, BLUE = 2;
        int[] channelNum = { -1, -1, -1 };
        // We examine the band names looking for "red...", "green...", "blue...".
        // Note that the channel numbers we record are indexed from 1, not 0.
        for (int i = 0; i < numBands; i++) {
            String name = sampleDimensionNames[i].toLowerCase();
            if (name != null) {
                if (name.matches("red.*")) {
                    channelNum[RED] = i + 1;
                } else if (name.matches("green.*")) {
                    channelNum[GREEN] = i + 1;
                } else if (name.matches("blue.*")) {
                    channelNum[BLUE] = i + 1;
                }
            }
        }
        // If we didn't find named bands "red...", "green...", "blue..."
        // we fall back to using the first three bands in order
        if (channelNum[RED] < 0 || channelNum[GREEN] < 0 || channelNum[BLUE] < 0) {
            channelNum[RED] = 1;
            channelNum[GREEN] = 2;
            channelNum[BLUE] = 3;
        }
        // Now we create a RasterSymbolizer using the selected channels
        SelectedChannelType[] sct = new SelectedChannelType[cov.getNumSampleDimensions()];
        ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NORMALIZE);
        for (int i = 0; i < 3; i++) {
            sct[i] = sf.createSelectedChannelType(String.valueOf(channelNum[i]), ce);
        }
        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
        ChannelSelection sel = sf.channelSelection(sct[RED], sct[GREEN], sct[BLUE]);
        sym.setChannelSelection(sel);

        return SLD.wrapSymbolizers(sym);
    }

}
