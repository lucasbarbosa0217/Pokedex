package br.com.primeit.pokedex.ui.activity;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import br.com.primeit.pokedex.R;
import br.com.primeit.pokedex.database.GetOnePokemonTask;
import br.com.primeit.pokedex.database.PokemonDatabase;
import br.com.primeit.pokedex.database.PokemonSalvoDao;
import br.com.primeit.pokedex.model.Pokemon;
import br.com.primeit.pokedex.model.PokemonSalvo;
import br.com.primeit.pokedex.model.info.PokemonInfo;

public class SavedInfoActivity  extends AppCompatActivity {
        private PokemonSalvoDao dao;
        private GetOnePokemonTask get;
    private PokemonSalvo pokemon;
    private SavedListPokemonView  listaPokemonView;


    @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.pokemon_saved_info_activity);

        super.onCreate(savedInstanceState);
            setTheme(R.style.Theme_Pokedex);
            Objects.requireNonNull(getSupportActionBar()).hide();
            Intent intent = getIntent();
     pokemon = (PokemonSalvo) getIntent().getSerializableExtra("pokemon");

            ConstraintLayout constraint = findViewById(R.id.pai);

            Log.w(TAG, "onCreate: "+pokemon );
            setInfo();
        FloatingActionButton fab = findViewById(R.id.salvar);
        listaPokemonView = new SavedListPokemonView(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              listaPokemonView.deleteMon(pokemon);
                Toast.makeText(SavedInfoActivity.this, "Pokémon Deleted", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        }




    @SuppressLint("SetTextI18n")
    public void setInfo(){
        setPokemonImage();
        setPokemonNumberField();
        TextView pokemonName = findViewById(R.id.PokemonInfoName);
        TextView pokemonHeight= findViewById(R.id.editaltura);
        TextView pokemonWeight= findViewById(R.id.editpeso);
        MaterialCardView fundo = findViewById(R.id.cardpokeinfo);
        TextView pokemonAbilities = findViewById(R.id.editaabilidades);
        CardView fundoTipo2 = findViewById(R.id.cardTipo2);
        TextView textTipo2 = findViewById(R.id.secondtype);
        TextView pokemonFirstType = findViewById(R.id.firsttype);
        CardView fundoTipo1 = findViewById(R.id.cardTipo1);
        TextView genus = findViewById(R.id.genusText);
        genus.setText(pokemon.getGenera());
        TextView descText = findViewById(R.id.descText);
        descText.setText(pokemon.getDesc());
        pokemonName.setText(pokemon.getNomePokemon());


        //getListTipos
        List<String> listTypes = Arrays.asList(pokemon.getTipos().split(","));
        //getTipo1
        String nomeTipo1 =listTypes.get(0);
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
            String nomeTipo2 = listTypes.get(1).trim();
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
        pokemonAbilities.setText(pokemon.getAbilidades());

        //SetPesoEAltura


        pokemonHeight.setText(""+Double.parseDouble(pokemon.getAltura())/10 +"m");
        pokemonWeight.setText(""+Double.parseDouble(pokemon.getPeso())/10 +"kg");

        //SetNome
        pokemonName.setText(pokemon.getNomePokemon());

    }

    private void setPokemonNumberField() {
        TextView numberField = findViewById(R.id.pokemon_number_field);
        numberField.setText("#"+pokemon.getNumeroPokemon());
    }

    private void setPokemonImage() {
        ImageView pokemonImageField = findViewById(R.id.PokeImageView);
        ImageView tinyImage = findViewById(R.id.tinyImage);
       File myDir = new File(getFilesDir(), "/saved_pokemon/Image-"+pokemon.getNumeroPokemon()+".png");
        File myDir2 = new File(getFilesDir(), "/saved_pokemon/ImageTiny-"+pokemon.getNumeroPokemon()+".png");

        Glide.with(this)
                .load(myDir)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(pokemonImageField);

        Glide.with(this)
                .load(myDir2)
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
}
