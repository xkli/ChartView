package com.water.chart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.water.chart.widget.ChartAdapter;
import com.water.chart.widget.ChartModel;
import com.water.chart.widget.ChartView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final ChartView chartView = (ChartView) findViewById(R.id.chart);
    final List<ChartModel> chartModels = new ArrayList<>();

    Random random = new Random();
    for (int i = 0; i < 5; i++) {
      ChartModel model = new ChartModel();
      model.point = random.nextInt(100);
      model.name = "chart:"+i;
      chartModels.add(model);
    }

    chartView.setChartAdapter(new ChartAdapter() {
                                @Override
                                public int getItemCount() {
                                  return chartModels.size();
                                }

                                @Override
                                public float getPoint(int position) {
                                  return chartModels.get(position).point;
                                }

                                @Override
                                public String getChartName(int position) {
                                  return chartModels.get(position).name;
                                }

                                @Override
                                public float getMaxPoint() {
                                  return 100;
                                }
                              }

                             );
  }
}
