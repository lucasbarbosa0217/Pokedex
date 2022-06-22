package br.com.primeit.pokedex.ui.recyclerview.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.primeit.pokedex.R;
import br.com.primeit.pokedex.model.Pokemon;

public class ListPokemonAdapter extends Adapter<ListPokemonAdapter.ViewHolder>{

    public OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemClick(int position, int numberPoke);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    private final Context context;
    public static List<Pokemon> pokemonsList;

    public ListPokemonAdapter(Context context) {
        pokemonsList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCreated = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_list_item, parent, false);
        return new ViewHolder(viewCreated);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pokemon pokemon = pokemonsList.get(position);
        holder.vincula(pokemon);
    }

    @Override
    public int getItemCount() {
        return pokemonsList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void update(List<Pokemon> pokemons){
        pokemonsList.addAll(pokemons);
        notifyDataSetChanged();

    }
    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<Pokemon> filteredList) {
        pokemonsList=filteredList;
        notifyDataSetChanged();
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView pokemonNameField;
        private final TextView pokemonNumberField;
        private final ImageView pokemonImageField;
        private Pokemon pokemon;
        public ViewHolder(View itemView) {
            super(itemView);
            pokemonNameField = itemView.findViewById(R.id.pokemon_name);
            pokemonImageField = itemView.findViewById(R.id.pokemon_image);
            pokemonNumberField= itemView.findViewById(R.id.pokemon_number);
            itemView.setOnClickListener(view -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
               if(position != RecyclerView.NO_POSITION){
                   mListener.onItemClick(position, pokemon.getNumber());
               }}
            });
        }

        void vincula (Pokemon pokemon){
            this.pokemon = pokemon;
            String name = pokemon.getName();
            String nameCap = name.substring(0,1).toUpperCase(Locale.ROOT) + name.substring(1);
            pokemonNameField.setText(nameCap);
            Glide.with(context)
                    .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+pokemon.getNumber()+".png")
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(pokemonImageField);
            String numberPokeText= ""+pokemon.getNumber();
            pokemonNumberField.setText(numberPokeText);
        }
    }





}




