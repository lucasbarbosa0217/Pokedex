package br.com.primeit.pokedex.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.compose.ui.graphics.Color;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;

import java.util.List;
import java.util.Locale;

import br.com.primeit.pokedex.R;
import br.com.primeit.pokedex.model.PokemonDesc;
import br.com.primeit.pokedex.model.PokemonDesc.genera;

import br.com.primeit.pokedex.model.info.PokemonInfo;
import br.com.primeit.pokedex.retrofit.PokedexRetrofit;
import br.com.primeit.pokedex.retrofit.service.PokemonService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonInfoActivity extends AppCompatActivity {
    public PokemonInfo pokemonsInfos;
    public ProgressDialog dialog;
    public int numberPoke;
    public PokemonService service = new PokedexRetrofit().getPokemonService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        Intent intent = getIntent();
        numberPoke = intent.getIntExtra("pokemon", 0);
        startLoadingScreen();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokemon_info_activity);
        getSupportActionBar().hide();
        searchDesc(numberPoke);
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
            public void onResponse(Call<PokemonDesc> call, Response<PokemonDesc> response) {
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
            public void onFailure(Call<PokemonDesc> call, Throwable t) {
                Toast.makeText(PokemonInfoActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setInfoDesc(PokemonDesc description) {
        List<PokemonDesc.genera> listGen = description.getGenera();
        PokemonDesc.genera genero = listGen.get(7);
        String generoString = "The "+ genero.toString();
        TextView genus = findViewById(R.id.genusText);
        genus.setText(generoString);

        List<PokemonDesc.flavor_text> listFlavor = description.getFlavor();
        PokemonDesc.flavor_text flavor = description.getFirstEnglishEntry();
        TextView descText = findViewById(R.id.descText);
        String flavorText = flavor.toString().replace("\n", " ");
        descText.setText(flavorText);

    }

    public void searchPokeInfo(int id){
        service = new PokedexRetrofit().getPokemonService();

        Call pokeInfo = service.searchInfo(id);
        pokeInfo.enqueue(new Callback<PokemonInfo>() {
            @Override
            public void onResponse(Call<PokemonInfo> call, retrofit2.Response<PokemonInfo> response) {
                if(response.isSuccessful()){
                    PokemonInfo info = response.body();
                    Log.w("ARRRG", "onResponse: "+response.body());
                    pokemonsInfos= info;
                    setInfo(pokemonsInfos);
                }else{
                    Toast.makeText(PokemonInfoActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    finish();
                }
                dialog.hide();
            }

            @Override
            public void onFailure(Call<PokemonInfo> call, Throwable t) {
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

    public void setInfo(PokemonInfo info){
        setPokemonImage(numberPoke);
        setPokemonNumberField(numberPoke);
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
            ConstraintLayout pai = (ConstraintLayout) fundoTipo2.getParent();
            pai.removeView(fundoTipo2);
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

    private void setPokemonNumberField(int numberPoke) {
    }

    private void setPokemonImage(int numberPoke) {
        ImageView pokemonImageField = findViewById(R.id.PokeImageView);
        Glide.with(this)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"+numberPoke+".png")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(pokemonImageField);
    }

    private Long chooseColor(String nomeTipo) {
        long color = 0;
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



}

