package com;
import java.io.File;
import java.awt.*;
import javax.swing.JPanel;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.labels.StandardCategorySeriesLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import java.util.ArrayList;
import java.awt.event.WindowEvent;
import javax.swing.JScrollPane;
import org.jfree.chart.ChartUtilities;
public class Chart extends ApplicationFrame{
	static String title;
public Chart(String paramString){
	super(paramString);
	JPanel localJPanel = createDemoPanel();
    localJPanel.setPreferredSize(new Dimension(800, 370));
    JScrollPane jsp = new JScrollPane(localJPanel);
	setContentPane(localJPanel);
}

private static CategoryDataset createDataset() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    File[] roots = File.listRoots();
    long totalSpace = 0;
    long freeSpace = 0;
    long usedSpace = 0;
    for (File root : roots) {
        totalSpace += root.getTotalSpace();
        freeSpace += root.getFreeSpace();
        usedSpace += root.getTotalSpace() - root.getFreeSpace();
    }
    double usedPercentage = ((double) usedSpace / totalSpace) * 100;
    double freePercentage = ((double) freeSpace / totalSpace) * 100;
    dataset.addValue(usedPercentage ,"Used space", "Server Storage" );
    dataset.addValue(freePercentage ,"Free space", "Server Storage" );
    return dataset;
}


/*private static CategoryDataset createDataset() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    File fileC = new File("C:");
    File fileD = new File("D:");
    long totalSpace = fileC.getTotalSpace() + fileD.getTotalSpace();
    long freeSpace = fileC.getFreeSpace() + fileD.getFreeSpace();
    long usedSpace = totalSpace - freeSpace;
    double usedPercentage = ((double) usedSpace / totalSpace) * 100;
    double freePercentage = ((double) freeSpace / totalSpace) * 100;
    dataset.addValue(usedPercentage, "Used space", "Combined C and D drives");
    dataset.addValue(freePercentage, "Free space", "Combined C and D drives");
    return dataset;
}*/

/*private static CategoryDataset createDataset() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    File[] roots = File.listRoots();
    for (File root : roots) {
        String drive = root.getAbsolutePath();
        long totalSpace = root.getTotalSpace();
        long freeSpace = root.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;
        double usedPercentage = ((double) usedSpace / totalSpace) * 100;
        double freePercentage = ((double) freeSpace / totalSpace) * 100;
        dataset.addValue(usedPercentage, "Used space", drive);
        dataset.addValue(freePercentage, "Free space", drive);
    }
    return dataset;
}*/

/*private static CategoryDataset createDataset() {
        DefaultCategoryDataset localDefaultCategoryDataset = new DefaultCategoryDataset();
        File fileC = new File("C:");
        long totalSpaceC = fileC.getTotalSpace();
        long freeSpaceC = fileC.getFreeSpace();
        long usedSpaceC = totalSpaceC - freeSpaceC;
        double usedPercentageC = ((double) usedSpaceC / totalSpaceC) * 100;
        double freePercentageC = ((double) freeSpaceC / totalSpaceC) * 100;
        localDefaultCategoryDataset.addValue(usedPercentageC, "Used space", "Local Disk C");
        localDefaultCategoryDataset.addValue(freePercentageC, "Free space", "Local Disk C");
        
        File fileD = new File("D:");
        long totalSpaceD = fileD.getTotalSpace();
        long freeSpaceD = fileD.getFreeSpace();
        long usedSpaceD = totalSpaceD - freeSpaceD;
        double usedPercentageD = ((double) usedSpaceD / totalSpaceD) * 100;
        double freePercentageD = ((double) freeSpaceD / totalSpaceD) * 100;
        localDefaultCategoryDataset.addValue(usedPercentageD, "Used space", "Local Disk D");
        localDefaultCategoryDataset.addValue(freePercentageD, "Free space", "Local Disk D");
        
        return localDefaultCategoryDataset;
    }*/
