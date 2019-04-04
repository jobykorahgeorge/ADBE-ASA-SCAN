package com.adobe.intelliscan.scan;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.adobe.intelliscan.R;
import com.adobe.intelliscan.model2.ProductResponse;
import com.adobe.intelliscan.models.KeyFeature1;
import com.adobe.intelliscan.models.ProductModel;
import com.adobe.intelliscan.models.SimilarProduct;
import com.adobe.intelliscan.network.GetDataService;
import com.adobe.intelliscan.network.VolleyHandler;
import com.adobe.intelliscan.utils.CustomRadioButton;
import com.adobe.intelliscan.utils.Preferences;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import me.crosswall.lib.coverflow.CoverFlow;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ProductFragment extends Fragment implements VolleyHandler.NetworkListener ,BrowserActivityCallBack{

    private static final String TAG = "ProductFragment";
    private String scannedColor;
    private ProductBean mProduct;
    private VolleyHandler mVolleyHandler;
    private ViewPager imagePager;
    private ImageButton closeImgBtton;
    private ImageButton cartImgButtn;
    private RadioGroup radioGroup;
    private LinearLayout colorsGroup;
    private TextView tv_keyFeatures;
    private RecyclerView specRecycleView;
    private RadioButton specButton;
    private ImageButton similarProd;
    private TabLayout tabLayout;
    private static Retrofit retrofit=null;
    ProductModel productModel;
    ProductResponse productResponse;
    SimilarProduct similarProduct;
    SimilarItemAdapter similarItemAdapter;
    ReviewRecyclerAdapter reviewRecyclerAdapter;
    KeyFeatureRVAdapter keyFeatureRVAdapter;
    LinearLayout.LayoutParams params;

    AlertDialog.Builder builder;
    LayoutInflater inflater;
    PlayerView playerView;
    RecyclerView recyclerView;
    RelativeLayout relativeFullLayout;

    ImageButton closeVideo ;
    ImageButton closeSimilar ;
    ImageButton closeReview;
    ImageButton playVideo;
    ImageButton reviews;
    RatingBar ratingBar;
    int currentpage;
    int sizeofData;
    RecyclerView keyFeatureRV;
    RecyclerView recyclerViewReview;

    FeatureCoverFlow featureCoverFlow;
    CoverFlowAdapter coverFlowAdapter;

    CustomRadioButton colorRadioButton;
    CustomRadioButton keyRadioButton;
    CustomRadioButton specRadioButton;

    ProgressDialog ringProgressDialog1;

    ScrollView scrollview;

    String SKU = "24-MB02";

    int currentPage;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;

//    View dialogLayout;

    int i =0;
    private OnInteractionListener mListener;


    public ProductFragment() {

    }

    public static ProductFragment newInstance(String scannedColor) {

        ProductFragment fragment = new ProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString("COLOR", scannedColor);
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        scannedColor = getArguments().getString("COLOR");
        Log.d(TAG, "scannedColor: " + scannedColor);
        View root = inflater.inflate(R.layout.fragment_product, container, false);
        initViews(root);
        fetchProductData();
        return root;

    }

    private void initViews(View root) {

        productModel = new ProductModel();
        productResponse = new ProductResponse();
        similarProduct = new SimilarProduct();
        builder = new AlertDialog.Builder(getContext(),R.style.CustomDialog);
        inflater  = getActivity().getLayoutInflater();
        imagePager = root.findViewById(R.id.prod_image_pager);
        closeImgBtton = root.findViewById(R.id.prod_close_ibtn);
        cartImgButtn = root.findViewById(R.id.add_cart_btn);
        radioGroup = root.findViewById(R.id.prod_radio_group);
        colorsGroup = root.findViewById(R.id.prod_colors_layout);
        tv_keyFeatures = root.findViewById(R.id.prod_key_features_text);
        specRecycleView = root.findViewById(R.id.prod_specs_recycle_view);
        specButton = root.findViewById(R.id.prod_spec_radio);
        similarProd = root.findViewById(R.id.prod_similar_radio);
        tabLayout = (TabLayout) root.findViewById(R.id.tab_layout);
        specRecycleView.setNestedScrollingEnabled(false);
        ratingBar = (RatingBar) root.findViewById(R.id.rating);
        keyFeatureRV =root.findViewById(R.id.prod_key_feature_recycleView);
        recyclerViewReview=root.findViewById(R.id.recycleViewReview);
        playerView = (PlayerView) root.findViewById(R.id.video_view);
        playVideo =(ImageButton)root.findViewById(R.id.play_video);
        reviews =(ImageButton)root.findViewById(R.id.review);
        recyclerView = root.findViewById(R.id.recycleView);
        relativeFullLayout = (RelativeLayout) root.findViewById(R.id.relative_full_layout);
        closeVideo = (ImageButton)root.findViewById(R.id.close_video);
        closeSimilar = (ImageButton) root.findViewById(R.id.close_similar);
        closeReview = (ImageButton)root.findViewById(R.id.close_review);
        colorRadioButton = (CustomRadioButton)root.findViewById(R.id.prod_color_radio);
        keyRadioButton =(CustomRadioButton)root.findViewById(R.id.prod_feature_radio);
        specRadioButton = (CustomRadioButton)root.findViewById(R.id.prod_spec_radio);
        scrollview = (ScrollView)root.findViewById(R.id.scrollview);
//        featureCoverFlow =(FeatureCoverFlow)root.findViewById(R.id.coverflow);

        ArrayList<String> imageUrlList = new ArrayList<>();
        imageUrlList.add("https://blog.teamtreehouse.com/wp-content/uploads/2015/05/InternetSlowdown_Day.gif");

//        coverFlowAdapter = new CoverFlowAdapter(getActivity(),imageUrlList);
//        featureCoverFlow.setAdapter(coverFlowAdapter);




        similarProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = "similar";
                loadAlertData(from);
            }
        });

        playVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = "video";
                loadAlertData(from);
            }
        });
        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = "reviews";
                loadAlertData(from);
            }
        });

        closeImgBtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.close();
                Intent i = new Intent(getActivity(),BarcodeScanActivity.class);
                startActivity(i);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("chk", "id" + checkedId);

                switch (checkedId) {
                    case R.id.prod_color_radio:
                        loadColorsLayout();
                        scrollview.scrollTo(0,colorRadioButton.getTop());
                        colorRadioButton.setTextColor(getResources().getColor(R.color.font_white));
                        keyRadioButton.setTextColor(getResources().getColor(R.color.radio_checked_bg));
                        specRadioButton.setTextColor(getResources().getColor(R.color.radio_checked_bg));
                        keyFeatureRV.setFocusable(false);
                        specRecycleView.setFocusable(false);

                        break;
                    case R.id.prod_feature_radio:
                        loadFeaturesLayout();
                        scrollview.scrollTo(0,colorRadioButton.getTop());
                        colorRadioButton.setTextColor(getResources().getColor(R.color.radio_checked_bg));
                        keyRadioButton.setTextColor(getResources().getColor(R.color.font_white));
                        specRadioButton.setTextColor(getResources().getColor(R.color.radio_checked_bg));
                        keyFeatureRV.setFocusable(false);
                        specRecycleView.setFocusable(false);
//                        colorRadioButton.setTextColor(getResources().getColor(R.color.font_white));
                        break;
                    case R.id.prod_spec_radio:
                        loadSpecsLayout();
                        scrollview.scrollTo(0,colorRadioButton.getTop());
                        colorRadioButton.setTextColor(getResources().getColor(R.color.radio_checked_bg));
                        keyRadioButton.setTextColor(getResources().getColor(R.color.radio_checked_bg));
                        specRadioButton.setTextColor(getResources().getColor(R.color.font_white));
                        keyFeatureRV.setFocusable(false);
                        specRecycleView.setFocusable(false);
                        break;
                }
            }
        });

        ((RadioButton) root.findViewById(R.id.prod_color_radio)).setChecked(true);


        cartImgButtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SKU VAL",SKU);

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(),R.style.CustomAlertDialog);
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.alert_dialog_cart,null);
                alertDialog.setView(dialogView);

                final AlertDialog alertDialog1 = alertDialog.create();

                alertDialog1.show();

                final TextView yes = (TextView)dialogView.findViewById(R.id.yes);



                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Preferences preferences = new Preferences(getActivity());
                        String url = preferences.getData(Preferences.API_BASE_URL_CONFIG);
                        Log.d("urlcall",url);

                        alertDialog1.dismiss();
                        final ProgressDialog ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait", "Adding item to cart", true);


                        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new com.android.volley.Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Toast toast = Toast.makeText(getContext(),"Successfully added to Cart",Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER,0,0);
                                        toast.show();

                                        ringProgressDialog.dismiss();



                                        final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(getActivity(),R.style.CustomAlertDialog);
                                        LayoutInflater inflater1 = getActivity().getLayoutInflater();
                                        final View dialogView = inflater1.inflate(R.layout.alert_dialog_cart,null);
                                        alertDialog2.setView(dialogView);
                                        final AlertDialog alertDialog3 = alertDialog2.create();
                                        alertDialog3.show();

                                        TextView alertcheck = (TextView)dialogView.findViewById(R.id.text_alert_checkout);
                                        alertcheck.setText("Do you want to checkout?");
                                        TextView yes1 = (TextView)dialogView.findViewById(R.id.yes);
                                        TextView no1 = (TextView)dialogView.findViewById(R.id.no);

                                        yes1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Uri uri = Uri.parse("");
                                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                getActivity().startActivity(intent);
                                                alertDialog3.dismiss();
                                            }
                                        });
                                        no1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                alertDialog3.dismiss();
                                            }
                                        });



                                    }
                                },
                                new com.android.volley.Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
                                    }
                                }){
                            @Override
                            protected Map<String,String> getParams(){
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("sku",SKU);
                                params.put("username",preferences.getData(Preferences.USER_NAME));
                                params.put("password", preferences.getData(Preferences.PASSWORD));
                                return params;
                            }

                        };

                        requestQueue.add(stringRequest);
                    }
                });

                TextView no = (TextView)dialogView.findViewById(R.id.no);
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog1.dismiss();
                    }
                });


            }
        });
    }



    private void loadAlertData(String from) {



        Preferences preferences = new Preferences(getActivity());
        String url = preferences.getData(Preferences.KEY_APP_SERVER_URL);
        String prodPath = "";
        prodPath = preferences.getData(Preferences.KEY_PROD_ONE_PATH)+scannedColor+".json";
        String prodUrl = url + prodPath;
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create(gson)).addConverterFactory(ScalarsConverterFactory.create()).build();
        }

        GetDataService service = retrofit.create(GetDataService.class);
        Call<ProductModel> call = service.loadData(prodPath);
        call.enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                if(response.isSuccessful())
                    productModel = response.body();

                    similarItemAdapter = new SimilarItemAdapter(getActivity(),productModel.getSimilarProducts());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(similarItemAdapter);


                    reviewRecyclerAdapter = new ReviewRecyclerAdapter(productModel.getReviews(),getActivity());
                    recyclerViewReview.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerViewReview.setAdapter(reviewRecyclerAdapter);



            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {

            }
        });

        if(from.equals("video")){

                if(productModel.getVideoPath().length()<3){
                    Toast.makeText(getActivity(),"No video for this product",Toast.LENGTH_SHORT).show();
                }
                else {


                    relativeFullLayout.setVisibility(View.GONE);
                    playerView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);

                    closeVideo.setVisibility(View.VISIBLE);
                    closeVideo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            playerView.setVisibility(View.GONE);
                            relativeFullLayout.setVisibility(View.VISIBLE);
                            closeVideo.setVisibility(View.GONE);
                        }
                    });


                    Player player = ExoPlayerFactory.newSimpleInstance(
                            new DefaultRenderersFactory(getActivity()),
                            new DefaultTrackSelector(), new DefaultLoadControl());
                    playerView.setPlayer(player);

                    player.setPlayWhenReady(true);
