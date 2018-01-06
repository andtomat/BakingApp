package com.bakingapp.ya.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.bakingapp.ya.data.model.Recipe;

public class RecipeAppWidgetService extends IntentService {
    public static final String RECIPE_WIDGET_ACTION_UPDATE =
            "com.bakingapp.ya.action.update";

    public RecipeAppWidgetService() {
        super("RecipeWidgetService");
    }

    public static void startActionUpdateRecipeWidgets(Context context, Recipe recipe) {
        Intent intent = new Intent(context, RecipeAppWidgetService.class);
        intent.setAction(RECIPE_WIDGET_ACTION_UPDATE);
        intent.putExtra("recipe", recipe);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (RECIPE_WIDGET_ACTION_UPDATE.equals(action) &&
                    intent.getParcelableExtra("recipe") != null) {
                handleActionUpdateWidgets(intent.getParcelableExtra("recipe"));
            }
        }
    }

    private void handleActionUpdateWidgets(Recipe recipe) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeAppWidget.class));
        RecipeAppWidget.updateRecipeWidgets(this, appWidgetManager, recipe, appWidgetIds);
    }
}