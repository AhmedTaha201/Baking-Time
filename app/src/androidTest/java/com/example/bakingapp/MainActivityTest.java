package com.example.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.bakingapp.IdlingResource.SimpleIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * Created by Taha on 6/21/2018.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private final int mRecipePosition = 1;
    @Rule
    public IntentsTestRule<MainActivity> mainActivityTestRule = new IntentsTestRule<>(MainActivity.class);
    IdlingRegistry mRegistry;
    private SimpleIdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mRegistry = IdlingRegistry.getInstance();
        mIdlingResource = mainActivityTestRule.getActivity().getIdlingResource();
        mRegistry.register(mIdlingResource);
    }

    @Test
    public void recipeClick_resultsTheRightIntentWithExtras() {

        onView(withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(mRecipePosition, click()));

        intended(allOf(
                hasExtra(RecipeActivity.RECIPE_EXTRA, mainActivityTestRule.getActivity().mRecipeList.get(mRecipePosition))
                , toPackage(mainActivityTestRule.getActivity().getPackageName())
                )
        );
    }


    @After
    public void unRegisterIdlingResource() {
        if (mIdlingResource != null) {
            mRegistry.unregister(mIdlingResource);
        }
    }
}

