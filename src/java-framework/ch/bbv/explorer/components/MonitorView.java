package ch.bbv.explorer.components;

import java.awt.*;
import java.text.DecimalFormat;

import javax.swing.JPanel;

class MonitorView extends JPanel {

  private static final int BORDER = 10;
  private static final int SPACE = 32;
  private double[] data;

  private String unit;

  private String info;

  private Double upperThreshold;

  private Double lowerThreshold;

  public Dimension getPreferredSize() {
    int perferredWidth = (data != null) ? (200 + data.length * 5) : 640;
    return new Dimension(perferredWidth, 240);
  }

  public void setData(double[] data) {
    this.data = data;
  }

  public void paint(Graphics g) {
    if (data == null || data.length < 2) {
      return;
    }
    g.clearRect(0, 0, getSize().width, getSize().height);
    double min = 0;
    double max = 0;
    for (int i = 0; i < data.length; i++) {
      min = Math.min(data[i], min);
      max = Math.max(data[i], max);
    }
    if (Math.abs(max - min) == 0) {
      max += 1;
    }

    FontMetrics metrics = g.getFontMetrics();
    int fontBaseLine = BORDER + metrics.getAscent() - metrics.getDescent();
    drawHeader(g, SPACE, BORDER, fontBaseLine);
    drawFooter(g, SPACE, BORDER, getSize().height - BORDER
        - metrics.getDescent());
    Rectangle rect = new Rectangle(BORDER, BORDER + metrics.getHeight(),
        getSize().width - 2 * BORDER, getSize().height - 3 * BORDER
            - metrics.getHeight() - metrics.getHeight());
    drawValues(g, new Rectangle(rect.x + SPACE, rect.y, rect.width - SPACE,
        rect.height), min, max);
    drawScale(g, new Rectangle(rect.x, rect.y, SPACE - 4, rect.height), min,
        max);
  }

  private void drawHeader(Graphics g, int space, int border, int baseline) {
    FontMetrics metrics = g.getFontMetrics();
    g.drawString(" [" + unit + "]", border + space, baseline);
    String stats = "Current = " + formatValue(data[data.length - 1]) + " "
        + unit;
    g.drawString(stats, getSize().width - metrics.stringWidth(stats) - border
        - 16, baseline);
  }

  private void drawFooter(Graphics g, int space, int border, int baseline) {
    g.drawString(info, border + space, baseline);
  }

  private String formatValue(double val) {
    String pattern = "#0.##";
    DecimalFormat format = new DecimalFormat(pattern);
    return format.format(val);
  }

  private void drawValues(Graphics g, Rectangle rect, double min, double max) {
    if (rect.width < 1 || rect.height < 1)
      return;
    Graphics2D g2d = (Graphics2D) g;
    double[] transformParams = getTransformParams(data, rect);
    double a = transformParams[0];
    double b = transformParams[1];
    Point[] points = transform(data, a, b, rect.x, rect.x + rect.width);
    g.setColor(Color.GRAY);
    for (int i = 0; i < points.length; i++) {
      g.drawLine(points[i].x, rect.y, points[i].x, rect.y + rect.height);
    }
    int gridcell = points[1].x - points[0].x;
    if (gridcell < 3)
      gridcell = 3;
    int hlines = rect.height / gridcell + 1;
    if (hlines > 1) {
      for (int i = 0; i < hlines; i++) {
        int y = rect.y + rect.height * i / (hlines - 1);
        g.drawLine(rect.x, y, rect.x + rect.width, y);
      }
    }
    g.drawRect(rect.x, rect.y, rect.width, rect.height);
    // draw zero line
    g2d.setStroke(new BasicStroke(2));
    g.drawLine(rect.x, (int) b, rect.x + rect.width, (int) b);
    g.setColor(Color.GREEN);
    if (upperThreshold != null && upperThreshold.doubleValue() >= min
        && upperThreshold.doubleValue() <= max) {
      int y = (int) (a * upperThreshold.doubleValue() + b);
      g.drawLine(rect.x, y, rect.x + rect.width, y);
    }

    if (lowerThreshold != null && lowerThreshold.doubleValue() >= min
        && lowerThreshold.doubleValue() <= max) {
      int y = (int) (a * lowerThreshold.doubleValue() + b);
      g.drawLine(rect.x, y, rect.x + rect.width, y);
    }

    points = transform(expandValues(data, 4), a, b, rect.x, rect.x + rect.width);

    g2d.setStroke(new BasicStroke((float) getSize().height / 120,
        BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
    for (int i = 1; i < points.length; i++) {
      g.drawLine(points[i - 1].x, points[i - 1].y, points[i].x, points[i].y);
    }
  }

  private void drawScale(Graphics g, Rectangle rect, double min, double max) {
    FontMetrics metrics = g.getFontMetrics();
    double[] transformParams = getTransformParams(data, rect);
    double a = transformParams[0];
    double b = transformParams[1];

    g.drawString(formatValue(max), rect.x + rect.width
        - metrics.stringWidth(formatValue(max)), (int) (a * max + b)
        + metrics.getAscent() / 2);
    g.drawString(formatValue(min), rect.x + rect.width
        - metrics.stringWidth(formatValue(min)), (int) (a * min + b));
    if (max != 0 && min != 0)
      g.drawString("0", rect.x + rect.width
          - metrics.stringWidth(formatValue(0)), (int) b);
  }

  private double[] getTransformParams(double[] data, Rectangle rect) {
    double min = 0;
    double max = 0;
    for (int i = 0; i < data.length; i++) {
      min = Math.min(data[i], min);
      max = Math.max(data[i], max);
    }
    if (Math.abs(max - min) == 0) {
      max += 1;
    }
    double a = rect.height / (min - max);
    double b = rect.y - a * max;

    return new double[] { a, b };
  }

  private Point[] transform(double[] data, double a, double b, int minX,
      int maxX) {
    Point[] points = new Point[data.length];
    for (int i = 0; i < data.length; i++) {
      int x = minX + (maxX - minX) * i / (data.length - 1);
      int y = (int) (a * data[i] + b);
      points[i] = new Point(x, y);
    }
    return points;
  }

  public void setLowerThreshold(Double lowerThreshold) {
    this.lowerThreshold = lowerThreshold;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public void setUpperThreshold(Double upperThreshold) {
    this.upperThreshold = upperThreshold;
  }

  private double[] expandValues(double[] values, int factor) {
    double[] interpolated = new double[(values.length - 1) * factor];
    for (int i = 0; i < values.length - 1; i++) {
      double last = values[i];
      double current = values[i + 1];
      for (int j = 0; j < factor; j++) {
        double rel = (double) j / factor;
        double x = (1 - Math.cos(rel * Math.PI)) * 0.5;
        interpolated[i * factor + j] = last * (1 - x) + current * x;
      }
    }
    return interpolated;
  }

  public void setInfo(String string) {
    info = string;
  }
}