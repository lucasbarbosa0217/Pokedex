package br.com.primeit.pokedex.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Objects;

import br.com.primeit.pokedex.R;
import br.com.primeit.pokedex.database.PokemonDatabase;
import br.com.primeit.pokedex.database.PokemonSalvoDao;
import br.com.primeit.pokedex.database.RecyclerView.PokemonSalvoAdapter;

public class SavedListPokemonActivity extends AppCompatActivity {
   private SavedListPokemonView  listaPokemonView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_list_pokemon_activity);
        setTheme(R.style.Theme_Splash);
        Objects.requireNonNull(getSupportActionBar()).hide();
        listaPokemonView = new SavedListPokemonView(this);
        configuraLista();
        listaPokemonView.atualizaPokemon();
    }

    private void configuraLista() {
        ListView listaViewPokemon = findViewById(R.id.saved_recycler);
        listaPokemonView.configuraAdapter(listaViewPokemon);
    }

}
