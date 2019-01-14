package com.raymondyang.shoppingapp;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.raymondyang.shoppingapp.customviews.MyDragShadowBuilder;
import com.raymondyang.shoppingapp.model.Product;
import com.raymondyang.shoppingapp.resourses.Products;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewProductActivity extends AppCompatActivity implements View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener, View.OnDragListener {
    public static final String TAG = ViewProductActivity.class.getSimpleName();
    @BindView(R.id.text_desc)
    TextView mTextDesc;
    @BindView(R.id.viewPager)
    ViewPager mProductContainer;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar mToolbar;
    @BindView(R.id.button)
    Button mAddtoCart;


    MenuItem mMenu;
    private Product mProduct;
    private ProductPagerAdapter mPagerAdapter;
    private GestureDetector mGestureDetector;
    private Rect mCartPositionRectangle;
    @BindView(R.id.plus_image)
    ImageView mPlusIcon;
    @BindView(R.id.cart_image)
    ImageView mCartIcon;
    @BindView(R.id.cart)
    ConstraintLayout mCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        ButterKnife.bind(this);
        //setSupportActionBar(mToolbar);
        mProductContainer.setOnTouchListener(this);
        mGestureDetector = new GestureDetector(this, this);
       // mTextDesc.setOnTouchListener(this);

        Intent intent = getIntent();
        mProduct = intent.getParcelableExtra("intent_product");



        mAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCurrentItemToCart();

            }
        });

        initViewPager();
    }

    private void getCardPosition(){
        mCartPositionRectangle = new Rect();
        mCart.getGlobalVisibleRect(mCartPositionRectangle);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        mCartPositionRectangle.left = mCartPositionRectangle.left - Math.round((int)(width * 0.18));
        mCartPositionRectangle.top = 0;
        mCartPositionRectangle.right = width;
        mCartPositionRectangle.bottom = mCartPositionRectangle.bottom - Math.round((int)(width * 0.03));
    }

    private void setDragMode(boolean isDragging){
        if(isDragging){
            mCartIcon.setVisibility(View.INVISIBLE);
            mPlusIcon.setVisibility(View.VISIBLE);

        } else {
            mCartIcon.setVisibility(View.VISIBLE);
            mPlusIcon.setVisibility(View.INVISIBLE);
        }
    }

    private void initViewPager(){
        List<Fragment> fragments = new ArrayList<>();

        Products products = new Products();

        Product[] selectedProducts = products.PRODUCT_MAP.get(mProduct.getType());

        for(Product product : selectedProducts){
            Bundle bundle = new Bundle();
            bundle.putParcelable("intent_product", product);
            ViewFragmentProduct viewFragmentProduct = new ViewFragmentProduct();
            viewFragmentProduct.setArguments(bundle);
            fragments.add(viewFragmentProduct);

        }
        mPagerAdapter = new ProductPagerAdapter(getSupportFragmentManager(), fragments);
        mProductContainer.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mProductContainer, true);

//        ConstraintLayout constraintLayout = findViewById(R.id.constr_text);
//        ConstraintSet constraintSet = new ConstraintSet();
//        constraintSet.clone(constraintLayout);
//        constraintSet.connect(mProductContainer.getId(),ConstraintSet.BOTTOM,constraintLayout.getId(),ConstraintSet.TOP,30);
//        constraintSet.applyTo(constraintLayout);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        getCardPosition();

        if(v.getId() == R.id.viewPager){
            mGestureDetector.onTouchEvent(event);
        }

