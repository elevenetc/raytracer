package com.elevenetc.raytracer.debug;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elevenetc.raytracer.utils.ViewUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CoresViewer extends MenuItem {

    private Binder binder;

    public void setBinder(Binder binder) {
        this.binder = binder;
    }

    @Override
    public View buildView(Context context) {
        int coresAmount = binder.coresAmount();
        CoresView coresView = new CoresView(context);
        coresView.initViews(coresAmount);
        binder.view = coresView;
        return coresView;
    }

    public static class Binder {

        private int coresAmount;
        private CoresView view;
        private CoreInfo[] coreInfos;

        public Binder(int coresAmount) {
            this.coresAmount = coresAmount;
            coreInfos = new CoreInfo[coresAmount];
        }

        public int coresAmount() {
            return coresAmount;
        }

        public void updateCore(int i, CoreInfo info) {
            coreInfos[i] = info;
            view.updateCore(i, info);
        }

        public static class CoreInfo {

            public State state;
            public long workStart;
            public long workEnd;

            public CoreInfo(State state, long workStart, long workEnd) {
                this.state = state;
                this.workStart = workStart;
                this.workEnd = workEnd;
            }

            public enum State {
                WORKING, IDLE
            }
        }
    }

    private static class CoresView extends LinearLayout {

        private CoreView[] views;

        public CoresView(Context context) {
            super(context);
            setOrientation(LinearLayout.HORIZONTAL);
        }

        public void updateCore(int i, Binder.CoreInfo info) {

            CoreView view = views[i];
            view.setState(info.state);

            if (info.state == Binder.CoreInfo.State.IDLE) {
                view.setStartTime(info.workStart);
                view.setEndTime(info.workEnd);
                view.setTotal(info.workEnd - info.workStart);
            } else {
                view.setStartTime(info.workStart);
                view.setEndTime(-1);
                view.setTotal(-1);
            }
        }

        public void initViews(int cores) {

            views = new CoreView[cores];

            for (int i = 0; i < cores; i++) {
                CoreView child = new CoreView(getContext());
                views[i] = child;
                LayoutParams lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
                lp.weight = 1;
                addView(child, lp);
            }
        }

    }

    private static class CoreView extends LinearLayout {

        private View stateView;
        private TextView textStart;
        private TextView textEnd;
        private TextView textTotalTime;
        private SimpleDateFormat format = new SimpleDateFormat("mm:ss.SSS");

        public CoreView(Context context) {
            super(context);

            setOrientation(LinearLayout.VERTICAL);

            stateView = new View(context);
            stateView.setLayoutParams(new LinearLayout.LayoutParams((int) ViewUtils.dpToPx(10), (int) ViewUtils.dpToPx(10)));

            textStart = new TextView(context);
            textEnd = new TextView(context);
            textTotalTime = new TextView(context);

            addView(stateView);
            addView(textStart);
            addView(textEnd);
            addView(textTotalTime);
        }

        public void setStartTime(long start) {

            if (start == -1) {
                textStart.setText("start: -");
            } else {
                textStart.setText("start: " + format.format(new Date(start)));
            }


        }

        public void setEndTime(long end) {

            if (end == -1) {
                textEnd.setText("end: -");
            } else {
                textEnd.setText("end: " + format.format(new Date(end)));
            }


        }

        public void setTotal(long start) {
            textTotalTime.setText("total: " + start + "ms");
        }

        public void setState(Binder.CoreInfo.State state) {
            if (state == Binder.CoreInfo.State.IDLE) {
                stateView.setBackgroundColor(Color.GREEN);
            } else {
                stateView.setBackgroundColor(Color.RED);
            }
        }
    }
}
