package com.water.chart.widget;

/**
 * Created by lixinke on 2017/4/8.
 */

public interface ChartAdapter {

  int getItemCount();

  float getPoint(int position);

  String getChartName(int position);

  float getMaxPoint();
}
