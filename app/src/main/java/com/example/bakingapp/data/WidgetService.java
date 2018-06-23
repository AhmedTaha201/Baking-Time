package com.example.bakingapp.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bakingapp.MainActivity;
import com.example.bakingapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Taha on 6/20/2018.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteListViewsFactory(getApplicationContext());
    }

    public class RemoteListViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        Context mContext;
        Set<String> mRecipeIngredients;

        public RemoteListViewsFactory(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            mRecipeIngredients = preferences.getStringSet(MainActivity.SP_KEY_INGREDIENTS, null);
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return mRecipeIngredients != null ? mRecipeIngredients.size() : 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {


            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
            List<String> ingredientList = new ArrayList<>(mRecipeIngredients);
            views.setTextViewText(R.id.widget_list_text, ingredientList.get(position));
            Log.e(WidgetService.class.getSimpleName(), "Position ==> " + position);
            return views;
        }


        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }

}
