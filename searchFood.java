package com.example.likaiapply.eatergo;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.felipecsl.quickreturn.library.QuickReturnAttacher;
import com.felipecsl.quickreturn.library.widget.QuickReturnAdapter;
import com.felipecsl.quickreturn.library.widget.QuickReturnTargetView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class searchFood extends Fragment {

    static boolean IS_SEARCH_VISIBLE; // retain list
    static boolean SEARCH_RETAIN; // retain search toolbar
    private static String STRING_FOOD_SEARCH; // retain editText
    private static int CURRENT_PAGE = 0; // Was used to control page items with CURRENT_PAGE++
    private View v; // Main content view

    private FatSecretSearch mFatSecretSearch;
    private FatSecretGet mFatSecretGet;
    private Animation mFadeIn;
    private InputMethodManager imm;
    private Toolbar mToolbarTop, mToolbarSearch;
    private EditText mSearch;
    String food_name;
    String calories;
    String carbohydrate;
    String protein;
    String fat;
    private ListView mListView;
    private ProgressBar mProgressMore, mProgressSearch;
    private SearchAdapter mSearchAdapter;
    private ArrayList<Item> mItem;
    QuickReturnAttacher mQuickReturnAttacher; // Library: https://github.com/felipecsl/QuickReturn
    RelativeLayout mSlidingLayout;

    public searchFood() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_search_food, container, false);
        if (savedInstanceState != null) {
            IS_SEARCH_VISIBLE = savedInstanceState.getBoolean("IS_SEARCH_VISIBLE");
            STRING_FOOD_SEARCH = savedInstanceState.getString("STRING_FOOD_SEARCH");
            SEARCH_RETAIN = savedInstanceState.getBoolean("SEARCH_RETAIN");
        }
        ImageButton button = (ImageButton) v.findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View button)
            {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                searchFood(mSearch.getText().toString(),0);
                findViewsById();
                getImplementation();
                Log.i("rew","button click");
                mSearchAdapter.notifyDataSetChanged();
                mSearch.clearFocus();
            }
        });
        findViewsById();

        getImplementation();

        return v ;
    }
    private void updateList() {
        if (mSearchAdapter.getCount() == 0) {
            mListView.setVisibility(View.GONE);
        } else {
            mListView.setVisibility(View.VISIBLE);
        }
    }


    private void findViewsById() {
        mFatSecretSearch = new FatSecretSearch(); // method.search
        mFatSecretGet = new FatSecretGet(); // method.get
        mFadeIn = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fade_in);
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); // handle soft keyboard
        mToolbarTop = (Toolbar) v.findViewById(R.id.toolbar_top);
        mToolbarSearch = (Toolbar) v.findViewById(R.id.toolbar_search);
        mSearch = (EditText) v.findViewById(R.id.etSearch);
        mListView = (ListView) v.findViewById(R.id.listView);
        listViewConfigurations();

        mProgressSearch = (ProgressBar) v.findViewById(R.id.searchProgress);
        mProgressSearch.setVisibility(View.INVISIBLE);
        updateList();
    }

    private void listViewConfigurations() {

        mItem = new ArrayList<>();
        mSearchAdapter = new SearchAdapter(getActivity(), mItem);
        mListView.setAdapter(new QuickReturnAdapter(mSearchAdapter));
        mQuickReturnAttacher = QuickReturnAttacher.forView(mListView);
        mSlidingLayout = (RelativeLayout) v.findViewById(R.id.relativeLayout);
        mSearchAdapter.notifyDataSetChanged();

        mQuickReturnAttacher.addTargetView(mSlidingLayout, QuickReturnTargetView.POSITION_TOP, Equations.dpToPx(getActivity(), 55));
    }
    private void getImplementation() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)return;
                if (position < mItem.size()) { // Should to be refactored
                    getFood(Long.valueOf(mItem.get(position - 1).getID()));
                } else {
                    if (mItem.size() == 20)
                        searchFood(mSearch.getText().toString(), 1);
                    else if (mItem.size() == 40)
                        searchFood(mSearch.getText().toString(), 2);
                    else if (mItem.size() == 60)
                        searchFood(mSearch.getText().toString(), 3);
                    else if (mItem.size() == 80)
                        searchFood(mSearch.getText().toString(), 4);
                    else if (mItem.size() == 100)
                        searchFood(mSearch.getText().toString(), 5);
                    else if (mItem.size() == 120)
                        searchFood(mSearch.getText().toString(), 6);
                    else if (mItem.size() == 140)
                        searchFood(mSearch.getText().toString(), 7);
                    else if (mItem.size() == 160)
                        searchFood(mSearch.getText().toString(), 8);
                    else if (mItem.size() == 180)
                        searchFood(mSearch.getText().toString(), 9);
                    else if (mItem.size() == 200)
                        searchFood(mSearch.getText().toString(), 10);
                }
            }
        });
    }

    private void getFood(final Long id) {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... arg0) {
                JSONObject foodGet = mFatSecretGet.getFood(id);
                try {
                    if (foodGet != null) {
                        food_name = foodGet.getString("food_name");
                        JSONObject servings = foodGet.getJSONObject("servings");

                        JSONObject serving = servings.getJSONObject("serving");
                        calories = serving.getString("calories");
                         carbohydrate = serving.getString("carbohydrate");
                        protein = serving.getString("protein");
                       fat = serving.getString("fat");
                        String serving_description = serving.getString("serving_description");
                        Log.i("serving_description", serving_description);
                        /**
                         * Displays results in the LogCat
                         */
                        Log.i("food_name", food_name);
                        Log.i("calories", calories);
                        Log.i("carbohydrate", carbohydrate);
                        Log.i("protein", protein);
                        Log.i("fat", fat);
                    }

                } catch (JSONException exception) {
                    return "Error";
                }
                return "";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Toast.makeText(getActivity(), "food name:"+food_name+"\ncalories:"+calories+"\ncarbohydrate:"+carbohydrate+"\nprotein:"+protein+"\nfat:"+fat, Toast.LENGTH_LONG).show();

                //if (result.equals("Error"))
                //    Toast.makeText(getActivity(), "No Items Containing Your Search", Toast.LENGTH_SHORT).show();
               // mCallbacks.fromFragment();
            }
        }.execute();
    }



    String brand;


    private void searchFood(final String item, final int page_num) {
        new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {

                mProgressSearch.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... arg0) {
                JSONObject food = mFatSecretSearch.searchFood(item, page_num);
                JSONArray FOODS_ARRAY;
                try {
                    if (food != null) {
                        FOODS_ARRAY = food.getJSONArray("food");
                        if (FOODS_ARRAY != null) {
                            for (int i = 0; i < FOODS_ARRAY.length(); i++) {
                                JSONObject food_items = FOODS_ARRAY.optJSONObject(i);
                                String food_name = food_items.getString("food_name");
                                String food_description = food_items.getString("food_description");
                                String[] row = food_description.split("-");
                                String id = food_items.getString("food_type");
                                if (id.equals("Brand")) {
                                    brand = food_items.getString("brand_name");
                                }
                                if (id.equals("Generic")) {
                                    brand = "Generic";
                                }
                                String food_id = food_items.getString("food_id");
                                mItem.add(new Item(food_name, row[1].substring(1),
                                        "" + brand, food_id));
                            }
                        }
                    }
                } catch (JSONException exception) {
                    return "Error";
                }
                return "";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result.equals("Error"))
                    Toast.makeText(getActivity(), "No Items Containing Your Search", Toast.LENGTH_SHORT).show();
                mSearchAdapter.notifyDataSetChanged();
                updateList();

                mProgressSearch.setVisibility(View.INVISIBLE);
                SEARCH_RETAIN = true;
            }
        }.execute();
    }


}
