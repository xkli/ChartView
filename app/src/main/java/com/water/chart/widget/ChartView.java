package com.water.chart.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.water.chart.R;

/**
 * Created by lixinke on 2017/4/8.
 */

public class ChartView extends View {

  private static final int DEFAULT_MIN_WIDTH = 300;
  private static final int DEFAULT_MIN_HEIGHT = 300;

  private static final int OFFSET = 30;
  private static final double CHART_HEIGHT_PERCENT = 0.75;
  private static final double CHART_BOTTOM_PERCENT = 0.15;

  private ChartAdapter mChartAdapter;
  private Paint mPaint;
  private int mChartTextSize = 30;
  private int mChartTextColor = Color.GRAY;
  private int mChartCircleColor;
  private int mChartCircleDiameter;
  private int mChartLineColor;

  public ChartView(Context context) {
    this(context, null);
  }

  public ChartView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    TypedArray chartTypeArray = context.obtainStyledAttributes(attrs, R.styleable.ChartView);
    mChartTextSize = chartTypeArray.getInteger(R.styleable.ChartView_chartTextSize, 30);
    mChartTextColor = chartTypeArray.getColor(R.styleable.ChartView_chartTextColor, Color.GRAY);
    mChartCircleColor = chartTypeArray.getColor(R.styleable.ChartView_chartCircleColor, Color.GRAY);
    mChartCircleDiameter = chartTypeArray.getInt(R.styleable.ChartView_chartCircleDiameter, 5);
    mChartLineColor = chartTypeArray.getColor(R.styleable.ChartView_chartLineColor, Color.GRAY);
    chartTypeArray.recycle();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    int width = 0;
    int widthModel = MeasureSpec.getMode(widthMeasureSpec);
    if (widthModel == MeasureSpec.AT_MOST || widthModel == MeasureSpec.EXACTLY) {
      width = MeasureSpec.getSize(widthMeasureSpec);
    } else if (widthModel == MeasureSpec.UNSPECIFIED) {
      width = getDefaultSize(DEFAULT_MIN_WIDTH, widthMeasureSpec);
    }

    int height = 0;
    int heightModel = MeasureSpec.getMode(heightMeasureSpec);
    if (heightModel == MeasureSpec.AT_MOST || heightModel == MeasureSpec.EXACTLY) {
      height = MeasureSpec.getSize(heightMeasureSpec);
    } else if (heightModel == MeasureSpec.UNSPECIFIED) {
      height = getDefaultSize(DEFAULT_MIN_HEIGHT, heightMeasureSpec);
    }

    setMeasuredDimension(width, height);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (mChartAdapter == null || mChartAdapter.getItemCount() == 0) {
      return;
    }
    float chartWidthTemp = getMeasuredWidth() / mChartAdapter.getItemCount();
    float chartWidth = (float) (chartWidthTemp * CHART_HEIGHT_PERCENT);
    float chartHeight = (float) (getMeasuredHeight() * CHART_HEIGHT_PERCENT);

    float obliqueStartX = 0;
    float obliqueStartY = 0;

    for (int i = 0, count = mChartAdapter.getItemCount(); i < count; i++) {
      float left = i * chartWidthTemp + 30;
      float top = (float) ((getMeasuredHeight() - chartHeight) - (getMeasuredHeight() * 0.15));
      float right = i * chartWidthTemp + chartWidth;
      float bottom = (float) (getMeasuredHeight() - (getMeasuredHeight() * 0.15));

      drawChartRect(canvas, left, top, right, bottom);

      String chartName = mChartAdapter.getChartName(i);
      drawChartName(canvas, chartName, left, right, bottom);

      float circleX = (left + right) / 2;
      float circleY = bottom - (mChartAdapter.getPoint(i) / mChartAdapter.getMaxPoint()) * chartHeight;
      mPaint.setStyle(Paint.Style.FILL);
      mPaint.setColor(mChartCircleColor);
      canvas.drawCircle(circleX, circleY, mChartCircleDiameter, mPaint);

      if (i == 0) {
        obliqueStartX = circleX;
        obliqueStartY = circleY;
      } else {
        mPaint.setColor(mChartLineColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mChartCircleDiameter);
        canvas.drawLine(obliqueStartX, obliqueStartY, circleX, circleY, mPaint);
        obliqueStartX = circleX;
        obliqueStartY = circleY;
      }
    }

    mPaint.setColor(Color.GRAY);
    float chartBottom = (float) (getMeasuredHeight() - (getMeasuredHeight() * CHART_BOTTOM_PERCENT));
    float endX = (float) (getMeasuredWidth() - chartWidthTemp * (1 - CHART_HEIGHT_PERCENT));
    canvas.drawLine(OFFSET, chartBottom, endX, chartBottom, mPaint);
  }

  private void drawChartName(Canvas canvas, String chartName, float left, float right, float bottom) {
    mPaint.setTextSize(mChartTextSize);
    mPaint.setStrokeWidth(1);
    mPaint.setTextAlign(Paint.Align.CENTER);
    mPaint.setColor(mChartTextColor);
    canvas.drawText(chartName, (left + right) / 2, bottom + 30, mPaint);
  }

  private void drawChartRect(Canvas canvas, float left, float top, float right, float bottom) {
    mPaint.setColor(Color.GRAY);
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setStrokeWidth(mChartCircleDiameter);
    canvas.drawRect(left, top, right, bottom, mPaint);
  }

  public ChartAdapter getChartAdapter() {
    return mChartAdapter;
  }

  public void setChartAdapter(ChartAdapter chartAdapter) {
    mChartAdapter = chartAdapter;
  }
}
