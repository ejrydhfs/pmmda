package ch.bbv.explorer.components;

import java.awt.Toolkit;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.*;

/**
 * The RealtimeMemoryMonitor is a frame which graphically shows and monitors the
 * amout of memory currently used by the active Java virtual machine.
 * @author marcelbaumann
 * @version $Revision$
 */
public class MemoryMonitor extends JFrame {
  private final static int VALUES = 250;

  private final static int DEFAULT_PERIOD_MS = 1000;

  private int periodMs = DEFAULT_PERIOD_MS;

  private MonitorThread thread;

  private MonitorView view;

  private List data;

  private double min = Double.MAX_VALUE;

  private double max = Double.MIN_VALUE;

  private double average;

  private JPopupMenu popup;

  private NumberFormat numFormat = new DecimalFormat("#.#");

  private static MemoryMonitor monitor;

  public static void create(JFrame frame) {
    KeyStroke hiddenMemoryMonitorKeyStroke = KeyStroke
        .getKeyStroke("control alt BACK_SPACE");
    ActionListener hiddenMemoryMonitorListener = new MemoryManagerActionListener();
    frame.getRootPane().registerKeyboardAction(hiddenMemoryMonitorListener,
        hiddenMemoryMonitorKeyStroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
  }

  /**
   * Returns the singleton instance of the memory monitor.
   * @return the singleton instance of the memory monitor.
   */
  static MemoryMonitor getInstance() {
    return monitor;
  }

  /**
   * Default constructor of the class.
   */
  public MemoryMonitor() {
    setTitle("Memory Monitor");
    data = new ArrayList();
    view = new MonitorView();
    view.setUnit("MB");
    setContentPane(view);
    thread = new MonitorThread();
    addWindowListener(new MemoryManagerWindowAdapter());
    pack();
    setLocation(40, Toolkit.getDefaultToolkit().getScreenSize().height
        - getSize().height - 60);
    addMouseListener(new MemoryMouseAdapter());
    monitor = this;
    show();
  }

  private static class GarbageCollectAction extends AbstractAction {
    public GarbageCollectAction() {
      super("Garbage Collect");
    }

    public void actionPerformed(ActionEvent evt) {
      Runtime.getRuntime().gc();
    }
  }

  private static class RunFinalizationAction extends AbstractAction {
    public RunFinalizationAction() {
      super("Run Finalization");
    }

    public void actionPerformed(ActionEvent evt) {
      Runtime.getRuntime().runFinalization();
    }
  }

  private class SampleSpeedAction extends AbstractAction {
    double samplesPerSecond;

    public SampleSpeedAction(double samplesPerSecond) {
      super(samplesPerSecond + " Samples/sec");
      this.samplesPerSecond = samplesPerSecond;
    }

    public void actionPerformed(ActionEvent evt) {
      periodMs = (int) (1000d / samplesPerSecond);
    }
  }

  private class MonitorThread extends Thread {
    private boolean running;

    public MonitorThread() {
      super("Realtime Memory Monitor");
      setPriority(MIN_PRIORITY);
      setDaemon(true);
      start();
    }

    public void run() {
      running = true;
      while (running) {
        updateMonitor();
        try {
          sleep(periodMs);
        } catch (Exception ignored) {
        }
      }
    }

    public void terminate() {
      running = false;
    }
  }

  public void updateMonitor() {
    long memoryUsed = Runtime.getRuntime().totalMemory()
        - Runtime.getRuntime().freeMemory();
    if (data.size() == VALUES) {
      data.remove(0);
    }
    double sample = (double) memoryUsed / (1024 * 1024);
    min = Math.min(min, sample);
    max = Math.max(max, sample);
    data.add(new Double(sample));
    view.setData(dataAsArray());
    String timeFrame = new SimpleDateFormat("mm:ss").format(new Date(data
        .size()
        * periodMs));
    double hz = 1000d / periodMs;
    StringBuffer statistics = new StringBuffer();
    statistics.append("min=").append(numFormat.format(min)).append(", max=")
        .append(numFormat.format(max)).append(", average=").append(
            numFormat.format(average));
    StringBuffer info = new StringBuffer();
    info.append("Timeframe= ").append(timeFrame).append(", Frequency: ")
        .append(hz).append(" Hz, ").append(data.size()).append(" Samples (")
        .append(statistics.toString()).append(")");
    view.setInfo(info.toString());

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        view.repaint(1000);
      }
    });
  }

  private double[] dataAsArray() {
    double sum = 0;
    double[] values = new double[data.size()];
    Iterator iter = data.iterator();
    for (int i = 0; i < values.length; i++) {
      values[i] = ((Double) iter.next()).doubleValue();
      sum += values[i];
    }
    average = sum / values.length;
    return values;
  }

  private static class MemoryManagerWindowAdapter extends WindowAdapter {
    public void windowClosing(WindowEvent evt) {
      getInstance().setVisible(false);
    }
  }

  private static class MemoryManagerActionListener implements ActionListener {
    public void actionPerformed(ActionEvent evt) {
      if (getInstance() == null) {
        new MemoryMonitor();
      }
      getInstance().setVisible(true);
    }
  }

  private class MemoryMouseAdapter extends MouseAdapter {
    private JPopupMenu popup;

    public void mousePressed(MouseEvent evt) {
      if (SwingUtilities.isRightMouseButton(evt))
        showPopup(evt.getX(), evt.getY());
    }

    private void showPopup(int x, int y) {
      if (popup == null) {
        popup = new JPopupMenu();

        popup.add(new GarbageCollectAction());
        popup.add(new RunFinalizationAction());
        popup.addSeparator();

        JMenu speed = new JMenu("Update Speed");
        popup.add(speed);
        speed.add(new SampleSpeedAction(1));
        speed.add(new SampleSpeedAction(5));
        speed.add(new SampleSpeedAction(10));
        speed.add(new SampleSpeedAction(20));
      }
      popup.show(getInstance(), x, y);
    }
  }
}