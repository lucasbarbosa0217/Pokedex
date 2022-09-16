package br.com.primeit.pokedex.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.primeit.pokedex.model.PokemonSalvo;

@Dao
public interface PokemonSalvoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long salva (PokemonSalvo pokemon);

    @Query("SELECT * FROM pokemonsalvo")
    List<PokemonSalvo> todos();

    @Query("SELECT * FROM pokemonsalvo")
    List<PokemonSalvo> getPokemon();

    @Delete
    void remove(PokemonSalvo pokemon);

    @Update
    void edita (PokemonSalvo pokemon);

}
