package com.nbvarnado.bakingapp;

import android.content.pm.ActivityInfo;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.nbvarnado.bakingapp.ui.ingredients.IngredientsActivity;
import com.nbvarnado.bakingapp.ui.recipe.StepListActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4ClassRunner.class)
public class StepListActivityTest {

    @Rule
    public IntentsTestRule<StepListActivity> mIntentsTestRule = new IntentsTestRule <>(StepListActivity.class);

    @Test
    public void clickIngredientsButton_DisplaysIngredients() {
        mIntentsTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // TODO: Determine how to test for either the ingredients activity or fragment.
        onView(withId(R.id.button_steps_ingredients)).perform(click());
        intended(hasComponent(IngredientsActivity.class.getName()));
    }

}