public void windowClosing(WindowEvent we){
	this.setVisible(false);
}
private static JFreeChart createChart(CategoryDataset paramCategoryDataset) {
        JFreeChart localJFreeChart = ChartFactory.createBarChart(title, "Disk space", "%", paramCategoryDataset, PlotOrientation.VERTICAL, true, true, false);
        CategoryPlot localCategoryPlot = (CategoryPlot) localJFreeChart.getPlot();
        localCategoryPlot.setDomainGridlinesVisible(true);
        localCategoryPlot.setRangeCrosshairVisible(true);
        localCategoryPlot.setRangeCrosshairPaint(Color.blue);
        NumberAxis localNumberAxis = (NumberAxis) localCategoryPlot.getRangeAxis();
        localNumberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer localBarRenderer = (BarRenderer) localCategoryPlot.getRenderer();
        localBarRenderer.setDrawBarOutline(false);
        GradientPaint localGradientPaint1 = new GradientPaint(0.0F, 0.0F, Color.blue, 0.0F, 0.0F, new Color(0, 0, 64));
        GradientPaint localGradientPaint2 = new GradientPaint(0.0F, 0.0F, Color.green, 0.0F, 0.0F, new Color(0, 64, 0));
        localBarRenderer.setSeriesPaint(0, localGradientPaint1);
        localBarRenderer.setSeriesPaint(1, localGradientPaint2);
        localBarRenderer.setLegendItemToolTipGenerator(new StandardCategorySeriesLabelGenerator("Tooltip: {0}"));
        CategoryAxis localCategoryAxis = localCategoryPlot.getDomainAxis();
        localCategoryAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(0.5235987755982988D));
        return localJFreeChart;
    }
public static JPanel createDemoPanel(){
	JFreeChart localJFreeChart = createChart(createDataset());
	return new ChartPanel(localJFreeChart);
}
}






/*package com;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategorySeriesLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import java.util.ArrayList;
import java.awt.event.WindowEvent;
import javax.swing.JScrollPane;
import org.jfree.chart.ChartUtilities;
public class Chart extends ApplicationFrame{
	static String title;
public Chart(String paramString){
	super(paramString);
	JPanel localJPanel = createDemoPanel();
    localJPanel.setPreferredSize(new Dimension(800, 370));
    JScrollPane jsp = new JScrollPane(localJPanel);
	setContentPane(localJPanel);
}
private static CategoryDataset createDataset(){
	DefaultCategoryDataset localDefaultCategoryDataset = new DefaultCategoryDataset();
	localDefaultCategoryDataset.addValue(NetworkMonitor.normal,"Total Normal Packets","Total Normal Packets");
	localDefaultCategoryDataset.addValue(NetworkMonitor.attack,"Total Attack Packets","Total Attack Packets");
	return localDefaultCategoryDataset;
}
public void windowClosing(WindowEvent we){
	this.setVisible(false);
}
private static JFreeChart createChart(CategoryDataset paramCategoryDataset){
	JFreeChart localJFreeChart = ChartFactory.createBarChart(title,"Packets Type", "Count", paramCategoryDataset, PlotOrientation.VERTICAL, true, true, false);
    CategoryPlot localCategoryPlot = (CategoryPlot)localJFreeChart.getPlot();
    localCategoryPlot.setDomainGridlinesVisible(true);
    localCategoryPlot.setRangeCrosshairVisible(true);
    localCategoryPlot.setRangeCrosshairPaint(Color.blue);
    NumberAxis localNumberAxis = (NumberAxis)localCategoryPlot.getRangeAxis();
    localNumberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    BarRenderer localBarRenderer = (BarRenderer)localCategoryPlot.getRenderer();
    localBarRenderer.setDrawBarOutline(false);
    GradientPaint localGradientPaint1 = new GradientPaint(0.0F, 0.0F, Color.blue, 0.0F, 0.0F, new Color(0, 0, 64));
    GradientPaint localGradientPaint2 = new GradientPaint(0.0F, 0.0F, Color.green, 0.0F, 0.0F, new Color(0, 64, 0));
    GradientPaint localGradientPaint3 = new GradientPaint(0.0F, 0.0F, Color.red, 0.0F, 0.0F, new Color(64, 0, 0));
    localBarRenderer.setSeriesPaint(0, localGradientPaint1);
    localBarRenderer.setSeriesPaint(1, localGradientPaint2);
    localBarRenderer.setSeriesPaint(2, localGradientPaint3);
    localBarRenderer.setLegendItemToolTipGenerator(new StandardCategorySeriesLabelGenerator("Tooltip: {0}"));
    CategoryAxis localCategoryAxis = localCategoryPlot.getDomainAxis();
    localCategoryAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(0.5235987755982988D));
    return localJFreeChart;
}
public static JPanel createDemoPanel(){
	JFreeChart localJFreeChart = createChart(createDataset());
	return new ChartPanel(localJFreeChart);
}
}*/