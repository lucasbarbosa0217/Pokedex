package br.com.primeit.pokedex.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import br.com.primeit.pokedex.R;

public class SavedListPokemonActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_list_pokemon_activity);
        setTheme(R.style.Theme_Splash);
        Objects.requireNonNull(getSupportActionBar()).hide();

    }

}
