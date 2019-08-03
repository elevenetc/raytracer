package com.elevenetc.raytracer.debug;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ContentFrameLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elevenetc.raytracer.utils.ViewUtils;

import java.util.LinkedList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.elevenetc.raytracer.utils.ViewUtils.dpToPx;

public class DebugMenu {

    public static class Builder {

        private String title;
        private List<MenuItem> items = new LinkedList<>();
        private TextView btnClose;
        private boolean opened = false;
        private MenuContainer itemsContainer;

        public Builder setTitle(String title) {

            this.title = title;
            return this;
        }

        public Builder addDivider() {
            items.add(new DividerItem());
            return this;
        }

        public Builder addDivider(String title) {
            items.add(new DividerItem(title));
            return this;
        }

        public Builder addCheckBox(String text, boolean checked, CheckBoxItem.Listener listener) {
            items.add(new CheckBoxItem(text, checked, listener));
            return this;
        }

        public Builder addView(MenuItem item) {
            items.add(item);
            return this;
        }

        public void build(AppCompatActivity activity) {

            itemsContainer = new MenuContainer(activity);
            ContentFrameLayout contentView = activity.findViewById(android.R.id.content);

            addCloseButton(activity, itemsContainer);

            if (title != null) {
                TextView textTitle = new TextView(activity);
                textTitle.setText(title);
                itemsContainer.addView(textTitle);
            }

            for (MenuItem item : items)
                itemsContainer.addView(item.buildView(activity), new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

            FrameLayout.LayoutParams lp = new ContentFrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            lp.gravity = Gravity.BOTTOM;
            contentView.addView(itemsContainer, lp);


            ViewUtils.getMeasuredSize(itemsContainer, (contW, contH) -> {
                int btnCloseH = btnClose.getHeight();
                itemsContainer.setTranslationY(contH - btnCloseH);
            });
        }

        private void addCloseButton(AppCompatActivity activity, MenuContainer itemsContainer) {
            btnClose = new TextView(activity);
            btnClose.setText("Debug menu");

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(WRAP_CONTENT, (int) dpToPx(25));
            lp.gravity = Gravity.RIGHT;

            itemsContainer.addView(btnClose, lp);

            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int btnCloseHeight = btnClose.getHeight();
                    int containerHeight = itemsContainer.getHeight();

                    if (opened) {
                        opened = false;
                        ObjectAnimator.ofFloat(itemsContainer, "translationY", 0).start();
                    } else {
                        opened = true;
                        ObjectAnimator.ofFloat(itemsContainer, "translationY", containerHeight - btnCloseHeight).start();
                    }
                }
            });
        }
    }

    public static void add(AppCompatActivity activity) {
        ContentFrameLayout view = activity.findViewById(android.R.id.content);
        view.addView(new MenuContainer(activity), new ContentFrameLayout.LayoutParams(200, 200));
    }

    static class MenuContainer extends LinearLayout {

        public MenuContainer(@NonNull Context context) {
            super(context);
            init();
        }

        private void init() {
            setOrientation(VERTICAL);
            setBackgroundColor(Color.WHITE);
        }
    }
}
