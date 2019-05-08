package com.example.onlineshoopingapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.onlineshoopingapp.R;
import com.example.onlineshoopingapp.model.BarcodeScanner;
import com.example.onlineshoopingapp.model.Product;
import com.example.onlineshoopingapp.model.ProductsAdapter;
import com.example.onlineshoopingapp.view.fragments.CategoriesFragment;
import com.example.onlineshoopingapp.view.fragments.ExpandedProductFragment;
import com.example.onlineshoopingapp.view.fragments.ProductsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements ProductsAdapter.ProductsAdapterInterface {
    //this is used as the path to the image we want to capture
    private BarcodeScanner barcodeScanner;
    private ArrayList<Product> phones;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        barcodeScanner = new BarcodeScanner(this);
        populateTestData();
        initializeUI();

    }



    private void displayProductsFragment(ArrayList<Product> products) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_main, ProductsFragment.newInstance(products,false))
                .addToBackStack(null)
                .commitAllowingStateLoss();

    }


    private void initializeUI() {
        Toolbar myToolbar = findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");
        myToolbar.setOnClickListener(view -> searchView.setIconified(false));
        //show the home fragment
        displayHomeFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            Fragment fragment =null;
            switch (menuItem.getItemId()){
                case R.id.home_nav_view:
                      fragment = ProductsFragment.newInstance(phones,false);
                    break;
                case R.id.sales_nav_view:
                    fragment = ProductsFragment.newInstance(phones,false);
                      break;
                case R.id.products_nav_view:
                    fragment = CategoriesFragment.newInstance();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main,fragment).commit();
            return true;
        });

    }

    private void displayHomeFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_main,ProductsFragment.newInstance(phones,false))
                .commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //RESULT_OK is a public field from the Activity class
        if (requestCode == barcodeScanner.getRequestImageCaptureCode() && resultCode == RESULT_OK) {
            ArrayList<String> QRValues = barcodeScanner.processImage();
            searchDatabaseByQRCodes(QRValues);
        }
    }

    /**
     * This method is used to search in the database
     * for items with corresponding qrCodes
     *
     * @param rawValues
     */
    private void searchDatabaseByQRCodes(ArrayList<String> rawValues) {
        //TODO
        //this is now just a test method,we don't actually search in the database
        ArrayList<Product> productsFound = new ArrayList<>();
        for(Product product : phones){
            if(rawValues.contains(product.getQRValue())){
                productsFound.add(product);
            }
        }
        displayProductsFragment(productsFound);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_camera_icon_menu:
                barcodeScanner.scan();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_toolbar_menu, menu);
        searchView = (SearchView) menu.findItem(R.id.search_icon_menu).getActionView();
        //due to a bug in the android system I need to do this to change the search icon to white...
        ImageView searchIcon = searchView.findViewById(R.id.search_button);
        searchIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_search_white_24dp));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                resetSearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return true;
            }
        });

        return true;


    }

    private void resetSearch() {
        searchView.setQuery("", false);
        searchView.setIconified(true);

    }

    /**
     * Finish the activity when the backButton is pressed
     */
    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    /**
     * Method called when the user has clicked on a specific item
     * Add the expandedProductFragment
     * Replace the current fragment with
     * the ExpandedProductFragment
     */
    @Override
    public void expandProduct(Product product) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_main, ExpandedProductFragment.newInstance(product))
                .addToBackStack(null)
                .commit();
    }

    private void populateTestData() {
        phones = new ArrayList<>();
        String testURI = "https://firebasestorage.googleapis.com/v0/b/fir-app-1d7d7.appspot.com/o/pic_familyP30_md.jpg?alt=media&token=4f7268fa-bd58-4026-817e-cfb2ad341940";
        phones.add(new Product("Huawei P30 Pro with Leica camera", 699.99, new String[]{testURI, testURI, testURI, testURI}, 600, 5, "qr1"));
        phones.add(new Product("Huawei P30 Pro with Leica camera", 699.99, new String[]{testURI, testURI, testURI, testURI}, 600, 5, null));
        phones.add(new Product("Huawei P30 Pro with Leica camera", 699.99, new String[]{testURI, testURI, testURI, testURI}, 600, 5, "qr2"));
        phones.add(new Product("Huawei P30 Pro with Leica camera", 699.99, new String[]{testURI, testURI, testURI, testURI}, 600, 5, null));
        phones.add(new Product("Huawei P30 Pro with Leica camera", 699.99, new String[]{testURI, testURI, testURI, testURI}, 600, 5, "qr1"));

    }
}
