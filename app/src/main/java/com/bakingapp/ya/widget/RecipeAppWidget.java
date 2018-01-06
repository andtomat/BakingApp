package com.bakingapp.ya.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.bakingapp.ya.R;
import com.bakingapp.ya.data.model.Ingredient;
import com.bakingapp.ya.data.model.Recipe;
import com.bakingapp.ya.recipedetail.RecipeDetailActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeAppWidget extends AppWidgetProvider {

    @SuppressWarnings("deprecation")
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                Recipe recipe, int appWidgetId) {


        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra("recipe", recipe);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_app_widget);
        views.removeAllViews(R.id.ll_recipe_widget_ingredient_list);
        views.setTextViewText(R.id.recipe_widget_title, recipe.getName());
        views.setOnClickPendingIntent(R.id.recipe_widget_holder, pendingIntent);

        int i = 0;

        views.setViewVisibility(R.id.recipe_widget_read, View.VISIBLE);

        for(Ingredient ingredient : recipe.getIngredients()) {
            if(i<6){
                RemoteViews rvIngredient = new RemoteViews(context.getPackageName(),
                        R.layout.recipe_app_widget_list_item);

                NumberFormat format = new DecimalFormat("0.#");
                String text = format.format(ingredient.getQuantity()) + " " + ingredient.getMeasure() + "  <b>" + ingredient.getIngredient() + "</b>";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    rvIngredient.setTextViewText(R.id.tv_recipe_widget_ingredient_item, Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
                } else {
                    rvIngredient.setTextViewText(R.id.tv_recipe_widget_ingredient_item, Html.fromHtml(text));
                }
                views.addView(R.id.ll_recipe_widget_ingredient_list, rvIngredient);
            }
            i++;
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           Recipe recipe, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipe, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

