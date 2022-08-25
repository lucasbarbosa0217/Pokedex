package br.com.primeit.pokedex.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import br.com.primeit.pokedex.model.PokemonSalvo;

@Database(entities = {PokemonSalvo.class},version = 1, exportSchema = false)
public abstract class PokemonDatabase extends RoomDatabase {

    private static final String NOME_BANCO_DE_DADOS = "pokedex.db";
    public abstract PokemonSalvoDao getPokemonDao();

    public static PokemonDatabase getInstance(Context context){
        return (PokemonDatabase) Room.databaseBuilder(context, RoomDatabase.class, NOME_BANCO_DE_DADOS)
                .build();
    }
}
