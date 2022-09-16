package br.com.primeit.pokedex.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Objects;

import br.com.primeit.pokedex.R;
import br.com.primeit.pokedex.database.RecyclerView.PokemonSalvoAdapter;
import br.com.primeit.pokedex.model.PokemonSalvo;

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
        configuraListenerDeCliquePorItem(listaViewPokemon);
        registerForContextMenu(listaViewPokemon);
    }

    private void configuraListenerDeCliquePorItem(ListView listaPokemonView) {
        listaPokemonView.setOnItemClickListener((adapterView, view, i, l) -> {
            PokemonSalvo pokemonSalvo = (PokemonSalvo) adapterView.getItemAtPosition(i);
            Intent intent = new Intent(SavedListPokemonActivity.this, SavedInfoActivity.class);

            intent.putExtra("pokemon", pokemonSalvo);

            startActivity(intent);
        });
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
         getMenuInflater().inflate(R.menu.menu_delete, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.deletemenu){
            listaPokemonView.allowDelete(item, this);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listaPokemonView.atualizaPokemon();
        configuraLista();
    }
}
