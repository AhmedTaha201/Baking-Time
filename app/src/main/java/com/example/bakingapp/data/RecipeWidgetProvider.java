package com.example.bakingapp.data;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.example.bakingapp.MainActivity;
import com.example.bakingapp.R;

/**
 * Created by Taha on 6/20/2018.
 */

public class RecipeWidgetProvider extends AppWidgetProvider {

    public static void updateWidgets(Context context, AppWidgetManager manager, int[] appWidgetIds) {
        for (int id : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            //Set the recipe name
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String name = preferences.getString(MainActivity.SP_KEY_NAME, context.getString(R.string.not_available));
            views.setTextViewText(R.id.widget_recipe_name, name);

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widget_recipe_name, pendingIntent);

            //Set The recipe ingredients list
            Intent listServiceIntent = new Intent(context, WidgetService.class);
            views.setRemoteAdapter(R.id.widget_recipe_ingredient_list, listServiceIntent);
            views.setEmptyView(R.id.widget_recipe_ingredient_list, R.id.widget_list_empty_view);

            manager.updateAppWidget(id, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        updateWidgets(context, appWidgetManager, appWidgetIds);
    }
}
