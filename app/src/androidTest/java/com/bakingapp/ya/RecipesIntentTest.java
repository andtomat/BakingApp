package com.bakingapp.ya;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.bakingapp.ya.recipedetail.RecipeDetailActivity;
import com.bakingapp.ya.recipes.RecipesActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.base.Preconditions.checkArgument;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RecipesIntentTest {

    private final static String RECIPE_NAME_EXAMPLE = "Brownies";

    @Rule
    public IntentsTestRule<RecipesActivity> mIntentTestRule = new IntentsTestRule<>(RecipesActivity.class);


    @Before
    public void stubAllExternalIntents() {
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Before
    public void registerIdlingResource() {
         Espresso.registerIdlingResources(
                mIntentTestRule.getActivity().getCountingIdlingResource());
    }


    private Matcher<View> withItemText(final String itemText) {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty");
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View item) {
                return allOf(
                        isDescendantOfA(isAssignableFrom(RecyclerView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is isDescendantOfA RV with text " + itemText);
            }
        };
    }

    @Test
    public void checkDataIntent_startRecipeDetailActivity() {
        onView(withId(R.id.rv_recipes)).perform(scrollTo(hasDescendant(withText(RECIPE_NAME_EXAMPLE))));
        onView(withItemText(RECIPE_NAME_EXAMPLE)).perform(click());
        intending(hasComponent(RecipeDetailActivity.class.getName()))
        .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, RecipeDetailActivity.createResultData(RECIPE_NAME_EXAMPLE)));
    }

    @Test
    public void checkIntent_startRecipeDetailActivity() {
        onView(withId(R.id.rv_recipes)).perform(scrollTo(hasDescendant(withText(RECIPE_NAME_EXAMPLE))));
        onView(withItemText(RECIPE_NAME_EXAMPLE)).perform(click());
        intended(hasComponent(RecipeDetailActivity.class.getName()));
    }

    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(
                mIntentTestRule.getActivity().getCountingIdlingResource());
    }

}