//                    player.setRepeatMode(Player.REPEAT_MODE_ALL);


                    Uri uri = Uri.parse(url + productModel.getVideoPath());
                    MediaSource mediaSource = buildMediaSource(uri);
                    ((SimpleExoPlayer) player).prepare(mediaSource);

                    closeVideo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            playerView.setVisibility(View.GONE);
                            relativeFullLayout.setVisibility(View.VISIBLE);
                            closeVideo.setVisibility(View.GONE);
                        }
                    });
                }

        }

        if(from.equals("similar")){
            relativeFullLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            closeSimilar.setVisibility(View.VISIBLE);
            closeSimilar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerView.setVisibility(View.GONE);
                    relativeFullLayout.setVisibility(View.VISIBLE);
                    closeSimilar.setVisibility(View.GONE);
                }
            });
        }

        if(from.equals("reviews")){
            relativeFullLayout.setVisibility(View.GONE);
            recyclerViewReview.setVisibility(View.VISIBLE);
            closeReview.setVisibility(View.VISIBLE);
            closeReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewReview.setVisibility(View.GONE);
                    relativeFullLayout.setVisibility(View.VISIBLE);
                    closeReview.setVisibility(View.GONE);
                }
            });

        }






    }

//    private void getSimilarAdapter(List<SimilarProduct> similarProducts){
//        final RecyclerView recyclerView = dialogLayout.findViewById(R.id.recycleView);
//        similarItemAdapter = new SimilarItemAdapter(getActivity(),similarProducts);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(similarItemAdapter);
//    }



    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }


    private void loadColorsLayout() {
        scrollview.scrollTo(0,colorRadioButton.getTop());
        colorsGroup.setVisibility(View.VISIBLE);
//        tv_keyFeatures.setVisibility(View.GONE);
        keyFeatureRV.setVisibility(View.GONE);
        specRecycleView.setVisibility(View.GONE);
        scrollview.scrollTo(0,colorRadioButton.getTop());
//        scrollview.scrollTo(0,colorRadioButton.getTop());
    }

    private void loadFeaturesLayout() {
//        scrollview.scrollTo(0,500);
        scrollview.scrollTo(0,colorRadioButton.getTop());

        colorsGroup.setVisibility(View.GONE);
//        tv_keyFeatures.setVisibility(View.VISIBLE);
        keyFeatureRV.setVisibility(View.VISIBLE);
        specRecycleView.setVisibility(View.GONE);
        scrollview.scrollTo(0,colorRadioButton.getTop());
//        scrollview.scrollTo(0,colorRadioButton.getTop());
    }

    private void loadSpecsLayout() {
//        scrollview.scrollTo(0,500);
        scrollview.scrollTo(0,colorRadioButton.getTop());
        colorsGroup.setVisibility(View.GONE);
//        tv_keyFeatures.setVisibility(View.GONE);
        keyFeatureRV.setVisibility(View.GONE);
        specRecycleView.setVisibility(View.VISIBLE);
        scrollview.scrollTo(0,colorRadioButton.getTop());
//        scrollview.scrollTo(0,colorRadioButton.getTop());

    }

    private void clearAll(){
        colorsGroup.setVisibility(View.INVISIBLE);
        tv_keyFeatures.setVisibility(View.INVISIBLE);
        specRecycleView.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnInteractionListener) {
            mListener = (OnInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void fetchProductData() {

        Preferences preferences = new Preferences(getActivity());
        String url = preferences.getData(Preferences.KEY_APP_SERVER_URL);
        String prodPath = "";
        prodPath = preferences.getData(Preferences.KEY_PROD_ONE_PATH)+scannedColor+".json";

        String prodUrl = url + prodPath;
        Log.d("URL CALL: ",prodUrl);
        mVolleyHandler = VolleyHandler.getInstance(this);
        mVolleyHandler.makeRequest(getActivity().getApplicationContext(), prodUrl);


        String url1 = preferences.getData(Preferences.KEY_APP_SERVER_URL);
        String prodPath1 = "";
        prodPath1 = preferences.getData(Preferences.KEY_PROD_ONE_PATH)+scannedColor+".json";
        String prodUrl1 = url1 + prodPath1;
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(url1).addConverterFactory(GsonConverterFactory.create(gson)).addConverterFactory(ScalarsConverterFactory.create()).build();
        }

        GetDataService service = retrofit.create(GetDataService.class);
        Call<ProductResponse> call = service.loadData1(prodPath1);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if(response.isSuccessful()){
                    productResponse = response.body();

                    keyFeatureRVAdapter = new KeyFeatureRVAdapter(getActivity(),productResponse.getKeyFeatures());
//                Log.d("ddddd",productResponse.getKeyFeatures().get(0).getCategoryFeatures().get(0).toString());
                    keyFeatureRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                    keyFeatureRV.setAdapter(keyFeatureRVAdapter);
                    keyFeatureRV.setFocusable(false);
                    ratingBar.setRating(Float.valueOf(productResponse.getRatings()));
                }

            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {

            }
        });

    }

    @Override   // VolleyHandler.NetworkListener
    public void onRequest() {
        ringProgressDialog1 = ProgressDialog.show(getActivity(), "Please wait", "Fetching Item Details", true);
    }


    @Override   // VolleyHandler.NetworkListener
    public void onResponse(JSONObject joResponse) {
        mProduct = parseProductResponse(joResponse);
        Log.d("ssss",mProduct.toString());
        if(mProduct != null) {
            ringProgressDialog1.dismiss();

            showProductDetails();
            radioGroup.setVisibility(View.VISIBLE);
            similarProd.setVisibility(View.VISIBLE);
            playVideo.setVisibility(View.VISIBLE);
            reviews.setVisibility(View.VISIBLE);
            cartImgButtn.setVisibility(View.VISIBLE);
            ratingBar.setVisibility(View.VISIBLE);
        }
    }

    @Override   // VolleyHandler.NetworkListener
    public void onError(VolleyError error) {
        ringProgressDialog1.dismiss();
        radioGroup.setVisibility(View.INVISIBLE);
        similarProd.setVisibility(View.INVISIBLE);
        playVideo.setVisibility(View.INVISIBLE);
        reviews.setVisibility(View.INVISIBLE);
        cartImgButtn.setVisibility(View.INVISIBLE);


        Toast toast = Toast.makeText(getActivity(),"Close Window Scan Again!",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        LinearLayout toastContentView = (LinearLayout) toast.getView();
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(R.drawable.warning);
        toastContentView.addView(imageView,0);
        toast.show();

    }


    private ProductBean parseProductResponse(JSONObject joResponse) {

        ProductBean product = new ProductBean();
//        ProductModel productModel = new ProductModel();
        try {
            product.setTitle(joResponse.getString("Product Title"));
            productModel.setVideoPath(joResponse.getString("videoPath"));


            JSONArray jaSpecs = joResponse.optJSONArray("Specifications");
            if (jaSpecs != null) {

                ArrayList<String> specsList = new ArrayList<>();
                for (int i = 0; i < jaSpecs.length(); i++) {

                    String spec = jaSpecs.getString(i);
                    if(spec.contains(":")){

                        specsList.add(spec);

                    }
                }
                product.setSpecsList(specsList);
            }
            else {
                specButton.setVisibility(View.GONE);
            }


            JSONArray jaColors = joResponse.optJSONArray("Colors");
            if (jaColors != null) {

                ArrayList<ProductColor> colorsList = new ArrayList<>();
                for (int i = 0; i < jaColors.length(); i++) {

                    JSONObject joColor = jaColors.getJSONObject(i);
                    ProductColor prodColor = new ProductColor();

                    prodColor.setColor(joColor.getString("Color"));
                    prodColor.setHex(joColor.getString("Hex").trim());
                    prodColor.setSKU(joColor.getString("SKU"));

                    JSONArray jaColorUrl = joColor.optJSONArray("Images");
                    if (jaColorUrl != null) {

                        Log.d(TAG, "jaColorUrl: "+jaColorUrl);
                        ArrayList<String> colorUrlsList = new ArrayList<>();
                        for (int j = 0; j < jaColorUrl.length(); j++) {

                            Preferences preferences = new Preferences(getActivity());
                            String colorUrl = preferences.getData(Preferences.KEY_APP_SERVER_URL) + jaColorUrl.getString(j);
                            colorUrlsList.add(colorUrl);
                        }

                        Log.d(TAG, "colorUrlsList: "+colorUrlsList);
                        prodColor.setImageUrlList(colorUrlsList);
                    }

                    colorsList.add(prodColor);
                }

                product.setProductColorsList(colorsList);


            }




        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, product.toString());
        return product;
    }

    private void showKeyFeatures(KeyFeature1 keyFeature) {
//        KeyFeatureRVAdapter keyFeatureRVAdapter = new KeyFeatureRVAdapter(keyFeature);
//        keyFeatureRV.setAdapter(keyFeatureRVAdapter);
//        keyFeatureRV.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void showProductDetails() {

        mListener.onProductFetched(mProduct.getTitle());
//        BarcodeScanActivity.titleTextView.setText(mProduct.getTitle());

//        if (mProduct.getKeyFeaturesList() != null) {
//            String featureStr = "";
//            for (String feature : mProduct.getKeyFeaturesList()) {
//                featureStr += feature + "\n";
//            }
//            tv_keyFeatures.setText(featureStr);
//        }



        if (mProduct.getSpecsList() != null) {
            SpecRVAdapter specRVAdapter = new SpecRVAdapter(mProduct.getSpecsList());
            specRecycleView.setAdapter(specRVAdapter);
            specRecycleView.setFocusable(false);
            // Set layout manager to position the items
            specRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

//        Log.d("new", mProduct.getProductColorsList().toString());
        if (mProduct.getProductColorsList() != null) {

            int sizeDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
            int borderDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
            int radiusDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
            int marginDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(sizeDp, sizeDp);
            layoutParams.setMargins(marginDp, 0, marginDp, 0);

            String colorHexPreload = null;
            for (final ProductColor prodColor : mProduct.getProductColorsList()) {

                ImageButton imageButton = new ImageButton(getActivity());
                imageButton.setLayoutParams(layoutParams);

                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setStroke(borderDp, getResources().getColor(android.R.color.white));
                gradientDrawable.setCornerRadius(radiusDp);
                gradientDrawable.setColor(Color.parseColor(prodColor.getHex()));
                imageButton.setBackground(gradientDrawable);
                imageButton.setTag(prodColor.getHex());
                SKU = prodColor.getSKU();
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setChecked(view);
                        loadImages(view.getTag().toString());
                        SKU = prodColor.getSKU();
                    }
                });

                if(colorHexPreload == null){
                    colorHexPreload = prodColor.getHex();
                }

                colorsGroup.addView(imageButton);
            }

            colorsGroup.findViewWithTag(colorHexPreload).performClick();
        }
    }

    private void loadImages(String hexColor){
        ArrayList<ProductColor> colorList = mProduct.getProductColorsList();
        ArrayList<String> imageUrlList = null;

        for(ProductColor prodColor : colorList){
            if(prodColor.getHex().equals(hexColor)){
                imageUrlList = prodColor.getImageUrlList();
                break;
            }
        }

        if(imageUrlList != null){
//            featureCoverFlow.setVisibility(View.VISIBLE);
//            coverFlowAdapter = new CoverFlowAdapter(getActivity(),imageUrlList);
//            featureCoverFlow.setAlignTime(0);
//            featureCoverFlow.setAdapter(coverFlowAdapter);
//
////            Log.d("covercount",Integer.toString(coverFlowAdapter.getCount()));
//            featureCoverFlow.setHorizontalScrollBarEnabled(true);
//            featureCoverFlow.setSelection(imageUrlList.size()-1);
//            featureCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
//                @Override
//                public void onScrolledToPosition(int position) {
//
//                }
//
//                @Override
//                public void onScrolling() {
//
//                }
//            });
//            coverFlowAdapter.notifyDataSetChanged();
//            if(!imageUrlList.contains("no_images_null_view"))
//                imageUrlList.add("no_images_null_view");



//            currentPage = 0;
            for(int k=0;k<10;k++)
            imageUrlList.add(imageUrlList.get(k));

           

            final ImagePagerAdapter adapter = new ImagePagerAdapter(getActivity(), imageUrlList);
            sizeofData = imageUrlList.size();
            imagePager.setPageTransformer(true,new ZoomOutPageTransformer(true));
            imagePager.setAdapter(adapter);
            imagePager.setOnPageChangeListener(new CircularViewPagerHandler(imagePager));
//            tabLayout.setupWithViewPager(imagePager, true);
//            imagePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                @Override
//                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//                }
//
//                @Override
//                public void onPageSelected(int position) {
//                    currentpage = position;
//                }
//
//                @Override
//                public void onPageScrollStateChanged(int state) {
//                    if (state == ViewPager.SCROLL_STATE_IDLE) {
//                        int pageCount = sizeofData;
//
//                        /*if (currentpage == 0){
//                            imagePager.setCurrentItem(pageCount-1,false);
//                        } else*/ if (currentpage == pageCount-1){
//                            imagePager.setCurrentItem(1,false);
//                        }
//                    }
//                }
//            });

//            new CoverFlow.Builder()
//                    .with(imagePager)
//                    .pagerMargin(0.5f)
//                    .scale(0.3f)
//                    .spaceSize(0f)
//                    .rotationY(0f)
//                    .build();
            imagePager.setOffscreenPageLimit(6);
//           imagePager.setPageMargin(10);
            imagePager.setClipChildren(false);
            imagePager.setPageMargin(20);

//            imagePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                @Override
//                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                    if(position>adapter.getCount()-1){
//                        imagePager.setCurrentItem(1,false);
//                    }
//                }
//
//                @Override
//                public void onPageSelected(int position) {
//
//                }
//
//                @Override
//                public void onPageScrollStateChanged(int state) {
//                    if(imagePager.getCurrentItem()>adapter.getCount()-2){
//                        imagePager.setCurrentItem(1,false);
//                    }
//                }
//            });


//            final Handler handler = new Handler();
//            final Runnable Update = new Runnable() {
//                @Override
//                public void run() {
//                    if(currentPage== adapter.getCount()-1){
//                        currentPage = 0;
//                        timer.cancel();
//                    }
//                    imagePager.setCurrentItem(currentPage++,true);
//                }
//            };
//
//            timer =new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    handler.post(Update);
//                }
//            }, DELAY_MS, PERIOD_MS);



//             imagePager.setPageMargin(-64);

//            tabLayout.setupWithViewPager(imagePager, true);

        }
    }

    private FeatureCoverFlow.OnScrollPositionListener onScrollListener(){
        return new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                Log.v("MainActiivty", "position: " + position);

            }

            @Override
            public void onScrolling() {
                Log.i("MainActivity", "scrolling");
            }
        };
    }

    private void setChecked(View view){
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        for(int i=0; i < viewGroup.getChildCount(); i++){
            ImageButton childView = (ImageButton) viewGroup.getChildAt(i);
            childView.setImageDrawable(null);
        }

        ((ImageButton) view).setImageDrawable(getResources().getDrawable(R.drawable.ic_checked));
    }

    @Override
    public void startBrowser(String url) {

        Uri uri = Uri.parse("http://www.google.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse("http://www.google.com"));
                getActivity().startActivity(intent);
    }


    public interface OnInteractionListener {
        void onProductFetched(String title);
        void close();
    }



}