//        int action = event.getAction();
//
//        switch (action){
//
//            case (MotionEvent.ACTION_DOWN):
//                Log.d(TAG, "onTouch: Action was Down");
//                return false;
//            case (MotionEvent.ACTION_MOVE):
//                Log.d(TAG, "onTouch: Action was MOVE");
//                return false;
//            case (MotionEvent.ACTION_UP):
//                Log.d(TAG, "onTouch: Action was UP");
//                return false;
//            case (MotionEvent.ACTION_CANCEL):
//                Log.d(TAG, "onTouch: Action was CANCEL");
//                return false;
//            case (MotionEvent.ACTION_OUTSIDE):
//                Log.d(TAG, "onTouch: Movement occurred outside");
//                return false;
//        }
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.i(TAG, "onDown()");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.i(TAG, "onShowPress()");

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.i(TAG, "onSingleTapUp()");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.i(TAG, "onScroll()");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.i(TAG, "onLongPress()");
        ViewFragmentProduct fragment = (ViewFragmentProduct)mPagerAdapter.getItem(mProductContainer.getCurrentItem());

        View.DragShadowBuilder myShadow = new MyDragShadowBuilder(fragment.mImageView, fragment.mProduct.getImage());

        ((ViewFragmentProduct)fragment).mImageView.startDragAndDrop(null,
                myShadow,
                null,
                0);

        myShadow.getView().setOnDragListener(this);

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.i(TAG, "onFling()");
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.i(TAG, "onSingleTapConfirmed()");
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.i(TAG, "onDoubleTap()");
        inflateFullScreenImage();
        return false;
    }

    private void inflateFullScreenImage() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("intent_product", ((ViewFragmentProduct)mPagerAdapter.getItem(mProductContainer.getCurrentItem())).mProduct);
        FullScreenProductFragment fullScreenProductFragment = new FullScreenProductFragment();
        fullScreenProductFragment.setArguments(bundle);

        Fade enterFade = new Fade();
        enterFade.setStartDelay(2000);
        enterFade.setDuration(1000);
        fullScreenProductFragment.setEnterTransition(enterFade);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.full_screen_container, fullScreenProductFragment);
        fragmentTransaction.commit();

    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.i(TAG, "onDoubleTapEvent()");
        return false;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.menu_cart, menu);
//        mMenu = menu.getItem(0);
//       // mMenu.getIcon().setTint(getResources().getColor(R.color.blue2));
//        mCartIcon = mMenu.getIcon();
//        return true;
//    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        mMenu = menu.findItem(R.id.cart);
//        View v = MenuItemCompat.getActionView(mMenu);
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.cart)
        {

        }

        return super.onOptionsItemSelected(item);
    }

    public void addCurrentItemToCart(){
        Product selectedProduct = ((ViewFragmentProduct)mPagerAdapter
                .getItem(mProductContainer.getCurrentItem())).mProduct;

        CartManager cartManager = new CartManager(this);
        cartManager.addItemToCart(selectedProduct);
        Toast.makeText(this, "added to the cart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {

        switch (event.getAction()){

            case DragEvent.ACTION_DRAG_STARTED:
                Log.d(TAG, "ACTION_DRAG_STARTED");
                setDragMode(true);
                return true;

            case DragEvent.ACTION_DRAG_ENTERED:
                Log.d(TAG, "ACTION_DRAG_ENTERED");
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                Log.d(TAG, "ACTION_DRAG_LOCATION");
                Point currentPoint = new Point(Math.round(event.getX()), Math.round(event.getY()));

                if(mCartPositionRectangle.contains(currentPoint.x, currentPoint.y)){
                    mCart.setBackgroundColor(this.getResources().getColor(R.color.blue2, null));
                } else {
                    mCart.setBackgroundColor(this.getResources().getColor(R.color.blue1, null));

                }
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                Log.d(TAG, "ACTION_DRAG_EXITED");
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                Log.d(TAG, "ACTION_DRAG_ENDED");
                Drawable background = mCart.getBackground();

                if(background instanceof ColorDrawable){
                    if(((ColorDrawable) background).getColor() == getResources().getColor(R.color.blue2)){
                        addCurrentItemToCart();
                    }

                }
                mCart.setBackgroundColor(this.getResources().getColor(R.color.blue1, null));

                setDragMode(false);
                return true;

            default:
                Log.d(TAG, "Unknown action type received by OnStartDrag.");
                break;
        }
        return false;
    }


}
