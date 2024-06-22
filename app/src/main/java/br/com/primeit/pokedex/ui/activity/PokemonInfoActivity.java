package br.com.primeit.pokedex.ui.activity;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import br.com.primeit.pokedex.R;
import br.com.primeit.pokedex.model.PokemonDesc;
import br.com.primeit.pokedex.model.PokemonSalvo;
import br.com.primeit.pokedex.model.PokemonInfo;
import br.com.primeit.pokedex.retrofit.PokedexRetrofit;
import br.com.primeit.pokedex.retrofit.service.PokemonService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonInfoActivity extends AppCompatActivity {
    public PokemonInfo pokemonsInfos;
    private Context context;
    private SavedListPokemonView  listaPokemonView;
    public ProgressDialog dialog;
    public int numberPoke;
    private SwipeRefreshLayout swipeContainer;
    private String flavorText = "";
    public PokemonService service = new PokedexRetrofit().getPokemonService();
    String generoString = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        listaPokemonView = new SavedListPokemonView(this);
        setContentView(R.layout.pokemon_info_activity);


        Intent intent = getIntent();
        numberPoke = intent.getIntExtra("pokemon", 0);
        startLoadingScreen();
        super.onCreate(savedInstanceState);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeContainer.setRefreshing(true);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchDesc(numberPoke);

            }
        });



        Objects.requireNonNull(getSupportActionBar()).hide();
        searchDesc(numberPoke);

        FloatingActionButton saveMon = findViewById(R.id.salvar);
        saveMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setMessage("Saving Pokémon...");
                dialog.show();
                String tipos = "";
                tipos = (pokemonsInfos.getTypes().get(0).getName());
                if(pokemonsInfos.getTypes().size() > 1){
                    tipos = tipos + ", "+ (pokemonsInfos.getTypes().get(1).getName());
                }
                PokemonSalvo pokemonSalvo = new PokemonSalvo();
                pokemonSalvo.setNomePokemon(pokemonsInfos.getName());
                pokemonSalvo.setNumeroPokemon(numberPoke);
                pokemonSalvo.setAltura(pokemonsInfos.getHeight());
                pokemonSalvo.setPeso(pokemonsInfos.getWeight());
                pokemonSalvo.setAbilidades(pokemonsInfos.getAbilities().toString().replace("[", "").replace("]", ""));
                pokemonSalvo.setTipos(tipos);
                pokemonSalvo.setGenera(generoString);
                pokemonSalvo.setUrlFotoGrande("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/"+numberPoke+".png");
                pokemonSalvo.setUrlFotoPequena("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+numberPoke+".png");
                pokemonSalvo.setDesc(flavorText);
                Log.w("POKE", "onClick: "+pokemonSalvo.toString() );
                listaPokemonView.salvaMon(pokemonSalvo);
                ImageView teste= findViewById(R.id.PokeImageView);
                ImageView tinyImage = findViewById(R.id.tinyImage);
                teste.buildDrawingCache();
                tinyImage.buildDrawingCache();;
                Bitmap bitmap2 = tinyImage.getDrawingCache();
                Bitmap bitmap = teste.getDrawingCache();
                SaveImage(bitmap2, pokemonSalvo.getNumeroPokemon(), 1);
                SaveImage(bitmap, pokemonSalvo.getNumeroPokemon(),2);

            }
        });
    }


    private void SaveImage(Bitmap finalBitmap, int number, int type) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(getFilesDir(), "saved_pokemon");

        myDir.mkdirs();
        Log.w(TAG, "SaveImage: "+myDir.getAbsolutePath() );
        String fname = "";
        if(type==2){fname = "Image-"+ number +".png";}else{fname = "ImageTiny-"+ number +".png";}

        File file = new File (myDir, fname);
        if (file.exists ()) {
            dialog.hide();
            swipeContainer.setRefreshing(false);

            Toast.makeText(PokemonInfoActivity.this, "Pokemon Already Saved", Toast.LENGTH_SHORT).show();
            return;
        };
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            final Handler handler = new Handler();

                Toast.makeText(PokemonInfoActivity.this, "Pokémon Saved", Toast.LENGTH_SHORT).show();
                dialog.hide();
                swipeContainer.setRefreshing(false);


        }
    }

    private void startLoadingScreen() {
        dialog = new ProgressDialog(PokemonInfoActivity.this);
        dialog.setMessage("Loading data...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
    }

    public void searchDesc(int id){
        service = new PokedexRetrofit().getPokemonService();
        Call<PokemonDesc> descCall = service.searchDesc(id);
        descCall.enqueue(new Callback<PokemonDesc>() {
            @Override
            public void onResponse(@NonNull Call<PokemonDesc> call, @NonNull Response<PokemonDesc> response) {
                if(response.isSuccessful()){
                    PokemonDesc description = response.body();
                    setInfoDesc(description);
                    searchPokeInfo(numberPoke);

                }
                else{
                    Toast.makeText(PokemonInfoActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PokemonDesc> call, @NonNull Throwable t) {
                Toast.makeText(PokemonInfoActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setInfoDesc(PokemonDesc description) {
        PokemonDesc.genera genero = description.getFirstEnglishGenera();
        generoString = "The "+ genero.toString();
        TextView genus = findViewById(R.id.genusText);
        genus.setText(generoString);
        PokemonDesc.flavor_text flavor = description.getFirstEnglishEntry();
        TextView descText = findViewById(R.id.descText);
       flavorText = flavor.toString().replace("\n", " ");
        descText.setText(flavorText);

    }

    public void searchPokeInfo(int id){
        service = new PokedexRetrofit().getPokemonService();

        Call<PokemonInfo> pokeInfo = service.searchInfo(id);
        pokeInfo.enqueue(new Callback<PokemonInfo>() {
            @Override
            public void onResponse(@NonNull Call<PokemonInfo> call, @NonNull retrofit2.Response<PokemonInfo> response) {
                if(response.isSuccessful()){
                    PokemonInfo info = response.body();
                    pokemonsInfos= info;
                    assert pokemonsInfos != null;
                    setInfo(pokemonsInfos);
                }else{
                    Toast.makeText(PokemonInfoActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    finish();
                }
                dialog.hide();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<PokemonInfo> call, @NonNull Throwable t) {
                Toast.makeText(PokemonInfoActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                finish();
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        }

    @SuppressLint("SetTextI18n")
    public void setInfo(PokemonInfo info){
        setPokemonImage(numberPoke);
        setPokemonNumberField();
        //findViewById
        pokemonsInfos = info;
        TextView pokemonName = findViewById(R.id.PokemonInfoName);
        TextView pokemonHeight= findViewById(R.id.editaltura);
        TextView pokemonWeight= findViewById(R.id.editpeso);
        MaterialCardView fundo = findViewById(R.id.cardpokeinfo);
        TextView pokemonAbilities = findViewById(R.id.editaabilidades);
        CardView fundoTipo2 = findViewById(R.id.cardTipo2);
        TextView textTipo2 = findViewById(R.id.secondtype);
        TextView pokemonFirstType = findViewById(R.id.firsttype);
        CardView fundoTipo1 = findViewById(R.id.cardTipo1);

        //getListTipos
        List<PokemonInfo.Types> listTypes = info.getTypes();

        //getTipo1
        PokemonInfo.Types tipo1 = listTypes.get(0);
        String nomeTipo1 =tipo1.getName();

        //SetTipo1
        long colorTipo1 = chooseColor(nomeTipo1);
        fundoTipo1.setCardBackgroundColor((int) colorTipo1);
        fundo.setCardBackgroundColor((int) colorTipo1);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor((int) colorTipo1);


        pokemonFirstType.setText(nomeTipo1.substring(0,1).toUpperCase(Locale.ROOT) + nomeTipo1.substring(1));

        if(listTypes.size() > 1){
            //getTipo2
            PokemonInfo.Types tipo2 = listTypes.get(1);
            String nomeTipo2 = tipo2.getName();
            //setTipo2
            long colorTipo2 = chooseColor(nomeTipo2);
            fundoTipo2.setCardBackgroundColor((int) colorTipo2);
            textTipo2.setText(nomeTipo2.substring(0,1).toUpperCase(Locale.ROOT) + nomeTipo2.substring(1));

        }
        else{
            if(fundoTipo2 != null){
                ConstraintLayout pai = (ConstraintLayout) fundoTipo2.getParent();
                pai.removeView(fundoTipo2);
            }

            fundoTipo1.requestLayout();
            fundoTipo1.getLayoutParams().width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        }

        //SetAbilities
        pokemonAbilities.setText(info.getAbilities().toString().replace("[", "").replace("]", ""));

        //SetPesoEAltura
        double altura = (Double.parseDouble(info.getHeight()))/10;
        double peso = (Double.parseDouble(info.getWeight()))/10;
        pokemonHeight.setText(altura+"m");
        pokemonWeight.setText(peso+"kg");

        //SetNome
        pokemonName.setText(info.getName());

    }

    private void setPokemonNumberField() {
        TextView numberField = findViewById(R.id.pokemon_number_field);
        numberField.setText("#"+numberPoke);
    }

    private void setPokemonImage(int numberPoke) {
        ImageView pokemonImageField = findViewById(R.id.PokeImageView);
        ImageView tinyImage = findViewById(R.id.tinyImage);
        Glide.with(this)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/"+numberPoke+".png")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(pokemonImageField);

        Glide.with(this)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+numberPoke+".png")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(tinyImage);
    }

    private Long chooseColor(String nomeTipo) {
        long color;
        switch(nomeTipo){
            case "grass":color= ContextCompat.getColor(this, R.color.type_grass);break;
            case "fire":color= ContextCompat.getColor(this, R.color.type_fire);break;
            case "water":color= ContextCompat.getColor(this, R.color.type_water);break;
            case "electric":color= ContextCompat.getColor(this, R.color.type_electric);break;
            case "poison":color= ContextCompat.getColor(this, R.color.type_poison);break;
            case "flying":color= ContextCompat.getColor(this, R.color.type_flying);break;
            case "bug":color= ContextCompat.getColor(this, R.color.type_bug);break;
            case "psychic":color= ContextCompat.getColor(this, R.color.type_psychic);break;
            case "ghost":color= ContextCompat.getColor(this, R.color.type_ghost);break;
            case "steel":color= ContextCompat.getColor(this, R.color.type_steel);break;
            case "rock":color= ContextCompat.getColor(this, R.color.type_rock);break;
            case "fighting":color= ContextCompat.getColor(this, R.color.type_fight);break;
            case "fairy":color= ContextCompat.getColor(this, R.color.type_fairy);break;
            case "dragon":color= ContextCompat.getColor(this, R.color.type_dragon);break;
            case "ice":color= ContextCompat.getColor(this, R.color.type_ice);break;
            case "dark":color= ContextCompat.getColor(this, R.color.type_dark);break;
            case "normal":color= ContextCompat.getColor(this, R.color.type_normal);break;
            case "ground":color= ContextCompat.getColor(this, R.color.type_ground);break;
            default: color = R.color.red_pokedex;
        }

        return color;
    }


    public  boolean isStoragePermissionGranted() {
        String TAG = "perm";
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }


    @Override
    protected void onDestroy() {
        dialog.hide();
        super.onDestroy();
    }
}

